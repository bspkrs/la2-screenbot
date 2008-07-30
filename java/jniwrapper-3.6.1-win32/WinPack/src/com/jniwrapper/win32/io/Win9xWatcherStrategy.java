/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.io;

import com.jniwrapper.Bool;
import com.jniwrapper.Function;
import com.jniwrapper.Str;
import com.jniwrapper.UInt32;
import com.jniwrapper.win32.FunctionName;
import com.jniwrapper.win32.Handle;
import com.jniwrapper.win32.system.Kernel32;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

/**
 * Win9xWatcherStrategy class implements a watcher strategy based on wrapping
 * <code>FindFirstChangeNotification</code>, <code>FindNextChangeNotification</code>, etc.
 * Win API functions.
 * <p/>
 * Platforms: Win95, Win98, WinMe, WinNT, Win2000, WinXP
 *
 * @author Serge Piletsky
 */
public class Win9xWatcherStrategy extends WatcherStrategy implements Comparator
{
    static final FunctionName FUNCTION_FIND_FIRST_CHANGE_NOTIFICATION = new FunctionName("FindFirstChangeNotification");
    static final String FUNCTION_FIND_NEXT_CHANGE_NOTIFICATION = "FindNextChangeNotification";
    static final String FUNCTION_FIND_CLOSE_CHANGE_NOTIFICATION = "FindCloseChangeNotification";

    static final int INFINITE_TIMEOUT = 0xFFFFFFFF;

    static final int WAIT_ABANDONED = Handle.STATUS_ABANDONED_WAIT_0;
    static final int WAIT_OBJECT_0 = Handle.STATUS_WAIT_0;
    static final int WAIT_TIMEOUT = Handle.STATUS_TIMEOUT;

    private Handle _changeHandle = new Handle();
    private FolderInfo _folderInfo;
    private Thread _watcherThread;

    public Win9xWatcherStrategy(FileSystemWatcher fileSystemWatcher)
    {
        super(fileSystemWatcher);
    }

    public void start() throws FileSystemException
    {
        final FileSystemWatcher watcher = getFileSystemWatcher();

        File folder = new File(watcher.getPath());
        if (!folder.exists())
            throw new FileSystemException("Folder \"" + folder + "\" does not exist.");

        _folderInfo = new FolderInfo(watcher.getPath(), watcher.getFileFilter(), watcher.isWatchSubree());
        _folderInfo.load();
        final Function findFirstChangeNotification = Kernel32.getInstance().getFunction(FUNCTION_FIND_FIRST_CHANGE_NOTIFICATION.toString());
        int errorCode = (int)findFirstChangeNotification.invoke(_changeHandle,
                new Str(watcher.getPath()),
                new Bool(watcher.isWatchSubree()),
                new UInt32(watcher.getOptions().getFlags()));
        if (_changeHandle.getValue() == Handle.INVALID_HANDLE_VALUE)
        {
            throw new FileSystemException(errorCode);
        }

        _watcherThread = new Thread(new Runnable()
        {
            public void run()
            {
                setWatching(true);
                while (isWatching())
                {
                    long waitStatus = Handle.waitFor(_changeHandle);
                    switch ((int)waitStatus)
                    {
                        case WAIT_ABANDONED:
                        case WAIT_TIMEOUT:
                            setWatching(false);
                            break;
                        case WAIT_OBJECT_0:
                            {
                                FolderInfo newFolderInfo = new FolderInfo(watcher.getPath(), watcher.getFileFilter(), watcher.isWatchSubree());
                                newFolderInfo.load();
                                final Function findNextChangeNotification = Kernel32.getInstance().getFunction(FUNCTION_FIND_NEXT_CHANGE_NOTIFICATION);
                                Bool result = new Bool();
                                findNextChangeNotification.invoke(result, _changeHandle);
                                if (!result.getValue())
                                {
                                    setWatching(false);
                                    break;
                                }
                                final FileSystemEvent event = findDifference(_folderInfo, newFolderInfo);
                                if (event != null && isWatching())
                                {
                                    watcher.fireFileSystemEvent(event);
                                }
                                _folderInfo = newFolderInfo;
                                break;
                            }
                        default:
                            setWatching(false);
                            break;
                    }
                }
            }
        });
        _watcherThread.start();
    }

    public void stop() throws FileSystemException
    {
        setWatching(false);
        final Kernel32 kernel32 = Kernel32.getInstance();
        final Function findCloseChangeNotification = kernel32.getFunction(FUNCTION_FIND_CLOSE_CHANGE_NOTIFICATION);
        Bool result = new Bool();
        int errorCode = (int)findCloseChangeNotification.invoke(result, _changeHandle);
        if (!result.getValue())
        {
            throw new FileSystemException(errorCode);
        }
    }

    private FileInfo findChanged(FolderInfo oldFolderInfo, FolderInfo newFolderInfo)
    {
        Collections.sort(oldFolderInfo.getFiles(), this);
        Collections.sort(newFolderInfo.getFiles(), this);
        FileInfo result = null;
        for (Iterator i = oldFolderInfo.getFiles().iterator(); i.hasNext();)
        {
            final Object element = i.next();
            int index = Collections.binarySearch(newFolderInfo.getFiles(), element, this);
            if (index >= 0)
            {
                if (element instanceof FolderInfo)
                {
                    final FolderInfo oldSubfolder = (FolderInfo)element;
                    final FolderInfo newFolder = (FolderInfo)newFolderInfo.getFiles().get(index);
                    result = findChanged(oldSubfolder, newFolder);
                    if (result != null)
                        break;
                }
            }
            else
            {
                result = (FileInfo)element;
                break;
            }
        }
        return result;
    }

    private FileSystemEvent findDifference(FolderInfo oldFolder, FolderInfo newFolder)
    {
        FileSystemEvent result = null;
        int fileAction = -1;
        int oldFolderSize = oldFolder.getFileCount();
        int newFolderSize = newFolder.getFileCount();
        if (oldFolderSize == newFolderSize)
        {
            fileAction = FileSystemEvent.FILE_MODIFIED;
        }
        else if (oldFolderSize < newFolderSize)
        {
            fileAction = FileSystemEvent.FILE_ADDED;
        }
        else if (oldFolderSize > newFolderSize)
        {
            fileAction = FileSystemEvent.FILE_REMOVED;
        }
        switch (fileAction)
        {
            case FileSystemEvent.FILE_MODIFIED:
                {
                    FileInfo fileInfo1 = findChanged(newFolder, oldFolder);
                    FileInfo fileInfo2 = findChanged(oldFolder, newFolder);
                    if (fileInfo1 != null && fileInfo2 != null && !fileInfo1.getFileName().equals(fileInfo2.getFileName()))
                    {
                        result = new FileSystemEvent(newFolder, FileSystemEvent.FILE_RENAMED, fileInfo2, fileInfo1);
                    }
                    else if (fileInfo1 != null)
                    {
                        result = new FileSystemEvent(newFolder, fileAction, fileInfo1);
                    }
                    break;
                }
            case FileSystemEvent.FILE_ADDED:
                {
                    FileInfo fileInfo = findChanged(newFolder, oldFolder);
                    if (fileInfo != null)
                    {
                        result = new FileSystemEvent(newFolder, fileAction, fileInfo);
                    }
                    break;
                }
            case FileSystemEvent.FILE_REMOVED:
                {
                    FileInfo fileInfo = findChanged(oldFolder, newFolder);
                    if (fileInfo != null)
                    {
                        result = new FileSystemEvent(oldFolder, fileAction, fileInfo);
                    }
                }
        }
        return result;
    }

    public int compare(Object o1, Object o2)
    {
        FileInfo fileInfo1 = (FileInfo)o1;
        FileInfo fileInfo2 = (FileInfo)o2;

        final FileSystemWatcher.WatcherOptions options = getFileSystemWatcher().getOptions();
        int result = fileInfo1.getFileName().compareTo(fileInfo2.getFileName());
        if (result == 0 && options.isNotifyChangeAttributes())
        {
            result = (int)fileInfo1.getAttributes() - (int)fileInfo2.getAttributes();
        }
        if (result == 0 && options.isNotifyChangeSize())
        {
            result = (int)fileInfo1.getSize() - (int)fileInfo2.getSize();
        }
        if (result == 0 && options.isNotifyLastModified() &&
                !(fileInfo1 instanceof FolderInfo) &&
                !(fileInfo2 instanceof FolderInfo))
        {
            result = (int)fileInfo1.getLastModified() - (int)fileInfo2.getLastModified();
        }
        return result;
    }
}

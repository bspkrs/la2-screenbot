/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.io;

import com.jniwrapper.*;
import com.jniwrapper.util.Logger;
import com.jniwrapper.win32.FunctionName;
import com.jniwrapper.win32.Handle;
import com.jniwrapper.win32.system.Kernel32;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * WinNTWatcherStrategy class implements a watcher strategy based on wrapping
 * <code>ReadDirectoryChangesW</code> Win API function.
 * <p/>
 * Platforms: WinNT (SP3 or later), Win2000, WinXP
 *
 * @author Serge Piletsky
 */

public class WinNTWatcherStrategy extends WatcherStrategy
{
    private static final Logger LOG = Logger.getInstance(WinNTWatcherStrategy.class);

    static final FunctionName FUNCTION_CREATE_FILE = new FunctionName("CreateFile");
    static final String FUNCTION_READ_DIRECTORY_CHANGES = "ReadDirectoryChangesW";

    static final int FILE_LIST_DIRECTORY = 0x001;
    static final int FILE_DELETE_CHILD = 0x0040;
    static final int FILE_ACCESS_MODE = FILE_LIST_DIRECTORY;// | FILE_DELETE_CHILD;
    static final int FILE_SHARE_READ = 0x00000001;
    static final int FILE_SHARE_WRITE = 0x00000002;
    static final int FILE_SHARE_DELETE = 0x00000004;
    static final int OPEN_EXISTING = 3;
    static final int FILE_FLAG_BACKUP_SEMANTICS = 0x02000000;

    int _bufferSize = 1024 * 64;
    private Handle _directory = new Handle();
    private Thread _watcherThread;

    public WinNTWatcherStrategy(FileSystemWatcher fileSystemWatcher)
    {
        super(fileSystemWatcher);
    }

    public void start() throws FileSystemException
    {
        final FileSystemWatcher watcher = getFileSystemWatcher();

        File folder = new File(watcher.getPath());
        if (!folder.exists())
            throw new FileSystemException("Folder \"" + folder + "\" does not exist.");

        final Kernel32 kernel32 = Kernel32.getInstance();
        String path = watcher.getPath();
        final Function functionCreateFile = kernel32.getFunction(FUNCTION_CREATE_FILE.toString());
        functionCreateFile.invoke(_directory, new Parameter[]
        {
            new Str(path),
            new UInt32(FILE_ACCESS_MODE),
            new UInt32(FILE_SHARE_DELETE | FILE_SHARE_READ | FILE_SHARE_WRITE),
            new Pointer(null, true),
            new UInt32(OPEN_EXISTING),
            new UInt32(FILE_FLAG_BACKUP_SEMANTICS),
            new Pointer(null, true)
        });
        final long handleValue = _directory.getValue();

        if (_directory.isNull() || handleValue == Handle.INVALID_HANDLE_VALUE)
            throw new FileSystemException("Could not open folder " + folder);

        final long bufferSize = getBufferSize();

        final PrimitiveArray buffer = new PrimitiveArray(UInt8.class, _bufferSize);
        final boolean watchSubtree = watcher.isWatchSubree();
        final int notifyFilter = (int)watcher.getOptions().getFlags();
        final UInt32 bytesReturned = new UInt32();

        _watcherThread = new Thread(new Runnable()
        {
            public void run()
            {
                setWatching(true);
                while (isWatching())
                {
                    final Function functionReadDirectoryChanges = kernel32.getFunction(FUNCTION_READ_DIRECTORY_CHANGES);
                    Int returnValue = new Int();
                    functionReadDirectoryChanges.invoke(returnValue, new Parameter[]
                    {
                        _directory,
                        new Pointer(buffer),
                        new UInt32(bufferSize),
                        new Bool(watchSubtree),
                        new UInt32(notifyFilter),
                        new Pointer(bytesReturned),
                        new Pointer(null, true), //null pointer to an overlapped structure
                        new Pointer(null, true)  //null pointer to completion callback
                    });
                    final long value = returnValue.getValue();

                    if (value != 0 && isWatching())
                    {
                        fireEvents(retrieveEvents(buffer.getBytes()));
                    }
                    else
                    {
                        notifyIfWatchedFolderRemoved();
                        unlockWatchedFolder();
                        break;
                    }
                }
            }
        });
        _watcherThread.start();
    }

    /**
     * Sets a new buffer size in bytes. <br>
     * Property <code>BufferSize</code> determinates a number of bytes,
     * that will be allocated for <code>ReadDirectoryChangesW</code> function to retrieve
     * file system evevents. By default this buffer is allocated with 100 Kb.
     *
     * @param value is a new buffer size
     */
    public void setBufferSize(int value)
    {
        _bufferSize = value;
    }

    /**
     * Returns the buffer size in bytes.
     *
     * @return a buffer size in bytes
     */
    public int getBufferSize()
    {
        return _bufferSize;
    }

    class FileActionInfo
    {
        private FileInfo _fileInfo;
        private int _action;

        public FileActionInfo(FileInfo fileInfo, int action)
        {
            _fileInfo = fileInfo;
            _action = action;
        }

        public FileInfo getFileInfo()
        {
            return _fileInfo;
        }

        public int getAction()
        {
            return _action;
        }
    }

    /**
     * Parses a passed buffer according to FILE_NOTIFY_INFORMATION structure.
     *
     * @param buffer
     */
    private List retrieveEvents(byte[] buffer)
    {
        final FileSystemWatcher watcher = getFileSystemWatcher();
        int index = 0;
        int nextEntryIndex = 0;
        boolean lastEvent = false;
        List actionInfos = new ArrayList();
        while (index < _bufferSize && !lastEvent)
        {
            // NextEntryOffset
            int nextEntryOffset = readDWORD(buffer, index);
            nextEntryIndex += nextEntryOffset;
            index += 4;
            lastEvent = nextEntryOffset == 0;
            // Action
            int action = readDWORD(buffer, index);
            index += 4;
            // FileNameLength
            int fileNameLength = readDWORD(buffer, index);
            index += 4;
            // FileName
            StringBuffer fileName = new StringBuffer(watcher.getPath());
            fileName.append(File.separatorChar);
            for (int i = 0; i < fileNameLength >> 1; i++)
            {
                char c = readWCHAR(buffer, index);
                index += 2;
                fileName.append(c);
            }
            actionInfos.add(new FileActionInfo(new FileInfo(fileName.toString(), 0, 0, 0), action));
            index = nextEntryIndex;
        }

        final int actionInfoCount = actionInfos.size();
        List events = new ArrayList(actionInfoCount);
        for (int i = 0; i < actionInfoCount; i++)
        {
            FileSystemEvent event = null;
            FileActionInfo actionInfo = (FileActionInfo)actionInfos.get(i);
            if (actionInfo.getAction() == FileSystemEvent.FILE_RENAMED)
            {
                if (i + 1 < actionInfoCount)
                {
                    FileActionInfo newActionInfo = (FileActionInfo)actionInfos.get(++i);
                    event = new FileSystemEvent(watcher, actionInfo.getAction(), actionInfo.getFileInfo(), newActionInfo.getFileInfo());
                }
            }
            else
            {
                event = new FileSystemEvent(watcher, actionInfo.getAction(), actionInfo.getFileInfo());
            }
            events.add(event);
        }
        return events;
    }

    private void fireEvents(List events)
    {
        final FileSystemWatcher watcher = getFileSystemWatcher();
        final FileFilter fileFilter = watcher.getFileFilter();
        for (Iterator i = events.iterator(); i.hasNext();)
        {
            FileSystemEvent event = (FileSystemEvent)i.next();
            if (fileFilter != null)
            {
                final File file1 = new File(event.getFileInfo().getFileName());
                File file2 = null;
                if (event.getOldFileInfo() != null)
                {
                    file2 = new File(event.getOldFileInfo().getFileName());
                }

                if (fileFilter.accept(file1) || (file2 != null && fileFilter.accept(file2)))
                {
                    watcher.fireFileSystemEvent(event);
                }
            }
            else
            {
                watcher.fireFileSystemEvent(event);
            }
        }
    }

    private int readDWORD(byte[] buffer, int offset)
    {
        UInt32 result = new UInt32();
        result.read(buffer, offset);
        return (int)result.getValue();
    }

    private char readWCHAR(byte[] buffer, int offset)
    {
        WideChar result = new WideChar();
        result.read(buffer, offset);
        return result.getValue();
    }

    public void stop() throws FileSystemException
    {
        setWatching(false);
        awakeWatcher();
        while (_watcherThread.isAlive())
            try
            {
                Thread.sleep(1);
            }
            catch (InterruptedException e)
            {
                LOG.error("", e);
            }
        unlockWatchedFolder();
    }

    // fire dummy event to awake watcher thread
    private void awakeWatcher()
    {
        try
        {
            File watchedFolder = new File(getFileSystemWatcher().getPath());
            if (watchedFolder.exists())
            {
                File dummyFile = new File(watchedFolder, "notify");
                dummyFile.createNewFile();
                dummyFile.delete();
            }
        }
        catch (IOException e)
        {
            LOG.error("", e);
        }
    }

    private void notifyIfWatchedFolderRemoved()
    {
        File watchedFolder = new File(getFileSystemWatcher().getPath());
        if (!watchedFolder.exists())
        {
            FileSystemEvent event = new FileSystemEvent(getFileSystemWatcher(),
                    FileSystemEvent.FILE_REMOVED,
                    new FileInfo(watchedFolder.getAbsolutePath(), 0, 0, 0));
            List events = new ArrayList();
            events.add(event);
            fireEvents(events);
        }
    }

    // closes handle to wathcing folder
    private void unlockWatchedFolder()
    {
        if (_directory.getValue() != Handle.INVALID_HANDLE_VALUE)
        {
            Handle.closeHandle(_directory);
        }
    }
}

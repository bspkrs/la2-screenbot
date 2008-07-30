/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.io;

import com.jniwrapper.util.FlagSet;
import com.jniwrapper.util.Logger;
import com.jniwrapper.win32.system.VersionInfo;

import java.io.FileFilter;
import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * FileSystemWatcher class is designed to listen for file system events
 * in a specified folder with various watching parameters.
 * <p/>
 * It uses different watching strategies depending on OS.
 * For NT systems, it automatically sets up {@link WinNTWatcherStrategy} and
 * for Win9x, it automatically sets up {@link Win9xWatcherStrategy}. Also, it
 * can use other strategies derived from {@link WatcherStrategy}.
 *
 * @author Serge Piletsky
 */
public class FileSystemWatcher
{
    private static final Logger LOG = Logger.getInstance(FileSystemWatcher.class);
    
    private String _path;
    private FileFilter _fileFilter;
    private WatcherOptions _notifyOptions = new WatcherOptions();
    private boolean _watchSubree = false;
    private List _fileSystemListeners = new LinkedList();
    private WatcherStrategy _watcherStrategy;

    /**
     * Creates a new instance to watch for modifications in a given folder.
     * Sub-directories of the specified folder are not monitored by default.
     *
     * @param path a folder to watch
     */
    public FileSystemWatcher(String path)
    {
        _path = path;

        VersionInfo versionInfo = new VersionInfo();
        if (versionInfo.isNT())
        {
            setStrategy(WinNTWatcherStrategy.class);
        }
        else
        {
            setStrategy(Win9xWatcherStrategy.class);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable()
        {
            public void run()
            {
                if (isWatching())
                {
                    try
                    {
                        stop();
                    }
                    catch (FileSystemException e)
                    {
                        LOG.error("", e);
                    }
                }
            }
        }, "FileSystemWatcher.stop"));
    }

    /**
     * Creates a new instance to watch for modifications in a given folder.
     *
     * @param path        a folder to watch.
     * @param fileFilter  specifies the files or folders to be watched.
     * @param watchSubree tells the watcher whether or not to watch the folder's subtree.
     */
    public FileSystemWatcher(String path, FileFilter fileFilter, boolean watchSubree)
    {
        this(path, watchSubree);
        _fileFilter = fileFilter;
    }

    /**
     * Creates a new instance to watch for modifications in a given folder.
     *
     * @param path       the folder to watch.
     * @param fileFilter specifies the files or folders to be watched.
     */
    public FileSystemWatcher(String path, FileFilter fileFilter)
    {
        this(path);
        _fileFilter = fileFilter;
    }

    /**
     * Creates a new watcher to watch for modifications in a given path.
     *
     * @param path        the folder to watch.
     * @param watchSubree if true, sub-directories will also be monitored. 
     */
    public FileSystemWatcher(String path, boolean watchSubree)
    {
        this(path);
        _watchSubree = watchSubree;
    }

    /**
     * Sets up a custom watching strategy.
     *
     * @param strategyClass a custom strategy class derived from {@link WatcherStrategy}
     */
    public void setStrategy(Class strategyClass)
    {
        if (strategyClass == null)
            throw new NullPointerException();
        if (_watcherStrategy != null)
        {
            if (strategyClass.equals(_watcherStrategy.getClass()))
                return;
            if (_watcherStrategy.isWatching())
                throw new IllegalStateException("Unable to change stragetgy while watcher is running.");
        }
        if (!WatcherStrategy.class.isAssignableFrom(strategyClass))
            throw new IllegalArgumentException("Stategy class must be derived from WatcherStrategy class.");
        _watcherStrategy = createStrategy(strategyClass);
    }

    /**
     * Creates an instance of the watching strategy from the specified class.
     *
     * @param strategyClass a custom strategy class derived from {@link WatcherStrategy}
     * @return an instance of the strategy.
     */
    protected WatcherStrategy createStrategy(Class strategyClass)
    {
        WatcherStrategy result = null;
        try
        {
            final Constructor constructor = strategyClass.getConstructor(new Class[]{FileSystemWatcher.class});
            result = (WatcherStrategy)constructor.newInstance(new Object[]{this});
        }
        catch (Exception e)
        {
            LOG.error("", e);
        }
        return result;
    }

    /**
     * @return watched folder path
     */
    public String getPath()
    {
        return _path;
    }

    /**
     * @return file filter
     */
    public FileFilter getFileFilter()
    {
        return _fileFilter;
    }

    /**
     * @return true if the watcher is configured to watch the subtree, otherwise false.
     */
    public boolean isWatchSubree()
    {
        return _watchSubree;
    }

    /**
     * @return {@link WatcherOptions} that contains a set of notify filters.
     */
    public WatcherOptions getOptions()
    {
        return _notifyOptions;
    }

    /**
     * Adds {@link FileSystemEventListener}
     *
     * @param listener
     */
    public void addFileSystemListener(FileSystemEventListener listener)
    {
        if (!_fileSystemListeners.contains(listener))
            _fileSystemListeners.add(listener);
    }

    /**
     * Removes {@link FileSystemEventListener}
     *
     * @param listener
     */
    public void removeFileSystemListener(FileSystemEventListener listener)
    {
        _fileSystemListeners.remove(listener);
    }

    protected void fireFileSystemEvent(FileSystemEvent event)
    {
        List listeners = null;
        synchronized (this)
        {
            listeners = new LinkedList(_fileSystemListeners);
            for (Iterator i = listeners.iterator(); i.hasNext();)
            {
                FileSystemEventListener listener = (FileSystemEventListener)i.next();
                listener.handle(event);
            }
        }
    }

    /**
     * Starts watching.
     *
     * @throws FileSystemException
     */
    public void start() throws FileSystemException
    {
        _watcherStrategy.start();
    }

    /**
     * Stops watching.
     *
     * @throws FileSystemException
     */
    public void stop() throws FileSystemException
    {
        _watcherStrategy.stop();
    }

    /**
     * @return true if the watcher is watching a specified folder, otherwise false.
     */
    public boolean isWatching()
    {
        return _watcherStrategy.isWatching();
    }

    /**
     * WatcherOptions class determines a set of properties to configure 
     * the behavior of <code>FileSystemWatcher</code>.
     */
    public class WatcherOptions extends FlagSet
    {
        public static final int NOTIFY_CHANGE_FILE_NAME = 0x00000001;
        public static final int NOTIFY_CHANGE_DIR_NAME = 0x00000002;
        public static final int NOTIFY_CHANGE_ATTRIBUTES = 0x00000004;
        public static final int NOTIFY_CHANGE_SIZE = 0x00000008;
        public static final int NOTIFY_CHANGE_LAST_WRITE = 0x00000010;
        public static final int NOTIFY_CHANGE_LAST_ACCESS = 0x00000020;
        public static final int NOTIFY_CHANGE_CREATION = 0x00000040;
        public static final int NOTIFY_CHANGE_SECURITY = 0x00000100;

        public WatcherOptions()
        {
            super();
            reset();
        }

        public void reset()
        {
            clear();
            add(NOTIFY_CHANGE_FILE_NAME | NOTIFY_CHANGE_DIR_NAME | NOTIFY_CHANGE_ATTRIBUTES | NOTIFY_CHANGE_SIZE | NOTIFY_CHANGE_LAST_WRITE);
        }

        public boolean isNotifyChangeFileName()
        {
            return contains(NOTIFY_CHANGE_FILE_NAME);
        }

        public void setNotifyChangeFileName(boolean value)
        {
            setupFlag(NOTIFY_CHANGE_FILE_NAME, value);
        }

        public boolean isNotifyChangeDirName()
        {
            return contains(NOTIFY_CHANGE_DIR_NAME);
        }

        public void setNotifyChangeDirName(boolean value)
        {
            setupFlag(NOTIFY_CHANGE_DIR_NAME, value);
        }

        public boolean isNotifyChangeAttributes()
        {
            return contains(NOTIFY_CHANGE_ATTRIBUTES);
        }

        public void setNotifyChangeAttributes(boolean value)
        {
            setupFlag(NOTIFY_CHANGE_ATTRIBUTES, value);
        }

        public boolean isNotifyChangeSize()
        {
            return contains(NOTIFY_CHANGE_SIZE);
        }

        public void setNotifyChangeSize(boolean value)
        {
            setupFlag(NOTIFY_CHANGE_SIZE, value);
        }

        public boolean isNotifyLastModified()
        {
            return contains(NOTIFY_CHANGE_LAST_WRITE);
        }

        public void setNotifyLastModified(boolean value)
        {
            setupFlag(NOTIFY_CHANGE_LAST_WRITE, value);
        }

        public boolean isNotifyLastAccess()
        {
            return contains(NOTIFY_CHANGE_LAST_ACCESS);
        }

        public void setNotifyLastAccess(boolean value)
        {
            setupFlag(NOTIFY_CHANGE_LAST_ACCESS, value);
        }
    }
}

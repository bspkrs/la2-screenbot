/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.io;

/**
 * WatcherStrategy class is a base class for various watching strategies used in
 * {@link FileSystemWatcher} class.
 * 
 * @author Serge Piletsky
 */
public abstract class WatcherStrategy
{
    private FileSystemWatcher _fileSystemWatcher;
    private boolean _watching = false;

    /**
     * Creates a strategy for a specified watcher {@link FileSystemWatcher}
     * 
     * @param fileSystemWatcher a wacher which will use this strategy.
     */
    public WatcherStrategy(FileSystemWatcher fileSystemWatcher)
    {
        _fileSystemWatcher = fileSystemWatcher;
    }

    /**
     * 
     * @return a watcher.
     */
    public FileSystemWatcher getFileSystemWatcher()
    {
        return _fileSystemWatcher;
    }

    protected boolean isWatching()
    {
        return _watching;
    }

    protected void setWatching(boolean value)
    {
        _watching = value;
    }

    /**
     * Starts watching.
     * 
     * @exception FileSystemException
     */
    public abstract void start() throws FileSystemException;

    /**
     * Stops watching.
     * 
     * @exception FileSystemException
     */
    public abstract void stop() throws FileSystemException;
}

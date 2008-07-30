/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.io;

import java.util.EventObject;

/**
 * This class provides information about an event occurred in the file system.
 * 
 * @author Serge Piletsky
 * @see FileSystemWatcher
 */
public class FileSystemEvent extends EventObject
{
    public static final int FILE_ADDED = 1;
    public static final int FILE_REMOVED = 2;
    public static final int FILE_MODIFIED = 3;
    public static final int FILE_RENAMED = 4;

    private int _action;
    private FileInfo _oldFileInfo;
    private FileInfo _fileInfo;

    public FileSystemEvent(Object source, int action, FileInfo oldFileInfo, FileInfo fileInfo)
    {
        super(source);
        _action = action;
        _oldFileInfo = oldFileInfo;
        _fileInfo = fileInfo;
    }

    public FileSystemEvent(Object source, int action, FileInfo fileInfo)
    {
        this(source, action, null, fileInfo);
    }

    /**
     * 
     * @return action type occurred in the file system.
     */
    public int getAction()
    {
        return _action;
    }

    /**
     * 
     * @return previous file information of the last modified file.
     */
    public FileInfo getOldFileInfo()
    {
        return _oldFileInfo;
    }

    /**
     * 
     * @return file information of the last created/modified/deleted file.
     */
    public FileInfo getFileInfo()
    {
        return _fileInfo;
    }

    public String toString()
    {
        StringBuffer result = new StringBuffer("FileSystemEvent: [");
        result.append("action = ").append(_action).
                append("; oldFileInfo = ").append(_oldFileInfo).
                append("; fileInfo = ").append(_fileInfo).append(']');
        return result.toString();
    }
}

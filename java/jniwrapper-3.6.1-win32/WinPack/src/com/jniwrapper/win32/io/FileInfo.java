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
 * FileInfo class is a file information container.
 * 
 * @author Serge Piletsky
 */
public class FileInfo
{
    private String _fileName;
    private long _attributes;
    private long _size;
    private long _lastModified;

    public FileInfo(String fileName, long attributes, long size, long lastModified)
    {
        _fileName = fileName;
        _attributes = attributes;
        _size = size;
        _lastModified = lastModified;
    }

    public FileInfo(String fileName)
    {
        _fileName = fileName;
    }

    public String getFileName()
    {
        return _fileName;
    }

    public long getAttributes()
    {
        return _attributes;
    }

    public long getSize()
    {
        return _size;
    }

    public long getLastModified()
    {
        return _lastModified;
    }

    public String toString()
    {
        return getFileName();
    }
}

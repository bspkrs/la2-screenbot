/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.io;

import com.jniwrapper.win32.LastError;

/**
 * FileSystemException class is an exception that can be thrown by
 * FileSystemWatcher at startup or during watching file system events.
 * 
 * @author Serge Piletsky
 */
public class FileSystemException extends RuntimeException
{
    /**
     * @deprecated Use another constructor {@link #FileSystemException(int)} instead.
     */
    public FileSystemException()
    {
        super(LastError.getMessage());
    }

    public FileSystemException(int errorCode)
    {
        super(LastError.getMessage(errorCode));
    }

    public FileSystemException(String s)
    {
        super(s);
    }
}

/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.registry;

import com.jniwrapper.win32.LastError;

/**
 * RegistryException is an exception that may be thrown during work with windows
 * registry using RegistryKey.
 * 
 * @author Renat Yanbekov
 */
public class RegistryException extends RuntimeException
{
    private int _errorCode = 0;

    public RegistryException()
    {
    }

    public RegistryException(String s)
    {
        super(s);
    }

    public RegistryException(int errorCode)
    {
        _errorCode = errorCode;
    }

    public RegistryException(String s, int errorCode)
    {
        super(s);
        _errorCode = errorCode;
    }

    public RegistryException(Throwable cause)
    {
        super(cause.getMessage());
    }

    public int getErrorCode()
    {
        return _errorCode;
    }

    public String getMessage()
    {
        String message = super.getMessage();
        if (message == null)
            return LastError.getMessage(_errorCode);
        else
            return message;
    }
}

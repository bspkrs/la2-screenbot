/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.service;

/**
 * Generic exception for service-related operations.
 * 
 * @author Alexander Evsukov
 */
public class ServiceException extends RuntimeException
{
    public ServiceException(String message)
    {
        super(message);
    }

    public ServiceException(Throwable cause)
    {
        super(cause);
    }

    public ServiceException(String message, Throwable cause)
    {
        super(message, cause);
    }
}

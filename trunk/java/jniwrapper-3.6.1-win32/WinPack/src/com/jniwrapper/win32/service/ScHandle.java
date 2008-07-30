/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.service;

import com.jniwrapper.Bool;
import com.jniwrapper.Function;
import com.jniwrapper.win32.Handle;
import com.jniwrapper.win32.system.AdvApi32;

/**
 * Handle to a service related object (service manager, service).
 * 
 * @author Alexei Orischenko
 */
abstract class ScHandle extends Handle
{
    private boolean _isOpened = false;
    private static final String FUNCTION_CLOSE_SERVICE_HANDLE = "CloseServiceHandle";
    private boolean _isLocked = false;

    protected void setOpened(boolean isOpened)
    {
        _isOpened = isOpened;
    }

    protected boolean isOpened()
    {
        return _isOpened;
    }

    /**
     * Locks the handle for closing.
     */
    protected void lock()
    {
        _isLocked = true;
    }

    /**
     * Unlocks the handle for closing.
     */
    protected void unlock()
    {
        _isLocked = false;
    }

    /**
     * 
     * @return <code>true</code>, if the handle is locked for closing.
     */
    protected boolean isLocked()
    {
        return _isLocked;
    }

    /**
     * 
     * @exception IllegalStateException if the handle is closed.
     */
    protected void checkOpened() throws IllegalStateException
    {
        if (!_isOpened)
        {
            throw new IllegalStateException(
                    "The handle is not opened. Please call " + getClass().getName() + ".open() first.");
        }
    }

    /**
     * Closes the handle to a service-related object.
     * 
     * @return true, if succeeds; otherwise false.
     */
    public boolean close()
    {
        if (isLocked())
        {
            return false;
        }

        if (!isOpened())
        {
            return true;
        }

        Function function = AdvApi32.getInstance().getFunction(FUNCTION_CLOSE_SERVICE_HANDLE);

        Bool result = new Bool();
        function.invoke(result, this);

        if (result.getValue())
        {
            setOpened(false);
        }

        return result.getValue();
    }

    /**
     * A utility method for checking an operation result.
     * 
     * @param msg error message for the exception to be thrown.
     * @param result boolean value to verify.
     * @exception ServiceException if the result is <code>false</code>.
     */
    protected void checkResult(String msg, Bool result)
    {
        if (!result.getValue())
        {
            throw new ServiceException(msg);
        }
    }
}

/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32;

import com.jniwrapper.Bool;
import com.jniwrapper.Function;
import com.jniwrapper.Pointer;
import com.jniwrapper.UInt32;
import com.jniwrapper.win32.system.Kernel32;

/**
 * A base class for all handle types.
 *
 * @author Serge Piletsky
 */
public class Handle extends Pointer.Void
{
    private static final String FUNCTION_CloseHandle = "CloseHandle";
    private static final String FUNCTION_WaitForSingleObject = "WaitForSingleObject";

    /**
     * Specifies the infinite timeout value for {@link Handle#waitFor(Handle, long)} function.
     */
    public static final int INFINITE_TIMEOUT = 0xFFFFFFFF;

    /**
     * Specifies the status value of {@link Handle#waitFor(Handle, long)} or {@link Handle#waitFor(Handle)} functions.
     */
    public static final int STATUS_WAIT_0 = 0x00000000;
    /**
     * Specifies the abandoned status value of {@link Handle#waitFor(Handle, long)} or {@link Handle#waitFor(Handle)} functions.
     */
    public static final int STATUS_ABANDONED_WAIT_0 = 0x00000080;
    /**
     * Specifies the timeout status value of {@link Handle#waitFor(Handle, long)} or {@link Handle#waitFor(Handle)} functions.
     */
    public static final int STATUS_TIMEOUT = 0x00000102;

    /**
     * Specifies invalid handle value.
     */
    public static final int INVALID_HANDLE_VALUE = -1;

    /**
     * Constucts a blank handle instance.
     */
    public Handle()
    {
        super();
    }

    /**
     * Constructs a handle with the passed value.
     *
     * @param value handle value.
     */
    public Handle(long value)
    {
        super(value);
    }

    public Object clone()
    {
        return new Handle(this.getValue());
    }


    public boolean equals(Object obj)
    {
        if (obj == null || !(obj instanceof Handle))
        {
            return false;
        }
        Handle anotherHandle = (Handle) obj;
        return getValue() == anotherHandle.getValue();
    }

    /**
     * Causes the current thread to wait on this handle the specified number of milliseconds.<br>
     * This function is the wrapper for <code>WaitForSingleObject</code> Windows API function.
     *
     * @param handle handle to wait for
     * @param timeout Time-out interval, in milliseconds
     * @return wait status
     * @since 3.6
     */
    public static long waitFor(Handle handle, long timeout)
    {
        Function function = Kernel32.getInstance().getFunction(FUNCTION_WaitForSingleObject);
        UInt32 waitStatus = new UInt32();
        function.invoke(waitStatus, handle, new UInt32(timeout));
        return waitStatus.getValue();
    }

    /**
     * Causes the current thread to wait on this handle infinitely.
     *
     * @param handle handle to wait for
     * @return wait status
     * @since 3.6
     */
    public static long waitFor(Handle handle)
    {
        return waitFor(handle, INFINITE_TIMEOUT);
    }

    /**
     * Closes the opened object handle. This method closes handles to the following objects:
     * <ul>
     *  <li>Access token</li>
     *  <li>Communications device</li>
     *  <li>Console input</li>
     *  <li>Console screen buffer</li>
     *  <li>Event</li>
     *  <li>File</li>
     *  <li>File mapping</li>
     *  <li>Job</li>
     *  <li>Mailslot</li>
     *  <li>Memory resource notification</li>
     *  <li>Mutex</li>
     *  <li>Named pipe</li>
     *  <li>Pipe</li>
     *  <li>Process</li>
     *  <li>Semaphore</li>
     *  <li>Socket</li>
     *  <li>Thread</li>
     *  <li>Waitable timer</li>
     * </ul>
     * <br>
     * This function is the wrapper for <code>CloseHandle</code> Windows API function.
     *
     * @param handle handle to an opened object
     * @return true if the function succeeds, false - otherwise.
     */
    public static boolean closeHandle(Handle handle)
    {
        if (handle == null || handle.isNull())
        {
            throw new IllegalArgumentException();
        }
        Function function = Kernel32.getInstance().getFunction(FUNCTION_CloseHandle);
        Bool functionResult = new Bool();
        function.invoke(functionResult, handle);
        return functionResult.getValue();
    }
}

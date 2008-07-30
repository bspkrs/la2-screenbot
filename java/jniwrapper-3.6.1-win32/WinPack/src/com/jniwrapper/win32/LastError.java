/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32;

import com.jniwrapper.*;
import com.jniwrapper.win32.system.Kernel32;

/**
 * This class provides a status value of the last error and error messages
 * associated with the system errors.
 *
 * @author Alexander Evsukov
 */
public class LastError
{
    public static final int NO_ERROR = 0;
    public static final int ERROR_SUCCESS = 0;

    private static final String FUNCTION_GET_LAST_ERROR = "GetLastError";
    private static final String FUNCTION_SET_LAST_ERROR = "SetLastError";

    private static final int FORMAT_MESSAGE_IGNORE_INSERTS = 0x200;
    private static final int FORMAT_MESSAGE_FROM_SYSTEM = 0x1000;
    private static final int FORMAT_MESSAGE_ARGUMENT_ARRAY = 0x2000;

    /**
     * Retrieves the last error code value of the native thread.
     *
     * @return last error code value.
     * @deprecated To retrieve the last error code, use the function invocation result instead. This function <b>does not</b> guarantee that the result value is always correct.
     */
    public static int getValue()
    {
        final Function getLastError = Kernel32.getInstance().getFunction(FUNCTION_GET_LAST_ERROR);
        Int32 errorCode = new Int32();
        getLastError.invoke(errorCode);
        return (int)errorCode.getValue();
    }

    /**
     * Returns the last error value and clears it depending on the passed
     * parameter.
     *
     * @param clear if true, sets the last error status to zero; otherwise the
     *              status is not unmodified.
     * @return last error code value.
     * @deprecated To retrieve the last error code, use the result value of the function invocation instead.  This function does not guarantee that the result value is correct. To clear the last error value, use the {@link #clearLastErrorCode()} method.
     */
    public static int getValue(boolean clear)
    {
        int result = getValue();
        if (clear)
        {
            clearLastErrorCode();
        }
        return result;
    }

    /**
     * Clears the last error value.
     */
    public static void clearLastErrorCode()
    {
        setValue(NO_ERROR);
    }

    /**
     * Sets the last error code.
     *
     * @param value the value to set as last error code.
     */
    public static void setValue(int value)
    {
        final Function setLastError = Kernel32.getInstance().getFunction(FUNCTION_SET_LAST_ERROR);
        Int32 errorCode = new Int32(value);
        setLastError.invoke(null, errorCode);
    }

    /**
     * @return system error message for the last error.
     * @deprecated Use the {@link #getMessage(long)} function instead.
     */
    public static String getMessage()
    {
        return getMessage(getValue());
    }

    /**
     * Returns the system error message and clears it if the passed parameter is
     * true.
     *
     * @param clearStatus if <code>true</code>, the last errror status is set to
     *                    zero.
     * @return last system error message.
     * @deprecated Use the {@link #getMessage(long)} function instead.
     */
    public static String getMessage(boolean clearStatus)
    {
        int lastError = getValue(clearStatus);
        return getMessage(lastError);
    }

    /**
     * Searches the system message-table resource(s) for the requested error
     * code message.
     *
     * @param errorCode system error code returned by the function.invoke method.
     * @return system error message for the passed code.
     */
    public static String getMessage(long errorCode)
    {
        /*
            DWORD FormatMessage(
              DWORD dwFlags,
              LPCVOID lpSource,
              DWORD dwMessageId,
              DWORD dwLanguageId,
              LPTSTR lpBuffer,
              DWORD nSize,
              va_list* Arguments);
        */

        final FunctionName FUNCTION_FORMAT_MESSAGE = new FunctionName("FormatMessage");

        final Function formatMessage = Kernel32.getInstance().getFunction(FUNCTION_FORMAT_MESSAGE.toString());
        final UInt32 dwFlags = new UInt32(FORMAT_MESSAGE_FROM_SYSTEM |
                FORMAT_MESSAGE_IGNORE_INSERTS |
                FORMAT_MESSAGE_ARGUMENT_ARRAY);
        final Pointer.Void NULL = new Pointer.Void();
        final Str buffer = new Str("", 255);

        final UInt32 result = new UInt32();

        final UInt32 errCode = new UInt32(errorCode);
        formatMessage.invoke(result, new Parameter[]{
            dwFlags,
            NULL,
            errCode,
            new UInt32(0),
            new Pointer(buffer),
            new UInt32(buffer.getMaxLength()),
            NULL
        });

        return buffer.getValue().trim();
    }
}

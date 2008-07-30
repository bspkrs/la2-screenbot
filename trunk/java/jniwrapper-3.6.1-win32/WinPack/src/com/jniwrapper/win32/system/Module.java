/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.system;

import com.jniwrapper.*;
import com.jniwrapper.win32.FunctionName;
import com.jniwrapper.win32.Handle;
import com.jniwrapper.win32.LastErrorException;

/**
 * This class represents current native process.
 *
 * @author Alexander Evsukov
 */
public class Module extends Handle
{
    private static final FunctionName FUNCTION_GET_MODULE_HANDLE = new FunctionName("GetModuleHandle");
    private static final FunctionName FUNCTION_GET_MODULE_FILE_NAME = new FunctionName("GetModuleFileName");

    private Module()
    {
    }

    /**
     * Returns module handle for the current native process. The returned value
     * is a handle to the file used to create the calling native process.
     */
    public static Module getCurrent()
    {
        final Function function = Kernel32.getInstance().getFunction(FUNCTION_GET_MODULE_HANDLE.toString());
        Module hModule = new Module();
        function.invoke(hModule, new Handle());
        return hModule;
    }

    /**
     * Returns the file name of calling native process.
     *
     * @return the file name of calling native process.
     */
    public String getFileName()
    {
        final Function function = Kernel32.getInstance().getFunction(FUNCTION_GET_MODULE_FILE_NAME.toString());
        final Str fname = new Str("", 255);
        final UInt32 result = new UInt32();
        long errorCode = function.invoke(result, this, new Pointer(fname), new UInt32(fname.getMaxLength()));
        if (result.getValue() == 0)
        {
            throw new LastErrorException(errorCode, "Unable to get module file name.");
        }

        final String fileName = fname.getValue().trim();
        return fileName;
    }
}

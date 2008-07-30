/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.gdi;

import com.jniwrapper.Function;
import com.jniwrapper.Int;
import com.jniwrapper.win32.Handle;
import com.jniwrapper.win32.LastErrorException;
import com.jniwrapper.win32.WinFunctionCache;

/**
 * This class provides a wrapper over Win32 GDI functions.
 *
 * @author Alexander Evsukov
 */
public class Gdi32 extends WinFunctionCache
{
    static final String FUNCTION_GET_STOCK_OBJECT = "GetStockObject";

    private static Gdi32 _instance;

    public Gdi32()
    {
        super("gdi32");
    }

    public static Gdi32 getInstance()
    {
        if (_instance == null)
        {
            _instance = new Gdi32();
        }

        return _instance;
    }

    /**
     * Use this method for getting actual instances of objects that correspond
     * to stock object constants.
     *
     * @param objectType a stock object type defined by Windows API.
     * @return handle to the stock object.
     */
    public static Handle getStockObject(int objectType)
    {
        Function function = getInstance().getFunction(FUNCTION_GET_STOCK_OBJECT);
        Int brType = new Int(objectType);
        Handle handle = new Handle();
        long errorCode = function.invoke(handle, brType);
        if (handle.getValue() == 0)
        {
            throw new LastErrorException(errorCode, "Failed to get stock object", true);
        }
        return handle;
    }

    public static Function get(String functionName)
    {
        return getInstance().getFunction(functionName);
    }
}

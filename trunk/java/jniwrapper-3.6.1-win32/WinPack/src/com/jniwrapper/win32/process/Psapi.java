/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.process;

import com.jniwrapper.Function;
import com.jniwrapper.IntBool;
import com.jniwrapper.Pointer;
import com.jniwrapper.UInt32;
import com.jniwrapper.win32.LastErrorException;
import com.jniwrapper.win32.WinFunctionCache;

/**
 * This class provides functions from the Psapi library.
 *
 * @author Serge Piletsky
 */
public class Psapi extends WinFunctionCache
{
    private static final String FUNCTION_GetPerformanceInfo = "GetPerformanceInfo";

    private static Psapi _instance;

    public Psapi()
    {
        super("Psapi");
    }

    public static Psapi getInstance()
    {
        if (_instance == null)
        {
            _instance = new Psapi();
        }
        return _instance;
    }

    /**
     * Returns the performance values stored in the {@link PerformanceInformation} structure.
     *
     * @return the performance values.
     */
    public static PerformanceInformation getPerformanceInformation()
    {
        PerformanceInformation performanceInformation = new PerformanceInformation();
        Function getPerformanceInfo = getInstance().getFunction(FUNCTION_GetPerformanceInfo);
        IntBool result = new IntBool();
        long errorCode = getPerformanceInfo.invoke(result, new Pointer(performanceInformation), new UInt32(performanceInformation.getLength()));
        if (result.getValue() == 0)
        {
            throw new LastErrorException(errorCode);
        }
        return performanceInformation;
    }
}

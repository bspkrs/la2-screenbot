/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.system;

import com.jniwrapper.Function;
import com.jniwrapper.win32.WinFunctionCache;

/**
 * This class provides functions from Advapi32 library.
 *
 * @author Serge Piletsky
 */
public class AdvApi32 extends WinFunctionCache
{
    static final String LIBRARY_NAME = "Advapi32";

    private static AdvApi32 _instance;

    protected AdvApi32()
    {
        super(LIBRARY_NAME);
    }

    public static AdvApi32 getInstance()
    {
        if (_instance == null)
        {
            _instance = new AdvApi32();
        }
        return _instance;
    }

    public static Function get(Object functionName)
    {
        return getInstance().getFunction(functionName.toString());
    }
}

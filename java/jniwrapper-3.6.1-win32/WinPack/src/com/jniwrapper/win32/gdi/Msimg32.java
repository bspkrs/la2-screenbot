/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.gdi;

import com.jniwrapper.win32.WinFunctionCache;

/**
 * This class wraps the functionality provided by MSIMG32 library.
 */
public class Msimg32 extends WinFunctionCache
{
    private static Msimg32 _instance;

    private Msimg32()
    {
        super("Msimg32");
    }

    public static Msimg32 getInstance()
    {
        if (_instance == null)
        {
            _instance = new Msimg32();
        }

        return _instance;
    }
}

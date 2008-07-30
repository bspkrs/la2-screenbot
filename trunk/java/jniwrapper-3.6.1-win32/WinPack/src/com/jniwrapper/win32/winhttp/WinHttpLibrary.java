/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.winhttp;

import com.jniwrapper.win32.WinFunctionCache;

/**
 * @author Vladimir Kondrashchenko
 */
class WinHttpLibrary extends WinFunctionCache
{
    private static WinHttpLibrary _instance;

    public WinHttpLibrary()
    {
        super("WinHttp");
    }

    public static WinHttpLibrary getInstance()
    {
        if (_instance == null)
        {
            _instance = new WinHttpLibrary();
        }
        return _instance;
    }
}

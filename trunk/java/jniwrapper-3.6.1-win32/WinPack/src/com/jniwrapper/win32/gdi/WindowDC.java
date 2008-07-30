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
import com.jniwrapper.win32.LastErrorException;
import com.jniwrapper.win32.ui.User32;
import com.jniwrapper.win32.ui.Wnd;

// TODO [leha]: comments
/**
 * WindowDC class provides the ability to retrieve a DC from a specified window.
 * 
 * @author Alexander Evsukov
 */
public class WindowDC extends DC
{
    private static final String FUNCTION_GET_DC = "GetDC";
    private static final String FUNCTION_RELEASE_DC = "ReleaseDC";
    private static final String FUNCTION_GET_WINDOW_DC = "GetWindowDC";

    private Wnd _wnd;

    public WindowDC(long value)
    {
        super(value);
    }

    public WindowDC(Wnd wnd)
    {
        _wnd = wnd==null? new Wnd():wnd;
        Function function = User32.getInstance().getFunction(FUNCTION_GET_DC);
        long errorCode = function.invoke(this, _wnd);
        if (this.isNull())
        {
            throw new LastErrorException(errorCode);
        }
    }

    public void release()
    {
        Function function = User32.getInstance().getFunction(FUNCTION_RELEASE_DC);
        Int result = new Int();
        long errorCode = function.invoke(result, _wnd, this);
        if (result.getValue() == 0)
        {
            throw new LastErrorException(errorCode);
        }
    }

    public static WindowDC getWindowDC(Wnd wnd)
    {
        final Function function = User32.getInstance().getFunction(FUNCTION_GET_WINDOW_DC);
        WindowDC result = new WindowDC(wnd);
        function.invoke(result, wnd);
        return result;
    }
}

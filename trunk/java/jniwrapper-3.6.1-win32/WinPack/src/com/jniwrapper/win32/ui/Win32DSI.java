/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.ui;

import com.jniwrapper.Parameter;
import com.jniwrapper.Structure;
import com.jniwrapper.win32.Handle;

/**
 * JAWT platform-specific data structure for Win32.
 * 
 * @author AKireyev
 */
public class Win32DSI extends Structure
{
    private Handle _handle = new Handle();
    private Handle _hdc = new Handle();
    private Handle _hpalette = new Handle();

    public Win32DSI()
    {
        init(new Parameter[]{_handle, _hdc, _hpalette});
    }

    public Win32DSI(Win32DSI that)
    {
        this();
        initFrom(that);
    }

    /**
     * Returns target component handle (either window or bitmap handle).
     */
    public Handle getHandle()
    {
        return _handle;
    }

    /**
     * Retruns DC handle. This handle should be used for drawing instead of
     * handles returned from the <code>GetDC</code> or <code>BeginPaint</code>.
     */
    public Handle getHdc()
    {
        return _hdc;
    }

    /**
     * Returns palette handle.
     */
    public Handle getHpalette()
    {
        return _hpalette;
    }

    public Object clone()
    {
        return new Win32DSI(this);
    }
}

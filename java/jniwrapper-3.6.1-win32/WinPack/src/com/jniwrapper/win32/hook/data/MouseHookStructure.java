/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.hook.data;

import com.jniwrapper.Parameter;
import com.jniwrapper.Structure;
import com.jniwrapper.UInt;
import com.jniwrapper.win32.Point;
import com.jniwrapper.win32.IntPtr;
import com.jniwrapper.win32.ui.Wnd;

/**
 * This class represents
 * @see <a href="http://msdn2.microsoft.com/en-us/library/ms644968.aspx">
 *      MOUSEHOOKSTRUCT structure</a>
 *
 * @author Serge Piletsky
 */
class MouseHookStructure extends Structure
{
    /**
     * Structure that contains the x- and y-coordinates of mouse cursor.
     */
    private Point point = new Point();
    /**
     * Handle to the window that will receive the mouse event.
     */
    private Wnd wnd = new Wnd();
    /**
     * Specifies the hit-test value. You can find list of hit-test values in
     * @see <a href="http://msdn2.microsoft.com/en-us/library/ms645618.aspx">
     *      WM_NCHITTEST Notification</a>
     */
    private UInt hitTestCode = new UInt();
    /**
     *  Extra information associated with the message
     */
    private IntPtr extraInfo = new IntPtr();

    MouseHookStructure()
    {
        init(new Parameter[]{point, wnd, hitTestCode, extraInfo}, (short)8);
    }

    MouseHookStructure(MouseHookStructure that)
    {
        this();
        initFrom(that);
    }

    Point getPoint()
    {
        return point;
    }

    Wnd getWnd()
    {
        return wnd;
    }

    long getHitTestCode()
    {
        return hitTestCode.getValue();
    }

    long getExtraInfo()
    {
        return extraInfo.getValue();
    }

    public Object clone()
    {
        return new MouseHookStructure(this);
    }
}

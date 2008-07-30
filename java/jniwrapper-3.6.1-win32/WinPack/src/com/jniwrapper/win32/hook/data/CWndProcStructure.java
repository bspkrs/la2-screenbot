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
import com.jniwrapper.UInt32;
import com.jniwrapper.win32.ui.Wnd;
import com.jniwrapper.win32.IntPtr;

/**
 * This class represents the CWPSTRUCT structure, which defines the message
 * parameters passed to a WH_CALLWNDPROC hook procedure.
 * 
 * @author Serge Piletsky
 */
class CWndProcStructure extends Structure
{
    protected IntPtr lParam = new IntPtr();
    protected IntPtr wParam = new IntPtr();
    protected UInt message = new UInt();
    protected Wnd hwnd = new Wnd();

    public CWndProcStructure()
    {
        init(new Parameter[]{lParam, wParam, message, hwnd}, (short)8);
    }

    public CWndProcStructure(CWndProcStructure that)
    {
        this();
        initFrom(that);
    }

    /**
     * Specifies additional information about the message. The exact meaning
     * depends on the message value.
     */
    long getLParam()
    {
        return lParam.getValue();
    }

    /**
     * Specifies additional information about the message. The exact meaning
     * depends on the message value.
     */
    long getWParam()
    {
        return wParam.getValue();
    }

    /**
     * Specifies the message.
     */
    long getMessage()
    {
        return message.getValue();
    }

    /**
     * Handle to the window to receive the message.
     */
    Wnd getWnd()
    {
        return hwnd;
    }

    public Object clone()
    {
        return new CWndProcStructure(this);
    }
}

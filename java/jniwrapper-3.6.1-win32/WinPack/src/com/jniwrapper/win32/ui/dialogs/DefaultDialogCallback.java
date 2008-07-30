/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.ui.dialogs;

import com.jniwrapper.Callback;
import com.jniwrapper.Parameter;
import com.jniwrapper.UInt;
import com.jniwrapper.win32.ui.Wnd;
import com.jniwrapper.win32.IntPtr;

/**
 * Default callback for Dialogs.
 */
public class DefaultDialogCallback extends Callback
{
    protected Wnd _wnd = new Wnd();
    protected UInt _msg = new UInt();
    protected IntPtr _wParam = new IntPtr();
    protected IntPtr _lparam = new IntPtr();
    protected IntPtr _returnParam = new IntPtr();

    public DefaultDialogCallback()
    {
        init(new Parameter[]
        {
            _wnd,
            _msg,
            _wParam,
            _lparam
        }, _returnParam);
    }

    public void callback()
    {
    }

    public Wnd getWnd()
    {
        return _wnd;
    }

    public UInt getMsg()
    {
        return _msg;
    }

    public IntPtr getwParam()
    {
        return _wParam;
    }

    public IntPtr getLparam()
    {
        return _lparam;
    }

    public IntPtr getReturnParam()
    {
        return _returnParam;
    }
}

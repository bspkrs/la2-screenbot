/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.ui;

import java.util.EventObject;

/**
 * WindowMessage class.
 *
 * @author Serge Piletsky
 */
public class WindowMessage extends EventObject
{
    private int _msg;
    private long _wParam;
    private long _lParam;

    public WindowMessage(Object source, int msg, long wParam, long lParam)
    {
        super(source);
        _msg = msg;
        _wParam = wParam;
        _lParam = lParam;
    }

    public int getMsg()
    {
        return _msg;
    }

    public void setMsg(int value)
    {
        _msg = value;
    }

    public long getWParam()
    {
        return _wParam;
    }

    public void setWParam(long value)
    {
        _wParam = value;
    }

    public long getLParam()
    {
        return _lParam;
    }

    public void setLParam(long value)
    {
        _lParam = value;
    }

    public String toString()
    {
        StringBuffer result = new StringBuffer("WindowMessage: [");
        result.append("MSG=").append(_msg).append("; WPARAM=").append(_wParam).append("; LPARAM=").append(_lParam).append(']');
        return result.toString();
    }
}

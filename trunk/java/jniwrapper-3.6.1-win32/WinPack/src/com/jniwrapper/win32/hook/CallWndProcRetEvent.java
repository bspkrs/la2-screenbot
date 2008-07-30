/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.hook;


import com.jniwrapper.win32.ui.Wnd;

/**
 * This class describes events of the {@link
 * com.jniwrapper.win32.hook.Hook.Descriptor#CALLWNDPROCRET} hook.
 * 
 * @author Serge Piletsky
 */
public class CallWndProcRetEvent extends CallWndProcEvent
{
    private long _result;

    public CallWndProcRetEvent(Object source, long result, long LParam, long WParam, long message, Wnd wnd, boolean sentByCurrentThread)
    {
        super(source, LParam, WParam, message, wnd, sentByCurrentThread);
        _result = result;
    }

    public long getResult()
    {
        return _result;
    }
}

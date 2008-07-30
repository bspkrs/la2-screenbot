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
 * com.jniwrapper.win32.hook.Hook.Descriptor#CALLWNDPROC} hook.
 * 
 * @author Serge Piletsky
 */
public class CallWndProcEvent extends HookEventObject
{
    /**
     * Specifies additional information about the message.
     */
    protected long _LParam;
    /**
     * Specifies additional information about the message.
     */
    protected long _WParam;
    /**
     * Specifies the message.
     */
    protected long _message;
    /**
     * Handle to the window to receive the message.
     */
    protected Wnd _wnd;
    /**
     * Specifies whether the message was sent by the current thread.
     */
    protected boolean _sentByCurrentThread;

    public CallWndProcEvent(Object source, long LParam, long WParam, long message, Wnd wnd, boolean sentByCurrentThread)
    {
        super(source);
        _LParam = LParam;
        _WParam = WParam;
        _message = message;
        _wnd = wnd;
        _sentByCurrentThread = sentByCurrentThread;
    }

    public long getLParam()
    {
        return _LParam;
    }

    public long getWParam()
    {
        return _WParam;
    }

    public long getMessage()
    {
        return _message;
    }

    public Wnd getWnd()
    {
        return _wnd;
    }

    public boolean isSentByCurrentThread()
    {
        return _sentByCurrentThread;
    }
}

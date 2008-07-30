/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.hook;

/**
 * This class describes events of the {@link
 * com.jniwrapper.win32.hook.Hook.Descriptor#SHELL} hook.
 * 
 * @author Serge Piletsky
 */
public class ShellEvent extends HookEventObject
{
    private long _code;
    private long _wParam;
    private long _lParam;

    public ShellEvent(Object source, long code, long wParam, long lParam)
    {
        super(source);
        _code = code;
        _wParam = wParam;
        _lParam = lParam;
    }

    public long getCode()
    {
        return _code;
    }

    public long getWParam()
    {
        return _wParam;
    }

    public long getLParam()
    {
        return _lParam;
    }
}

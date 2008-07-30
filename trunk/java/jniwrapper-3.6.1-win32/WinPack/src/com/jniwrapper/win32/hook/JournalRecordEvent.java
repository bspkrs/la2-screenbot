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
 * com.jniwrapper.win32.hook.Hook.Descriptor#JOURNALRECORD} hook. This event
 * contains a message to be recorded.
 * 
 * @author Serge Piletsky
 */
public class JournalRecordEvent extends HookEventObject
{
    private static final int HC_SYSMODALON = 4;
    private static final int HC_SYSMODALOFF = 5;

    private long _code;
    private long _message;
    private long _paramL;
    private long _paramH;
    private long _time;
    private Wnd _wnd;

    public JournalRecordEvent(Object source, long code)
    {
        super(source);
        _code = code;
    }

    public JournalRecordEvent(Object source, long code, long message, long paramL, long paramH, long time, Wnd wnd)
    {
        super(source);
        _code = code;
        _message = message;
        _paramL = paramL;
        _paramH = paramH;
        _time = time;
        _wnd = wnd;
    }

    public long getCode()
    {
        return _code;
    }

    public boolean isAction()
    {
        return _code == 0;
    }

    public boolean isSystemModalDialogDestroyed()
    {
        return _code == HC_SYSMODALOFF;
    }

    public boolean isSystemModalDialogDisplayed()
    {
        return _code == HC_SYSMODALON;
    }

    public long getMessage()
    {
        return _message;
    }

    public long getParamL()
    {
        return _paramL;
    }

    public long getParamH()
    {
        return _paramH;
    }

    public long getTime()
    {
        return _time;
    }

    public Wnd getWnd()
    {
        return _wnd;
    }
}

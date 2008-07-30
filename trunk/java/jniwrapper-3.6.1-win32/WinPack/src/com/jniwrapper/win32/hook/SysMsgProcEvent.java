/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.hook;

import com.jniwrapper.win32.Msg;

/**
 * This class describes events of the {@link
 * com.jniwrapper.win32.hook.Hook.Descriptor#SYSMSGFILTER} hook.
 * 
 * @author Serge Piletsky
 */
public class SysMsgProcEvent extends HookEventObject
{
    private static final int MSGF_DIALOGBOX = 0;
    private static final int MSGF_MENU = 2;
    private static final int MSGF_SCROLLBAR = 5;

    private long _code;
    private Msg _msg;

    public SysMsgProcEvent(Object source, long code, Msg msg)
    {
        super(source);
        _code = code;
        _msg = msg;
    }

    public long getCode()
    {
        return _code;
    }

    public Msg getMsg()
    {
        return _msg;
    }

    public boolean isMessageBoxEvent()
    {
        return _code == MSGF_DIALOGBOX;
    }

    public boolean isMenuEvent()
    {
        return _code == MSGF_MENU;
    }

    public boolean isScrollbarEvent()
    {
        return _code == MSGF_SCROLLBAR;
    }
}


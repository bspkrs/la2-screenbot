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
 * com.jniwrapper.win32.hook.Hook.Descriptor#GETMESSAGE} hook.
 * 
 * @author Serge Piletsky
 */
public class GetMsgEvent extends HookEventObject
{
    private boolean _isMessageRemoved;
    private Msg _msg;

    public GetMsgEvent(Object source, boolean messageRemoved, Msg msg)
    {
        super(source);
        _isMessageRemoved = messageRemoved;
        _msg = msg;
    }

    public boolean isMessageRemoved()
    {
        return _isMessageRemoved;
    }

    public Msg getMsg()
    {
        return _msg;
    }
}

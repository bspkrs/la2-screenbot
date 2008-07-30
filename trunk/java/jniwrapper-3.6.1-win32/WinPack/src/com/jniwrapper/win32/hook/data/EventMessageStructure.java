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

/**
 * This class represents EVENTMSG structure.
 *
 * @author Serge Piletsky
 */
class EventMessageStructure extends Structure
{
    private UInt message = new UInt();
    private UInt paramL = new UInt();
    private UInt paramH = new UInt();
    private UInt32 time = new UInt32();
    private Wnd hwnd = new Wnd();

    EventMessageStructure()
    {
        init(new Parameter[]{message, paramL, paramH, time, hwnd});
    }

    EventMessageStructure(EventMessageStructure that)
    {
        this();
        initFrom(that);
    }

    long getMessage()
    {
        return message.getValue();
    }

    long getParamL()
    {
        return paramL.getValue();
    }

    long getParamH()
    {
        return paramH.getValue();
    }

    long getTime()
    {
        return time.getValue();
    }

    Wnd getHwnd()
    {
        return hwnd;
    }

    public Object clone()
    {
        return new EventMessageStructure(this);
    }
}

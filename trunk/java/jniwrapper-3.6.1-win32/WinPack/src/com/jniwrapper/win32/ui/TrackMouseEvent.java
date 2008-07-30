/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.ui;

import com.jniwrapper.Parameter;
import com.jniwrapper.Structure;
import com.jniwrapper.UInt32;

/**
 * This class represents TRACKMOUSEEVENT structure.
 *
 * @author Serge Piletsky
 */
public class TrackMouseEvent extends Structure
{
    public static final int TME_HOVER = 0x00000001;
    public static final int TME_LEAVE = 0x00000002;
    public static final int TME_QUERY = 0x40000000;
    public static final int TME_CANCEL = 0x80000000;

    public static final int HOVER_DEFAULT = 0xFFFFFFFF;

    private UInt32 _cbSize = new UInt32();
    private UInt32 _dwFlags = new UInt32();
    private Wnd _hwndTrack = new Wnd();
    private UInt32 _dwHoverTime = new UInt32();

    public TrackMouseEvent(Wnd wnd)
    {
        this(wnd, HOVER_DEFAULT);
    }

    public TrackMouseEvent(Wnd wnd, int hoverTime)
    {
        init(new Parameter[]{_cbSize, _dwFlags, _hwndTrack, _dwHoverTime});
        _cbSize.setValue(getLength());
        _hwndTrack.setValue(wnd.getValue());
        _dwHoverTime.setValue(hoverTime);
        _dwFlags.setValue(TME_HOVER | TME_LEAVE);
    }

    public TrackMouseEvent(TrackMouseEvent that)
    {
        this(that._hwndTrack);
        initFrom(that);
    }

    public Wnd getWindowHandle()
    {
        return _hwndTrack;
    }

    public int getHoverTime()
    {
        return (int)_dwHoverTime.getValue();
    }

    public void setHoverTime(int value)
    {
        _dwHoverTime.setValue(value);
    }

    public int getFlags()
    {
        return (int)_dwFlags.getValue();
    }

    public void setFlags(int flags)
    {
        _dwFlags.setValue(flags);
    }

    public Object clone()
    {
        return new TrackMouseEvent(this);
    }
}

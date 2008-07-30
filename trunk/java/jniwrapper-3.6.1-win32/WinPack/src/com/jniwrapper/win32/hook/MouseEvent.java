/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.hook;

import com.jniwrapper.win32.Point;
import com.jniwrapper.win32.ui.Wnd;

/**
 * This class describes events of the {@link
 * com.jniwrapper.win32.hook.Hook.Descriptor#MOUSE} hook.
 * 
 * @author Serge Piletsky
 */
public class MouseEvent extends HookEventObject
{
    private long _mouseMessageID;
    private Point _point;
    private Wnd _wnd;
    private long _hitTestCode;
    private long _extraInfo;

    public MouseEvent(Object source, long mouseMessageID, Point point, Wnd wnd, long hitTestCode, long extraInfo)
    {
        super(source);
        _mouseMessageID = mouseMessageID;
        _point = point;
        _wnd = wnd;
        _hitTestCode = hitTestCode;
        _extraInfo = extraInfo;
    }

    public long getMouseMessageID()
    {
        return _mouseMessageID;
    }

    public Point getPoint()
    {
        return _point;
    }

    public Wnd getWindow()
    {
        return _wnd;
    }

    public long getHitTestCode()
    {
        return _hitTestCode;
    }

    public long getExtraInfo()
    {
        return _extraInfo;
    }
}

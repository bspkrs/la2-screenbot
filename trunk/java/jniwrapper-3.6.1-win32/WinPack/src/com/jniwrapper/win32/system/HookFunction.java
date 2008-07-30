/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.system;

import com.jniwrapper.Callback;
import com.jniwrapper.LongInt;
import com.jniwrapper.Parameter;
import com.jniwrapper.UInt32;
import com.jniwrapper.win32.ui.Wnd;

/**
 * <code>HookFunction</code> is a superclass for all classes representing callback functions
 * used in WinEventHook class.
 * Any class derived from <code>HookFunction</code> must override the <code>callback()</code> method.
 * Use getters to access the function parameters in a <code>callback()</code> method implementation.
 *
 * @author Vladimir Kondrashchenko
 */
abstract public class HookFunction extends Callback
{
    private UInt32 _hWinEventHook = new UInt32();
    private UInt32 _event = new UInt32();
    private Wnd _hwnd = new Wnd();
    private LongInt _idObject = new LongInt();
    private LongInt _idChild = new LongInt();
    private UInt32 _dwEventThread = new UInt32();
    private UInt32 _dwmsEventTime = new UInt32();

    public HookFunction()
    {
        init(new Parameter[]{_hWinEventHook, _event, _hwnd, _idObject, _idChild, _dwEventThread, _dwmsEventTime},
                null);
    }

    /**
     *
     * @return the identifier of an event hook function.
     */
    public long getWinEventHook()
    {
        return _hWinEventHook.getValue();
    }

    /**
     * Specifies the event that occurred.
     *
     * @return the event constant.
     */
    public int getEvent()
    {
        return (int)_event.getValue();
    }

    /**
     *
     * @return the handle to the window that generates the event.
     */
    public Wnd getWnd()
    {
        return _hwnd;
    }

    /**
     * Identifies the object associated with the event.
     *
     * @return the object identifier.
     */
    public long getObject()
    {
        return _idObject.getValue();
    }

    /**
     * Identifies whether the event was triggered by a child element of the object.
     *
     * @return the identifier of the child element of the object, or 0 if the event was triggered by the object.
     */
    public long getIdChild()
    {
        return _idChild.getValue();
    }

    /**
     * Identifies the thread that generated the event.
     *
     * @return the thread identifier.
     */
    public long getEventThread()
    {
        return _dwEventThread.getValue();
    }

    /**
     *
     * @return the time in milliseconds when the event was generated.
     */
    public long getEventTime()
    {
        return _dwmsEventTime.getValue();
    }
}

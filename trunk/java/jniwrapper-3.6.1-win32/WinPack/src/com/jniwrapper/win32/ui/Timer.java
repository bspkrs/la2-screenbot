/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.ui;

import com.jniwrapper.Function;
import com.jniwrapper.Parameter;
import com.jniwrapper.UInt;
import com.jniwrapper.UInt32;
import com.jniwrapper.win32.LastErrorException;

/**
 * System timer that executes a callback after a specified timeout value
 * elapsed.
 * 
 * @author Alexander Evsukov
 */
public class Timer extends UInt
{
    private static final String FUNCTION_SET_TIMER = "SetTimer";
    private static final String FUNCTION_KILL_TIMER = "KillTimer";

    private Wnd _wnd;
    private long _timerID;
    private long _timeout;
    private Timer.Callback _callback;

    public Timer(Wnd wnd, long timerID, long timeout, Callback callback)
    {
        _wnd = wnd;
        _timerID = timerID;
        _timeout = timeout;
        _callback = callback;
    }

    public Wnd getWnd()
    {
        return _wnd;
    }

    public long getTimerID()
    {
        return _timerID;
    }

    public long getTimeout()
    {
        return _timeout;
    }

    public Callback getCallback()
    {
        return _callback;
    }

    /**
     * Starts the timer using <code>SetTimer</code> API function.
     */
    public void start()
    {
        final Function function = User32.getInstance().getFunction(FUNCTION_SET_TIMER);
        long errorCode = function.invoke(this, _wnd, new UInt(_timerID), new UInt(_timeout), _callback);
        if (getValue() == 0)
        {
            throw new LastErrorException(errorCode, "Failed to create a timer.", true);
        }
    }

    /**
     * Kills the timer using <code>KillTimer</code> API function.
     */
    public void stop()
    {
        final Function function = User32.getInstance().getFunction(FUNCTION_KILL_TIMER);
        function.invoke(null, _wnd, new UInt(_timerID));
    }

    public abstract static class Callback extends com.jniwrapper.Callback
    {
        private Wnd _wnd = new Wnd();
        private UInt _msg = new UInt();
        private UInt _timerID = new UInt();
        private UInt32 _time = new UInt32();

        public Callback()
        {
            init(new Parameter[]{
                _wnd,
                _msg,
                _timerID,
                _time
            }, null);
        }

        public Wnd getWnd()
        {
            return _wnd;
        }

        public UInt getMsg()
        {
            return _msg;
        }

        public UInt getTimerID()
        {
            return _timerID;
        }

        /**
         * Returns the number of milliseconds elapsed since the system was
         * started. This is the value returned by the GetTickCount function from
         * Windows API.
         * 
         * @return number of milliseconds since the system start.
         */
        public UInt32 getTime()
        {
            return _time;
        }
    }
}

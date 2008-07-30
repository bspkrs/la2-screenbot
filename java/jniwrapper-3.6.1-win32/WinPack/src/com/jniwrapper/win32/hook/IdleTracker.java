/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.hook;

import com.jniwrapper.win32.hook.Hook;
import com.jniwrapper.win32.hook.HookEventListener;
import com.jniwrapper.win32.hook.HookEventObject;

import javax.swing.Timer;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * This class provides functionality for controlling the mouse and keyboard idleness.
 *
 * @author Vladimir Kondrashchenko
 */
public class IdleTracker
{
    private long _idleDelay;
    private List _listeners = new ArrayList();
    private Hook _mouseHook = Hook.MOUSE;
    private Hook _keyboardHook = Hook.KEYBOARD;
    private Timer _timer;

    /**
     * Creates an instance of <CODE>com.jniwrapper.win32.hook.IdleTracker</CODE> class
     * and initilizes the idleness timeout with 1 minute timeout.
     */
    public IdleTracker()
    {
        this(60000);
    }

    /**
     * Creates an instance of <CODE>com.jniwrapper.win32.hook.IdleTracker</CODE> class
     * and initilizes the idleness timeout.
     *
     * @param idleDelay is the timeout of mouse and keyboard idleness in milliseconds.
     */
    public IdleTracker(long idleDelay)
    {
        _idleDelay = idleDelay;

        _mouseHook.addListener(new HookEventListener()
        {
            public void onHookEvent(HookEventObject event)
            {
                _timer.restart();
            }
        });

        _keyboardHook.addListener(new HookEventListener()
        {
            public void onHookEvent(HookEventObject event)
            {
                _timer.restart();
            }
        });

        _timer = new Timer((int)_idleDelay, new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                for (Iterator i = _listeners.iterator(); i.hasNext();)
                {
                    IdleTrackerListener listener = (IdleTrackerListener)i.next();
                    listener.timeoutElapsed();
                }
            }
        });
    }

    /**
     * Starts idle tracking.
     */
    public void start()
    {
        if (!_mouseHook.isInstalled())
        {
            _mouseHook.install();
        }
        if (!_keyboardHook.isInstalled())
        {
            _keyboardHook.install();
        }
        _timer.start();
    }

    /**
     * Stops idle tracking.
     */
    public void stop()
    {
        if (_mouseHook.isInstalled())
        {
            _mouseHook.uninstall();
        }
        if (_keyboardHook.isInstalled())
        {
            _keyboardHook.uninstall();
        }
        _timer.stop();
    }

    /**
     * Adds a listener of the idle timeout event.
     *
     * @param listener
     */
    public void addListener(IdleTrackerListener listener)
    {
        _listeners.add(listener);
    }

    /**
     * Removes a listener of the idle timeout event.
     *
     * @param listener
     */
    public void removeListener(IdleTrackerListener listener)
    {
        _listeners.remove(listener);
    }
}

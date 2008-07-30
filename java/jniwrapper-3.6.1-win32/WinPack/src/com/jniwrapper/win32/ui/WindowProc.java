/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.ui;

import com.jniwrapper.*;
import com.jniwrapper.win32.FunctionName;
import com.jniwrapper.win32.Handle;
import com.jniwrapper.win32.LastErrorException;
import com.jniwrapper.win32.IntPtr;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

// TODO: refactor (capture/restore of state for callback), problems specific for subclass

/**
 * This class represents default WINDOWPROC callback. Also, it provides ability
 * to substitute window proc. of a specified window to enable custom message
 * handling and processing.
 * 
 * @author Serge Piletsky
 */
public class WindowProc extends Callback
{
    private static final FunctionName FUNCTION_DEF_WINDOW_PROC = new FunctionName("DefWindowProc");

    protected Wnd _wnd = new Wnd();
    protected UInt _msg = new UInt();
    protected IntPtr _wParam = new IntPtr();
    protected IntPtr _lParam = new IntPtr();
    protected IntPtr _lResult = new IntPtr();
    private Function _defWindowProc;
    private boolean _substituted = false;
    private Handle _wndProc;
    private List _messageListeners = new LinkedList();

    /**
     * 
     * @param wnd is a handle of the window whose window procedure can be
     * substituted.
     */
    public WindowProc(Wnd wnd)
    {
        this();
        setWnd(wnd);
    }

    public WindowProc()
    {
        init(new Parameter[]{_wnd, _msg, _wParam, _lParam}, _lResult);
        _defWindowProc = getDefWindowProc();
    }

    public void callback()
    {
        if (isSubstituted())
        {
            boolean isCallWindowProc = notifyListeners(true);

            if (isCallWindowProc)
            {
                _lResult.setValue(Wnd.callWindowProc(_wndProc, _wnd, _msg, _wParam, _lParam));
            }

            notifyListeners(false);
        }
        else
        {
            _defWindowProc.invoke(_lResult, _wnd, _msg, _wParam, _lParam);
        }
    }

    /**
     * 
     * @param beforeWndProc
     * @return true if call window procedure.
     */
    private boolean notifyListeners(boolean beforeWndProc)
    {
        boolean result = true;

        final WindowMessage message = new WindowMessage(this, (int)_msg.getValue(), _wParam.getValue(), _lParam.getValue());
        List copyMessageListeners = new LinkedList(_messageListeners);
        for (Iterator i = copyMessageListeners.iterator(); i.hasNext();)
        {
            WindowMessageListener listener = (WindowMessageListener) i.next();
            if (listener.canHandle(message, beforeWndProc))
            {
                _lResult.setValue(listener.handle(message));
                _msg.setValue(message.getMsg());
                _wParam.setValue(message.getWParam());
                _lParam.setValue(message.getLParam());
            }

            if (listener instanceof WindowMessageListenerEx)
            {
                boolean isCallWindowProc = ((WindowMessageListenerEx) listener).isCallWindowProc(message);
                result = result && isCallWindowProc;
            }
        }

        return result;
    }

    private static Function getDefWindowProc()
    {
        final Function function = User32.getInstance().getFunction(FUNCTION_DEF_WINDOW_PROC.toString());
        return function;
    }

    /**
     * Adds a specified window message listener.
     * 
     * @param listener
     */
    public void addMessageListener(WindowMessageListener listener)
    {
        if (!_messageListeners.contains(listener))
            _messageListeners.add(listener);
    }

    /**
     * Removes a specified listener.
     * 
     * @param listener
     */
    public void removeMessageListener(WindowMessageListener listener)
    {
        _messageListeners.remove(listener);
    }

    /**
     * Substitutes a custom window procedure to enable custom message handling.
     *
     * @throws RuntimeException if fails to substitute window procedure (for example, if trying
     * substitute procedure of window created by another process).
     */
    public void substitute()
    {
        _wndProc = new Handle(_wnd.getWindowLong(Wnd.GWL_WNDPROC));
        try
        {
            long result = _wnd.setWindowLong(Wnd.GWL_WNDPROC, this);
            if (result != 0)
            {
                _substituted = true;
            }
        }
        catch(LastErrorException e)
        {
            throw new LastErrorException("Failed to substitute the window procedure for the specified window.", e);
        }
    }

    /**
     * Restores the native window procedure and disables custom message
     * handling.
     */
    public void restoreNative()
    {
        if (_wndProc == null || _wndProc.isNull())
            throw new IllegalStateException();
        _wnd.setWindowLong(Wnd.GWL_WNDPROC, _wndProc);
        _substituted = false;
    }

    /**
     * Checks if native window procedure is substituted.
     * 
     * @return true if native procedure is substituted; otherwise false.
     */
    public boolean isSubstituted()
    {
        return _substituted;
    }

    public Wnd getWnd()
    {
        return _wnd;
    }

    public void setWnd(Wnd wnd)
    {
        if (wnd == null || wnd.isNull())
            throw new IllegalArgumentException();

        final boolean wasSubstituted = isSubstituted();
        if (wasSubstituted)
        {
            restoreNative();
        }
        _wnd.setValue(wnd.getValue());
        if (wasSubstituted)
        {
            substitute();
        }
    }
}

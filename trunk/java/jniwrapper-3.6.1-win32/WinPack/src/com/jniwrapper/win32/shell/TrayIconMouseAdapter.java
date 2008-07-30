/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.shell;

import com.jniwrapper.Function;
import com.jniwrapper.UInt;
import com.jniwrapper.win32.Msg;
import com.jniwrapper.win32.ui.User32;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TrayIconMouseAdapter implements TrayIconListener
{
    public static final String FUNCTION_GetDoubleClickTime = "GetDoubleClickTime";
    private List _listeners = new LinkedList();
    private int _doubleClickTime = 0;

    static final int MOUSE_CLICKED = 0;
    static final int MOUSE_ENTERED = 1;
    static final int MOUSE_EXITED = 2;
    static final int MOUSE_PRESSED = 3;
    static final int MOUSE_RELEASED = 4;

    public TrayIconMouseAdapter()
    {
        final Function function = User32.getInstance().getFunction(FUNCTION_GetDoubleClickTime);
        UInt result = new UInt();
        function.invoke(result);
        _doubleClickTime = (int)result.getValue();
    }

    public void trayActionPerformed(long message, int x, int y)
    {
        int modifiers = 0;
        boolean popupTrigger = false;
        int clickCount = 0;

        MouseEvent mouseEvent = null;
        int eventKind = -1;

        switch ((int)message)
        {
            case Msg.WM_MOUSEHOVER:
            case Msg.WM_MOUSELEAVE:
                mouseEvent = new MouseEvent(null, 0, new Date().getTime(), modifiers, x, y, 0, false);

            case Msg.WM_LBUTTONDOWN:
            case Msg.WM_RBUTTONDOWN:
                eventKind = MOUSE_PRESSED;

            case Msg.WM_LBUTTONUP:
            case Msg.WM_RBUTTONUP:
                eventKind = MOUSE_RELEASED;
                break;

            case Msg.WM_RBUTTONDBLCLK:
            case Msg.WM_LBUTTONDBLCLK:
                eventKind = MOUSE_CLICKED;
                break;
        }

        for (Iterator i = _listeners.iterator(); i.hasNext();)
        {
            MouseListener listener = (MouseListener)i.next();
            switch (eventKind)
            {
                case MOUSE_CLICKED:
                    listener.mouseClicked(mouseEvent);
                    break;

                case MOUSE_ENTERED:
                    listener.mouseEntered(mouseEvent);
                    break;

                case MOUSE_EXITED:
                    listener.mouseExited(mouseEvent);
                    break;

                case MOUSE_PRESSED:
                    listener.mousePressed(mouseEvent);
                    break;

                case MOUSE_RELEASED:
                    listener.mouseReleased(mouseEvent);
                    break;
            }
        }
    }

    /**
     * Adds mouse listener
     *
     * @param listener
     */
    public void addTrayListener(MouseListener listener)
    {
        _listeners.add(listener);
    }

    /**
     * Removes mouse listener
     *
     * @param listener
     */
    public void removeTrayListener(MouseListener listener)
    {
        _listeners.remove(listener);
    }
}

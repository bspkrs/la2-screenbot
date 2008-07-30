/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.shell;

import com.jniwrapper.*;
import com.jniwrapper.util.Logger;
import com.jniwrapper.win32.FunctionName;
import com.jniwrapper.win32.LastErrorException;
import com.jniwrapper.win32.Msg;
import com.jniwrapper.win32.Point;
import com.jniwrapper.win32.gdi.Cursor;
import com.jniwrapper.win32.gdi.Icon;
import com.jniwrapper.win32.system.DllVersionInfo;
import com.jniwrapper.win32.ui.User32;
import com.jniwrapper.win32.ui.WindowProc;
import com.jniwrapper.win32.ui.Wnd;
import com.jniwrapper.win32.ui.WndClass;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

/**
 * TrayIcon class enables to add/modify a tray icon and its properties such as hint,
 * icon, tooltip, callout.
 * <p/>
 * You can register TrayIcon listeners for receiving mouse events (WM_MOUSEMOVE, WM_LBUTTONDOWN etc.)
 *
 * @author Alexander Kireyev
 * @author Vladimir Kondrashchenko
 */
public class TrayIcon extends Component
{
    private static final Logger LOG = Logger.getInstance(TrayIcon.class);

    static final FunctionName FUNCTION_SHELL_NOTIFY_ICON = new FunctionName("Shell_NotifyIcon");
    static final FunctionName FUNCTION_REGISTER_WINDOW_MESSAGE = new FunctionName("RegisterWindowMessage");
    /*
     * Operation constants for adding, modifying and removing icon in a tray.
     */
    // operation ID for adding tray icon to tray
    static final int NIM_ADD = 0x00000000;
    // operation ID for modifying icon in the tray
    static final int NIM_MODIFY = 0x00000001;
    // operation ID for removig icon from the tray
    static final int NIM_DELETE = 0x00000002;

    /*
     * Constants for configuring a tray icon.
     */
    static final int NIF_MESSAGE = 0x00000001;
    static final int NIF_ICON = 0x00000002;
    static final int NIF_TIP = 0x00000004;
    static final int NIF_STATE = 0x00000008;
    static final int NIF_INFO = 0x00000010;

    /*
     * Extended tray icon attributes.
     */
    static final int NIS_HIDDEN = 0x00000001;
    static final int NIS_SHAREDICON = 0x00000002;
    public static final int WM_TRAY = Msg.WM_USER + 1;
    public static final int WM_RESTORE_TRAY;

    static
    {
        Function registerWindowMessage = User32.getInstance().getFunction(FUNCTION_REGISTER_WINDOW_MESSAGE.toString());
        UInt res = new UInt();
        registerWindowMessage.invoke(res, new Str("TaskbarCreated"));
        WM_RESTORE_TRAY = (int)res.getValue();
    }

    /**
     * Balloon events
     */
    private static final int NIN_BALLOONSHOWN = Msg.WM_USER + 2;
    private static final int NIN_BALLOONHIDE = Msg.WM_USER + 3;
    private static final int NIN_BALLOONTIMEOUT = Msg.WM_USER + 4;
    private static final int NIN_BALLOONUSERCLICK = Msg.WM_USER + 5;

    static final String CLASS_NAME = "JW_TrayWindowClassName";

    private static Map _messageHandlers = new HashMap();
    private static final Object _trayWindowLock = new Object();
    private static int _curID = 0;
    private static long _hWnd;

    private final int _trayID;
    private boolean _disposed = false;
    private List _listeners = new ArrayList();

    private boolean _hidingAvailable = false;
    private boolean _visible = false;
    private Icon _icon;
    private JPopupMenu _popupMenu = null;
    private JFrame _popupWindow = null;
    private TrayIconListener _popupTrayIconListener = null;

    public TrayIcon()
    {
        this(null);
    }

    /**
     * Creates an instance of TrayIcon with a specified icon.
     *
     * @param icon
     */
    public TrayIcon(Icon icon)
    {
        DllVersionInfo dllVersionInfo = new DllVersionInfo("Comctl32");

        _hidingAvailable = dllVersionInfo.getMajorVersion() >= 5;

        _trayID = _curID++;
        ensureEventProcessing();
        _messageHandlers.put(new Integer(_trayID), this);
        NotifyIconData notifyicondata = new NotifyIconData(_hWnd, _trayID);
        notifyicondata.setCallbackMessage(WM_TRAY);
        notifyicondata.setFlags(NIF_ICON | NIF_MESSAGE | NIF_TIP | NIF_INFO);
        notify(NIM_ADD, notifyicondata);
        _visible = true;
        setIcon(icon);    
    }

    /**
     * @param operation      specifies the action to be taken.
     * @param notifyIconData is a prepared structure for notify function.
     */
    private void notify(int operation, NotifyIconData notifyIconData)
    {
        if (_disposed)
        {
            throw new IllegalStateException("Already disposed");
        }
        Function function = Shell32.getInstance().getFunction(FUNCTION_SHELL_NOTIFY_ICON.toString());
        Bool result = new Bool();
        long errorCode = function.invoke(result, new Int32(operation), new Pointer(notifyIconData));
        if (!result.getValue())
        {
            throw new LastErrorException(errorCode, "Icon operation failed.");
        }
    }

    private static void ensureEventProcessing()
    {
        synchronized (_trayWindowLock)
        {
            if (_hWnd == 0)
            {
                Thread thread = new Thread()
                {
                    public void run()
                    {
                        createEmptyNativeWindow();
                        Wnd.eventLoop(_hWnd);
                    }
                };
                thread.setDaemon(true);
                thread.start();
                try
                {
                    _trayWindowLock.wait();
                }
                catch (InterruptedException e)
                {
                    LOG.error("", e);
                }
                if (_hWnd == 0)
                {
                    throw new RuntimeException("Event processing window creation failed");
                }
            }
        }
    }

    /**
     * Sets an icon in the system tray.
     *
     * @param icon
     */
    public void setIcon(Icon icon)
    {
        if (icon != null && !icon.isNull())
        {
            _icon = icon;
            NotifyIconData notifyicondata = new NotifyIconData(_hWnd, _trayID);
            notifyicondata.setIcon(icon);
            notifyicondata.setFlags(NIF_ICON);
            notify(NIM_MODIFY, notifyicondata);
        }
    }

    /**
     * Sets and shows a balloon message.
     *
     * @param value is a container of the ballon message attributes.
     */
    public void showMessage(TrayMessage value)
    {
        NotifyIconData notifyicondata = new NotifyIconData(_hWnd, _trayID);
        notifyicondata.setFlags(NIF_INFO);
        notifyicondata.setInfoTitle(value.getTitle());
        notifyicondata.setInfo(value.getMessage());
        notifyicondata.setTimeout(value.getTimeout() * 1000);
        notifyicondata.setInfoFlags(value.getIconType());
        notify(NIM_MODIFY, notifyicondata);
    }

    /**
     * Disposes a tray icon.
     */
    public void dispose()
    {
        if (!_disposed)
        {
            notify(NIM_DELETE, new NotifyIconData(_hWnd, _trayID));
            _messageHandlers.remove(new Integer(_trayID));
            _disposed = true;
        }
    }

    /**
     * Sets up a tooltip for the tray icon.
     *
     * @param tip is a message to be shown in the tooltip.
     */
    public void setToolTip(String tip)
    {
        NotifyIconData notifyicondata = new NotifyIconData(_hWnd, _trayID);
        notifyicondata.setToolTip(tip);
        notifyicondata.setFlags(NIF_TIP);
        notify(NIM_MODIFY, notifyicondata);
    }

    /**
     * Sets up a popup menu for the tray icon. The popup menu activates by
     * right mouse click.
     * <b>NOTE:</b> for correct displaying of the popup menu it is required
     * to set up <i>-Djavax.swing.adjustPopupLocationToFit=false</i> parameter
     * for an application.
     *
     * @param popupMenu the popup menu to be set up.
     */
    public void setPopupMenu(JPopupMenu popupMenu)
    {
        _popupMenu = popupMenu;

        if (_popupWindow == null)
        {
            _popupWindow = new JFrame();
            _popupWindow.setLocation(-300, -300);
            _popupWindow.pack();
            Wnd wnd = new Wnd(_popupWindow);
            wnd.setTopmost(true);
            wnd.setWindowExStyle(Wnd.WS_EX_TOOLWINDOW);
            _popupWindow.setVisible(true);
            _popupWindow.addWindowListener(new WindowAdapter()
            {
                public void windowDeactivated(WindowEvent e)
                {
                    if (_popupMenu != null)
                    {
                        _popupMenu.setVisible(false);
                    }
                }
            });
        }

        if (_popupTrayIconListener == null)
        {
            _popupTrayIconListener = new TrayIconListener()
            {
                public void trayActionPerformed(final long message, final int x, final int y)
                {
                    if (message == Msg.WM_RBUTTONUP)
                    {
                        SwingUtilities.invokeLater(new Runnable()
                        {
                            public void run()
                            {
                                _popupWindow.setVisible(true);
                                if (_popupMenu.getSize().equals(new Dimension(0, 0)))
                                {
                                    int px = x - (int)_popupWindow.getLocation().getX();
                                    int py =  y - (int)_popupWindow.getLocation().getY();
                                    _popupMenu.show(_popupWindow, px, py);
                                    if (x - (int)_popupMenu.getSize().getWidth() >= 0)
                                    {
                                        px = x - (int)_popupMenu.getSize().getWidth();
                                    }
                                    else
                                    {
                                        px = x;
                                    }
                                    if (y - (int)_popupMenu.getSize().getHeight() >= 0)
                                    {
                                        py =  y - (int)_popupMenu.getSize().getHeight();
                                    }
                                    else
                                    {
                                        py =  y;
                                    }
                                    _popupMenu.setLocation(px, py);
                                }
                                else
                                {
                                    int px, py;
                                    if (x - (int)_popupMenu.getSize().getWidth() >= 0)
                                    {
                                        px = x - (int)_popupWindow.getLocation().getX()  - (int)_popupMenu.getSize().getWidth();
                                    }
                                    else
                                    {
                                        px = x - (int)_popupWindow.getLocation().getX();
                                    }
                                    if (y - (int)_popupMenu.getSize().getHeight() >= 0)
                                    {
                                        py =  y - (int)_popupWindow.getLocation().getY() - (int)_popupMenu.getSize().getHeight();
                                    }
                                    else
                                    {
                                        py =  y - (int)_popupWindow.getLocation().getY();
                                    }
                                    _popupMenu.show(_popupWindow, px, py);
                                    _popupMenu.repaint();
                                }
                            }
                        });
                   }
                }
            };
        }

        addTrayListener(_popupTrayIconListener);
    }

    /**
     * Returns the set popup menu for the tray icon or null if it is not present.
     *
     * @return the set popup menu for the tray icon or null if it is not present.
     */
    public JPopupMenu getPopupMenu()
    {
        return _popupMenu;
    }

    /**
     * Removes the tray icon popup menu. 
     */
    public void removePopupMenu()
    {
        if (_popupMenu == null)
        {
            return;
        }
        _popupWindow.dispatchEvent(new WindowEvent(_popupWindow, WindowEvent.WINDOW_CLOSING));
        _popupWindow = null;
        _popupMenu = null;
        removeTrayListener(_popupTrayIconListener);
    }

    /**
     * Adds a specified tray icon listener.
     *
     * @param listener
     */
    public void addTrayListener(TrayIconListener listener)
    {
        _listeners.add(listener);
    }

    /**
     * Adds a standard mouse listener for the tray icon.
     *
     * @param listener
     */
    public void addTrayListener(MouseListener listener)
    {
        _listeners.add(listener);
    }

    /**
     * Adds a balloon events listener for the specified tray icon.
     *
     * @param listener
     */
    public void addTrayListener(BalloonListener listener)
    {
        _listeners.add(listener);
    }

    /**
     * Removes a specified listener.
     *
     * @param listener
     */
    public void removeTrayListener(TrayIconListener listener)
    {
        _listeners.remove(listener);
    }

    /**
     * Removes specified mouse listener.
     *
     * @param listener
     */
    public void removeTrayListener(MouseListener listener)
    {
        _listeners.remove(listener);
    }

    /**
     * Removes specified balloon events listener.
     *
     * @param listener
     */
    public void removeTrayListener(BalloonListener listener)
    {
        _listeners.remove(listener);
    }

    private MouseEvent getMouseEvent(int message, int x, int y)
    {
        MouseEvent mouseEvent;
        switch (message)
        {
            case Msg.WM_LBUTTONDBLCLK:
                mouseEvent = new MouseEvent(this, _trayID, System.currentTimeMillis(),
                                0, x, y, 2, false, MouseEvent.BUTTON1);
                break;
            case Msg.WM_RBUTTONDBLCLK:
                mouseEvent = new MouseEvent(this, _trayID, System.currentTimeMillis(),
                                0, x, y, 2, false, MouseEvent.BUTTON2);
                break;
            case Msg.WM_MBUTTONDBLCLK:
                mouseEvent = new MouseEvent(this, _trayID, System.currentTimeMillis(),
                                0, x, y, 2, false, MouseEvent.BUTTON3);
                break;
            case Msg.WM_LBUTTONDOWN:
            case Msg.WM_LBUTTONUP:
                mouseEvent = new MouseEvent(this, _trayID, System.currentTimeMillis(),
                                0, x, y, 1, false, MouseEvent.BUTTON1);
                break;
            case Msg.WM_RBUTTONDOWN:
            case Msg.WM_RBUTTONUP:
                mouseEvent = new MouseEvent(this, _trayID, System.currentTimeMillis(),
                                0, x, y, 1, false, MouseEvent.BUTTON2);
                break;
            case Msg.WM_MBUTTONDOWN:
            case Msg.WM_MBUTTONUP:
                mouseEvent = new MouseEvent(this, _trayID, System.currentTimeMillis(),
                                0, x, y, 1, false, MouseEvent.BUTTON3);
                break;
            case Msg.WM_MOUSEHOVER:
            case Msg.WM_MOUSELEAVE:
            default:
                mouseEvent = new MouseEvent(this, _trayID, System.currentTimeMillis(),
                                0, x, y, 0, false, 0);
                break;
        }
        return mouseEvent;
    }

    private void onIconMessage(long message, int x, int y)
    {
        for (Iterator i = _listeners.iterator(); i.hasNext();)
        {
            Object anyListener = i.next();
            if (anyListener instanceof TrayIconListener)
            {
                TrayIconListener listener = (TrayIconListener)anyListener;
                listener.trayActionPerformed(message, x, y);
            }
            else if (anyListener instanceof MouseListener)
            {
                MouseListener listener = (MouseListener)anyListener;
                int intMessage = (int)message;
                switch (intMessage)
                {
                    case Msg.WM_LBUTTONDBLCLK:
                    case Msg.WM_RBUTTONDBLCLK:
                    case Msg.WM_MBUTTONDBLCLK:
                        listener.mouseClicked(getMouseEvent(intMessage, x, y));
                        break;
                    case Msg.WM_LBUTTONDOWN:
                    case Msg.WM_RBUTTONDOWN:
                    case Msg.WM_MBUTTONDOWN:
                        listener.mousePressed(getMouseEvent(intMessage, x, y));
                        break;
                    case Msg.WM_LBUTTONUP:
                    case Msg.WM_RBUTTONUP:
                    case Msg.WM_MBUTTONUP:
                        listener.mouseReleased(getMouseEvent(intMessage, x, y));
                        break;
                    case Msg.WM_MOUSEHOVER:
                        listener.mouseEntered(getMouseEvent(intMessage, x, y));
                        break;
                    case Msg.WM_MOUSELEAVE:
                        listener.mouseExited(getMouseEvent(intMessage, x, y));
                        break;
                }
            }
            else if (anyListener instanceof BalloonListener)
            {
                BalloonListener listener = (BalloonListener)anyListener;
                switch ((int)message)
                {
                    case NIN_BALLOONSHOWN:
                        listener.balloonShown(new EventObject(this));
                        break;
                    case NIN_BALLOONHIDE:
                        listener.balloonHide(new EventObject(this));
                        break;
                    case NIN_BALLOONTIMEOUT:
                        listener.balloonTimeOut(new EventObject(this));
                        break;
                    case NIN_BALLOONUSERCLICK:
                        listener.balloonUserClick(new EventObject(this));
                        break;
                }
            }
        }
    }

    /**
     * Shows or hides an icon in the system tray.
     *
     * @param visible
     */
    public void setVisible(boolean visible)
    {
        if (_visible != visible)
        {
            _visible = visible;

            NotifyIconData notifyicondata = new NotifyIconData(_hWnd, _trayID);

            if (_hidingAvailable)
            {
                notifyicondata.setFlags(NIF_STATE);
                notifyicondata.setState(visible ? 0 : NIS_HIDDEN);
                notifyicondata.setStateMask(NIS_HIDDEN);
                notify(NIM_MODIFY, notifyicondata);
            }
            else
            {
                notifyicondata.setFlags(NIF_ICON | NIF_MESSAGE | NIF_TIP | NIF_INFO);
                notifyicondata.setCallbackMessage(WM_TRAY);
                if (visible)
                {
                    if (_icon != null && !_icon.isNull())
                    {
                        notifyicondata.setIcon(_icon);
                    }
                }
                notify(visible ? NIM_ADD : NIM_DELETE, notifyicondata);
            }
        }
    }

    private static void createEmptyNativeWindow()
    {
        synchronized (_trayWindowLock)
        {
            WndClass wndClass = new WndClass(new TrayIconWindowProc(), CLASS_NAME);
            wndClass.register();

            Wnd hWnd = Wnd.createWindow(CLASS_NAME);
            _hWnd = hWnd.getValue();
            _trayWindowLock.notify();
        }
    }

    private static class TrayIconWindowProc extends WindowProc
    {
        boolean _timerRuning = false;
        boolean _mouseIn = false;
        int _mouseX = 0;
        int _mouseY = 0;
        Timer _timer;
        TrayIcon _handler = null;

        Timer getTimer()
        {
            if (_timer == null)
            {
                _timer = new Timer(150, new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        if (_timerRuning)
                        {
                            _timer.stop();
                            _timerRuning = false;
                        }
                        final Point cursorPosition = Cursor.getCursorPosition();
                        int mx = (int)cursorPosition.getX();
                        int my = (int)cursorPosition.getY();
                        if (_handler != null)
                        {
                            if ((mx != _mouseX) || (my != _mouseY))
                            {
                                _handler.onIconMessage(Msg.WM_MOUSELEAVE, mx, my);
                                _mouseIn = false;
                                _mouseY = 0;
                                _mouseX = 0;
                            }
                        }
                    }
                });
            }
            return _timer;
        }

        public void callback()
        {
            final int msg = (int)_msg.getValue();

            if (msg == WM_TRAY)
            {
                final long lParam = _lParam.getValue();
                final long wParam = _wParam.getValue();
                final Point cursorPos = Cursor.getCursorPosition();
                int mx = (int)cursorPos.getX();
                int my = (int)cursorPos.getY();

                Integer id = new Integer((int)wParam);
                _handler = (TrayIcon)_messageHandlers.get(id);

                if (!_mouseIn)
                {
                    if ((_handler != null) && ((mx != _mouseX) || (my != _mouseY)))
                        _handler.onIconMessage(Msg.WM_MOUSEHOVER, mx, my);
                }
                if (lParam == Msg.WM_MOUSEMOVE)
                {
                    final Timer timer = getTimer();
                    if (_timerRuning)
                    {
                        timer.stop();
                        _timerRuning = false;
                    }
                    timer.start();
                    _timerRuning = true;
                }

                _mouseX = mx;
                _mouseY = my;
                if (_handler != null)
                {
                    _handler.onIconMessage(lParam, _mouseX, _mouseY);
                }
                _mouseIn = true;
                _lResult.setValue(0);
            }
            else if (msg == WM_RESTORE_TRAY)
            {
                Iterator iterator = _messageHandlers.keySet().iterator();
                while (iterator.hasNext())
                {
                    TrayIcon trayIcon = (TrayIcon)_messageHandlers.get(iterator.next());
                    NotifyIconData notifyicondata = new NotifyIconData(_hWnd, trayIcon._trayID);
                    notifyicondata.setCallbackMessage(WM_TRAY);
                    notifyicondata.setFlags(NIF_ICON | NIF_MESSAGE | NIF_TIP | NIF_INFO);
                    trayIcon.notify(NIM_ADD, notifyicondata);
                    trayIcon._visible = true;
                    trayIcon.setIcon(trayIcon._icon);
                }
            }
            else
            {
                super.callback();
            }
        }
    }
}

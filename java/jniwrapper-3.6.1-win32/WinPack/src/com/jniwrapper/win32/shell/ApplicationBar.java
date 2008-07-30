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
import com.jniwrapper.util.EnumItem;
import com.jniwrapper.util.Enums;
import com.jniwrapper.win32.Handle;
import com.jniwrapper.win32.Rect;
import com.jniwrapper.win32.IntPtr;

/**
 * The ApplicationBar class sets or retrieves the application bar state
 * implemented by the BarState class.
 *
 * @author Vladimir Kondrashchenko
 */
public class ApplicationBar
{
    /**
     * BarState class is an enumeration of the application bar states such as
     * autohide, always-on-top or both.
     */
    public static class BarState extends EnumItem
    {
        //Autohide off, always-on-top off
        public static final BarState NONE = new BarState(0x0000000);

        //Autohide on, always-on-top off
        public static final BarState AUTOHIDE = new BarState(0x0000001);

        //Always-on-top on, autohide off
        public static final BarState ALWAYSONTOP = new BarState(0x0000002);

        //Autohide and always-on-top both on
        public static final BarState AUTOHIDE_ALWAYSONTOP =
                new BarState(0x0000001 | 0x0000002);

        private BarState(long value)
        {
            super((int)value);
        }

        public static BarState create(int value)
        {
            return (BarState)Enums.getItem(BarState.class, value);
        }
    }

    /**
     * AppBarData class is a wrapper for APPBARDATA structure.
     */
    static class AppBarData extends Structure
    {
        private UInt32 _cbSize = new UInt32();
        private Handle _hWnd = new Handle();
        private UInt _uCallbackMessage = new UInt();
        private UInt _uEdge = new UInt();
        private Rect _rc = new Rect();
        private IntPtr _lparam = new IntPtr();

        public AppBarData()
        {
            init(new Parameter[]{_cbSize, _hWnd, _uCallbackMessage, _uEdge, _rc, _lparam}, (short)8);

            _cbSize.setValue(getLength());
        }

        public AppBarData(AppBarData that)
        {
            this();
            initFrom(that);
        }

        public void setHandle(Handle value)
        {
            _hWnd = value;
        }

        public Handle getHandle()
        {
            return _hWnd;
        }

        public void setCallBackMessage(long value)
        {
            _uCallbackMessage.setValue(value);
        }

        public long getCallBackMessage()
        {
            return _uCallbackMessage.getValue();
        }

        public void setEdge(long value)
        {
            _uEdge.setValue(value);
        }

        public long getEdge()
        {
            return _uEdge.getValue();
        }

        public void setRect(Rect value)
        {
            _rc = value;
        }

        public Rect getRect()
        {
            return _rc;
        }

        public void setParam(long value)
        {
            _lparam.setValue(value);
        }

        public long getParam()
        {
            return _lparam.getValue();
        }

        public Object clone()
        {
            return new AppBarData(this);
        }
    }

    private static final String FUNCTION_APP_BAR_MESSAGE = "SHAppBarMessage";

    //Notifies the system that an appbar has been activated.
    private static final long AMB_ACTIVATE = 0x00000006;

    //Retrieves the handle to the autohide appbar associated with a particular edge of the screen.
    private static final long AMB_GETAUTOHIDEBAR = 0x00000007;

    //Retrieves the autohide and always-on-top states of the Microsoft® Windows® taskbar.
    private static final long AMB_GETSTATE = 0x00000004;

    //Retrieves the bounding rectangle of the Windows taskbar.
    private static final long AMB_GETTASKBARPOS = 0x00000005;

    //Registers a new appbar.
    private static final long AMB_NEW = 0x00000000;

    //Requests the size and screen position for an appbar.
    private static final long AMB_QUERYPOS = 0x00000002;

    //Unregisters an appbar.
    private static final long AMB_REMOVE = 0x00000001;

    //Registers or unregisters an autohide appbar for an edge of the screen.
    private static final long AMB_SETAUTOHIDEBAR = 0x00000008;

    //Sets the size and screen position of an appbar.
    private static final long AMB_SETPOS = 0x00000003;

    //Sets the state of the appbar's autohide and always-on-top attributes.
    private static final long AMB_SETSTATE = 0x0000000a;

    //Notifies the system when an appbar's position has changed.
    private static final long AMB_WINDOWPOSCHANGED = 0x00000009;

    public static void setAppBarState(BarState state)
    {
        AppBarData appBarData = new AppBarData();
        appBarData.setParam(state.getValue());
        appBarData.setHandle(new Handle(0));

        Function function = Shell32.getInstance().getFunction(FUNCTION_APP_BAR_MESSAGE);
        function.invoke(null, new LongInt(AMB_SETSTATE), new Pointer(appBarData));
    }

    public static BarState getAppBarState()
    {
        AppBarData appBarData = new AppBarData();

        UInt result = new UInt();
        Function function = Shell32.getInstance().getFunction(FUNCTION_APP_BAR_MESSAGE);
        function.invoke(result, new LongInt(AMB_GETSTATE), new Pointer(appBarData));
        return BarState.create((int)result.getValue());
    }

    public static Rect getAppBarPosition()
    {
        AppBarData appBarData = new AppBarData();

        Function function = Shell32.getInstance().getFunction(FUNCTION_APP_BAR_MESSAGE);
        function.invoke(null, new LongInt(AMB_GETTASKBARPOS), new Pointer(appBarData));
        return appBarData.getRect();
    }
}

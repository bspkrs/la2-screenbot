/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.gdi;

import com.jniwrapper.Function;
import com.jniwrapper.Pointer;
import com.jniwrapper.Int;
import com.jniwrapper.util.EnumItem;
import com.jniwrapper.win32.Handle;
import com.jniwrapper.win32.Point;
import com.jniwrapper.win32.ui.User32;

import java.util.HashMap;
import java.util.Map;

/**
 * This class provides various methods for working with the cursor.
 *
 * @author Serge Piletsky
 */
public class Cursor extends GdiObject
{
    private static final String FUNCTION_GET_CURSOR_POS = "GetCursorPos";
    private static final String FUNCTION_SET_CURSOR_POS = "SetCursorPos";
    private static final String FUNCTION_SetCursor = "SetCursor";
    private static final String FUNCTION_LoadCursor = "LoadCursorW";

    static final Map _cursors = new HashMap();

    {
        _cursors.put(new Integer(java.awt.Cursor.DEFAULT_CURSOR), CursorKind.IDC_ARROW);
        _cursors.put(new Integer(java.awt.Cursor.TEXT_CURSOR), CursorKind.IDC_IBEAM);
        _cursors.put(new Integer(java.awt.Cursor.HAND_CURSOR), CursorKind.IDC_HAND);
        _cursors.put(new Integer(java.awt.Cursor.MOVE_CURSOR), CursorKind.IDC_SIZEALL);
        _cursors.put(new Integer(java.awt.Cursor.WAIT_CURSOR), CursorKind.IDC_WAIT);
        _cursors.put(new Integer(java.awt.Cursor.E_RESIZE_CURSOR), CursorKind.IDC_SIZEWE);
        _cursors.put(new Integer(java.awt.Cursor.N_RESIZE_CURSOR), CursorKind.IDC_SIZENS);
        _cursors.put(new Integer(java.awt.Cursor.NE_RESIZE_CURSOR), CursorKind.IDC_SIZENESW);
        _cursors.put(new Integer(java.awt.Cursor.NW_RESIZE_CURSOR), CursorKind.IDC_SIZENWSE);
        _cursors.put(new Integer(java.awt.Cursor.SE_RESIZE_CURSOR), CursorKind.IDC_SIZENWSE);
        _cursors.put(new Integer(java.awt.Cursor.SW_RESIZE_CURSOR), CursorKind.IDC_SIZENESW);
        _cursors.put(new Integer(java.awt.Cursor.W_RESIZE_CURSOR), CursorKind.IDC_SIZEWE);
        _cursors.put(new Integer(java.awt.Cursor.CROSSHAIR_CURSOR), CursorKind.IDC_CROSS);
    }

    public Cursor()
    {
        super();
    }

    public Cursor(long value)
    {
        super(value);
    }

    /**
     * Creates an instance of the native cursor from an AWT cursor.
     *
     * @param cursor an AWT cursor
     */
    public Cursor(java.awt.Cursor cursor)
    {
        setCursor(cursor);
    }

    /**
     * Returns the cursor position, in screen coordinates.
     *
     * @return the cursor position, in screen coordinates
     */
    public static Point getCursorPosition()
    {
        Point result = new Point();
        final Function function = User32.getInstance().getFunction(FUNCTION_GET_CURSOR_POS);
        function.invoke(null, new Pointer(result));
        return result;
    }

    /**
     * Determines the cursor position, in screen coordinates.
     *
     * @param x horizontal coordinate of the cursor
     * @param y vertical coordinate of the cursor
     * @return true if the function succeeds
     */
    public static boolean setCursorPosition(int x, int y)
    {
        Int result = new Int();
        Int xInt = new Int(x);
        Int yInt = new Int(y);
        final Function function = User32.getInstance().getFunction(FUNCTION_SET_CURSOR_POS);
        function.invoke(result, xInt, yInt);
        return result.getValue() != 0;
    }


    /**
     * Loads the cursor of a specified type.
     *
     * @param cursor type of the cursor
     */
    protected void loadCursor(CursorKind cursor)
    {
        Function loadCursor = User32.getInstance().getFunction(FUNCTION_LoadCursor);
        Handle current = new Handle();
        loadCursor.invoke(this, current, new Pointer.Void(cursor.getValue()));
    }

    /**
     * Sets the cursor by the specified AWT cursor.
     *
     * @param cursor AWT cursor to set
     */
    public void setCursor(java.awt.Cursor cursor)
    {
        Function setCursor = User32.getInstance().getFunction(FUNCTION_SetCursor);
        int type = cursor.getType();
        CursorKind cursorKind = (CursorKind) _cursors.get(new Integer(type));
        if (cursorKind != null)
        {
            loadCursor(cursorKind);
            setCursor.invoke(null, this);
        } else
        {
            if (type == -1)
            {
                String name = cursor.getName();
                if ("Invalid32x32".equals(name))
                {
                    loadCursor(CursorKind.IDC_NO);
                    setCursor.invoke(null, this);
                }
            }
        }
    }

    /**
     * This enumeration (IDC_*) specifies various types of the cursor.
     */
    public static class CursorKind extends EnumItem
    {
        /**
         * Standard arrow cursor.
         */
        public static CursorKind IDC_ARROW = new CursorKind(32512, "IDC_ARROW");
        /**
         * I-beam cursor.
         */
        public static CursorKind IDC_IBEAM = new CursorKind(32513, "IDC_IBEAM");
        /**
         * Hourglass cursor.
         */
        public static CursorKind IDC_WAIT = new CursorKind(32514, "IDC_WAIT");
        /**
         * Crosshair cursor.
         */
        public static CursorKind IDC_CROSS = new CursorKind(32515, "IDC_CROSS");
        /**
         * Vertical arrow cursor.
         */
        public static CursorKind IDC_UPARROW = new CursorKind(32516, "IDC_UPARROW");
        /**
         * Obsolete for applications marked version 4.0 or later. Use IDC_SIZEALL.
         */
        public static CursorKind IDC_SIZE = new CursorKind(32640, "IDC_SIZE");
        /**
         * Obsolete for applications marked version 4.0 or later.
         */
        public static CursorKind IDC_ICON = new CursorKind(32641, "IDC_ICON");
        /**
         * Double-pointed arrow cursor pointing northwest and southeast.
         */
        public static CursorKind IDC_SIZENWSE = new CursorKind(32642, "IDC_SIZENWSE");
        /**
         * Double-pointed arrow cursor pointing northeast and southwest.
         */
        public static CursorKind IDC_SIZENESW = new CursorKind(32643, "IDC_SIZENESW");
        /**
         * Double-pointed arrow cursor pointing west and east.
         */
        public static CursorKind IDC_SIZEWE = new CursorKind(32644, "IDC_SIZEWE");
        /**
         * Double-pointed arrow cursor pointing north and south.
         */
        public static CursorKind IDC_SIZENS = new CursorKind(32645, "IDC_SIZENS");
        /**
         * Four-pointed arrow cursor pointing north, south, east, and west.
         */
        public static CursorKind IDC_SIZEALL = new CursorKind(32646, "IDC_SIZEALL");
        /**
         * Slashed circle cursor.
         */
        public static CursorKind IDC_NO = new CursorKind(32648, "IDC_NO");
        /**
         * Windows 98/Me, Windows 2000/XP: Hand cursor.
         */
        public static CursorKind IDC_HAND = new CursorKind(32649, "IDC_HAND");
        /**
         * Standard arrow and small hourglass cursor.
         */
        public static CursorKind IDC_APPSTARTING = new CursorKind(32650, "IDC_APPSTARTING");
        /**
         * Arrow and question mark cursor.
         */
        public static CursorKind IDC_HELP = new CursorKind(32651, "IDC_HELP");

        private String _name;

        private CursorKind(int value, String name)
        {
            super(value);
            _name = name;
        }

        public String toString()
        {
            return _name;
        }
    }

    /**
     * CursorPositionCode class represents the enumeration of the mouse position codes.
     */
    public static class CursorPositionCode extends EnumItem
    {
        public static final CursorPositionCode ERROR = new CursorPositionCode(-2);
        public static final CursorPositionCode TRANSPARENT = new CursorPositionCode(-1);
        public static final CursorPositionCode NOWHERE = new CursorPositionCode(0);
        public static final CursorPositionCode CLIENT = new CursorPositionCode(1);
        public static final CursorPositionCode CAPTION = new CursorPositionCode(2);
        public static final CursorPositionCode SYSMENU = new CursorPositionCode(3);
        public static final CursorPositionCode GROWBOX = new CursorPositionCode(4);
        public static final CursorPositionCode SIZE = GROWBOX;
        public static final CursorPositionCode MENU = new CursorPositionCode(5);
        public static final CursorPositionCode HSCROLL = new CursorPositionCode(6);
        public static final CursorPositionCode VSCROLL = new CursorPositionCode(7);
        public static final CursorPositionCode MINBUTTON = new CursorPositionCode(8);
        public static final CursorPositionCode MAXBUTTON = new CursorPositionCode(9);
        public static final CursorPositionCode LEFT = new CursorPositionCode(10);
        public static final CursorPositionCode RIGHT = new CursorPositionCode(11);
        public static final CursorPositionCode TOP = new CursorPositionCode(12);
        public static final CursorPositionCode TOPLEFT = new CursorPositionCode(13);
        public static final CursorPositionCode TOPRIGHT = new CursorPositionCode(14);
        public static final CursorPositionCode BOTTOM = new CursorPositionCode(15);
        public static final CursorPositionCode BOTTOMLEFT = new CursorPositionCode(16);
        public static final CursorPositionCode BOTTOMRIGHT = new CursorPositionCode(17);
        public static final CursorPositionCode BORDER = new CursorPositionCode(18);
        public static final CursorPositionCode REDUCE = MINBUTTON;
        public static final CursorPositionCode ZOOM = MAXBUTTON;
        public static final CursorPositionCode SIZEFIRST = LEFT;
        public static final CursorPositionCode SIZELAST = BOTTOMRIGHT;
        public static final CursorPositionCode OBJECT = new CursorPositionCode(19);
        public static final CursorPositionCode CLOSE = new CursorPositionCode(20);
        public static final CursorPositionCode HELP = new CursorPositionCode(21);

        private CursorPositionCode(int value)
        {
            super(value);
        }
    }
}

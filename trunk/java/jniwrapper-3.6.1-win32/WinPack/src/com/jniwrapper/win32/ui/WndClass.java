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

/**
 * This class represents WNDCLASS structure.
 * 
 * @author Alexander Kireyev
 * @author Alexander Evsukov
 */
public class WndClass extends Structure
{
    public static final int CS_VREDRAW = 0x0001;
    public static final int CS_HREDRAW = 0x0002;
    public static final int CS_DBLCLKS = 0x0008;
    public static final int CS_OWNDC = 0x0020;
    public static final int CS_CLASSDC = 0x0040;
    public static final int CS_PARENTDC = 0x0080;
    public static final int CS_NOCLOSE = 0x0200;
    public static final int CS_SAVEBITS = 0x0800;
    public static final int CS_BYTEALIGNCLIENT = 0x1000;
    public static final int CS_BYTEALIGNWINDOW = 0x2000;
    public static final int CS_GLOBALCLASS = 0x4000;
    public static final int CS_IME = 0x00010000;

    public static final int CW_USEDEFAULT = 0x80000000;

    private static final FunctionName FUNCTION_REGISTER_CLASS = new FunctionName("RegisterClass");

    protected Callback _lpfnWndProc;
    protected UInt32 _style = new UInt32();
    protected Int _cbClsExtra = new Int();
    protected Int _cbWndExtra = new Int();

    protected Handle _hInstance = new Handle();
    protected Handle _hIcon = new Handle();
    protected Handle _hCursor = new Handle();
    protected Handle _hbrBackground = new Handle();
    protected Str _lpszClassName;

    public WndClass(Callback windowProc, String className)
    {
        _lpszClassName = new Str();
        _lpfnWndProc = windowProc;
        _lpszClassName.setValue(className);
        _cbClsExtra.setValue(0);
        _cbWndExtra.setValue(0);
        _hInstance.setValue(0);
        _hIcon.setValue(0);
        _hCursor.setValue(0);
        _hbrBackground.setValue(0);
        init(new Parameter[]{_style, _lpfnWndProc, _cbClsExtra, _cbWndExtra,
                             _hInstance, _hIcon, _hCursor, _hbrBackground,
                             new Pointer(null, true), new Pointer(_lpszClassName)}, (short)8);
    }

    public WndClass(WndClass that)
    {
        this(that._lpfnWndProc, that._lpszClassName.getValue());
        initFrom(that);
    }

    public Int16 register()
    {
        final Function function = User32.getInstance().getFunction(FUNCTION_REGISTER_CLASS.toString());
        Int16 atom = new Int16();
        long errorCode = function.invoke(atom, new Pointer(this));
        if (atom.getValue() == 0)
        {
            throw new LastErrorException(errorCode, "Failed to register window class: " + _lpszClassName.getValue() + ".");
        }
        return atom;
    }

    public void setStyle(UInt32 style)
    {
        setStyle(style.getValue());
    }

    public void setStyle(long style)
    {
        _style.setValue(style);
    }

    public void setBackground(Handle bkground)
    {
        _hbrBackground.setValue(bkground.getValue());
    }

    public Object clone()
    {
        return new WndClass(this);
    }
}

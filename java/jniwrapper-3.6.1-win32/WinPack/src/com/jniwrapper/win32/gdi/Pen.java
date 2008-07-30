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
import com.jniwrapper.Parameter;
import com.jniwrapper.Int;

public class Pen extends GdiObject
{
    public static final int PS_SOLID = 0;
    public static final int PS_DASH = 1;
    public static final int PS_DOT = 2;
    public static final int PS_DASHDOT = 3;
    public static final int PS_DASHDOTDOT = 4;
    public static final int PS_NULL = 5;
    public static final int PS_INSIDEFRAME = 6;
    public static final int PS_USERSTYLE = 7;
    public static final int PS_ALTERNATE = 8;
    public static final int PS_STYLE_MASK = 0x0000000F;

    public static final int PS_ENDCAP_ROUND = 0x00000000;
    public static final int PS_ENDCAP_SQUARE = 0x00000100;
    public static final int PS_ENDCAP_FLAT = 0x00000200;
    public static final int PS_ENDCAP_MASK = 0x00000F00;

    public static final int PS_JOIN_ROUND = 0x00000000;
    public static final int PS_JOIN_BEVEL = 0x00001000;
    public static final int PS_JOIN_MITER = 0x00002000;
    public static final int PS_JOIN_MASK = 0x0000F000;

    public static final int PS_COSMETIC = 0x00000000;
    public static final int PS_GEOMETRIC = 0x00010000;
    public static final int PS_TYPE_MASK = 0x000F0000;
            
            
    private static final String FUNCTION_CREATE_PEN = "CreatePen";

    public Pen(long value)
    {
        super(value);
    }

    public Pen()
    {
    }

    public static Pen createPen(int penStyle, int penWidth, ColorRef penColor)
    {
        Pen result = new Pen();

        Function function = Gdi32.getInstance().getFunction(FUNCTION_CREATE_PEN);
        function.invoke(result, new Parameter[] {
            new Int(penStyle),
            new Int(penWidth),
            penColor
        });

        return result;
    }
}

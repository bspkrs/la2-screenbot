/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.gdi;

import com.jniwrapper.*;
import com.jniwrapper.util.EnumItem;
import com.jniwrapper.win32.LastErrorException;
import com.jniwrapper.win32.ui.User32;

import java.awt.*;

/**
 * SystemColor class provides the ability to read and write system colors. Also,
 * represents enumeration of predefined system colors.
 */
public class SystemColor extends EnumItem
{
    public static final SystemColor SCROLLBAR         = new SystemColor(0);
    public static final SystemColor BACKGROUND        = new SystemColor(1);
    public static final SystemColor ACTIVECAPTION     = new SystemColor(2);
    public static final SystemColor INACTIVECAPTION   = new SystemColor(3);
    public static final SystemColor MENU              = new SystemColor(4);
    public static final SystemColor WINDOW            = new SystemColor(5);
    public static final SystemColor WINDOWFRAME       = new SystemColor(6);
    public static final SystemColor MENUTEXT          = new SystemColor(7);
    public static final SystemColor WINDOWTEXT        = new SystemColor(8);
    public static final SystemColor CAPTIONTEXT       = new SystemColor(9);
    public static final SystemColor ACTIVEBORDER      = new SystemColor(10);
    public static final SystemColor INACTIVEBORDER    = new SystemColor(11);
    public static final SystemColor APPWORKSPACE      = new SystemColor(12);
    public static final SystemColor HIGHLIGHT         = new SystemColor(13);
    public static final SystemColor HIGHLIGHTTEXT     = new SystemColor(14);
    public static final SystemColor BTNFACE           = new SystemColor(15);
    public static final SystemColor BTNSHADOW         = new SystemColor(16);
    public static final SystemColor GRAYTEXT          = new SystemColor(17);
    public static final SystemColor BTNTEXT           = new SystemColor(18);
    public static final SystemColor INACTIVECAPTIONTEXT = new SystemColor(19);
    public static final SystemColor BTNHIGHLIGHT      = new SystemColor(20);

    public static final SystemColor _3DDKSHADOW       = new SystemColor(21);
    public static final SystemColor _3DLIGHT          = new SystemColor(22);
    public static final SystemColor INFOTEXT          = new SystemColor(23);
    public static final SystemColor INFOBK            = new SystemColor(24);

    public static final SystemColor HOTLIGHT                = new SystemColor(26);
    public static final SystemColor GRADIENTACTIVECAPTION   = new SystemColor(27);
    public static final SystemColor GRADIENTINACTIVECAPTION = new SystemColor(28);

    public static final SystemColor DESKTOP           = BACKGROUND;
    public static final SystemColor _3DFACE           = BTNFACE;
    public static final SystemColor _3DSHADOW         = BTNSHADOW;
    public static final SystemColor _3DHIGHLIGHT      = BTNHIGHLIGHT;
    public static final SystemColor _3DHILIGHT        = BTNHIGHLIGHT;
    public static final SystemColor BTNHILIGHT        = BTNHIGHLIGHT;

    static final String FUNCTION_GET_SYS_COLOR = "GetSysColor";
    static final String FUNCTION_SET_SYS_COLOR = "SetSysColors";

    private SystemColor(int colorIndex)
    {
        super(colorIndex);
    }

    /**
     * 
     * @return system color value.
     */
    public Color getColor()
    {
        return getSysColor(getValue());
    }

    /**
     * Sets a new system color.
     * 
     * @param newColor
     */
    public void setColor(Color newColor)
    {
        setSysColor(getValue(), newColor);
    }

    /**
     * Retrieves a system color by the specified index.
     * 
     * @param colorIndex the index of the color.
     * @return a system color.
     */
    public static Color getSysColor(int colorIndex)
    {
        final Function function = User32.getInstance().getFunction(FUNCTION_GET_SYS_COLOR);
        ColorRef result = new ColorRef();
        function.invoke(result, new Int(colorIndex));
        return result.toColor();
    }

    /**
     * Sets a system color by the specified index.
     * 
     * @param colorIndex the index of the color.
     * @param color a new color.
     */
    public static void setSysColor(int colorIndex, Color color)
    {
        setSysColor(new int[]{colorIndex}, new Color[]{color});
    }

    /**
     * Sets system colors by the specified indexes.
     * 
     * @param colorIndices an array of color indexes.
     * @param colors an array of colors.
     */
    public static void setSysColor(int[] colorIndices, Color[] colors)
    {
        if (colorIndices == null || colors == null || colorIndices.length != colors.length)
            throw new IllegalArgumentException();
        int elementCount = colorIndices.length;
        PrimitiveArray indicesArray = new PrimitiveArray(Int.class, elementCount);
        PrimitiveArray colorsArray = new PrimitiveArray(ColorRef.class, elementCount);
        for (int i = 0; i < elementCount; i++)
        {
            indicesArray.setElement(i, new Int(colorIndices[i]));
            colorsArray.setElement(i, new ColorRef(colors[i]));
        }
        Bool result = new Bool();
        final Function function = User32.getInstance().getFunction(FUNCTION_SET_SYS_COLOR);
        long errorCode = function.invoke(result, new Int(1), new Pointer(indicesArray), new Pointer(colorsArray));
        if (!result.getValue())
        {
            throw new LastErrorException(errorCode);
        }
    }
}

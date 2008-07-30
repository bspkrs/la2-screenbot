/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.gdi;

import com.jniwrapper.UInt32;

import java.awt.*;

/**
 * This class represents COLORREF structure.
 * 
 * @author Serge Piletsky
 */
public class ColorRef extends UInt32
{
    /**
     * This constant indicates incorrect color value.
     */
    static final int CLR_INVALID = 0xFFFFFFFF;

    public ColorRef()
    {
        super();
    }

    public ColorRef(long value)
    {
        super(value);
    }

    public ColorRef(Color color)
    {
        setColor(color);
    }

    /**
     * Sets a new color value.
     * 
     * @param color new color value.
     */
    public void setColor(Color color)
    {
        int rgb = color.getRGB();
        setValue(toBGR(rgb));
    }

    /**
     * Returns an instance of {@link java.awt.Color} converted from the native
     * presentation.
     */
    public Color toColor()
    {
        return fromNativeColor((int)getValue());
    }

    /**
     * Converts color value from RGB to BGR colorspace.
     *
     * @param rgb color value
     * @return bgr color value
     */
    public static int toBGR(int rgb)
    {
        int r = (rgb & 0xFF0000) >> 16;
        int g = rgb & 0xFF00;
        int b = rgb & 0xFF;
        int result =  r | g | b << 16;
        return result;
    }

    /**
     * Converts color value BGR from to RGB colorspace.
     *
     * @param bgr color value
     * @return rgb color value
     */
    public static int toRGB(int bgr)
    {
        int b = (bgr & 0xFF0000) >> 16;
        int g = bgr & 0xFF00;
        int r = bgr & 0xFF;
        int result =  b | g | r << 16;
        return result;
    }

    /**
     * Converts a Java color to a native color presentation.
     * 
     * @param color is Java color.
     * @return native color presentation.
     */
    public static int toNativeColor(Color color)
    {
        return toBGR(color.getRGB());
    }

    /**
     * Converts a native color to Java color presentation.
     * 
     * @param color is a native color presentation.
     * @return Java color.
     */
    public static Color fromNativeColor(int color)
    {
        return new Color(toRGB(color));
    }

    /**
     * Checks return value correctness.
     *
     * @return result
     */
    public boolean isInvalid()
    {
        long value = getValue();
        return (value == CLR_INVALID);
    }
}

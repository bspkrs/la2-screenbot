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
import com.jniwrapper.win32.Handle;

/**
 * This class represents BITMAP structure.
 *
 * @author Andrew Kharchenko
 * @author Serge Piletsky
 */
public class BitmapStructure extends Structure
{
    /**
     * Specifies bitmap type. This value must be zero.
     */
    private LongInt _bmType = new LongInt();
    /**
     * Specifies bitmap width in pixels. Must be greater than zero.
     */
    private LongInt _bmWidth = new LongInt();
    /**
     * Specifies bitmap height in pixels. Must be greater than zero.
     */
    private LongInt _bmHeight = new LongInt();
    /**
     * Specifies number of bytes in each scan line.
     * The value must be divisible by 2.
     */
    private LongInt _bmWidthBytes = new LongInt();
    /**
     * Specifies color planes count.
     */
    private UInt16 _bmPlanes = new UInt16();
    /**
     * Specifies color depth (4,8,16 bit color quality etc.).
     */
    private UInt16 _bmBitsPixel = new UInt16();
    /**
     * Pointer to bitmap field.
     */
    private Handle _bmBits = new Handle();

    public BitmapStructure()
    {
        init(new Parameter[]{_bmType, _bmWidth, _bmHeight, _bmWidthBytes, _bmPlanes, _bmBitsPixel, _bmBits}, (short)8);
    }

    public BitmapStructure(long width,
                        long height,
                        long widthBytes,
                        long planes,
                        long bitsPixel)
    {
        this();
        setBitmapWidth(width);
        setBitmapHeight(height);
        setBitmapWidthBytes(widthBytes);
        setPlanes(planes);
        setBitsPixel(bitsPixel);
    }

    public BitmapStructure(BitmapStructure that)
    {
        this();
        initFrom(that);
    }

    /**
     * Sets bitmap width in pixels.
     * @param width
     */
    public void setBitmapWidth(long width)
    {
        _bmWidth.setValue(width);
    }

    /**
     * Returns bitmap width in pixels.
     * @return  bitmap width in pixels
     */
    public long getBitmapWidth()
    {
        return _bmWidth.getValue();
    }

    /**
     * Sets bitmap height in pixels.
     * @param height
     */
    public void setBitmapHeight(long height)
    {
        _bmHeight.setValue(height);
    }

    /**
     * Returns bitmap height in pixels.
     * @return bitmap height in pixels
     */
    public long getBitmapHeight()
    {
        return _bmHeight.getValue();
    }

    /**
     * Sets number of bytes in bitmap scan line.
     * @param width
     */
    public void setBitmapWidthBytes(long width)
    {
        _bmWidthBytes.setValue(width);
    }

    /**
     * Return number of bytes in bitmap scan line.
     * @return bitmap scan line in bytes
     */
    public long getBitmapWidthBytes()
    {
        return _bmWidthBytes.getValue();
    }

    /**
     * Sets bitmap color planes count.
     * @param count of color planes
     */
    public void setPlanes(long count)
    {
        _bmPlanes.setValue(count);
    }

    /**
     * Returns bitmap color planes count.
     * @return color planes count
     */
    public long getPlanes()
    {
        return _bmPlanes.getValue();
    }

    /**
     * Sets bitmap color depth (4,8,16 etc.)
     * @param count
     */
    public void setBitsPixel(long count)
    {
        _bmBitsPixel.setValue(count);
    }

    /**
     * Returns bitmap color depth.
     * @return  color depth
     */
    public long getBitsPixel()
    {
        return _bmBitsPixel.getValue();
    }

    /**
     * Returns handle to bitmap field.
     * @return handle to bitmap
     */
    public Handle getBits()
    {
        return _bmBits;
    }

    /**
     * Returns copy of the BitmapStructure object
     * @return copy of the BitmapStructure object
     */
    public Object clone()
    {
        return new BitmapStructure(this);
    }
}

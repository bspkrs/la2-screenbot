/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.gdi;

import com.jniwrapper.Parameter;
import com.jniwrapper.PrimitiveArray;
import com.jniwrapper.Structure;

/**
 * This class represents BITMAPINFO structure.
 *
 * @author Serge Piletsky
 */
public class BitmapInfo extends Structure
{
    private BitmapInfoHeader _header = new BitmapInfoHeader();
    private PrimitiveArray _colors;

    public BitmapInfo()
    {
        this(0);
    }

    public BitmapInfo(int colors)
    {
        _colors = new PrimitiveArray(RGBQuad.class, colors);
        init(new Parameter[]{_header, _colors});
    }

    public BitmapInfo(BitmapInfo that)
    {
        this();
        initFrom(that);
    }

    public BitmapInfoHeader getBitmapInfoHeader()
    {
        return _header;
    }

    public PrimitiveArray getColors()
    {
        return _colors;
    }

    void setColors(PrimitiveArray colors)
    {
        _colors = colors;
    }

    void setBitmapInfoHeader(BitmapInfoHeader bitmapInfoHeader)
    {
        _header = bitmapInfoHeader;
    }

    public Object clone()
    {
        return new BitmapInfo(this);
    }
}
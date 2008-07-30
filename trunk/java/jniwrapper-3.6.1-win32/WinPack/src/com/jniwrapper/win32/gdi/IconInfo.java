/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.gdi;

import com.jniwrapper.Int;
import com.jniwrapper.Parameter;
import com.jniwrapper.Structure;
import com.jniwrapper.UInt32;

/**
 * This class represents ICONINFO structure.
 *
 * @author Serge Piletsky
 */
public class IconInfo extends Structure
{
    private Int _icon = new Int();
    private UInt32 _xHotSpot = new UInt32();
    private UInt32 _yHotSpot = new UInt32();
    private Bitmap _maskBitmap;
    private Bitmap _colorBitmap;

    public IconInfo()
    {
        _maskBitmap = new DDBitmap();
        _colorBitmap = new DDBitmap();
        init(new Parameter[]{_icon, _xHotSpot, _yHotSpot, _maskBitmap, _colorBitmap, }, (short)8);
    }

    public IconInfo(Bitmap maskBitmap, Bitmap colorBitmap)
    {
        _maskBitmap = maskBitmap;
        _colorBitmap = colorBitmap;
        init(new Parameter[]{_icon, _xHotSpot, _yHotSpot, _maskBitmap, _colorBitmap, }, (short)8);
    }

    public IconInfo(IconInfo that)
    {
        this();
        initFrom(that);
    }

    public Bitmap getMaskBitmap()
    {
        return _maskBitmap;
    }

    public Bitmap getColorBitmap()
    {
        return _colorBitmap;
    }

    public boolean isIcon()
    {
        return _icon.getValue() != 0;
    }

    public boolean isCursor()
    {
        return !isIcon();
    }

    public Object clone()
    {
        return new IconInfo(this);
    }
}

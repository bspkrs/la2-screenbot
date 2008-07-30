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
import com.jniwrapper.util.Enums;

/**
 * This class represents BITMAPINFOHEADER structure.
 *
 * @author Serge Piletsky
 */
public class BitmapInfoHeader extends Structure
{
    private UInt32 _size = new UInt32();
    private LongInt _width = new LongInt();
    private LongInt _height = new LongInt();
    private UInt16 _planes = new UInt16();
    private UInt16 _bitCount = new UInt16();
    private UInt32 _compression = new UInt32();
    private UInt32 _sizeImage = new UInt32();
    private LongInt _XPelsPerMeter = new LongInt();
    private LongInt _YPelsPerMeter = new LongInt();
    private UInt32 _clrUsed = new UInt32();
    private UInt32 _clrImportant = new UInt32();

    public BitmapInfoHeader()
    {
        init(new Parameter[]{_size, _width, _height, _planes, _bitCount,
                             _compression, _sizeImage, _XPelsPerMeter, _YPelsPerMeter,
                             _clrUsed, _clrImportant});
        _size.setValue(getLength());
    }

    public BitmapInfoHeader(BitmapInfoHeader that)
    {
        this();
        initFrom(that);
    }

    public String toString()
    {
        return "BitmapInfoHeader: [SIZE=" + getLength() +  
                "; size=" + _size.getValue() +
                "; width=" + _width.getValue() +
                "; height=" + _height.getValue() +
                "; planes=" + _planes.getValue() +
                "; bitCount=" + _bitCount.getValue() +
                "; compression=" + _compression.getValue() +
                "; sizeImage=" + _sizeImage.getValue() +
                "; XPelsPerMeter=" + _XPelsPerMeter.getValue() +
                "; YPelsPerMeter=" + _YPelsPerMeter.getValue() +
                "; clrUsed=" + _clrUsed.getValue() +
                "; clrImportant=" + _clrImportant.getValue() +
                "]";
    }

    public long getWidth()
    {
        return _width.getValue();
    }

    public void setWidth(long value)
    {
        _width.setValue(value);
    }

    public long getHeight()
    {
        return _height.getValue();
    }

    public void setHeight(long value)
    {
        _height.setValue(value);
    }

    public long getPlanes()
    {
        return _planes.getValue();
    }

    public void setPlanes(long value)
    {
        _planes.setValue(value);
    }

    public long getBitCount()
    {
        return _bitCount.getValue();
    }

    public void setBitCount(long value)
    {
        _bitCount.setValue(value);
    }

    public Bitmap.Compression getCompression()
    {
        final int value = (int)_compression.getValue();
        final Bitmap.Compression result = (Bitmap.Compression)Enums.getItem(Bitmap.Compression.class, value);
        return result;
    }

    public void setCompression(Bitmap.Compression compression)
    {
        _compression.setValue(compression.getValue());
    }

    public long getSizeImage()
    {
        return _sizeImage.getValue();
    }

    public void setSizeImage(long value)
    {
        _sizeImage.setValue(value);
    }

    public long getClrUsed()
    {
        return _clrUsed.getValue();
    }

    public void setClrUsed(long clrUsed)
    {
        _clrUsed.setValue(clrUsed);
    }

    public long getClrImportant()
    {
        return _clrImportant.getValue();
    }

    public void setClrImportant(long clrImportant)
    {
        _clrImportant.setValue(clrImportant);
    }

    public void setXPelsPerMeter(long value)
    {
        _XPelsPerMeter.setValue(value);
    }

    public long getXPelsPerMeter()
    {
        return _XPelsPerMeter.getValue();
    }

    public void setYPelsPerMeter(long value)
    {
        _YPelsPerMeter.setValue(value);
    }

    public long getYPelsPerMeter()
    {
        return _YPelsPerMeter.getValue();
    }

    public Object clone()
    {
        return new BitmapInfoHeader(this);
    }
}

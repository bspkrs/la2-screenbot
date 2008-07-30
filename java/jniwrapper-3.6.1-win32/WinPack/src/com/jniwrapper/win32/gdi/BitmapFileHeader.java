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
import com.jniwrapper.Structure;
import com.jniwrapper.UInt16;
import com.jniwrapper.UInt32;

/**
 * This class represents BITMAPFILEHEADER structure.
 */
public class BitmapFileHeader extends Structure
{
    public static final int BITMAP_TYPE = 0x4D42;

    /**
     * Specifies the file type, must be BM.
     */
    UInt16 _type = new UInt16(BITMAP_TYPE);
    /**
     * Specifies bitmap file size, in bytes.
     */
    UInt32 _size = new UInt32();
    /**
     * Reserved. Must be zero.
     */
    UInt16 _reserved1 = new UInt16();
    /**
     * Reserved. Must be zero.
     */
    UInt16 _reserved2 = new UInt16();
    /**
     * Specifies the offset, in bytes, from the beginning of
     * the BITMAPFILEHEADER structure to the bitmap bits.
     */
    UInt32 _offBits = new UInt32();

    public BitmapFileHeader()
    {
        init(new Parameter[]
        {
            _type,
            _size,
            _reserved1,
            _reserved2,
            _offBits
        });
    }

    public BitmapFileHeader(BitmapFileHeader that)
    {
        this();
        initFrom(that);
    }

    /**
     * Returns bitmap type value.
     *
     * @return bitmap type
     */
    public long getType()
    {
        return _type.getValue();
    }

    /**
     * Sets bitmap type.
     *
     * @param type
     */
    public void setType(long type)
    {
        _type.setValue(type);
    }

    /**
     * Returns bitmap file size.
     *
     * @return size of bitmap file
     */
    public long getSize()
    {
        return _size.getValue();
    }

    /**
     * Sets bitmap file size.
     *
     * @param size of bitmap file
     */
    public void setSize(long size)
    {
        _size.setValue(size);
    }

    public long getReserved1()
    {
        return _reserved1.getValue();
    }

    public void setReserved1(long reserved1)
    {
        _reserved1.setValue(reserved1);
    }

    public long getReserved2()
    {
        return _reserved2.getValue();
    }

    public void setReserved2(long reserved2)
    {
        _reserved2.setValue(reserved2);
    }

    /**
     * Sets offset, in bytes, from the beginning of
     * the BITMAPFILEHEADER structure to the bitmap bits.
     *
     * @return offset
     */
    public long getOffBits()
    {
        return _offBits.getValue();
    }

    /**
     * Returns offset, in bytes, from the beginning of
     * the BITMAPFILEHEADER structure to the bitmap bits.
     *
     * @param offBits
     */
    public void setOffBits(long offBits)
    {
        _offBits.setValue(offBits);
    }

    public Object clone()
    {
        return new BitmapFileHeader(this);
    }
}

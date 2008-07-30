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
 * This class represents DIBSECTION structure.
 *
 * @author Serge Piletsky
 */
public class DibSection extends Structure
{
    private BitmapStructure _bitmap = new BitmapStructure();
    private BitmapInfoHeader _bitmapInfoHeader = new BitmapInfoHeader();
    private PrimitiveArray _bitFields = new PrimitiveArray(UInt32.class, 3);
    private Handle _selection = new Handle();
    private UInt32 _offset = new UInt32();

    public DibSection()
    {
        init(new Parameter[]{_bitmap, _bitmapInfoHeader, _bitFields, _selection, _offset}, (short)8);
    }

    public DibSection(DibSection that)
    {
        this();
        initFrom(that);
    }

    public BitmapStructure getBitmap()
    {
        return _bitmap;
    }

    public BitmapInfoHeader getBitmapInfoHeader()
    {
        return _bitmapInfoHeader;
    }

    public long getRedMask()
    {
        return ((UInt32)_bitFields.getElement(0)).getValue();
    }

    public long getGreenMask()
    {
        return ((UInt32)_bitFields.getElement(1)).getValue();
    }

    public long getBlueMask()
    {
        return ((UInt32)_bitFields.getElement(2)).getValue();
    }

    public Object clone()
    {
        return new DibSection(this);
    }
}
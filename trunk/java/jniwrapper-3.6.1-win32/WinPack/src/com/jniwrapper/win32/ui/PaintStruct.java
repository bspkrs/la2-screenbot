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
import com.jniwrapper.win32.Rect;
import com.jniwrapper.win32.gdi.DC;

/**
 * This class represents PAINTSTRUCT structure.
 *
 * @author Serge Piletsky
 */
public class PaintStruct extends Structure
{
    private DC _hdc = new DC();
    private IntBool _erase = new IntBool();
    private Rect _rcPaint = new Rect();
    private IntBool _restore = new IntBool();
    private IntBool _incUpdate = new IntBool();
    private PrimitiveArray _rgbReserved = new PrimitiveArray(UInt8.class, 32);

    public PaintStruct()
    {
        init(new Parameter[] {_hdc, _erase, _rcPaint, _restore, _incUpdate, _rgbReserved}, (short)8);
    }

    public PaintStruct(PaintStruct that)
    {
        this();
        initFrom(that);
    }

    public Object clone()
    {
        return new PaintStruct(this);
    }
}

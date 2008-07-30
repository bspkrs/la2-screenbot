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
import com.jniwrapper.UInt8;

// TODO [leha]: comments
/**
 * PaletteEntry class represents PALETTEENTRY structure.
 */
public class PaletteEntry extends Structure
{
    private UInt8 _peRed;
    private UInt8 _peGreen;
    private UInt8 _peBlue;
    private UInt8 _peFlags;

    public PaletteEntry()
    {
        _peRed = new UInt8();
        _peGreen = new UInt8();
        _peBlue = new UInt8();
        _peFlags = new UInt8();

        init();
    }

    public PaletteEntry(PaletteEntry that)
    {
        _peRed = (UInt8)that._peRed.clone();
        _peGreen = (UInt8)that._peGreen.clone();
        _peBlue = (UInt8)that._peBlue.clone();
        _peFlags = (UInt8)that._peFlags.clone();

        init();
    }

    private void init()
    {
        init(
                new Parameter[]{
                    _peRed,
                    _peGreen,
                    _peBlue,
                    _peFlags
                },
                (short)1
        );
    }

    public UInt8 getPeRed()
    {
        return _peRed;
    }

    public UInt8 getPeGreen()
    {
        return _peGreen;
    }

    public UInt8 getPeBlue()
    {
        return _peBlue;
    }

    public UInt8 getPeFlags()
    {
        return _peFlags;
    }

    public Object clone()
    {
        return new PaletteEntry(this);
    }
}

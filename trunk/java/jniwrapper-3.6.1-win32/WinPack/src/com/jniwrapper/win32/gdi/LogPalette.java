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

// TODO [leha]: comments
/**
 * The LogPalette class represents the LOGPALETTE structure.
 */
public class LogPalette extends Structure
{
    private UInt16 _palVersion;
    private UInt16 _palNumEntries;
    private Pointer _palPalEntry;

    public LogPalette()
    {
        _palVersion = new UInt16();
        _palNumEntries = new UInt16();
        _palPalEntry = new Pointer(new PaletteEntry());

        init();
    }

    public LogPalette(LogPalette that)
    {
        _palVersion = (UInt16)that._palVersion.clone();
        _palNumEntries = (UInt16)that._palNumEntries.clone();
        _palPalEntry = (Pointer)that._palPalEntry.clone();

        init();
    }

    private void init()
    {
        init(
                new Parameter[]{
                    _palVersion,
                    _palNumEntries,
                    _palPalEntry
                }, (short)4
        );
    }

    public UInt16 getPalVersion()
    {
        return _palVersion;
    }

    public UInt16 getPalNumEntries()
    {
        return _palNumEntries;
    }

    public Pointer getPalPalEntry()
    {
        return _palPalEntry;
    }

    public Object clone()
    {
        return new LogPalette(this);
    }
}

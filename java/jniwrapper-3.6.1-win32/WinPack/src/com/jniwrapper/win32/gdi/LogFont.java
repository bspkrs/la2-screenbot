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

/** The LogFont class represents the LOGFONT structure.
 */
public class LogFont extends Structure
{
    protected LongInt lfHeight = new LongInt();
    protected LongInt lfWidth = new LongInt();
    protected LongInt lfEscapement = new LongInt();
    protected LongInt lfOrientation = new LongInt();
    protected LongInt lfWeight = new LongInt();
    protected UInt8 lfItalic = new UInt8();
    protected UInt8 lfUnderline = new UInt8();
    protected UInt8 lfStrikeOut = new UInt8();
    protected UInt8 lfCharSet = new UInt8();
    protected UInt8 lfOutPrecision = new UInt8();
    protected UInt8 lfClipPrecision = new UInt8();
    protected UInt8 lfQuality = new UInt8();
    protected UInt8 lfPitchAndFamily = new UInt8();
    protected Str lfFaceName = new Str(32);

    public LogFont()
    {
        init(new Parameter[] {
            lfHeight, lfWidth, lfEscapement, lfOrientation, lfWeight,
            lfItalic, lfUnderline, lfStrikeOut, lfCharSet,
            lfOutPrecision, lfClipPrecision,
            lfQuality, lfPitchAndFamily,
            lfFaceName
        });
    }

    public LogFont(LogFont that)
    {
        this();
        initFrom(that);
    }

    public LongInt getLfHeight()
    {
        return lfHeight;
    }

    public LongInt getLfWidth()
    {
        return lfWidth;
    }

    public LongInt getLfEscapement()
    {
        return lfEscapement;
    }

    public LongInt getLfOrientation()
    {
        return lfOrientation;
    }

    public LongInt getLfWeight()
    {
        return lfWeight;
    }

    public UInt8 getLfItalic()
    {
        return lfItalic;
    }

    public UInt8 getLfUnderline()
    {
        return lfUnderline;
    }

    public UInt8 getLfStrikeOut()
    {
        return lfStrikeOut;
    }

    public UInt8 getLfCharSet()
    {
        return lfCharSet;
    }

    public UInt8 getLfOutPrecision()
    {
        return lfOutPrecision;
    }

    public UInt8 getLfClipPrecision()
    {
        return lfClipPrecision;
    }

    public UInt8 getLfQuality()
    {
        return lfQuality;
    }

    public UInt8 getLfPitchAndFamily()
    {
        return lfPitchAndFamily;
    }

    public Str getLfFaceName()
    {
        return lfFaceName;
    }

    public Object clone()
    {
        return new LogFont(this);
    }
}

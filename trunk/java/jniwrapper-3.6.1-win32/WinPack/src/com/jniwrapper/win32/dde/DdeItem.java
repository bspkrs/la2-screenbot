/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.dde;

/**
 * This class is provided for description of item data.
 *
 * @author Vladimir Kondrashchenko
 */
public class DdeItem
{
    /**
     * Specifies ANSI text format.
     */
    public static final long CF_TEXT = 1;

    /**
     * Specifies a handle to bitmap.
     */
    public static final long CF_BITMAP = 2;

    /**
     * Specifies a handle to a metafile picture format.
     */
    public static final long CF_METAFILEPICT = 3;

    /**
     * Specifies Microsoft Symbolic Link (SYLK) format.
     */
    public static final long CF_SYLK = 4;

    /**
     * Specifies Software Arts' Data Interchange Format.
     */
    public static final long CF_DIF = 5;

    /**
     * Specifies Tagged-image file format.
     */
    public static final long CF_TIFF = 6;

    /**
     * Specifies text format containing characters in the OEM character set.
     */
    public static final long CF_OEMTEXT = 7;

    /**
     * Specifies memory object containing a BITMAPINFO structure.
     */
    public static final long CF_DIB = 8;

    /**
     * Specifies handle to a color palette.
     */
    public static final long CF_PALETTE = 9;

    /**
     * Specifies data for the pen extensions.
     */
    public static final long CF_PENDATA = 10;

    /**
     * Specifies audio data more complex than CF_WAVE.
     */
    public static final long CF_RIFF = 11;

    /**
     * Specifies audio data in one of the standard wave formats.
     */
    public static final long CF_WAVE = 12;

    /**
     * Specifies Unicode text format.
     */
    public static final long CF_UNICODETEXT = 13;

    /**
     * Specifies handle to an enhanced metafile.
     */
    public static final long CF_ENHMETAFILE = 14;

    /**
     * Specifies handle to type HDROP that identifies a list of files.
     */
    public static final long CF_HDROP = 15;

    /**
     * Specifies which character set is used.
     */
    public static final long CF_LOCALE = 16;

    /**
     * Specifies memory object containing a BITMAPV5HEADER structure.
     */
    public static final long CF_DIBV5 = 17;


    /**
     * Specifies owner-display format.
     */
    public static final long CF_OWNERDISPLAY = 0x0080;

    /**
     * Specifies text display format.
     */
    public static final long CF_DSPTEXT = 0x0081;

    /**
     * Specifies bitmap display format.
     */
    public static final long CF_DSPBITMAP = 0x0082;

    /**
     * Specifies metafile-picture display format.
     */
    public static final long CF_DSPMETAFILEPICT = 0x0083;

    /**
     * Specifies enhanced metafile display format.
     */
    public static final long CF_DSPENHMETAFILE = 0x008E;


    /**
     * Specifies first value in private clipboard formats.
     */
    public static final long CF_PRIVATEFIRST = 0x0200;

    /**
     * Specifies last value in private clipboard formats.
     */
    public static final long CF_PRIVATELAST = 0x02FF;


    /**
     * Specifies first value in application-defined Microsoft Windows
     * Graphics Device Interface (GDI) object clipboard formats
     */
    public static final long CF_GDIOBJFIRST = 0x0300;

    /**
     * Specifies last value in application-defined Microsoft Windows
     * Graphics Device Interface (GDI) object clipboard formats
     */
    public static final long CF_GDIOBJLAST = 0x03FF;

    private String _name;
    private long _format;

    /**
     * Creates an instance of the class with a specified item name and data format.
     *
     * @param name is a name of the item.
     * @param format is a data format.
     */
    public DdeItem(String name, long format)
    {
        _name = name;
        _format = format;
    }

    /**
     * Creates an instance of the class with specified name and sets the data format
     * to {@link #CF_TEXT CF_TEXT}.
     *
     * @param name
     */
    public DdeItem(String name)
    {
        _name = name;
        _format = CF_TEXT;
    }

    /**
     * Returns the name of the item.
     *
     * @return the name of the item.
     */
    public String getName()
    {
        return _name;
    }

    /**
     * Returns the item data format.
     *
     * @return the item data format.
     */
    public long getFormat()
    {
        return _format;
    }
}

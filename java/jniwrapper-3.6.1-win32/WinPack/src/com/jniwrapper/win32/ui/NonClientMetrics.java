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
import com.jniwrapper.win32.gdi.Font;
import com.jniwrapper.win32.gdi.LogFont;

public class NonClientMetrics extends Structure
{
    private UInt cbSize = new UInt();
    private Int iBorderWidth = new Int();
    private Int iScrollWidth = new Int();
    private Int iScrollHeight = new Int();
    private Int iCaptionWidth = new Int();
    private Int iCaptionHeight = new Int();
    private LogFont lfCaptionFont = new LogFont();
    private Int iSmCaptionWidth = new Int();
    private Int iSmCaptionHeight = new Int();
    private LogFont lfSmCaptionFont = new LogFont();
    private Int iMenuWidth = new Int();
    private Int iMenuHeight = new Int();
    private LogFont lfMenuFont = new LogFont();
    private LogFont lfStatusFont = new LogFont();
    private LogFont lfMessageFont = new LogFont();

    public NonClientMetrics()
    {
        init(new Parameter[]{
            cbSize, iBorderWidth, iScrollWidth, iScrollHeight,
            iCaptionWidth, iCaptionHeight,
            lfCaptionFont,
            iSmCaptionWidth, iSmCaptionHeight,
            lfSmCaptionFont,
            iMenuWidth, iMenuHeight,
            lfMenuFont, lfStatusFont, lfMessageFont
        });

        cbSize.setValue(getLength());

        initMetrics();
    }

    public NonClientMetrics(NonClientMetrics that)
    {
        this();
        initFrom(that);
    }

    public LogFont getLfMenuFont()
    {
        return lfMenuFont;
    }

    public Font getMenuFont()
    {
        LogFont logFont = getLfMenuFont();

        Font resultFont = Font.createFontIndirect(logFont);
        return resultFont;
    }

    private void initMetrics()
    {
        Pointer pointer = new Pointer(this);

        SystemParametersInfo.systemParametersInfo(new UInt(SystemParametersInfo.SPI_GETNONCLIENTMETRICS),
                new UInt(getLength()),
                pointer,
                new UInt(SystemParametersInfo.SPIF_DONT_UPDATE_PROFILE));
    }

    public Object clone()
    {
        return new NonClientMetrics(this);
    }
}

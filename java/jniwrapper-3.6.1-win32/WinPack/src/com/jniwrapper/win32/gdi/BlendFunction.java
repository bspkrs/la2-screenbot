/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.gdi;

import com.jniwrapper.Structure;
import com.jniwrapper.UInt8;
import com.jniwrapper.Parameter;

// TODO [Sanders]: Make sure documentation isn't word-to-word from MSDN. The below looks like a copy.
// E.g. what is AlphaBlend function?
// TODO [Sanders]: Is it a good name for the Structure? It now reads as a Function?
/**
 * BlendStructure containts parameters for the AlphaBlend function. If source
 * constant alpha is 255, the AlphaBlend function uses alpha for pixels only. If
 * this value is 0, the image drawn by AlphaBlend is transparent. The image is
 * semi-transparent in other cases. Alpha format is AC_SRC_ALPHA.
 */
public class BlendFunction extends Structure
{
    public static final int AC_SRC_OVER = 0x00;
    public static final int AC_SRC_ALPHA = 0x01;

    /**
     * A method of producing a transparent image (AC_SRC_OVER is supported only).
     */
    private UInt8 _blendOp = new UInt8();

    /**
     * Must be 0
     */
    private UInt8 _blendFlags = new UInt8();

    /**
     * If source constant alpha is 255, AlphaBlend function uses alpha for
     * pixels only. If this value is 0, the image drawn by AlphaBlend is
     * transparent. The image is semi-transparent in other cases.
     */
    private UInt8 _sourceConstantAlpha = new UInt8();

    /**
     * Alpha format is AC_SRC_ALPHA.
     */
    private UInt8 _alphaFormat = new UInt8();

    public BlendFunction()
    {
        init(new Parameter[] { _blendOp, _blendFlags, _sourceConstantAlpha, _alphaFormat});

        setBlendOp(AC_SRC_OVER);
        setBlendFlags(0);
    }

    public BlendFunction(BlendFunction that)
    {
        this();
        initFrom(that);
    }

    private void setBlendOp(int blendOp)
    {
        _blendOp.setValue(blendOp);
    }

    private void setBlendFlags(int blendFlags)
    {
        _blendFlags.setValue(blendFlags);
    }

    public void setSourceConstantAlpha(int sourceConstantAlpha)
    {
        _sourceConstantAlpha.setValue(sourceConstantAlpha);
    }

    public void setAlphaFormat(int alphaFormat)
    {
        _alphaFormat.setValue(alphaFormat);
    }

    public Object clone()
    {
        return new BlendFunction(this);
    }
}

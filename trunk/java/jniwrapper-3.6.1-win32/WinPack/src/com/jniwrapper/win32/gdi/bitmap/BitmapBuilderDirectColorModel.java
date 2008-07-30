/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.gdi.bitmap;

import com.jniwrapper.ArrayParameter;

import java.awt.Image;
import java.awt.image.BufferedImage;

public abstract class BitmapBuilderDirectColorModel extends BitmapBuilder
{
    // TODO [Sanders]: Remove if not needed.    
    private static final String INVALID_COLOR_MODEL_MSG = "This builder works with DirectColorModel only. CurrentModel = ";

    protected BitmapBuilderDirectColorModel(int bitCount, BufferedImage bufferedImage)
    {
        super(bitCount, bufferedImage);

        checkColorModel();
    }

    public int getColorTableSize()
    {
        return 0;
    }

    // TODO [Sanders]: Remove if not needed.
    private void checkColorModel()
    {
//        if (!(getColorModel() instanceof DirectColorModel))
//        {
//            throw new RuntimeException(INVALID_COLOR_MODEL_MSG + getColorModel());
//        }
    }

    public void buildColorTable(ArrayParameter quadArray)
    {
    }

    public void setBitmapColors()
    {
        int[] rgb = getBufferedImage().getRGB(0, 0, getWidth(), getHeight(), null, 0, getWidth());

        for (int x = 0; x < getWidth(); x++)
        {
            for (int y = 0; y < getHeight(); y++)
            {
                int argbColor = rgb[getImageOffset(x, y)];
                setArbgColor(argbColor, getOffsetInBitmapBytes(x, y));
            }
        }
    }
    public void setArbgColor(int argbColor, int offset)
    {
        setPixel(new ARGB(argbColor), offset);
    }

    public abstract void setPixel(ARGB argb, int offset);

    public Image getTransparentMask()
    {
        return null;
    }
}

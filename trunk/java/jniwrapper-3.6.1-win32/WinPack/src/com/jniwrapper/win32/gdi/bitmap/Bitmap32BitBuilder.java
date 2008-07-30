/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.gdi.bitmap;

import java.awt.image.BufferedImage;

class Bitmap32BitBuilder extends BitmapBuilderDirectColorModel
{
    public Bitmap32BitBuilder(BufferedImage bufferedImage)
    {
        super(32, bufferedImage);
    }

    public void setPixel(ARGB argb, int offset)
    {
        byte r = (byte)argb.getRed();
        byte g = (byte)argb.getGreen();
        byte b = (byte)argb.getBlue();
        byte a = (byte) argb.getAlpha();

        if (a != (byte) 255)
        {
            r = getTransparentColor(r, a);
            g = getTransparentColor(g, a);
            b = getTransparentColor(b, a);
        }

        setBitmapByte(offset + 3, a);
        setBitmapByte(offset + 2, r);
        setBitmapByte(offset + 1, g);
        setBitmapByte(offset, b);
    }

    private byte getTransparentColor(byte c, byte a)
    {
        if (a == -1)
        {
            return c;
        }

        int color = c >= 0 ? c : c + 256;
        int alpha = a >= 0 ? a : a + 256;

        int result = color * alpha / 255;
        return (byte) result;
    }
}

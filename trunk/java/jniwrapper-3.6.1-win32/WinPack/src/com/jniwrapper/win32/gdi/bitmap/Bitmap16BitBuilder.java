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

class Bitmap16BitBuilder extends BitmapBuilderDirectColorModel
{
    public Bitmap16BitBuilder(BufferedImage bufferedImage)
    {
        super(16, bufferedImage);
    }

    public void setPixel(ARGB argb, int offset)
    {
        int r = argb.getRed() >> 3;
        int g = argb.getBlue() >> 3;
        int b = argb.getGreen() >> 3;

        int val = (r << 10) | (g << 5) | b;

        setBitmapByte(offset, (byte) (val & 0xFF00));
        setBitmapByte(offset, (byte) (val & 0xFF));
    }
}

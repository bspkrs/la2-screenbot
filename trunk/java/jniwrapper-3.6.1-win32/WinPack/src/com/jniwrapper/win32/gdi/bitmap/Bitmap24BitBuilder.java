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

class Bitmap24BitBuilder extends BitmapBuilderDirectColorModel
{
    public Bitmap24BitBuilder(BufferedImage bufferedImage)
    {
        super(24, bufferedImage);
    }

    public void setPixel(ARGB argb, int offset)
    {
        setBitmapByte(offset + 2, (byte) argb.getRed());
        setBitmapByte(offset + 1, (byte) argb.getGreen());
        setBitmapByte(offset, (byte) argb.getBlue());
    }
}

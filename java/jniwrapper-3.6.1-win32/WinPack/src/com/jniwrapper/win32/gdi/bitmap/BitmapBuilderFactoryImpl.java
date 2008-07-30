/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.gdi.bitmap;

import java.awt.image.*;

public class BitmapBuilderFactoryImpl implements BitmapBuilderFactory
{
    private static final String NO_IMPL_MSG = "There isn't builder for specified color model. Color model = ";

    public BitmapBuilder createBuilder(BufferedImage bufferedImage)
    {
        ColorModel colorModel = bufferedImage.getColorModel();

        if (colorModel instanceof IndexColorModel)
        {
            return createBuilderByIndexModel(bufferedImage);
        }
        else if (colorModel instanceof DirectColorModel)
        {
            return createBuilderByDirectModel(bufferedImage);
        }
        else if (colorModel instanceof ComponentColorModel)
        {
            return createBuilderByComponentModel(bufferedImage);
        }
        else
        {
            throw new RuntimeException(NO_IMPL_MSG + colorModel);
        }
    }

    private BitmapBuilder createBuilderByIndexModel(BufferedImage bufferedImage)
    {
        if (!(bufferedImage.getColorModel() instanceof IndexColorModel))
        {
            throw new RuntimeException("Must be IndexColorModel");
        }

        IndexColorModel indexColorModel = (IndexColorModel) bufferedImage.getColorModel();

        if (indexColorModel.getPixelSize() <= 8 && indexColorModel.getMapSize() <= 256)
        {
            int bitCount = 0;

            if (indexColorModel.getMapSize() <= 2)
            {
                bitCount = 1;
            }
            else if (indexColorModel.getMapSize() <= 16)
            {
                bitCount = 4;
            }
            else
            {
                bitCount = 8;
            }

            return new BitmapBuilderIndexColorModel(bitCount, bufferedImage);
        }
        else
        {
            throw new RuntimeException(NO_IMPL_MSG);
        }
    }

    private BitmapBuilder createBuilderByDirectModel(BufferedImage bufferedImage)
    {
        int pixelSize = bufferedImage.getColorModel().getPixelSize();

        if (pixelSize <= 8 || pixelSize > 32)
        {
            throw new RuntimeException(NO_IMPL_MSG);
        }
        else if (pixelSize <= 16)
        {
            return new Bitmap16BitBuilder(bufferedImage);
        }
        else if (pixelSize <= 24)
        {
            return new Bitmap24BitBuilder(bufferedImage);
        }
        else
        {
            return new Bitmap32BitBuilder(bufferedImage);
        }
    }

    private BitmapBuilder createBuilderByComponentModel(BufferedImage bufferedImage)
    {
        if (bufferedImage.getColorModel().hasAlpha())
        {
            return new Bitmap32BitBuilder(bufferedImage);
        }
        else
        {
            return new Bitmap24BitBuilder(bufferedImage);
        }
    }
}

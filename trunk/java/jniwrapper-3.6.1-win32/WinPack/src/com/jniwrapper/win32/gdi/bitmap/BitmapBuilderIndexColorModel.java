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
import com.jniwrapper.win32.gdi.RGBQuad;

import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.awt.Image;
import java.util.Hashtable;

class BitmapBuilderIndexColorModel extends BitmapBuilder
{
    private static final String INVALID_COLOR_MODEL_MSG = "This builder works with IndexColorModel only. CurrentModel = ";

    public BitmapBuilderIndexColorModel(int bitCount, BufferedImage bufferedImage)
    {
        super(bitCount, bufferedImage);

        checkColorModel();
    }

    public int getColorTableSize()
    {
        return getIndexColorModel().getMapSize();
    }

    public void buildColorTable(ArrayParameter quadArray)
    {
        int paletteSize = getColorTableSize();

        byte[] reds = new byte[paletteSize];
        byte[] greens = new byte[paletteSize];
        byte[] blues = new byte[paletteSize];

        IndexColorModel indexColorModel = getIndexColorModel();

        indexColorModel.getReds(reds);
        indexColorModel.getGreens(greens);
        indexColorModel.getBlues(blues);

        for (int i = 0; i < paletteSize; i++)
        {
            RGBQuad quad = new RGBQuad();

            quad.setRed(getColorComponent(reds[i]));
            quad.setGreen(getColorComponent(greens[i]));
            quad.setBlue(getColorComponent(blues[i]));

            quadArray.setElement(i, quad);
        }
    }

    private long getColorComponent(byte color)
    {
        return (color >= 0) ? color : color + 256;
    }

    public void setBitmapColors()
    {
        byte[] pixelIndexes = getPixelIndexes();

        int pixelsPerBit = 8 / getBitCount();

        for (int y = 0; y < getHeight(); y++)
        {
            for (int x = 0; x < getWidth(); x += pixelsPerBit)
            {
                byte bitmapByte = 0;

                for (int i = 0; i < pixelsPerBit && x + i < getWidth(); i++)
                {
                    byte paletteIndex = pixelIndexes[getImageOffset(x + i, y)];
                    bitmapByte = addPixelValueToBitmapByte(bitmapByte, paletteIndex, (pixelsPerBit - i - 1) * getBitCount());
                }

                setBitmapByte(getOffsetInBitmapBytes(x, y), bitmapByte);
            }
        }
    }

    public byte addPixelValueToBitmapByte(byte bitmapByte, byte value, int bitOffset)
    {
        return (byte) (bitmapByte | (value << bitOffset));
    }

    private byte[] getPixelIndexes()
    {
        byte[] indexes = new byte[getWidth() * getHeight()];
        getBufferedImage().getRaster().getDataElements(0, 0, getWidth(), getHeight(), indexes);

        return indexes;
    }

    public Image getTransparentMask()
    {
        if (getIndexColorModel().getTransparentPixel() != -1)
        {
            IndexColorModel blackWhiteModel = new IndexColorModel(
                    1,
                    2,
                    createBlackWhitePalette(),
                    createBlackWhitePalette(),
                    createBlackWhitePalette());

            WritableRaster raster = createRasterWithMask();

            BufferedImage result = new BufferedImage(
                    blackWhiteModel,
                    raster,
                    false,
                    new Hashtable());


            return result;
        }
        else
        {
            return null;
        }
    }

    private WritableRaster createRasterWithMask()
    {
        int transparentPixel = getIndexColorModel().getTransparentPixel();
        WritableRaster raster = getIndexColorModel().createCompatibleWritableRaster(getWidth(), getHeight());

        byte[] pixelIndexes = getPixelIndexes();
        byte[] blackWhiteIndexes = new byte[getWidth() * getHeight()];

        for (int i = 0; i < pixelIndexes.length; i++)
        {
            int pixelIndex = pixelIndexes[i] >= 0 ? pixelIndexes[i] : pixelIndexes[i] + 256;
            byte color = (pixelIndex == transparentPixel) ? (byte) 0 : (byte) 1;
            blackWhiteIndexes[i] = color;
        }

        raster.setDataElements(0, 0, getWidth(), getHeight(), blackWhiteIndexes);
        return raster;
    }

    private byte[] createBlackWhitePalette()
    {
        return new byte[] { 0, -1 };
    }

    protected IndexColorModel getIndexColorModel()
    {
        return (IndexColorModel) getColorModel();
    }

    private void checkColorModel()
    {
        if (!(getColorModel() instanceof IndexColorModel))
        {
            throw new RuntimeException(INVALID_COLOR_MODEL_MSG + getColorModel());
        }
    }
}

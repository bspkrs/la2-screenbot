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
import com.jniwrapper.Int8;
import com.jniwrapper.PrimitiveArray;
import com.jniwrapper.win32.gdi.BitmapInfo;
import com.jniwrapper.win32.gdi.BitmapInfoHeader;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

// TODO: refactor builder: introduce fields _colorTable, move native-specific code to Bitmap (BitmapInfo)
public abstract class BitmapBuilder
{
    private static final int DWORD_SIZE = 32;
    private static final int BYTES_IN_DWORD = DWORD_SIZE / 8;

    private int _bitCount;
    private BufferedImage _bufferedImage;

    private int _scansizeBytes;

    private PrimitiveArray _bitmapData;

    protected BitmapBuilder(int bitCount, BufferedImage bufferedImage)
    {
        _bitCount = bitCount;
        _bufferedImage = bufferedImage;
        _scansizeBytes = calculateScansizeBytes();

        _bitmapData = new PrimitiveArray(createBitsArray(getScansizeBytes() * getHeight()));
    }

    public final BitmapInfo getBitmapInfo()
    {
        BitmapInfo result = getColorTableSize() > 0 ? new BitmapInfo(getColorTableSize()) : new BitmapInfo();

        buildColorTable(result.getColors());

        final BitmapInfoHeader bitmapInfoHeader = result.getBitmapInfoHeader();
        bitmapInfoHeader.setWidth(getWidth());
        bitmapInfoHeader.setHeight(getHeight());
        bitmapInfoHeader.setPlanes(1);
        bitmapInfoHeader.setBitCount(getBitCount());

        bitmapInfoHeader.setClrUsed(getColorTableSize());

        return result;
    }

    public abstract void setBitmapColors();

    /**
     * Returns count of bits per pixel.
     *
     * @return count of bits per pixel.
     */
    protected int getBitCount()
    {
        return _bitCount;
    }

    /**
     * Returns count of bytes per row.
     *
     * @return count of bytes per row.
     */
    protected final int getScansizeBytes()
    {
        return _scansizeBytes;
    }

    private int calculateScansizeBytes()
    {
        int bitsPerRow = getWidth() * getBitCount();
        return ((bitsPerRow + DWORD_SIZE - 1) / DWORD_SIZE) * BYTES_IN_DWORD;
    }

    protected void setBitmapByte(int offset, byte value)
    {
        _bitmapData.setElement(offset, new Int8(value));
    }

    public int getWidth()
    {
        return _bufferedImage.getWidth();
    }

    public int getHeight()
    {
        return _bufferedImage.getHeight();
    }

    private byte[] createBitsArray(int size)
    {
        return new byte[size];
    }

    protected abstract int getColorTableSize();

    protected abstract void buildColorTable(ArrayParameter resultQuadArray);

    protected int getImageOffset(int x, int y)
    {
        return x + y * getWidth();
    }

    /**
     * Offset in bitmap array
     *
     * @param x
     * @param y
     * @return offset in bitmap array
     */
    protected int getOffsetInBitmapBytes(int x, int y)
    {
        return (getBitCount() * x / 8) + (getHeight() - y - 1) * getScansizeBytes();
    }

    public abstract Image getTransparentMask();

    protected BufferedImage getBufferedImage()
    {
        return _bufferedImage;
    }

    protected ColorModel getColorModel()
    {
        return _bufferedImage.getColorModel();
    }

    public PrimitiveArray getBitmapData()
    {
        return _bitmapData;
    }
}

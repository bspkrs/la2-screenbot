/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.gdi;

import com.jniwrapper.win32.gdi.bitmap.BitmapBuilder;
import com.jniwrapper.win32.ui.User32;
import com.jniwrapper.win32.Handle;
import com.jniwrapper.win32.LastErrorException;
import com.jniwrapper.*;
import com.jniwrapper.util.ImageUtils;

import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * This class provides functionality for working with bitmaps in DIB (device-independent bitmap ) format.
 *
 * @author Serge Piletsky
 * @author Vladimir Kondrashchenko
 */
public class DIBitmap extends Bitmap
{
    private static String FUNCTION_GETDIBCOLORTABLE = "GetDIBColorTable";

    public DIBitmap()
    {
        setSize(0, 0);
    }

    public DIBitmap(long value)
    {
        super(value);
        setSize(0, 0);
    }

    public DIBitmap(int width, int height)
    {
        this(new DDBitmap(width, height));
    }

    public DIBitmap(DDBitmap ddBitmap)
    {
        BitmapInfoHeader bitmapInfo = ddBitmap.getBitmapInfoHeader();

        int height = (int)bitmapInfo.getHeight();
        int width = (int)bitmapInfo.getWidth();
        int bitscount = (int)bitmapInfo.getSizeImage();

        PrimitiveArray bits = new PrimitiveArray(UInt8.class, bitscount);
        Pointer pbits = new Pointer(bits);
        createDIBSection(this, new WindowDC(null), ddBitmap.getBitmapInfo(), DIB_RGB_COLORS, new Pointer(pbits), new Handle(), 0);
        ddBitmap.getDIBits(new WindowDC(null), 0, height, pbits, ddBitmap.getBitmapInfo(), DIB_RGB_COLORS);

        setSize(width, height);
    }

    public DIBitmap(BitmapInfo bitmapInfo)
    {
        int height = (int)bitmapInfo.getBitmapInfoHeader().getHeight();
        int width = (int)bitmapInfo.getBitmapInfoHeader().getWidth();

        PrimitiveArray bits = new PrimitiveArray(new UInt8((short)0), (int)bitmapInfo.getBitmapInfoHeader().getSizeImage());
        Pointer pbits = new Pointer(bits);
        createDIBSection(this, new WindowDC(null), bitmapInfo, DIB_RGB_COLORS, new Pointer(pbits), new Handle(), 0);

        setSize(width, height);
    }

    public DIBitmap(String fileName)
    {
        loadFromFile(fileName);
    }

    public DIBitmap(Image image)
    {
        BufferedImage bufferedImage = ImageUtils.createBufferedImage(image);
        setBitsForBitmap(bufferedImage);
        setSize((int)getBitmapInfoHeader().getWidth(), (int)getBitmapInfoHeader().getHeight());
    }

    public void loadFromFile(String fileName)
    {
        final Function function = User32.getInstance().getFunction(User32.FUNCTION_LOAD_IMAGE.toString());
        final Handle result = new Handle();
        long errorCode = function.invoke(result, new Parameter[]
        {
            new Pointer(null, true),
            new Str(fileName),
            new UInt(ImageType.BITMAP.getValue()),
            new Int(0),
            new Int(0),
            new UInt(GdiObject.ImageLoadParameters.SHARED | GdiObject.ImageLoadParameters.LOADFROMFILE | GdiObject.ImageLoadParameters.CREATEDIBSECTION | GdiObject.ImageLoadParameters.DEFAULTSIZE)
        });
        if (result.isNull())
        {
            throw new LastErrorException(errorCode, "Failed to load resource.", true);
        }

        setValue(result.getValue());
        BitmapInfoHeader bitmapInfo = getBitmapInfoHeader();
        setSize((int)bitmapInfo.getWidth(), (int)bitmapInfo.getHeight());
    }

    private void setBitsForBitmap(BufferedImage bufferedImage)
    {
        BitmapBuilder builder = _builderFactory.createBuilder(bufferedImage);

        BitmapInfo bitmapInfo = builder.getBitmapInfo();

        createDIBSection(this,
                new Pointer(new Pointer(builder.getBitmapData())),
                bitmapInfo);
        builder.setBitmapColors();

        Image transparentMaskImage = builder.getTransparentMask();
        if (transparentMaskImage != null)
        {
            setTransparentMask(transparentMaskImage);
        }
    }

    /**
     * Returns the BitmapInfo structure that contains information about the bitmap width, height, bits per pixel,
     * total image size, number of colors in palette, and other information.
     *
     * @return BitmapInfo structure that contains various information about bitmap.
     */
    public BitmapInfoHeader getBitmapInfoHeader()
    {
        DibSection dibSection = new DibSection();
        getObject(this, dibSection);
        return dibSection.getBitmapInfoHeader();
    }

    public BitmapStructure getBitmapStructure()
    {
        DibSection dibSection = new DibSection();
        getObject(this, dibSection);
        return dibSection.getBitmap();
    }

    public BitmapInfo getBitmapInfo()
    {
        BitmapInfo bitmapInfo = new BitmapInfo();
        bitmapInfo.setBitmapInfoHeader(getBitmapInfoHeader());

        int bitcount = (int)getBitmapInfoHeader().getBitCount();

        if (bitcount <= 8)
        {
            Function function = Gdi32.getInstance().getFunction(FUNCTION_GETDIBCOLORTABLE);
            DC dc = DC.createCompatibleDC(new WindowDC(null));
            dc.selectObject(this);
            UInt res = new UInt();
            PrimitiveArray rgbQuad = new PrimitiveArray(RGBQuad.class, (int)Math.pow(2, bitcount));
            function.invoke(res, new Parameter[]
            {
                dc,
                new UInt(0),
                new UInt((int)Math.pow(2, bitcount)),
                new Pointer(rgbQuad)
            });
            dc.release();
            bitmapInfo.setColors(rgbQuad);
        }
        
        return bitmapInfo;
    }

    public DibSection getDIBSection()
    {
        DibSection dibSection = new DibSection();
        getObject(this, dibSection);
        return dibSection;
    }

    public DDBitmap toDDBitmap(DC dc)
    {
        DDBitmap result = new DDBitmap(dc, getWidth(), getHeight());

        PrimitiveArray bits = getDIBytes();

        Function function = Gdi32.getInstance().getFunction(FUNCTION_CREATEDIBITMAP);
        function.invoke(result,
                new Parameter[] {
                    dc,
                    new Pointer(getDIBSection().getBitmapInfoHeader()),
                    new UInt32(CBM_INIT),
                    new Pointer(bits),
                    new Pointer(getBitmapInfo()),
                    new UInt(DIB_RGB_COLORS)
                });
        return result;
    }

    /**
     * Returns an array of device independent bitmap bytes as PrimitiveArray of UInt8.
     *
     * @return array of bitmap bytes.
     */
    public PrimitiveArray getDIBytes()
    {
        BitmapStructure bitmap = getBitmapStructure();
        PrimitiveArray bits = new PrimitiveArray(UInt8.class, (int)getBitmapInfoHeader().getSizeImage());
        bitmap.getBits().castTo(new Pointer(bits));

        return bits;
    }

    /**
     * Returns array of device independent bitmap bytes as byte array.
     *
     * @return array of bitmap bytes.
     */
    public byte[] getBytes() {
        BitmapStructure bitmap = getBitmapStructure();

        int size = (int) getBitmapInfoHeader().getSizeImage();
        MemoryBuffer bitsBuffer = DataBufferFactory.getInstance().createExternMemoryBuffer(bitmap.getBits().getValue(), size);

        return bitsBuffer.readByteArray(0, size);
    }

    public long getRGBbyIndex(long index)
    {
        long color = (index & 0xffff) | ((0x10ff & 0xffff) << 16);
        long red = (byte)color & 0xff;
        long green = (byte)(color >> 8) & 0xff;
        long blue = (byte)(color >> 16) & 0xff;
        return color;
    }

    public void setDIBytes(PrimitiveArray bytes)
    {
        BitmapInfo bitmapInfo = getBitmapInfo();
        PrimitiveArray bits = bytes;
        setDIBits(new WindowDC(null), 0, getHeight(), new Pointer(bits), bitmapInfo, DIB_RGB_COLORS);
    }
}

/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.gdi;

import com.jniwrapper.*;
import com.jniwrapper.util.ImageUtils;
import com.jniwrapper.win32.Handle;
import com.jniwrapper.win32.LastErrorException;
import com.jniwrapper.win32.system.LocalMemoryBlock;
import com.jniwrapper.win32.system.MemoryAllocationAttributes;
import com.jniwrapper.win32.gdi.bitmap.BitmapBuilder;
import com.jniwrapper.win32.ui.User32;

import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * This class provides functionality for working with bitmaps in DDB (device-dependent bitmap) format.
 *
 * @author Serge Piletsky
 * @author Vladimir Kondrashchenko
 */
public class DDBitmap extends Bitmap
{
    private static final String FUNCTION_CREATE_COMPATIBLE_BITMAP = "CreateCompatibleBitmap";

    private DC _dc;

    public DDBitmap()
    {
        _dc = new WindowDC(null);
        setSize(0, 0);
    }

    public DDBitmap(long value)
    {
        super(value);
        _dc = new WindowDC(null);
        setSize(0, 0);
    }

    public DDBitmap(int width, int height)
    {
        this(new WindowDC(null), width, height);
    }

    public DDBitmap(DC dc, int width, int height)
    {
        _dc = dc;
        setSize(width, height);
        Function function = Gdi32.get(FUNCTION_CREATE_COMPATIBLE_BITMAP);
        function.invoke(this, _dc, new Int(width), new Int(height));
    }

    public DDBitmap(DIBitmap diBitmap)
    {
        this(new WindowDC(null), diBitmap);
    }

    public DDBitmap(DC dc, DIBitmap diBitmap)
    {
        this(dc, diBitmap.getWidth(), diBitmap.getHeight());

        PrimitiveArray bits = diBitmap.getDIBytes();

        Function function = Gdi32.getInstance().getFunction(FUNCTION_CREATEDIBITMAP);
        function.invoke(this,
                new Parameter[] {
                    dc,
                    new Pointer(diBitmap.getDIBSection().getBitmapInfoHeader()),
                    new UInt32(CBM_INIT),
                    new Pointer(bits),
                    new Pointer(diBitmap.getBitmapInfo()),
                    new UInt(DIB_RGB_COLORS)
                });
    }

    public DDBitmap(String fileName)
    {
        this(new WindowDC(null), fileName);
    }

    public DDBitmap(DC dc, String fileName)
    {
        _dc = dc;
        loadFromFile(fileName);
    }

    public DDBitmap(Image image)
    {
        this(new WindowDC(null), image);
    }

    public DDBitmap(DC dc, Image image)
    {
        this(dc, image.getWidth(null), image.getHeight(null));
        BufferedImage bufferedImage = ImageUtils.createBufferedImage(image);
        setBitsForBitmap(bufferedImage);
        BitmapInfo bitmapInfo = getBitmapInfo();
        setSize((int)bitmapInfo.getBitmapInfoHeader().getWidth(), (int)bitmapInfo.getBitmapInfoHeader().getHeight());
    }

    public DDBitmap(PredefinedBitmap predefinedBitmap)
    {
        this(new WindowDC(null), predefinedBitmap);
    }

    public DDBitmap(DC dc, PredefinedBitmap predefinedBitmap)
    {
        _dc = dc;
        loadPredefinedBitmap(predefinedBitmap);
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
            new UInt(GdiObject.ImageLoadParameters.SHARED | GdiObject.ImageLoadParameters.LOADFROMFILE | GdiObject.ImageLoadParameters.DEFAULTSIZE)
        });
        if (result.isNull())
        {
            throw new LastErrorException(errorCode, "Failed to load resource.", true);
        }

        setValue(result.getValue());

        setSize((int)getBitmapStructure().getBitmapWidth(), (int)getBitmapStructure().getBitmapHeight());
    }

    public void loadPredefinedBitmap(PredefinedBitmap predefinedBitmap)
    {
        final Function function = User32.getInstance().getFunction(User32.FUNCTION_LOAD_IMAGE.toString());
        final Handle result = new Handle();
        long errorCode = function.invoke(result, new Parameter[]
        {
            new Pointer(null, true),
            new UInt32(predefinedBitmap.getValue()),
            new UInt(ImageType.BITMAP.getValue()),
            new Int(0),
            new Int(0),
            new UInt(GdiObject.ImageLoadParameters.SHARED | GdiObject.ImageLoadParameters.DEFAULTSIZE)
        });
        if (result.isNull())
        {
            throw new LastErrorException(errorCode, "Failed to load resource.", true);
        }

        setValue(result.getValue());

        BitmapInfo bitmapInfo = getBitmapInfo();
        setSize((int)bitmapInfo.getBitmapInfoHeader().getWidth(), (int)bitmapInfo.getBitmapInfoHeader().getHeight());
    }

    private void setBitsForBitmap(BufferedImage bufferedImage)
    {
        BitmapBuilder builder = _builderFactory.createBuilder(bufferedImage);

        BitmapInfo bitmapInfo = builder.getBitmapInfo();

        DIBitmap dibitmap = new DIBitmap();

        createDIBSection(dibitmap,
                new Pointer(new Pointer(builder.getBitmapData())),
                bitmapInfo);
        builder.setBitmapColors();

        PrimitiveArray pa = builder.getBitmapData();
        dibitmap.getDIBits(_dc, 0, getHeight(), new Pointer(pa), bitmapInfo, DIB_RGB_COLORS);
        setDIBits(_dc, 0, getHeight(), new Pointer(pa), bitmapInfo, DIB_RGB_COLORS);

        Image transparentMaskImage = builder.getTransparentMask();
        if (transparentMaskImage != null)
        {
            setTransparentMask(transparentMaskImage);
        }
    }

    public BitmapInfo getBitmapInfo()
    {
        BitmapInfo bitmapInfo = new BitmapInfo();
        getDIBits(_dc, 0, 0 , new Pointer(null, true), bitmapInfo, DIB_RGB_COLORS);
        bitmapInfo.getBitmapInfoHeader().setCompression(Compression.RGB);

        return bitmapInfo;
    }

    public BitmapStructure getBitmapStructure()
    {
        BitmapStructure bitmapStructure = new BitmapStructure();
        getObject(this, bitmapStructure);
        return bitmapStructure;
    }

    public BitmapInfoHeader getBitmapInfoHeader()
    {
        return getBitmapInfo().getBitmapInfoHeader();
    }

    public DIBitmap toDIBitmap()
    {
        BitmapInfoHeader bitmapInfo = getBitmapInfoHeader();

        int height = (int)bitmapInfo.getHeight();
        int width = (int)bitmapInfo.getWidth();
        int bitscount = (int)bitmapInfo.getSizeImage();

        PrimitiveArray bits = new PrimitiveArray(UInt8.class, bitscount);
        Pointer pbits = new Pointer(bits);

        DIBitmap result = new DIBitmap(width, height);

        createDIBSection(result, _dc, getBitmapInfo(), DIB_RGB_COLORS, new Pointer(pbits), new Handle(), 0);
        getDIBits(_dc, 0, height, pbits, getBitmapInfo(), DIB_RGB_COLORS);

        return result;
    }

    /**
     * Returns an array of device independent bitmap bytes as PrimitiveArray of UInt8.
     *
     * @return array of bitmap bytes.
     */
    public PrimitiveArray getDIBytes()
    {
        BitmapInfo bitmapInfo = getBitmapInfo();

        final int height = getHeight();
        final int width = getWidth();
        final int bitPerPixel = getBitCount();

        // retrieve bytes count
        final Pointer NULL = new Pointer(null, true);
        getDIBits(_dc, 0, height, NULL, bitmapInfo, DIB_RGB_COLORS);
        // retrieve bytes from bitmap
        int bytesCount = height * width * bitPerPixel / 8;
        byte buffer[] = new byte[bytesCount];
        PrimitiveArray bits = new PrimitiveArray(buffer);
        getDIBits(_dc, 0, height, new Pointer(bits), bitmapInfo, DIB_RGB_COLORS);

        return bits;
    }

    /**
     * Returns an array of device independent bitmap bytes as PrimitiveArray of UInt8.
     *
     * @return array of bitmap bytes.
     */
    public byte[] getBytes()
    {
        BitmapInfo bitmapInfo = getBitmapInfo();

        final int height = getHeight();
        final int width = getWidth();
        final int bitPerPixel = getBitCount();

        // retrieve bytes count
        final Pointer NULL = new Pointer(null, true);
        getDIBits(_dc, 0, height, NULL, bitmapInfo, DIB_RGB_COLORS);
        // retrieve bytes from bitmap
        int bytesCount = height * width * bitPerPixel / 8;
        LocalMemoryBlock memoryBuffer = new LocalMemoryBlock(bytesCount, MemoryAllocationAttributes.FIXED);

        getDIBits(_dc, 0, height, memoryBuffer, bitmapInfo, DIB_RGB_COLORS);
        MemoryBuffer bitsBuffer = DataBufferFactory.getInstance().createExternMemoryBuffer(memoryBuffer.getValue(), bytesCount);
        byte[] result = bitsBuffer.readByteArray(0, bytesCount);
        memoryBuffer.free();

        return result;
    }

    public void setDIBytes(PrimitiveArray bytes)
    {
        BitmapInfo bitmapInfo = getBitmapInfo();
        PrimitiveArray bits = bytes;
        setDIBits(_dc, 0, getHeight(), new Pointer(bits), bitmapInfo, DIB_RGB_COLORS);
    }
}

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
import com.jniwrapper.util.EnumItem;
import com.jniwrapper.util.FlagSet;
import com.jniwrapper.win32.Handle;
import com.jniwrapper.win32.Size;
import com.jniwrapper.win32.gdi.bitmap.BitmapBuilderFactory;
import com.jniwrapper.win32.gdi.bitmap.BitmapBuilderFactoryImpl;

import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This class represents common functionality for working with bitmaps.
 *
 * @author Serge Piletsky
 * @author Vladimir Kondrashchenko
 */
public abstract class Bitmap extends GdiObject
{
    protected static final BitmapBuilderFactory _builderFactory = new BitmapBuilderFactoryImpl();

    private static final String FUNCTION_CREATE_DIBSECTION = "CreateDIBSection";
    protected static final String FUNCTION_GET_DIBITS = "GetDIBits";
    protected static final String FUNCTION_SET_DIBITS = "SetDIBits";
    protected static final String FUNCTION_CREATEDIBITMAP = "CreateDIBitmap";

    /**
     * Color table in RGB.
     */
    protected static final int DIB_RGB_COLORS = 0;
    /**
     * Color table in palette indixes.
     */
    protected static final int DIB_PAL_COLORS = 1;

    /**
     * Initialize bitmap.
     */
    protected static final int CBM_INIT = 0x04;

    private Dimension _size;
    private DIBitmap _transparentMask;

    public Bitmap()
    {
        super();
    }

    public Bitmap(long value)
    {
        super(value);
    }

    /**
     * Sets bits for a bitmap.
     *
     * @param dc             dc compatible with a bitmap.
     * @param startScan      start scanline in a bitmap.
     * @param scanLines      the number of scanlines in a bitmap.
     * @param bits           a pointer to PrimitiveArray of UInt8 (result bits).
     * @param bitmapInfo     BitmapInfo structure describing a bitmap.
     * @param colorModelType a type of color model.
     * @return the number of scanlines with bits.
     */
    public long setDIBits(DC dc, int startScan, int scanLines,
                          Pointer bits, BitmapInfo bitmapInfo, int colorModelType)
    {
        Int result = new Int();
        Function function = Gdi32.get(FUNCTION_SET_DIBITS);
        function.invoke(result, new Parameter[]
        {
            dc,
            this,
            new UInt(startScan),
            new UInt(scanLines),
            bits,
            new Pointer(bitmapInfo),
            new UInt(colorModelType)
        });
        return result.getValue();
    }

    /**
     * Returns bits of a bitmap.
     *
     * @param dc             dc compatible with a bitmap.
     * @param startScan      start scanline in bitmap.
     * @param scanLines      the number of scanlines in a bitmap.
     * @param bits           a pointer to PrimitiveArray of UInt8 (result bits).
     * @param bitmapInfo     BitmapInfo structure describing a bitmap.
     * @param colorModelType a type of color model.
     * @return the number of scanlines with bits.
     */
    public long getDIBits(DC dc, int startScan, int scanLines, Pointer bits, BitmapInfo bitmapInfo, int colorModelType)
    {
        Int result = new Int();
        Function function = Gdi32.get(FUNCTION_GET_DIBITS);
        function.invoke(result, new Parameter[]
        {
            dc,
            this,
            new UInt(startScan),
            new UInt(scanLines),
            bits,
            new Pointer(bitmapInfo),
            new UInt(colorModelType)
        });
        return result.getValue();
    }

    /**
     * Returns bits of a bitmap.
     *
     * @param dc             dc compatible with a bitmap.
     * @param startScan      start scanline in bitmap.
     * @param scanLines      the number of scanlines in a bitmap.
     * @param bits           a pointer to PrimitiveArray of UInt8 (result bits).
     * @param bitmapInfo     BitmapInfo structure describing a bitmap.
     * @param colorModelType a type of color model.
     * @return the number of scanlines with bits.
     */
    public long getDIBits(DC dc, int startScan, int scanLines, Pointer.Void bits, BitmapInfo bitmapInfo, int colorModelType)
    {
        Int result = new Int();
        Function function = Gdi32.get(FUNCTION_GET_DIBITS);
        function.invoke(result, new Parameter[]
        {
            dc,
            this,
            new UInt(startScan),
            new UInt(scanLines),
            bits,
            new Pointer(bitmapInfo),
            new UInt(colorModelType)
        });
        return result.getValue();
    }

    /**
     * Sets bitmap size in pixels.
     *
     * @param width
     * @param height
     */
    protected void setSize(int width, int height)
    {
        _size = new Dimension(width, height);
    }

    /**
     * Return bitmap size as an Size object instance.
     *
     * @return Size object instance
     */
    public Size getSize()
    {
        return new Size(getWidth(), getHeight());
    }

    /**
     * Returns bitmap width.
     *
     * @return width - bitmap width in pixels
     */
    public int getWidth()
    {
        return (int) _size.getWidth();
    }

    /**
     * Returns bitmap height in pixels.
     *
     * @return height - bitmap height in pixels
     */
    public int getHeight()
    {
        return (int) _size.getHeight();
    }

    /**
     * Allows read byte from bitmap field.
     *
     * @param bits    - bitmap field
     * @param offset  - offset from bitmap origin
     * @return byte -  integer byte value
     */
    private int readByte(PrimitiveArray bits, int offset)
    {
        return (int) ((Int8) bits.getElement(offset)).getValue();
    }

    /**
     * Creates a handle to device independent bitmap (DIB).
     * @param result     [out] is a handle to DIB.
     * @param ppvBits    [out] is a pointer to the location of bits in DIB section.
     * @param bitmapInfo [in] describes source DDBitmap.
     */
    public void createDIBSection(DIBitmap result,
                                 Pointer ppvBits,
                                 BitmapInfo bitmapInfo)
    {
        createDIBSection(result, new DC(), bitmapInfo, DIB_RGB_COLORS, ppvBits, new Handle(), 0);
    }

    /**
     * Creates a handle to device independent bitmap (DIB).
     *
     * @param result     [out] is a handle to DIB.
     * @param hdc        [in] is a handle to the DC of source DDBitmap.
     * @param bitmapInfo [in] describes source DDBitmap.
     * @param colorModel [in] is the type of color model.
     * @param ppvBits    [out] is a pointer to the location of bits in DIB section.
     * @param hSection   [in] is a hable to DIB section.
     * @param dwOffset   [in] is an offset of bits location in DIB section.
     */
    public void createDIBSection(DIBitmap result,
                                 DC hdc,
                                 BitmapInfo bitmapInfo,
                                 int colorModel,
                                 Pointer ppvBits,
                                 Handle hSection,
                                 int dwOffset)
    {
        Function function = Gdi32.get(FUNCTION_CREATE_DIBSECTION);

        function.invoke(result, new Parameter[]
        {
            hdc,
            new Pointer(bitmapInfo),
            new UInt16(colorModel),
            ppvBits,
            hSection,
            new UInt(dwOffset),
        });
    }

    /**
     * Returns the number of bits per pixel.
     *
     * @return the number of bits per pixel.
     */
    public int getBitCount()
    {
        return (int) getBitmapInfoHeader().getBitCount();
    }

    /**
     * Sets transparency mask using input image.
     *
     * @param transparentMask - an image
     */
    protected void setTransparentMask(Image transparentMask)
    {
        _transparentMask = new DIBitmap(transparentMask);
    }

    /**
     * Returns transparancy bitmap mask.
     *
     * @return bitmap Bitmap instance
     */
    public Bitmap getTransparentMask()
    {
        return _transparentMask;
    }

    /**
     * Returns true if a transparent bitmap.
     *
     * @return true if a transparent bitmap.
     */
    public boolean isTransparent()
    {
        return getBitCount() == 32 || _transparentMask != null;
    }

    /**
     * Deletes the object.
     */
    public void deleteObject()
    {
        super.deleteObject();

        if (_transparentMask != null)
        {
            _transparentMask.deleteObject();
        }
    }

    /**
     * Converts windows Bitmap to {@link java.awt.image.BufferedImage}.
     *
     * @return image BufferedImage instance.
     */
    public BufferedImage toImage()
    {
        final int bitCount = getBitCount();
        final int width = getWidth();
        final int height = getHeight();

        return bitCount >= 4 ?
                transformBitmap(getBitmapInfo().getColors(), width, height, bitCount, getBytes()) :
                transformMonochromeBitmap(width, height, bitCount, getDIBytes());
    }

    /**
     * Converts monochrome bitmap array to {@link java.awt.image.BufferedImage}.
     *
     * @return image BufferedImage instance.
     */
    private BufferedImage transformMonochromeBitmap(final int width, final int height, final int bitCount, PrimitiveArray bits)
    {
        int foregroundColor = 0xFF000000;
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        int bitIndex = 0;
        int byteIndex = 0;
        int bytesPerLine = ((width * bitCount + 31) / 32) * 4;

        for (int y = height - 1; y >= 0; y--)
        {
            for (int x = 0; x < width; x++)
            {
                byteIndex = (height - 1 - y) * bytesPerLine + (x * bitCount / 8);
                bitIndex = x * bitCount % 8;
                int byteValue = readByte(bits, byteIndex);
                FlagSet bitset = new FlagSet(byteValue);
                boolean isSet = bitset.getBit(7 - bitIndex);
                int rgb = (!isSet ? foregroundColor : 0);
                result.setRGB(x, y, rgb);
            }
        }

        return result;
    }

    /**
     * Converts bitmap array to {@link java.awt.image.BufferedImage}.
     *
     * @return image BufferedImage instance.
     */
    private BufferedImage transformBitmap(PrimitiveArray colors, final int width, final int height, final int bitCount, byte[] bytes)
    {
        final int pixelsLength = bytes.length / (bitCount == 4 ? (bitCount * 2) : (bitCount / 8));
        final int[] pixels = new int[pixelsLength];

        int index = 0;
        int bitIndex = 0;
        for (int i = pixelsLength - 1 - width, j = 0; i >= 0; i -= width) {
            for (int h = 0; h < width; h++) {
                switch (bitCount) {
                    case 4: // 16 colors, used pallete
                    {
                        index = bitIndex / 8;
                        int byte1 = (short) bytes[index];
                        if (bitIndex % 8 != 0) {
                            byte1 = byte1 & 0xF;
                        }
                        else {
                            byte1 = (byte1 >> 4);
                        }
                        RGBQuad color = (RGBQuad) colors.getElement(byte1);
                        bitIndex += 4;
                        pixels[i + h] = color.getRGB();
                        break;
                    }
                    case 8: // 256 colors, used pallete
                    {
                        int byte1 = (short) bytes[j++];
                        RGBQuad color = (RGBQuad) colors.getElement(byte1);
                        pixels[i + h] = color.getRGB();
                        break;
                    }
                    case 16:// 2 bytes per pixel, 5-5-5 bits per pixel
                    {
                        int byte1 = (short) bytes[j++];
                        int byte2 = (short) bytes[j++];
                        int word = byte2 << 8 | byte1;
                        int b = (word & 0x1F) << 3;
                        int g = (word & 0x3E0) << 6;
                        int r = (word & 0x7C00) << 9;
                        pixels[i + h] = (b | g | r);
                        break;
                    }
                    case 24:// 3 bytes per pixel
                    case 32:// 4 bytes per pixel, high byte used as alpha
                    {
                        int b = ((short) bytes[j++]) & 0xff;
                        int g = ((short) bytes[j++]) & 0xff;
                        int r = ((short) bytes[j++]) & 0xff;
                        int a = ((short) bytes[j++]) & 0xff;
                        if (a == 0) a = 0xFF;
                        pixels[i + h] = b | g << 8 | r << 16 | a << 24;
                        break;
                    }
                }
            }
            if (bitCount != 4) {
                index += (int) (4 * (Math.ceil(width * bitCount / 32.0) - (width * bitCount / 32.0)));
            } else {
                double extrabytes = (4 * (Math.ceil(width * bitCount / 32.0) - (width * bitCount / 32.0)));
                bitIndex += (int) (extrabytes * 8);
            }
        }

        DirectColorModel colorModel = new DirectColorModel(24, 0x00FF0000, 0x0000FF00, 0x000000FF);
        int[] bandmasks = new int[]{ colorModel.getRedMask(), colorModel.getGreenMask(), colorModel.getBlueMask()};
        DataBufferInt buffer = new DataBufferInt(pixels, pixels.length);
        WritableRaster raster = Raster.createPackedRaster(buffer, width, height, width, bandmasks, null);

        return new BufferedImage(colorModel, raster, false, null);
    }

    /**
     * Saves bitmap to the specified file.
     *
     * @param fileName path to the file where the bitmap will be saved.
     */
    public void saveToFile(String fileName) throws IOException
    {
        if (isNull())
        {
            throw new RuntimeException("No bitmap was specified.");
        }
        File file = new File(fileName);
        if (!file.exists())
        {
            file.createNewFile();
        }
        BitmapFileHeader fileHeader = new BitmapFileHeader();
        BitmapInfoHeader infoHeader = getBitmapInfo().getBitmapInfoHeader();
        PrimitiveArray rgbHeader = getBitmapInfo().getColors();

        OutputStream out = new FileOutputStream(file);

        if (infoHeader.getBitCount() == 1)
        {
            rgbHeader = new PrimitiveArray(new Parameter[]
            {
                new RGBQuad(0, 0, 0),
                new RGBQuad(255, 255, 255)
            });

        }
        fileHeader.setOffBits(fileHeader.getLength() + infoHeader.getLength()
                + rgbHeader.getLength());

        fileHeader.setSize(fileHeader.getLength() + infoHeader.getLength()
                + rgbHeader.getLength() + getDIBytes().getLength());

        out.write(BitmapFileHeader.toByteArray(fileHeader));
        out.write(BitmapFileHeader.toByteArray(infoHeader));
        out.write(BitmapFileHeader.toByteArray(rgbHeader));
        out.write(BitmapFileHeader.toByteArray(getDIBytes()));

        out.close();
    }

    public abstract BitmapInfoHeader getBitmapInfoHeader();

    public abstract BitmapInfo getBitmapInfo();

    public abstract void loadFromFile(String fileName);

    /**
     * Returns array of device independent bitmap bytes as PrimitiveArray of UInt8.
     *
     * @return array of bitmap bytes.
     * @deprecated use {#getBytes} method to obtain byte array of image
     */
    public abstract PrimitiveArray getDIBytes();

    /**
     * Returns array of device independent bitmap bytes as byte array.
     *
     * @return array of bitmap bytes.
     */
    public abstract byte[] getBytes();


    /**
     * Sets bytes of the bitmap.
     *
     * @param bytes
     */
    public abstract void setDIBytes(PrimitiveArray bytes);

    /**
     * Class Compression represents compression level enumeration.
     */
    public static class Compression extends EnumItem
    {
        /**
         * Uncompressed format.
         */
        public static final Compression RGB = new Compression(0);
        /**
         * Run-Length Encoded format for bitmaps with 8 bpp.
         */
        public static final Compression RLE8 = new Compression(1);
        /**
         * Run-Length Encoded format for bitmaps with 4 bpp.
         */
        public static final Compression RLE4 = new Compression(2);
        /**
         * Specifies that the bitmap is not compressed.
         */
        public static final Compression BITFIELDS = new Compression(3);
        /**
         * Specifies image compression using the JPEG format.
         */
        public static final Compression JPEG = new Compression(4);
        /**
         * Specifies image compression using the PNG format.
         */
        public static final Compression PNG = new Compression(5);

        protected Compression(int value)
        {
            super(value);
        }
    }

    /**
     * Class PredefinedBitmap enumeration represents predefined windows bitmaps.
     */
    public static class PredefinedBitmap extends EnumItem
    {
        public static final PredefinedBitmap BTNCORNERS = new PredefinedBitmap(32758);
        public static final PredefinedBitmap BTSIZE = new PredefinedBitmap(32761);
        public static final PredefinedBitmap CHECK = new PredefinedBitmap(32760);
        public static final PredefinedBitmap CHECKBOXES = new PredefinedBitmap(32759);
        public static final PredefinedBitmap CLOSE = new PredefinedBitmap(32754);
        public static final PredefinedBitmap COMBO = new PredefinedBitmap(32738);
        public static final PredefinedBitmap DNARROW = new PredefinedBitmap(32752);
        public static final PredefinedBitmap DNARROWD = new PredefinedBitmap(32742);
        public static final PredefinedBitmap DNARROWI = new PredefinedBitmap(32736);
        public static final PredefinedBitmap LFARROW = new PredefinedBitmap(32750);
        public static final PredefinedBitmap LFARROWD = new PredefinedBitmap(32740);
        public static final PredefinedBitmap LFARROWI = new PredefinedBitmap(32734);
        public static final PredefinedBitmap MNARROW = new PredefinedBitmap(32739);
        public static final PredefinedBitmap REDUCE = new PredefinedBitmap(32749);
        public static final PredefinedBitmap REDUCED = new PredefinedBitmap(32746);
        public static final PredefinedBitmap RESTORE = new PredefinedBitmap(32747);
        public static final PredefinedBitmap RESTORED = new PredefinedBitmap(32744);
        public static final PredefinedBitmap RGARROW = new PredefinedBitmap(32751);
        public static final PredefinedBitmap RGARROWD = new PredefinedBitmap(32741);
        public static final PredefinedBitmap RGARROWI = new PredefinedBitmap(32735);
        public static final PredefinedBitmap SIZE = new PredefinedBitmap(32766);
        public static final PredefinedBitmap UPARROW = new PredefinedBitmap(32753);
        public static final PredefinedBitmap UPARROWD = new PredefinedBitmap(32743);
        public static final PredefinedBitmap UPARROWI = new PredefinedBitmap(32737);
        public static final PredefinedBitmap ZOOM = new PredefinedBitmap(32748);
        public static final PredefinedBitmap ZOOMD = new PredefinedBitmap(32745);

        private PredefinedBitmap(int value)
        {
            super(value);
        }
    }

}

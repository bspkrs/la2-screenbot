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
import com.jniwrapper.util.Logger;
import com.jniwrapper.util.StreamUtils;
import com.jniwrapper.win32.FunctionName;
import com.jniwrapper.win32.Handle;
import com.jniwrapper.win32.LastErrorException;
import com.jniwrapper.win32.Size;
import com.jniwrapper.win32.ui.User32;

import java.awt.Dimension;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.LinkedList;

/**
 * This class represents generic methods to work with icons.
 *
 * @author Serge Piletsky
 * @author Vladimir Kondrashchenko
 */

public class Icon extends GdiObject
{
    private static final Logger LOG = Logger.getInstance(Icon.class);

    private static final FunctionName FUNCTION_LOAD_ICON = new FunctionName("LoadIcon");
    private static final FunctionName FUNCTION_LOAD_IMAGE = new FunctionName("LoadImage");
    private static final String FUNCTION_GET_ICON_INFO = "GetIconInfo";
    private static final String FUNCTION_CreateIconIndirect = "CreateIconIndirect";

    private Dimension _size = new Dimension(IconType.SMALL.getSize());
    private int _bitCount;

    static Function getFunction(Object functionName)
    {
        return User32.getInstance().getFunction(functionName.toString());
    }

    protected void update()
    {
        if (!isNull())
        {
            final IconInfo iconInfo = getIconInfo();
            final Bitmap colorBitmap = iconInfo.getColorBitmap();
            if (!colorBitmap.isNull())
            {
                final Size size = colorBitmap.getSize();
                _size.width = size.getCx();
                _size.height = size.getCy();
                _bitCount = colorBitmap.getBitCount();
            }
        }
    }

    /**
     * Creates a NULL icon.
     */
    public Icon()
    {
    }

    public Icon(long value)
    {
        super(value);
    }

    /**
     * Creates a NULL icon with a specified size.
     */
    public Icon(Dimension size)
    {
        _size.setSize(size);
    }

    /**
     * Creates an icon from the executable (.exe) file associated with an
     * application instance.
     *
     * @param iconName Name of the icon resource to be loaded.
     */
    public Icon(String iconName)
    {
        this(new Handle(), iconName);
    }

    /**
     * Creates an icon from the executable (.exe) file associated with an
     * application instance.
     *
     * @param hInstance Handle to an instance of the module whose executable
     *                  file contains the icon to be loaded. This parameter must be NULL when a
     *                  standard icon is being loaded.
     * @param iconName  Name of the icon resource to be loaded.
     */
    public Icon(Handle hInstance, String iconName)
    {
        final Function function = getFunction(FUNCTION_LOAD_ICON);
        long errorCode = function.invoke(this, hInstance, new Str(iconName));
        if (isNull())
        {
            throw new LastErrorException(errorCode, "Failed to load icon.", true);
        }
        update();
    }

    /**
     * Creates an icon from the specified file.
     *
     * @param file Specifies the file to load the image from.
     */
    public Icon(File file)
    {
        load(file);
    }

    /**
     * Creates an icon from the specified file.
     *
     * @param file Specifies the file to load the image from.
     * @param size Specifies the icon size.
     */
    public Icon(File file, Dimension size)
    {
        load(file, size);
    }

    /**
     * Creates an icon from the specified file.
     *
     * @param hInstance Handle to an instance of the module that contains the
     *                  image to be loaded. To load an OEM image, set this parameter to zero.
     * @param file      Specifies the file to load the image from.
     */
    public Icon(Handle hInstance, File file)
    {
        load(hInstance, file, getSize());
    }

    /**
     * Loads the icon from the specified file. This method uses the current size
     * of this icon.
     *
     * @param file Specifies the file to load the image from.
     */
    public void load(File file)
    {
        load(file, getSize());
    }

    /**
     * Loads the icon from the specified file.
     *
     * @param file Specifies the file to load the image from.
     * @param size Specifies the size of the icon to load.
     */
    public void load(File file, Dimension size)
    {
        load(new Handle(), file, size);
    }

    /**
     * Loads the icon from the specified file.
     *
     * @param hInstance Handle to an instance of the module that contains the
     *                  image to be loaded. To load an OEM image, set this parameter to zero.
     * @param file      Specifies the file to load the image from.
     * @param size      Specifies the size of the icon to load.
     */
    public void load(Handle hInstance, File file, Dimension size)
    {
        if (file == null)
        {
            throw new IllegalArgumentException("File is null.");
        }
        if (!file.exists())
        {
            throw new IllegalArgumentException("File does not exsist.");
        }
        if (size == null)
        {
            throw new IllegalArgumentException("Invalid size parameter.");
        }

        final Function function = getFunction(FUNCTION_LOAD_IMAGE.toString());
        long errorCode = function.invoke(this, new Parameter[]
        {
            hInstance,
            new Str(file.getAbsolutePath()),
            new UInt(ImageType.ICON.getValue()),
            new Int(size.width),
            new Int(size.height),
            new UInt(GdiObject.ImageLoadParameters.SHARED | GdiObject.ImageLoadParameters.LOADFROMFILE)
        });
        if (isNull())
        {
            throw new LastErrorException(errorCode, "Failed to load icon.", true);
        }
        update();
    }

    /**
     * Creates an icon from the specified stream. This method uses the current
     * size of this icon.
     *
     * @param stream Specifies the stream to load the icon from.
     */
    public Icon(InputStream stream) throws IOException
    {
        load(stream, getSize());
    }

    /**
     * Creates an icon from the specified stream.
     *
     * @param stream Specifies the stream to load the icon from.
     * @param size   Specifies the size of the icon to find in the stream.
     */
    public Icon(InputStream stream, Dimension size) throws IOException
    {
        load(stream, size);
    }

    /**
     * Creates an icon from the standard Swing Icon.
     *
     * @param c Specifies the component for a paintIcon method implementation.
     * If the implementation allows null component argument, this parameter may be null.
     * @param icon Specifies the standard Swing Icon.
     */
    public Icon(Component c, javax.swing.Icon icon)
    {
        this();
        load(c, icon);
    }

    /**
     * Creates an icon from the standard Swing Icon.
     *
     * @param c Specifies the component for a paintIcon method implementation.
     * If the implementation allows null component argument, this parameter may be null.
     * @param icon Specifies the standard Swing Icon.
     */
    private void load(Component c, javax.swing.Icon icon)
    {
        int width = icon.getIconWidth();
        int height = icon.getIconHeight();
        BufferedImage colorImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        BufferedImage maskImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        icon.paintIcon(c, colorImage.getGraphics(), 0, 0);

        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                if (colorImage.getRGB(i,j) == 0)
                {
                    maskImage.setRGB(i, j, 255 | 255 << 8 | 255 << 16);
                }
                else
                {
                    maskImage.setRGB(i, j, 0);
                }
            }
        }

        Bitmap colorBitmap = new DDBitmap(colorImage);
        Bitmap maskBitmap = new DDBitmap(maskImage);
        final IconInfo iconInfo = new IconInfo(maskBitmap, colorBitmap);
        final Function createIconIndirect = getFunction(FUNCTION_CreateIconIndirect);
        try
        {
            long errorCode = createIconIndirect.invoke(this, new Pointer(iconInfo));

            if (isNull())
            {
                throw new LastErrorException(errorCode, "Failed to create icon from the resource.");
            }
        }
        finally
        {
            colorBitmap.deleteObject();
            maskBitmap.deleteObject();
        }
    }

    /**
     * Loads the icon from the specified stream.
     *
     * @param stream Specifies the stream to load the icon from.
     * @param size   Specifies the size of the icon to find in the stream.
     */
    public void load(InputStream stream, Dimension size) throws IOException
    {
        loadFromStream(stream, size, -1);
    }

    /**
     * Loads the icon from the specified stream.
     *
     * @param stream    Specifies the stream to load the icon from.
     * @param iconIndex Specifies the index of the icon in the stream.
     */
    public void load(InputStream stream, int iconIndex) throws IOException
    {
        loadFromStream(stream, null, iconIndex);
    }

    private void loadFromStream(InputStream stream, Dimension size, int iconIndex)
    {
        if (stream == null)
        {
            throw new IllegalArgumentException();
        }

        try
        {
            byte[] bytes = StreamUtils.readBytes(stream);

            if (bytes.length == 0)
            {
                throw new IllegalArgumentException("Input Stream is empty.");
            }

            final IconDir iconDir = new IconDir();
            iconDir.read(bytes, 0);

            final int count = iconDir.getCount();
            iconDir.getEntries().setElementCount(count);

            final ComplexArray entries = iconDir.getEntries();
            boolean resourceFound = false;
            for (int i = 0; i < count; i++)
            {
                final IconDirEntry iconDirEntry = (IconDirEntry)entries.getElement(i);
                int offset = iconDir.getLength() + i * iconDirEntry.getLength();
                iconDirEntry.read(bytes, offset);

                final int width = iconDirEntry.getWidth();
                final int height = iconDirEntry.getHeight();

                if (size == null)
                {
                    if (i != iconIndex)
                    {
                        continue;
                    }
                }
                else
                {
                    boolean sizeEquals = width == size.width && height == size.height;
                    if (!sizeEquals)
                    {
                        continue;
                    }
                }

                resourceFound = true;

                int imageDataOffset = iconDirEntry.getImageOffset();
                int imageDataSize = iconDirEntry.getBytesInRes();

                final byte[] imageData = new byte[imageDataSize];
                System.arraycopy(bytes, imageDataOffset, imageData, 0, imageDataSize);

                offset = 0;
                final BitmapInfo bitmapInfo = new BitmapInfo();
                bitmapInfo.read(imageData, offset);

                final BitmapInfoHeader bitmapInfoHeader = bitmapInfo.getBitmapInfoHeader();
                offset += bitmapInfoHeader.getLength();

                int colorsUsed = (int)bitmapInfoHeader.getClrUsed();
                final long bitCount = bitmapInfoHeader.getBitCount();

                if (colorsUsed == 0 && bitCount == 8)
                {
                    colorsUsed = 256;
                }
                if (colorsUsed == 0 && bitCount == 4)
                {
                    colorsUsed = 16;
                }

                List colors = colorsUsed > 0 ? new ArrayList(colorsUsed) : Collections.EMPTY_LIST;
                if (colorsUsed > 0)
                {
                    for (int index = 0; index < colorsUsed; index++)
                    {
                        final RGBQuad rgbQuad = new RGBQuad();
                        rgbQuad.read(imageData, offset);
                        colors.add(rgbQuad);
                        offset += rgbQuad.getLength();
                    }
                }
                int bitmapHeight = (int)bitmapInfoHeader.getHeight();
                int scanLines = bitmapHeight / 2;
                int bitmapWidth = (int)bitmapInfoHeader.getWidth();
                int scanLineSizeInBytes = (int)((bitmapWidth * bitCount) / 8);

                int xorBytesCount = scanLineSizeInBytes * scanLines;

                byte[] xorBytes = new byte[xorBytesCount];
                System.arraycopy(imageData, offset, xorBytes, 0, xorBytesCount);

                offset += xorBytesCount;

                BitmapInfo xorBitmapInfo = new BitmapInfo(colorsUsed);

                BitmapInfoHeader xorBitmapInfoHeader = xorBitmapInfo.getBitmapInfoHeader();
                xorBitmapInfoHeader.setWidth(bitmapWidth);
                xorBitmapInfoHeader.setHeight(scanLines);
                xorBitmapInfoHeader.setPlanes(1);
                xorBitmapInfoHeader.setBitCount(bitCount);
                xorBitmapInfoHeader.setCompression(Bitmap.Compression.RGB);
                xorBitmapInfoHeader.setSizeImage(0);
                if (colorsUsed > 0)
                {
                    xorBitmapInfoHeader.setClrUsed(colorsUsed);
                    xorBitmapInfoHeader.setClrImportant(colorsUsed);
                    final PrimitiveArray xorColors = xorBitmapInfo.getColors();
                    for (int index = 0; index < colorsUsed; index++)
                    {
                        xorColors.setElement(index, (Parameter)colors.get(index));
                    }
                }

                final DC dc = new WindowDC(null);
                DC compatibleDC = DC.createCompatibleDC(dc);

                final Bitmap xorBitmap = new DDBitmap(dc, width, scanLines);
                final PrimitiveArray xorBitmapData = new PrimitiveArray(xorBytes, Int8.class);
                xorBitmap.setDIBits(compatibleDC, 0, scanLines, new Pointer(xorBitmapData), xorBitmapInfo, Bitmap.DIB_RGB_COLORS);

                scanLineSizeInBytes = ((bitmapWidth + 31) / 32) * 4;
                int andBytesCount = scanLines * scanLineSizeInBytes;
                byte[] andBytes = new byte[andBytesCount];
                System.arraycopy(imageData, offset, andBytes, 0, andBytesCount);

                final BitmapInfo andBitmapInfo = new BitmapInfo(2);
                final BitmapInfoHeader andBitmapInfoHeader = andBitmapInfo.getBitmapInfoHeader();
                andBitmapInfoHeader.setWidth(width);
                andBitmapInfoHeader.setHeight(scanLines);
                andBitmapInfoHeader.setPlanes(1);
                andBitmapInfoHeader.setBitCount(1);
                andBitmapInfoHeader.setCompression(Bitmap.Compression.RGB);
                andBitmapInfoHeader.setSizeImage(0);
                andBitmapInfoHeader.setClrUsed(2);
                andBitmapInfoHeader.setClrImportant(2);
                final PrimitiveArray andColors = andBitmapInfo.getColors();
                andColors.setElement(0, new RGBQuad(0, 0, 0));
                andColors.setElement(1, new RGBQuad(0xFF, 0xFF, 0xFF));

                final PrimitiveArray andBitmapData = new PrimitiveArray(andBytes, UInt8.class);
                final Bitmap andBitmap = new DDBitmap(dc, width, scanLines);
                andBitmap.setDIBits(compatibleDC, 0, scanLines, new Pointer(andBitmapData), andBitmapInfo, Bitmap.DIB_RGB_COLORS);

                compatibleDC.release();
                dc.release();

                final IconInfo iconInfo = new IconInfo(andBitmap, xorBitmap);
                final Function createIconIndirect = getFunction(FUNCTION_CreateIconIndirect);
                try
                {
                    long errorCode = createIconIndirect.invoke(this, new Pointer(iconInfo));

                    if (isNull())
                    {
                        throw new LastErrorException(errorCode, "Failed to create icon from the resource.");
                    }
                }
                finally
                {
                    xorBitmap.deleteObject();
                    andBitmap.deleteObject();
                }

                break;
            }

            if (!resourceFound)
            {
                throw new RuntimeException("Resource not found.");
            }
        }
        catch (Exception e)
        {
            LOG.error("", e);
        }
        update();
    }

    /**
     * Returns the icon information.
     *
     * @return the icon information.
     */
    public IconInfo getIconInfo()
    {
        IconInfo result = new IconInfo();
        final Function function = getFunction(FUNCTION_GET_ICON_INFO);
        Bool returnValue = new Bool();
        long errorCode = function.invoke(returnValue, this, new Pointer(result));
        if (!returnValue.getValue())
        {
            throw new LastErrorException(errorCode, "Failed to retrieve IconInfo for this icon.");
        }
        return result;
    }

    /**
     * Returns the size of the icon.
     *
     * @return the size of the icon.
     */
    public Dimension getSize()
    {
        return _size;
    }


    public int getBitCount()
    {
        return _bitCount;
    }

    /**
     * Converts this icon to Java {@link java.awt.image.BufferedImage}.
     *
     * @return image instance created from this icon.
     */
    public BufferedImage toImage()
    {
        final IconInfo iconInfo = getIconInfo();
        Bitmap mask = iconInfo.getMaskBitmap();
        Bitmap color = iconInfo.getColorBitmap();
        WindowDC dc = new WindowDC(null);
        BitmapStructure bitmapStructure = new BitmapStructure();
        getObject(color, bitmapStructure);
        final int bitCount = (int)bitmapStructure.getBitsPixel();
        BitmapInfo bitmapInfo = bitCount >= 16 ? new BitmapInfo() : new BitmapInfo(bitCount);
        BitmapInfoHeader bitmapInfoHeader = bitmapInfo.getBitmapInfoHeader();
        final int width = (int)bitmapStructure.getBitmapWidth();
        final int height = (int)bitmapStructure.getBitmapHeight();
        // fill bitmap info
        bitmapInfoHeader.setWidth(width);
        bitmapInfoHeader.setHeight(height);
        bitmapInfoHeader.setPlanes(1);
        bitmapInfoHeader.setBitCount(bitCount);
        bitmapInfoHeader.setCompression(Bitmap.Compression.RGB);
        if (bitCount == 4 || bitCount == 8)
        {
            final int clrUsed = 1 << bitCount;
            bitmapInfoHeader.setClrUsed(clrUsed);
            bitmapInfoHeader.setClrImportant(clrUsed);
        }
        // retrieve bytes count
        final Pointer NULL = new Pointer(null, true);
        color.getDIBits(dc, 0, height, NULL, bitmapInfo, Bitmap.DIB_RGB_COLORS);
        // retrieve bytes from bitmap
        int bytesCount = (int)bitmapInfoHeader.getSizeImage();
        PrimitiveArray colorBits = new PrimitiveArray(UInt8.class, bytesCount);
        color.getDIBits(dc, 0, height, new Pointer(colorBits), bitmapInfo, Bitmap.DIB_RGB_COLORS);

        PrimitiveArray maskBits = new PrimitiveArray(UInt8.class, bytesCount);
        mask.getDIBits(dc, 0, height, new Pointer(maskBits), bitmapInfo, Bitmap.DIB_RGB_COLORS);

        dc.release();
        // transform data
        PrimitiveArray colors = bitmapInfo.getColors();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        int index1 = 0;
        int index2 = 0;
        for (int y = height - 1; y >= 0; y--)
        {
            for (int x = 0; x < width; x++)
            {
                int rgb1 = 0;
                int rgb2 = 0;
                switch (bitCount)
                {
                    case 8: // 256 colors, used pallete
                        {
                            RGBQuad pixelColor1 = (RGBQuad)colors.getElement(index1++);
                            int r1 = (int)pixelColor1.getRed();
                            int g1 = (int)pixelColor1.getGreen();
                            int b1 = (int)pixelColor1.getBlue();
                            rgb1 = 0xFF000000 | b1 | g1 | r1;
                            RGBQuad pixelColor2 = (RGBQuad)colors.getElement(index2++);
                            int r2 = (int)pixelColor2.getRed();
                            int g2 = (int)pixelColor2.getGreen();
                            int b2 = (int)pixelColor2.getBlue();
                            rgb2 = b2 | g2 | r2;
                            break;
                        }
                    case 16:// 2 bytes per pixel, 5-5-5 bits per pixel
                        {
                            int byte1 = readByte(colorBits, index1++);
                            int byte2 = readByte(colorBits, index1++);
                            int word = byte2 << 8 | byte1;
                            int b1 = (word & 0x1F) << 3;
                            int g1 = (word & 0x3E0) << 6;
                            int r1 = (word & 0x7C00) << 9;
                            rgb1 = 0xFF000000 | b1 | g1 | r1;
                            byte1 = readByte(maskBits, index2++);
                            byte2 = readByte(maskBits, index2++);
                            word = byte2 << 8 | byte1;
                            int b2 = (word & 0x1F) << 3;
                            int g2 = (word & 0x3E0) << 6;
                            int r2 = (word & 0x7C00) << 9;
                            rgb2 = b2 | g2 | r2;
                            break;
                        }
                    case 24:// 3 bytes per pixel
                    case 32:// 4 bytes per pixel, high byte used as alpha
                        {
                            int b1 = readByte(colorBits, index1++);
                            int g1 = readByte(colorBits, index1++);
                            int r1 = readByte(colorBits, index1++);
                            rgb1 = b1 | g1 << 8 | r1 << 16;
                            if (bitCount == 32)
                            {
                                int alpha = readByte(colorBits, index1++);
                                // XP returns valid alpha-channel, but other OS'es return 0 always
                                if (alpha == 0)
                                {
                                    alpha = 0xFF;
                                }
                                rgb1 |= alpha << 24;
                            }
                            else
                            {
                                rgb1 |= 0xFF000000;
                            }
                            int b2 = readByte(maskBits, index2++);
                            int g2 = readByte(maskBits, index2++);
                            int r2 = readByte(maskBits, index2++);
                            rgb2 = b2 | g2 << 8 | r2 << 16;
                            if (bitCount == 32)
                            {
                                index2++;
                            }
                        }
                }
                if (rgb2 != 0)
                {
                    result.setRGB(x, y, rgb2);
                }
                else
                {
                    result.setRGB(x, y, rgb1);
                }
            }
        }
        return result;
    }

    private static int readByte(PrimitiveArray bits, int offset)
    {
        final UInt8 element = (UInt8)bits.getElement(offset);
        return (int)element.getValue();
    }

    /*
     * The SystemIcon class represents the enumeration of standard system icons.
     */
    public static class SystemIcon extends EnumItem
    {
        public static final SystemIcon SAMPLE = new SystemIcon(32512);
        public static final SystemIcon HAND = new SystemIcon(32513);
        public static final SystemIcon QUES = new SystemIcon(32514);
        public static final SystemIcon BANG = new SystemIcon(32515);
        public static final SystemIcon NOTE = new SystemIcon(32516);
        public static final SystemIcon WINLOGO = new SystemIcon(32517);

        private Icon _smallIcon;
        private Icon _bigIcon;

        private SystemIcon(int value)
        {
            super(value);
        }

        /**
         * Loads the system icon.
         *
         * @param iconID Specifies the icon identifier.
         * @param size   Specifies the size of the icon.
         * @return System icon
         */
        public static Icon loadSystemIcon(int iconID, Dimension size)
        {
            Icon result = new Icon();
            final Function function = getFunction(FUNCTION_LOAD_IMAGE);
            long errorCode = function.invoke(result, new Parameter[]
            {
                new Handle(),
                new Int32(iconID),
                new UInt32(ImageType.ICON.getValue()),
                new Int(size.width),
                new Int(size.height),
                new UInt32(ImageLoadParameters.SHARED)
            });
            if (result.isNull())
            {
                throw new LastErrorException(errorCode, "Failed to load icon for " + iconID);
            }
            return result;
        }

        public Icon getIcon(IconType type)
        {
            Icon result = null;
            if (type.equals(IconType.SMALL))
            {
                if (_smallIcon == null)
                {
                    _smallIcon = loadSystemIcon(getValue(), type.getSize());
                }
                result = _smallIcon;
            }
            else if (type.equals(IconType.BIG))
            {
                if (_bigIcon == null)
                {
                    _bigIcon = loadSystemIcon(getValue(), type.getSize());
                }
                result = _bigIcon;
            }
            return result;
        }

        public Icon getSmall()
        {
            return getIcon(IconType.SMALL);
        }

        public Icon getBig()
        {
            return getIcon(IconType.BIG);
        }
    }

    /**
     * IconType represents the enumeration of default icon types.
     */
    public static class IconType extends EnumItem
    {
        /**
         * Corresponds to <code>ICON_SMALL</code> type.
         */
        public static final IconType SMALL = new IconType(0, new Dimension(16, 16));

        /**
         * Corresponds to <code>ICON_BIG</code> type.
         */
        public static final IconType BIG = new IconType(1, new Dimension(32, 32));

        private Dimension _size;

        private IconType(int value, Dimension size)
        {
            super(value);
            _size = size;
        }

        /**
         * Returns the size associated with a currect icon type.
         *
         * @return the size associated with a currect icon type.
         */
        public Dimension getSize()
        {
            return _size;
        }
    }

    /**
     * Loads the collection of all icons from the specified input stream.
     *
     * @param stream streams that contains icons
     * @return list of Icon objects
     */
    public static List loadFromStream(InputStream stream) {
        if (stream == null) {
            throw new IllegalArgumentException();
        }

        List result = new LinkedList();

        try {
            Function createIconIndirect = User32.getInstance().getFunction(FUNCTION_CreateIconIndirect);

            byte[] bytes = StreamUtils.readBytes(stream);

            if (bytes.length == 0) {
                throw new IllegalArgumentException("Input Stream is empty.");
            }

            IconDir iconDir = new IconDir();
            iconDir.read(bytes, 0);

            int count = iconDir.getCount();
            iconDir.getEntries().setElementCount(count);

            ComplexArray entries = iconDir.getEntries();
            for (int i = 0; i < count; i++) {
                IconDirEntry iconDirEntry = (IconDirEntry) entries.getElement(i);
                int offset = iconDir.getLength() + i * iconDirEntry.getLength();
                iconDirEntry.read(bytes, offset);

                int width = iconDirEntry.getWidth();
                int height = iconDirEntry.getHeight();

                if (width == 0 && height == 0) {
                    continue;
                }

                int imageDataOffset = iconDirEntry.getImageOffset();
                int imageDataSize = iconDirEntry.getBytesInRes();

                byte[] imageData = new byte[imageDataSize];
                System.arraycopy(bytes, imageDataOffset, imageData, 0, imageDataSize);

                offset = 0;
                BitmapInfo bitmapInfo = new BitmapInfo();
                bitmapInfo.read(imageData, offset);

                BitmapInfoHeader bitmapInfoHeader = bitmapInfo.getBitmapInfoHeader();
                offset += bitmapInfoHeader.getLength();

                int colorsUsed = (int) bitmapInfoHeader.getClrUsed();
                long bitCount = bitmapInfoHeader.getBitCount();

                if (colorsUsed == 0 && bitCount == 8) {
                    colorsUsed = 256;
                }
                if (colorsUsed == 0 && bitCount == 4) {
                    colorsUsed = 16;
                }

                List colors = colorsUsed > 0 ? new ArrayList(colorsUsed) : Collections.EMPTY_LIST;
                if (colorsUsed > 0) {
                    for (int index = 0; index < colorsUsed; index++) {
                        RGBQuad rgbQuad = new RGBQuad();
                        rgbQuad.read(imageData, offset);
                        colors.add(rgbQuad);
                        offset += rgbQuad.getLength();
                    }
                }
                int bitmapHeight = (int) bitmapInfoHeader.getHeight();
                int scanLines = bitmapHeight / 2;
                int bitmapWidth = (int) bitmapInfoHeader.getWidth();
                int scanLineSizeInBytes = (int) ((bitmapWidth * bitCount) / 8);

                int xorBytesCount = scanLineSizeInBytes * scanLines;

                byte[] xorBytes = new byte[xorBytesCount];
                System.arraycopy(imageData, offset, xorBytes, 0, xorBytesCount);

                offset += xorBytesCount;

                BitmapInfo xorBitmapInfo = new BitmapInfo(colorsUsed);

                BitmapInfoHeader xorBitmapInfoHeader = xorBitmapInfo.getBitmapInfoHeader();
                xorBitmapInfoHeader.setWidth(bitmapWidth);
                xorBitmapInfoHeader.setHeight(scanLines);
                xorBitmapInfoHeader.setPlanes(1);
                xorBitmapInfoHeader.setBitCount(bitCount);
                xorBitmapInfoHeader.setCompression(Bitmap.Compression.RGB);
                xorBitmapInfoHeader.setSizeImage(0);
                if (colorsUsed > 0) {
                    xorBitmapInfoHeader.setClrUsed(colorsUsed);
                    xorBitmapInfoHeader.setClrImportant(colorsUsed);
                    PrimitiveArray xorColors = xorBitmapInfo.getColors();
                    for (int index = 0; index < colorsUsed; index++) {
                        xorColors.setElement(index, (Parameter) colors.get(index));
                    }
                }

                DC dc = new WindowDC(null);
                DC compatibleDC = DC.createCompatibleDC(dc);

                Bitmap xorBitmap = new DDBitmap(dc, width, scanLines);
                PrimitiveArray xorBitmapData = new PrimitiveArray(xorBytes, Int8.class);
                xorBitmap.setDIBits(compatibleDC, 0, scanLines, new Pointer(xorBitmapData), xorBitmapInfo, Bitmap.DIB_RGB_COLORS);

                scanLineSizeInBytes = ((bitmapWidth + 31) / 32) * 4;
                int andBytesCount = scanLines * scanLineSizeInBytes;
                byte[] andBytes = new byte[andBytesCount];
                System.arraycopy(imageData, offset, andBytes, 0, andBytesCount);

                BitmapInfo andBitmapInfo = new BitmapInfo(2);
                BitmapInfoHeader andBitmapInfoHeader = andBitmapInfo.getBitmapInfoHeader();
                andBitmapInfoHeader.setWidth(width);
                andBitmapInfoHeader.setHeight(scanLines);
                andBitmapInfoHeader.setPlanes(1);
                andBitmapInfoHeader.setBitCount(1);
                andBitmapInfoHeader.setCompression(Bitmap.Compression.RGB);
                andBitmapInfoHeader.setSizeImage(0);
                andBitmapInfoHeader.setClrUsed(2);
                andBitmapInfoHeader.setClrImportant(2);
                PrimitiveArray andColors = andBitmapInfo.getColors();
                andColors.setElement(0, new RGBQuad(0, 0, 0));
                andColors.setElement(1, new RGBQuad(0xFF, 0xFF, 0xFF));

                PrimitiveArray andBitmapData = new PrimitiveArray(andBytes, UInt8.class);
                Bitmap andBitmap = new DDBitmap(dc, width, scanLines);
                andBitmap.setDIBits(compatibleDC, 0, scanLines, new Pointer(andBitmapData), andBitmapInfo, Bitmap.DIB_RGB_COLORS);

                compatibleDC.release();
                dc.release();

                IconInfo iconInfo = new IconInfo(andBitmap, xorBitmap);
                try {
                    Icon icon = new Icon();
                    long errorCode = createIconIndirect.invoke(icon, new Pointer(iconInfo));
                    if (icon.isNull()) {
                        throw new LastErrorException(errorCode, "Failed to create icon from the resource.");
                    }
                    icon._size.setSize(width, height);
                    icon._bitCount = (int) bitCount;
                    result.add(icon);
                }
                finally {
                    xorBitmap.deleteObject();
                    andBitmap.deleteObject();
                }
            }
        }
        catch (Exception e) {
            LOG.error("", e);
        }
        return result;
    }
}
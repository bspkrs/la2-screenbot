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
import com.jniwrapper.util.Enums;
import com.jniwrapper.win32.*;
import com.jniwrapper.win32.ui.User32;

import java.awt.Color;

/**
 * This class provides methods for working with Windows device contexts (DC). It
 * corresponds to HDC Windows data type.
 *
 * @author Serge Piletsky
 */
public class DC extends Handle
{
    public static final int DC_PEN = 19;

    static final String FUNCTION_CREATE_COMPATIBLE_DC = "CreateCompatibleDC";
    static final String FUNCTION_SELECT_OBJECT = "SelectObject";
    static final String FUNCTION_BIT_BLT = "BitBlt";
    static final String FUNCTION_STRETCH_BLT = "StretchBlt";
    static final String FUNCTION_PAT_BLT = "PatBlt";
    static final String FUNCTION_DELETE_DC = "DeleteDC";
    static final String FUNCTION_SET_DC_BRUSH_COLOR = "SetDCBrushColor";
    static final String FUNCTION_SET_DC_PEN_COLOR = "SetDCPenColor";

    static final FunctionName FUNCTION_TEXT_OUT = new FunctionName("TextOut");
    static final FunctionName FUNCTION_DRAW_TEXT = new FunctionName("DrawText");
    static final FunctionName FUNCTION_GET_TEXT_EXTENT_POINT_32 = new FunctionName("GetTextExtentPoint32");


    static final String FUNCTION_DRAW_ICON_EX = "DrawIconEx";
    static final String FUNCTION_DRAW_ICON = "DrawIcon";
    static final String FUNCTION_SET_BACKGROUND_MODE = "SetBkMode";
    static final String FUNCTION_SET_BACKGROUND_COLOR = "SetBkColor";
    static final String FUNCTION_SET_TEXT_COLOR = "SetTextColor";

    static final String FUNCTION_SELECT_CLIP_RGN = "SelectClipRgn";
    static final String FUNCTION_FILL_REGION = "FillRgn";
    static final String FUNCTION_FRAME_RGN = "FrameRgn";
    static final String FUNCTION_GET_POLY_FILL_MODE = "GetPolyFillMode";
    static final String FUNCTION_INVERT_RGN = "InvertRgn";
    static final String FUNCTION_PAINT_RGN = "PaintRgn";
    static final String FUNCTION_SET_POLY_FILL_MODE = "SetPolyFillMode";
    static final String FUNCTION_GET_PIXEL = "GetPixel";

    private static final String FUNCTION_GRADIENT_FILL = "GradientFill";
    private static final String FUNCTION_LINE_TO = "LineTo";
    private static final String FUNCTION_MOVE_TO_EX = "MoveToEx";

    private static final String FUNCTION_RECTANGLE = "Rectangle";
    private static final String FUNCTION_ROUND_RECT = "RoundRect";
    private static final String FUNCTION_FILL_RECT = "FillRect";

    private static final String FUNCTION_SetMapMode = "SetMapMode";
    private static final String FUNCTION_GetDeviceCaps = "GetDeviceCaps";
    private static final String FUNCTION_IntersectClipRect = "IntersectClipRect";

    /**
     * Background modes.
     */
    public static final int BKMODE_TRANSPARENT = 1;
    public static final int BKMODE_OPAQUE = 2;
    public static final int BKMODE_LAST = 2;
    private static final String FUNCTION_ALPHA_BLEND = "AlphaBlend";

    public static final long GRADIENT_FILL_RECT_H = 0x00000000;
    public static final long GRADIENT_FILL_RECT_V = 0x00000001;

    public static final int DT_RIGHT = 0x00000002;

    public DC()
    {
        super();
    }

    public DC(long value)
    {
        super(value);
    }

    /**
     * Creates a compatible DC for a given DC.
     *
     * @param dc DC to create a compatible one from.
     * @return a compatible DC.
     */
    public static DC createCompatibleDC(DC dc)
    {
        Function function = Gdi32.getInstance().getFunction(FUNCTION_CREATE_COMPATIBLE_DC);
        DC result = new DC();
        function.invoke(result, dc == null ? (Parameter)new Pointer(null, true) : dc);
        return result;
    }

    /**
     * @return the result of execution of the <code>SelectObject</code> API
     *         function.
     */
    private long selectObject(GdiObject object)
    {
        Function function = Gdi32.get(FUNCTION_SELECT_OBJECT);
        Handle result = new Handle();
        function.invoke(result, this, object);
        return result.getValue();
    }

    /**
     * Selects a specified brush in the device context.
     *
     * @param brush a brush to select.
     * @return the previous brush.
     */
    public Brush selectObject(Brush brush)
    {
        final long handle = selectObject((GdiObject)brush);
        final Brush result = new Brush(handle);
        return result;
    }

    /**
     * Selects a specified pen in the device context.
     *
     * @param pen a pen to select.
     * @return the previous pen.
     */
    public Pen selectObject(Pen pen)
    {
        final long handle = selectObject((GdiObject)pen);
        final Pen result = new Pen(handle);
        return result;
    }

    /**
     * Selects a specified bitmap in the device context.
     *
     * @param bitmap a bitmap to select.
     * @return the previous bitmap.
     */
    public Bitmap selectObject(Bitmap bitmap)
    {
        final long handle = selectObject((GdiObject)bitmap);
        final Bitmap result = new DDBitmap(handle);
        return result;
    }

    /**
     * Selects a specified font in the device context.
     *
     * @param font a font to select.
     * @return the previous font.
     */
    public Font selectObject(Font font)
    {
        final long handle = selectObject((GdiObject)font);
        final Font result = new Font(handle);
        return result;
    }

    /**
     * Selects the passed region in the device context.
     *
     * @param region the region to select
     * @return region
     */
    public Region selectObject(Region region)
    {
        final long handle = selectObject((GdiObject)region);
        final Region result = new Region(handle);
        return result;
    }

    /**
     * Performs a bit-block transfer of the color data corresponding to a
     * rectangle of pixels from the specified source device context into a
     * destination device context. All coordinate parameters are in logical
     * units. <br> <b>NOTE:</b> <code>bitBlt</code> does only clipping on the
     * destination DC.<br>
     *
     * @param xDest  the x-coordinate of the upper-left corner of the destination
     *               rectangle.
     * @param yDest  the y-coordinate of the upper-left corner of the destination
     *               rectangle.
     * @param width  the width of the source and destination rectangles.
     * @param height the height of the source and the destination rectangles.
     * @param xSrc   the x-coordinate of the upper-left corner of the source
     *               rectangle.
     * @param ySrc   the y-coordinate of the upper-left corner of the source
     *               rectangle.
     * @param rop    a raster-operation code.
     */
    public static void bitBlt(DC hdcDest,
                              int xDest,
                              int yDest,
                              int width,
                              int height,
                              DC hdcSrc,
                              int xSrc,
                              int ySrc,
                              RasterOperation rop)
    {
        Function function = Gdi32.get(FUNCTION_BIT_BLT);
        function.invoke(null, new Parameter[]
        {
            hdcDest,
            new Int(xDest),
            new Int(yDest),
            new Int(width),
            new Int(height),
            hdcSrc,
            new Int(xSrc),
            new Int(ySrc),
            new UInt32(rop.getValue())
        });
    }

    /**
     * Copies a bitmap from a source rectangle into a destination rectangle,
     * stretching or shrinking to fit the dimensions of the destination
     * rectangle, if necessary. The image transformation is determined by the
     * stretching mode currently set in the device context. All coordinate
     * parameters are in logical units.
     *
     * @param hDCDest     a destination device context.
     * @param xOriginDest the x-coordinate of the upper-left corner of the
     *                    destination rectangle.
     * @param yOriginDest the y-coordinate of the upper-left corner of the
     *                    destination rectangle.
     * @param widthDest   the width of the destination rectangle.
     * @param heightDest  the height of the destination rectangle.
     * @param hDCSrc      source device context.
     * @param xOriginSrc  the x-coordinate of the upper-left corner of the source
     *                    rectangle.
     * @param yOriginSrc  the y-coordinate of the upper-left corner of the source
     *                    rectangle.
     * @param widthSrc    specifies the width of the source rectangle.
     * @param heightSrc   specifies the height of the source rectangle.
     * @param rasterOp    specifies the raster operation to be performed. Raster
     *                    operation codes define how the system combines colors in output
     *                    operations that involve a brush, a source bitmap, and a destination
     *                    bitmap.
     * @return nonzero if the method succeeds, zero if otherwise.
     */
    public static boolean stretchBlt(DC hDCDest,
                                     int xOriginDest,
                                     int yOriginDest,
                                     int widthDest,
                                     int heightDest,
                                     DC hDCSrc,
                                     int xOriginSrc,
                                     int yOriginSrc,
                                     int widthSrc,
                                     int heightSrc,
                                     RasterOperation rasterOp)
    {
        Int result = new Int();
        Function function = Gdi32.get(FUNCTION_STRETCH_BLT);
        function.invoke(result, new Parameter[]
        {
            hDCDest,
            new Int(xOriginDest),
            new Int(yOriginDest),
            new Int(widthDest),
            new Int(heightDest),
            hDCSrc,
            new Int(xOriginSrc),
            new Int(yOriginSrc),
            new Int(widthSrc),
            new Int(heightSrc),
            new UInt32(rasterOp.getValue())
        });
        return result.getValue() > 0;
    }

    /**
     * This method paints the specified rectangle using the brush that is
     * currently selected into the specified device context. The brush color and
     * the surface color or colors are combined by using the specified raster
     * operation. <br><br> <b>NOTE:</b> The values of the <code>rastOps</code>
     * parameter for this method are a limited subset of the full 256 ternary
     * raster-operation codes; in particular, an operation code that refers to a
     * source rectangle cannot be used.
     *
     * @param xLeft    specifies the x-coordinate, in logical units, of the
     *                 upper-left corner of the rectangle to be filled.
     * @param yLeft    specifies the y-coordinate, in logical units, of the
     *                 upper-left corner of the rectangle to be filled.
     * @param width    specifies the width, in logical units, of the rectangle.
     * @param height   specifies the height, in logical units, of the rectangle.
     * @param rasterOp specifies the raster operation code. This code can be one
     *                 of the following values:<br> PATCOPY - Copies the specified pattern into
     *                 the destination bitmap.<br> PATINVERT - Combines the colors of the
     *                 specified pattern with the colors of the destination rectangle by using
     *                 the Boolean XOR operator.<br> DSTINVERT - Inverts the destination
     *                 rectangle.<br> BLACKNESS - Fills the destination rectangle using the
     *                 color associated with index 0 in the physical palette. (This color is
     *                 black for the default physical palette.)<br> WHITENESS - Fills the
     *                 destination rectangle using the color associated with index 1 in the
     *                 physical palette. (This color is white for the default physical palette.)
     * @return If the method succeeds, the return value is non-zero. If the
     *         method fails, the return value is zero.
     */
    public boolean patBlt(int xLeft,
                          int yLeft,
                          int width,
                          int height,
                          RasterOperation rasterOp)
    {
        Bool result = new Bool();
        Function function = Gdi32.get(FUNCTION_PAT_BLT);
        function.invoke(result,
                new Parameter[]
                {
                    this,
                    new Int(xLeft),
                    new Int(yLeft),
                    new Int(width),
                    new Int(height),
                    new UInt32(rasterOp.getValue())
                });
        return result.getValue();
    }

    /**
     * Releases the current DC by calling its delete method.
     */
    public void release()
    {
        deleteDC(this);
    }

    /**
     * Deletes the specified DC.
     *
     * @param dc DC to be deleted.
     */
    public static void deleteDC(DC dc)
    {
        Function function = Gdi32.get(FUNCTION_DELETE_DC);
        Int result = new Int();
        long errorCode = function.invoke(result, dc);
        if (result.getValue() == 0)
        {
            throw new LastErrorException(errorCode);
        }
    }

    /**
     * Draws the text at given coordinates.
     *
     * @param x    the x-coordinate of the upper-left corner.
     * @param y    the y-coordinate of the upper-left corner.
     * @param text a text to display.
     */
    public void textOut(int x, int y, String text)
    {
        final Function function = Gdi32.get(FUNCTION_TEXT_OUT.toString());
        UShortInt returnValue = new UShortInt();
        long errorCode = function.invoke(returnValue, new Parameter[]
        {
            this,
            new Int(x),
            new Int(y),
            new Str(text),
            new Int(text.length())
        });
        if (returnValue.getValue() == 0)
        {
            throw new LastErrorException(errorCode);
        }
    }

    /**
     * Draws a specified text.
     *
     * @param text     a text to be drawn.
     * @param clipRect a clip rectangle for drawing.
     * @param uFormat  drawing format.
     */
    public void drawText(String text, Rect clipRect, UInt uFormat)
    {
        final Function function = User32.getInstance().getFunction(FUNCTION_DRAW_TEXT.toString());
        Int returnValue = new Int();
        long errorCode = function.invoke(returnValue, new Parameter[]
        {
            this,
            new Str(text),
            new Int(text.length()),
            new Pointer(clipRect),
            new UInt(uFormat)
        });

        if (returnValue.getValue() == 0)
        {
            throw new LastErrorException(errorCode);
        }
    }

    /**
     * Returns the size of a specified text.
     *
     * @param dc  unused
     * @param str a text to be measured.
     * @return the size of text in DC.
     */
    public Size getTextExtentPoint32(DC dc, String str)
    {
        Size result = new Size();
        Gdi32.getInstance().getFunction(FUNCTION_GET_TEXT_EXTENT_POINT_32.toString()).invoke(null,
                new Parameter[]{
                    this,
                    new Str(str),
                    new UInt(str.length()),
                    new Pointer(result),
                });

        return result;
    }

    /**
     * Draws a specified icon in this device context.
     *
     * @param x          the x-coordinate of the image.
     * @param y          the y-coordinate of the image.
     * @param icon       the icon to be drawn.
     * @param width      the width of the drawn icon.
     * @param heght      the height of the drawn icon.
     * @param frameIndex the frame index.
     * @param bgBrush    the background brush.
     * @param flags      drawing flags.
     */
    public void drawIconEx(int x,
                           int y,
                           Icon icon,
                           int width,
                           int heght,
                           int frameIndex,
                           Handle bgBrush,
                           int flags)
    {
        final Function function = User32.getInstance().getFunction(FUNCTION_DRAW_ICON_EX);
        UShortInt returnValue = new UShortInt();
        long errorCode = function.invoke(returnValue, new Parameter[]
        {
            this,
            new Int(x),
            new Int(y),
            icon,
            new Int(width),
            new Int(heght),
            new UInt(frameIndex),
            bgBrush,
            new UInt(flags)
        });
        if (returnValue.getValue() == 0)
        {
            throw new LastErrorException(errorCode);
        }
    }

    /**
     * Draws a specified icon in this device context.
     *
     * @param x    the x-coordinate of the image.
     * @param y    the y-coordinate of the image.
     * @param icon the icon to be drawn.
     * @return the result of drawing: true if succeeds; false if fails.
     */
    public boolean drawIcon(int x, int y, Icon icon)
    {
        final Function function = User32.getInstance().getFunction(FUNCTION_DRAW_ICON);
        Bool result = new Bool();
        function.invoke(result, this, new Int(x), new Int(y), icon);
        return result.getValue();
    }

    /**
     * Sets the background mode in this device context.
     *
     * @param bgMode a new background mode.
     * @return the previous background mode.
     */
    public int setBkMode(int bgMode)
    {
        final Function function = Gdi32.get(FUNCTION_SET_BACKGROUND_MODE);
        UShortInt returnValue = new UShortInt();
        long errorCode = function.invoke(returnValue, this, new Int(bgMode));
        if (returnValue.getValue() == 0)
        {
            throw new LastErrorException(errorCode);
        }

        return (int)returnValue.getValue();
    }

    /**
     * Sets the background color in this device context.
     *
     * @param color the background color.
     * @return the previous background color.
     */
    public ColorRef setBkColor(ColorRef color)
    {
        ColorRef returnValue = new ColorRef();
        final Function function = Gdi32.get(FUNCTION_SET_BACKGROUND_COLOR);
        long errorCode = function.invoke(returnValue, this, color);
        if (returnValue.isInvalid())
        {
            throw new LastErrorException(errorCode, "Error while changing text color");
        }

        return returnValue;
    }

    /**
     * Sets the text color in this device context.
     *
     * @param color the text color.
     * @return the previous text color.
     */
    public ColorRef setTextColor(ColorRef color)
    {
        ColorRef returnValue = new ColorRef();
        final Function function = Gdi32.get(FUNCTION_SET_TEXT_COLOR);
        long errorCode = function.invoke(returnValue, this, color);
        if (returnValue.isInvalid())
        {
            throw new LastErrorException(errorCode, "Error while changing text color");
        }

        return returnValue;
    }

    /**
     * Selects a given region as the current clipping region.
     *
     * @param region the region to be selected.
     */
    public void selectClipRgn(Region region)
    {
        Int resultRgnType = new Int();
        final Function function = Gdi32.getInstance().getFunction(FUNCTION_SELECT_CLIP_RGN);
        long errorCode = function.invoke(resultRgnType, this, region);
        if (Region.RGN_ERROR == resultRgnType.getValue())
        {
            throw new LastErrorException(errorCode, "Failed to select region");
        }
    }

    /**
     * Fills a region by using the specified brush.
     *
     * @param region the region to fill.
     * @param brush  a brush to be used for painting.
     */
    public void fillRegion(Region region, Brush brush)
    {
        Bool result = new Bool();
        final Function function = Gdi32.getInstance().getFunction(FUNCTION_FILL_REGION);
        long errorCode = function.invoke(result, this, region, brush);
        if (!result.getValue())
        {
            throw new LastErrorException(errorCode, "Failed to fill region");
        }
    }

    /**
     * Draws a border around the specified region by using the specified brush.
     *
     * @param region
     * @param brush
     * @param width
     * @param heigh
     */
    public void frameRegion(Region region, Brush brush, int width, int heigh)
    {
        Bool result = new Bool();
        final Function function = Gdi32.getInstance().getFunction(FUNCTION_FRAME_RGN);
        long errorCode = function.invoke(result, new Parameter[]
        {
            this,
            region,
            brush,
            new Int(width),
            new Int(heigh)
        });
        if (!result.getValue())
        {
            throw new LastErrorException(errorCode, "Failed to frame region");
        }
    }

    /**
     * Retrieves the current polygon fill mode.
     *
     * @return polygon fill mode.
     */
    public int getPolyFillMode()
    {
        Int result = new Int();
        final Function function = Gdi32.getInstance().getFunction(FUNCTION_GET_POLY_FILL_MODE);
        function.invoke(result, this);
        return (int)result.getValue();
    }

    /**
     * Inverts the colors in the specified region.
     *
     * @param region
     * @return If the function succeeds, the return value is true; otherwise
     *         false.
     */
    public boolean invertRegion(Region region)
    {
        Bool result = new Bool();
        final Function function = Gdi32.getInstance().getFunction(FUNCTION_INVERT_RGN);
        function.invoke(result, this, region);
        return result.getValue();
    }

    /**
     * Paints the specified region by using the brush currently selected into
     * the device context.
     *
     * @param region
     * @return If the function succeeds, the return value is true; otherwise
     *         false.
     */
    public boolean paintRegion(Region region)
    {
        Bool result = new Bool();
        final Function function = Gdi32.getInstance().getFunction(FUNCTION_PAINT_RGN);
        function.invoke(result, this, region);
        return result.getValue();
    }

    /**
     * Sets the polygon fill mode for functions that fill polygons.
     *
     * @param polyFillMode
     */
    public void setPolyFillMode(PolyFillMode polyFillMode)
    {
        final Function function = Gdi32.getInstance().getFunction(FUNCTION_SET_POLY_FILL_MODE);
        function.invoke(null, this, new Int(polyFillMode.getValue()));
    }

    /**
     * Returns a color (by ColorRef) of the specified pixel on this device
     * context.
     *
     * @param x the x-coordinate of pixel.
     * @param y the y-coordinate of pixel.
     * @return the color (by ColorRef) of the pixel.
     */
    public ColorRef getPixel(int x, int y)
    {
        ColorRef result = new ColorRef();
        final Function function = Gdi32.get(FUNCTION_GET_PIXEL);
        function.invoke(result, this, new Int(x), new Int(y));
        return result;
    }

    /**
     * This method sets the current device context (DC) brush color to the
     * specified color value. If the device cannot represent the specified color
     * value, the color is set to the nearest physical color.
     *
     * @param hDC    an instance of the {@link com.jniwrapper.win32.gdi.DC} class
     *               representing the current device context.
     * @param clrref an instance of the {@link com.jniwrapper.win32.gdi.ColorRef}
     *               class representing the color for the brush.
     * @return If the method succeeds, the return value specifies the previous
     *         DC brush color as a <code>ColorRef</code> value. If the method fails, the
     *         return value is CLR_INVALID.
     */
    public static ColorRef setDCBrushColor(DC hDC, ColorRef clrref)
    {
        ColorRef result = new ColorRef();
        Function function = Gdi32.get(FUNCTION_SET_DC_BRUSH_COLOR);
        function.invoke(result, hDC, clrref);
        return result;
    }

    /**
     * This method sets the current device context (DC) pen color to the
     * specified color value or the nearest to the specified color value in case
     * when the device cannot set the specified color value.
     *
     * @param hDC      an instance of the {@link com.jniwrapper.win32.gdi.DC} class.
     * @param colorRef an instance of the {@link com.jniwrapper.win32.gdi.ColorRef}
     *                 class.
     * @return If the method succeeds, the return value specifies the previous
     *         DC pen color as instance of the {@link com.jniwrapper.win32.gdi.ColorRef}
     *         class.
     */
    public ColorRef setDCPenColor(DC hDC, ColorRef colorRef)
    {
        ColorRef color = new ColorRef();
        Function function = Gdi32.get(FUNCTION_SET_DC_PEN_COLOR);
        function.invoke(color, hDC, colorRef);
        return color;
    }

    /**
     * The RasterOperation class represents the enumeration of raster operations.
     */
    public static class RasterOperation extends EnumItem
    {
        /**
         * Fills the destination rectangle using the color associated
         * with index 0 in the physical palette. (This color is black
         * for the default physical palette.)
         */
        public static final RasterOperation BLACKNESS = new RasterOperation(0x00000042);
        /**
         * Windows 98/Me, Windows 2000/XP: Includes any windows that are
         * layered on top of your window in the resulting image.
         * By default, the image only contains your window. Note that
         * this generally cannot be used for printing device contexts.
         */
        public static final RasterOperation CAPTUREBLT = new RasterOperation(0x40000000);
        /**
         * Inverts the destination rectangle.
         */
        public static final RasterOperation DSTINVERT = new RasterOperation(0x00550009);
        /**
         * Merges the colors of the source rectangle with the brush
         * currently selected in hdcDest, by using the Boolean AND operator.
         */
        public static final RasterOperation MERGECOPY = new RasterOperation(0x00C000CA);
        /**
         * Merges the colors of the inverted source rectangle with the
         * colors of the destination rectangle by using the Boolean OR operator.
         */
        public static final RasterOperation MERGEPAINT = new RasterOperation(0x00BB0226);
        /**
         * Windows 98/Me, Windows 2000/XP: Prevents the bitmap from being mirrored.
         */
        public static final RasterOperation NOMIRRORBITMAP = new RasterOperation(0x80000000);
        /**
         * Copies the inverted source rectangle to the destination.
         */
        public static final RasterOperation NOTSRCCOPY = new RasterOperation(0x00330008);
        /**
         * Combines the colors of the source and destination rectangles
         * by using the Boolean OR operator and then inverts the resultant color.
         */
        public static final RasterOperation NOTSRCERASE = new RasterOperation(0x001100A6);
        /**
         * Copies the brush currently selected in hdcDest, into the destination bitmap.
         */
        public static final RasterOperation PATCOPY = new RasterOperation(0x00F00021);
        /**
         * Combines the colors of the brush currently selected in hdcDest,
         * with the colors of the destination rectangle by using the Boolean XOR operator.
         */
        public static final RasterOperation PATINVERT = new RasterOperation(0x005A0049);
        /**
         * Combines the colors of the brush currently selected in hdcDest,
         * with the colors of the inverted source rectangle by using the
         * Boolean OR operator. The result of this operation is combined
         * with the colors of the destination rectangle by using the Boolean OR operator.
         */
        public static final RasterOperation PATPAINT = new RasterOperation(0x00FB0A09);
        /**
         * Combines the colors of the source and destination rectangles
         * by using the Boolean AND operator.
         */
        public static final RasterOperation SRCAND = new RasterOperation(0x008800C6);
        /**
         * Copies the source rectangle directly to the destination rectangle.
         */
        public static final RasterOperation SRCCOPY = new RasterOperation(0x00CC0020);
        /**
         * Combines the inverted colors of the destination rectangle with the
         * colors of the source rectangle by using the Boolean AND operator.
         */
        public static final RasterOperation SRCERASE = new RasterOperation(0x00440328);
        /**
         * Combines the colors of the source and destination rectangles by using
         * the Boolean XOR operator.
         */
        public static final RasterOperation SRCINVERT = new RasterOperation(0x00660046);
        /**
         * Combines the colors of the source and destination rectangles by using
         * the Boolean OR operator.
         */
        public static final RasterOperation SRCPAINT = new RasterOperation(0x00EE0086);
        /**
         * Fills the destination rectangle using the color associated with index 1
         * in the physical palette. (This color is white for the default physical palette.)
         */
        public static final RasterOperation WHITENESS = new RasterOperation(0x00FF0062);

        private RasterOperation(int value)
        {
            super(value);
        }

        /**
         * Represnts the OR operation.
         */
        public static RasterOperation or(RasterOperation[] operations)
        {
            int result = 0;
            for (int i = 0; i < operations.length; i++)
            {
                result |= operations[i].getValue();
            }
            return new RasterOperation(result);
        }
    }

    /**
     * Paints 32bpp bitmap with transparent pixels on the current screen DC.
     *
     * @param bitmap a bitmap to be painted.
     * @param x      the x-coordinate of the image.
     * @param y      the y-coordinate of the image.
     */
    private void transparentBlt32(Bitmap bitmap, int x, int y)
    {
        // allocate resources
        DC bitmapDC = DC.createCompatibleDC(this);
        Bitmap oldImageBitmap = bitmapDC.selectObject(bitmap);

        // do work
        BlendFunction blendFunction = new BlendFunction();
        blendFunction.setSourceConstantAlpha(0xFF);
        blendFunction.setAlphaFormat(BlendFunction.AC_SRC_ALPHA);

        alphaBlend(x,
                y,
                bitmap.getWidth(),
                bitmap.getHeight(),
                bitmapDC,
                0,
                0,
                bitmap.getWidth(),
                bitmap.getHeight(),
                blendFunction);

        // release resources
        bitmapDC.selectObject(oldImageBitmap);
        DC.deleteDC(bitmapDC);
    }

    /**
     * Paints a transparent bitmap on screen DC.
     *
     * @param bitmap
     * @param x
     * @param y
     */
    public void transparentBlt(Bitmap bitmap, int x, int y)
    {
        if (bitmap.getBitCount() == 32)
        {
            transparentBlt32(bitmap, x, y);
        }
        else if (bitmap.getTransparentMask() != null)
        {
            transparentBlt(bitmap, bitmap.getTransparentMask(), x, y);
        }
    }

    /**
     * Paints a bitmap on this device context.
     *
     * @param bitmap a bitmap to be painted.
     * @param x      the x-coordinate of the image.
     * @param y      the y-coordinate of the image.
     */
    public void paintBitmap(Bitmap bitmap, int x, int y)
    {
        if (bitmap.isTransparent())
        {
            transparentBlt(bitmap, x, y);
        }
        else
        {
            paintOpaqueBitmap(bitmap, x, y);
        }
    }

    /**
     * Paints an opaque bitmap on this device context.
     *
     * @param bitmap a bitmap to be painted.
     * @param x      the x-coordinate of the image.
     * @param y      the y-coordinate of the image.
     */
    public void paintOpaqueBitmap(Bitmap bitmap, int x, int y)
    {
        DC tempDC = DC.createCompatibleDC(this);
        Bitmap oldBitmap = tempDC.selectObject(bitmap);

        DC.bitBlt(this, x, y, bitmap.getWidth(), bitmap.getHeight(), tempDC, 0, 0, DC.RasterOperation.SRCCOPY);

        tempDC.selectObject(oldBitmap);
        DC.deleteDC(tempDC);
    }

    /**
     * Paints a transparent image on screen DC. Transparent pixels are pixels
     * with the color of pixel (0, 0).
     *
     * @param bitmap     a bitmap to be painted.
     * @param maskBitmap a mask of the bitmap.
     * @param x          the x-coordinate of the image.
     * @param y          the y-coordinate of the image.
     */
    private void transparentBlt(Bitmap bitmap, Bitmap maskBitmap, int x, int y)
    {
        Size bitmapSize = bitmap.getSize();

        // allocate resources
        DC tempDC = DC.createCompatibleDC(this);
        DC maskDC = DC.createCompatibleDC(this);
        DC resultDC = DC.createCompatibleDC(this);

        Bitmap resultBitmap = new DDBitmap(this, bitmapSize.getCx(), bitmapSize.getCy());
        Bitmap workMaskBitmap = new DDBitmap(this, bitmapSize.getCx(), bitmapSize.getCy());
        Bitmap oldMaskBitmap = maskDC.selectObject(workMaskBitmap);
        Bitmap oldResultBitmap = resultDC.selectObject(resultBitmap);

        // do work
        Bitmap oldBitmap = tempDC.selectObject(bitmap);
        copyImage(resultDC, bitmapSize, tempDC);

        tempDC.selectObject(maskBitmap);
        copyImage(maskDC, bitmapSize, tempDC);

        maskBackgroundForSourceImage(resultDC, maskDC, bitmapSize);
        combineBackgroundForeground(x, y, bitmapSize, maskDC, resultDC);

        // release resources
        tempDC.selectObject(oldBitmap);
        maskDC.selectObject(oldMaskBitmap);
        resultDC.selectObject(oldResultBitmap);
        resultBitmap.deleteObject();

        deleteDC(resultDC);
        deleteDC(maskDC);
        deleteDC(tempDC);
    }

    private void maskBackgroundForSourceImage(DC resultDC, DC maskDC, Size bitmapSize)
    {
        resultDC.setBkColor(new ColorRef(Color.black));
        resultDC.setTextColor(new ColorRef(Color.white));

        bitBlt(resultDC, 0, 0, bitmapSize.getCx(), bitmapSize.getCy(), maskDC, 0, 0, RasterOperation.SRCAND);
    }

    private void copyImage(DC resultDC, Size bitmapSize, DC tempDC)
    {
        bitBlt(resultDC, 0, 0, bitmapSize.getCx(), bitmapSize.getCy(), tempDC, 0, 0, RasterOperation.SRCCOPY);
    }

    private void combineBackgroundForeground(int x, int y, Size bitmapSize, DC maskDC, DC resultDC)
    {
        maskDC.patBlt(0, 0, bitmapSize.getCx(), bitmapSize.getCy(), RasterOperation.DSTINVERT);
        bitBlt(this, x, y, bitmapSize.getCx(), bitmapSize.getCy(), maskDC, 0, 0, RasterOperation.SRCAND);
        bitBlt(this, x, y, bitmapSize.getCx(), bitmapSize.getCy(), resultDC, 0, 0, RasterOperation.SRCPAINT);
    }

    /**
     * Copies an image with transparent pixels to the destination rectangle of
     * the DC.
     *
     * @param xDest         the x-coordinate of the upper-left corner for the
     *                      destination rectangle.
     * @param yDest         the y-coordinate of the upper-left corner for the
     *                      destination rectangle.
     * @param widthDest     the width of the destination rectangle.
     * @param heightDest    the height of the destination rectangle.
     * @param hdcSrc        DC that contains the image.
     * @param xSrc          the x-coordinate of the upper-left corner for the image.
     * @param ySrc          the y-coordinate of the upper-left corner for the image.
     * @param widthSrc      the width of the image.
     * @param heightSrc     the height of the image.
     * @param blendFunction blend function.
     * @return true if succeeds, false if fails.
     */
    public boolean alphaBlend(int xDest,
                              int yDest,
                              int widthDest,
                              int heightDest,
                              DC hdcSrc,
                              int xSrc,
                              int ySrc,
                              int widthSrc,
                              int heightSrc,
                              BlendFunction blendFunction)
    {
        Bool result = new Bool();

        Msimg32.getInstance().getFunction(FUNCTION_ALPHA_BLEND).invoke(result, new Parameter[]{
            this,
            new Int(xDest),
            new Int(yDest),
            new Int(widthDest),
            new Int(heightDest),
            hdcSrc,
            new Int(xSrc),
            new Int(ySrc),
            new Int(widthSrc),
            new Int(heightSrc),
            blendFunction
        });

        return result.getValue();
    }

    /**
     * Draws the grayed picture from source DC on the current DC. The function
     * applies the effect to non-white pixels of the source DC.
     * <p>Usage: paint some text, a transparent picture on a white background, and
     * then use this function for screen DC (i.e. current DC must be screen DC).
     *
     * @param srcDC    source DC.
     * @param destRect a destination rectangle.
     */
    public void drawGrayed(DC srcDC, Rect destRect)
    {
        int width = destRect.getWidth();
        int height = destRect.getHeight();
        int x = (int)destRect.getLeft();
        int y = (int)destRect.getTop();

        DC maskDC = DC.createCompatibleDC(this);
        Bitmap maskBitmap = new DDBitmap(this, width, height);
        Bitmap oldMaskBitmap = maskDC.selectObject(maskBitmap);

        DC imageDC = DC.createCompatibleDC(this);
        Bitmap imageBitmap = new DDBitmap(this, width, height);
        Bitmap oldImageBitmap = imageDC.selectObject(imageBitmap);

        // do work
        bitBlt(maskDC, 0, 0, width, height, srcDC, 0, 0, RasterOperation.SRCCOPY);

        bitBlt(this, x + 1, y + 1, width, height, maskDC, 0, 0, RasterOperation.MERGEPAINT);
        bitBlt(this, x, y, width, height, maskDC, 0, 0, RasterOperation.SRCAND);

        Rect rect = new Rect(0, 0, width, height);
        LogBrush logBrush = new LogBrush();
        logBrush.setColor(new ColorRef(new Color(0x7F, 0x7F, 0x7F)));
        Brush brush = Brush.createBrushIndirect(logBrush);
        imageDC.fillRect(rect, brush);
        brush.deleteObject();

        maskDC.patBlt(0, 0, width, height, RasterOperation.DSTINVERT);
        bitBlt(imageDC, 0, 0, width, height, maskDC, 0, 0, RasterOperation.SRCAND);

        bitBlt(this, x, y, width, height, imageDC, 0, 0, RasterOperation.SRCPAINT);

        // release resources
        imageDC.selectObject(oldImageBitmap);
        imageBitmap.deleteObject();
        DC.deleteDC(imageDC);

        maskDC.selectObject(oldMaskBitmap);
        maskBitmap.deleteObject();
        DC.deleteDC(maskDC);
    }

    /**
     * Fills a given rectangle with a specified brush.
     *
     * @param rect  a rectangle to be filled.
     * @param brush a brush to use for filling.
     */
    public void fillRect(Rect rect, Brush brush)
    {
        Region region = Region.createRectRegion(rect);
        fillRegion(region, brush);
        region.deleteObject();
    }

    /**
     * Paints a bitmap using a mask. The mask is a monochrome bitmap. The
     * function paints pixels of the foreground color by black pixels of the
     * mask. Other pixels of the current DC are not changed.
     * <p>Precondition: the current DC is screen DC.
     *
     * @param predefinedMaskBitmap a predefined bitmap for the mask.
     * @param x                    the x-coordinate of the upper corner for the destination
     *                             rectangle.
     * @param y                    the y-coordinate of the upper corner for the destination
     *                             rectangle.
     * @param fgBrush              a brush for the foreground color.
     */
    public void maskBlt(Bitmap.PredefinedBitmap predefinedMaskBitmap, int x, int y, Brush fgBrush)
    {
        // allocate resources
        DC bitmapDC = createCompatibleDC(this);
        Bitmap bitmap = new DDBitmap(predefinedMaskBitmap);
        Bitmap oldBitmap = bitmapDC.selectObject(bitmap);

        Size bitmapSize = bitmap.getSize();
        int width = bitmapSize.getCx();
        int height = bitmapSize.getCy();

        DC maskDC = createCompatibleDC(this);
        Bitmap maskBitmap = new DDBitmap(this, width, height);
        Bitmap oldMaskBitmap = maskDC.selectObject(maskBitmap);

        DC fgDC = createCompatibleDC(this);
        Bitmap fgBitmap = new DDBitmap(this, width, height);
        Bitmap oldFgBitmap = fgDC.selectObject(fgBitmap);

        // do work
        bitBlt(maskDC, 0, 0, width, height, bitmapDC, 0, 0, RasterOperation.SRCCOPY);

        bitBlt(this, x, y, width, height, maskDC, 0, 0, RasterOperation.SRCAND);
        maskDC.patBlt(0, 0, width, height, RasterOperation.DSTINVERT);

        Brush oldBrush = fgDC.selectObject(fgBrush);
        bitBlt(fgDC, 0, 0, width, height, maskDC, 0, 0, RasterOperation.MERGECOPY);
        fgDC.selectObject(oldBrush);

        bitBlt(this, x, y, width, height, fgDC, 0, 0, RasterOperation.SRCPAINT);

        // release resources
        fgDC.selectObject(oldFgBitmap);
        fgBitmap.deleteObject();
        deleteDC(fgDC);

        maskDC.selectObject(oldMaskBitmap);
        maskBitmap.deleteObject();
        deleteDC(maskDC);

        bitmapDC.selectObject(oldBitmap);
        bitmap.deleteObject();
        deleteDC(bitmapDC);
    }

    /**
     * Fills a rectangle by horisontal gradient. <b>Note:</b> This function
     * doesn't work on Windows 95 / NT.
     *
     * @param rect a rectangle for fill.
     * @return true if succeeds.
     */
    public boolean gradientFill(Rect rect, Color leftColor, Color rightColor)
    {
        Function function = Msimg32.getInstance().getFunction(FUNCTION_GRADIENT_FILL);

        TriVertex leftUpperVertex = new TriVertex();
        TriVertex rightLowerVertex = new TriVertex();

        initVertex(leftUpperVertex, leftColor, rect.getLeft(), rect.getTop());
        initVertex(rightLowerVertex, rightColor, rect.getRight(), rect.getBottom());

        GradientRect gradients = new GradientRect();
        gradients.setUpperLeft(0);
        gradients.setLowerRight(1);

        ComplexArray vertices = new ComplexArray(new TriVertex(), 2);
        vertices.setElement(0, leftUpperVertex);
        vertices.setElement(1, rightLowerVertex);

        Bool result = new Bool();
        function.invoke(result, new Parameter[]{
            this,
            new Pointer(vertices),
            new ULongInt(2),
            new Pointer(new ComplexArray(new Parameter[]{gradients})),
            new ULongInt(1),
            new ULongInt(GRADIENT_FILL_RECT_H)
        });

        return result.getValue();
    }

    private void initVertex(TriVertex vertex, Color color, long x, long y)
    {
        vertex.setX(x);
        vertex.setY(y);

        vertex.setRed(color.getRed() << 8);
        vertex.setGreen(color.getGreen() << 8);
        vertex.setBlue(color.getBlue() << 8);
        vertex.setAlpha(0);
    }

    public boolean lineTo(int x, int y)
    {
        final Function function = Gdi32.getInstance().getFunction(FUNCTION_LINE_TO);
        Bool result = new Bool();
        function.invoke(result, this, new Int(x), new Int(y));

        return result.getValue();
    }

    public boolean moveTo(int x, int y)
    {
        final Function function = Gdi32.getInstance().getFunction(FUNCTION_MOVE_TO_EX);
        Bool result = new Bool();
        function.invoke(result, this, new Int(x), new Int(y), new Handle());

        return result.getValue();
    }

    public boolean drawRectangle(Rect rect)
    {
        moveTo(rect.getLeftAsInt(), rect.getTopAsInt());
        lineTo(rect.getRightAsInt(), rect.getTopAsInt());
        lineTo(rect.getRightAsInt(), rect.getBottomAsInt());
        lineTo(rect.getLeftAsInt(), rect.getBottomAsInt());
        lineTo(rect.getLeftAsInt(), rect.getTopAsInt());

        return true;
    }

    /**
     * Enumeration of available mapping modes.
     */
    public static class MappingMode extends EnumItem
    {
        /**
         * Logical units are mapped to arbitrary units with arbitrarily scaled axes. Use the SetWindowExtEx and SetViewportExtEx functions to specify the units, orientation, and scaling.
         */
        public static MappingMode TEXT = new MappingMode(1);

        /**
         * Each logical unit is mapped to 0.001 inch. Positive x is to the right; positive y is up.
         */
        public static MappingMode LOMETRIC = new MappingMode(2);

        /**
         * Each logical unit is mapped to 0.01 millimeter. Positive x is to the right; positive y is up.
         */
        public static MappingMode HIMETRIC = new MappingMode(3);

        /**
         * Logical units are mapped to arbitrary units with equally scaled axes; that is, one unit along the x-axis is equal to one unit along the y-axis.
         */
        public static MappingMode LOENGLISH = new MappingMode(4);

        /**
         * Each logical unit is mapped to 0.01 inch. Positive x is to the right; positive y is up.
         */
        public static MappingMode HIENGLISH = new MappingMode(5);

        /**
         * Each logical unit is mapped to 0.1 millimeter. Positive x is to the right; positive y is up.
         */
        public static MappingMode TWIPS = new MappingMode(6);

        /**
         * Each logical unit is mapped to one device pixel. Positive x is to the right; positive y is down.
         */
        public static MappingMode ISOTROPIC = new MappingMode(7);

        /**
         * Each logical unit is mapped to one twentieth of a printer's point (1/1440 inch, also called a twip). Positive x is to the right; positive y is up.
         */
        public static MappingMode ANISOTROPIC = new MappingMode(8);

        private MappingMode(int value)
        {
            super(value);
        }
    }

    /**
     * Sets the mapping mode of the specified device context.
     * The mapping mode defines the unit of measure used to transform page-space units into device-space units,
     * and also defines the orientation of the device's x and y axes.
     *
     * @param mode Specifies the new mapping mode.
     * @return previous mapping mode.
     */
    public MappingMode setMapMode(MappingMode mode)
    {
        final Function function = Gdi32.get(FUNCTION_SetMapMode);
        Int result = new Int();
        function.invoke(result, this, new Int(mode.getValue()));

        final MappingMode item = (MappingMode)Enums.getItem(MappingMode.class, (int)result.getValue());
        return item;
    }

    /**
     * Returns device-specific information for the specified device.
     *
     * @param index index of capability.
     * @return The return value specifies the value of the desired item.
     */
    public int getDeviceCaps(int index)
    {
        final Function function = Gdi32.get(FUNCTION_GetDeviceCaps);
        Int result = new Int();
        function.invoke(result, this, new Int(index));
        return (int)result.getValue();
    }

    /**
     * Creates a new clipping region from the intersection of the current clipping region and the specified rectangle.
     *
     * @param left   x-coord of upper-left corner
     * @param top    y-coord of upper-left corner
     * @param right  x-coord of lower-right corner
     * @param bottom y-coord of lower-right corner
     * @return value specifies the new clipping region's type and can be one of the following values.
     */
    public int intersectClipRect(int left, int top, int right, int bottom)
    {
        final Function function = Gdi32.get(FUNCTION_IntersectClipRect);
        Int result = new Int();
        function.invoke(result, new Parameter[]
        {
            this, new Int(left), new Int(top), new Int(right), new Int(bottom)
        });
        return (int)result.getValue();
    }

    public boolean rectangle(int left, int top, int right, int bottom) {
        final Function function = Gdi32.get(FUNCTION_RECTANGLE);
        Bool result = new Bool();
        function.invoke(result, new Parameter[] {this, new Int(left), new Int(top), new Int(right), new Int(bottom)});
        return result.getValue();
    }

    public boolean roundRect(int left, int top, int right, int bottom, int arcWidth, int arcHeight) {
        final Function function = Gdi32.get(FUNCTION_ROUND_RECT);
        Bool result = new Bool();
        function.invoke(result, new Parameter[] {this, new Int(left), new Int(top), new Int(right), new Int(bottom), new Int(arcWidth), new Int(arcHeight)});
        return result.getValue();
    }

    public boolean fillRectangle(int left, int top, int right, int bottom) {
        final Function function = User32.getInstance().getFunction(FUNCTION_FILL_RECT);
        Bool result = new Bool();
        function.invoke(result, new Parameter[] {this, new Int(left), new Int(top), new Int(right), new Int(bottom)});
        return result.getValue();
    }
}

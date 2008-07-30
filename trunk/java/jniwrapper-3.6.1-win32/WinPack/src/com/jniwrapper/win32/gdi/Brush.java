/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.gdi;

import com.jniwrapper.Function;
import com.jniwrapper.Int;
import com.jniwrapper.Pointer;
import com.jniwrapper.util.EnumItem;
import com.jniwrapper.win32.Handle;
import com.jniwrapper.win32.Point;
import com.jniwrapper.win32.ui.User32;

/**
 * This class provides methods and enumerations to work with brushes.
 * 
 * @author Andrew Kharchenko
 */
public class Brush extends GdiObject
{
    private static final String FUNCTION_CREATE_BRUSH_INDIRECT = "CreateBrushIndirect";
    private static final String FUNCTION_CREATE_HATCH_BRUSH = "CreateHatchBrush";
    private static final String FUNCTION_CREATE_PATTERN_BRUSH = "CreatePatternBrush";
    private static final String FUNCTION_CREATE_SOLID_BRUSH = "CreateSolidBrush";
    private static final String FUNCTION_GET_BRUSH_ORG_EX = "GetBrushOrgEx";
    private static final String FUNCTION_GET_SYS_COLOR_BRUSH = "GetSysColorBrush";
    private static final String FUNCTION_SET_BRUSH_ORG_EX = "SetBrushOrgEx";

    /**
     * BrushStyle class represents the enumeration of brush styles.
     */
    public static class BrushStyle extends EnumItem
    {
        /**
         * Solid brush.
         */
        public static final BrushStyle SOLID = new BrushStyle(0);
        /**
         * Same as HOLLOW.
         */
        public static final BrushStyle NULL = new BrushStyle(1);
        /**
         * Hollow brush.
         */
        public static final BrushStyle HOLLOW = NULL;
        /**
         * Hatched brush.
         */
        public static final BrushStyle HATCHED = new BrushStyle(2);
        /**
         * Pattern brush defined by a memory bitmap.
         */
        public static final BrushStyle PATTERN = new BrushStyle(3);
        /**
         * A pattern brush defined by a device-independent bitmap (DIB) specification.
         */
        public static final BrushStyle DIBPATTERN = new BrushStyle(5);
        /**
         * A pattern brush defined by a device-independent bitmap (DIB) specification.
         */
        public static final BrushStyle DIBPATTERNPT = new BrushStyle(6);
        /**
         * Same as PATTERN.
         */
        public static final BrushStyle PATTERN8X8 = new BrushStyle(7);
        /**
         * Same as DIBPATTERN.
         */
        public static final BrushStyle DIBPATTERN8X8 = new BrushStyle(8);

        private BrushStyle(int value)
        {
            super(value);
        }
    }

    /**
     * HatchStyle class represents the enumeration of hatch styles.
     */
    public  static class HatchStyle extends EnumItem
    {
        /**
         * A 45-degree upward, left-to-right hatch.
         */
        public static final HatchStyle BDIAGONAL = new HatchStyle(3);
        /**
         * Horizontal and vertical cross-hatch.
         */
        public static final HatchStyle CROSS = new HatchStyle(4);
        /**
         * 45-degree crosshatch.
         */
        public static final HatchStyle DIAGCROSS = new HatchStyle(5);
        /**
         * A 45-degree downward, left-to-right hatch.
         */
        public static final HatchStyle FDIAGONAL = new HatchStyle(2);
        /**
         * Horizontal hatch.
         */
        public static final HatchStyle VERTICAL = new HatchStyle(1);
        /**
         * Vertical hatch.
         */
        public static final HatchStyle HORIZONTAL = new HatchStyle(0);

        private HatchStyle(int value)
        {
            super(value);
        }
    }

    /**
     * Enumeration of stock brushes.
     */
    public static class StockBrush extends EnumItem
    {
        public static final StockBrush WHITE_BRUSH = new StockBrush(0);
        public static final StockBrush LTGRAY_BRUSH = new StockBrush(1);
        public static final StockBrush GRAY_BRUSH = new StockBrush(2);
        public static final StockBrush DKGRAY_BRUSH = new StockBrush(3);
        public static final StockBrush BLACK_BRUSH = new StockBrush(4);
        public static final StockBrush NULL_BRUSH = new StockBrush(5);
        public static final StockBrush HOLLOW_BRUSH = NULL_BRUSH;

        private StockBrush(int value)
        {
            super(value);
        }
    }

    /**
     * Creates an empty brush.
     */
    public Brush()
    {
    }

    /**
     * Constructs a brush with a given handle.
     * 
     * @param handle a handle for already existing brush object.
     */
    public Brush(long handle)
    {
        super(handle);
    }

    /**
     * Constructs a new brush with the passed style.
     * 
     * @param style brush style.
     */
    public Brush(BrushStyle style)
    {
        super(style.getValue());
    }


    /**
     * This method retrieves one of the predefined stock brushes.
     * 
     * @param object the type of stock brush.
     * @return a requested brush, if the method succeeds, and <code>null</code>
     * if the method fails.
     */
    public static Brush getStockObject(StockBrush object)
    {
        if (object.getValue() < StockBrush.WHITE_BRUSH.getValue() ||
            object.getValue() > StockBrush.HOLLOW_BRUSH.getValue())
        {
            throw new IllegalArgumentException("Unknown stock brush kind: " + object);
        }

        final Handle handle = Gdi32.getStockObject(object.getValue());
        return new Brush(handle.getValue());
    }

    /**
     * Creates a logical brush that has the specified style, color, and pattern.
     * 
     * @param lb definition of a new brush.
     * @return a new brush if the method succeeds, and <code>null</code> if
     * otherwise.
     */
    public static Brush createBrushIndirect(LogBrush lb)
    {
        Brush result = new Brush();
        Function function = Gdi32.get(FUNCTION_CREATE_BRUSH_INDIRECT);
        function.invoke(result, new Pointer(lb));
        return result;
    }

    /**
     * Creates a logical brush that has the specified hatch pattern and color.
     * 
     * @param style hatch style of the brush.
     * @param colorRef the color of a new brush.
     * @return a new brush instance, or <code>null</code>if the method fails.
     */
    public static Brush createHatchBrush(HatchStyle style, ColorRef colorRef)
    {
        Brush result = new Brush();
        Function function = Gdi32.get(FUNCTION_CREATE_HATCH_BRUSH);
        function.invoke(result, new Int(style.getValue()), colorRef);
        return result;
    }

    /**
     * Creates a logical brush with the specified bitmap pattern.
     * 
     * @param bitmap the background for a newly created brush.
     * @return a new brush or <code>null</code>if the method fails.
     */
    public static Brush createPatternBrush(Bitmap bitmap)
    {
        Brush result = new Brush();
        Function function = Gdi32.get(FUNCTION_CREATE_PATTERN_BRUSH);
        function.invoke(result, bitmap);
        return result;
    }

    /**
     * Creates a logical brush that has the specified solid color. <br><br>
     * <b>NOTE:</b> For painting with a system color, use {@link
     * #getSysColorBrush(int)} instead, because it returns a cached system object
     * instead of allocating a new one.
     * 
     * @param colorRef the color for a brush.
     * @return a new brush or <code>null</code> class if the method fails.
     */
    public static Brush createSolidBrush(ColorRef colorRef)
    {
        Brush result = new Brush();
        Function function = Gdi32.get(FUNCTION_CREATE_SOLID_BRUSH);
        function.invoke(result, colorRef);
        return result;
    }

    /**
     * Retrieves the current brush origin for the specified device context.
     * 
     * @param hDC current device context.
     * @return the brush origin, or <code>null</code> if the method fails.
     */
    public static Point getBrushOrigin(DC hDC)
    {
        Int result = new Int();
        Point origin = new Point();
        Function function = Gdi32.get(FUNCTION_GET_BRUSH_ORG_EX);
        function.invoke(result, hDC, new Pointer(origin));
        if (result.getValue() > 0)
        {
            return new Point(origin.getX(),  origin.getY());
        }
        return null;
    }

    /**
     * Retrieves a handle of a logical brush that corresponds to the specified
     * color index.
     * 
     * @param index a color index.
     * @return a brush if the index parameter is supported by the current
     * platform. Otherwise, <code>null</code>.
     */
    public static Brush getSysColorBrush(int index)
    {
        Brush result = new Brush();
        Function function = User32.getInstance().getFunction(FUNCTION_GET_SYS_COLOR_BRUSH);
        function.invoke(result, new Int(index));
        return result;
    }

    /**
     * Sets the brush origin that GDI assigns to the next brush which an
     * application selects into the specified device context.
     * 
     * @param hDC current device context.
     * @param xOrg the x-coordinate of the new brush origin in device units. If
     * this value is greater than the brush width, the system reduces its value
     * using the modulus operator (<code>xOrg</code> <b>mod</b> brush width).
     * @param yOrg the y-coordinate of the new brush origin in device units. If
     * this value is greater than the brush height, the system reduces it by
     * using the modulus operator (<code>yOrg</code> <b>mod</b> brush height).
     * @return the previous brush origin, or <code>null</code> if the method
     * fails.
     */
    public static Point setBrushOrigin(DC hDC,
                                        int xOrg,
                                        int yOrg)
    {
        Int result = new Int();
        Point prevOrig = new Point();
        Function function = Gdi32.get(FUNCTION_SET_BRUSH_ORG_EX);
        function.invoke(result, hDC, new Int(xOrg), new Int(yOrg), new Pointer(prevOrig));
        if (result.getValue() > 0)
        {
            return new Point(prevOrig.getX(), prevOrig.getY());
        }
        return null;
    }
}

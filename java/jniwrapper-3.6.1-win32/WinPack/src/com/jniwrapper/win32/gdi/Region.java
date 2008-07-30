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
import com.jniwrapper.win32.LastErrorException;
import com.jniwrapper.win32.Point;
import com.jniwrapper.win32.Rect;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * This class provides operations on region objects.
 * 
 * @author Serge Piletsky
 */
public class Region extends GdiObject
{
    static final String FUNCTION_CREATE_ELLIPTIC_RGN = "CreateEllipticRgn";
    static final String FUNCTION_CREATE_ELLIPTIC_RGN_INDIRECT = "CreateEllipticRgnIndirect";
    static final String FUNCTION_COMBINE_RGN = "CombineRgn";
    static final String FUNCTION_CREATE_POLYGON_RGN = "CreatePolygonRgn";
    static final String FUNCTION_CREATE_POLY_POLYGON_RGN = "CreatePolyPolygonRgn";
    static final String FUNCTION_CREATE_RECT_RGN = "CreateRectRgn";
    static final String FUNCTION_CREATE_RECT_RGN_INDIRECT = "CreateRectRgnIndirect";
    static final String FUNCTION_CREATE_ROUND_RECT_RGN = "CreateRoundRectRgn";
    static final String FUNCTION_EQUAL_RGN = "EqualRgn";
    static final String FUNCTION_GET_REGION_DATA = "GetRegionData";
    static final String FUNCTION_GET_RGN_BOX = "GetRgnBox";
    static final String FUNCTION_OFFSET_RGN = "OffsetRgn";
    static final String FUNCTION_POINT_IN_REGION = "PtInRegion";
    static final String FUNCTION_RECT_IN_REGION = "RectInRegion";
    static final String FUNCTION_SET_RECT_RGN = "SetRectRgn";

    public static final int RGN_ERROR = 0;

    /**
     * The region is empty.
     */
    public static final int NULLREGION = 1;

    /**
     * The region consists of a single rectangle.
     */
    public static final int SIMPLEREGION = 2;

    /**
     * The region consists of more than one rectangle.
     */
    public static final int COMPLEXREGION = 3;

    /**
     * CombineMode class represents EnumItemeration of combined operations.
     */
    public static class CombineMode extends EnumItem
    {
        public static final CombineMode AND = new CombineMode(1);
        public static final CombineMode OR = new CombineMode(2);
        public static final CombineMode XOR = new CombineMode(3);
        public static final CombineMode DIFF = new CombineMode(4);
        public static final CombineMode COPY = new CombineMode(5);
        public static final CombineMode MIN = AND;
        public static final CombineMode MAX = COPY;

        private CombineMode(int value)
        {
            super(value);
        }
    }

    public Region()
    {
    }

    public Region(long value)
    {
        super(value);
    }

    /**
     * Factory method for creating elliptic regions.
     * 
     * @return a created region, which can hold <code>NULL</code> value in case
     * of error.
     */
    public static Region createElliptic(int left, int top, int right, int bottom)
    {
        final Function function = Gdi32.getInstance().getFunction(FUNCTION_CREATE_ELLIPTIC_RGN);
        final Region result = new Region();
        function.invoke(result, new Int(left), new Int(top), new Int(right), new Int(bottom));
        return result;
    }

    /**
     * Factory method for creating elliptic regions via bounding rectangle.
     * 
     * @param rect bounding rectangle for an elliptic region to be created.
     * @return a created region, which can hold <code>NULL</code> value in case
     * of error.
     */
    public static Region createElliptic(Rect rect)
    {
        final Region result = new Region();
        final Function function = Gdi32.getInstance().getFunction(FUNCTION_CREATE_ELLIPTIC_RGN_INDIRECT);
        function.invoke(result, new Pointer(rect));
        return null;
    }

    public static Region createRoundRectRegion(int left, int top, int right, int bottom,
                                               int ellipseWidth, int ellipseHight)
    {
        final Region result = new Region();
        final Function function = Gdi32.getInstance().getFunction(FUNCTION_CREATE_ROUND_RECT_RGN);
        function.invoke(result, new Parameter[]
        {
            new Int(left),
            new Int(top),
            new Int(right),
            new Int(bottom),
            new Int(ellipseWidth),
            new Int(ellipseHight)
        });
        return result;
    }

    /**
     * Creates a combined region from two specified regions.
     * 
     * @param srcRgn1 the first region.
     * @param srcRgn2 the second region.
     * @param combineMode combine method.
     * @return a combined region.
     */
    public static Region combineRgn(Region srcRgn1, Region srcRgn2, CombineMode combineMode)
    {
        Region result = Region.createRectRegion(0, 0, 0, 0);
        Int resultRgnType = new Int();
        final Function function = Gdi32.getInstance().getFunction(FUNCTION_COMBINE_RGN);
        long errorCode = function.invoke(resultRgnType, result, srcRgn1, srcRgn2, new Int(combineMode.getValue()));
        final long value = resultRgnType.getValue();
        if (RGN_ERROR == value)
        {
            throw new LastErrorException(errorCode, "Failed to combine regions.");
        }
        return result;
    }

    /**
     * Creates a polygonal region.
     * 
     * @param points
     * @param polyFillMode
     * @return polygon region.
     */
    public static Region createPolygonRgn(Point[] points, int nPoints, PolyFillMode polyFillMode)
    {
        PrimitiveArray pointsArray = new PrimitiveArray(Point.class, nPoints);
        for (int i = 0; i < nPoints; i++)
        {
            pointsArray.setElement(i, points[i]);
        }
        Region result = new Region();
        final Function function = Gdi32.getInstance().getFunction(FUNCTION_CREATE_POLYGON_RGN);
        function.invoke(result, new Pointer(pointsArray), new Int(nPoints), new Int(polyFillMode.getValue()));
        return result;
    }

    /**
     * Creates a region consisting of a series of polygons.
     * 
     * @param points
     * @param polyCounts
     * @param count
     * @param polyFillMode
     * @return poly polygon region.
     */
    public static Region createPolyPolygonRgn(Point[] points, int[] polyCounts, int count, PolyFillMode polyFillMode)
    {
        Region result = new Region();
        final int length = points.length;
        PrimitiveArray pointsArray = new PrimitiveArray(Point.class, length);
        for (int i = 0; i < length; i++)
        {
            pointsArray.setElement(i, points[i]);
        }
        PrimitiveArray polyCountsArray = new PrimitiveArray(Int.class, count);
        for (int i = 0; i < length; i++)
        {
            polyCountsArray.setElement(i, new Int(polyCounts[i]));
        }
        final Function function = Gdi32.getInstance().getFunction(FUNCTION_CREATE_POLY_POLYGON_RGN);
        function.invoke(result, new Pointer(pointsArray), new Pointer(polyCountsArray), new Int(count), new Int(polyFillMode.getValue()));
        return result;
    }

    /**
     * Creates a rectangular region.
     * 
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @return rectangular region.
     */
    public static Region createRectRegion(int left, int top, int right, int bottom)
    {
        Region result = new Region();
        final Function function = Gdi32.getInstance().getFunction(FUNCTION_CREATE_RECT_RGN);
        function.invoke(result, new Parameter[]
        {
            new Int(left),
            new Int(top),
            new Int(right),
            new Int(bottom),
        });
        return result;
    }

    /**
     * Creates a rectangular region.
     * 
     * @param rect
     * @return rectangular region.
     */
    public static Region createRectRegion(Rect rect)
    {
        Region result = new Region();
        final Function function = Gdi32.getInstance().getFunction(FUNCTION_CREATE_RECT_RGN_INDIRECT);
        function.invoke(result, new Pointer(rect));
        return result;
    }

    /**
     * Checks the two specified regions to determine whether they are identical.
     * 
     * @param region
     * @return true if the regions are equal, false if otherwise.
     */
    public boolean equalRegion(Region region)
    {
        Bool result = new Bool();
        final Function function = Gdi32.getInstance().getFunction(FUNCTION_CREATE_RECT_RGN);
        function.invoke(result, this, region);
        return result.getValue();
    }

    /**
     * Fills the specified buffer with data describing a region. This data
     * includes the dimensions of the rectangles that make up the region.
     * 
     * @param rgnData is a region data buffer.
     * @param count is the size of the region data buffer.
     * @return <code>count</code> if the function succeeds and count specifies
     * an adequate number of bytes. If <code>count</code> is too small or the
     * function fails, the return value is 0.
     */
    public int getRagionData(PrimitiveArray rgnData, int count)
    {
        final Function function = Gdi32.getInstance().getFunction(FUNCTION_GET_REGION_DATA);
        UInt32 returnValue = new UInt32();
        function.invoke(returnValue, this, new UInt32(count), new Pointer(rgnData));
        return (int)returnValue.getValue();
    }

    /**
     * Retrieves the bounding rectangle of the region.
     * 
     * @return the bounding rectangle of the specified region.
     */
    public Rect getRegionBox()
    {
        Rect result = new Rect();
        final Function function = Gdi32.getInstance().getFunction(FUNCTION_GET_RGN_BOX);
        Int returnResult = new Int();
        function.invoke(returnResult, this, new Pointer(result));
        return result;
    }

    /**
     * Moves a region by the specified offsets.
     * 
     * @param xOffset
     * @param yOffset
     */
    public void offsetRegion(int xOffset, int yOffset)
    {
        final Function function = Gdi32.getInstance().getFunction(FUNCTION_OFFSET_RGN);
        function.invoke(null, this, new Int(xOffset), new Int(yOffset));
    }

    /**
     * Determines whether the specified point is inside the specified region.
     * 
     * @param x
     * @param y
     * @return true if the point is inside the specified region; false if
     * otherwise.
     */
    public boolean isPointInRegion(int x, int y)
    {
        Bool result = new Bool();
        final Function function = Gdi32.getInstance().getFunction(FUNCTION_POINT_IN_REGION);
        function.invoke(result, this, new Int(x), new Int(y));
        return result.getValue();
    }

    /**
     * Determines whether the specified point is inside the specified region.
     * 
     * @param point
     * @return true if the point is inside the specified region; false if
     * otherwise.
     */
    public boolean isPointInRegion(Point point)
    {
        return isPointInRegion((int)point.getY(), (int)point.getY());
    }

    /**
     * Determines whether any part of the specified rectangle is within the
     * boundaries of a region.
     * 
     * @param rect
     * @return true if any part of the specified rectangle is within the
     * boundaries of a region.; false if otherwise.
     */
    public boolean isRectInRegion(Rect rect)
    {
        Bool result = new Bool();
        final Function function = Gdi32.getInstance().getFunction(FUNCTION_RECT_IN_REGION);
        function.invoke(result, this, new Pointer(rect));
        return result.getValue();
    }

    /**
     * Converts a region into a rectangular region with the specified
     * coordinates.
     * 
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setRectRegion(int left, int top, int right, int bottom)
    {
        final Function function = Gdi32.getInstance().getFunction(FUNCTION_SET_RECT_RGN);
        function.invoke(null, new Parameter[]
        {
            this,
            new Int(left),
            new Int(top),
            new Int(right),
            new Int(bottom)
        });
    }

    /**
     * Creates a region from the specified image.
     * 
     * @param image is a source image.
     * @param transparent is a transparent color of the image.
     * @return a region created from the image.
     */
    public static Region createFromImage(Image image, Color transparent)
    {
        return createFromImage(image, transparent, null);
    }

    /**
     * Creates a region from specified image.
     * 
     * @param image is a source image.
     * @param transparent is a transparent color of the image.
     * @param tolerance is a color tolerance for the transparent color.
     * @return a region created from the image.
     */
    public static Region createFromImage(Image image, Color transparent, Color tolerance)
    {
        if (image == null || transparent == null)
            throw new IllegalArgumentException();
        if (tolerance == null)
            tolerance = new Color(0x101010);

        final int width = image.getWidth(null);
        final int height = image.getHeight(null);

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.drawImage(image, 0, 0, transparent, null);
        graphics2D.dispose();

        Region result = Region.createRectRegion(new Rect());
        int transparentRedValue = transparent.getRed();
        int transparentGreenValue = transparent.getGreen();
        int transparentBlueValue = transparent.getBlue();
        int toleranceRedValue = Math.min(transparentRedValue + tolerance.getRed(), 255);
        int toleranceGreenValue = Math.min(transparentGreenValue + tolerance.getGreen(), 255);
        int toleranceBlueValue = Math.min(transparentBlueValue + tolerance.getBlue(), 255);

        for (int x = 0; x < width; x++)
        {
            boolean continuous = false;
            int yBegin = 0;
            for (int y = 0; y < height; y++)
            {
                final int rgb = bufferedImage.getRGB(x, y);
                final int redValue = (rgb & 0xFF0000) >> 16;
                boolean isTransparent = false;
                if (redValue >= transparentRedValue && redValue <= toleranceRedValue)
                {
                    final int greenValue = (rgb & 0xFF00) >> 8;
                    if (greenValue >= transparentGreenValue && greenValue <= toleranceGreenValue)
                    {
                        final int blueVale = rgb & 0xFF;
                        isTransparent = blueVale >= transparentBlueValue && blueVale <= toleranceBlueValue;
                    }
                }
                if (isTransparent)
                {
                    if (continuous)
                    {
                        continuous = false;
                        final Region rectRegion = createRectRegion(x, yBegin, x + 1, y - 1);
                        Region newResult = combineRgn(result, rectRegion, CombineMode.OR);
                        result.deleteObject();
                        result = newResult;
                        rectRegion.deleteObject();
                    }
                }
                else
                {
                    if (!continuous)
                    {
                        continuous = true;
                        yBegin = y;
                    }
                }
            }
            if (continuous)
            {
                final Region rectRegion = createRectRegion(x, yBegin, x + 1, height);
                Region newResult = combineRgn(result, rectRegion, CombineMode.OR);
                result.deleteObject();
                result = newResult;
                rectRegion.deleteObject();
            }
        }
        return result;
    }

    /**
     * Creates a region from the specified image. The image should be
     * transparent so there is no need to specify transparency color.
     * 
     * @param image is a source image.
     * @return a region created from the image.
     */
    public static Region createFromImage(Image image)
    {
        if (image == null)
            throw new IllegalArgumentException();

        final int width = image.getWidth(null);
        final int height = image.getHeight(null);

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.drawImage(image, 0, 0, null);
        graphics2D.dispose();

        Region result = Region.createRectRegion(new Rect());

        for (int x = 0; x < width; x++)
        {
            boolean continuous = false;
            int yBegin = 0;
            for (int y = 0; y < height; y++)
            {
                final int argb = bufferedImage.getRGB(x, y);
                boolean isTransparent = ((argb & 0xFF000000) >> 24) == 0;
                if (isTransparent)
                {
                    if (continuous)
                    {
                        continuous = false;
                        final Region rectRegion = createRectRegion(x, yBegin, x + 1, y - 1);
                        Region newResult = combineRgn(result, rectRegion, CombineMode.OR);
                        result.deleteObject();
                        result = newResult;
                        rectRegion.deleteObject();
                    }
                }
                else
                {
                    if (!continuous)
                    {
                        continuous = true;
                        yBegin = y;
                    }
                }
            }
            if (continuous)
            {
                final Region rectRegion = createRectRegion(x, yBegin, x + 1, height);
                Region newResult = combineRgn(result, rectRegion, CombineMode.OR);
                result.deleteObject();
                result = newResult;
                rectRegion.deleteObject();
            }
        }
        return result;
    }
}

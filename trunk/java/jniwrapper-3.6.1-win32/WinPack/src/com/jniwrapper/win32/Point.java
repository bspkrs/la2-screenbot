/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32;

import com.jniwrapper.Parameter;
import com.jniwrapper.Structure;
import com.jniwrapper.LongInt;

/**
 * This class represents <code>POINT</code> structure.
 */
public class Point extends Structure
{
    private LongInt _x = new LongInt();
    private LongInt _y = new LongInt();

    /**
     * Constructs a point with zero x- and y-coordinates.
     */
    public Point()
    {
        init(new Parameter[]{_x, _y});
    }

    /**
     * Constructs a new point with the given x and y values.
     */
    public Point(long x, long y)
    {
        this();
        setX(x);
        setY(y);
    }

    /**
     * Constructs a new point with the same coordinates as in the passed one.
     * 
     * @param that a point to copy the coordinates from.
     */
    public Point(Point that)
    {
        this();
        setX(that.getX());
        setY(that.getY());
    }

    public long getX()
    {
        return _x.getValue();
    }

    public void setX(long x)
    {
        _x.setValue(x);
    }

    public long getY()
    {
        return _y.getValue();
    }

    public void setY(long y)
    {
        _y.setValue(y);
    }

    public Object clone()
    {
        return new Point(this);
    }

    /**
     * Compares two points. Two points are equal if corresponding x- and
     * y-coordinates are equal.
     * 
     * @param o another instance of Point.
     * @return true, if points represent the same coordinates on the screen;
     * false if otherwise.
     */
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Point)) return false;
        if (!super.equals(o)) return false;

        final Point point = (Point)o;

        if (!_x.equals(point._x)) return false;
        if (!_y.equals(point._y)) return false;

        return true;
    }

    public int hashCode()
    {
        int result;
        result = _x.hashCode();
        result = 29 * result + _y.hashCode();
        return result;
    }
}

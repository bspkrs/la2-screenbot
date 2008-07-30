/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32;

import com.jniwrapper.LongInt;
import com.jniwrapper.Parameter;
import com.jniwrapper.Structure;

import java.awt.*;

/**
 * This class represents <code>RECT</code> structure.
 */
public class Rect extends Structure
{
    private LongInt _left = new LongInt();
    private LongInt _top = new LongInt();
    private LongInt _right = new LongInt();
    private LongInt _bottom = new LongInt();

    public Rect()
    {
        init(new Parameter[]{_left, _top, _right, _bottom});
    }

    public Rect(Rect that)
    {
        this();
        initFrom(that);
    }

    public Rect(long left, long top, long right, long bottom)
    {
        this();
        setLeft(left);
        setTop(top);
        setRight(right);
        setBottom(bottom);
    }

    public Rect(Rectangle rectangle)
    {
        this(rectangle.x, rectangle.y, rectangle.x + rectangle.width, rectangle.y + rectangle.height);
    }

    public void setLeft(long value)
    {
        _left.setValue(value);
    }

    public long getLeft()
    {
        return _left.getValue();
    }

    public void setTop(long value)
    {
        _top.setValue(value);
    }

    public long getTop()
    {
        return _top.getValue();
    }

    public void setRight(long value)
    {
        _right.setValue(value);
    }

    public long getRight()
    {
        return _right.getValue();
    }

    public void setBottom(long value)
    {
        _bottom.setValue(value);
    }

    public long getBottom()
    {
        return _bottom.getValue();
    }

    public void moveTo(long left, long top)
    {
        long width = getWidth();
        long height = getHeight();
        setLeft(left);
        setTop(top);
        setRight(left + width);
        setBottom(top + height);
    }

    public void moveBy(int deltaX, int deltaY)
    {
        setLeft(getTop() + deltaX);
        setRight(getBottom() + deltaX);
        setTop(getTop() + deltaY);
        setBottom(getBottom() + deltaY);
    }

    public String toString()
    {
        StringBuffer result = new StringBuffer();
        result.append("Rect: [").
                append(getLeft()).append(',').append(getTop()).append(',').append(getRight()).
                append(',').append(getBottom()).append(']');
        return result.toString();
    }

    public Object clone()
    {
        return new Rect(this);
    }

    public void assign(Rect anotherRect)
    {
        setTop(anotherRect.getTop());
        setBottom(anotherRect.getBottom());
        setLeft(anotherRect.getLeft());
        setRight(anotherRect.getRight());
    }

    public void setBounds(int left, int top, int right, int bottom)
    {
        setTop(top);
        setBottom(bottom);
        setLeft(left);
        setRight(right);
    }

    public void setBounds(Rectangle rectangle)
    {
        setTop(rectangle.y);
        setBottom(rectangle.x + rectangle.width);
        setLeft(rectangle.x);
        setRight(rectangle.y + rectangle.height);
    }

    public int getWidth()
    {
        return (int) (_right.getValue() - _left.getValue());
    }

    public int getHeight()
    {
        return (int) (_bottom.getValue() - _top.getValue());
    }

	// TODO: Why do we need these getXxxAsInt()?
    public int getLeftAsInt()
    {
        return (int) getLeft();
    }

    public int getTopAsInt()
    {
        return (int) getTop();
    }

    public int getBottomAsInt()
    {
        return (int) getBottom();
    }

    public int getRightAsInt()
    {
        return (int) getRight();
    }

	// TODO: Document
    public Rect centerRect(Size innerRect)
    {
        int centeredX = (int) getLeft() + (getWidth() - innerRect.getCx()) / 2;
        int centeredY = (int) getTop() + (getHeight() - innerRect.getCy()) / 2;

        return new Rect(centeredX, centeredY, centeredX + innerRect.getCx(), centeredY + innerRect.getCy());
    }
}

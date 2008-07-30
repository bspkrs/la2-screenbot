/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.gdi;

import com.jniwrapper.Parameter;
import com.jniwrapper.Structure;
import com.jniwrapper.ULongInt;

/**
 * Represents GRADIENT_RECT native structure.
 */
public class GradientRect extends Structure
{
    private ULongInt _upperLeft = new ULongInt();
    private ULongInt _lowerRight = new ULongInt();

    public GradientRect()
    {
        init(new Parameter[] {_upperLeft, _lowerRight});
    }

    public GradientRect(GradientRect that)
    {
        this();
        initFrom(that);
    }

    public long getUpperLeft()
    {
        return _upperLeft.getValue();
    }

    public void setUpperLeft(long upperLeft)
    {
        _upperLeft.setValue(upperLeft);
    }

    public long getLowerRight()
    {
        return _lowerRight.getValue();
    }

    public void setLowerRight(long lowerRight)
    {
        _lowerRight.setValue(lowerRight);
    }

    public Object clone()
    {
        return new GradientRect(this);
    }
}

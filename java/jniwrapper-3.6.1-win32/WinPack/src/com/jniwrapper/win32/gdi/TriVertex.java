/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.gdi;

import com.jniwrapper.Structure;
import com.jniwrapper.LongInt;
import com.jniwrapper.UShortInt;
import com.jniwrapper.Parameter;

public class TriVertex extends Structure
{
    private LongInt _x = new LongInt();
    private LongInt _y = new LongInt();

    private UShortInt _red = new UShortInt();
    private UShortInt _green = new UShortInt();
    private UShortInt _blue = new UShortInt();
    private UShortInt _alpha = new UShortInt();

    public TriVertex()
    {
        init(new Parameter[] {
            _x, _y, _red, _green, _blue, _alpha
        });
    }

    public TriVertex(TriVertex that)
    {
        this();
        initFrom(that);
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

    public int getRed()
    {
        return (int)_red.getValue();
    }

    public void setRed(int red)
    {
        _red.setValue(red);
    }

    public int getGreen()
    {
        return (int)_green.getValue();
    }

    public void setGreen(int green)
    {
        _green.setValue(green);
    }

    public int getBlue()
    {
        return (int)_blue.getValue();
    }

    public void setBlue(int blue)
    {
        _blue.setValue(blue);
    }

    public int getAlpha()
    {
        return (int)_alpha.getValue();
    }

    public void setAlpha(int alpha)
    {
        _alpha.setValue(alpha);
    }

    public Object clone()
    {
        return new TriVertex(this);
    }
}

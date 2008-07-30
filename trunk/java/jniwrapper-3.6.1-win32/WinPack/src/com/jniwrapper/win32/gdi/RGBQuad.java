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
import com.jniwrapper.UInt8;

/**
 * This class represents RGBQUAD structure.
 *
 * @author Serge Piletsky
 */
public class RGBQuad extends Structure
{
    private UInt8 _blue = new UInt8();
    private UInt8 _green = new UInt8();
    private UInt8 _red = new UInt8();
    private UInt8 _reserved = new UInt8();

    public RGBQuad(int r, int g, int b)
    {
        this();

        setRed(r);
        setGreen(g);
        setBlue(b);
    }

    public RGBQuad()
    {
        init();
    }

    private void init()
    {
        init(new Parameter[]{_blue, _green, _red, _reserved});
        _reserved.setValue(0);
    }

    public RGBQuad(RGBQuad that)
    {
        init();

        setRed(that.getRed());
        setGreen(that.getGreen());
        setBlue(that.getBlue());
    }



    public long getBlue()
    {
        return _blue.getValue();
    }

    public void setBlue(long value)
    {
        _blue.setValue(value);
    }

    public long getGreen()
    {
        return _green.getValue();
    }

    public void setGreen(long value)
    {
        _green.setValue(value);
    }

    public long getRed()
    {
        return _red.getValue();
    }

    public void setRed(long value)
    {
        _red.setValue(value);
    }

    public int getRGB()
    {
        int result = (int)((getRed() << 16) | (getGreen() << 8) | getBlue());
        return result;
    }

    public boolean equals(Object obj)
    {
        if (obj instanceof RGBQuad)
        {
            RGBQuad quad = (RGBQuad) obj;
            return (getRed() == quad.getRed()) && (getGreen() == quad.getGreen()) && (getBlue() == quad.getBlue());
        }
        return false;
    }

    public int hashCode()
    {
        int result;
        result = _blue.hashCode();
        result = 29 * result + _green.hashCode();
        result = 29 * result + _red.hashCode();
        return result;
    }

    public String toString()
    {
        StringBuffer result = new StringBuffer();
        result.append("RGBQuad (");
        result.append(getRed());
        result.append(", ");
        result.append(getGreen());
        result.append(", ");
        result.append(getBlue());
        result.append(")");

        return result.toString();
    }

    public Object clone()
    {
        return new RGBQuad(this);
    }
}

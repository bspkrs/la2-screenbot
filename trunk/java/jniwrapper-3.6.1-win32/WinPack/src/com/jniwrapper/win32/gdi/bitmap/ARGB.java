/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.gdi.bitmap;

/**
 * Represents Java ARGB color used in BufferedImage.getRGB().
 */
public class ARGB
{
    private int _argb = 0;

    public ARGB(int argb)
    {
        _argb = argb;
    }

    public ARGB(int alpha, int red, int green, int blue)
    {
        _argb = 0;
        setAlpha(alpha);
        setRed(red);
        setGreen(green);
        setBlue(blue);
    }

    private void setColorComponent(int index, int val)
    {
        if (index >= 0 && index <= 3)
        {
            int offset = 8 * index;
//            int mask = 0xFF << offset;
//            int negateMask = mask ^ 0xFFFFFF;
//            _argb &= negateMask;
            _argb |= (val << offset);
        }
        else
        {
            throw new RuntimeException("Invalid index for component");
        }
    }

    private int getColorComponent(int index)
    {
        if (index >= 0 && index <= 3)
        {
            int offset = 8 * index;
            int mask = 0xFF << offset;
            int result = (_argb & mask) >> offset;
            return result;
        }
        else
        {
            throw new RuntimeException("Invalid index for component");
        }
    }

    public int getAlpha()
    {
        return getColorComponent(3);
    }

    public int getRed()
    {
        return getColorComponent(2);
    }

    public int getGreen()
    {
        return getColorComponent(1);
    }

    public int getBlue()
    {
        return getColorComponent(0);
    }

    public void setAlpha(int val)
    {
        setColorComponent(3, val);
    }

    public void setRed(int val)
    {
        setColorComponent(2, val);
    }

    public void setGreen(int val)
    {
        setColorComponent(1, val);
    }

    public void setBlue(int val)
    {
        setColorComponent(0, val);
    }

    public int getValue()
    {
        return _argb;
    }
}

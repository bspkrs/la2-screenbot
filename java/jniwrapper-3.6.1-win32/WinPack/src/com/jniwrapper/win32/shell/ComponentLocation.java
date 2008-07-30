/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.shell;

import com.jniwrapper.*;

/**
 * Represents <code>COMPPOS</code> native structure.
 */
public class ComponentLocation extends Structure
{
    private UInt32 dwSize = new UInt32();
    private Int iLeft = new Int();
    private Int iTop = new Int();
    private UInt32 dwWidth = new UInt32();
    private UInt32 dwHeight = new UInt32();
    private Int izIndex = new Int();
    private IntBool fCanResize = new IntBool();
    private IntBool fCanResizeX = new IntBool();
    private IntBool fCanResizeY = new IntBool();
    private Int iPreferredLeftPercent = new Int();
    private Int iPreferredTopPercent = new Int();

    public ComponentLocation()
    {
        init(new Parameter[]{dwSize, iLeft, iTop, dwWidth, dwHeight, izIndex, fCanResize, fCanResizeX, fCanResizeY, iPreferredLeftPercent, iPreferredTopPercent});
        dwSize.setValue(getLength());
    }

    public ComponentLocation(ComponentLocation that)
    {
        this();
        initFrom(that);
    }

    void copyFrom(ComponentLocation that)
    {
        dwSize.setValue(that.dwSize.getValue());
        iLeft.setValue(that.iLeft.getValue());
        iTop.setValue(that.iTop.getValue());
        dwWidth.setValue(that.dwWidth.getValue());
        dwHeight.setValue(that.dwHeight.getValue());
        izIndex.setValue(that.izIndex.getValue());
        fCanResize.setValue(that.fCanResize.getValue());
        fCanResizeX.setValue(that.fCanResizeX.getValue());
        fCanResizeY.setValue(that.fCanResizeY.getValue());
        iPreferredLeftPercent.setValue(that.iPreferredLeftPercent.getValue());
        iPreferredTopPercent.setValue(that.iPreferredTopPercent.getValue());
    }

    public int getLeft()
    {
        return (int)iLeft.getValue();
    }

    public void setiLeft(int left)
    {
        iLeft.setValue(left);
    }

    public int getTop()
    {
        return (int)iTop.getValue();
    }

    public void setTop(int top)
    {
        iTop.setValue(top);
    }

    public int getWidth()
    {
        return (int)dwWidth.getValue();
    }

    public void setWidth(int width)
    {
        dwWidth.setValue(width);
    }

    public int getHeight()
    {
        return (int)dwHeight.getValue();
    }

    public void setHeight(int height)
    {
        dwHeight.setValue(height);
    }

    public int getIndex()
    {
        return (int)izIndex.getValue();
    }

    public void setIndex(int index)
    {
        izIndex.setValue(index);
    }

    public boolean getCanResize()
    {
        return fCanResize.getBooleanValue();
    }

    public void setCanResize(boolean canResize)
    {
        fCanResize.setBooleanValue(canResize);
    }

    public boolean getCanResizeX()
    {
        return fCanResizeX.getBooleanValue();
    }

    public void setCanResizeX(boolean canResizeX)
    {
        fCanResizeX.setBooleanValue(canResizeX);
    }

    public boolean getCanResizeY()
    {
        return fCanResizeY.getBooleanValue();
    }

    public void setCanResizeY(boolean canResizeY)
    {
        fCanResizeY.setBooleanValue(canResizeY);
    }

    public int getPreferredLeftPercent()
    {
        return (int)iPreferredLeftPercent.getValue();
    }

    public void setPreferredLeftPercent(int preferredLeftPercent)
    {
        iPreferredLeftPercent.setValue(preferredLeftPercent);
    }

    public int getPreferredTopPercent()
    {
        return (int)iPreferredTopPercent.getValue();
    }

    public void setPreferredTopPercent(int preferredTopPercent)
    {
        iPreferredTopPercent.setValue(preferredTopPercent);
    }

    public Object clone()
    {
        return new ComponentLocation(this);
    }
}

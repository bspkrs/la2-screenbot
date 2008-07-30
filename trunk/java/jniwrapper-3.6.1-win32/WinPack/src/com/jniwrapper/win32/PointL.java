/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32;

import com.jniwrapper.Int32;
import com.jniwrapper.Parameter;
import com.jniwrapper.Structure;

/**
 * This structure contains coordinates of the point and corresponds to
 * <code>POINTL</code> native structure.
 * 
 * @see <a href="http://msdn.microsoft.com/library/en-us/gdi/metafile_4quq.asp">Microsoft
 * COM SDK documentation</a>
 */
public class PointL extends Structure
{
    private Int32 _x;
    private Int32 _y;

    public PointL()
    {
        _x = new Int32();
        _y = new Int32();

        init();
    }

    public PointL(PointL that)
    {
        _x = (Int32)that._x.clone();
        _y = (Int32)that._y.clone();

        init();
    }

    private void init()
    {
        init(
                new Parameter[]{
                    _x,
                    _y
                }
        );
    }

    public int getX()
    {
        return (int)_x.getValue();
    }

    public void setX(int value)
    {
        _x.setValue(value);
    }

    public int getY()
    {
        return (int)_y.getValue();
    }

    public void setY(int value)
    {
        _y.setValue(value);
    }

    public Object clone()
    {
        return new PointL(this);
    }
}

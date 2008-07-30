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
import com.jniwrapper.win32.IntPtr;

/**
 * This class represents the 
 * @see <a href="http://msdn.microsoft.com/library/default.asp?url=/library/en-us/gdi/brushes_2u0i.asp">
 *      LOGBRUSH structure</a>
 *
 * @author Andrew Kharchenko
 */
public class LogBrush extends Structure
{
    /**
     * Specifies brush style.
     */
    protected UInt _style = new UInt();
    /**
     * Specifies brush color.
     */
    protected ColorRef _color = new ColorRef();
    /**
     * Specifies hatch style.
     */
    protected IntPtr _hatch = new IntPtr();

    public LogBrush()
    {
        init(new Parameter[]{_style, _color, _hatch});
    }

    public LogBrush(LogBrush that)
    {
        this();
        initFrom(that);
    }

    public void setStyle(UInt style)
    {
        setStyle(style.getValue());
    }

    public void setStyle(long style)
    {
        _style.setValue(style);
    }

    public void setColor(ColorRef color)
    {
        if (_style.getValue() == Brush.BrushStyle.HATCHED.getValue() ||
                _style.getValue() == Brush.BrushStyle.SOLID.getValue())
        {
            _color.setValue(color.getValue());
        }
        else
        {
            throw new IllegalArgumentException("");
        }
    }

    public void setHatch(Int32 hatch)
    {
        setHatch(hatch.getValue());
    }

    public void setHatch(long hatch)
    {
        _hatch.setValue(hatch);
    }

    public Object clone()
    {
        return new LogBrush(this);
    }
}

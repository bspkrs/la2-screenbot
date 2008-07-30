/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.gdi;

import com.jniwrapper.ComplexArray;
import com.jniwrapper.Parameter;
import com.jniwrapper.Structure;
import com.jniwrapper.UInt16;

/**
 * This class represents ICONDIR structure.
 * 
 * @author Serge Piletsky
 */
class IconDir extends Structure
{
    private UInt16 _reserved = new UInt16();
    private UInt16 _type = new UInt16();
    private UInt16 _count = new UInt16();
    private ComplexArray _entries = new ComplexArray(new IconDirEntry(), 0);

    public IconDir()
    {
        init(new Parameter[]{_reserved, _type, _count, _entries});
    }

    public IconDir(IconDir that)
    {
        this();
        initFrom(that);
    }

    public int getType()
    {
        return (int)_type.getValue();
    }

    public int getCount()
    {
        return (int)_count.getValue();
    }

    public ComplexArray getEntries()
    {
        return _entries;
    }

    public Object clone()
    {
        return new IconDir(this);
    }
}

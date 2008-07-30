/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.security;

import com.jniwrapper.LongInt;
import com.jniwrapper.Parameter;
import com.jniwrapper.Structure;
import com.jniwrapper.UInt32;

/**
 * The opaque structure that represents native <code>LUID</code> structure.
 */
public class Luid extends Structure
{
    private UInt32 _lowPart = new UInt32();
    private LongInt _highPart = new LongInt();

    public Luid()
    {
        init(new Parameter[]{_lowPart, _highPart});
    }

    private Luid(Luid other)
    {
        this();
        _lowPart.setValue(other._lowPart.getValue());
        _highPart.setValue(other._highPart.getValue());
    }

    public Object clone()
    {
        return new Luid(this);
    }
}

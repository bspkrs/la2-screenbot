/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.security;

import com.jniwrapper.Parameter;
import com.jniwrapper.Structure;
import com.jniwrapper.UInt32;

/**
 * This structure represents a locally unique identifier and its attributes and
 * corresponds to the native <code>LUID_AND_ATTRIBUTES</code> structure.
 */
public class LuidAndAttributes extends Structure
{
    private Luid _luid = new Luid();
    private UInt32 _attributes = new UInt32();

    public LuidAndAttributes()
    {
        init(new Parameter[]{_luid, _attributes});
    }

    private LuidAndAttributes(LuidAndAttributes other)
    {
        _luid = (Luid) other._luid.clone();
        _attributes = (UInt32) other._attributes.clone();

        init(new Parameter[]{_luid, _attributes});
    }

    public Luid getLuid()
    {
        return _luid;
    }

    public int getAttributes()
    {
        return (int) _attributes.getValue();
    }

    public void setAttributes(int attributes)
    {
        _attributes.setValue(attributes);
    }

    public Object clone()
    {
        return new LuidAndAttributes(this);
    }
}

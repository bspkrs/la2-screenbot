/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32;

/**
 * This class represents <code>SIZE_T</code> native type. This is guaranteed
 * to be the same size as a pointer.  Its size with change with pointer size (32/64).
 *
 * @author Vadim Steshenko
 */
public class SizeT extends IntPtr
{
    /**
     * Constucts a blank instance.
     */
    public SizeT()
    {
        super();
    }

    /**
     * Constructs with the passed value.
     *
     * @param value value.
     */
    public SizeT(long value)
    {
        super(value);
    }

    public Object clone()
    {
        return new SizeT(this.getValue());
    }
}

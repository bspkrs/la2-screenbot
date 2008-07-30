/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32;

import com.jniwrapper.Pointer;

/**
 * This class represents <code>LONG_PTR</code> native type. This is guaranteed
 * to be the same size as a pointer.  Its size with change with pointer size (32/64).
 * It should be used anywhere that a pointer is cast to an Int32 type.
 *
 * @author Vadim Steshenko
 */
public class IntPtr extends Pointer.Void
{
    /**
     * Constucts a blank instance.
     */
    public IntPtr()
    {
        super();
    }

    /**
     * Constructs with the passed value.
     *
     * @param value value.
     */
    public IntPtr(long value)
    {
        super(value);
    }

    public Object clone()
    {
        return new IntPtr(this.getValue());
    }
}

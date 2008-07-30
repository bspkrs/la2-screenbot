/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.system;

import com.jniwrapper.util.EnumItem;

/**
 * This is an enumeration class defining memory allocation attribues.
 */
public class MemoryAllocationAttributes extends EnumItem
{
    /**
     * Allocates fixed memory.
     */
    public static final MemoryAllocationAttributes FIXED = new MemoryAllocationAttributes(0x0000);

    /**
     * Allocates movable memory.
     */
    public static final MemoryAllocationAttributes MOVEABLE = new MemoryAllocationAttributes(0x0002);

    /**
     * Sets all allocated bytes to zero.
     */
    public static final MemoryAllocationAttributes ZEROINIT = new MemoryAllocationAttributes(0x0040);

    /**
     * Combines FIXED, ZEROINIT attrbiutes.
     */
    public static final MemoryAllocationAttributes LPTR = new MemoryAllocationAttributes(FIXED.getValue() | ZEROINIT.getValue());

    /**
     * Combines MOVEABLE, ZEROINIT attrbiutes.
     */
    public static final MemoryAllocationAttributes LHND = new MemoryAllocationAttributes(MOVEABLE.getValue() | ZEROINIT.getValue());

    private MemoryAllocationAttributes(int value)
    {
        super(value);
    }
}

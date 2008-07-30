/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.process.monitoring;

import com.jniwrapper.Parameter;
import com.jniwrapper.UInt32;
import com.jniwrapper.win32.SizeT;
import com.jniwrapper.win32.IntPtr;

/**
 * The HeapList class represents the HEAPLIST32 structure.
 * 
 * @author Serge Piletsky
 */
public class HeapList extends PerformanceEntry
{
    private SizeT _size = new SizeT();
    private UInt32 _processID = new UInt32();
    private IntPtr _heapID = new IntPtr();
    private UInt32 _flags = new UInt32();

    public HeapList()
    {
        super();
        init(new Parameter[]{_size, _processID, _heapID, _flags}, (short)8);
        _size.setValue(getLength());
    }

    /**
     * Identifier of the process to be examined.
     * 
     * @return Identifier of the process to be examined.
     */
    public long getProcessID()
    {
        return _processID.getValue();
    }

    /**
     * Heap identifier in the owning process context.
     * 
     * @return Heap identifier in the owning process context.
     */
    public long getHeapID()
    {
        return _heapID.getValue();
    }

    /**
     * Process's default heap.
     */
    public final static int HF32_DEFAULT = 1;

    /**
     * Shared heap.
     */
    public final static int HF32_SHARED = 2;

    /**
     * Flags. These values are defined as <code>HF32_DEFAULT</code>,
     * <code>HF32_SHARED</code>
     * 
     * @return Flags.
     */
    public long getFlags()
    {
        return _flags.getValue();
    }
}

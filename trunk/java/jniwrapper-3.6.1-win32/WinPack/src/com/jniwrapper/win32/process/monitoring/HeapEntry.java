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
import com.jniwrapper.win32.Handle;
import com.jniwrapper.win32.SizeT;
import com.jniwrapper.win32.IntPtr;

/**
 * The HeapEntry class represents the HEAPENTRY32 structure.
 * 
 * @author Serge Piletsky
 */
public class HeapEntry extends PerformanceEntry
{
    private SizeT _size = new SizeT();
    private Handle _handle = new Handle();
    private IntPtr _address = new IntPtr();
    private SizeT _blockSize = new SizeT();
    private UInt32 _flags = new UInt32();
    private UInt32 _lockCount = new UInt32();
    private UInt32 _resvd = new UInt32();
    private UInt32 _processID = new UInt32();
    private IntPtr _heapID = new IntPtr();

    public HeapEntry()
    {
        super();
        init(new Parameter[]{
                _size,
                _handle,
                _address,
                _blockSize,
                _flags,
                _lockCount,
                _resvd,
                _processID,
                _heapID
        }, (short)8);
        _size.setValue(getLength());
    }

    /**
     * Handle to the heap block.
     * 
     * @return Handle to the heap block.
     */
    public Handle getHandle()
    {
        return _handle;
    }

    /**
     * Linear address of the start of the block.
     * 
     * @return Linear address of the start of the block.
     */
    public long getAddress()
    {
        return _address.getValue();
    }

    /**
     * Size of the heap block, in bytes.
     * 
     * @return Size of the heap block, in bytes.
     */
    public long getBlockSize()
    {
        return _blockSize.getValue();
    }

    /**
     * The memory block has a fixed (unmovable) location.
     */
    public final static int LF32_FIXED = 0x00000001;
    /**
     * The memory block is not used.
     */
    public final static int LF32_FREE = 0x00000002;
    /**
     * The memory block location can be moved.
     */
    public final static int LF32_MOVEABLE = 0x00000004;

    /**
     * Flags. These values are defined as <code>LF32_FIXED</code>,
     * <code>LF32_FREE</code>, <code>LF32_MOVEABLE</code>
     * 
     * @return Flags.
     */
    public long getFlags()
    {
        return _flags.getValue();
    }

    /**
     * Lock count on the memory block.
     * 
     * @return Lock count on the memory block.
     */
    public long getLockCount()
    {
        return _lockCount.getValue();
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

    public String getDebugInfo()
    {
        return "HeapEntry [handle=" + _handle +
                "; address=" + _address +
                "; blockSize=" + _blockSize +
                "; flags=" + _flags +
                "; lockCount=" + _lockCount +
                "; processID=" + _processID +
                "; heapID=" + _heapID + "];";

    }
}

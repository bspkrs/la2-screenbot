/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.system;

import com.jniwrapper.Function;
import com.jniwrapper.Int;
import com.jniwrapper.UInt;
import com.jniwrapper.ULongInt;
import com.jniwrapper.win32.Handle;

/**
 * This object represents a global memory block and corresponds to the <code>HGLOBAL</code> native type.
 *
 * @author Orischenko Alexei
 */
public class GlobalMemoryBlock extends Handle
{
    private static final String FUNCTION_GlobalAlloc = "GlobalAlloc";
    private static final String FUNCTION_GlobalFree = "GlobalFree";

    private static final String FUNCTION_GlobalSize = "GlobalSize";

    private static final String FUNCTION_GlobalLock = "GlobalLock";
    private static final String FUNCTION_GlobalUnlock = "GlobalUnlock";

    /**
     * Creates a global memory block.
     */
    public GlobalMemoryBlock()
    {
    }

    /**
     * Creates a LocalMemoryBlock object that references the memory that handle references.
     *
     * @param handle a handle to the local memory block
     */
    public GlobalMemoryBlock(Handle handle)
    {
        super(handle.getValue());
    }

    /**
     * Allocates the global memory block of the specified size.
     *
     * @param size the size of the allocated memory
     * @param attributes allocation attributes
     */
    public GlobalMemoryBlock(int size, MemoryAllocationAttributes attributes)
    {
        alloc(size, attributes);
    }

    /**
     * Frees the allocated memory.
     */
    public void free()
    {
        GlobalMemoryBlock result = new GlobalMemoryBlock();
        final Function function = Kernel32.getInstance().getFunction(FUNCTION_GlobalFree);
        function.invoke(result, this);

        if (!result.isNull())
        {
            throw new RuntimeException("Can't free global memory block.");
        }
    }

    private void alloc(int size, MemoryAllocationAttributes attributes)
    {
        final Function function = Kernel32.getInstance().getFunction(FUNCTION_GlobalAlloc);
        function.invoke(this, new UInt(attributes.getValue()), new ULongInt(size));
    }

    /**
     * Returns the size of the allocated memory block.
     *
     * @return size of the allocated memory block.
     */
    public int size()
    {
        final Function function = Kernel32.getInstance().getFunction(FUNCTION_GlobalSize);
        ULongInt result = new ULongInt();
        function.invoke(result, this);
        return (int)result.getValue();
    }

    /**
     * Locks the memory block and returns the pointer to the allocated memory.
     *
     * @return pointer to the allocated memory.
     */
    public Handle lock()
    {
        Handle result = new Handle();
        final Function function = Kernel32.getInstance().getFunction(FUNCTION_GlobalLock);
        function.invoke(result, this);
        return result;
    }

    /**
     * Decrements the lock count for the memory block.
     *
     * @return lock count after decrementing. If the lock count is zero and the
     * <code>GetLastError</code> function returns <code>NO_ERROR</code>, the memory block is unlocked.
     */
    public int unlock()
    {
        Int result = new Int();
        final Function function = Kernel32.getInstance().getFunction(FUNCTION_GlobalUnlock);
        function.invoke(result, this);

        return (int) result.getValue();
    }
}

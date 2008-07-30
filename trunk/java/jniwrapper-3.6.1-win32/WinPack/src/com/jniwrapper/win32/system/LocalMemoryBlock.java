/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.system;

import com.jniwrapper.*;
import com.jniwrapper.win32.Handle;

/**
 * This class represents a local memory block and corresponds to the <code>HLOCAL</code> native type.
 *
 * @author Orischenko Alexei
 */
public class LocalMemoryBlock extends Handle
{
    private static final String FUNCTION_LocalAlloc = "LocalAlloc";
    private static final String FUNCTION_LocalFree = "LocalFree";

    private static final String FUNCTION_LocalSize = "LocalSize";

    private static final String FUNCTION_LocalLock = "LocalLock";
    private static final String FUNCTION_LocalUnlock = "LocalUnlock";

    /**
     * Creates a LocalMemoryBlock object.
     */
    public LocalMemoryBlock()
    {
    }

    /**
     * Creates a LocalMemoryBlock object that references the memory that handles references.
     *
     * @param handle handle to the local memory block
     */
    public LocalMemoryBlock(Handle handle)
    {
        super(handle.getValue());
    }

    /**
     * Allocates a local memory block of the specified size.
     *
     * @param size the size of the allocated memory
     * @param attributes allocation attributes
     */
    public LocalMemoryBlock(int size, MemoryAllocationAttributes attributes)
    {
        alloc(size, attributes);
    }

    /**
     * Frees the allocated memory.
     *
     * @throws RuntimeException if failed to free the memory block.
     */
    public void free()
    {
        Function function = Kernel32.getInstance().getFunction(FUNCTION_LocalFree);

        LocalMemoryBlock result = new LocalMemoryBlock();
        function.invoke(result, this);

        if (!result.isNull())
        {
            throw new RuntimeException("Can't free local memory block.");
        }
    }

    private void alloc(long numBytes, MemoryAllocationAttributes attributes)
    {
        Function function = Kernel32.getInstance().getFunction(FUNCTION_LocalAlloc);

        function.invoke(this, new Parameter[]{
            new UInt(attributes.getValue()),
            new ULongInt(numBytes)
        });
    }

    /**
     * Returns the size of the allocated memory block.
     *
     * @return size of the allocated memory block.
     */
    public int size()
    {
        final Function function = Kernel32.getInstance().getFunction(FUNCTION_LocalSize);
        UInt result = new UInt();
        function.invoke(result, this);
        return (int)result.getValue();
    }

    /**
     * Locks the memory block and returns a pointer to the allocated memory.
     *
     * @return pointer to the allocated memory.
     */
    public Handle lock()
    {
        Handle result = new Handle();
        final Function function = Kernel32.getInstance().getFunction(FUNCTION_LocalLock);
        function.invoke(result, this);
        return result;
    }

    /**
     * Decrements the lock count for the memory block.
     *
     * @return lock count after decrementing. If the lock count is zero and
     * <code>GetLastError</code> function returns <code>NO_ERROR</code>, the memory block is unlocked.
     */
    public int unlock()
    {
        Int result = new Int();
        final Function function = Kernel32.getInstance().getFunction(FUNCTION_LocalUnlock);
        function.invoke(result, this);

        return (int) result.getValue();
    }
}

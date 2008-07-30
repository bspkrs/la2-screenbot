/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.process.monitoring;

import com.jniwrapper.Function;
import com.jniwrapper.Pointer;
import com.jniwrapper.UInt32;
import com.jniwrapper.win32.system.Kernel32;
import com.jniwrapper.win32.IntPtr;

class HeapEntryIterator extends PerformanceEntryIterator
{
    private static final String FUNCTION_Heap32First = "Heap32First";
    private static final String FUNCTION_Heap32Next = "Heap32Next";

    private long _processID;
    private long _heapID;

    public HeapEntryIterator(long processID, long heapID)
    {
        super();
        _processID = processID;
        _heapID = heapID;
        getFirstEntry();
    }

    PerformanceEntry createEntry()
    {
        return new HeapEntry();
    }

    void getFirstEntry()
    {
        _lastEntry = createEntry();
        getFirstEntryFunction().invoke(_result,
                new Pointer(_lastEntry),
                new UInt32(_processID),
                new IntPtr(_heapID));
    }

    void getNextEntry()
    {
        _lastEntry = createEntry();
        getNextEntryFunction().invoke(_result, new Pointer(_lastEntry));
    }

    Function getFirstEntryFunction()
    {
        return Kernel32.getInstance().getFunction(FUNCTION_Heap32First);
    }

    Function getNextEntryFunction()
    {
        return Kernel32.getInstance().getFunction(FUNCTION_Heap32Next);
    }
}

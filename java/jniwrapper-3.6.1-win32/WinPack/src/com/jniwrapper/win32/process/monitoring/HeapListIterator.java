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
import com.jniwrapper.win32.system.Kernel32;

class HeapListIterator extends PerformanceEntryIterator
{
    private static final String FUNCTION_Heap32ListFirst = "Heap32ListFirst";
    private static final String FUNCTION_Heap32ListNext = "Heap32ListNext";

    public HeapListIterator(Snapshot snapshot)
    {
        super(snapshot);
    }

    PerformanceEntry createEntry()
    {
        return new HeapList();
    }

    Function getFirstEntryFunction()
    {
        return Kernel32.getInstance().getFunction(FUNCTION_Heap32ListFirst);
    }

    Function getNextEntryFunction()
    {
        return Kernel32.getInstance().getFunction(FUNCTION_Heap32ListNext);
    }

}

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

class ThreadEntryIterator extends PerformanceEntryIterator
{
    private static final String FUNCTION_Thread32First = "Thread32First";
    private static final String FUNCTION_Thread32Next = "Thread32Next";

    public ThreadEntryIterator(Snapshot snapshot)
    {
        super(snapshot);
    }

    PerformanceEntry createEntry()
    {
        return new ThreadEntry();
    }

    Function getFirstEntryFunction()
    {
        return Kernel32.getInstance().getFunction(FUNCTION_Thread32First);
    }

    Function getNextEntryFunction()
    {
        return Kernel32.getInstance().getFunction(FUNCTION_Thread32Next);
    }
}

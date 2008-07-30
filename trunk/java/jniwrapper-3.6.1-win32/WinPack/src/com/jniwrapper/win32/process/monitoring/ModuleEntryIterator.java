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
import com.jniwrapper.win32.FunctionName;
import com.jniwrapper.win32.system.Kernel32;

class ModuleEntryIterator extends PerformanceEntryIterator
{
    private static final FunctionName FUNCTION_Module32First = new FunctionName("Module32First", false);
    private static final FunctionName FUNCTION_Module32Next = new FunctionName("Module32Next", false);

    public ModuleEntryIterator(Snapshot snapshot)
    {
        super(snapshot);
    }

    PerformanceEntry createEntry()
    {
        return new ModuleEntry();
    }

    Function getFirstEntryFunction()
    {
        return Kernel32.getInstance().getFunction(FUNCTION_Module32First.toString());
    }

    Function getNextEntryFunction()
    {
        return Kernel32.getInstance().getFunction(FUNCTION_Module32Next.toString());
    }
}

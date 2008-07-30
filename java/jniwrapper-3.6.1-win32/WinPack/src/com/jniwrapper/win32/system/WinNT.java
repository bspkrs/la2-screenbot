/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.system;

import com.jniwrapper.util.FunctionCache;
import com.jniwrapper.Pointer;
import com.jniwrapper.Function;
import com.jniwrapper.LongInt;

/**
 * This class represents native <code>ntdll.dll</code> library.
 */
public class WinNT extends FunctionCache
{
    private static WinNT _instance = new WinNT();

    public static final int STANDARD_RIGHTS_REQUIRED = 0x000F0000;

    private WinNT()
    {
        super("ntdll");
    }

    public static WinNT getInstance()
    {
        return _instance;
    }

    public static void zeroMemory(Pointer.Void dest, long len)
    {
        Function zeroMemoryFunction = getInstance().getFunction("RtlZeroMemory");
        zeroMemoryFunction.invoke(null, dest, new LongInt(len));
    }

    public static void copyMemory(Pointer.Void dest, Pointer.Void src, long len)
    {
        Function copyMemoryFunction = getInstance().getFunction("memcpy");
        copyMemoryFunction.setCallingConvention(Function.CDECL_CALLING_CONVENTION);
        copyMemoryFunction.invoke(null, dest, src, new LongInt(len));
    }
}

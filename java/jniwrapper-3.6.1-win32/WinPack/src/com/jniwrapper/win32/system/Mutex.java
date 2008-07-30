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
import com.jniwrapper.win32.FunctionName;
import com.jniwrapper.win32.Handle;
import com.jniwrapper.win32.LastErrorException;

/**
 * Provides functionality for Windows mutex handling.
 *
 * @author Alexander Evsukov
 */
public class Mutex extends Handle
{
    private static final FunctionName FUNCTION_CREATE_MUTEX = new FunctionName("CreateMutex");
    private static final FunctionName FUNCTION_OPEN_MUTEX = new FunctionName("OpenMutex");

    private Mutex()
    {
    }

    public boolean exists()
    {
        return !isNull();
    }

    public static Mutex openMutex(int desiredAccess, boolean inheritHandle, String mutexName)
    {
        final Function openMutex = Kernel32.getInstance().getFunction(FUNCTION_OPEN_MUTEX.toString());
        final Mutex mutexHandle = new Mutex();
        openMutex.invoke(mutexHandle,
                new UInt32(desiredAccess),
                new Bool(inheritHandle),
                new Str(mutexName));
        return mutexHandle;
    }

    public static Mutex createMutex(Parameter mutexAttributes, boolean initialOwner, String mutexName)
    {
        final Function createMutex = Kernel32.getInstance().getFunction(FUNCTION_CREATE_MUTEX.toString());
        final Mutex mutexHandle = new Mutex();
        long result = createMutex.invoke(mutexHandle,
                mutexAttributes,
                new Bool(initialOwner),
                new Str(mutexName));

        if (mutexHandle.isNull())
        {
            throw new LastErrorException(result, "Mutex creation failed.", true);
        }

        return mutexHandle;
    }

    public static Mutex createMutex(boolean initialOwner, String mutexName)
    {
        return createMutex(new Handle(), initialOwner, mutexName);
    }
}

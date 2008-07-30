/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.process;

import com.jniwrapper.util.FlagSet;

/**
 * ProcessCreationOptions class represents options for process creation.
 */
public class ProcessOptions extends FlagSet
{
    public static final int CREATE_NO_WINDOW = 0x08000000;
    public static final int CREATE_DEFAULT_ERROR_MODE = 0x04000000;
    public static final int CREATE_NEW_CONSOLE = 0x00000010;
    public static final int CREATE_NEW_PROCESS_GROUP = 0x00000200;
    public static final int CREATE_SHARED_WOW_VDM = 0x00001000;
    public static final int CREATE_SUSPENDED = 0x00000004;
    public static final int CREATE_UNICODE_ENVIRONMENT = 0x00000400;
    public static final int CREATE_FORCEDOS = 0x00002000;
    public final static int SYNCHRONIZE = 0x00100000;

    public ProcessOptions()
    {
    }

    public ProcessOptions(long flags)
    {
        super(flags);
    }

    public ProcessOptions(boolean createDefault)
    {
        setDefaultErrorMode(createDefault);
    }

    /**
     * This flag is valid only when starting a console application. If set, the
     * console application is run without a console window.
     */
    public void setNoWindow(boolean value)
    {
        setupFlag(CREATE_NO_WINDOW, value);
    }

    /**
     * This flag is valid only when starting a console application. If set, the
     * console application is run without a console window.
     */
    public boolean isNoWindow()
    {
        return contains(CREATE_NO_WINDOW);
    }

    /**
     * The new process does not inherit the error mode of the calling process.
     * Instead, the new process gets the current default error mode.
     */
    public void setDefaultErrorMode(boolean value)
    {
        setupFlag(CREATE_DEFAULT_ERROR_MODE, value);
    }

    /**
     * The new process does not inherit the error mode of the calling process.
     * Instead, the new process gets the current default error mode.
     */
    public boolean isDefaultErrorMode()
    {
        return contains(CREATE_DEFAULT_ERROR_MODE);
    }

    /**
     * The new process has a new console, instead of inheriting its parent's
     * console (the default).
     */
    public void setNewConsole(boolean value)
    {
        setupFlag(CREATE_NEW_CONSOLE, value);
    }

    /**
     * The new process has a new console, instead of inheriting its parent's
     * console (the default).
     */
    public boolean isNewConsole()
    {
        return contains(CREATE_NEW_CONSOLE);
    }

    /**
     * The new process is the root process of a new process group.
     */
    public void setNewProcessGroup(boolean value)
    {
        setupFlag(CREATE_NEW_PROCESS_GROUP, value);
    }

    /**
     * The new process is the root process of a new process group.
     */
    public boolean isNewProcessGroup()
    {
        return contains(CREATE_NEW_PROCESS_GROUP);
    }

    /**
     * The flag is valid only when starting a 16-bit Windows-based application.
     */
    public void setSharedWOW_VDM(boolean value)
    {
        setupFlag(CREATE_SHARED_WOW_VDM, value);
    }

    /**
     * The flag is valid only when starting a 16-bit Windows-based application.
     */
    public boolean isSharedWOW_VDM()
    {
        return contains(CREATE_SHARED_WOW_VDM);
    }

    /**
     * The primary thread of the new process is created in a suspended state.
     */
    public void setSuspended(boolean value)
    {
        setupFlag(CREATE_SUSPENDED, value);
    }

    /**
     * The primary thread of the new process is created in a suspended state.
     */
    public boolean isSuspended()
    {
        return contains(CREATE_SUSPENDED);
    }

    /**
     * Indicates the format of the Environment parameter.
     */
    public void setUnicodeEnvironment(boolean value)
    {
        setupFlag(CREATE_UNICODE_ENVIRONMENT, value);
    }

    /**
     * Indicates the format of the Environment parameter.
     */
    public boolean isUnicodeEnvironment()
    {
        return contains(CREATE_UNICODE_ENVIRONMENT);
    }

    /**
     * Requires to wait for the process to terminate using the wait functions.
     */
    public void setSynchronize(boolean value)
    {
        setupFlag(SYNCHRONIZE, value);
    }

    /**
     * Requires to wait for the process to terminate using the wait functions.
     */
    public boolean isSynchronize()
    {
        return contains(SYNCHRONIZE);
    }
}

/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.process;

import com.jniwrapper.*;
import com.jniwrapper.util.FlagSet;
import com.jniwrapper.win32.FunctionName;
import com.jniwrapper.win32.LastErrorException;
import com.jniwrapper.win32.system.Kernel32;

/**
 * CurrentProcess class represents a current process and provides various
 * information about it.
 *
 * @author Serge Piletsky
 */
public class CurrentProcess extends Process
{
    private static final String FUNCTION_GetCurrentProcess = "GetCurrentProcess";
    private static final String FUNCTION_GetCurrentProcessId = "GetCurrentProcessId";
    private static final FunctionName FUNCTION_GetCommandLine = new FunctionName("GetCommandLine");
    private static final String FUNCTION_GetProcessShutdownParameters = "GetProcessShutdownParameters";
    private static final String FUNCTION_SetProcessShutdownParameters = "SetProcessShutdownParameters";
    private static final FunctionName FUNCTION_GetStartupInfo = new FunctionName("GetStartupInfo");

    /**
     * Creates an instance that corresponds to currently running process.
     */
    public CurrentProcess()
    {
        final Function function = Kernel32.getInstance().getFunction(FUNCTION_GetCurrentProcess);
        function.invoke(this);
    }

    /**
     * Returns the process identifier of the calling process.
     *
     * @return the process identifier of the calling process.
     */
    public long getProcessID()
    {
        final Function function = Kernel32.getInstance().getFunction(FUNCTION_GetCurrentProcessId);
        UInt32 result = new UInt32();
        function.invoke(result);
        return result.getValue();
    }

    /**
     * Returns the major and minor version numbers of the system on which the
     * specified process is expected to run.
     *
     * @return version of the system on which the process is expected to run.
     */
    public long getVersion()
    {
        return Process.getVersion(0);
    }

    /**
     * Returns the command-line string for the current process.
     *
     * @return the command-line string for the current process.
     */
    public String getCommandLine()
    {
        final Function function = Kernel32.getInstance().getFunction(FUNCTION_GetCommandLine.toString());
        ExternalStringPointer externalStringPointer = new ExternalStringPointer();
        function.invoke(externalStringPointer);
        return externalStringPointer.readString();
    }

    /**
     * ShutdownFlags class represents system shutdown flags.
     */
    public static class ShutdownFlags extends FlagSet
    {
        /**
         * This flag means that if process takes longer than
         * the specified timeout to shut down, a retry dialog box
         * is not displayed.
         */
        public static final int SHUTDOWN_NORETRY = 0x00000001;

        /**
         * Sets flag state.
         *
         * @param set
         */
        public void setNoRetry(boolean set)
        {
            setupFlag(SHUTDOWN_NORETRY, set);
        }

        /**
         * Checks flag state.
         *
         * @return boolean flag state.
         */
        public boolean isNoRetry()
        {
            return contains(SHUTDOWN_NORETRY);
        }
    }

    /**
     * Returns shutdown parameters for the currently calling process.
     *
     * @param flags [out] shutdown flags.
     * @return shutdown priority level.
     */
    public long getShutdownParameters(ShutdownFlags flags)
    {
        if (flags == null)
            throw new IllegalArgumentException();

        final Function function = Kernel32.getInstance().getFunction(FUNCTION_GetProcessShutdownParameters);
        final Bool functionResult = new Bool();
        UInt32 _flags = new UInt32();
        UInt32 level = new UInt32();
        long errorCode = function.invoke(functionResult, new Pointer.OutOnly(level), new Pointer.OutOnly(_flags));
        flags.clear();
        flags.add(_flags.getValue());

        if (!functionResult.getValue())
            throw new LastErrorException(errorCode);

        return level.getValue();
    }

    /**
     * Sets shutdown parameters for the currently calling process.
     *
     * @param level shutdown priority level.
     * @param flags shutdown flags.
     */
    public void setShutdownParameters(long level, ShutdownFlags flags)
    {
        if (flags == null)
            throw new IllegalArgumentException();

        final Function function = Kernel32.getInstance().getFunction(FUNCTION_SetProcessShutdownParameters);
        final Bool functionResult = new Bool();
        UInt32 _flags = new UInt32(flags.getFlags());
        long errorCode = function.invoke(functionResult, new UInt32(level), _flags);

        if (!functionResult.getValue())
            throw new LastErrorException(errorCode);
    }

    /**
     * @return the <code>StartupInfo</code> structure that was specified when the calling process was created.
     */
    public StartupInfo getStartupInfo()
    {
        StartupInfo result = new StartupInfo();
        final Function function = Kernel32.getInstance().getFunction(FUNCTION_GetStartupInfo.toString());
        Bool returnValue = new Bool();
        long errorCode = function.invoke(returnValue, new Pointer.OutOnly(result));

        if (!returnValue.getValue())
            throw new LastErrorException(errorCode);

        return result;
    }
}

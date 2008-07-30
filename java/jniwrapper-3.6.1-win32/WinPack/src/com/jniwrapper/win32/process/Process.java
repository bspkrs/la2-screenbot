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
import com.jniwrapper.util.EnumItem;
import com.jniwrapper.util.Enums;
import com.jniwrapper.util.FlagSet;
import com.jniwrapper.util.Logger;
import com.jniwrapper.win32.FunctionName;
import com.jniwrapper.win32.Handle;
import com.jniwrapper.win32.LastErrorException;
import com.jniwrapper.win32.io.FileTime;
import com.jniwrapper.win32.system.Kernel32;
import com.jniwrapper.win32.system.SecurityAttributes;
import com.jniwrapper.win32.system.AdvApi32;
import com.jniwrapper.win32.ui.User32;
import com.jniwrapper.win32.ui.Wnd;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Process class represents a process and provides various information about it.
 * <p/>
 * NOTE: Every created or opened process should be closed using the <code>close()</code> method.
 *
 * @author Serge Piletsky
 * @author Vladimir Kondrashchenko
 */
public class Process extends Handle
{
    private static final Logger LOG = Logger.getInstance(Process.class);

    private static final FunctionName FUNCTION_CreateProcess = new FunctionName("CreateProcess");
    private static final String FUNCTION_CreateProcessWithLogonW = "CreateProcessWithLogonW";
    private static final String FUNCTION_GetProcessID = "GetProcessId"; // available only in WinXP SP1 or Win2003 Server
    private static final String FUNCTION_GetExitCodeProcess = "GetExitCodeProcess";
    private static final String FUNCTION_GetPriorityClass = "GetPriorityClass";
    private static final String FUNCTION_GetGuiResources = "GetGuiResources";
    private static final String FUNCTION_GetProcessHandleCount = "GetProcessHandleCount";
    private static final String FUNCTION_GetProcessIoCounters = "GetProcessIoCounters";
    private static final String FUNCTION_GetProcessPriorityBoost = "GetProcessPriorityBoost";
    private static final String FUNCTION_GetProcessTimes = "GetProcessTimes";
    private static final String FUNCTION_OpenProcess = "OpenProcess";
    private static final String FUNCTION_SetPriorityClass = "SetPriorityClass";
    private static final String FUNCTION_SetProcessPriorityBoost = "SetProcessPriorityBoost";
    private static final String FUNCTION_TerminateProces = "TerminateProcess";
    private static final String FUNCTION_GetProcessVersion = "GetProcessVersion";
    private static final String FUNCTION_GetProcessIdOfThread = "GetProcessIdOfThread";
    private static final String FUNCTION_GetThreadID = "GetThreadId";
    private static final String FUNCTION_GetProcessWorkingSetSize = "GetProcessWorkingSetSize";
    private static final String FUNCTION_SetProcessWorkingSetSize = "SetProcessWorkingSetSize";
    private static final String FUNCTION_GetProcessAffinityMask = "GetProcessAffinityMask";
    private static final String FUNCTION_SetProcessAffinityMask = "SetProcessAffinityMask";
    private static final String FUNCTION_GetProcessMemoryInfo = "GetProcessMemoryInfo";
    private static final FunctionName FUNCTION_GetProcessImageFileName = new FunctionName("GetProcessImageFileName");
    private static final String FUNCTION_EnumProcesses = "EnumProcesses";
    private static final String FUNCTION_EnumProcessModules = "EnumProcessModules";
    private static final FunctionName FUNCTION_GET_MODULE_FILE_NAME_EX = new FunctionName("GetModuleFileNameEx");
    private static final String FUNCTION_EnumWindows = "EnumWindows";

    private static final int GR_GDIOBJECTS = 0;
    private static final int GR_USEROBJECTS = 1;

    private static final int LOGON_WITH_PROFILE = 1;
    private static final int LOGON_NETCREDENTIALS_ONLY = 2;

    private Handle _thread = new Handle();
    private long _processID;
    private long _threadID;

    Process(long value)
    {
        super(value);
    }

    Process()
    {
    }

    /**
     * Creates a new process and its primary thread.
     *
     * @param commandLine Can be null; specifies the command line to execute.
     */
    public Process(String commandLine)
    {
        this(null, commandLine, new ProcessOptions(true), null, new StartupInfo());
    }

    /**
     * Creates a new process and its primary thread.
     *
     * @param applicationName  Can be null; specifies the module to execute in which case the executable name must be in the white space-delimited string pointed to by CommandLine.
     * @param commandLine      Can be null; specifies the command line to execute.
     * @param options          Options that control the priority class and the creation of the process.
     * @param currentDirectory Can be null; specifies the current drive and directory for the new process.
     */
    public Process(String applicationName,
                   String commandLine,
                   ProcessOptions options,
                   String currentDirectory,
                   StartupInfo startupInfo)
    {
        this(applicationName,
                commandLine,
                null,
                null,
                false,
                options,
                null,
                currentDirectory,
                startupInfo);
    }

    /**
     * Creates a new process and its primary thread.
     *
     * @param applicationName   Can be null; specifies the module to execute in which case the executable name must be in the white space-delimited string pointed to by CommandLine.
     * @param commandLine       Can be null; specifies the command line to execute.
     * @param processAttributes Can be null; determines whether the returned handle can be inherited by child processes.
     * @param threadAttributes  Can be null; determines whether the returned handle can be inherited by child processes.
     * @param inheritHandles    If this parameter is true, each inheritable handle in the calling process is inherited by the new process. If the parameter is false, the handles are not inherited.
     * @param options           Options that control the priority class and the creation of the process.
     * @param environment       Can be null; environment block for the new process; If this parameter is null, the new process uses the environment of the calling process.
     * @param currentDirectory  Can be null; specifies the current drive and directory for the new process.
     * @param startupInfo       Specifies the window station, desktop, standard handles, and appearance of the main window for the new process.
     */
    public Process(String applicationName,
                   String commandLine,
                   SecurityAttributes processAttributes,
                   SecurityAttributes threadAttributes,
                   boolean inheritHandles,
                   ProcessOptions options,
                   ProcessVariables environment,
                   String currentDirectory,
                   StartupInfo startupInfo)
    {
        ProcessInformation processInformation = new ProcessInformation();
        Bool functionResult = new Bool();
        final Function function = Kernel32.getInstance().getFunction(FUNCTION_CreateProcess.toString());
        long errorCode = function.invoke(functionResult, new Parameter[]
        {
            applicationName == null ? new Handle() : (Parameter)new Str(applicationName),
            commandLine == null ? new Handle() : (Parameter)new Str(commandLine),
            new Pointer(processAttributes, processAttributes == null),
            new Pointer(threadAttributes, threadAttributes == null),
            new Bool(inheritHandles),
            new UInt32(options.getFlags()),
            environment == null ? new Handle() : environment,
            currentDirectory == null ? new Handle() : (Parameter)new Str(currentDirectory),
            new Pointer(startupInfo),
            new Pointer.OutOnly(processInformation)
        });

        if (!functionResult.getValue())
            throw new LastErrorException(errorCode);

        setValue(processInformation.getProcess().getValue());
        _processID = processInformation.getProcessID();
        _threadID = processInformation.getThreadID();
        registerResource();
    }

    /**
     * Creates a new process and its primary thread. The new process then runs the specified executable file in the security context of the specified credentials (user, domain, and password).
     *
     * @param userName         Specifies the name of the user.
     * @param domain           Can be null; specifies the name of the domain or server whose account database contains the lpUsername account.
     * @param password         Specifies the clear-text password for the lpUsername account.
     * @param logonWithProfile Specifies the way to logon. True specifies logon with profile, false - logon with net credentials only.
     * @param applicationName  Can be null; specifies the module to execute in which case the executable name must be in the white space-delimited string pointed to by CommandLine.
     * @param commandLine      Can be null; specifies the command line to execute.
     * @param options          Options that control the priority class and the creation of the process.
     * @param environment      Can be null; environment block for the new process; If this parameter is null, the new process uses the environment of the calling process.
     * @param currentDirectory Can be null; specifies the current drive and directory for the new process.
     * @param startupInfo      Specifies the window station, desktop, standard handles, and appearance of the main window for the new process.
     */
    public Process(String userName,
                   String domain,
                   String password,
                   boolean logonWithProfile,
                   String applicationName,
                   String commandLine,
                   ProcessOptions options,
                   ProcessVariables environment,
                   String currentDirectory,
                   StartupInfo startupInfo)
    {
        ProcessInformation processInformation = new ProcessInformation();
        Bool functionResult = new Bool();
        final Function function = AdvApi32.getInstance().getFunction(FUNCTION_CreateProcessWithLogonW);
        long errorCode = function.invoke(functionResult, new Parameter[]
        {
            new Str(userName),
            domain == null ? new Handle() : (Parameter)new Str(domain),
            new Str(password),
            new UInt32(logonWithProfile ? LOGON_WITH_PROFILE : LOGON_NETCREDENTIALS_ONLY),
            applicationName == null ? new Handle() : (Parameter)new Str(applicationName),
            commandLine == null ? new Handle() : (Parameter)new Str(commandLine),
            new UInt32(options.getFlags()),
            environment == null ? new Handle() : environment,
            currentDirectory == null ? new Handle() : (Parameter)new Str(currentDirectory),
            new Pointer(startupInfo),
            new Pointer.OutOnly(processInformation)
        });

        if (!functionResult.getValue())
            throw new LastErrorException(errorCode);

        setValue(processInformation.getProcess().getValue());
        _processID = processInformation.getProcessID();
        _threadID = processInformation.getThreadID();
        registerResource();
    }

    /**
     * Opens an existing process object.
     *
     * @param desiredAccess Access rights to the process object.
     * @param inheritHandle If this parameter is true, the handle is inheritable. Otherwise the handle cannot be inherited.
     * @param processID     ID of the process to open.
     * @return the opened process.
     */
    public static Process openProcess(ProcessSecurityOptions desiredAccess,
                                      boolean inheritHandle,
                                      long processID)
    {
        Process result = new Process();
        final Function function = Kernel32.getInstance().getFunction(FUNCTION_OpenProcess);
        function.invoke(result, new UInt32(desiredAccess.getFlags()), new Bool(inheritHandle), new UInt32(processID));
        result._processID = processID;
        result._threadID = 0;
        result.registerResource();
        return result;
    }

    /**
     * Registers process handle to be garbage-collected automatically.
     */
    protected void registerResource()
    {
        NativeResourceCollector.getInstance().addNativeResource(this, new ProcessResource(getValue()));
    }

    /**
     * Returns the identifier of the process.
     * <p/>
     * IMPORTANT NOTE: This function is available in WinXP (since SP1) and WinServer 2003 only.
     *
     * @return process ID.
     */
    public static long getProcessID(Process process)
    {
        UInt32 result = new UInt32();
        final Function function = Kernel32.getInstance().getFunction(FUNCTION_GetProcessID);
        function.invoke(result, process);
        return result.getValue();
    }

    /**
     * Returns the termination status of the process.
     *
     * @return termination status of the process.
     */
    public long getExitCode()
    {
        UInt32 result = new UInt32();
        final Function function = Kernel32.getInstance().getFunction(FUNCTION_GetExitCodeProcess);
        function.invoke(null, this, new Pointer.OutOnly(result));
        return result.getValue();
    }

    /**
     * PrioriryClass class is enumeration of priorities for a system process.
     */
    public static class PriorityClass extends EnumItem
    {
        public static final PriorityClass NORMAL_PRIORITY_CLASS = new PriorityClass(0x00000020);
        public static final PriorityClass IDLE_PRIORITY_CLASS = new PriorityClass(0x00000040);
        public static final PriorityClass HIGH_PRIORITY_CLASS = new PriorityClass(0x00000080);
        public static final PriorityClass REALTIME_PRIORITY_CLASS = new PriorityClass(0x00000100);

        PriorityClass(int value)
        {
            super(value);
        }
    }

    /**
     * Returns the priority class for the specified process.
     *
     * @return the priority class for the specified process.
     */
    public PriorityClass getPriorityClass()
    {
        UInt32 result = new UInt32();
        final Function function = Kernel32.getInstance().getFunction(FUNCTION_GetPriorityClass);
        function.invoke(result, this);
        return (PriorityClass)Enums.getItem(PriorityClass.class, (int)result.getValue());
    }

    /**
     * Sets the priority class for the specified process.
     *
     * @param prioriryClass Priority class for the process
     */
    public void setPriorityClass(PriorityClass prioriryClass)
    {
        final Function function = Kernel32.getInstance().getFunction(FUNCTION_SetPriorityClass);
        Bool functionResult = new Bool();
        long errorCode = function.invoke(functionResult, this, new UInt32(prioriryClass.getValue()));
        if (!functionResult.getValue())
            throw new LastErrorException(errorCode);
    }

    /**
     * Returns the count of handles to graphical user interface (GUI) objects in use by the specified process.
     */
    private long getGuiResourceCount(long flags)
    {
        UInt32 result = new UInt32();
        final Function function = User32.getInstance().getFunction(FUNCTION_GetGuiResources);
        function.invoke(result, this, new UInt32(flags));
        return result.getValue();
    }

    /**
     * Returns the count of GDI objects.
     *
     * @return the count of GDI objects.
     */
    public long getGdiObjectsCount()
    {
        return getGuiResourceCount(GR_GDIOBJECTS);
    }

    /**
     * Returns the count of USER objects.
     *
     * @return the count of GDI objects.
     */
    public long getUSERObjectsCount()
    {
        return getGuiResourceCount(GR_USEROBJECTS);
    }

    /**
     * Returns the number of open handles that belong to the specified process.
     *
     * @return number of open handles that belong to the specified process.
     */
    public long getHandleCount()
    {
        UInt32 result = new UInt32();
        final Function function = Kernel32.getInstance().getFunction(FUNCTION_GetProcessHandleCount);
        Bool returnValue = new Bool();
        long errorCode = function.invoke(returnValue, this, new Pointer(result));

        if (!returnValue.getValue())
            throw new LastErrorException(errorCode);

        return result.getValue();
    }

    /**
     * Returns accounting information for all I/O operations performed by the specified process.
     *
     * @return accounting information.
     */
    public IOCounters getIOCounters()
    {
        IOCounters result = new IOCounters();
        final Function function = Kernel32.getInstance().getFunction(FUNCTION_GetProcessIoCounters);
        Bool returnValue = new Bool();
        long errorCode = function.invoke(returnValue, this, new Pointer.OutOnly(result));

        if (!returnValue.getValue())
            throw new LastErrorException(errorCode);

        return result;
    }

    /**
     * Returns timing information about the specified process.
     *
     * @param creationTime [out] the creation time of the process.
     * @param exitTime     [out] the exit time of the process.
     * @param kernelTime   [out] the amount of time that the process has executed in kernel mode.
     * @param userTime     [out] the amount of time that the process has executed in user mode.
     */
    public void getProcessTimes(FileTime creationTime,
                                FileTime exitTime,
                                FileTime kernelTime,
                                FileTime userTime)
    {
        final Function function = Kernel32.getInstance().getFunction(FUNCTION_GetProcessTimes);
        Bool returnValue = new Bool();
        long errorCode = function.invoke(returnValue, new Parameter[]
        {
            this,
            new Pointer.OutOnly(creationTime),
            new Pointer.OutOnly(exitTime),
            new Pointer.OutOnly(kernelTime),
            new Pointer.OutOnly(userTime),
        });

        if (!returnValue.getValue())
            throw new LastErrorException(errorCode);
    }

    /**
     * Enables or disables the ability of the system to temporarily boost the priority of the threads of the process.
     *
     * @param enable If this parameter is true, dynamic boosting is enabled. If the parameter is false, dynamic boosting is disabled.
     */
    public void setPriorityBoost(boolean enable)
    {
        final Function function = Kernel32.getInstance().getFunction(FUNCTION_SetProcessPriorityBoost);
        Bool returnValue = new Bool();
        long errorCode = function.invoke(returnValue, this, new Bool(!enable));

        if (!returnValue.getValue())
            throw new LastErrorException(errorCode);
    }

    /**
     * Returns the priority boost control state of the process.
     *
     * @return true indicates that dynamic boosting is enabled; false otherwise.
     */
    public boolean getPriorityBoost()
    {
        final Function function = Kernel32.getInstance().getFunction(FUNCTION_GetProcessPriorityBoost);
        Bool result = new Bool();
        Bool functionResult = new Bool();
        long errorCode = function.invoke(functionResult, this, new Pointer(result));

        if (!functionResult.getValue())
            throw new LastErrorException(errorCode);

        return !result.getValue();
    }

    /**
     * Terminates the process and all of its threads.
     *
     * @param exitCode the process exit code.
     */
    public void terminate(long exitCode)
    {
        final Function function = Kernel32.getInstance().getFunction(FUNCTION_TerminateProces);
        Bool functionResult = new Bool();
        long errorCode = function.invoke(functionResult, this, new UInt(exitCode));

        if (!functionResult.getValue())
            throw new LastErrorException(errorCode);
    }

    /**
     * Returns the major and minor version numbers of the system on which the specified process expects to run.
     * The high word of the return value contains the major version number.
     * The low word of the return value contains the minor version number.
     *
     * @return version of the system on which the process expects to run.
     */
    public long getVersion()
    {
        return getVersion(getProcessID());
    }

    /**
     * Returns the major and minor version numbers of the system on which the specified process expects to run.
     * The high word of the return value contains the major version number.
     * The low word of the return value contains the minor version number.
     *
     * @param processID identifier of the process.
     * @return version of the system on which the process expects to run.
     */
    public static long getVersion(long processID)
    {
        final Function function = Kernel32.getInstance().getFunction(FUNCTION_GetProcessVersion);
        UInt32 result = new UInt32();
        function.invoke(result, new UInt32(processID));
        return result.getValue();
    }

    /**
     * Returns the minimum and maximum working set sizes of the specified process.
     * <p/>
     * The "Working Set" of a process is a set of memory pages currently visible to the process
     * in physical RAM memory. These pages are resident and available for an application to use
     * without triggering a page fault. The minimum and maximum working set sizes affect the
     * virtual memory paging behavior of a process.
     *
     * @param minimumWorkingSetSize [out] Minimum working set size for the process, in bytes.
     * @param maximumWorkingSetSize [out] Maximum working set size for the process, in bytes.
     */
    public void getWorkingSetSize(UInt32 minimumWorkingSetSize, UInt32 maximumWorkingSetSize)
    {
        final Function function = Kernel32.getInstance().getFunction(FUNCTION_GetProcessWorkingSetSize);
        Bool functionResult = new Bool();
        long errorCode = function.invoke(functionResult, this, new Pointer(minimumWorkingSetSize), new Pointer(maximumWorkingSetSize));

        if (!functionResult.getValue())
            throw new LastErrorException(errorCode);
    }

    /**
     * Sets the minimum and maximum working set sizes for the specified process.
     * <p/>
     * The "Working Set" of a process is a set of memory pages currently visible to the process
     * in physical RAM memory. These pages are resident and available for an application to use
     * without triggering a page fault. The minimum and maximum working set sizes affect the
     * virtual memory paging behavior of a process.
     *
     * @param minSize Minimum working set size for the process, in bytes.
     * @param maxSize Maximum working set size for the process, in bytes.
     */
    public void setWorkingSetSize(long minSize, long maxSize)
    {
        final Function function = Kernel32.getInstance().getFunction(FUNCTION_SetProcessWorkingSetSize);
        Bool functionResult = new Bool();
        long errorCode = function.invoke(functionResult, this, new UInt32(minSize), new UInt32(maxSize));

        if (!functionResult.getValue())
            throw new LastErrorException(errorCode);
    }

    private void retrieveAffinityMasks(UInt32 processAffinityMask, UInt32 systemAffinityMask)
    {
        final Function function = Kernel32.getInstance().getFunction(FUNCTION_GetProcessAffinityMask);
        Bool functionResult = new Bool();
        long errorCode = function.invoke(functionResult, this, new Pointer(processAffinityMask), new Pointer(systemAffinityMask));

        if (!functionResult.getValue())
            throw new LastErrorException(errorCode);
    }

    /**
     * Returns the process affinity mask for the specified process and the system affinity mask for the system.
     * <p/>
     * A process affinity mask is a bit vector in which each bit represents the processors
     * that a process is allowed to run on.
     *
     * @return the process affinity mask.
     */
    public long getAffinityMask()
    {
        UInt32 processAffinityMask = new UInt32();
        UInt32 systemAffinityMask = new UInt32();
        retrieveAffinityMasks(processAffinityMask, systemAffinityMask);

        return processAffinityMask.getValue();
    }

    /**
     * Returns the system affinity mask for the system.
     * <p/>
     * A system affinity mask is a bit vector in which each bit represents the processors that
     * are configured into a system.
     *
     * @return the the system affinity mask.
     */
    public long getSystemAffinityMask()
    {
        UInt32 processAffinityMask = new UInt32();
        UInt32 systemAffinityMask = new UInt32();
        retrieveAffinityMasks(processAffinityMask, systemAffinityMask);

        return systemAffinityMask.getValue();
    }

    /**
     * Sets a processor affinity mask for the threads of the process.
     *
     * @param affinityMask Affinity mask for the threads of the process.
     */
    public void setAffinityMask(long affinityMask)
    {
        final Function function = Kernel32.getInstance().getFunction(FUNCTION_SetProcessAffinityMask);
        Bool functionResult = new Bool();
        long errorCode = function.invoke(functionResult, this, new UInt32(affinityMask));

        if (!functionResult.getValue())
            throw new LastErrorException(errorCode);
    }

    /**
     * Returns the process identifier of the process associated with a specified thread.
     *
     * @param thread Handle of the thread.
     * @return a process ID.
     */
    public static long getThreadProcessID(Handle thread)
    {
        final Function function = Kernel32.getInstance().getFunction(FUNCTION_GetProcessIdOfThread);
        UInt32 result = new UInt32();
        function.invoke(result, thread);
        return result.getValue();
    }

    /**
     * Returns the identifier of the thread.
     *
     * @param thread Handle to the thread.
     * @return a thread ID.
     */
    public static long getThreadID(Handle thread)
    {
        final Function function = Kernel32.getInstance().getFunction(FUNCTION_GetThreadID);
        UInt32 result = new UInt32();
        function.invoke(result, thread);
        return result.getValue();
    }

    /**
     * Causes the current thread to wait, if necessary, until the
     * process represented by this <code>Process</code> object has
     * terminated.
     *
     * @param timeout Time-out interval, in milliseconds.
     * @return exit code of the process.
     */
    public long waitFor(long timeout)
    {
        long waitStatus = Handle.waitFor(this, timeout);
        switch ((int)waitStatus)
        {
            case STATUS_ABANDONED_WAIT_0:
                throw new RuntimeException("Abandoned.");

            case STATUS_TIMEOUT:
                throw new RuntimeException("Timeout elapsed.");
        }
        return waitStatus;
    }

    /**
     * Causes the current thread to wait, if necessary, until the
     * process represented by this <code>Process</code> object has
     * terminated.
     *
     * @return process Exit Code
     */
    public long waitFor()
    {
        long waitStatus = Handle.waitFor(this);
        switch ((int)waitStatus)
        {
            case STATUS_ABANDONED_WAIT_0:
                throw new RuntimeException("Abandoned.");

            case STATUS_TIMEOUT:
                throw new RuntimeException("Timeout elapsed.");
        }
        return waitStatus;
    }

    /**
     * Returns the identifier of the thread.
     *
     * @return thread identifier 
     */
    public long getThreadID()
    {
        return _threadID;
    }

    /**
     * Returns the identifier of the process.
     *
     * @return process ID.
     */
    public long getProcessID()
    {
        return _processID;
    }

    /**
     * Returns a Handle to the primary thread of the process.
     *
     * @return a Handle to the primary thread of the process.
     */
    public Handle getThread()
    {
        return _thread;
    }

    /**
     * Closes the opened process safely and all associated handles.
     */
    public void close()
    {
        try
        {
            close(this);
        }
        catch (Throwable e)
        {
            LOG.error("", e);
        }
    }

    /**
     * Returns information about the memory usage of this process.
     *
     * @return information about the memory usage of this process.
     */
    public ProcessMemoryCounters getProcessMemoryCounters()
    {
        ProcessMemoryCounters processMemoryCounters = new ProcessMemoryCounters();
        Function getProcessMemoryInfo = Psapi.getInstance().getFunction(FUNCTION_GetProcessMemoryInfo);
        IntBool result = new IntBool();
        long errorCode = getProcessMemoryInfo.invoke(result, this, new Pointer(processMemoryCounters), new UInt32(processMemoryCounters.getLength()));
        if (result.getValue() == 0)
        {
            throw new LastErrorException(errorCode);
        }
        return processMemoryCounters;
    }

    /**
     * Returns the name of the executable file for this process.
     * <b>NOTE:</b> Requires Windows XP operation system or higher.
     * For lower versions of operation systems use <CODE>getProcessFileName()</CODE> method.
     *
     * @return the name of the executable file for this process.
     */
    public String getProcessImageFileName()
    {
        Function getProcessImageFileName = Psapi.getInstance().getFunction(FUNCTION_GetProcessImageFileName.toString());
        UInt32 result = new UInt32();
        int bufferLength = 1000;
        Str imageName = new Str(bufferLength);
        long errorCode = getProcessImageFileName.invoke(result, this, new Pointer(imageName), new UInt32(bufferLength));
        if (result.getValue() == 0)
        {
            return null;
        }
        return imageName.getValue();
    }

    /**
     * Returns the name of the executable file for this process.
     * <b>NOTE:</b> Use this method for versions of operation system lower than
     * Windows XP otherwise it is recommended to use getProcessImageFileName() method.
     *
     * @return the name of the executable file for this process.
     */
    public String getProcessFileName()
    {
        try
        {
            List modules = getModules();
            return getModuleFileName((Handle)modules.get(0));
        }
        catch (Exception e)
        {
            return null;
        }
    }

    private static void close(Process process)
    {
        if (!process.isNull())
        {
            final Handle thread = process.getThread();
            if (!thread.isNull())
            {
                Handle.closeHandle(thread);
            }
            Handle.closeHandle(process);
        }
    }

    /**
     * This class responds for destroying a native resource when the instance is collected by garbage-collector.
     */
    protected static class ProcessResource implements NativeResource
    {
        private long _handle;

        public ProcessResource(long handle)
        {
            _handle = handle;
        }

        /**
         * Frees a process.
         *
         * @throws Throwable
         */
        public void release() throws Throwable
        {
            close(new Process(_handle));
        }
    }

    /**
     * Gets a list of all running processes. Each process is represented by
     * <CODE>Process</CODE> class instance.
     *
     * @return an array of <CODE>Process</CODE> class instances.
     */
    public static List getProcesses()
    {
        ProcessSecurityOptions processSecurityOptions = new ProcessSecurityOptions();
        processSecurityOptions.setQueryInformation(true);
        processSecurityOptions.setVMRead(true);
        return getProcesses(processSecurityOptions);
    }

    /**
     * Gets a list of all running processes with the specified <CODE>ProcessSecurityOptions</CODE>. Each process is represented by
     * <CODE>Process</CODE> class instance.
     *
     * @param processSecurityOptions security options for all the processes of the list.
     * @return an array of <CODE>Process</CODE> class instances.
     */
    public static List getProcesses(ProcessSecurityOptions processSecurityOptions)
    {
        final Function function = Psapi.getInstance().getFunction(FUNCTION_EnumProcesses);
        Bool res = new Bool();
        PrimitiveArray processID = new PrimitiveArray(UInt32.class, 1024);
        UInt32 cb = new UInt32();
        cb.setValue(processID.getLength());
        UInt32 bytesReturned = new UInt32();
        do
        {
            function.invoke(res, new Pointer(processID), cb, new Pointer(bytesReturned));
            if (!res.getValue())
            {
                return null;
            }
            if (bytesReturned.getValue() == cb.getValue())
            {
                processID.setElementCount(processID.getElementCount() * 10);
                cb.setValue(processID.getLength());
            }
            else
            {
                break;
            }
        }
        while (true);

        int count = (int)bytesReturned.getValue() / bytesReturned.getLength();
        List result = new ArrayList(count);

        for (int i = 0; i < count; i++)
        {
            int id = (int)((UInt32)processID.getElement(i)).getValue();
            result.add(Process.openProcess(processSecurityOptions, false, id));
        }

        return result;
    }

    /**
     * Gets the list of handles of the modules attached to the process.
     *
     * @return the list of handles of the modules.
     */
    public List getModules()
    {
        Function function = Psapi.getInstance().getFunction(FUNCTION_EnumProcessModules);
        IntBool funcRes = new IntBool();
        UInt32 bytesNeeded = new UInt32();
        PrimitiveArray modules = new PrimitiveArray(Handle.class, 1024);
        do
        {
            funcRes.setValue(0);
            long errorCode = function.invoke(funcRes,
                    this,
                    new Pointer(modules),
                    new UInt32(modules.getLength()),
                    new Pointer(bytesNeeded));
            if (bytesNeeded.getValue() > modules.getLength())
            {
                modules.setElementCount(modules.getElementCount() * 10);
                continue;
            }
            else
            {
                if (funcRes.getValue() == 0)
                {
                    throw new LastErrorException(errorCode, "Unable to get list of modules of the process.");
                }
                break;
            }
        }
        while (true);

        int count = (int)(bytesNeeded.getValue() / new Handle().getLength());

        List result = new ArrayList(count);

        for (int i = 0; i < count; i++)
        {
            result.add((Handle)modules.getElement(i));
        }

        return result;
    }

    /**
     * Gets the file name of the specified module attached to the process.
     *
     * @param module is the handle of the module attached to the process.
     * @return the file name of the module.
     */
    public String getModuleFileName(Handle module)
    {
        final Function function = Psapi.getInstance().getFunction(FUNCTION_GET_MODULE_FILE_NAME_EX.toString());
        UInt32 res = new UInt32();
        Str fileName = new Str(1024);
        function.invoke(res, this, module, new Pointer(fileName), new UInt32(fileName.getLength()));
        if (res.getValue() > fileName.getMaxLength())
        {
            fileName = new Str((int)res.getValue());
            function.invoke(res, this, module, new Pointer(fileName), new UInt32(fileName.getLength()));
        }
        return fileName.getValue();
    }

    public static List getApplicationWindows()
    {
        List windows = Wnd.getAllWindows();
        List result = new LinkedList();
        for (int i = 0; i < windows.size(); i++)
        {
            Wnd wnd = (Wnd)windows.get(i);
            FlagSet style = new FlagSet(wnd.getWindowStyle());
            if (wnd.getParent().isNull() && wnd.isVisible() && style.contains(Wnd.WS_OVERLAPPEDWINDOW))
            {
                result.add(wnd);
            }
        }
        return result;
    }
}

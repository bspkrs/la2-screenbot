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
import com.jniwrapper.win32.system.AccessOptions;

/**
 * ProcessSecurityOptions class represents a set of options required for opening
 * a process.
 * 
 * @author Serge Piletsky
 */
public class ProcessSecurityOptions extends FlagSet
{
    public static final int PROCESS_TERMINATE = 0x0001;
    public static final int PROCESS_CREATE_THREAD = 0x0002;
    public static final int PROCESS_SET_SESSIONID = 0x0004;
    public static final int PROCESS_VM_OPERATION = 0x0008;
    public static final int PROCESS_VM_READ = 0x0010;
    public static final int PROCESS_VM_WRITE = 0x0020;
    public static final int PROCESS_DUP_HANDLE = 0x0040;
    public static final int PROCESS_CREATE_PROCESS = 0x0080;
    public static final int PROCESS_SET_QUOTA = 0x0100;
    public static final int PROCESS_SET_INFORMATION = 0x0200;
    public static final int PROCESS_QUERY_INFORMATION = 0x0400;
    public static final int PROCESS_ALL_ACCESS = AccessOptions.STANDARD_RIGHTS_REQUIRED.getValue() | ProcessOptions.SYNCHRONIZE | 0xFFF;

    public ProcessSecurityOptions()
    {
    }

    public ProcessSecurityOptions(boolean allAccess)
    {
        setupFlag(PROCESS_ALL_ACCESS, allAccess);
    }

    public ProcessSecurityOptions(long flags)
    {
        super(flags);
    }

    /**
     * All possible access rights for a process object.
     */
    public void setAllAccess(boolean value)
    {
        setupFlag(PROCESS_ALL_ACCESS, value);
    }

    /**
     * All possible access rights for a process object.
     */
    public boolean isAllAccess()
    {
        return contains(PROCESS_ALL_ACCESS);
    }

    /**
     * Required to create a process.
     */
    public void setCreateProcess(boolean value)
    {
        setupFlag(PROCESS_CREATE_PROCESS, value);
    }

    /**
     * Required to create a process.
     */
    public boolean isCreateProcess()
    {
        return contains(PROCESS_CREATE_PROCESS);
    }

    /**
     * Required to create a thread.
     */
    public void setCreateThread(boolean value)
    {
        setupFlag(PROCESS_CREATE_THREAD, value);
    }

    /**
     * Required to create a thread.
     */
    public boolean isCreateThread()
    {
        return contains(PROCESS_CREATE_THREAD);
    }

    /**
     * Required to duplicate a handle.
     */
    public void setDuplicateHandle(boolean value)
    {
        setupFlag(PROCESS_DUP_HANDLE, value);
    }

    /**
     * Required to duplicate a handle.
     */
    public boolean isDuplicateHandle()
    {
        return contains(PROCESS_DUP_HANDLE);
    }

    /**
     * Required to retrieve certain information about a process, such as its
     * exit code and priority class.
     */
    public void setQueryInformation(boolean value)
    {
        setupFlag(PROCESS_QUERY_INFORMATION, value);
    }

    /**
     * Required to retrieve certain information about a process, such as its
     * exit code and priority class.
     */
    public boolean isQueryInformation()
    {
        return contains(PROCESS_QUERY_INFORMATION);
    }

    /**
     * Required to set memory limits.
     */
    public void setQuota(boolean value)
    {
        setupFlag(PROCESS_SET_QUOTA, value);
    }

    /**
     * Required to set memory limits.
     */
    public boolean isQuota()
    {
        return contains(PROCESS_SET_QUOTA);
    }

    /**
     * Required to set certain information about a process, such as its priority
     * class.
     */
    public void setInformation(boolean value)
    {
        setupFlag(PROCESS_SET_INFORMATION, value);
    }

    /**
     * Required to set certain information about a process, such as its priority
     * class.
     */
    public boolean isInformation()
    {
        return contains(PROCESS_SET_INFORMATION);
    }

    /**
     * Required to terminate a process.
     */
    public void setTerminate(boolean value)
    {
        setupFlag(PROCESS_TERMINATE, value);
    }

    /**
     * Required to terminate a process.
     */
    public boolean isTerminate()
    {
        return contains(PROCESS_TERMINATE);
    }

    /**
     * Required to perform an operation on the address space of a process.
     */
    public void setVMOperation(boolean value)
    {
        setupFlag(PROCESS_VM_OPERATION, value);
    }

    /**
     * Required to perform an operation on the address space of a process.
     */
    public boolean isVMOperation()
    {
        return contains(PROCESS_VM_OPERATION);
    }

    /**
     * Required to read memory in a process.
     */
    public void setVMRead(boolean value)
    {
        setupFlag(PROCESS_VM_READ, value);
    }

    /**
     * Required to read memory in a process.
     */
    public boolean isVMRead()
    {
        return contains(PROCESS_VM_READ);
    }

    /**
     * Required to write to memory in a process.
     */
    public void setVMWrite(boolean value)
    {
        setupFlag(PROCESS_VM_WRITE, value);
    }

    /**
     * Required to write to memory in a process.
     */
    public boolean isVMWrite()
    {
        return contains(PROCESS_VM_WRITE);
    }
}

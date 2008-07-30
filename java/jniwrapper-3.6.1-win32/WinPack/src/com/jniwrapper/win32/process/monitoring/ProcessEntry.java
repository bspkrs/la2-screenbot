/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.process.monitoring;

import com.jniwrapper.LongInt;
import com.jniwrapper.Parameter;
import com.jniwrapper.Str;
import com.jniwrapper.UInt32;
import com.jniwrapper.win32.IntPtr;

/**
 * ProcessEntry class represents PROCESSENTRY32 structure.
 * 
 * @author Serge Piletsky
 */
public class ProcessEntry extends PerformanceEntry
{
    private static final int MAX_PATH = 260;

    private UInt32 _size = new UInt32();
    private UInt32 _usage = new UInt32();
    private UInt32 _processID = new UInt32();
    private IntPtr _defaultHeapID = new IntPtr();
    private UInt32 _moduleID = new UInt32();
    private UInt32 _threadsCount = new UInt32();
    private UInt32 _parentProcessID = new UInt32();
    private LongInt _basePriorityClass = new LongInt();
    private UInt32 _flags = new UInt32();
    private Str _exeFile = new Str(MAX_PATH);

    public ProcessEntry()
    {
        super();
        init(new Parameter[]{
                _size,
                _usage,
                _processID,
                _defaultHeapID,
                _moduleID,
                _threadsCount,
                _parentProcessID,
                _basePriorityClass,
                _flags,
                _exeFile
        }, (short)8);
        _size.setValue(getLength());
    }

    /**
     * The number of references to the process.
     * 
     * @return Number of references to the process.
     */
    public long getUsageCounter()
    {
        return _usage.getValue();
    }

    /**
     * Identifier of the process.
     * 
     * @return Identifier of the process.
     */
    public long getProcessID()
    {
        return _processID.getValue();
    }

    /**
     * Identifier of the default heap for the process.
     * 
     * @return Identifier of the default heap for the process.
     */
    public long getDefaultHeapID()
    {
        return _defaultHeapID.getValue();
    }

    /**
     * Module identifier of the process.
     * 
     * @return Module identifier of the process.
     */
    public long getModuleID()
    {
        return _moduleID.getValue();
    }

    /**
     * The number of execution threads started by the process.
     * 
     * @return The number of execution threads started by the process.
     */
    public long getThreadsCount()
    {
        return _threadsCount.getValue();
    }

    /**
     * Identifier of the process that created the process being examined.
     * 
     * @return Identifier of the process that created the process being
     * examined.
     */
    public long getParentProcessID()
    {
        return _parentProcessID.getValue();
    }

    /**
     * Base priority of any threads created by this process.
     * 
     * @return Base priority of any threads created by this process.
     */
    public long getBasePriorityClass()
    {
        return _basePriorityClass.getValue();
    }

    /**
     * String that specifies the name of the executable file for the process.
     * 
     * @return String that specifies the name of the executable file for the
     * process.
     */
    public String getExeFile()
    {
        return _exeFile.getValue();
    }
}

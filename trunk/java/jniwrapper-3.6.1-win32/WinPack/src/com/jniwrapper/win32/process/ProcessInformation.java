/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.process;

import com.jniwrapper.Parameter;
import com.jniwrapper.Structure;
import com.jniwrapper.UInt32;
import com.jniwrapper.win32.Handle;

/**
 * ProcessInformation class represents PROCESS_INFORMATION structure.
 * 
 * @author Serge Piletsky
 */
class ProcessInformation extends Structure
{
    private Process _process = new Process();
    private Handle _thread = new Handle();
    private UInt32 _processID = new UInt32();
    private UInt32 _threadID = new UInt32();

    public ProcessInformation()
    {
        init(new Parameter[]{_process, _thread, _processID, _threadID});
    }

    public ProcessInformation(ProcessInformation that)
    {
        this();
        initFrom(that);
    }

    public Process getProcess()
    {
        return _process;
    }

    /**
     * Handle to the primary thread of the newly created process.
     */
    public Handle getThread()
    {
        return _thread;
    }

    /**
     * A process identifier.
     */
    public long getProcessID()
    {
        return _processID.getValue();
    }


    /**
     * A thread identifier.
     */
    public long getThreadID()
    {
        return _threadID.getValue();
    }

    public Object clone()
    {
        return new ProcessInformation(this);
    }
}

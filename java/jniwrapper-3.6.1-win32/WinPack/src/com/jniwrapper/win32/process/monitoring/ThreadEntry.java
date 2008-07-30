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
import com.jniwrapper.UInt32;

/**
 * ThreadEntry class represents THREADENTRY32 structure.
 * 
 * @author Serge Piletsky
 */
public class ThreadEntry extends PerformanceEntry
{
    private UInt32 _size = new UInt32();
    private UInt32 _usage = new UInt32();
    private UInt32 _threadID = new UInt32();
    private UInt32 _ownerProcessID = new UInt32();
    private LongInt _basePriority = new LongInt();
    private LongInt _deltaPrioriry = new LongInt();
    private UInt32 _flags = new UInt32();

    public ThreadEntry()
    {
        super();
        init(new Parameter[]{_size, _usage, _threadID, _ownerProcessID, _basePriority, _deltaPrioriry, _flags});
        _size.setValue(getLength());
    }

    /**
     * Returns number of references to the thread.
     * 
     * @return Returns number of references to the thread.
     */
    public long getUsage()
    {
        return _usage.getValue();
    }

    /**
     * Returns thread identifier.
     * 
     * @return thread identifier.
     */
    public long getThreadID()
    {
        return _threadID.getValue();
    }

    /**
     * Returns identifier of the process that created the thread.
     * 
     * @return identifier of the process that created the thread.
     */
    public long getOwnerProcessID()
    {
        return _ownerProcessID.getValue();
    }

    /**
     * Returns initial priority level assigned to a thread.
     * 
     * @return initial priority level assigned to a thread.
     */
    public long getBasePriority()
    {
        return _basePriority.getValue();
    }

    /**
     * Returns change in the priority level of a thread.
     * 
     * @return change in the priority level of a thread.
     */
    public long getDeltaPrioriry()
    {
        return _deltaPrioriry.getValue();
    }
}

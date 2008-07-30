/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.process;

import com.jniwrapper.Int64;
import com.jniwrapper.Parameter;
import com.jniwrapper.Structure;
import com.jniwrapper.UInt64;

/**
 * The IOCounters class represents the IO_COUNTERS structure. It contains I/O accounting
 * information for a process or a job object.
 * 
 * @author Serge Piletsky
 */
public class IOCounters extends Structure
{
    private UInt64 _readOperationCount = new UInt64();
    private UInt64 _writeOperationCount = new UInt64();
    private UInt64 _otherOperationCount = new UInt64();
    private UInt64 _readTransferCount = new UInt64();
    private UInt64 _writeTransferCount = new UInt64();
    private UInt64 _otherTransferCount = new UInt64();

    public IOCounters()
    {
        init(new Parameter[]
        {
            _readOperationCount,
            _writeOperationCount,
            _otherOperationCount,
            _readTransferCount,
            _writeTransferCount,
            _otherTransferCount
        });
    }

    public IOCounters(IOCounters that)
    {
        this();
        initFrom(that);
    }

    /**
     * Returns the number of read operations performed.
     * 
     * @return the number of read operations performed.
     */
    public long getReadOperationCount()
    {
        return _readOperationCount.getValue();
    }

    /**
     * Returns the number of write operations performed.
     * 
     * @return the number of write operations performed.
     */
    public long getWriteOperationCount()
    {
        return _writeOperationCount.getValue();
    }

    /**
     * Returns the number of I/O operations performed, other than read and write
     * operations.
     * 
     * @return number of I/O operations performed, other than read and write
     * operations.
     */
    public long getOtherOperationCount()
    {
        return _otherOperationCount.getValue();
    }

    /**
     * Returns the number of bytes read.
     * 
     * @return the number of bytes read.
     */
    public long getReadTransferCount()
    {
        return _readTransferCount.getValue();
    }

    /**
     * Returns the number of bytes written.
     * 
     * @return the number of bytes written.
     */
    public long getWriteTransferCount()
    {
        return _writeTransferCount.getValue();
    }

    /**
     * Returns the number of bytes transferred during operations other than read
     * and write operations.
     * 
     * @return the number of bytes transferred during operations other than read and
     * write operations.
     */
    public long getOtherTransferCount()
    {
        return _otherTransferCount.getValue();
    }

    public Object clone()
    {
        return new IOCounters(this);
    }
}

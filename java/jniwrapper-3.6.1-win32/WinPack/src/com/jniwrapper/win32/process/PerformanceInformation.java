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
import com.jniwrapper.ULongInt;
import com.jniwrapper.win32.SizeT;

/**
 * This class is a wrapper for the <code>PERFORMANCE_INFORMATION</code> native structure and contains performance information.
 *
 * @author Serge Piletsky
 */
public class PerformanceInformation extends Structure
{
    private UInt32 _cb = new UInt32();
    private SizeT _commitTotal = new SizeT();
    private SizeT _commitLimit = new SizeT();
    private SizeT _commitPeak = new SizeT();
    private SizeT _physicalTotalp = new SizeT();
    private SizeT _physicalAvailable = new SizeT();
    private SizeT _systemCache = new SizeT();
    private SizeT _kernelTotal = new SizeT();
    private SizeT _kernelPaged = new SizeT();
    private SizeT _kernelNonpaged = new SizeT();
    private SizeT _pageSize = new SizeT();
    private UInt32 _handleCount = new UInt32();
    private UInt32 _processCount = new UInt32();
    private UInt32 _threadCount = new UInt32();

    public PerformanceInformation()
    {
        init(new Parameter[]
        {
            _cb,
            _commitTotal,
            _commitLimit,
            _commitPeak,
            _physicalTotalp,
            _physicalAvailable,
            _systemCache,
            _kernelTotal,
            _kernelPaged,
            _kernelNonpaged,
            _pageSize,
            _handleCount,
            _processCount,
            _threadCount
        }, (short)8);
        _cb.setValue(getLength());
    }

    public PerformanceInformation(PerformanceInformation that)
    {
        this();
        initFrom(that);
    }

    /**
     * Returns total number of pages committed by the system.
     *
     * @return total number of pages committed by the system.
     */
    public long getCommitTotal()
    {
        return _commitTotal.getValue();
    }

    /**
     * Returns current maximum number of page commits that can be performed by the system.
     *
     * @return current maximum number of page commits that can be performed by the system.
     */
    public long getCommitLimit()
    {
        return _commitLimit.getValue();
    }

    /**
     * Returns maximum number of page commit totals that have occurred since the last reboot.
     *
     * @return maximum number of page commit totals that have occurred since the last reboot.
     */
    public long getCommitPeak()
    {
        return _commitPeak.getValue();
    }

    /**
     * Returns total amount of physical memory, in pages.
     *
     * @return total amount of physical memory, in pages.
     */
    public long getPhysicalTotalp()
    {
        return _physicalTotalp.getValue();
    }

    /**
     * Returns amount of physical memory available to user processes, in pages.
     *
     * @return amount of physical memory available to user processes, in pages.
     */
    public long getPhysicalAvailable()
    {
        return _physicalAvailable.getValue();
    }

    /**
     * Returns total amount of system cache memory, in pages.
     *
     * @return total amount of system cache memory, in pages.
     */
    public long getSystemCache()
    {
        return _systemCache.getValue();
    }

    /**
     * Returns total amount of the sum of the paged and nonpaged kernel pools, in pages.
     *
     * @return total amount of the sum of the paged and nonpaged kernel pools, in pages.
     */
    public long getKernelTotal()
    {
        return _kernelTotal.getValue();
    }

    /**
     * Returns total amount of the paged kernel pool, in pages.
     *
     * @return total amount of the paged kernel pool, in pages.
     */
    public long getKernelPaged()
    {
        return _kernelPaged.getValue();
    }

    /**
     * Returns total amount of the nonpaged kernel pool, in pages.
     *
     * @return total amount of the nonpaged kernel pool, in pages.
     */
    public long getKernelNonpaged()
    {
        return _kernelNonpaged.getValue();
    }

    /**
     * Returns size of a page, in bytes.
     *
     * @return size of a page, in bytes.
     */
    public long getPageSize()
    {
        return _pageSize.getValue();
    }

    /**
     * Returns total number of open handles.
     *
     * @return total number of open handles.
     */
    public long getHandleCount()
    {
        return _handleCount.getValue();
    }

    /**
     * Returns total number of processes.
     *
     * @return total number of processes.
     */
    public long getProcessCount()
    {
        return _processCount.getValue();
    }

    /**
     * Returns total number of threads.
     *
     * @return total number of threads.
     */
    public long getThreadCount()
    {
        return _threadCount.getValue();
    }

    public Object clone()
    {
        return new PerformanceInformation(this);
    }
}

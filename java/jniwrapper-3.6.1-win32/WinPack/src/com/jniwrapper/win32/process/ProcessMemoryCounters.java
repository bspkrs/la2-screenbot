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
import com.jniwrapper.win32.SizeT;

/**
 * This class is a wrapper for <code>PROCESS_MEMORY_COUNTERS</code> native structure and contains various memory statistics.
 *
 * @author Serge Piletsky
 */
public class ProcessMemoryCounters extends Structure
{
    private UInt32 _cb = new UInt32();
    private UInt32 _pageFaultCount = new UInt32();
    private SizeT _peakWorkingSetSize = new SizeT();
    private SizeT _workingSetSize = new SizeT();
    private SizeT _quotaPeakPagedPoolUsage = new SizeT();
    private SizeT _quotaPagedPoolUsage = new SizeT();
    private SizeT _quotaPeakNonPagedPoolUsage = new SizeT();
    private SizeT _quotaNonPagedPoolUsage = new SizeT();
    private SizeT _pagefileUsage = new SizeT();
    private SizeT _peakPagefileUsage = new SizeT();

    public ProcessMemoryCounters()
    {
        init(new Parameter[]
        {
            _cb,
            _pageFaultCount,
            _peakWorkingSetSize,
            _workingSetSize,
            _quotaPeakPagedPoolUsage,
            _quotaPagedPoolUsage,
            _quotaPeakNonPagedPoolUsage,
            _quotaNonPagedPoolUsage,
            _pagefileUsage,
            _peakPagefileUsage
        });
        _cb.setValue(getLength());
    }

    public ProcessMemoryCounters(ProcessMemoryCounters that)
    {
        this();
        initFrom(that);
    }

    /**
     * Returns a number of page faults.
     *
     * @return a number of page faults.
     */
    public long getPageFaultCount()
    {
        return _pageFaultCount.getValue();
    }

    /**
     * Returns peak working set size.
     *
     * @return peak working set size.
     */
    public long getPeakWorkingSetSize()
    {
        return _peakWorkingSetSize.getValue();
    }

    /**
     * Returns current working set size.
     *
     * @return Current working set size.
     */
    public long getWorkingSetSize()
    {
        return _workingSetSize.getValue();
    }

    /**
     * Returns peak paged pool usage.
     *
     * @return peak paged pool usage.
     */
    public long getQuotaPeakPagedPoolUsage()
    {
        return _quotaPeakPagedPoolUsage.getValue();
    }

    /**
     * Returns current paged pool usage.
     *
     * @return current paged pool usage.
     */
    public long getQuotaPagedPoolUsage()
    {
        return _quotaPagedPoolUsage.getValue();
    }

    /**
     * Returns peak nonpaged pool usage.
     *
     * @return peak nonpaged pool usage.
     */
    public long getQuotaPeakNonPagedPoolUsage()
    {
        return _quotaPeakNonPagedPoolUsage.getValue();
    }

    /**
     * Returns current nonpaged pool usage.
     *
     * @return current nonpaged pool usage.
     */
    public long getQuotaNonPagedPoolUsage()
    {
        return _quotaNonPagedPoolUsage.getValue();
    }

    /**
     * Returns current space allocated for the pagefile.
     *
     * @return current space allocated for the pagefile.
     */
    public long getPagefileUsage()
    {
        return _pagefileUsage.getValue();
    }

    /**
     * Returns peak space allocated for the pagefile.
     *
     * @return peak space allocated for the pagefile.
     */
    public long getPeakPagefileUsage()
    {
        return _peakPagefileUsage.getValue();
    }

    public Object clone()
    {
        return new ProcessMemoryCounters(this);
    }
}

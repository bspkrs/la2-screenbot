/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.system;

import com.jniwrapper.*;
import com.jniwrapper.win32.SizeT;

/**
 * This class represents MEMORYSTATUS structure.
 * 
 * @author Serge Piletsky
 */
public class MemoryStatus extends Structure
{
    static final String FUNCTION_GET_MEMORY_STATUS = "GlobalMemoryStatus";

    private UInt32 _length = new UInt32();
    private UInt32 _memoryLoad = new UInt32();
    private SizeT _totalPhys = new SizeT();
    private SizeT _availPhys = new SizeT();
    private SizeT _totalPageFile = new SizeT();
    private SizeT _availPageFile = new SizeT();
    private SizeT _totalVirtual = new SizeT();
    private SizeT _availVirtual = new SizeT();

    /**
     * Creates an instance with information on the current memory state.
     */
    public MemoryStatus()
    {
        init(new Parameter[]
        {
            _length,
            _memoryLoad,
            _totalPhys,
            _availPhys,
            _totalPageFile,
            _availPageFile,
            _totalVirtual,
            _availVirtual
        });

        Function function = Kernel32.getInstance().getFunction(FUNCTION_GET_MEMORY_STATUS);
        function.invoke(null, new Pointer.OutOnly(this));
    }

    public MemoryStatus(MemoryStatus that)
    {
        this();
        initFrom(that);
    }

    public long getMemoryLoad()
    {
        return _memoryLoad.getValue();
    }

    public long getTotalPhys()
    {
        return _totalPhys.getValue();
    }

    public long getAvailPhys()
    {
        return _availPhys.getValue();
    }

    public long getTotalPageFile()
    {
        return _totalPageFile.getValue();
    }

    public long getAvailPageFile()
    {
        return _availPageFile.getValue();
    }

    public long getTotalVirtual()
    {
        return _totalVirtual.getValue();
    }

    public long getAvailVirtual()
    {
        return _availVirtual.getValue();
    }

    public Object clone()
    {
        return new MemoryStatus(this);
    }
}

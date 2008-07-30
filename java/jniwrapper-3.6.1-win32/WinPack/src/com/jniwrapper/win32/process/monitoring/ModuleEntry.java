/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.process.monitoring;

import com.jniwrapper.Parameter;
import com.jniwrapper.Str;
import com.jniwrapper.UInt32;
import com.jniwrapper.win32.Handle;

/**
 * ModuleEntry class represents MODULEENTRY32 structure.
 * 
 * @author Serge Piletsky
 */
public class ModuleEntry extends PerformanceEntry
{
    private static final int MAX_MODULE_NAME32 = 255;
    private static final int MAX_PATH = 260;

    private UInt32 _size = new UInt32();
    private UInt32 _moduleID = new UInt32();
    private UInt32 _processID = new UInt32();
    private UInt32 _glblCntUsage = new UInt32();
    private UInt32 _procCntUsage = new UInt32();
    private Handle _modBaseAddr = new Handle();
    private UInt32 _modBaseSize = new UInt32();
    private Handle _module = new Handle();
    private Str _moduleName = new Str(MAX_MODULE_NAME32 + 1);
    private Str _exePath = new Str(MAX_PATH);

    public ModuleEntry()
    {
        super();
        init(new Parameter[]
        {
            _size,
            _moduleID,
            _processID,
            _glblCntUsage,
            _procCntUsage,
            _modBaseAddr,
            _modBaseSize,
            _module,
            _moduleName,
            _exePath
        }, (short)8);
        _size.setValue(getLength());
    }

    /**
     * Module identifier in the context of the owning process.
     * 
     * @return Module identifier in the context of the owning process.
     */
    public long getModuleID()
    {
        return _moduleID.getValue();
    }

    /**
     * Identifier of the process to be examined.
     * 
     * @return Identifier of the process to be examined.
     */
    public long getProcessID()
    {
        return _processID.getValue();
    }

    /**
     * Global usage count on the module.
     * 
     * @return Global usage count on the module.
     */
    public long getGlobalUsageCount()
    {
        return _glblCntUsage.getValue();
    }

    /**
     * Module usage count in the context of the owning process.
     * 
     * @return Module usage count in the context of the owning process.
     */
    public long getProcessUsageCount()
    {
        return _procCntUsage.getValue();
    }

    /**
     * Base address of the module in the context of the owning process.
     * 
     * @return Base address of the module in the context of the owning process.
     */
    public Handle getModBaseAddr()
    {
        return _modBaseAddr;
    }

    /**
     * Size of the module, in bytes.
     * 
     * @return Size of the module, in bytes.
     */
    public long getModBaseSize()
    {
        return _modBaseSize.getValue();
    }

    /**
     * Handle to the module in the context of the owning process.
     * 
     * @return Handle to the module in the context of the owning process.
     */
    public Handle getModule()
    {
        return _module;
    }

    /**
     * String that specifies the module name.
     * 
     * @return String that specifies the module name.
     */
    public String getModuleName()
    {
        return _moduleName.getValue();
    }

    /**
     * String that specifies the module path.
     * 
     * @return String that specifies the module path.
     */
    public String getExePath()
    {
        return _exePath.getValue();
    }
}

/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.service;

import com.jniwrapper.*;

/**
 * This structure corresponds to <code>QUERY_SERVICE_CONFIG</code> native
 * structure.
 * 
 * @author Alexei Orischenko
 */
class ServiceConfig extends Structure
{
    private UInt32 _serviceType = new UInt32();
    private UInt32 _startupType = new UInt32();
    private UInt32 _errorControl = new UInt32();
    private ExternalStringPointer _binaryPath = new ExternalStringPointer();
    private Str _loadOrderGroup = new Str();
    private UInt32 _tagID = new UInt32();
    private StringArray _dependencies = new StringArray();
    private Str _serviceStartName = new Str();
    private Str _displayName = new Str();

    ServiceConfig()
    {
        init(new Parameter[]{
            _serviceType, _startupType, _errorControl,
            _binaryPath, new Pointer(_loadOrderGroup), _tagID,
            new Pointer(_dependencies), new Pointer(_serviceStartName), new Pointer(_displayName)
        }, (short)8);
    }

    ServiceConfig(ServiceConfig that)
    {
        this();
        initFrom(that);
    }

    public int getServiceType()
    {
        return (int)_serviceType.getValue();
    }

    public void setServiceType(int serviceType)
    {
        _serviceType.setValue(serviceType);
    }

    public int getStartupType()
    {
        return (int)_startupType.getValue();
    }

    public void setStartupType(int startupType)
    {
        _startupType.setValue(startupType);
    }

    public int getErrorControl()
    {
        return (int)_errorControl.getValue();
    }

    public void setErrorControl(int errorControl)
    {
        _errorControl.setValue(errorControl);
    }

    public String getBinaryPath()
    {
        return _binaryPath.readString();
    }

//    public void setBinaryPath(String binaryPath)
//    {
//        _binaryPath.setValue(binaryPath);
//    }

    public String getLoadOrderGroup()
    {
        return _loadOrderGroup.getValue();
    }

    public void setLoadOrderGroup(String loadOrderGroup)
    {
        _loadOrderGroup.setValue(loadOrderGroup);
    }

    public int getTagID()
    {
        return (int)_tagID.getValue();
    }

    public void setTagID(int tagID)
    {
        _tagID.setValue(tagID);
    }

    public String[] getDependencies()
    {
        return _dependencies.getValue();
    }

    public void setDependencies(String[] dependencies)
    {
        _dependencies.setValue(dependencies);
    }

    public String getServiceStartName()
    {
        return _serviceStartName.getValue();
    }

    public void setServiceStartName(String serviceStartName)
    {
        _serviceStartName.setValue(serviceStartName);
    }

    public String getDisplayName()
    {
        return _displayName.getValue();
    }

    public void setDisplayName(String displayName)
    {
        _displayName.setValue(displayName);
    }

    public Object clone()
    {
        return new ServiceConfig(this);
    }
}

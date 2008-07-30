/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.service;

import com.jniwrapper.Parameter;
import com.jniwrapper.Pointer;
import com.jniwrapper.Str;
import com.jniwrapper.Structure;

/**
 * This structure corresponds to native <code>ENUM_SERVICE_STATUS</code> structure.
 *
 * @author Alexei Orischenko
 */
class EnumServiceStatus extends Structure
{
    private Str _serviceName = new Str();
    private Str _displayName = new Str();
    private Service.Status _serviceStatus = new Service.Status();

    private void init()
    {
        init(new Parameter[]{new Pointer(_serviceName), new Pointer(_displayName), _serviceStatus}, (short)8);
    }

    public EnumServiceStatus(EnumServiceStatus that)
    {
        _serviceName = (Str)that._serviceName.clone();
        _displayName = (Str)that._displayName.clone();
        _serviceStatus = (Service.Status)that._serviceStatus.clone();

        init();
    }

    public EnumServiceStatus()
    {
        init();
    }

    public String getServiceName()
    {
        return _serviceName.getValue();
    }

    public void setServiceName(String serviceName)
    {
        _serviceName.setValue(serviceName);
    }

    public String getDisplayName()
    {
        return _displayName.getValue();
    }

    public void setDisplayName(String displayName)
    {
        _displayName.setValue(displayName);
    }

    public Service.Status getServiceStatus()
    {
        return _serviceStatus;
    }

    public Object clone()
    {
        return new EnumServiceStatus(this);
    }
}

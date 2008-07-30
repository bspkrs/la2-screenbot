/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.ui.dialogs;

import com.jniwrapper.Structure;
import com.jniwrapper.UInt16;
import com.jniwrapper.Parameter;

/**
 * DEVNAMES native structure.
 */
public class DevNames extends Structure
{
    private UInt16 _driverOffset = new UInt16();
    private UInt16 _deviceOffset = new UInt16();
    private UInt16 _outputOffset = new UInt16();
    private UInt16 _default = new UInt16();

    private void init()
    {
        init(new Parameter[] {
           _driverOffset, _deviceOffset, _outputOffset, _default
        });
    }

    public DevNames()
    {
        init();
    }

    public DevNames(DevNames that)
    {
        this();
        initFrom(that);
    }

    public int getDriverOffset()
    {
        return (int) _driverOffset.getValue();
    }

    public void setDriverOffset(int driverOffset)
    {
        _driverOffset.setValue(driverOffset);
    }

    public int getDeviceOffset()
    {
        return (int) _deviceOffset.getValue();
    }

    public void setDeviceOffset(int deviceOffset)
    {
        _deviceOffset.setValue(deviceOffset);
    }

    public int getOutputOffset()
    {
        return (int) _outputOffset.getValue();
    }

    public void setOutputOffset(int outputOffset)
    {
        _outputOffset.setValue(outputOffset);
    }

    public int getDefault()
    {
        return (int)_default.getValue();
    }

    public void setDefault(int aDefault)
    {
        _default.setValue(aDefault);
    }

    public Object clone()
    {
        return new DevNames(this);
    }
}

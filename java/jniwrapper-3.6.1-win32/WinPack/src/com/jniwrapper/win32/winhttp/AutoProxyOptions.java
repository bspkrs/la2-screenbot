/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.winhttp;

import com.jniwrapper.*;

/**
 * Represents WINHTTP_AUTOPROXY_OPTIONS native structure.
 */
class AutoProxyOptions extends Structure
{
    private UInt32 _flags = new UInt32();
    private UInt32 _autoDetectFlags = new UInt32();
    private WideString _autoConfigUrl = new WideString();
    private Pointer _pAutoConfigUrl = new Pointer(_autoConfigUrl);
    private Pointer.Void _lpvReserved = new Pointer.Void();
    private UInt32 _dwReserved = new UInt32();
    private IntBool _autoLogon = new IntBool();

    public AutoProxyOptions()
    {
        init(new Parameter[]{
            _flags,
            _autoDetectFlags,
            _pAutoConfigUrl,
            _lpvReserved,
            _dwReserved,
            _autoLogon
        });
    }

    public AutoProxyOptions(AutoProxyOptions that)
    {
        this();
        initFrom(that);
    }

    public long getFlags()
    {
        return _flags.getValue();
    }

    public void setFlags(long flags)
    {
        _flags.setValue(flags);
    }

    public long getAutoDetectFlags()
    {
        return _autoDetectFlags.getValue();
    }

    public void setAutoDetectFlags(long autoDetectFlags)
    {
        _autoDetectFlags.setValue(autoDetectFlags);
    }

    public String getAutoConfigUrl()
    {
        return _autoConfigUrl.getValue();
    }

    public void setAutoConfigUrl(String autoConfigUrl)
    {
        if (autoConfigUrl != null)
        {
            _autoConfigUrl.setValue(autoConfigUrl);
            _pAutoConfigUrl.setReferencedObject(_autoConfigUrl);
        }
        else
        {
            _pAutoConfigUrl.setNull(true);
        }
    }

    public boolean isAutoLogon()
    {
        return _autoLogon.getBooleanValue();
    }

    public void setAutoLogon(boolean autoLogon)
    {
        _autoLogon.setBooleanValue(autoLogon);
    }

    public Object clone()
    {
        return new AutoProxyOptions(this);
    }
}

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
import com.jniwrapper.win32.Handle;

/**
 * Represents WINHTTP_CURRENT_USER_IE_PROXY_CONFIG native structure.
 */
class IEProxyConfig extends Structure
{
    private IntBool _autoDetect = new IntBool();
    private Handle _autoConfigUrl = new Handle();
    private Handle _proxy = new Handle();
    private Handle _proxyBypass = new Handle();

    public IEProxyConfig()
    {
        init(new Parameter[]{
            _autoDetect,
            _autoConfigUrl,
            _proxy,
            _proxyBypass
        });
    }

    public IEProxyConfig(IEProxyConfig that)
    {
        this();
        initFrom(that);
    }

    public boolean isAutoDetect()
    {
        return _autoDetect.getBooleanValue();
    }

    public void setAutoDetect(boolean autoDetect)
    {
        _autoDetect.setBooleanValue(autoDetect);
    }

    public String getAutoConfigUrl()
    {
        return getValue(_autoConfigUrl);
    }

    public void setAutoConfigUrl(String autoConfigUrl)
    {
        setValue(autoConfigUrl, _autoConfigUrl);
    }

    public String getProxy()
    {
        return getValue(_proxy);
    }

    public void setProxy(String proxy)
    {
        setValue(proxy, _proxy);
    }

    public String getProxyBypass()
    {
        return getValue(_proxyBypass);
    }

    public void setProxyBypass(String proxyBypass)
    {
        setValue(proxyBypass, _proxyBypass);
    }

    private void setValue(String value, Handle handle)
    {
        if (value == null)
        {
            handle.setValue(0);
        }
        else
        {
            Pointer proxyBypassPtr = new Pointer(new WideString(value));
            proxyBypassPtr.castTo(_proxyBypass);
            proxyBypassPtr.setReferencedObject(null, true);
        }
    }

    private String getValue(Handle handle)
    {
        if (handle.isNull())
        {
            return "";
        }
        else
        {
            ExternalStringPointer esp = new ExternalStringPointer(handle);
            return esp.readString();
        }
    }

    public Object clone()
    {
        return new IEProxyConfig(this);
    }
}

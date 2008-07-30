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
 * Represents WINHTTP_PROXY_INFO native structure.
 */
class ProxyInfo extends Structure
{
    private UInt32 _accessType = new UInt32();
    private WideString _proxy = new WideString();
    private Pointer _pProxy = new Pointer(_proxy);
    private WideString _proxyBypass = new WideString();
    private Pointer _pProxyBypass = new Pointer(_proxyBypass);

    public ProxyInfo()
    {
        init(new Parameter[]{
            _accessType,
            _pProxy,
            _pProxyBypass
        });
    }

    public ProxyInfo(ProxyInfo that)
    {
        this();
        initFrom(that);
    }

    public long getAccessType()
    {
        return _accessType.getValue();
    }

    public void setAccessType(long accessType)
    {
        _accessType.setValue(accessType);
    }

    public String getProxy()
    {
        return _proxy.getValue();
    }

    public void setProxy(String proxy)
    {
        if (proxy == null)
        {
            _pProxy.setNull(true);
        }
        else
        {
            _proxy.setValue(proxy);
            _pProxy.setNull(false);
        }
    }

    public String getProxyBypass()
    {
        return _proxyBypass.getValue();
    }

    public void setProxyBypass(String proxyBypass)
    {
        if (proxyBypass == null)
        {
            _pProxyBypass.setNull(true);
        }
        else
        {
            _proxyBypass.setValue(proxyBypass);
            _pProxyBypass.setNull(false);
        }
    }

    public Object clone()
    {
        return new ProxyInfo(this);
    }
}

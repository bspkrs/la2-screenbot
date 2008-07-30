/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.winhttp;

import com.jniwrapper.Function;
import com.jniwrapper.IntBool;
import com.jniwrapper.Pointer;
import com.jniwrapper.Str;
import com.jniwrapper.win32.winhttp.AutoProxyOptions;
import com.jniwrapper.win32.winhttp.DefaultProxySettings;
import com.jniwrapper.win32.winhttp.ProxyInfo;
import com.jniwrapper.win32.winhttp.ProxySettings;

/**
 * Returns the proxy configuration for the specified URL.
 */
public class RemoteProxySettings implements ProxySettings
{
    private static final String FUNCTION_GET_PROXY_FOR_URL = "WinHttpGetProxyForUrl";

    private static final long WINHTTP_AUTOPROXY_AUTO_DETECT = 0x00000001L;
    private static final long WINHTTP_AUTOPROXY_CONFIG_URL = 0x00000002L;

    private static final long WINHTTP_AUTO_DETECT_TYPE_DHCP = 0x00000001L;
    private static final long WINHTTP_AUTO_DETECT_TYPE_DNS_A = 0x00000002L;

    private static final int WINHTTP_ACCESS_TYPE_NO_PROXY = 1;

    private boolean _isProxySet;
    private int _proxyPort;
    private String _proxyURL;
    private String _proxyBypass;

    /**
     * Returns the proxy configuration for the specified URL.
     *
     * @param url       specifes the URL
     * @param autoLogon if <code>true</code> credentials should automatically be sent
     * @throws WinHttpException
     */
    public RemoteProxySettings(String url, boolean autoLogon) throws WinHttpException
    {
        if (url == null)
        {
            throw new IllegalArgumentException("URL argument is null.");
        }
        AutoProxyOptions options = new AutoProxyOptions();
        options.setFlags(WINHTTP_AUTOPROXY_AUTO_DETECT);
        options.setAutoDetectFlags(WINHTTP_AUTO_DETECT_TYPE_DNS_A | WINHTTP_AUTO_DETECT_TYPE_DHCP);
        options.setAutoLogon(autoLogon);
        options.setAutoConfigUrl(null);

        readInfo(url, options);
    }

    /**
     * Returns the proxy configuration for the specified URL.
     *
     * @param url        specifes the URL
     * @param pacFileUrl specifies the URL of the PAC file
     * @param autoLogon  if <code>true</code> credentials should automatically be sent
     * @throws WinHttpException
     */
    public RemoteProxySettings(String url, String pacFileUrl, boolean autoLogon) throws WinHttpException
    {
        if (url == null)
        {
            throw new IllegalArgumentException("URL argument is null.");
        }
        if (pacFileUrl == null)
        {
            throw new IllegalArgumentException("pacFileUrl argument is null.");
        }

        AutoProxyOptions options = new AutoProxyOptions();
        options.setFlags(WINHTTP_AUTOPROXY_CONFIG_URL);
        options.setAutoDetectFlags(0);
        options.setAutoLogon(autoLogon);
        options.setAutoConfigUrl(pacFileUrl);

        readInfo(url, options);
    }

    private void readInfo(String url, AutoProxyOptions options) throws WinHttpException
    {
        WinHttpSession session = new WinHttpSession(null, null, null);
        try
        {
            Function function = WinHttpLibrary.getInstance().getFunction(FUNCTION_GET_PROXY_FOR_URL);

            IntBool result = new IntBool();
            ProxyInfo proxyInfo = new ProxyInfo();
            long lastError = function.invoke(result,
                    session,
                    new Pointer(new Str(url)),
                    new Pointer(options),
                    new Pointer.OutOnly(proxyInfo));

            if (result.getValue() == 0)
            {
                throw new WinHttpException(lastError);
            }

            long accessType = proxyInfo.getAccessType();
            if (accessType == WINHTTP_ACCESS_TYPE_NO_PROXY)
            {
                _isProxySet = false;
            }
            else
            {
                _isProxySet = true;
            }
            _proxyBypass = proxyInfo.getProxyBypass();

            String proxy = proxyInfo.getProxy();
            int portOffset = proxy.lastIndexOf(':');
            String port = proxy.substring(portOffset + 1, proxy.length());
            try
            {
                _proxyPort = new Integer(port).intValue();
                _proxyURL = proxy.substring(0, portOffset);
            }
            catch (NumberFormatException e)
            {
                _proxyPort = -1;
                _proxyURL = proxy;
            }
        }
        finally
        {
            session.close();
        }
    }

    /**
     * Returns <code>true</code> if proxy is set; <code>false</code> otherwise.
     *
     * @return true if proxy is set; false otherwise
     */
    public boolean isSet()
    {
        return _isProxySet;
    }

    /**
     * Returns the proxy URL.
     *
     * @return the proxy URL
     */
    public String getURL()
    {
        return _proxyURL;
    }

    /**
     * Returns the proxy port.
     *
     * @return the proxy port
     */
    public int getPort()
    {
        return _proxyPort;
    }

    /**
     * Returns the proxy bypass.
     *
     * @return the proxy bypass
     */
    public String[] getProxyBypass()
    {
        return DefaultProxySettings.parseBypasses(_proxyBypass);
    }

}

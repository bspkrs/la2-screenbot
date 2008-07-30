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

import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Provides information about default  proxy configuration from the registry.
 */
public class DefaultProxySettings implements ProxySettings
{
    private static final String FUNCTION_GET_DEFAULT_PROXY_CONFIGURATION = "WinHttpGetDefaultProxyConfiguration";
    private static final int WINHTTP_ACCESS_TYPE_NO_PROXY = 1;

    private boolean _isProxySet;
    private int _proxyPort;
    private String _proxyURL;
    private String _proxyBypass;

    /**
     * Creates an instance of default proxy settings.
     *
     * @throws WinHttpException
     */
    public DefaultProxySettings() throws WinHttpException
    {
        Function function = WinHttpLibrary.getInstance().getFunction(FUNCTION_GET_DEFAULT_PROXY_CONFIGURATION);

        ProxyInfo proxyInfo = new ProxyInfo();
        IntBool result = new IntBool();
        long errorCode = function.invoke(result, new Pointer(proxyInfo));
        if (result.getValue() == 0)
        {
            throw new WinHttpException(errorCode);
        }
        long accessType = proxyInfo.getAccessType();
        _isProxySet = (accessType != WINHTTP_ACCESS_TYPE_NO_PROXY);
        _proxyBypass = proxyInfo.getProxyBypass();

        String proxy = proxyInfo.getProxy();
        int portOffset = proxy.lastIndexOf(':');
        String port = proxy.substring(portOffset + 1, proxy.length());
        try
        {
            _proxyPort = Integer.parseInt(port);
            _proxyURL = proxy.substring(0, portOffset);
        }
        catch (NumberFormatException e)
        {
            _proxyPort = -1;
            _proxyURL = proxy;
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
        return parseBypasses(_proxyBypass);
    }

    static String[] parseBypasses(String bypass)
    {
        List tokens = new LinkedList();
        StringTokenizer tokenizer = new StringTokenizer(bypass, "; ");
        while (tokenizer.hasMoreTokens())
        {
            tokens.add(tokenizer.nextToken());
        }
        String result[] = new String[tokens.size()];
        tokens.toArray(result);

        return result;
    }
}

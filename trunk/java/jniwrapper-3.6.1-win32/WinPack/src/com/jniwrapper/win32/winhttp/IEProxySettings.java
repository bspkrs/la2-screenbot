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

import java.util.*;
import java.net.URL;
import java.net.MalformedURLException;

/**
 * Provides information about proxy settings of Internet Explorer.
 */
public class IEProxySettings implements ProxySettings {
    private static final String FUNCTION_GET_IE_PROXY_CONFIGURATION = "WinHttpGetIEProxyConfigForCurrentUser";
    private static final String HTTP = "http";
    private static final int UNSPECIFIED = -1;

    private String _proxyURL;
    private int _proxyPort = UNSPECIFIED;
    private boolean _isAutoDetect;
    private String _proxyBypass;
    private String _autoConfigURL;
    private List serverInfos;

    /**
     * This class contains information about sever protocol, proxy addres and port.
     */
    public static class ServerInfo {
        private String protocol;
        private String proxyAddress;
        private int proxyPort = UNSPECIFIED;

        ServerInfo(String proxyInfo) {
            parseInfo(proxyInfo);
        }

        private void parseInfo(String proxyInfo) {
            // Get protocol first
            int protocolPrefixIndex = proxyInfo.indexOf('=');
            if (protocolPrefixIndex != -1) {
                protocol = proxyInfo.substring(0, protocolPrefixIndex);
                proxyInfo = proxyInfo.substring(protocolPrefixIndex + 1);
            } else {
                protocol = HTTP;
            }
            // Then try to parse proxy info using URL
            try {
                URL serverURL = new URL(proxyInfo);
                proxyAddress = serverURL.getProtocol() + "://" + serverURL.getHost();
                proxyPort = serverURL.getPort();
            } catch (MalformedURLException e) {
                // Otherwise just parse with StringTokenizer
                StringTokenizer infoTokenizer = new StringTokenizer(proxyInfo, ":");
                proxyAddress = infoTokenizer.nextToken();
                if (infoTokenizer.hasMoreTokens()) {
                    String port = infoTokenizer.nextToken();
                    proxyPort = Integer.parseInt(port);
                }
            }
        }

        ServerInfo(String serverType, String proxyAddress, int proxyPort) {
            this.protocol = serverType;
            this.proxyAddress = proxyAddress;
            this.proxyPort = proxyPort;
        }

        public String getProtocol() {
            return protocol;
        }

        public String getProxyAddress() {
            return proxyAddress;
        }

        public int getProxyPort() {
            return proxyPort;
        }

        public String toString() {
            StringBuffer result = new StringBuffer("ServerInfo[");
            result.append("protocol=").append(protocol);
            result.append(";proxyAddress=").append(proxyAddress);
            result.append(";port=").append(proxyPort);
            result.append("]");
            return result.toString();
        }
    }

    /**
     * Creates a new instance of Internet Explorer proxy settings.
     *
     * @throws WinHttpException
     */
    public IEProxySettings() throws WinHttpException {
        Function function = WinHttpLibrary.getInstance().getFunction(FUNCTION_GET_IE_PROXY_CONFIGURATION);

        IEProxyConfig ieProxyInfo = new IEProxyConfig();
        IntBool result = new IntBool();
        long errorCode = function.invoke(result, new Pointer(ieProxyInfo));
        if (result.getValue() == 0) {
            throw new WinHttpException(errorCode);
        }

        _isAutoDetect = ieProxyInfo.isAutoDetect();
        _autoConfigURL = ieProxyInfo.getAutoConfigUrl();
        _proxyBypass = ieProxyInfo.getProxyBypass();

        String proxyInfo = ieProxyInfo.getProxy();
        serverInfos = new LinkedList();
        StringTokenizer tokenizer = new StringTokenizer(proxyInfo, ";");
        while (tokenizer.hasMoreTokens()) {
            ServerInfo info = new ServerInfo(tokenizer.nextToken());
            serverInfos.add(info);
        }

        ServerInfo defaultServerInfo = null;
        for (int i = 0; i < serverInfos.size(); i++) {
            ServerInfo serverInfo = (ServerInfo) serverInfos.get(i);
            if (HTTP.equalsIgnoreCase(serverInfo.getProtocol())) {
                defaultServerInfo = serverInfo;
            }
        }
        if (defaultServerInfo != null) {
            _proxyURL = defaultServerInfo.getProxyAddress();
            _proxyPort = defaultServerInfo.getProxyPort();
        }
    }

    /**
     * Returns <code>true</code> if autodetection is on; <code>false</code> otherwise.
     *
     * @return <code>true</code> if autodetection is on; <code>false</code> otherwise.
     */
    public boolean isAutoDetect() {
        return _isAutoDetect;
    }

    /**
     * Returns the Auto Config URL.
     *
     * @return the Auto Config URL
     */
    public String getAutoConfigURL() {
        return _autoConfigURL;
    }

    /**
     * Returns <code>true</code> if proxy is set; <code>false</code> othewise.
     *
     * @return <code>true</code> if proxy is set; <code>false</code> othewise
     */
    public boolean isSet() {
        return _proxyPort != UNSPECIFIED;
    }

    /**
     * Returns the proxy port.
     *
     * @return the proxy port
     */
    public int getPort() {
        return _proxyPort;
    }

    /**
     * Returns the proxy URL.
     *
     * @return the proxy URL
     */
    public String getURL() {
        return _proxyURL;
    }

    /**
     * Returns the proxy bypass.
     *
     * @return the proxy bypass
     */
    public String[] getProxyBypass() {
        return DefaultProxySettings.parseBypasses(_proxyBypass);
    }

    /**
     * Returns the list of {@link ServerInfo} objects.
     *
     * @return the list of {@link ServerInfo} objects
     */
    public List getServers() {
        return Collections.unmodifiableList(serverInfos);
    }
}

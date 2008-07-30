/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.winhttp;

/**
 * A generic interface for getting proxy settings.
 */
public interface ProxySettings
{
    /**
     * Returns <code>true</code> if proxy is set; <code>false</code> otherwise.
     *
     * @return true if proxy is set; false otherwise
     */
    public boolean isSet();

    /**
     * Returns the proxy URL.
     *
     * @return the proxy URL
     */
    public String getURL();

    /**
     * Returns the proxy port.
     *
     * @return the proxy port
     */
    public int getPort();

    /**
     * Returns the proxy bypass.
     *
     * @return the proxy bypass
     */
    public String[] getProxyBypass();
}
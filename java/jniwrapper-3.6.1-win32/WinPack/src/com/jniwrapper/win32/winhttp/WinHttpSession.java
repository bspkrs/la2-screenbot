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
import com.jniwrapper.win32.winhttp.WinHttpException;
import com.jniwrapper.win32.winhttp.WinHttpLibrary;

/**
 * This class provides functionality for getting system default proxy settings,
 * Internet Explorer proxy settings and settings for the specified URL.
 *
 * @author Vladimir Kondrashchenko
 */
class WinHttpSession extends Handle
{
    private static final String FUNCTION_OPEN = "WinHttpOpen";
    private static final String FUNCTION_CLOSE_HANDLE = "WinHttpCloseHandle";

    private static final int ACCESS_TYPE_DEFAULT_PROXY = 0;
    private static final int ACCESS_TYPE_NAMED_PROXY = 3;

    /**
     * Initializes an application's use of WinHTTP functions. Requests will
     * pass the specified proxy taking into account proxy bypass list.
     *
     * @param userAgent   specifies the name of the application. May be <code>null</code>.
     * @param proxyName   specifies the name of the proxy. May be <code>null</code>.
     * @param proxyBypass specifies the bypass list. May be <code>null</code>.
     * @throws WinHttpException
     */
    public WinHttpSession(String userAgent, String proxyName, String proxyBypass) throws WinHttpException
    {
        Function function = WinHttpLibrary.getInstance().getFunction(FUNCTION_OPEN);

        long errorCode = function.invoke(this, new Parameter[]
        {
            userAgent == null ? new WideString() : new WideString(userAgent),
            proxyName == null ? new UInt32(ACCESS_TYPE_DEFAULT_PROXY) : new UInt32(ACCESS_TYPE_NAMED_PROXY),
            proxyName == null ? new Pointer(null, true) : new Pointer(new WideString(proxyName)),
            proxyBypass == null ? new Pointer(null, true) : new Pointer(new WideString(proxyBypass)),
            new UInt32(0)
        });

        if (getValue() == 0)
        {
            throw new WinHttpException(errorCode);
        }
    }

    /**
     * Initializes an application's use of WinHTTP functions. Requests will
     * pass the specified proxy taking into account proxy bypass list.
     *
     * @throws WinHttpException
     */
    public WinHttpSession() throws WinHttpException
    {
        this(null, null, null);
    }

    /**
     * Closes the handle of WinHTTP session.
     *
     * @throws WinHttpException
     */
    public void close() throws WinHttpException
    {
        Function function = WinHttpLibrary.getInstance().getFunction(FUNCTION_CLOSE_HANDLE);

        IntBool result = new IntBool();
        long errorCode = function.invoke(result, this);
        if (result.getValue() == 0)
        {
            throw new WinHttpException(errorCode);
        }
    }
}

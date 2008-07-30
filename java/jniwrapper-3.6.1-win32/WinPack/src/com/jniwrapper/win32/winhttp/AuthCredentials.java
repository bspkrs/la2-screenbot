/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.winhttp;

import com.jniwrapper.util.EnumItem;

/**
 * This class is designed for determining authorization credentials that are required
 * on remote proxy settings requests. The default authorization target is @link AuthTarget.PROXY and
 * the default authorization scheme is @link AuthScheme.NTLM.
 *
 * @see RemoteProxySettings
 *
 * @author Vladimir Kondrashchenko
 */
public class AuthCredentials
{
    private AuthTarget _authTarget = AuthTarget.PROXY;
    private AuthScheme _authScheme = AuthScheme.NTLM;
    private String _userName;
    private String _password;

    public AuthCredentials(String userName, String password)
    {
        _userName = userName;
        _password = password;
    }

    public AuthScheme getAuthScheme()
    {
        return _authScheme;
    }

    public void setAuthScheme(AuthScheme authScheme)
    {
        _authScheme = authScheme;
    }

    public String getPassword()
    {
        return _password;
    }

    public void setPassword(String password)
    {
        _password = password;
    }

    public String getUserName()
    {
        return _userName;
    }

    public void setUserName(String userName)
    {
        _userName = userName;
    }

    public AuthTarget getAuthTarget()
    {
        return _authTarget;
    }

    public void setAuthTarget(AuthTarget authTarget)
    {
        _authTarget = authTarget;
    }

    /**
     * This class is an enumeration of authorization targets.
     */
    public static class AuthTarget extends EnumItem
    {
        /**
         * Authentication target is a server.
         */
        public static final AuthTarget SERVER = new AuthTarget(0);
        /**
         * Authentication target is a proxy.
         */
        public static final AuthTarget PROXY = new AuthTarget(1);

        private AuthTarget(int value)
        {
            super(value);
        }
    }

    /**
     * This class is an enumeration of authorization schemes.
     */
    public static class AuthScheme extends EnumItem
    {
        /**
         * Indicates basic authentication is first.
         */
        public static final AuthScheme BASIC = new AuthScheme(1);
        /**
         * Indicates NTLM authentication is first.
         */
        public static final AuthScheme NTLM = new AuthScheme(2);
        /**
         * Indicates passport authentication is first.
         */
        public static final AuthScheme PASSPORT = new AuthScheme(4);
        /**
         * Indicates digest authentication is first.
         */
        public static final AuthScheme DIGEST = new AuthScheme(8);
        /**
         * Selects between NTLM and Kerberos authentication.
         */
        public static final AuthScheme NEGOTIATE = new AuthScheme(10);

        private AuthScheme(int value)
        {
            super(value);
        }
    }
}

/*
 * Copyright (c) 1998-2007 TeamDev Ltd. All Rights Reserved.
 * Use is subject to license terms.
 */
package com.jniwrapper.win32.security;

import com.jniwrapper.Parameter;
import com.jniwrapper.Structure;

/**
 * This class is the wrapper for the TOKEN_USER structure.
 * (@see <a href="http://msdn.microsoft.com/library/default.asp?url=/library/en-us/secauthz/security/token_user.asp">
 * TOKEN_USER structure</a>).
 *
 * @author Alexei Razoryonov
 */
public class TokenUser extends Structure
{
    private SidAndAttributes _user = new SidAndAttributes();

    public TokenUser()
    {
        init(new Parameter[]{_user});
    }

    public SidAndAttributes getUser()
    {
        return _user;
    }

    public void setUser(SidAndAttributes user)
    {
        _user = user;
    }
}

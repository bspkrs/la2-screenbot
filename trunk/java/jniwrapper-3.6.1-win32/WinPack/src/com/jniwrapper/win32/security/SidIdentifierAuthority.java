/*
 * Copyright (c) 1998-2007 TeamDev Ltd. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.jniwrapper.win32.security;

import com.jniwrapper.Char;
import com.jniwrapper.Parameter;
import com.jniwrapper.PrimitiveArray;
import com.jniwrapper.Structure;

/**
 * This class is the SID_IDENTIFIER_AUTHORITY structure wrapper.
 * (@see <a href="http://msdn.microsoft.com/library/default.asp?url=/library/en-us/secauthz/security/sid_identifier_authority.asp">
 * SSID_IDENTIFIER_AUTHORITY structure</a>).
 *
 * @author Alexei Razoryonov
 */
public class SidIdentifierAuthority extends Structure
{
    private PrimitiveArray _value = new PrimitiveArray(Char.class, 6);

    public SidIdentifierAuthority()
    {
        init(new Parameter[]{_value});
    }

    public PrimitiveArray getValue()
    {
        return _value;
    }

    public void setValue(PrimitiveArray value)
    {
        _value = value;
    }
}

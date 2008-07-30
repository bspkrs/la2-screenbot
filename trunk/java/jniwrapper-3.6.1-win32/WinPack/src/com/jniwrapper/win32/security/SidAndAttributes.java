/*
 * Copyright (c) 1998-2007 TeamDev Ltd. All Rights Reserved.
 * Use is subject to license terms.
 */
package com.jniwrapper.win32.security;

import com.jniwrapper.Parameter;
import com.jniwrapper.Pointer;
import com.jniwrapper.Structure;
import com.jniwrapper.UInt32;

/**
 * This class is the wrapper for the SID_AND_ATTRIBUTES structure.
 * (@see <a href="http://msdn.microsoft.com/library/default.asp?url=/library/en-us/secauthz/security/sid_and_attributes.asp">
 * SID_AND_ATTRIBUTES structure</a>).
 *
 * @author Alexei Razoryonov
 */

public class SidAndAttributes extends Structure
{
    private Sid _sid = new Sid();
    private Pointer _pSid = new Pointer(_sid); 
    private UInt32 _attributes = new UInt32();

    public SidAndAttributes()
    {
        init(new Parameter[]{_pSid, _attributes});
    }

    public Pointer getpSid()
    {
        return _pSid;
    }

    public void setpSid(Pointer pSid)
    {
        _pSid = pSid;
    }

    public UInt32 getAttributes()
    {
        return _attributes;
    }

    public void setAttributes(UInt32 attributes)
    {
        _attributes = attributes;
    }
}

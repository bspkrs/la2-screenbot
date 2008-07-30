/*
 * Copyright (c) 1998-2007 TeamDev Ltd. All Rights Reserved.
 * Use is subject to license terms.
 */
package com.jniwrapper.win32.security;

import com.jniwrapper.*;
import com.jniwrapper.win32.system.AdvApi32;

/**
 * This class is the wrapper for the SID structure.
 * @see <a href="http://msdn.microsoft.com/library/default.asp?url=/library/en-us/secauthz/security/sid.asp">
 * Security Identifier (SID) structure</a>  
 *
 * @author Alexei Razoryonov
 */

public class Sid extends Structure
{
    private static final String FUNCTION_LOOKUP_ACCOUNT_SID = "LookupAccountSidA";
    private static final String FUNCTION_IS_VALID_SID = "IsValidSid";
    private static final String FUNCTION_GET_LENGTH_SID = "GetLengthSid";
    private static final String FUNCTION_EQUAL_SID = "EqualSid";


    private static final int SUB_AUTHORITY_SIZE = 5;
    private Int8 _revision = new Int8();
    private Int8 _subAuthorityCount = new Int8();
    private SidIdentifierAuthority _identifierAuthority = new SidIdentifierAuthority();
    private Pointer.Void _pSubAuthority = new Pointer.Void();
    private PrimitiveArray _subAuthority = new PrimitiveArray(UInt32.class, SUB_AUTHORITY_SIZE);


    public Sid()
    {
        init(new Parameter[]{_revision, _subAuthorityCount, _identifierAuthority, _subAuthority});
    }

    public Int8 getRevision()
    {
        return _revision;
    }

    public void setRevision(Int8 revision)
    {
        _revision = revision;
    }

    public Int8 getSubAuthorityCount()
    {
        return _subAuthorityCount;
    }

    public void setSubAuthorityCount(Int8 subAuthorityCount)
    {
        _subAuthorityCount = subAuthorityCount;
    }

    public SidIdentifierAuthority getIdentifierAuthority()
    {
        return _identifierAuthority;
    }

    public void setIdentifierAuthority(SidIdentifierAuthority identifierAuthority)
    {
        _identifierAuthority = identifierAuthority;
    }

    public PrimitiveArray getSubAuthority()
    {
        return _subAuthority;
    }

    public void setSubAuthority(PrimitiveArray subAuthority)
    {
        _subAuthority = subAuthority;
    }

    public Pointer.Void getpSubAuthority()
    {
        return _pSubAuthority;
    }

    public void setpSubAuthority(Pointer.Void pSubAuthority)
    {
        _pSubAuthority = pSubAuthority;
    }

    public boolean lookupAccountSid(AnsiString name, AnsiString domainName)
    {
        if (!isValidSid(this))
        {
            return false;
        }
        Function lookupAccountSid = AdvApi32.get(FUNCTION_LOOKUP_ACCOUNT_SID);
        IntBool result = new IntBool();
        Int accountType = new Int();
        UInt32 nameSize = new UInt32(100);
        UInt32 domainNameSize = new UInt32(100);

        lookupAccountSid.invoke(result, new Parameter[]{new Pointer.Void(), new Pointer(this), name, new Pointer(nameSize), domainName, new Pointer(domainNameSize), new Pointer(accountType)});
        return result.getValue() != 0;
    }


    public boolean isValidSid(Sid sid)
    {
        Function isValidSid = AdvApi32.get(FUNCTION_IS_VALID_SID);
        IntBool result = new IntBool();
        isValidSid.invoke(result, new Pointer(sid));
        return result.getValue() != 0;
    }


    private long getLengthSid(Sid sid)
    {
        Function isValidSid = AdvApi32.get(FUNCTION_GET_LENGTH_SID);
        IntBool result = new IntBool();
        isValidSid.invoke(result, new Pointer(sid));
        return result.getValue();
    }
}

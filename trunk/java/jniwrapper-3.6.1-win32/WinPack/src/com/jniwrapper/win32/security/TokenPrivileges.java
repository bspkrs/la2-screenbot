/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.security;

import com.jniwrapper.*;
import com.jniwrapper.win32.FunctionName;
import com.jniwrapper.win32.Handle;
import com.jniwrapper.win32.system.AdvApi32;

/**
 * This structure contains information about privileges for access token and corresponds to
 * native <code>TOKEN_PRIVILEGES</code> structure.
 */
public class TokenPrivileges extends Structure
{
    private UInt32 _privilegeCount = new UInt32();
    private ComplexArray _privileges = new ComplexArray(new LuidAndAttributes(), 1);
    private static final String FUNCTION_LOOKUP_PRIVILEGE_VALUE = "LookupPrivilegeValue";

    public TokenPrivileges()
    {
        init(new Parameter[]{_privilegeCount, _privileges});
    }

    private TokenPrivileges(TokenPrivileges other)
    {
        _privilegeCount = (UInt32) other._privilegeCount.clone();
        _privileges = (ComplexArray) other._privileges.clone();

        init(new Parameter[]{_privilegeCount, _privileges});
    }

    public int getPrivilegeCount()
    {
        return (int) _privilegeCount.getValue();
    }

    public void setPrivilegeCount(int privilegeCount)
    {
        _privilegeCount.setValue(privilegeCount);
    }

    public LuidAndAttributes getPrivileges(int index)
    {
        return (LuidAndAttributes) _privileges.getElement(index);
    }

    public Object clone()
    {
        return new TokenPrivileges(this);
    }

    public static TokenPrivileges lookup(String name)
    {
        TokenPrivileges tokenPrivileges = new TokenPrivileges();

        if (lookupPrivilegeValue(name, tokenPrivileges.getPrivileges(0).getLuid()))
        {
            return tokenPrivileges;
        }
        else
        {
            return null;
        }
    }

    /**
     * Lookups privilege by name.
     *
     * @param name name of privelege
     * @param luid
     * @return true if succeded else returns false
     */
    private static boolean lookupPrivilegeValue(String name, Luid luid)
    {
        Function function = AdvApi32.getInstance().getFunction(new FunctionName(FUNCTION_LOOKUP_PRIVILEGE_VALUE).toString());
        Bool bool = new Bool();
        function.invoke(bool, new Parameter[]{
                new Handle(),
                new Str(name),
                new Pointer(luid)
        });

        return bool.getValue();
    }
}

/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.system;

import com.jniwrapper.*;

/**
 * Class SecurityAttributes represets SECURITY_ATTRIBUTES type.
 *
 * @author Serge Piletsky
 */
public class SecurityAttributes extends Structure
{
    private UInt32 _length = new UInt32();
    private Pointer.Void _securityDescriptor = new Pointer.Void();
    private IntBool _inheritHandle = new IntBool();

    public SecurityAttributes()
    {
        init(new Parameter[]{_length, _securityDescriptor, _inheritHandle}, (short)8);
        _length.setValue(getLength());
    }

    public SecurityAttributes(SecurityAttributes that)
    {
        this();
        initFrom(that);
    }

    public Pointer.Void getSecurityDescriptor()
    {
        return _securityDescriptor;
    }

    public boolean getInheritHandle()
    {
        return _inheritHandle.getBooleanValue();
    }

    public void setInheritHandle(boolean value)
    {
        _inheritHandle.setBooleanValue(value);
    }

    public Object clone()
    {
        return new SecurityAttributes(this);
    }
}

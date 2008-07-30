/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.registry;

import com.jniwrapper.util.EnumItem;

/**
 * RegistryKeyType represents EnumItemeration of registry key types.
 */
public class RegistryKeyType extends EnumItem
{
    public static final RegistryKeyType NONE = new RegistryKeyType(0);
    public static final RegistryKeyType SZ = new RegistryKeyType(1);
    public static final RegistryKeyType EXPAND_SZ = new RegistryKeyType(2);
    public static final RegistryKeyType BINARY = new RegistryKeyType(3);
    public static final RegistryKeyType DWORD = new RegistryKeyType(4);
    public static final RegistryKeyType DWORD_LITTLE_ENDIAN = new RegistryKeyType(4);
    public static final RegistryKeyType DWORD_BIG_ENDIAN = new RegistryKeyType(5);
    public static final RegistryKeyType LINK = new RegistryKeyType(6);
    public static final RegistryKeyType MULTI_SZ = new RegistryKeyType(7);
    public static final RegistryKeyType RESOURCE_LIST = new RegistryKeyType(8);
    public static final RegistryKeyType FULL_RESOURCE_DESCRIPTOR = new RegistryKeyType(9);
    public static final RegistryKeyType RESOURCE_REQUIREMENTS_LIST = new RegistryKeyType(10);

    protected RegistryKeyType(int value)
    {
        super(value);
    }
}


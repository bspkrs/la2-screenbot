/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.system;

import com.jniwrapper.util.EnumItem;

/**
 * This class provides generic flags for access options flags.
 *
 * @author Vladimir Kondrashchenko
 */
public class AccessOptions extends EnumItem
{
    /**
     * Provides the right to delete the object.
     */
    public static final AccessOptions DELETE = new AccessOptions(0x00010000);

    /**
     * Provides the right to read object's information.
     */
    public static final AccessOptions READ_CONTROL = new AccessOptions(0x00020000);

    /**
     * Provides the right to modify DACL.
     */
    public static final AccessOptions WRITE_DAC = new AccessOptions(0x00040000);

    /**
     * Provides the right to modify owner of the object.
     */
    public static final AccessOptions WRITE_OWNER = new AccessOptions(0x00080000);

    /**
     * Provides the right to use the object for synchronization.
     */
    public static final AccessOptions SYNCHRONIZE = new AccessOptions(0x00080000);

    /**
     * Provides all rights except the synchronize right.
     */
    public static final AccessOptions STANDARD_RIGHTS_REQUIRED = new AccessOptions(0x000F0000);

    /**
     * Provides the READ_CONTROL right.
     */
    public static final AccessOptions STANDARD_RIGHTS_READ = READ_CONTROL;

    /**
     * Provides the READ_CONTROL right.
     */
    public static final AccessOptions STANDARD_RIGHTS_WRITE = READ_CONTROL;

    /**
     * Provies the READ_CONTROL right.
     */
    public static final AccessOptions STANDARD_RIGHTS_EXECUTE = READ_CONTROL;

    /**
     * Provides all rights.
     */
    public static final AccessOptions STANDARD_RIGHTS_ALL = new AccessOptions(0x001F0000);

    /**
     * Provides all rights specific to the object type.
     */
    public static final AccessOptions SPECIFIC_RIGHTS_ALL = new AccessOptions(0x0000FFFF);

    /**
     * Provides all standard rights for the object.
     */
    public static final AccessOptions ACCESS_SYSTEM_SECURITY = new AccessOptions(0x01000000);

    /**
     * Provides maximum allowed access to the object.
     */
    public static final AccessOptions MAXIMUM_ALLOWED = new AccessOptions(0x02000000);

    /**
     * Provides the right to read.
     */
    public static final AccessOptions GENERIC_READ = new AccessOptions(0x80000000);

    /**
     * Provides the right to write.
     */
    public static final AccessOptions GENERIC_WRITE = new AccessOptions(0x40000000);

    /**
     * Provides the right to execute.
     */
    public static final AccessOptions GENERIC_EXECUTE = new AccessOptions(0x20000000);

    /**
     * Provides all rights.
     */
    public static final AccessOptions GENERIC_ALL = new AccessOptions(0x10000000);

    private AccessOptions(int value)
    {
        super(value);
    }
}

/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.shell;

import com.jniwrapper.util.FlagSet;

/**
 * Enumeration of all available flags for {@link IActiveDesktop#applyChanges(com.jniwrapper.win32.shell.ApplyChangesOptions)} method.
 */
public class ApplyChangesOptions extends FlagSet
{
    /**
     * Save the desktop item.
     */
    public static final int SAVE = 0x1;
    /**
     * Regenerate the desktop HTML file.
     */
    public static final int HTMLGEN = 0x2;
    /**
     * Refresh the desktop item.
     */
    public static final int REFRESH = 0x4;
    /**
     * Aggregate of the SAVE, HTMLGEN, and REFRESH values.
     */
    public static final int ALL = SAVE | HTMLGEN | REFRESH;
    /**
     * Force an Active Desktop change.
     */
    public static final int FORCE = 0x8;
    /**
     * Starts a timer and aggregates all the buffered refresh
     * requests during that time interval into a single refresh
     */
    public static final int BUFFERED_REFRESH = 0x10;

    public ApplyChangesOptions()
    {
    }

    public ApplyChangesOptions(long flags)
    {
        super(flags);
    }
}

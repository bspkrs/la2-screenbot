/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.hook;

import java.util.EventListener;

/**
 * This class is a listener of the mouse and keyboard idle timeout events.
 *
 * @see <code>com.jniwrapper.win32.hook.IdleTracker</code>
 *
 * @author Vladimir Kondrashchenko
 */
public interface IdleTrackerListener extends EventListener
{
    public void timeoutElapsed();
}

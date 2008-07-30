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
 * Base interface for all hook event listeners.
 *
 * @author Serge Piletsky
 */
public interface HookEventListener extends EventListener
{
    public void onHookEvent(HookEventObject event);
}

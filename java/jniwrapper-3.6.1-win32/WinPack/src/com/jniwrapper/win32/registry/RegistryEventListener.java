/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.registry;

import java.util.EventListener;
import java.util.EventObject;

/**
 * RegistryEventListener interface.
 * 
 * @author Serge Piletsky
 */
public interface RegistryEventListener extends EventListener
{
    public void handle(EventObject event);
}

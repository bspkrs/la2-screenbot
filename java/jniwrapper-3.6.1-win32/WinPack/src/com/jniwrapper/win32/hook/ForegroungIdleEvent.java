/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.hook;

/**
 * This class describes events of the {@link
 * com.jniwrapper.win32.hook.Hook.Descriptor#FOREGROUNDIDLE} hook.
 * 
 * @author Serge Piletsky
 */
public class ForegroungIdleEvent extends HookEventObject
{
    public ForegroungIdleEvent(Object source)
    {
        super(source);
    }
}

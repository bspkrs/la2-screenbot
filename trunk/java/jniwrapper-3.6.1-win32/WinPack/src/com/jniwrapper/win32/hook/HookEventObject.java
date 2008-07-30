/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.hook;

import java.util.EventObject;

/**
 * Base hook event object.
 *
 * @author Serge Piletsky
 */
public class HookEventObject extends EventObject
{
    HookEventObject(Object source)
    {
        super(source);
    }
}

/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.hook.data;

import com.jniwrapper.Parameter;

/**
 * @author Serge Piletsky
 */
class ForegroungIdleHookData extends AbstractHookData
{
    public ForegroungIdleHookData()
    {
        init(new Parameter[]{_hookHandle, _eventDescriptor});
    }
}

/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.hook.data;

import com.jniwrapper.AnsiString;
import com.jniwrapper.Structure;
import com.jniwrapper.win32.Handle;

/**
 * @author Serge Piletsky
 */
abstract class AbstractHookData extends Structure
{
    protected Handle _hookHandle = new Handle();
    protected AnsiString _eventDescriptor = new AnsiString(40);

    public String getEventDescriptor()
    {
        return _eventDescriptor.getValue();
    }
}

/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.hook.data;

import com.jniwrapper.Int;
import com.jniwrapper.Parameter;

class JournalRecordHookData extends AbstractHookData
{
    private Int code = new Int();
    private EventMessageStructure eventMessage = new EventMessageStructure();

    public JournalRecordHookData()
    {
        init(new Parameter[]{_hookHandle, _eventDescriptor, code, eventMessage});
    }

    long getCode()
    {
        return code.getValue();
    }

    EventMessageStructure getEventMessage()
    {
        return eventMessage;
    }
}

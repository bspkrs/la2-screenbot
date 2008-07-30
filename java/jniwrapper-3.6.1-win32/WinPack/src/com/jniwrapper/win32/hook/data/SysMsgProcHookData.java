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
import com.jniwrapper.win32.Msg;

/**
 * @author Serge Piletsky
 */
class SysMsgProcHookData extends AbstractHookData
{
    private Int code = new Int();
    private Msg msg = new Msg();

    public SysMsgProcHookData()
    {
        init(new Parameter[]{_hookHandle, _eventDescriptor, code, msg});
    }

    long getCode()
    {
        return code.getValue();
    }

    Msg getMsg()
    {
        return msg;
    }
}

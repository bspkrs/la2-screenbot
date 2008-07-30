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
import com.jniwrapper.UInt32;
import com.jniwrapper.win32.Msg;
import com.jniwrapper.win32.IntPtr;

class GetMsgHookData extends AbstractHookData
{
    private IntPtr wParam = new IntPtr();
    private Msg msg = new Msg();

    GetMsgHookData()
    {
        init(new Parameter[]{_hookHandle, _eventDescriptor, wParam, msg});
    }

    long getWParam()
    {
        return wParam.getValue();
    }

    Msg getMsg()
    {
        return msg;
    }
}

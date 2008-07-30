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
import com.jniwrapper.Int;
import com.jniwrapper.win32.IntPtr;

class ShellHookData extends AbstractHookData
{
    private Int code = new Int();
    private IntPtr wParam = new IntPtr();
    private IntPtr lParam = new IntPtr();

    ShellHookData()
    {
        init(new Parameter[]{_hookHandle, _eventDescriptor, code, wParam, lParam});
    }

    long getCode()
    {
        return code.getValue();
    }

    long getWParam()
    {
        return wParam.getValue();
    }

    long getLParam()
    {
        return lParam.getValue();
    }
}

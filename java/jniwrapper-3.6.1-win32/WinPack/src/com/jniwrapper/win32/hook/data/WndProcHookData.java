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
import com.jniwrapper.win32.IntPtr;

/**
 * @author Serge Piletsky
 */
class WndProcHookData extends AbstractHookData
{
    private IntPtr wParam = new IntPtr();
    private CWndProcStructure cwpStruct = new CWndProcStructure();

    WndProcHookData()
    {
        init(new Parameter[]{_hookHandle, _eventDescriptor, wParam, cwpStruct});
    }

    long getwParam()
    {
        return wParam.getValue();
    }

    CWndProcStructure getCwpStruct()
    {
        return cwpStruct;
    }
}

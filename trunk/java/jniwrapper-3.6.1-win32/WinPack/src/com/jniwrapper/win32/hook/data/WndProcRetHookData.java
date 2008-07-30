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
import com.jniwrapper.win32.IntPtr;

/**
 * @author Serge Piletsky
 */
class WndProcRetHookData extends AbstractHookData
{
    private IntPtr wParam = new IntPtr();
    private CWndProcRetStructure cwpRetStruct = new CWndProcRetStructure();

    WndProcRetHookData()
    {
        init(new Parameter[]{_hookHandle, _eventDescriptor, wParam, cwpRetStruct});
    }

    long getwParam()
    {
        return wParam.getValue();
    }

    CWndProcRetStructure getCwpRetStruct()
    {
        return cwpRetStruct;
    }
}

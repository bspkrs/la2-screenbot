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
 * This class represents the CWPRETSTRUCT structure, which defines the message
 * parameters passed to a WH_CALLWNDPROCRET hook procedure.
 * 
 * @author Serge Piletsky
 */
class CWndProcRetStructure extends CWndProcStructure
{
    private IntPtr lResult = new IntPtr();

    CWndProcRetStructure()
    {
        init(new Parameter[]{lResult, lParam, wParam, message, hwnd});
    }

    /**
     * Specifies the return value of the window procedure that processed the
     * message specified by the message value.
     */
    long getResult()
    {
        return lResult.getValue();
    }
}

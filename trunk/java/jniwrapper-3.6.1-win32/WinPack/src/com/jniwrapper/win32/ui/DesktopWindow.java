/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.ui;

import com.jniwrapper.Function;

/**
 * @author Alexander Evsukov
 */
public class DesktopWindow extends Wnd
{
    static final String FUNCTION_GET_DESKTOP_WINDOW = "GetDesktopWindow";

    private DesktopWindow()
    {
    }

    public static DesktopWindow getInstance()
    {
        DesktopWindow hDesktop = new DesktopWindow();
        final Function function = User32.getInstance().getFunction(FUNCTION_GET_DESKTOP_WINDOW);
        function.invoke(hDesktop);
        return hDesktop;
    }
}

/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.gdi;

import com.jniwrapper.Parameter;
import com.jniwrapper.Pointer;
import com.jniwrapper.win32.FunctionName;

/**
 * The Font class represents the HFONT type.
 */
public class Font extends GdiObject
{
    private static final FunctionName FUNCTION_CREATE_FONT_INDIRECT = new FunctionName("CreateFontIndirect");

    public Font()
    {
    }

    public Font(long value)
    {
        super(value);
    }

    public static Font createFontIndirect(LogFont logFont)
    {
        Font result = new Font();
        Gdi32.getInstance().getFunction(FUNCTION_CREATE_FONT_INDIRECT.toString()).invoke(
                result,
                new Parameter[] { new Pointer(logFont) });

        return result;
    }
}

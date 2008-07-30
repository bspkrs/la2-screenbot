/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.gdi;

import com.jniwrapper.util.EnumItem;

/**
 * PolyFillMode class represents EnumItemeration of poly fill modes.
 *
 * @author Serge Piletsky
 */
public class PolyFillMode extends EnumItem
{
    public static final PolyFillMode ALTERNATE = new PolyFillMode(1);
    public static final PolyFillMode WINDING = new PolyFillMode(2);

    private PolyFillMode(int value)
    {
        super(value);
    }
}

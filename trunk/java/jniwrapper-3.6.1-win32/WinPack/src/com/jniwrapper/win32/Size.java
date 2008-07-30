/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32;

import com.jniwrapper.Parameter;
import com.jniwrapper.Structure;
import com.jniwrapper.LongInt;

/**
 * The Size class corresponds to <code>SIZE</code> native structure.
 */
public class Size extends Structure
{
    private LongInt _cx;
    private LongInt _cy;


    public Size()
    {
        _cx = new LongInt();
        _cy = new LongInt();

        init();
    }

    public Size(int cx, int cy)
    {
        this();

        setCx(cx);
        setCy(cy);
    }

    public Size(Size that)
    {
        this();
        initFrom(that);
    }

    private void init()
    {
        init(new Parameter[]{_cx, _cy}, (short)4);
    }

    public int getCx()
    {
        return (int)_cx.getValue();
    }

    public void setCx(int value)
    {
        _cx.setValue(value);
    }

    public int getCy()
    {
        return (int)_cy.getValue();
    }

    public void setCy(int value)
    {
        _cy.setValue(value);
    }

    public Object clone()
    {
        return new Size(this);
    }

    public String toString()
    {
        StringBuffer result = new StringBuffer(getClass().getName());
        result.append("[cx=").append(_cx.getValue()).append(",cy=").append(_cy.getValue()).append(']');
        return result.toString();
    }
}

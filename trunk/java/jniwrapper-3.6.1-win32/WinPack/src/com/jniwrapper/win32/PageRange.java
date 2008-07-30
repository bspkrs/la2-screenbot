/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32;

import com.jniwrapper.LongInt;
import com.jniwrapper.Parameter;
import com.jniwrapper.Structure;

public class PageRange extends Structure
{
    public static final long TO_LAST_PAGE = 65535;

    private LongInt _fromPage = new LongInt();
    private LongInt _toPage = new LongInt();

    private void init()
    {
        init(new Parameter[] { _fromPage, _toPage });
    }

    public PageRange()
    {
        init();
    }

    public PageRange(PageRange that)
    {
        _fromPage = (LongInt)that._fromPage.clone();
        _toPage = (LongInt)that._toPage.clone();

        init();
    }

    public int getFromPage()
    {
        return (int) _fromPage.getValue();
    }

    public void setFromPage(int fromPage)
    {
        _fromPage.setValue(fromPage);
    }

    public int getToPage()
    {
        return (int) _toPage.getValue();
    }

    public void setToPage(long toPage)
    {
        _toPage.setValue(toPage);
    }

    public Object clone()
    {
        return new PageRange(this);
    }
}

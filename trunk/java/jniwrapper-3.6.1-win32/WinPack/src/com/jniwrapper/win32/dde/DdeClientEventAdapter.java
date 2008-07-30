/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.dde;

/**
 * An abstract adapter class for processing DDE client events.
 *
 * @author Vladimir Kondrashchenko
 */
public abstract class DdeClientEventAdapter extends DdeEventAdapter implements DdeClientEventHandler
{
    public void error(int errorCode)
    {
    }

    public DdeResponse itemChanged(DdeItem item, byte[] data)
    {
        return null;
    }

    public void asyncActionComplete(DdeItem item, byte[] data, long transactionID)
    {
    }
}

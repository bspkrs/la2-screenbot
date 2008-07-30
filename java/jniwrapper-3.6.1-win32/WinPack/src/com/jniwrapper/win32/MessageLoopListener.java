/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32;

import java.util.EventListener;

/**
 * The listener interface for receiving events in the {@link MessageLoopThread}
 *
 * @author Serge Piletsky
 */
public interface MessageLoopListener extends EventListener
{
    /**
     * Invoked when the new message occures in the OLE message loop.
     * @param msg the occured message.
     *
     * @return true if message is processed else false
     */
    public boolean onMessage(Msg msg);
}

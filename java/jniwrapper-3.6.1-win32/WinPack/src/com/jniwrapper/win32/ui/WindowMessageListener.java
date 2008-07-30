/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.ui;

import java.util.EventListener;

/**
 * WindowMessageListener interface.
 * 
 * @author Serge Piletsky
 */
public interface WindowMessageListener extends EventListener
{
    /**
     * Tests if the listener can handle a specified message.
     * 
     * @param message
     * @param beforeWindowProc the flag specifies that theevent occurred before
     * or after window procedure.
     * @return true if the message can be processed; otherwise false.
     */
    public boolean canHandle(WindowMessage message, boolean beforeWindowProc);

    /**
     * Handles window message.
     * 
     * @param message
     * @return the result of message processing.
     */
    public int handle(WindowMessage message);
}

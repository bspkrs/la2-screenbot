/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.ui;

// TODO [Sanders]: Find a better name.
public interface WindowMessageListenerEx extends WindowMessageListener
{
    // TODO [Sanders]: Find better name and document.
    public boolean isCallWindowProc(WindowMessage message);
}

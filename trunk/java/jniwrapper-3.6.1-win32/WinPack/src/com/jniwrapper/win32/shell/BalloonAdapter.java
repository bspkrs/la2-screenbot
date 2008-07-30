/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.shell;

import java.util.EventObject;

/**
 * Event adapter for watching balloon tooltip messages.
 *
 * @author Vladimir Kondrashchenko
 */
public abstract class BalloonAdapter implements BalloonListener
{
    /**
     * Fires when the balloon tooltip is shown.
     *
     * @param balloonEventObject describes source of the event.
     */
    public void balloonShown(EventObject balloonEventObject) { }

    /**
     * Fires when the balloon tooltip disappears not because of
     * timeout or user click.
     *
     * @param balloonEventObject describes source of the event.
     */
    public void balloonHide(EventObject balloonEventObject) { }

    /**
     * Fires when the balloon tooltip disappears because of timeout.
     *
     * @param balloonEventObject describes source of the event.
     */
    public void balloonTimeOut(EventObject balloonEventObject) { }

    /**
     * Fires when the balloon tooltip disappears because of user click.
     *
     * @param balloonEventObject describes source of the event.
     */
    public void balloonUserClick(EventObject balloonEventObject) { }
}

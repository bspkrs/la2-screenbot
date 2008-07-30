package com.jniwrapper.win32.shell.events;

import java.util.EventListener;

/**
 * The listener interface for receiving and processing shell events.
 */
public interface ShellEventsListener extends EventListener
{
    /**
     * Invoked when a shell event has occurred.
     */
    public void processEvent(ShellEvent event);
}

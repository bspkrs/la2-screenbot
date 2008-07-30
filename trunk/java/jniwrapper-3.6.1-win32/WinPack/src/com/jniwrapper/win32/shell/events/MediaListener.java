package com.jniwrapper.win32.shell.events;

import java.util.EventListener;

/**
 * The listener interface for receiving media events.
 * This interface just aggregates two {@link ShellEvent#SHCNE_MEDIAINSERTED} and {@link ShellEvent#SHCNE_MEDIAREMOVED}
 * events and provides appropriate handlers for conveniance.
 */
public interface MediaListener extends EventListener
{
    /**
     * Invoked when a new media has been inserted.
     */
    public void mediaInserted(String driveName);

    /**
     * Invoked when a media been removed.
     */
    public void mediaRemoved(String driveName);
}

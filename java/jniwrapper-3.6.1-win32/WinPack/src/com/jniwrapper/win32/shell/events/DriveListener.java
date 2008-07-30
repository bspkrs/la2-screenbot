package com.jniwrapper.win32.shell.events;

import java.util.EventListener;

/**
 * The listener interface for receiving drive events.
 * This interface just aggregates two {@link ShellEvent#SHCNE_DRIVEADD} and {@link ShellEvent#SHCNE_DRIVEREMOVED} events
 * and provides appropriate handlers for convenience.
 */
public interface DriveListener extends EventListener
{
    /**
     * Invoked when a new drive has been added.
     */
    public void driveAdded(String driveName);

    /**
     * Invoked when a drive has been removed.
     */
    public void driveRemoved(String driveName);
}

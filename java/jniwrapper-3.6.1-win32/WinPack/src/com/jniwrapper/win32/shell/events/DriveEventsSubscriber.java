package com.jniwrapper.win32.shell.events;

import com.jniwrapper.win32.shell.ShellFolder;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * In addition to the {@link ShellEventsSubscriber} this class provides a convenient way for processing drive events.
 */
public class DriveEventsSubscriber extends ShellEventsSubscriber implements ShellEventsListener
{
    private List _driveListeners = Collections.synchronizedList(new LinkedList());

    public DriveEventsSubscriber()
    {
        addShellEventsListener(this);
    }

    public void addDriveListener(DriveListener listener)
    {
        _driveListeners.add(listener);
    }

    public void removeDriveListener(DriveListener listener)
    {
        _driveListeners.remove(listener);
    }

    public void processEvent(ShellEvent event)
    {
        int id = event.getId();
        switch (id)
        {
            case ShellEvent.SHCNE_DRIVEADD:
            case ShellEvent.SHCNE_DRIVEREMOVED:
            {
                String path = ShellFolder.getPathFromIDList(event.getItem1());
                for (Iterator iterator = _driveListeners.iterator(); iterator.hasNext();)
                {
                    DriveListener driveListener = (DriveListener) iterator.next();
                    if (id == ShellEvent.SHCNE_DRIVEADD)
                        driveListener.driveAdded(path);
                    else
                        driveListener.driveRemoved(path);
                }
                break;
            }
        }
    }
}

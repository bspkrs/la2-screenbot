package com.jniwrapper.win32.shell.events;

import com.jniwrapper.win32.shell.ShellFolder;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * In addition to the {@link ShellEventsSubscriber} this class provides convenient way for processing media events.
 */
public class MediaEventsSubscriber extends ShellEventsSubscriber implements ShellEventsListener
{
    private List _mediaListeners = Collections.synchronizedList(new LinkedList());

    public MediaEventsSubscriber()
    {
        addShellEventsListener(this);
    }

    public void addMediaListener(MediaListener listener)
    {
        _mediaListeners.add(listener);
    }

    public void removeMediaListener(MediaListener listener)
    {
        _mediaListeners.remove(listener);
    }

    public void processEvent(ShellEvent event)
    {
        int id = event.getId();
        switch (id)
        {
            case ShellEvent.SHCNE_MEDIAREMOVED:
            case ShellEvent.SHCNE_MEDIAINSERTED:
            {
                String path = ShellFolder.getPathFromIDList(event.getItem1());
                for (Iterator iterator = _mediaListeners.iterator(); iterator.hasNext();)
                {
                    MediaListener mediaListener = (MediaListener) iterator.next();
                    if (id == ShellEvent.SHCNE_MEDIAREMOVED)
                        mediaListener.mediaRemoved(path);
                    else
                        mediaListener.mediaInserted(path);
                }
                break;
            }
        }
    }
}

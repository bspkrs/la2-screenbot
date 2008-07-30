/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.system;

import com.jniwrapper.*;
import com.jniwrapper.win32.Handle;

/**
 * This class represents a Windows Event object.
 * 
 * @author Serge Piletsky
 */
public class EventObject extends Handle
{
    private static final String FUNCTION_CreateEvent = "CreateEventA";
    private static final String FUNCTION_ResetEvent = "ResetEvent";
    private static final String FUNCTION_SetEvent = "SetEvent";

    private String _name;

    /**
     * Creates new event object without a name.
     */
    public EventObject()
    {
        this(null);
    }

    /**
     * Creates a new named event object.
     *
     * @param name the name of the event; if this parameter is null then event object created without name.
     */
    public EventObject(String name)
    {
        final Function createEvent = Kernel32.getInstance().getFunction(FUNCTION_CreateEvent);
        createEvent.invoke(this,
                new Handle(),
                new Bool(true),
                new Bool(false),
                new Pointer(name == null? null : new AnsiString(name), name == null));
        _name = name;
    }

    /**
     * Creates event object by specified handle.
     *
     * @param eventHandle native handle of event object
     */
    public EventObject(long eventHandle) {
        this.setValue(eventHandle);
    }

    /**
     * Waits for an event object.
     */
    public void waitFor()
    {
        Handle.waitFor(this);
    }

    /**
     * Resets the state of the event object.
     */
    public void reset()
    {
        final Function resetEvent = Kernel32.getInstance().getFunction(FUNCTION_ResetEvent.toString());
        resetEvent.invoke(null, this);
    }

    /**
     * Notifies the event object.
     */
    public void notifyEvent()
    {
        final Function setEvent = Kernel32.getInstance().getFunction(FUNCTION_SetEvent.toString());
        setEvent.invoke(null, this);
    }

    /**
     * Closes the event object.
     */
    public void close()
    {
        Handle.closeHandle(this);
    }

    /**
     * Returns the event name.
     *
     * @return name
     */
    public String getName()
    {
        return _name;
    }
}

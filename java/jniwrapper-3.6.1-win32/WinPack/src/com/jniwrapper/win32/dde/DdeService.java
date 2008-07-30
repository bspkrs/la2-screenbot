/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.dde;

import com.jniwrapper.util.Logger;
import com.jniwrapper.win32.MessageLoopThread;

import java.lang.reflect.InvocationTargetException;

/**
 * This class provides functionality for registering and handling a DDE service.
 *
 * @author Vladimir Kondrashchenko
 */
public class DdeService
{
    private static final Logger _log = Logger.getInstance(DdeService.class);

    private MessageLoopThread _messageLoop;
    private DdeServiceHelper _ddeServiceHelper;

    /**
     * Creates a DDE service with the specified name.
     *
     * @param name is the service name.
     */
    public DdeService(String name)
    {
        _ddeServiceHelper = new DdeServiceHelper(name);
        try
        {
            invokeHelperMethod("createCallback", null);
        }
        catch (DdeException e)
        {
            _log.error("", e);
        }
    }

    /**
     * Registers the service.
     *
     * @throws DdeException
     */
    public void register() throws DdeException
    {
        register("Global\\JavaDdeServiceRegistered");
    }

    /**
     * Registers the service. If <code>eventName</code> is not <code>null</code>,
     * the appropriate event object will set to a signal state after the service is registered.
     *
     * @param eventName specified the event object name.
     * @throws DdeException
     */
    private void register(String eventName) throws DdeException
    {
        invokeHelperMethod("register", new Object[] {eventName});
    }

    /**
     * Returns <code>true</code> if the service is already registered.
     *
     * @return <code>true</code> if the service is already registered.
     */
    public boolean isRegistered()
    {
        return _ddeServiceHelper.isRegistered();
    }

    /**
     * Unregisters the service.
     *
     * @throws DdeException
     */
    public void unregister() throws DdeException
    {
        invokeHelperMethod("unregister", null);
    }

    /**
     * Specifies the handler of service events.
     *
     * @param eventHandler
     * @see DdeServiceEventHandler
     */
    public void setEventHandler(DdeServiceEventHandler eventHandler)
    {
        if (eventHandler == null)
        {
            throw new IllegalArgumentException("Event handler must be not null.");
        }
        _ddeServiceHelper.setEventHandler(eventHandler);
    }

    /**
     * Removes the service events handler.
     */
    public void removeEventHandler()
    {
        _ddeServiceHelper.removeEventHandler();
    }

    /**
     * Returns the name of the service.
     *
     * @return the name of the service.
     */
    public String getName()
    {
        return _ddeServiceHelper.getName();
    }

    /**
     * Notifies clients that the value of item data has been changed.
     *
     * @param topic is the name of the topic.
     * @param item is the name of the item.
     * @throws DdeException
     */
    public void postAdvise(String topic, String item) throws DdeException
    {
        invokeHelperMethod("postAdvise", new Object[] {topic, item});
    }

    private Object invokeHelperMethod(String methodName, Object[] params) throws DdeException
    {
        Object result = null;
        try
        {
            result = getMessageLoop().doInvokeMethod(_ddeServiceHelper, methodName, params);
        }
        catch (InterruptedException e)
        {
            _log.error("", e);
        }
        catch (InvocationTargetException e)
        {
            Throwable cause = e.getCause().getCause();
            if (cause instanceof DdeException)
            {
                throw (DdeException) cause;
            }
            else
            {
                _log.error("", e);
            }
        }
        return result;
    }


    private MessageLoopThread getMessageLoop()
    {
        if (_messageLoop == null)
        {
            _messageLoop = new MessageLoopThread();
            _messageLoop.doStart();
        }
        return _messageLoop;
    }
}

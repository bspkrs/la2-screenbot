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
 * This class provides functionality for establishing connection with a DDE service.
 *
 * @author Vladimir Kondrashchenko
 */
public class DdeClient
{
    private static final Logger _log = Logger.getInstance(DdeClient.class);

    private String _service;
    private String _topic;
    private MessageLoopThread _messageLoop;
    private DdeClientEventHandler _clientEventHandler;
    private DdeClientHelper _ddeClientHelper = new DdeClientHelper();

    /**
     * Creates a DDE client of the specified service and topic.
     *
     * @param service is the name of the service of interest. It can be the basic name
     *                (for example, MyService) or an instance-specific service name (for example, "MyService(0X0046025E)".
     * @param topic is the name of the service's topic.
     */
    public DdeClient(String service, String topic)
    {
        _service = service;
        _topic = topic;
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
     * Establishes connection with the service.
     *
     * @throws DdeException
     */
    public void connect() throws DdeException
    {
        invokeHelperMethod("connect", new Object[] {_service, _topic});
        if (_clientEventHandler != null)
        {
            setEventHandler(_clientEventHandler);
        }
    }

    /**
     * Disconnects the client from the service.
     *
     * @throws DdeException
     */
    public void disconnect() throws DdeException
    {
        invokeHelperMethod("disconnect", null);
    }

    /**
     * Makes an asynchronous request to the service for data
     * that corresponds to the specified item.
     *
     * @param item specifies an item name and required data format.
     * @return an asynchronous transaction identifier.
     * @see DdeItem
     * @throws DdeException
     */
    public long getAsync(DdeItem item) throws DdeException
    {
        Long result = (Long) invokeHelperMethod("getAsync", new Object[] {item});
        return result.longValue();
    }

    /**
     * Makes a synchronous request to the service for data
     * that corresponds to the specified item.
     *
     * @param item specifies an item name and required data format.
     * @param timeout is the maximum amount of time, in milliseconds, that the client waits
     *        for the server response.
     * @return the data of interest.
     * @throws DdeException
     */
    public byte[] get(DdeItem item, long timeout) throws DdeException
    {
        Object result = invokeHelperMethod("get", new Object[] {item, new Long(timeout)});
        return (byte[]) result;
    }

    /**
     * Asynchronously sends data to the service.
     *
     * @param item specifies an item name and data format.
     * @param data is the data to be sent.
     * @return an asynchronous transaction identifier.
     * @see DdeItem
     * @throws DdeException
     */
    public long sendAsync(DdeItem item, byte[] data) throws DdeException
    {
        Long result = (Long) invokeHelperMethod("sendAsync", new Object[] {item, data});
        return result.longValue();
    }

    /**
     * Sends data to the service and waits for a confirmation response.
     *
     * @param item specifies an item name and data format.
     * @param data is the data to be sent.
     * @param timeout is the maximum amount of time, in milliseconds, that the client waits
     *        for the server's response.
     * @see DdeItem
     * @throws DdeException
     */
    public void send(DdeItem item, byte[] data, long timeout) throws DdeException
    {
        invokeHelperMethod("send", new Object[] {item, data, new Long(timeout)});
    }

    /**
     * Asynchronously executes the specified command.
     *
     * @param command specifies the command to be executed.
     * @return an asynchronous transaction identifier.
     * @throws DdeException
     */
    public long executeAsync(String command) throws DdeException
    {
        Long result = (Long) invokeHelperMethod("executeAsync", new Object[] {command});
        return result.longValue();
    }

    /**
     * Executes the specified command and waits for a confirmation response
     * from the service.
     *
     * @param command specifies the command to be executed.
     * @param timeout is the maximum amount of time, in milliseconds, that the client waits
     *        for the server's response.
     * @throws DdeException
     */
    public void execute(String command, long timeout) throws DdeException
    {
        invokeHelperMethod("execute", new Object[] {command, new Long(timeout)});
    }

    /**
     * Asynchronously begins an advise loop. An advise loop allows the client to get notifications
     * when item data changes.
     *
     * @param item specifies an item name and required data format.
     * @param sendData if <code>true</code>, the service will send the changed data with notification,
     *                 otherwise, the service will notify the client without sending the data.
     * @param sync if <code>true</code>, the service will not send the next notification until
     *             the previous one is processed by the client.
     * @return an asynchronous transaction identifier.
     * @throws DdeException
     */
    public long startAdviseLoopAsync(DdeItem item, boolean sendData, boolean sync) throws DdeException
    {
        Long result = (Long) invokeHelperMethod("startAdviseLoopAsync", new Object[] {
            item, new Boolean(sendData), new Boolean(sync)});
        return result.longValue();
    }

    /**
     * Begins an advise loop and waits for confirmation response from the service.
     * An advise loop allows the client to get notifications
     * when item data changes.
     *
     * @param item specifies an item name and required data format.
     * @param sendData if <code>true</code>, the service will send the changed data with notification,
     *                 otherwise, the service will notify the client without sending the data.
     * @param sync if <code>true</code>, the service will not send the next notification until
     *             the previous one is processed by the client.
     * @param timeout is the maximum amount of time, in milliseconds, that the client waits
     *        for the server's response.
     * @throws DdeException
     */
    public void startAdviseLoop(DdeItem item, boolean sendData, boolean sync, long timeout) throws DdeException
    {
        invokeHelperMethod("startAdviseLoop", new Object[] {item, new Boolean(sendData), new Boolean(sync),
                                                            new Long(timeout)});
    }

    /**
     * Asynchronously stops an advise loop.
     *
     * @param item specifies an item name.
     * @return an asynchronous transaction identifier.
     * @throws DdeException
     */
    public long stopAdviseLoopAsync(DdeItem item) throws DdeException
    {
        Long result = (Long) invokeHelperMethod("stopAdviseLoopAsync", new Object[] {item});
        return result.longValue();
    }

    /**
     * Stops an advise loop and waits for a confirmation response from the service.
     *
     * @param item specifies an item name.
     * @param timeout is the maximum amount of time, in milliseconds, that the client waits
     *        for the server's response.
     * @throws DdeException
     */
    public void stopAdviseLoop(DdeItem item, long timeout) throws DdeException
    {
        invokeHelperMethod("stopAdviseLoop", new Object[] {item, new Long(timeout)});
    }

    /**
     * Abandons the specified asynchronous transaction and releases all transaction resources,
     * so that the event handler will not receive notification when the transaction is processed.
     *
     * @param transactionID is a transaction identifier.
     * @throws DdeException
     */
    public void abandonTransaction(long transactionID) throws DdeException
    {
        invokeHelperMethod("abandonTransaction", new Object[] {new Long(transactionID)});
    }

    /**
     * Returns the service name which the client is connected to.
     *
     * @return the service name which the client is connected to.
     */
    public String getServiceName()
    {
        return _service;
    }

    /**
     * Returns the topic name which the client is connected to.
     *
     * @return the topic name which the client is connected to.
     */
    public String getTopicName()
    {
        return _topic;
    }

    /**
     * Specifies the handler of the client events.
     *
     * @param eventHandler
     * @see DdeClientEventHandler
     */
    public void setEventHandler(DdeClientEventHandler eventHandler)
    {
        if (!_ddeClientHelper.isConnected())
        {
            _clientEventHandler = eventHandler;   
        }
        else
        {
            _ddeClientHelper.setEventHandler(eventHandler);
        }
    }

    /**
     * Removes the handler of the client events.
     */ 
    public void removeEventHandler()
    {
        _ddeClientHelper.removeEventHandler();
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

    private Object invokeHelperMethod(String methodName, Object[] params) throws DdeException
    {
        Object result = null;
        try
        {
            result = getMessageLoop().doInvokeMethod(_ddeClientHelper, methodName, params);
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
}

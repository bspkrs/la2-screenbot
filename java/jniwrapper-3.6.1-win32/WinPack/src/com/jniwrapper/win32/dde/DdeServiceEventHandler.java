/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.dde;

/**
 * The interface for processing DDE service events.
 *
 * @author Vladimir Kondrashchenko
 */
public interface DdeServiceEventHandler extends DdeEventHandler
{
    /**
     * Occurs when the value of the item has been changed and it is
     * necessary to notify clients.
     *
     * @param topic is the name of the topic.
     * @param item specifies the item name and data format.
     * @return a new value of the item data or <code>null</code> if the service is unable to process the request.
     */
    public byte[] adviseRequest(String topic, DdeItem item);

    /**
     * Occurs when a client begins an advise loop.
     *
     * @param topic is the name of the topic.
     * @param item specifies the item name and data format.
     * @return <code>true</code> to allow the client to start the advise loop, or <code>false</code> otherwise.
     */
    public boolean adviseStart(String topic, DdeItem item);

    /**
     * Occurs when a client stops an advise loop.
     *
     * @param topic is the name of the topic.
     * @param item specifies the item name.
     */
    public void adviseStop(String topic, DdeItem item);

    /**
     * Occurs when a client makes a command execution request.
     *
     * @param topic is the name of the topic.
     * @param command is the command to be executed.
     * @return the result of the request processing.
     */
    public DdeResponse execute(String topic, String command);

    /**
     * Occurs when a client attempts to establish connection with the service.
     *
     * @param topic is the name of the topic.
     * @param sameApplication specifies if the client and the service are the same application.
     * @return <code>true</code> to allow the client to connect, or <code>false</code> otherwise.
     */
    public boolean beforeConnect(String topic, boolean sameApplication);

    /**
     * Occurs when a client sends data to the service.
     *
     * @param topic is the name of the topic.
     * @param item specifies the item name and data format.
     * @param data is the sent data.
     * @return the result of the event processing.
     */
    public DdeResponse pokeData(String topic, DdeItem item, byte[] data);

    /**
     * Occurs when a client makes a request for data.
     *
     * @param topic is the name of the topic.
     * @param item specifies the item name and data format.
     * @return a new value of the item data or <code>null</code> if the service is unable to process the request.
     */
    public byte[] requestData(String topic, DdeItem item);
}

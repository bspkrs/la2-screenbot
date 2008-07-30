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
 * This is an interface for processing DDE client events.
 *
 * @author Vladimir Kondrashchenko
 */
public interface DdeClientEventHandler extends DdeEventHandler
{
    /**
     * Fires when a critical error occurs.
     *
     * @param errorCode is the error code.
     */
    public void error(int errorCode);

    /**
     * Occurs when data of the item is changed.
     *
     * @param item specifies the item name and data format.
     * @param data if on starting an advise loop the <code>sendData</code> argument was set to <code>true</code>,
     *             then the <code>data</code> contains a new value of the item data; otherwise, this argument is
     *             <code>null</code>.
     * @return the result of event processing.
     * @see DdeItem
     * @see DdeResponse
     */
    public DdeResponse itemChanged(DdeItem item,  byte[] data);

    /**
     * Occurs when an asynchronous transaction is processed by the service.
     *
     * @param item specifies the item name and data format.
     * @param data if the transaction manipulates data, then the <code>data</code> contains the value of
     *             the item. If the transaction failed, the <code>data</code> is <code>null</code>.
     * @param transactionID is an asyncronous transaction identifier.
     */
    public void asyncActionComplete(DdeItem item, byte[] data, long transactionID);
}

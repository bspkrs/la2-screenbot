/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.dde;

import com.jniwrapper.*;
import com.jniwrapper.win32.Handle;
import com.jniwrapper.win32.ui.User32;

/**
 * @author Vladimir Kondrashchenko
 */
class DdeClientHelper
{
    private static final String FUNCTION_CONNECT = "DdeConnect";
    private static final String FUNCTION_DISCONNECT = "DdeDisconnect";
    private static final String FUNCTION_CLIENTTRANSACTION = "DdeClientTransaction";
    private static final String FUNCTION_ABANDONTRANSACTION = "DdeAbandonTransaction";

    private static final long TIMEOUT_ASYNC = 0xFFFFFFFFL;
    private static final long XTYPF_NODATA = 0x0004;
    private static final long XTYPF_ACKREQ = 0x0008;

    private long _idInst;
    private boolean _connected;

    private DdeCallback _callback;
    private Handle _conv;

    public DdeClientHelper()
    {
        _connected = false;
    }

    public void createCallback()
    {
        _callback = new DdeCallback();
    }

    public void connect(String service, String topic) throws DdeException
    {
        if (!_connected)
        {
            _idInst = DdeFunctions.ddeInitialize(_callback);
            _callback.setIdInst(_idInst);
            _connected = true;
        }
        _conv = ddeConnect(_idInst, service, topic);
    }

    public void disconnect() throws DdeException
    {
        if (_connected)
        {
            ddeDisconnect(_idInst, _conv);
            DdeFunctions.ddeUninitialize(_idInst);
            _connected = false;
        }
    }

    public Long getAsync(DdeItem item) throws DdeException
    {
        Handle hItem = DdeFunctions.createStringHandle(_idInst, item.getName());
        Handle result = clientTransaction(_idInst, new Handle(), _conv, hItem,
                DdeCallback.XTYP_REQUEST, item.getFormat(), TIMEOUT_ASYNC);
        DdeFunctions.freeStringHandle(_idInst, hItem);
        return new Long(result.getValue());
    }

    public byte[] get(DdeItem item, Long timeout) throws DdeException
    {
        Handle hItem = DdeFunctions.createStringHandle(_idInst, item.getName());
        Handle data = clientTransaction(_idInst, new Handle(), _conv, hItem,
                DdeCallback.XTYP_REQUEST, item.getFormat(), timeout.longValue());
        byte[] resData = DdeFunctions.getData(data);
        DdeFunctions.freeStringHandle(_idInst, hItem);
        DdeFunctions.freeDataHandle(_idInst, data);
        byte[] result = resData;
        return result;
    }

    public Long sendAsync(DdeItem item, byte[] data) throws DdeException
    {
        Handle hItem = DdeFunctions.createStringHandle(_idInst, item.getName());
        Handle hData = DdeFunctions.createDataHandle(_idInst, data, 0, item.getFormat(), hItem);
        Handle result = clientTransaction(_idInst, hData, _conv, hItem,
                DdeCallback.XTYP_POKE, item.getFormat(), TIMEOUT_ASYNC);
        DdeFunctions.freeStringHandle(_idInst, hItem);
        DdeFunctions.freeDataHandle(_idInst, hData);
        return new Long(result.getValue());
    }

    public void send(DdeItem item, byte[] data, Long timeout) throws DdeException
    {
        Handle hItem = DdeFunctions.createStringHandle(_idInst, item.getName());
        Handle hData = DdeFunctions.createDataHandle(_idInst, data, 0, item.getFormat(), hItem);
        clientTransaction(_idInst, hData, _conv, hItem, DdeCallback.XTYP_POKE,
                item.getFormat(), timeout.longValue());
        DdeFunctions.freeStringHandle(_idInst, hItem);
        DdeFunctions.freeDataHandle(_idInst, hData);
    }

    public Long executeAsync(String command) throws DdeException
    {
        Handle result =  clientTransaction(_idInst, command, _conv, new Handle(),
                DdeCallback.XTYP_EXECUTE, DdeItem.CF_TEXT, TIMEOUT_ASYNC);
        return new Long(result.getValue());
    }

    public void execute(String command, Long timeout) throws DdeException
    {
        clientTransaction(_idInst, command, _conv, new Handle(), DdeCallback.XTYP_EXECUTE,
                DdeItem.CF_TEXT, timeout.longValue());
    }

    public Long startAdviseLoopAsync(DdeItem item, Boolean sendData, Boolean sync) throws DdeException
    {
        Handle hItem = DdeFunctions.createStringHandle(_idInst, item.getName());
        long flag = DdeCallback.XTYP_ADVSTART;
        if (!sendData.booleanValue())
        {
            flag |= XTYPF_NODATA;
        }
        if (sync.booleanValue())
        {
            flag |= XTYPF_ACKREQ;
        }
        Handle result = clientTransaction(_idInst, new Handle(), _conv,
                hItem, flag, item.getFormat(), TIMEOUT_ASYNC);
        DdeFunctions.freeStringHandle(_idInst, hItem);
        return new Long(result.getValue());
    }

    public void startAdviseLoop(DdeItem item, Boolean sendData, Boolean sync, Long timeout) throws DdeException
    {
        Handle hItem = DdeFunctions.createStringHandle(_idInst, item.getName());
        long flag = DdeCallback.XTYP_ADVSTART;
        if (!sendData.booleanValue())
        {
            flag |= XTYPF_NODATA;
        }
        if (sync.booleanValue())
        {
            flag |= XTYPF_ACKREQ;
        }
        clientTransaction(_idInst, new Handle(), _conv, hItem, flag,
                item.getFormat(), timeout.longValue());
        DdeFunctions.freeStringHandle(_idInst, hItem);
    }

    public Long stopAdviseLoopAsync(DdeItem item) throws DdeException
    {
        Handle hItem = DdeFunctions.createStringHandle(_idInst, item.getName());
        Handle result = clientTransaction(_idInst, new Handle(), _conv, hItem,
                DdeCallback.XTYP_ADVSTOP, 0, TIMEOUT_ASYNC);
        DdeFunctions.freeStringHandle(_idInst, hItem);
        return new Long(result.getValue());
    }

    public void stopAdviseLoop(DdeItem item, Long timeout) throws DdeException
    {
        Handle hItem = DdeFunctions.createStringHandle(_idInst, item.getName());
        clientTransaction(_idInst, new Handle(), _conv,
                hItem, DdeCallback.XTYP_ADVSTOP, 0, timeout.longValue());
        DdeFunctions.freeStringHandle(_idInst, hItem);
     }

    public void abandonTransaction(Long transactionID) throws DdeException
    {
        ddeAbandonTransaction(_idInst, _conv, transactionID.longValue());
    }

    private Handle ddeConnect(long idInst, String service, String topic) throws DdeException
    {
        Function function = User32.getInstance().getFunction(FUNCTION_CONNECT);

        Handle result = new Handle();
        Handle hService = DdeFunctions.createStringHandle(idInst, service);
        Handle hTopic = DdeFunctions.createStringHandle(idInst, topic);
        function.invoke(result, new UInt32(idInst), hService, hTopic, new Pointer(null, true));
        DdeFunctions.freeStringHandle(idInst, hService);
        DdeFunctions.freeStringHandle(idInst, hTopic);
        if (result.getValue() == 0)
        {
            throw new DdeException(DdeFunctions.getLastError(idInst));
        }

        return result;
    }

    private void ddeDisconnect(long idInst, Handle conv) throws DdeException
    {
        Function function = User32.getInstance().getFunction(FUNCTION_DISCONNECT);

        IntBool result = new IntBool();
        function.invoke(result, conv);

        if (result.getValue() == 0)
        {
            throw new DdeException(DdeFunctions.getLastError(idInst));
        }
    }


    private void ddeAbandonTransaction(long idInst, Handle conv, long transactionID) throws DdeException
    {
        Function function = User32.getInstance().getFunction(FUNCTION_ABANDONTRANSACTION);

        IntBool result = new IntBool();
        function.invoke(result, new UInt32(idInst), conv, new UInt32(transactionID));

        if (result.getValue() == 0)
        {
            throw new DdeException(DdeFunctions.getLastError(idInst));
        }
    }

    private Handle clientTransaction(long idInst, Handle data, Handle conv, Handle item,
                                           long type, long format, long timeout) throws DdeException
    {
        return clientTransaction(idInst, (Parameter) data, conv, item, type, format, timeout);
    }

    private Handle clientTransaction(long idInst, String data, Handle conv, Handle item,
                                           long type, long format, long timeout) throws DdeException
    {
        return clientTransaction(idInst, new Str(data), conv, item, type, format, timeout);
    }

    private Handle clientTransaction(long idInst, Parameter data, Handle conv, Handle item,
                                           long type, long format, long timeout) throws DdeException
    {
        Function function = User32.getInstance().getFunction(FUNCTION_CLIENTTRANSACTION);

        Handle retVal = new Handle();
        UInt32 result = new UInt32();
        function.invoke(retVal, new Parameter[]{
            data,
            data instanceof Str ? new UInt32(((Str) data).getLength()) : new UInt32(-1),
            conv,
            item,
            new UInt(format),
            new UInt(type),
            new UInt32(timeout),
            new Pointer(result)});

        if (retVal.getValue() == 0)
        {
            throw new DdeException(DdeFunctions.getLastError(idInst));
        }

        if (timeout == TIMEOUT_ASYNC)
        {
            return new Handle(result.getValue());
        }
        else
        {
            return retVal;
        }
    }

    public boolean isConnected()
    {
        return _connected;
    }

    public void setEventHandler(DdeClientEventHandler eventHandler)
    {
        _callback.addClientEventHandler(_conv, eventHandler);
    }

    public void removeEventHandler()
    {
        _callback.removeClientEventHandler(_conv);
    }
}

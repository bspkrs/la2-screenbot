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
import com.jniwrapper.util.Logger;
import com.jniwrapper.win32.Handle;
import com.jniwrapper.win32.FunctionName;
import com.jniwrapper.win32.ui.User32;

import java.util.*;
import java.text.StringCharacterIterator;

/**
 * @author Vladimir Kondrashchenko
 */
class DdeCallback extends Callback
{
    private static final Logger _log = Logger.getInstance(DdeCallback.class);

    private static final FunctionName FUNCTION_QUERYSTRING = new FunctionName("DdeQueryString");

    private static final int XTYPF_NOBLOCK = 0x0002;
    private static final int XCLASS_BOOL= 0x1000;
    private static final int XCLASS_DATA = 0x2000;
    private static final int XCLASS_FLAGS = 0x4000;
    private static final int XCLASS_NOTIFICATION = 0x8000;

    public static final int XTYP_ERROR = (0x0000 | XCLASS_NOTIFICATION | XTYPF_NOBLOCK );
    public static final int XTYP_ADVDATA = (0x0010 | XCLASS_FLAGS);
    public static final int XTYP_ADVREQ = (0x0020 | XCLASS_DATA | XTYPF_NOBLOCK );
    public static final int XTYP_ADVSTART = (0x0030 | XCLASS_BOOL);
    public static final int XTYP_ADVSTOP = (0x0040 | XCLASS_NOTIFICATION);
    public static final int XTYP_EXECUTE = (0x0050 | XCLASS_FLAGS);
    public static final int XTYP_CONNECT = (0x0060 | XCLASS_BOOL | XTYPF_NOBLOCK);
    public static final int XTYP_CONNECT_CONFIRM = (0x0070 | XCLASS_NOTIFICATION | XTYPF_NOBLOCK);
    public static final int XTYP_XACT_COMPLETE = (0x0080 | XCLASS_NOTIFICATION);
    public static final int XTYP_POKE = (0x0090 | XCLASS_FLAGS);
    public static final int XTYP_REGISTER = (0x00A0 | XCLASS_NOTIFICATION | XTYPF_NOBLOCK);
    public static final int XTYP_REQUEST = (0x00B0 | XCLASS_DATA);
    public static final int XTYP_DISCONNECT = (0x00C0 | XCLASS_NOTIFICATION | XTYPF_NOBLOCK);
    public static final int XTYP_UNREGISTER = (0x00D0 | XCLASS_NOTIFICATION | XTYPF_NOBLOCK);
    public static final int XTYP_WILDCONNECT = (0x00E0 | XCLASS_DATA | XTYPF_NOBLOCK);

    private UInt _type = new UInt();
    private UInt _fmt = new UInt();
    private Handle _conv = new Handle();
    private Handle _sz1 = new Handle();
    private Handle _sz2 = new Handle();
    private Handle _data = new Handle();
    private Handle _data1 = new Handle();
    private Handle _data2 = new Handle();
    private Handle _result = new Handle();

    private Map _clientHandlers = new Hashtable();
    private Map _serverHandlers = new Hashtable();
    private Map _convToServer = new Hashtable();

    private long _idInst;

    public DdeCallback()
    {
        init(new Parameter[] {_type, _fmt, _conv, _sz1, _sz2, _data, _data1, _data2}, _result);
    }

    public void setIdInst(long idInst)
    {
        _idInst = idInst;
    }

    public void callback()
    {
        try
        {
            switch ((int)_type.getValue())
            {
                case XTYP_ERROR :
                    processError();
                    break;

                case XTYP_ADVDATA :
                    processAdvData();
                    break;

                case XTYP_ADVREQ :
                    processAdvReq();
                    break;

                case XTYP_ADVSTART :
                    processAdvStart();
                    break;

                case XTYP_ADVSTOP :
                    processAdvStop();
                    break;

                case XTYP_EXECUTE :
                    processExecute();
                    break;

                case XTYP_CONNECT :
                    processConnect();
                    break;

                case XTYP_CONNECT_CONFIRM :
                    String service = queryString(_idInst, _sz2);
                    _convToServer.put(_conv, service);
                    break;

                case XTYP_XACT_COMPLETE :
                    processActComplete();
                    break;

                case XTYP_POKE :
                    processPoke();
                    break;

                case XTYP_REGISTER :
                    processRegister();
                    break;

                case XTYP_REQUEST :
                    processRequest();
                    break;

                case XTYP_DISCONNECT :
                    processDisconnect();
                    break;

                case XTYP_UNREGISTER :
                    processUnregister();
                    break;

                case XTYP_WILDCONNECT :
                    break;
            }
        }
        catch (Exception e)
        {
            _log.error("", e);
        }
    }

    private void processError()
    {
        DdeClientEventHandler clientEventHandler = getClientHandler(_conv);
        if (clientEventHandler != null)
        {
            clientEventHandler.error((int) _data1.getValue());
        }
    }

    private void processAdvData() throws DdeException
    {
        DdeClientEventHandler clientEventHandler = getClientHandler(_conv);
        DdeResponse response = DdeResponse.NOTPROCESSED;
        if (clientEventHandler != null)
        {
            String item = queryString(_idInst, _sz2);
            byte[] data;
            if (_data.getValue() != 0)
            {
                data = DdeFunctions.getData(_data);
            }
            else
            {
                data = null;
            }
            response = clientEventHandler.itemChanged(new DdeItem(item, _fmt.getValue()), data);
            if (response == null)
            {
                response = DdeResponse.NOTPROCESSED;
            }
        }
        _result.setValue(response.getValue());
    }

    private void processAdvReq() throws DdeException
    {
        DdeServiceEventHandler serviceEventHandler = getServiceHandler(_conv);
        byte[] data = null;
        Handle hData;
        if (serviceEventHandler != null)
        {
            String topic = queryString(_idInst, _sz1);
            String item = queryString(_idInst, _sz2);
            data = serviceEventHandler.adviseRequest(topic, new DdeItem(item, _fmt.getValue()));
        }
        if (data != null)
        {
            hData = DdeFunctions.createDataHandle(_idInst, data, 0, _fmt.getValue(), _sz2);
        }
        else
        {
            hData = new Handle();
        }
        _result.setValue(hData.getValue());
    }

    private void processAdvStart() throws DdeException
    {
        DdeServiceEventHandler serviceEventHandler = getServiceHandler(_conv);
        boolean result = true;
        if (serviceEventHandler != null)
        {
            String topic = queryString(_idInst, _sz1);
            String item = queryString(_idInst, _sz2);
            result = serviceEventHandler.adviseStart(topic, new DdeItem(item, _fmt.getValue()));
        }
        if (result)
        {
            _result.setValue(1);
        }
        else
        {
            _result.setValue(0);
        }
    }

    private void processAdvStop() throws DdeException
    {
        DdeServiceEventHandler serviceEventHandler = getServiceHandler(_conv);
        if (serviceEventHandler != null)
        {
            String topic = queryString(_idInst, _sz1);
            String item = queryString(_idInst, _sz2);
            serviceEventHandler.adviseStop(topic, new DdeItem(item, 0));
        }
    }

    private void processExecute() throws DdeException
    {
        DdeServiceEventHandler serviceEventHandler = getServiceHandler(_conv);
        DdeResponse response = DdeResponse.NOTPROCESSED;
        if (serviceEventHandler != null)
        {
            String topic = queryString(_idInst, _sz1);
            byte[] data = DdeFunctions.getData(_data);
            StringCharacterIterator commandIterator = new StringCharacterIterator(new String(data));
            StringBuffer command = new StringBuffer();
            do
            {
                if (commandIterator.current() != '\0')
                {
                    command.append(commandIterator.current());
                }
            } while (commandIterator.next() != '\uFFFF');
            //command = command.replaceAll("\0", "");
            response = serviceEventHandler.execute(topic, command.toString());
            if (response == null)
            {
                response = DdeResponse.NOTPROCESSED;
            }
        }
        _result.setValue(response.getValue());
    }

    private void processConnect() throws DdeException
    {
        String service = queryString(_idInst, _sz2);
        DdeServiceEventHandler serviceEventHandler = (DdeServiceEventHandler) _serverHandlers.get(service);
        boolean result = true;
        if (serviceEventHandler != null)
        {
            String topic = queryString(_idInst, _sz1);
            boolean sameApplication = false;
            if (_data2.getValue() == 1)
            {
                sameApplication = true;
            }
            result = serviceEventHandler.beforeConnect(topic, sameApplication);
        }
        if (result)
        {
            _result.setValue(1);
        }
        else
        {
            _result.setValue(0);
        }
    }

    private void processActComplete() throws DdeException
    {
        DdeClientEventHandler clientEventHandler = getClientHandler(_conv);
        if (clientEventHandler != null)
        {
            byte[] data;
            if (_data.getValue() == 0)
            {
                data = null;
            }
            else if (_data.getValue() == 1)
            {
                data = new byte[0];
            }
            else
            {
                data = DdeFunctions.getData(_data);
            }

            String item = queryString(_idInst, _sz2);
            clientEventHandler.asyncActionComplete(new DdeItem(item, _fmt.getValue()),
                    data, _data1.getValue());
        }
    }

    private void processPoke() throws DdeException
    {
        DdeServiceEventHandler serviceEventHandler = getServiceHandler(_conv);
        DdeResponse response = DdeResponse.NOTPROCESSED;
        if (serviceEventHandler != null)
        {
            String topic = queryString(_idInst, _sz1);
            String item = queryString(_idInst, _sz2);
            byte[] data = DdeFunctions.getData(_data);
            response = serviceEventHandler.pokeData(topic, new DdeItem(item, _fmt.getValue()), data);
            if (response == null)
            {
                response = DdeResponse.NOTPROCESSED;
            }
        }
        _result.setValue(response.getValue());
    }

    private void processRegister() throws DdeException
    {
        DdeServiceEventHandler serviceEventHandler = getServiceHandler(_conv);
        String service = queryString(_idInst, _sz1);
        String instanceName = queryString(_idInst, _sz2);
        Collection values = _serverHandlers.values();
        for (Iterator i = values.iterator(); i.hasNext();)
        {
            serviceEventHandler = (DdeServiceEventHandler) i.next();
            serviceEventHandler.serviceRegister(service, instanceName);
        }
        values = _clientHandlers.values();
        DdeClientEventHandler clientEventHandler;
        for (Iterator i = values.iterator(); i.hasNext();)
        {
            clientEventHandler = (DdeClientEventHandler) i.next();
            clientEventHandler.serviceRegister(service, instanceName);
        }
    }

    private void processRequest() throws DdeException
    {
        DdeServiceEventHandler serviceEventHandler = getServiceHandler(_conv);
        byte[] data = null;
        if (serviceEventHandler != null)
        {
            String topic = queryString(_idInst, _sz1);
            String item = queryString(_idInst, _sz2);
            data = serviceEventHandler.requestData(topic, new DdeItem(item, _fmt.getValue()));
        }
        Handle hData;
        if (data != null)
        {
            hData = DdeFunctions.createDataHandle(_idInst, data, 0, _fmt.getValue(), _sz2);
        }
        else
        {
            hData = new Handle();
        }
        _result.setValue(hData.getValue());
    }

    private void processDisconnect()
    {
        DdeServiceEventHandler serviceEventHandler = getServiceHandler(_conv);
        if (serviceEventHandler != null)
        {
            boolean sameApplication = false;
            if (_data2.getValue() == 1)
            {
                sameApplication = true;
            }
            serviceEventHandler.disconnect(sameApplication);
        }
        DdeClientEventHandler clientEventHandler = getClientHandler(_conv);
        if (clientEventHandler != null)
        {
            boolean sameApplication = false;
            if (_data2.getValue() == 1)
            {
                sameApplication = true;
            }
            clientEventHandler.disconnect(sameApplication);
        }
    }

    private void processUnregister() throws DdeException
    {
        DdeServiceEventHandler serviceEventHandler = getServiceHandler(_conv);
        String service = queryString(_idInst, _sz1);
        String instanceName = queryString(_idInst, _sz2);
        Collection values = _serverHandlers.values();
        for (Iterator i = values.iterator(); i.hasNext();)
        {
            serviceEventHandler = (DdeServiceEventHandler) i.next();
            serviceEventHandler.serviceUnregister(service, instanceName);
        }
        values = _clientHandlers.values();
        DdeClientEventHandler clientEventHandler;
        for (Iterator i = values.iterator(); i.hasNext();)
        {
            clientEventHandler = (DdeClientEventHandler) i.next();
            clientEventHandler.serviceUnregister(service, instanceName);
        }
    }

   private String queryString(long idInst, Handle handle) throws DdeException
    {
        Function function = User32.getInstance().getFunction(FUNCTION_QUERYSTRING.toString());

        UInt32 result = new UInt32();
        Str str = new Str(255);
        function.invoke(result, new Parameter[]{
            new UInt32(idInst),
            handle,
            new Pointer(str),
            new UInt32(255), new Int(DdeFunctions.getCodePage())});

        if (result.getValue() == 0)
        {
            throw new DdeException(DdeFunctions.getLastError(idInst));
        }

        return str.getValue();
    }

    public void addClientEventHandler(Handle conv, DdeClientEventHandler eventHandler)
    {
        _clientHandlers.put(conv, eventHandler);
    }

    public void removeClientEventHandler(Handle conv)
    {
        _clientHandlers.remove(conv);
    }

    public void addServerEventHandler(String server, DdeServiceEventHandler eventHandler)
    {
        _serverHandlers.put(server, eventHandler);
    }

    public void removeServerEventHandler(String server)
    {
        _serverHandlers.remove(server);
    }

    private DdeClientEventHandler getClientHandler(Handle conv)
    {
        Object res = _clientHandlers.get(conv);
        if (res != null)
        {
            return (DdeClientEventHandler) res;
        }
        else
        {
            return null;
        }
    }

    private DdeServiceEventHandler getServiceHandler(Handle conv)
    {
        String server = (String) _convToServer.get(conv);
        if (server == null)
        {
            return null;
        }
        else
        {
            return (DdeServiceEventHandler) _serverHandlers.get(server);
        }
    }
}

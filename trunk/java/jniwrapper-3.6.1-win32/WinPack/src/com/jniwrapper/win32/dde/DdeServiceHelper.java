/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.dde;

import com.jniwrapper.Function;
import com.jniwrapper.IntBool;
import com.jniwrapper.UInt;
import com.jniwrapper.UInt32;
import com.jniwrapper.win32.Handle;
import com.jniwrapper.win32.system.EventObject;
import com.jniwrapper.win32.ui.User32;

/**
 * @author Vladimir Kondrashchenko
 */
class DdeServiceHelper
{
    private static final String FUNCTION_NAMESERVICE = "DdeNameService";
    private static final String FUNCTION_POSTADVISE = "DdePostAdvise";

    private static final long DNS_REGISTER = 0x0001;
    private static final long DNS_UNREGISTER = 0x0002;

    private String _name;
    private boolean _registered;
    private long _idInst;
    private DdeCallback _callback;

    public DdeServiceHelper(String name)
    {
        _name = name;
    }

    public void createCallback()
    {
        _callback = new DdeCallback();
    }

    public void register(String eventName) throws DdeException
    {
         if (!_registered)
        {
            _idInst = DdeFunctions.ddeInitialize(_callback);
            _callback.setIdInst(_idInst);
            Handle name = DdeFunctions.createStringHandle(_idInst, _name);
            ddeNameService(_idInst, name, DNS_REGISTER);
            _registered = true;
            if (eventName != null)
            {
                EventObject notifyEvent = new EventObject(eventName);
                if (!notifyEvent.isNull())
                {
                    notifyEvent.notifyEvent();
                    notifyEvent.close();
                }
            }
            DdeFunctions.freeStringHandle(_idInst, name);
        }
    }

    private void ddeNameService(long idInst, Handle service, long flag) throws DdeException
    {
        Function function = User32.getInstance().getFunction(FUNCTION_NAMESERVICE);

        Handle result = new Handle();
        function.invoke(result, new UInt32(idInst), service, new UInt(0), new UInt(flag));

        if (result.getValue() == 0)
        {
            throw new DdeException(DdeFunctions.getLastError(idInst));
        }
    }

    public boolean isRegistered()
    {
        return _registered;
    }

    public void unregister() throws DdeException
    {
        if (_registered)
        {
            Handle name = DdeFunctions.createStringHandle(_idInst, _name);
            ddeNameService(_idInst, name, DNS_UNREGISTER);
            DdeFunctions.freeStringHandle(_idInst, name);
            DdeFunctions.ddeUninitialize(_idInst);
            _registered = false;
        }
    }

    public void setEventHandler(DdeServiceEventHandler eventHandler)
    {
        _callback.addServerEventHandler(_name, eventHandler);
    }

    public void removeEventHandler()
    {
        _callback.removeServerEventHandler(_name);
    }

    public String getName()
    {
        return _name;
    }

    public void postAdvise(String topic, String item) throws DdeException
    {
        ddePostAdvise(_idInst, topic, item);
    }


    private void ddePostAdvise(long idInst, String topic, String item) throws DdeException
    {
        Function function = User32.getInstance().getFunction(FUNCTION_POSTADVISE);

        IntBool result = new IntBool();
        Handle hTopic = DdeFunctions.createStringHandle(idInst, topic);
        Handle hItem = DdeFunctions.createStringHandle(idInst, item);
        function.invoke(result, new UInt32(idInst), hTopic, hItem);

        if (result.getValue() == 0)
        {
            throw new DdeException(DdeFunctions.getLastError(idInst));
        }
    }
}

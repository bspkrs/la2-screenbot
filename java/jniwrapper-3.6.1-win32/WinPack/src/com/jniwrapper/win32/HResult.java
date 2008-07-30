/*
 * Copyright 2000-2004 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */
package com.jniwrapper.win32;

import com.jniwrapper.Int32;
import com.jniwrapper.IntegerParameter;
import com.jniwrapper.util.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * The HResult class represents the HRESULT data type and is used to describe errors.
 */
public class HResult extends Int32
{
    public static final int S_OK = 0;
    public static final int S_FALSE = 1;
    public static final int E_NOTIMPL = 0x80004001;
    public static final int E_OUTOFMEMORY = 0x8007000e;
    public static final int E_INVALIDARG = 0x80070057;
    public static final int E_NOINTERFACE = 0x80004002;
    public static final int E_POINTER = 0x80004003;
    public static final int E_HANDLE = 0x80070006;
    public static final int E_ABORT = 0x80004004;
    public static final int E_FAIL = 0x80004005;
    public static final int E_UNEXPECTED = 0x8000FFFF;
    public static final int E_ACCESSDENIED = 0x80070005;

    public static final int CLASS_E_NOAGGREGATION = 0x80040110;
    public static final int CLASS_E_CLASSNOTAVAILABLE = 0x80040111;
    public static final int CLASS_E_NOTLICENSED = 0x80040112;

    public static final int CONNECT_E_FIRST = 0x80040200;
    public static final int CONNECT_E_NOCONNECTION = 0x80040201;
    public static final int CONNECT_E_ADVISELIMIT = 0x80040202;
    public static final int CONNECT_E_CANNOTCONNECT = 0x80040203;
    public static final int CONNECT_E_OVERRIDDEN = 0x80040204;

    public static final int SELFREG_E_TYPELIB = 0x80040200;
    public static final int SELFREG_E_CLASS = 0x80040201;

    public static final int PERPROP_E_NOPAGEAVAILABLE = 0x80040200;

    public static final int INET_E_INVALID_URL = 0x800c0002;
    public static final int INET_E_NO_SESSION = 0x800c0003;
    public static final int INET_E_CANNOT_CONNECT = 0x800c0004;
    public static final int INET_E_RESOURCE_NOT_FOUND = 0x800c0005;
    public static final int INET_E_OBJECT_NOT_FOUND = 0x800c0006;
    public static final int INET_E_DATA_NOT_AVAILABLE = 0x800c0007;
    public static final int INET_E_DOWNLOAD_FAILURE = 0x800c0008;
    public static final int INET_E_AUTHENTICATION_REQUIRED = 0x800c0009;
    public static final int INET_E_NO_VALID_MEDIA = 0x800c000a;
    public static final int INET_E_CONNECTION_TIMEOUT = 0x800c000b;
    public static final int INET_E_INVALID_REQUEST = 0x800c000c;
    public static final int INET_E_UNKNOWN_PROTOCOL = 0x800c000d;
    public static final int INET_E_SECURITY_PROBLEM = 0x800c000e;
    public static final int INET_E_CANNOT_LOAD_DATA = 0x800c000f;
    public static final int INET_E_CANNOT_INSTANTIATE_OBJECT = 0x800c0010;
    public static final int INET_E_USE_DEFAULT_PROTOCOLHANDLER = 0x800c0011;
    public static final int INET_E_DEFAULT_ACTION = 0x800c0011;
    public static final int INET_E_USE_DEFAULT_SETTING = 0x800c0012;
    public static final int INET_E_QUERYOPTION_UNKNOWN = 0x800c0013;
    public static final int INET_E_REDIRECTING = 0x800c0014;
    public static final int INET_E_REDIRECT_FAILED = 0x800c0014;
    public static final int INET_E_REDIRECT_TO_DIR = 0x800c0015;
    public static final int INET_E_CANNOT_LOCK_REQUEST = 0x800c0016;
    public static final int INET_E_USE_EXTEND_BINDING = 0x800c0017;
    public static final int INET_E_ERROR_FIRST = 0x800c0002;
    public static final int INET_E_ERROR_LAST = 0x800c0017;
    public static final int INET_E_CODE_DOWNLOAD_DECLINED = 0x800c0100;
    public static final int INET_E_RESULT_DISPATCHED = 0x800c0200;
    public static final int INET_E_CANNOT_REPLACE_SFP_FILE = 0x800c0300;

    public static final int DISP_E_UNKNOWNINTERFACE = 0x80020001;
    public static final int DISP_E_MEMBERNOTFOUND = 0x80020003;
    public static final int DISP_E_PARAMNOTFOUND = 0x80020004;
    public static final int DISP_E_TYPEMISMATCH = 0x80020005;
    public static final int DISP_E_UNKNOWNNAME = 0x80020006;
    public static final int DISP_E_NONAMEDARGS = 0x80020007;
    public static final int DISP_E_BADVARTYPE = 0x80020008;
    public static final int DISP_E_EXCEPTION = 0x80020009;
    public static final int DISP_E_OVERFLOW = 0x8002000A;
    public static final int DISP_E_BADINDEX = 0x8002000B;
    public static final int DISP_E_UNKNOWNLCID = 0x8002000C;
    public static final int DISP_E_ARRAYISLOCKED = 0x8002000D;
    public static final int DISP_E_BADPARAMCOUNT = 0x8002000E;
    public static final int DISP_E_PARAMNOTOPTIONAL = 0x8002000F;
    public static final int DISP_E_BADCALLEE = 0x80020010;
    public static final int DISP_E_NOTACOLLECTION = 0x80020011;
    public static final int DISP_E_DIVBYZERO = 0x80020012;

    public static final int CO_E_NOTINITIALIZED = 0x800401F0;
    public static final int CO_E_ALREADYINITIALIZED = 0x800401F1;
    public static final int CO_E_CANTDETERMINECLASS = 0x800401F2;
    public static final int CO_E_CLASSSTRING = 0x800401F3;
    public static final int CO_E_IIDSTRING = 0x800401F4;
    public static final int CO_E_APPNOTFOUND = 0x800401F5;
    public static final int CO_E_APPSINGLEUSE = 0x800401F6;
    public static final int CO_E_ERRORINAPP = 0x800401F7;
    public static final int CO_E_DLLNOTFOUND = 0x800401F8;
    public static final int CO_E_ERRORINDLL = 0x800401F9;
    public static final int CO_E_WRONGOSFORAPP = 0x800401FA;
    public static final int CO_E_OBJNOTREG = 0x800401FB;
    public static final int CO_E_OBJISREG = 0x800401FC;
    public static final int CO_E_OBJNOTCONNECTED = 0x800401FD;
    public static final int CO_E_APPDIDNTREG = 0x800401FE;
    public static final int CO_E_RELEASED = 0x800401FF;

    public static final int RPC_E_DISCONNECTED = 0x80010108;

    private static Map _namesMap = null;

    private static final Logger LOG = Logger.getInstance(HResult.class);

    public HResult()
    {
    }

    public HResult(int value)
    {
        super(value);
    }

    public HResult(IntegerParameter value)
    {
        super(value);
    }

    public HResult(short severity, short facility, boolean isError)
    {
        super((int)
                (((long)severity & 0xffff) |
                (((long)facility & 0x1fff) << 16) |
                ((isError ? 1L : 0L) << 31))
        );
    }

    public short getSCode()
    {
        return (short)(getValue() & 0xffff);
    }

    public short getFacility()
    {
        return (short)((getValue() >> 16) & 0x1fff);
    }

    public boolean isError()
    {
        return (getValue() & 0x80000000L) != 0;
    }

    public Object clone()
    {
        return new HResult(this);
    }

    private static Map getNamesMap()
    {
        if (_namesMap == null)
        {
            try
            {
                _namesMap = new HashMap();
                Field[] declaredFields = HResult.class.getDeclaredFields();
                for (int i = 0; i < declaredFields.length; i++)
                {
                    Field field = declaredFields[i];
                    field.setAccessible(true);
                    if (int.class.equals(field.getType()) && (field.getModifiers() & Modifier.FINAL) != 0)
                    {
                        _namesMap.put(new Integer(field.getInt(null)), field.getName());
                    }
                }
            }
            catch (Exception e)
            {
                LOG.error("", e);
                return Collections.EMPTY_MAP;
            }
        }
        return _namesMap;
    }

    public static String decodeHResult(int hResult)
    {
        String name = (String)getNamesMap().get(new Integer(hResult));
        String desc = (String) LastError.getMessage(hResult);

        if (name != null)
        {
            if (desc != null && !desc.equals(""))
            {
                return name + " (" + desc + ")";
            }
            else
            {
                return name;
            }
        }
        else
        {
            return desc != null ? desc : "";
        }
    }
}

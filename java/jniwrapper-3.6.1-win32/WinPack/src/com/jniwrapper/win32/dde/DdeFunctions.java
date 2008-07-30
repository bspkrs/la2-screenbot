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
import com.jniwrapper.win32.FunctionName;
import com.jniwrapper.win32.Handle;
import com.jniwrapper.win32.ui.User32;

/**
 * @author Vladimir Kondrashchenko
 */
class DdeFunctions
{
    private static final FunctionName FUNCTION_INITIALIZE = new FunctionName("DdeInitialize");
    private static final String FUNCTION_GETLASTERROR = "DdeGetLastError";
    private static final String FUNCTION_UNINITIALIZE = "DdeUninitialize";
    private static final String FUNCTION_CREATEDATAHANDLE = "DdeCreateDataHandle";
    private static final String FUNCTION_GETDATA = "DdeGetData";
    private static final String FUNCTION_FREESTRINGHANDLE = "DdeFreeStringHandle";
    private static final String FUNCTION_FREEDATAHANDLE = "DdeFreeDataHandle";
    private static final FunctionName FUNCTION_CREATESTRINGHANDLE = new FunctionName("DdeCreateStringHandle");

    private static final long APPCLASS_STANDARD = 0x00000000L;

    private static final long CP_WINUNICODE = 1200;
    private static final long CP_WINANSI = 1004;

    private static final long HDATA_APPOWNED = 0x0001;

    public static long getCodePage()
    {
        if (PlatformContext.isUnicode())
        {
            return CP_WINUNICODE;
        }
        else
        {
            return CP_WINANSI;
        }
    }

    public static long ddeInitialize(Callback callback) throws DdeException
    {
        Function function = User32.getInstance().getFunction(FUNCTION_INITIALIZE.toString());

        UInt result = new UInt();
        UInt32 idInst = new UInt32(0);
        function.invoke(result, new Pointer(idInst), callback, new UInt32(APPCLASS_STANDARD), new UInt32(0));
        if (result.getValue() != 0)
        {
            throw new DdeException(getLastError(idInst.getValue()));
        }
        return  idInst.getValue();
    }

    public static void ddeUninitialize(long idInst) throws DdeException
    {
        Function function = User32.getInstance().getFunction(FUNCTION_UNINITIALIZE);

        IntBool result = new IntBool();
        function.invoke(result, new UInt32(idInst));

        if (result.getValue() == 0)
        {
            throw new DdeException(getLastError(idInst));
        }
    }

    public static int getLastError(long idInst)
    {
        Function function = User32.getInstance().getFunction(FUNCTION_GETLASTERROR);

        UInt result = new UInt();
        function.invoke(result, new UInt32(idInst));

        return (int) result.getValue();
    }

    public static Handle createStringHandle(long idInst, String string) throws DdeException
    {
        Function function = User32.getInstance().getFunction(FUNCTION_CREATESTRINGHANDLE.toString());

        Handle result = new Handle();
        function.invoke(result, new UInt32(idInst), new Str(string), new Int(getCodePage()));

        if (result.getValue() == 0)
        {
            throw new DdeException(getLastError(idInst));
        }

        return result;
    }

    public static Handle createDataHandle(long idInst, byte[] data, int offset, long format, Handle item) throws DdeException
    {
        Function function = User32.getInstance().getFunction(FUNCTION_CREATEDATAHANDLE);

        PrimitiveArray paData = new PrimitiveArray(data);
        Handle retVal = new Handle();

        function.invoke(retVal, new Parameter[]{
            new UInt32(idInst),
            new Pointer(paData),
            new UInt32(paData.getElementCount()),
            new UInt32(offset),
            item == null ? new Handle() : item,
            new UInt(format),
            new UInt(HDATA_APPOWNED)});

        if (retVal.getValue() == 0)
        {
            throw new DdeException(getLastError(idInst));
        }

        return retVal;
    }

    public static byte[] getData(Handle data)
    {
        Function function = User32.getInstance().getFunction(FUNCTION_GETDATA);

        UInt32 count = new UInt32();

        function.invoke(count, data, new Pointer(null, true), new UInt32(0), new UInt32(0));

        PrimitiveArray retVal = new PrimitiveArray(UInt8.class, (int)count.getValue());

        function.invoke(count, data, new Pointer(retVal), new UInt32(count.getValue()), new UInt32(0));

        return retVal.getBytes();
    }

    public static void freeStringHandle(long idInst, Handle handle) throws DdeException
    {
        Function function = User32.getInstance().getFunction(FUNCTION_FREESTRINGHANDLE);

        IntBool result = new IntBool();
        function.invoke(result, new UInt32(idInst), handle);

        if (result.getValue() == 0)
        {
            throw new DdeException(getLastError(idInst));
        }
    }

    public static void freeDataHandle(long idInst, Handle handle) throws DdeException
    {
        Function function = User32.getInstance().getFunction(FUNCTION_FREEDATAHANDLE);

        IntBool result = new IntBool();
        function.invoke(result, handle);

        if (result.getValue() == 0)
        {
            throw new DdeException(getLastError(idInst));
        }
    }
}

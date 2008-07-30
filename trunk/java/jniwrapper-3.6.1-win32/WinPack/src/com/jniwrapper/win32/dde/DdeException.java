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
 * @author Vladimir Kondrashchenko
 */
public class DdeException extends Exception
{
    public static final int NO_ERROR = 0;
    public static final int ADVACKTIMEOUT = 0x4000;
    public static final int BUSY = 0x4001;
    public static final int DATAACKTIMEOUT = 0x4002;
    public static final int DLL_NOT_INITIALIZED = 0x4003;
    public static final int DLL_USAGE = 0x4004;
    public static final int EXECACKTIMEOUT = 0x4005;
    public static final int INVALIDPARAMETER = 0x4006;
    public static final int LOW_MEMORY = 0x4007;
    public static final int MEMORY_ERROR = 0x4008;
    public static final int NOTPROCESSED = 0x4009;
    public static final int NO_CONV_ESTABLISHED = 0x400a;
    public static final int POKEACKTIMEOUT = 0x400b;
    public static final int POSTMSG_FAILED = 0x400c;
    public static final int REENTRANCY = 0x400d;
    public static final int SERVER_DIED = 0x400e;
    public static final int SYS_ERROR = 0x400f;
    public static final int UNADVACKTIMEOUT = 0x4010;
    public static final int UNFOUND_QUEUE_ID = 0x4011;

    private int _errorCode;

    /**
     * Creates an instance of the class with the specified error code and error message.
     *
     * @param errorCode
     */
    public DdeException(int errorCode)
    {
        super(getMessage(errorCode));
        _errorCode = errorCode;
    }

    /**
     * Creates an instance of the class with the specified error message.
     *
     * @param message
     */
    public DdeException(String message)
    {
        super(message);
        _errorCode = -1;
    }

    /**
     * Returns the error code.
     *
     * @return the error code.
     */
    public int getErrorCode()
    {
        return _errorCode;
    }

    private static String getMessage(int errorcode)
    {
        String message = null;
        switch (errorcode)
        {
            case NO_ERROR :
                message = "No errors.";
                break;
            case ADVACKTIMEOUT :
                message = "A request for a synchronous advise transaction has timed out.";
                break;
            case BUSY :
                message = "The response to the transaction caused the DDE_FBUSY flag to be set.";
                break;
            case DATAACKTIMEOUT :
                message = "A request for a synchronous data transaction has timed out.";
                break;
            case DLL_NOT_INITIALIZED :
                message = "A DDEML function was called without first calling the DdeInitialize function.";
                break;
            case DLL_USAGE :
                message = "An application has attempted to perform illegal transaction.";
                break;
            case EXECACKTIMEOUT :
                message = "A request for a synchronous execute transaction has timed out.";
                break;
            case INVALIDPARAMETER :
                message = "A parameter failed to be validated by the DDEML.";
                break;
            case LOW_MEMORY :
                message = "Low memory.";
                break;
            case MEMORY_ERROR :
                message = "A memory allocation has failed.";
                break;
            case NOTPROCESSED :
                message = "A transaction has failed.";
                break;
            case NO_CONV_ESTABLISHED :
                message = "A client's attempt to establish a conversation has failed.";
                break;
            case POKEACKTIMEOUT :
                message = "A request for a synchronous poke transaction has timed out.";
                break;
            case POSTMSG_FAILED :
                message = "An internal call to the PostMessage function has failed.";
                break;
            case REENTRANCY :
                message = "A synchronous transaction already in progress.";
                break;
            case SERVER_DIED :
                message = "A server-side transaction was attempted on a conversation terminated by the client.";
                break;
            case SYS_ERROR :
                message = "An internal error has occurred in the DDEML.";
                break;
            case UNADVACKTIMEOUT :
                message = "A request to end an advise transaction has timed out.";
                break;
            case UNFOUND_QUEUE_ID :
                message = "An invalid transaction identifier was passed to a DDEML function.";
                break;
        }
        return message;
    }
}

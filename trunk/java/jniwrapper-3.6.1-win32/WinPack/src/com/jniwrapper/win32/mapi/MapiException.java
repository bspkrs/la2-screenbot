/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.mapi;

/**
 * This class represents all the exceptions thrown by the native MAPISendMail function.
 *
 * @author Alexey Razoryonov
 */
public class MapiException extends Exception
{
    public static final int SUCCESS = 0;
    public static final int MAPI_E_USER_ABORT = 1;
    public static final int MAPI_E_FAILURE = 2;
    public static final int MAPI_E_LOGON_FAILURE = 3;
    public static final int MAPI_E_LOGIN_FAILURE = MAPI_E_LOGON_FAILURE;
    public static final int MAPI_E_INSUFFICIENT_MEMORY = 5;
    public static final int MAPI_E_ACCESS_DENIED = 6;
    public static final int MAPI_E_TOO_MANY_FILES = 9;
    public static final int MAPI_E_TOO_MANY_RECIPIENTS = 10;
    public static final int MAPI_E_ATTACHMENT_NOT_FOUND = 11;
    public static final int MAPI_E_ATTACHMENT_OPEN_FAILURE = 12;
    public static final int MAPI_E_UNKNOWN_RECIPIENT = 14;
    public static final int MAPI_E_BAD_RECIPTYPE = 15;
    public static final int MAPI_E_TEXT_TOO_LARGE = 18;
    public static final int MAPI_E_AMBIGUOUS_RECIPIENT = 21;
    public static final int MAPI_E_AMBIG_RECIP = MAPI_E_AMBIGUOUS_RECIPIENT;
    public static final int MAPI_E_INVALID_RECIPS = 25;
    public static final int MAPI_E_NOT_SUPPORTED = 26;

    private int _errorCode;

    public MapiException(int errorCode)
    {
        super(getMessage(errorCode));
        _errorCode = errorCode;
    }

    public MapiException(String message)
    {
        super(message);
        _errorCode = -1;
    }

    public int getErrorCode()
    {
        return _errorCode;
    }

    public static String getMessage(int errorCode)
    {
        String message = null;
        switch (errorCode)
        {
            case SUCCESS:
                message = "The call succeeded and the message was sent.";
                break;
            case MAPI_E_USER_ABORT:
                message = "The user canceled one of the dialog boxes. No message was sent.";
                break;
            case MAPI_E_FAILURE:
                message = "One or more unspecified errors occurred. No message was sent.";
                break;
            case MAPI_E_LOGON_FAILURE:
                message = "There was no default logon, and the user failed to log on successfully" +
                        " when the logon dialog box was displayed. No message was sent.";
                break;
            case MAPI_E_INSUFFICIENT_MEMORY:
                message = "There was insufficient memory to proceed. No message was sent.";
                break;
            case MAPI_E_TOO_MANY_FILES:
                message = "There were too many file attachments. No message was sent.";
                break;
            case MAPI_E_TOO_MANY_RECIPIENTS:
                message = "There were too many recipients. No message was sent.";
                break;
            case MAPI_E_ATTACHMENT_NOT_FOUND:
                message = "The specified attachment was not found. No message was sent.";
                break;
            case MAPI_E_ATTACHMENT_OPEN_FAILURE:
                message = "The specified attachment could not be opened. No message was sent.";
                break;
            case MAPI_E_UNKNOWN_RECIPIENT:
                message = "A recipient did not appear in the address list. No message was sent.";
                break;
            case MAPI_E_BAD_RECIPTYPE:
                message = "The type of a recipient was not MAPI_TO, MAPI_CC, or MAPI_BCC. No message was sent.";
                break;
            case MAPI_E_TEXT_TOO_LARGE:
                message = "The text in the message was too large. No message was sent.";
                break;
            case MAPI_E_AMBIGUOUS_RECIPIENT:
                message = "A recipient matched more than one of the recipient descriptor" +
                        " structures and MAPI_DIALOG was not set. No message was sent.";
                break;
            case MAPI_E_INVALID_RECIPS:
                message = "One or more recipients were invalid or did not resolve to any address.";
                break;

        }
        return message;
    }
}

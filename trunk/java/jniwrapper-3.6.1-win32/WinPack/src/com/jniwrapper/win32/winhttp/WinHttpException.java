/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.winhttp;

import com.jniwrapper.win32.LastError;

/**
 * Thrown to indicate that an error of some sort has occurred in Win HTTP.
 *
 * @author Vladimir Kondrashchenko
 */
public class WinHttpException extends Exception
{
    private static final int  WINHTTP_ERROR_BASE = 12000;

    public static final int  ERROR_WINHTTP_TIMEOUT = (WINHTTP_ERROR_BASE + 2);
    public static final int  ERROR_WINHTTP_INTERNAL_ERROR = (WINHTTP_ERROR_BASE + 4);
    public static final int  ERROR_WINHTTP_INVALID_URL = (WINHTTP_ERROR_BASE + 5);
    public static final int  ERROR_WINHTTP_UNRECOGNIZED_SCHEME = (WINHTTP_ERROR_BASE + 6);
    public static final int  ERROR_WINHTTP_NAME_NOT_RESOLVED = (WINHTTP_ERROR_BASE + 7);
    public static final int  ERROR_WINHTTP_INVALID_OPTION = (WINHTTP_ERROR_BASE + 9);
    public static final int  ERROR_WINHTTP_OPTION_NOT_SETTABLE = (WINHTTP_ERROR_BASE + 11);
    public static final int  ERROR_WINHTTP_SHUTDOWN = (WINHTTP_ERROR_BASE + 12);

    public static final int  ERROR_WINHTTP_LOGIN_FAILURE = (WINHTTP_ERROR_BASE + 15);
    public static final int  ERROR_WINHTTP_OPERATION_CANCELLED = (WINHTTP_ERROR_BASE + 17);
    public static final int  ERROR_WINHTTP_INCORRECT_HANDLE_TYPE = (WINHTTP_ERROR_BASE + 18);
    public static final int  ERROR_WINHTTP_INCORRECT_HANDLE_STATE = (WINHTTP_ERROR_BASE + 19);
    public static final int  ERROR_WINHTTP_CANNOT_CONNECT = (WINHTTP_ERROR_BASE + 29);
    public static final int  ERROR_WINHTTP_CONNECTION_ERROR = (WINHTTP_ERROR_BASE + 30);
    public static final int  ERROR_WINHTTP_RESEND_REQUEST = (WINHTTP_ERROR_BASE + 32);
    public static final int  ERROR_WINHTTP_CLIENT_AUTH_CERT_NEEDED = (WINHTTP_ERROR_BASE + 44);

    public static final int  ERROR_WINHTTP_HEADER_NOT_FOUND = (WINHTTP_ERROR_BASE + 150);
    public static final int  ERROR_WINHTTP_INVALID_SERVER_RESPONSE = (WINHTTP_ERROR_BASE + 152);
    public static final int  ERROR_WINHTTP_REDIRECT_FAILED = (WINHTTP_ERROR_BASE + 156);

    public static final int  ERROR_WINHTTP_AUTO_PROXY_SERVICE_ERROR = (WINHTTP_ERROR_BASE + 178);
    public static final int  ERROR_WINHTTP_BAD_AUTO_PROXY_SCRIPT = (WINHTTP_ERROR_BASE + 166);
    public static final int  ERROR_WINHTTP_UNABLE_TO_DOWNLOAD_SCRIPT = (WINHTTP_ERROR_BASE + 167);

    public static final int  ERROR_WINHTTP_SECURE_FAILURE = (WINHTTP_ERROR_BASE + 175);

    public static final int  ERROR_WINHTTP_SECURE_CERT_DATE_INVALID = (WINHTTP_ERROR_BASE + 37);
    public static final int  ERROR_WINHTTP_SECURE_CERT_CN_INVALID = (WINHTTP_ERROR_BASE + 38);
    public static final int  ERROR_WINHTTP_SECURE_INVALID_CA = (WINHTTP_ERROR_BASE + 45);
    public static final int  ERROR_WINHTTP_SECURE_CERT_REV_FAILED = (WINHTTP_ERROR_BASE + 57);
    public static final int  ERROR_WINHTTP_SECURE_INVALID_CERT = (WINHTTP_ERROR_BASE + 169);
    public static final int  ERROR_WINHTTP_SECURE_CERT_REVOKED = (WINHTTP_ERROR_BASE + 170);
    public static final int  ERROR_WINHTTP_SECURE_CERT_WRONG_USAGE = (WINHTTP_ERROR_BASE + 179);

    public static final int  ERROR_WINHTTP_AUTODETECTION_FAILED = (WINHTTP_ERROR_BASE + 180);
    public static final int  ERROR_WINHTTP_HEADER_COUNT_EXCEEDED = (WINHTTP_ERROR_BASE + 181);
    public static final int  ERROR_WINHTTP_HEADER_SIZE_OVERFLOW = (WINHTTP_ERROR_BASE + 182);
    public static final int  ERROR_WINHTTP_CHUNKED_ENCODING_HEADER_SIZE_OVERFLOW = (WINHTTP_ERROR_BASE + 183);
    public static final int  ERROR_WINHTTP_RESPONSE_DRAIN_OVERFLOW = (WINHTTP_ERROR_BASE + 184);

    private int _errorCode;

    public WinHttpException()
    {
        super();
    }

    public WinHttpException(String message)
    {
        super(message);
    }

    public WinHttpException(long errorCode)
    {
        this(getMessage((int) errorCode));
        _errorCode = (int) errorCode;
    }

    public int getErrorCode()
    {
        return _errorCode;
    }

    private static String getMessage(int errorCode)
    {
        switch (errorCode)
        {
            case ERROR_WINHTTP_AUTO_PROXY_SERVICE_ERROR :
                return "A proxy for the specified URL cannot be located.";
            case ERROR_WINHTTP_AUTODETECTION_FAILED :
                return "WinHTTP was unable to discover the URL of the Proxy Auto-Configuration (PAC) file.";
            case ERROR_WINHTTP_BAD_AUTO_PROXY_SCRIPT :
                return "An error occurred executing the script code in the Proxy Auto-Configuration (PAC) file.";
            case ERROR_WINHTTP_CANNOT_CONNECT :
                return "Connection to the server failed.";
            case ERROR_WINHTTP_CHUNKED_ENCODING_HEADER_SIZE_OVERFLOW :
                return "An overflow condition is encountered in the course of parsing chunked encoding.";
            case ERROR_WINHTTP_CLIENT_AUTH_CERT_NEEDED :
                return "The server requests client authentication.";
            case ERROR_WINHTTP_CONNECTION_ERROR :
                return "The connection with the server has been reset or terminated";
            case ERROR_WINHTTP_HEADER_COUNT_EXCEEDED :
                return "A larger number of headers were present in a response than WinHTTP could receive.";
            case ERROR_WINHTTP_HEADER_NOT_FOUND :
                return "The requested header could not be located.";
            case ERROR_WINHTTP_HEADER_SIZE_OVERFLOW  :
                return "The size of headers received exceeds the limit for the request handle.";
            case ERROR_WINHTTP_INCORRECT_HANDLE_STATE :
                return "The requested operation cannot be carried out because the handle supplied is not in the correct state.";
            case ERROR_WINHTTP_INCORRECT_HANDLE_TYPE :
                return "The type of handle supplied is incorrect for this operation.";
            case ERROR_WINHTTP_INTERNAL_ERROR :
                return "An internal error has occurred.";
            case ERROR_WINHTTP_INVALID_OPTION :
                return "A request to WinHttpQueryOption or WinHttpSetOption specified an invalid option value.";
            case ERROR_WINHTTP_INVALID_SERVER_RESPONSE :
                return "The server response could not be parsed.";
            case ERROR_WINHTTP_INVALID_URL :
                return "The URL is invalid.";
            case ERROR_WINHTTP_LOGIN_FAILURE :
                return "The login attempt failed.";
            case ERROR_WINHTTP_NAME_NOT_RESOLVED :
                return "The server name could not be resolved.";
            case ERROR_WINHTTP_OPERATION_CANCELLED :
                return "The operation was canceled.";
            case ERROR_WINHTTP_OPTION_NOT_SETTABLE :
                return "The requested option cannot be set, only queried.";
            case ERROR_WINHTTP_REDIRECT_FAILED :
                return "The redirection failed.";
            case ERROR_WINHTTP_RESEND_REQUEST :
                return "The WinHTTP function failed.";
            case ERROR_WINHTTP_RESPONSE_DRAIN_OVERFLOW :
                return "An incoming response exceeds an internal WinHTTP size limit.";
            case ERROR_WINHTTP_SECURE_CERT_CN_INVALID :
                return "A certificate's CN name does not match the passed value";
            case ERROR_WINHTTP_SECURE_CERT_DATE_INVALID :
                return "Indicates that a required certificate is not within its validity period";
            case ERROR_WINHTTP_SECURE_CERT_REV_FAILED :
                return "Revocation could not be checked because the revocation server was offline";
            case ERROR_WINHTTP_SECURE_CERT_REVOKED :
                return "A certificate has been revoked";
            case ERROR_WINHTTP_SECURE_CERT_WRONG_USAGE :
                return "A certificate is not valid for the requested usage";
            case ERROR_WINHTTP_SECURE_FAILURE :
                return "One or more errors were found in the SSL certificate sent by the server.";
            case ERROR_WINHTTP_SECURE_INVALID_CA :
                return "A certificate chain is not trusted by the trust provider.";
            case ERROR_WINHTTP_SECURE_INVALID_CERT :
                return "A certificate is invalid";
            case ERROR_WINHTTP_SHUTDOWN :
                return "The WinHTTP function support is being shut down or unloaded.";
            case ERROR_WINHTTP_TIMEOUT :
                return "The request has timed out.";
            case ERROR_WINHTTP_UNABLE_TO_DOWNLOAD_SCRIPT :
                return "The PAC file could not be downloaded.";
            case ERROR_WINHTTP_UNRECOGNIZED_SCHEME :
                return "The URL specified a scheme other than \"http:\" or \"https:\".";
            default :
                return LastError.getMessage(errorCode);
        }
    }
}

/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32;


/**
 * This is an exception that holds the last error code taken when an instance is created.
 *
 * @author Alexander Evsukov
 * @see com.jniwrapper.win32.LastError
 */
public class LastErrorException extends RuntimeException
{
    private long _errorCode;

    /**
     * Constructs a new instance storing the last error code and setting the system
     * error message. Stores the current value of the system error code for
     * later retrieval by the {@link #getErrorCode()} method.
     *
     * @deprecated Use the {@link #LastErrorException(long)} instead.
     */
    public LastErrorException()
    {
        super();
        _errorCode = LastError.getValue();
    }

    /**
     * Constructs a new instance storing the last error code and setting the system
     * error message. Stores the current value of the system error code for
     * later retrieval by the {@link #getErrorCode()} method.
     *
     * @param errorCode specifies the last error code.
     */
    public LastErrorException(long errorCode)
    {
        super();
        _errorCode = errorCode;
    }

    /**
     * Constructs a new instance with the passed details message. Stores the
     * current value of the system error code for later retrieval by the {@link
     * #getErrorCode()} method. Th system error message for the stored error code
     * should be retrieved by the {@link #getErrorMessage()} method.
     *
     * @param message the details message.
     * @deprecated Use the {@link #LastErrorException(long, String)} instead.
     */
    public LastErrorException(String message)
    {
        super(message);
        _errorCode = LastError.getValue();
    }

    /**
     * Constructs a new instance with the passed details message. Stores the
     * current value of the system error code for later retrieval by the {@link
     * #getErrorCode()} method. The system error message for the stored error code
     * should be retrieved by the {@link #getErrorMessage()} method.
     *
     * @param errorCode specifies the last error code.
     * @param message   the details message.
     */
    public LastErrorException(long errorCode, String message)
    {
        super(message);
        _errorCode = errorCode;
    }

    /**
     * Constructs a new instance with the passed details message. Stores the
     * current value of the system error code for later retrieval by the {@link
     * #getErrorCode()} method. The system error message for the stored error code
     * should be retrieved by the {@link #getErrorMessage()} method. This
     * constructor clears the system error code depending on the value of the
     * <code>clearError</code> parameter.
     *
     * @param message    the detailed message.
     * @param clearError if <code>true</code>, the error code is cleared.
     * @deprecated Use the {@link #LastErrorException(long, String, boolean)} instead.
     */
    public LastErrorException(String message, boolean clearError)
    {
        super(message);
        _errorCode = LastError.getValue();
        if (clearError)
        {
            LastError.clearLastErrorCode();
        }
    }

    /**
     * Constructs a new instance with the passed details message. Stores the
     * current value of the system error code for later retrieval by the {@link
     * #getErrorCode()} method. The system error message for the stored error code
     * should be retrieved by the {@link #getErrorMessage()} method. This
     * constructor clears the system error code depending on the value of the
     * <code>clearError</code> parameter.
     *
     * @param errorCode  specifies the last error code.
     * @param message    the detailed message.
     * @param clearError if <code>true</code>, the error code is cleared.
     */
    public LastErrorException(long errorCode, String message, boolean clearError)
    {
        super(message);
        _errorCode = errorCode;
        if (clearError)
        {
            LastError.clearLastErrorCode();
        }
    }

    /**
     * Constructs a new instance with the cause. Saves the system error code that
     * can be later queried by the {@link #getErrorCode()} method.
     *
     * @param cause the cause saved for later retrieval.
     * @deprecated Use the {@link #LastErrorException(long, Throwable)} instead.
     */
    public LastErrorException(Throwable cause)
    {
        super(cause.getMessage());
        _errorCode = LastError.getValue();
    }

    /**
     * Constructs a new instance with the cause. Saves the system error code that
     * can be later queried by the {@link #getErrorCode()} method.
     *
     * @param errorCode specifies the last error code.
     * @param cause     the cause saved for later retrieval.
     */
    public LastErrorException(long errorCode, Throwable cause)
    {
        super(cause.getMessage());
        _errorCode = errorCode;
    }

    /**
     * Constructs a new instance with the cause. Saves the system error code that
     * can be later queried by the {@link #getErrorCode()} method.
     *
     * @param message    the detailed message.
     * @param cause     the cause saved for later retrieval.
     */
    public LastErrorException(String message, Throwable cause)
    {
        super(message, cause);
    }

    /**
     * @return the system error code saved at construction time.
     */
    public long getErrorCode()
    {
        return _errorCode;
    }

    /**
     * @return the system error message for the stored error code.
     */
    public String getErrorMessage()
    {
        return LastError.getMessage(getErrorCode());
    }

    /**
     * Returns a combined message consisting of the details message passed on
     * the construction and the system error message that corresponds to the stored
     * error code. If the details message was not configured, returns an error
     * message. The details and system error messages are separated by the space
     * character.
     *
     * @return combined error message.
     */
    public String getMessage()
    {
        final String detailsMessage = super.getMessage();
        if (detailsMessage == null || detailsMessage.length() == 0)
        {
            return getErrorMessage();
        }
        return detailsMessage + " " + getErrorMessage();
    }
}

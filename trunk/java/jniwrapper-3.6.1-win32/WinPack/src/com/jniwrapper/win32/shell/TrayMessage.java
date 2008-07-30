/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.shell;

/**
 * TrayMessage class represents callout attributes.
 * 
 * @author Serge Piletsky
 */
public class TrayMessage
{
    /*
     * Constants for configuring TrayMessage icon type
     */
    public static final int ICON_NONE = 0;
    public static final int ICON_INFO = 1;
    public static final int ICON_WARNING = 2;
    public static final int ICON_ERROR = 3;

    private String _title;
    private String _message;
    private int _iconType = ICON_NONE;
    private int _timeout = 10;

    public TrayMessage(String message)
    {
        this(null, message);
    }

    public TrayMessage(String message, int iconType)
    {
        this(null, message, iconType);
    }

    public TrayMessage(String title, String message)
    {
        this(title, message, ICON_NONE);
    }

    public TrayMessage(String title, String message, int iconType)
    {
        setTitle(title);
        setMessage(message);
        setIconType(iconType);
    }

    public String getTitle()
    {
        return _title;
    }

    /**
     * Sets the title of the ballon.
     * 
     * @param title
     */
    public void setTitle(String title)
    {
        _title = title;
    }

    public String getMessage()
    {
        return _message;
    }

    /**
     * Sets a message that will be shown in the ballon.
     * 
     * @param message
     */
    public void setMessage(String message)
    {
        _message = message;
    }

    public int getIconType()
    {
        return _iconType;
    }

    /**
     * Sets an icon type for the message. The passed value must be one of the
     * <code>TrayMessage.ICON_type</code> constants.
     * 
     * @param iconType
     */
    public void setIconType(int iconType)
    {
        switch (iconType)
        {
            case ICON_NONE:
            case ICON_INFO:
            case ICON_WARNING:
            case ICON_ERROR:
                _iconType = iconType;
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    public int getTimeout()
    {
        return _timeout;
    }

    /**
     * Sets timeout in seconds for showing the message. The timeout
     * should be in the range from 10 to 30 seconds.
     * 
     * @param timeout Specifies the time in seconds while
     * the balloon tooltip is displayed.
     */
    public void setTimeout(int timeout)
    {
        if (timeout < 10)
        {
            _timeout = 10;
        }
        else if (timeout > 30)
        {
            _timeout = 30;
        }
        else
        {
            _timeout = timeout;
        }
    }

    public static class Info extends TrayMessage
    {
        public Info(String title, String message)
        {
            super(title, message, ICON_INFO);
        }
    }

    public static class Warning extends TrayMessage
    {
        public Warning(String title, String message)
        {
            super(title, message, ICON_WARNING);
        }
    }

    public static class Error extends TrayMessage
    {
        public Error(String title, String message)
        {
            super(title, message, ICON_ERROR);
        }
    }
}

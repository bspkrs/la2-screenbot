/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.system.eventlog;

import com.jniwrapper.util.EnumItem;

import java.util.Date;

/**
 * This class provides functionality for working with event log messages.
 *
 * @author Vladimir Kondrashchenko
 * @see EventLog
 */
public class EventLogMessage
{
    private int _recordNumber;
    private long _eventID = 0;
    private Type _eventType;
    private int _category = 0;
    private Date _date;
    private byte[] _data;
    private String _source;
    private String _user;
    private String _computer;
    private String _message;

    EventLogMessage()
    {
    }

    public EventLogMessage(String sourceName)
    {
        if (sourceName == null)
        {
            throw new IllegalArgumentException("Illegal message source.");
        }
        _source = sourceName;
    }

    public void setEventID(long eventID)
    {
        if (eventID < 0)
        {
            throw new IllegalArgumentException("Event ID must be positive.");
        }
        _eventID = eventID;
    }

    public void setEventType(Type eventType)
    {
        _eventType = eventType;
    }

    public void setCategory(int category)
    {
        if (category < 0)
        {
            throw new IllegalArgumentException("Category argument must be positive.");
        }
        _category = category;
    }

    void setDate(Date date)
    {
        _date = date;
    }

    public void setData(byte[] data)
    {
        _data = data;
    }

    public void setSource(String source)
    {
        _source = source;
    }

    void setUser(String user)
    {
        _user = user;
    }

    void setComputer(String computer)
    {
        _computer = computer;
    }

    public void setMessage(String message)
    {
        _message = message;
    }

    void setRecordNumber(int recordNumber)
    {
        _recordNumber = recordNumber;
    }

    public long getEventID()
    {
        return _eventID;
    }

    public Type getEventType()
    {
        return _eventType;
    }

    public int getCategory()
    {
        return _category;
    }

    public Date getDate()
    {
        return _date;
    }

    public byte[] getData()
    {
        return _data;
    }

    public String getSource()
    {
        return _source;
    }

    public String getUser()
    {
        return _user;
    }

    public String getComputer()
    {
        return _computer;
    }

    public String getMessage()
    {
        return _message;
    }

    public int getRecordNumber()
    {
        return _recordNumber;
    }

    /**
     * Specifies the type of an event log message.
     */
    public static class Type extends EnumItem
    {
        private String _name;

        /**
         * Information event.
         */
        public static final Type SUCCESS = new Type(0x0000);
        /**
         * Error event.
         */
        public static final Type ERROR = new Type(0x0001);
        /**
         * Warning event.
         */
        public static final Type WARNING = new Type(0x0002);
        /**
         * Information event.
         */
        public static final Type INFORMATION = new Type(0x0004);
        /**
         * Success Audit event.
         */
        public static final Type AUDIT_SUCCESS = new Type(0x0008);
        /**
         * Failure Audit event.
         */
        public static final Type AUDIT_FAILURE = new Type(0x0010);

        Type(int value)
        {
            super(value);
            switch (value)
            {
                case 0x0000 :
                    _name = "Success";
                    break;
                case 0x0001 :
                    _name = "Error";
                    break;
                case 0x0002 :
                    _name = "Warning";
                    break;
                case 0x0004 :
                    _name = "Information";
                    break;
                case 0x0008 :
                    _name = "Success Audit";
                    break;
                case 0x0010 :
                    _name = "Failure Audit";
                    break;
            }
        }

        public String toString()
        {
            return _name;
        }
    }
}

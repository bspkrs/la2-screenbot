/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.io;

import com.jniwrapper.Parameter;
import com.jniwrapper.Structure;
import com.jniwrapper.UInt32;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * FileTime class represents the FILTIME structure.
 */
public class FileTime extends Structure
{
    private static final long MILLISECONDS_IN_SECOND = 1000;
    private static final long MILLISECONDS_IN_MINUTE = 60 * MILLISECONDS_IN_SECOND;
    private static final long MILLISECONDS_IN_HOUR = 60 * MILLISECONDS_IN_MINUTE;
    private static final long MILLISECONDS_IN_DAY = 24 * MILLISECONDS_IN_HOUR;

    // the difference between January 1, 1970, 00:00:00 GMT (Java begin date) and January 1, 1601 (FILETIME begin date)
    private static long TIME_DIFFERENCE_IN_MILLISECONDS = 11644473600000L;

    private UInt32 _dwLowDateTime;
    private UInt32 _dwHighDateTime;

    private void init()
    {
        init(new Parameter[]{_dwLowDateTime, _dwHighDateTime});
    }

    public FileTime()
    {
        _dwLowDateTime = new UInt32();
        _dwHighDateTime = new UInt32();
        init();
    }

    public FileTime(long lowDateTime, long highDateTime)
    {
        this();
        setLowDateTime(lowDateTime);
        setHighDateTime(highDateTime);
    }

    public FileTime(FileTime that)
    {
        _dwLowDateTime = (UInt32)that._dwLowDateTime.clone();
        _dwHighDateTime = (UInt32)that._dwHighDateTime.clone();
        init();
    }

    /**
     * Returns low-order part of the file time.
     *
     * @return low-order part of the file time.
     */
    public long getLowDateTime()
    {
        return _dwLowDateTime.getValue();
    }

    public void setLowDateTime(long value)
    {
        _dwLowDateTime.setValue(value);
    }

    /**
     * Returns high-order part of the file time.
     *
     * @return high-order part of the file time.
     */
    public long getHighDateTime()
    {
        return _dwHighDateTime.getValue();
    }

    public void setHighDateTime(long value)
    {
        _dwHighDateTime.setValue(value);
    }

    /**
     * Converts the value of this <code>FILETIME</code> structure to Java {@link Date} format.
     *
     * @return the converted value
     */
    public Date toDate()
    {
        //calculate number of 100-nanosecond intervals since January 1, 1601
        long lowDateTime = getLowDateTime();
        long highDateTime = getHighDateTime();

        long numberOf100NanosecondIntervals = ((highDateTime << 32) | lowDateTime);

        long milliseconds = numberOf100NanosecondIntervals / 10000;

        long days = milliseconds / MILLISECONDS_IN_DAY;
        milliseconds -= days * MILLISECONDS_IN_DAY;
        long hours = milliseconds / MILLISECONDS_IN_HOUR;
        milliseconds -= hours * MILLISECONDS_IN_HOUR;
        long minutes = milliseconds / MILLISECONDS_IN_MINUTE;
        milliseconds -= minutes * MILLISECONDS_IN_MINUTE;
        long seconds = milliseconds / MILLISECONDS_IN_SECOND;
        milliseconds -= seconds * MILLISECONDS_IN_SECOND;

        // since January 1, 1601
        Calendar calendar = getGMTCalendar();
        calendar.set(1601, Calendar.JANUARY, 1, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        calendar.add(Calendar.DATE, (int)days);
        calendar.add(Calendar.HOUR_OF_DAY, (int)hours);
        calendar.add(Calendar.MINUTE, (int)minutes);
        calendar.add(Calendar.SECOND, (int)seconds);
        calendar.add(Calendar.MILLISECOND, (int)milliseconds);

        return calendar.getTime();
    }

    private Calendar getGMTCalendar()
    {
        return Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    }

    /**
     * Converts the specified {@link Date} value to the internal presentation of <code>FILETIME</code> format.
     *
     * @param date the date to set
     */
    public void fromDate(java.util.Date date)
    {
        long milliseconds = date.getTime() + TIME_DIFFERENCE_IN_MILLISECONDS;
        long numberOf100NanosecondIntervals = milliseconds * 10000;

        long highDateTime = numberOf100NanosecondIntervals >> 32;
        long lowDateTime = numberOf100NanosecondIntervals & 0x00000000FFFFFFFFL;

        setHighDateTime(highDateTime);
        setLowDateTime(lowDateTime);
    }

    public Object clone()
    {
        return new FileTime(this);
    }
}

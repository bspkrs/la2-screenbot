/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.system;

import com.jniwrapper.*;

/**
 * This class is the wrapper for the native <code>SYSTEMTIME</code> structure.
 * Also it provides the {@link #getCurrent()} function to obtain the current system time,
 * using the <code>GetSystemTime</code> Win API function and
 * the {@link #setSystemTime(SystemTime)} function to se he system time.
 *
 * @author Serge Piletsky
 */
public class SystemTime extends Structure
{
    private static final String FUNCTION_GetSystemTime = "GetSystemTime";
    private static final String FUNCTION_SetSystemTime = "SetSystemTime";

    private UInt16 wYear = new UInt16();
    private UInt16 wMonth = new UInt16();
    private UInt16 wDayOfWeek = new UInt16();
    private UInt16 wDay = new UInt16();
    private UInt16 wHour = new UInt16();
    private UInt16 wMinute = new UInt16();
    private UInt16 wSecond = new UInt16();
    private UInt16 wMilliseconds = new UInt16();

    public SystemTime()
    {
        init(new Parameter[]{wYear, wMonth, wDayOfWeek, wDay, wHour, wMinute, wSecond, wMilliseconds});
    }

    public SystemTime(SystemTime that)
    {
        this();
        initFrom(that);
    }

    public int getYear()
    {
        return (int)wYear.getValue();
    }

    public void setYear(int value)
    {
        wYear.setValue(value);
    }

    public int getMonth()
    {
        return (int)wMonth.getValue();
    }

    public void setMonth(int value)
    {
        wMonth.setValue(value);
    }

    public int getDayOfWeek()
    {
        return (int)wDayOfWeek.getValue();
    }

    public void setDayOfWeek(int value)
    {
        wDayOfWeek.setValue(value);
    }

    public int getDay()
    {
        return (int)wDay.getValue();
    }

    public void setDay(int value)
    {
        wDay.setValue(value);
    }

    public int getHour()
    {
        return (int)wHour.getValue();
    }

    public void setHour(int value)
    {
        wHour.setValue(value);
    }

    public int getMinute()
    {
        return (int)wMinute.getValue();
    }

    public void setMinute(int value)
    {
        wMinute.setValue(value);
    }

    public int getSecond()
    {
        return (int)wSecond.getValue();
    }

    public void setSecond(int value)
    {
        wSecond.setValue(value);
    }

    public int getMilliseconds()
    {
        return (int)wMilliseconds.getValue();
    }

    public void setMilliseconds(int value)
    {
        wMilliseconds.setValue(value);
    }

    public Object clone()
    {
        return new SystemTime(this);
    }

    /**
     * Returns the instance of the <code>SystemTime</code>.
     *
     * @return the instance of the <code>SystemTime</code>
     */
    public static SystemTime getCurrent()
    {
        final Function getSystemTime = Kernel32.getInstance().getFunction(FUNCTION_GetSystemTime);
        SystemTime result = new SystemTime();
        getSystemTime.invoke(null, new Pointer(result));
        return result;
    }


    /**
     * Sets the current system time and date.
     *
     * @param value the data to set
     */
    public static void setSystemTime(SystemTime value)
    {
        final Function setSystemTime = Kernel32.getInstance().getFunction(FUNCTION_SetSystemTime);
        setSystemTime.invoke(null, new Pointer(value));
    }

    public String toString()
    {
        StringBuffer result = new StringBuffer("SystemTime: [").
                append("Year=").append(getYear()).
                append(", Month=").append(getMonth()).
                append(", DayOfWeek=").append(getDayOfWeek()).
                append(", Day=").append(getDay()).
                append(", Hour=").append(getHour()).
                append(", Minute=").append(getMinute()).
                append(", Second=").append(getSecond()).
                append(", Milliseconds=").append(getMilliseconds()).
                append("]");
        return result.toString();
    }
}

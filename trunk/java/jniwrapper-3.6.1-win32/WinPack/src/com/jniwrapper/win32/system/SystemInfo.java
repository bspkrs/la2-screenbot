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
import com.jniwrapper.win32.FunctionName;
import com.jniwrapper.win32.LastErrorException;
import com.jniwrapper.win32.IntPtr;

/**
 * This class provides information and methods relevant to SYSTEM_INFO
 * structure, and provides additional methods related to system information.
 * 
 * @author Serge Piletsky
 * @author Alexander Evsukov
 */
public class SystemInfo extends Structure
{
    private UInt16 _processorArchitecture = new UInt16();
    private UInt16 _reserved = new UInt16();
    private UInt32 _pageSize = new UInt32();

    //private UInt32 _minAppAddress = new UInt32();
    //private UInt32 _maxAppAddress = new UInt32();
    private Pointer.Void _minAppAddress = new Pointer.Void();
    private Pointer.Void _maxAppAddress = new Pointer.Void();

    //private UInt32 _activeProcessorMask = new UInt32();
    private IntPtr _activeProcessorMask = new IntPtr();

    private UInt32 _numberOfProcessors = new UInt32();
    private UInt32 _processorType = new UInt32();
    private UInt32 _allocationGranularity = new UInt32();
    private UInt16 _processorLevel = new UInt16();
    private UInt16 _processorRevision = new UInt16();

    static final String FUNCTION_GET_SYSTEM_INFO = "GetSystemInfo";
    static final FunctionName FUNCTION_GET_COMPUTER_NAME = new FunctionName("GetComputerName");
    static final FunctionName FUNCTION_GET_USER_NAME = new FunctionName("GetUserName");

    /**
     * Constructs and initializes a new instance with actual values representing
     * system information.
     */
    public SystemInfo()
    {
        init(new Parameter[]
        {
            _processorArchitecture,
            _reserved,
            _pageSize,
            _minAppAddress,
            _maxAppAddress,
            _activeProcessorMask,
            _numberOfProcessors,
            _processorType,
            _allocationGranularity,
            _processorLevel,
            _processorRevision
        });

        Function function = Kernel32.getInstance().getFunction(FUNCTION_GET_SYSTEM_INFO);
        function.invoke(null, new Pointer(this));
    }

    public SystemInfo(SystemInfo that)
    {
        this();
        initFrom(that);
    }

    public long getProcessorArchitecture()
    {
        return _processorArchitecture.getValue();
    }

    public long getPageSize()
    {
        return _pageSize.getValue();
    }

    public long getMinAppAddress()
    {
        return _minAppAddress.getValue();
    }

    public long getMaxAppAddress()
    {
        return _maxAppAddress.getValue();
    }

    public long getActiveProcessorMask()
    {
        return _activeProcessorMask.getValue();
    }

    public long getNumberOfProcessors()
    {
        return _numberOfProcessors.getValue();
    }

    public long getProcessorType()
    {
        return _processorType.getValue();
    }

    public long getAllocationGranularity()
    {
        return _allocationGranularity.getValue();
    }

    public long getProcessorLevel()
    {
        return _processorLevel.getValue();
    }

    public long getProcessorRevision()
    {
        return _processorRevision.getValue();
    }

    public Object clone()
    {
        return new SystemInfo(this);
    }

    /**
     * Returns the name of the local computer.
     * 
     * @return name of the computer.
     */
    public static String getComputerName()
    {
        Function function = Kernel32.getInstance().getFunction(FUNCTION_GET_COMPUTER_NAME.toString());
        Str computerName = new Str("", 256);
        UInt32 size = new UInt32(computerName.getLength());
        Bool retVal = new Bool();
        long errorCode = function.invoke(retVal, new Pointer(computerName), new Pointer(size));
        if (retVal.getValue())
        {
            String result = computerName.getValue();
            return result;
        }
        else
        {
            throw new LastErrorException(errorCode, "Unable to get computer name.");
        }
    }

    /**
     * Returns the user name logged into the system.
     * 
     * @return name of the logged in user.
     */
    public static String getUserName()
    {
        final Function function = AdvApi32.get(FUNCTION_GET_USER_NAME);
        Str userName = new Str("", 256);
        UInt32 size = new UInt32(userName.getLength());
        Bool retVal = new Bool();
        long errorCode = function.invoke(retVal, new Pointer(userName), new Pointer(size));
        if (retVal.getValue())
        {
            String result = userName.getValue();
            return result;
        }
        else
        {
            throw new LastErrorException(errorCode, "Unable to get user name.");
        }
    }
}

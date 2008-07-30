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
 * This provides version information about underlying Windows platform.
 * 
 * @author Alexander Evsukov
 */
public class VersionInfo extends Structure
{
    static final String FUNCTION_GET_VERSIONEX = "GetVersionExA";

    /**
     * Win32s on Windows 3.1.
     */
    public static final int VER_PLATFORM_WIN32s = 0;
    /**
     * Windows 95, Windows 98, or Windows Me.
     */
    public static final int VER_PLATFORM_WIN32_WINDOWS = 1;
    /**
     * Windows NT, Windows 2000, Windows XP, or Windows .NET Server 2003 family.
     */
    public static final int VER_PLATFORM_WIN32_NT = 2;
    /**
     * Windows CE.
     */
    public static final int VER_PLATFORM_WIN32_CE = 3;

    /*
     * Members from OSVERSIONINFO structure
     */
    private UInt32 _dwOSVersionInfoSize = new UInt32();
    private UInt32 _dwMajorVersion = new UInt32();
    private UInt32 _dwMinorVersion = new UInt32();
    private UInt32 _dwBuildNumber = new UInt32();
    private UInt32 _dwPlatformId = new UInt32();
    private AnsiString _szCSDVersion = new AnsiString(128);

    public VersionInfo()
    {
        init(new Parameter[]{_dwOSVersionInfoSize,
                             _dwMajorVersion,
                             _dwMinorVersion,
                             _dwBuildNumber,
                             _dwPlatformId,
                             _szCSDVersion});
        _dwOSVersionInfoSize.setValue(getLength());
        Function function = Kernel32.getInstance().getFunction(FUNCTION_GET_VERSIONEX);
        Bool result = new Bool();
        function.invoke(result, new Pointer(this));
    }

    public VersionInfo(VersionInfo that)
    {
        this();
        initFrom(that);
    }

    public long getBuildNumber()
    {
        long result = _dwBuildNumber.getValue();

        if (isNT())
        {
            return result;
        }

        // Extract build number from the structure value.
        // On Windows 95/98/Me the low-order word contains the build number and
        // the high-order word contains the major and minor version numbers.
        return result & 0xFFFF;
    }

    public long getMajor()
    {
        return _dwMajorVersion.getValue();
    }

    public long getMinor()
    {
        return _dwMinorVersion.getValue();
    }

    /**
     * Returns operating system platform, which can be one of the
     * <code>VER_PLATFORM_XXX</code> values.
     */
    public long getPlatformId()
    {
        return _dwPlatformId.getValue();
    }

    /**
     * Returns additional version information string. For Windows NT/2000/XP
     * this string represents Service Pack, or is empty if no Service Pack has
     * been installed. For Windows 95/98/Me the result indicates additional
     * version information. For example, " C" indicates Windows 95 OSR2 and " A"
     * indicates Windows 98 Second Edition.
     */
    public String getServicePack()
    {
        return _szCSDVersion.getValue();
    }

    public boolean isNT()
    {
        return getPlatformId() == VER_PLATFORM_WIN32_NT;
    }

    public boolean isWin9x()
    {
        return getPlatformId() == VER_PLATFORM_WIN32_WINDOWS;
    }

    public boolean isWin2k()
    {
        return (getMajor() >= 5) &
               (getPlatformId() == VersionInfo.VER_PLATFORM_WIN32_NT);
    }

    public boolean isWinMe()
    {
        return (getPlatformId() == VersionInfo.VER_PLATFORM_WIN32_WINDOWS) &
               ((getMajor() > 4) || (getMajor() == 4 && getMinor() >= 90));
    }

    public boolean isWinXP() {
        return getMajor() >= 5 & getMinor() == 1 & getPlatformId() == VersionInfo.VER_PLATFORM_WIN32_NT;
    }

    public boolean isWin2003() {
        return getMajor() >= 5 & getMinor() == 2 & getPlatformId() == VersionInfo.VER_PLATFORM_WIN32_NT;
    }

    public Object clone()
    {
        return new VersionInfo(this);
    }
}

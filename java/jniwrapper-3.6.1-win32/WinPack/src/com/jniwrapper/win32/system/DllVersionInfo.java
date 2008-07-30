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
 * This class represents the DLLVERSIONINFO structure and provides the ability to load this structure from a specified library.
 * <p><b>Note:</b> Not all libraries have the <code>DllGetVersion</code> function, which is used by this class.
 * Therefore, loading of version information from such a library will fail with {@link NoSuchFunctionException} thrown.
 *
 * @author Serge Piletsky
 */
public class DllVersionInfo extends Structure
{
    private static final String FUNCTION_DllGetVersion = "DllGetVersion";

    /**
     * The DLL was built for all Microsoft® Windows® platforms.
     */
    public static final int DLLVER_PLATFORM_WINDOWS = 1;

    /**
     * The DLL was built specifically for Microsoft Windows NT®.
     */
    public static final int DLLVER_PLATFORM_NT = 2;

    private UInt32 cbSize = new UInt32();
    private UInt32 dwMajorVersion = new UInt32();
    private UInt32 dwMinorVersion = new UInt32();
    private UInt32 dwBuildNumber = new UInt32();
    private UInt32 dwPlatformID = new UInt32();

    private DllVersionInfo()
    {
        init(new Parameter[]{cbSize, dwMajorVersion, dwMinorVersion, dwBuildNumber, dwPlatformID});
        cbSize.setValue(getLength());
    }

    private DllVersionInfo(DllVersionInfo that)
    {
        this();
        initFrom(that);
    }

    /**
     * Creates a new instance and automatically tries to load DLL version information from the
     * specified library.
     *
     * @param libraryName the library name to load version info from.
     * @throws NoSuchFunctionException if the specified library does not export <code>DllGetVersion</code>.
     */
    public DllVersionInfo(String libraryName) throws NoSuchFunctionException
    {
        this();
        loadVersionInfo(libraryName);
    }

    /**
     * Returns the major version of the DLL. If the DLL version is 4.0.950, this value will be 4.
     *
     * @return the major version of the DLL.
     */
    public int getMajorVersion()
    {
        return (int)dwMajorVersion.getValue();
    }

    /**
     * Returns the minor version of the DLL. If the DLL version is 4.0.950, this value will be 0.
     *
     * @return the minor version of the DLL.
     */
    public int getMinorVersion()
    {
        return (int)dwMinorVersion.getValue();
    }

    /**
     * Returns the build number of the DLL. If the DLL version is 4.0.950, this value will be 950.
     *
     * @return the build number of the DLL.
     */
    public int getBuildNumber()
    {
        return (int)dwBuildNumber.getValue();
    }

    /**
     * Returns the platform identifier for which the DLL was built. This can be one of the following values:
     * <p/>
     * {@link #DLLVER_PLATFORM_WINDOWS}<br>
     * {@link #DLLVER_PLATFORM_NT}
     *
     * @return the platform identifier for which the DLL was built.
     */
    public int getPlatformID()
    {
        return (int)dwPlatformID.getValue();
    }

    private static class UnloadableLibrary extends Library
    {
        public UnloadableLibrary(String name)
        {
            super(name);
        }

        public void unload()
        {
            super.unload();
        }
    }

    private void loadVersionInfo(String libraryName) throws NoSuchFunctionException
    {
        UnloadableLibrary library = new UnloadableLibrary(libraryName);
        try
        {
            Function function = library.getFunction(FUNCTION_DllGetVersion);

            LongInt result = new LongInt();
            function.invoke(result, new Pointer(this));
            if (result.getValue() != 0)
            {
                throw new RuntimeException("Unable to get version of the DLL.");
            }
        }
        finally
        {
            library.unload();
        }
    }

    public String toString()
    {
        return "DllVersionInfo: [Major Version = " + getMajorVersion() +
                "; Minor Version = " + getMinorVersion() +
                "; Build Number = " + getBuildNumber() +
                "; PlatformID = " + getPlatformID() + "]";
    }

    public Object clone()
    {
        return new DllVersionInfo(this);
    }
}

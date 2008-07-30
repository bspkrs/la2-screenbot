/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.registry;

/**
 * This utility class provides path information on the browsers registered with the system.
 * <p>
 * For example, a default browser registered in the system can be launched using the following code:
 * <pre>
 * String browserPath = BrowserPaths.getDefaultBrowserPath();
 * Runtime.getRuntime().exec(browserPath);
 * </pre>
 * @author Alexander Evsukov
 * @since 2.5
 */
public class BrowserPaths
{
    private static final String REG_IEXPLORE_COMMAND = "Applications\\iexplore.exe\\shell\\open\\command";
    private static final String REG_FIREFOX_COMMAND = "Applications\\FIREFOX.EXE\\shell\\open\\command";

    private static final String PARAM_DEF_IE = "%1";
    private static final String PARAM_DEF_FIREFOX = "-url";

    private BrowserPaths()
    {
    }
    
    /**
     * Retrieves the default browser command line from
     * <code>HKEY_CLASSES_ROOT\http\shell\open\command\default</code>.
     * Normally the command line includes a parameter placeholder.
     *
     * @return default browser command line.
     */
    public static String getDefaultBrowserCommand()
    {
        RegistryKey rkey = RegistryKey.CLASSES_ROOT.openSubKey("http\\shell\\open\\command");

        return rkey.values().get("").toString();
    }

    /**
     * Retrieves the command line for running <code>iexplore.exe</code> from
     * <code>HKEY_CLASSES_ROOT\Applications\iexplore.exe\shell\open\command\default</code>.
     * The command line includes the parameter placeholder (<code>%1</code>).
     * @return the command line for running <code>iexplore.exe</code>.
     */
    static String getIExploreCommand()
    {
        RegistryKey rkey = RegistryKey.CLASSES_ROOT.openSubKey(REG_IEXPLORE_COMMAND);
        return rkey.values().get("").toString();
    }

    /**
     * Retrieves the command line for running <code>firefox.exe</code> from
     * <code>HKEY_CLASSES_ROOT\Applications\iexplore.exe\shell\open\command\default</code>.
     * The command line includes the parameter placeholder (<code>%1</code>).
     * @return the command line for running <code>firefox.exe</code> if installed, otherwise <code>null</code>.
     */
    static String getFirefoxCommand()
    {
        if (RegistryKey.CLASSES_ROOT.exists(REG_FIREFOX_COMMAND))
        {
            RegistryKey rkey = RegistryKey.CLASSES_ROOT.openSubKey(REG_FIREFOX_COMMAND);
            return rkey.values().get("").toString();
        }
        return null;
    }

    /**
     * Obtains the path to the default browser's executable file from the registry.
     * @return the path string to default browser executable file.
     */
    public static String getDefaultBrowserPath()
    {
        final String defaultBrowserCommand = getDefaultBrowserCommand();
        if (defaultBrowserCommand.indexOf("iexplore") != -1)
        {
            return getExecutablePath(defaultBrowserCommand, PARAM_DEF_IE);
        }
        if (defaultBrowserCommand.toLowerCase().indexOf("firefox") != -1)
        {
            return getExecutablePath(defaultBrowserCommand, PARAM_DEF_FIREFOX);
        }

        return getExecutablePath(defaultBrowserCommand, PARAM_DEF_IE);
    }

    /**
     * Obtains the path to <code>iexplore.exe</code> from the registry.
     * @return the full path string of iexplore.exe.
     */
    public static String getIExplorePath()
    {
        return getExecutablePath(getIExploreCommand(), PARAM_DEF_IE);
    }

    /**
     * Obtains the path to <code>firefox.exe</code> from the registry.
     * @return the full path string of firefox.exe or <code>null</code> if the application is not installed.
     */
    public static String getFirefoxPath()
    {
        final String firefoxCommand = getFirefoxCommand();
        if (firefoxCommand != null)
        {
            return getExecutablePath(firefoxCommand, PARAM_DEF_FIREFOX);
        }
        return null;
    }

    /**
     * Retrieves the path to the executable file of a browser assuming that the command line
     * contains parameter placeholder.
     * @param command the command line obtained from the Registry.
     *
     * @return path string for the executable.
     */
    private static String getExecutablePath(String command, String paramDef)
    {
        final int paramPos = command.lastIndexOf(paramDef);
        if (paramPos == -1)
        {
            return command;
        }
        final String exePath = command.substring(0, paramPos - 1);
        return exePath;
    }
}

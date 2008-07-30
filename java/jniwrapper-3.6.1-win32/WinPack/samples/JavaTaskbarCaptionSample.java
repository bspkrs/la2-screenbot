/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
import com.jniwrapper.win32.process.CurrentProcess;
import com.jniwrapper.win32.registry.RegistryKey;
import com.jniwrapper.win32.registry.RegistryKeyValues;

import java.util.StringTokenizer;

/**
 * This sample demonstrates WinPack ability to setup
 * title and infotip for taskbar group of java programms.
 *
 * @author Vladimir Kondrashchenko
 */
public class JavaTaskbarCaptionSample
{
    /**
     * Registers a new title for taskbar group of java programms
     * in the system registry.
     *
     * <b>NOTE:</b>This function changes the system registry.
     * New title will set as the default title for
     * taskbar groups of java programms, so it won't change
     * when current process finishes. To restore default title call
     * <CODE>restoreDefaultJavaTaskbarCaption()</CODE>
     *
     * @param title - New title for taskbar group of java programms.
     */
    public static void setJavaTaskbarTitle(String title)
    {
        CurrentProcess currentProcess = new CurrentProcess();
        StringTokenizer stringTokenizer = new StringTokenizer(currentProcess.getCommandLine());
        String javaPath = stringTokenizer.nextToken() + ".exe";
        RegistryKey regKey = RegistryKey.CURRENT_USER;
        RegistryKeyValues regKeyValue =
                new RegistryKeyValues(regKey.openSubKey("Software\\Microsoft\\Windows\\ShellNoRoam\\MUICache", true));
        regKeyValue.put(javaPath, title);
    }

    /**
     * Restores the default ("java") title for taskbar group of java programms.
     * <b>NOTE:</b>This function changes the registry.
     */
    public static void restoreDefaultJavaTaskbarCaption()
    {
        CurrentProcess currentProcess = new CurrentProcess();
        StringTokenizer stringTokenizer = new StringTokenizer(currentProcess.getCommandLine());
        String javaPath = stringTokenizer.nextToken() + ".exe";
        RegistryKey regKey = RegistryKey.CURRENT_USER;
        RegistryKeyValues regKeyValue =
                new RegistryKeyValues(regKey.openSubKey("Software\\Microsoft\\Windows\\ShellNoRoam\\MUICache", true));
        regKeyValue.put(javaPath, "java");
    }

    public static void main(String[] args)
    {
        setJavaTaskbarTitle("Powered by JNIWrapper");
    }
}

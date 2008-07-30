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
import com.jniwrapper.util.EnumItem;
import com.jniwrapper.win32.security.AccessToken;
import com.jniwrapper.win32.ui.User32;

/**
 * This class responds for termination of windows session.
 */
public class WindowsSession
{
    private static final String FUNCTION_EXIT_WINDOWS_EX = "ExitWindowsEx";

    private WindowsSession()
    {
    }

    /**
     * Logs off the current user.
     *
     * @param isNotify if true then notify other application about shutdown else don't notify
     *
     * @return true if succeeded else returns false.
     */
    public static boolean logoff(boolean isNotify)
    {
        return exitWindows(Action.LOGOFF, isNotify);
    }

    /**
     * Shuts the system down.
     *
     * @param isNotify if true then notify other application about shutdown else don't notify
     *
     * @return true if succeeded else returns false.
     */
    public static boolean shutdown(boolean isNotify)
    {
        return exitWindows(Action.SHUTDOWN, isNotify);
    }

    /**
     * Shuts the system down and power off.
     *
     * @param isNotify if true then notify other application about shutdown else don't notify
     *
     * @return true if succeeded else returns false.
     */
    public static boolean powerOff(boolean isNotify)
    {
        return exitWindows(Action.POWEROFF, isNotify);
    }

    /**
     * Shuts the system down and restarts the system.
     *
     * @param isNotify if true then notify other application about shutdown else don't notify
     *
     * @return true if succeeded else returns false.
     */
    public static boolean reboot(boolean isNotify)
    {
        return exitWindows(Action.REBOOT, isNotify);
    }

    /**
     * This enumeration defines actions that can be performed for
     * terminating the current Windows session.
     */
    private static class Action extends EnumItem
    {
        /**
         * Shut downs all processes in logon session then logs off.
         */
        public static final Action LOGOFF = new Action(0);

        /**
         * Shut downs the system and turns off the power. Process must have SE_SHUTDOWN_NAME privilege.
         */
        public static final Action POWEROFF = new Action(0x00000008);

        /**
         * Shut downs the system and restarts the computer. Process must have SE_SHUTDOWN_NAME privilege.
         */
        public static final Action REBOOT = new Action(0x00000002);

        /**
         * Shut downs the system. Process must have SE_SHUTDOWN_NAME privilege.
         */
        public static final Action SHUTDOWN = new Action(0x00000001);

        private Action(int value)
        {
            super(value);
        }
    }

    /**
     * This enumeration defines notification modes that Windows can use during
     * the shutdown sequence.
     */
    private static class NotifyMode extends EnumItem
    {
        /**
         * Don't notify and force close all applications (other applications can loose data in this case).
         */
        public static final NotifyMode FORCE = new NotifyMode(0x00000004);

        /**
         * Notify other applications about shutdown (application can take action before shutdown in this case).
         */
        public static final NotifyMode FORCEIFHUNG = new NotifyMode(0x00000010);

        private NotifyMode(int value)
        {
            super(value);
        }
    }

    private static boolean exitWindowsEx(Action action, NotifyMode notify)
    {
        Function func = User32.getInstance().getFunction(FUNCTION_EXIT_WINDOWS_EX);
        Bool result = new Bool();
        func.invoke(result, new Parameter[]{new UInt(action.getValue()), new UInt32(notify.getValue())});

        return result.getValue();
    }

    /**
     * Logs off the current user or shuts the system down.
     *
     * @param action an action to take.
     * @param isNotify if true then notify other application about shutdown else don't notify
     *
     * @return true if succeeded else returns false.
     */
    private static boolean exitWindows(Action action, boolean isNotify)
    {
        if (action.getValue() != Action.LOGOFF.getValue())
        {
            if (!adjustToken())
            {
                return false;
            }
        }

        NotifyMode notifyMode = isNotify ? NotifyMode.FORCEIFHUNG : NotifyMode.FORCE;

        return exitWindowsEx(action, notifyMode);
    }

    private static boolean adjustToken()
    {
        AccessToken token = new AccessToken();
        return token.enablePrivelege("SeShutdownPrivilege");
    }
}

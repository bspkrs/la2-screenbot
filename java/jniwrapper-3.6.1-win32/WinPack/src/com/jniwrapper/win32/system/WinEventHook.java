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
import com.jniwrapper.util.FlagSet;
import com.jniwrapper.win32.Handle;
import com.jniwrapper.win32.ui.User32;

/**
 * <code>WinEventHook</code> class provides windows event hook functionality.
 *
 * @author Vladimir Kondrashchenko
 */
public class WinEventHook
{
    private static final String FUNCTION_SET_WIN_EVENT_HOOK = "SetWinEventHook";
    private static final String FUNCTION_UNHOOK_WIN_EVENT = "UnhookWinEvent";

    /**
     * <code>WinEventFlag</code> class specifies whether hook function contained in a DLL or not and events to skip
     *
     * @author Vladimir Kondrashchenko
     */
    public static class WinEventFlag extends FlagSet
    {
        /**
         * Setup WINEVENT_INCONTEXT flag is callback function contained in DLL module
         */
        private static final long WINEVENT_INCONTEXT = 0x0004;

        /**
         * Setup WINEVENT_OUTOFCONTEXT flag is callback function is not mapped
         * into the address space of the process
         */
        private static final long WINEVENT_OUTOFCONTEXT = 0x0000;

        /**
         * Setup WINEVENT_SKIPOWNPROCESS flag to prevent this instance of the hook
         * from receiving events generated by threads in this process.
         */
        private static final long WINEVENT_SKIPOWNPROCESS = 0x0002;

        /**
         * setup WINEVENT_SKIPOWNTHREAD flag to prevents this instance of the hook
         * from receiving events generated by the thread that is registering this hook.
         */
        private static final long WINEVENT_SKIPOWNTHREAD = 0x0001;

        public WinEventFlag()
        {

        }

        public WinEventFlag(long flags)
        {
            super(flags);
        }

        public void setInContext(boolean value)
        {
            setupFlag(WINEVENT_INCONTEXT, value);
        }

        public boolean getInContext()
        {
            return contains(WINEVENT_INCONTEXT);
        }

        public void setOutOfContext(boolean value)
        {
            setupFlag(WINEVENT_OUTOFCONTEXT, value);
        }

        public boolean getOutOfContext()
        {
            return contains(WINEVENT_OUTOFCONTEXT);
        }

        public void setskipOwnProcess(boolean value)
        {
            setupFlag(WINEVENT_SKIPOWNPROCESS, value);
        }

        public boolean getSkipOwnProcess()
        {
            return contains(WINEVENT_SKIPOWNPROCESS);
        }

        public void setSkipOwnThread(boolean value)
        {
            setupFlag(WINEVENT_SKIPOWNTHREAD, value);
        }

        public boolean getSkipOwnThread()
        {
            return contains(WINEVENT_SKIPOWNTHREAD);
        }
    }


    /**
     * WinEvents class contains windows events constants which used
     * in the WinEventHook class
     */
    public static class WinEvent extends EnumItem
    {
        public static final WinEvent EVENT_MIN = new WinEvent(0x00000001);
        public static final WinEvent EVENT_MAX = new WinEvent(0x7FFFFFFF);

        public static final WinEvent EVENT_SYSTEM_SOUND = new WinEvent(0x0001);
        public static final WinEvent EVENT_SYSTEM_ALERT = new WinEvent(0x0002);

        public static final WinEvent EVENT_SYSTEM_FOREGROUND = new WinEvent(0x0003);

        public static final WinEvent EVENT_SYSTEM_MENUSTART = new WinEvent(0x0004);
        public static final WinEvent EVENT_SYSTEM_MENUEND = new WinEvent(0x0005);

        public static final WinEvent EVENT_SYSTEM_MENUPOPUPSTART = new WinEvent(0x0006);
        public static final WinEvent EVENT_SYSTEM_MENUPOPUPEND = new WinEvent(0x0007);

        public static final WinEvent EVENT_SYSTEM_CAPTURESTART = new WinEvent(0x0008);
        public static final WinEvent EVENT_SYSTEM_CAPTUREEND = new WinEvent(0x0009);

        public static final WinEvent EVENT_SYSTEM_MOVESIZESTART = new WinEvent(0x000A);
        public static final WinEvent EVENT_SYSTEM_MOVESIZEEND = new WinEvent(0x000B);

        public static final WinEvent EVENT_SYSTEM_CONTEXTHELPSTART = new WinEvent(0x000C);
        public static final WinEvent EVENT_SYSTEM_CONTEXTHELPEND = new WinEvent(0x000D);

        public static final WinEvent EVENT_SYSTEM_DRAGDROPSTART = new WinEvent(0x000E);
        public static final WinEvent EVENT_SYSTEM_DRAGDROPEND = new WinEvent(0x000F);

        public static final WinEvent EVENT_SYSTEM_DIALOGSTART = new WinEvent(0x0010);
        public static final WinEvent EVENT_SYSTEM_DIALOGEND = new WinEvent(0x0011);

        public static final WinEvent EVENT_SYSTEM_SCROLLINGSTART = new WinEvent(0x0012);
        public static final WinEvent EVENT_SYSTEM_SCROLLINGEND = new WinEvent(0x0013);

        public static final WinEvent EVENT_SYSTEM_SWITCHSTART = new WinEvent(0x0014);
        public static final WinEvent EVENT_SYSTEM_SWITCHEND = new WinEvent(0x0015);

        public static final WinEvent EVENT_SYSTEM_MINIMIZESTART = new WinEvent(0x0016);
        public static final WinEvent EVENT_SYSTEM_MINIMIZEEND = new WinEvent(0x0017);

        public static final WinEvent EVENT_OBJECT_CREATE = new WinEvent(0x8000);
        public static final WinEvent EVENT_OBJECT_DESTROY = new WinEvent(0x8001);
        public static final WinEvent EVENT_OBJECT_SHOW = new WinEvent(0x8002);
        public static final WinEvent EVENT_OBJECT_HIDE = new WinEvent(0x8003);
        public static final WinEvent EVENT_OBJECT_REORDER = new WinEvent(0x8004);

        public static final WinEvent EVENT_OBJECT_FOCUS = new WinEvent(0x8005);
        public static final WinEvent EVENT_OBJECT_SELECTION = new WinEvent(0x8006);
        public static final WinEvent EVENT_OBJECT_SELECTIONADD = new WinEvent(0x8007);
        public static final WinEvent EVENT_OBJECT_SELECTIONREMOVE = new WinEvent(0x8008);
        public static final WinEvent EVENT_OBJECT_SELECTIONWITHIN = new WinEvent(0x8009);

        public static final WinEvent EVENT_OBJECT_STATECHANGE = new WinEvent(0x800A);
        public static final WinEvent EVENT_OBJECT_LOCATIONCHANGE = new WinEvent(0x800B);

        public static final WinEvent EVENT_OBJECT_NAMECHANGE = new WinEvent(0x800C);
        public static final WinEvent EVENT_OBJECT_DESCRIPTIONCHANGE = new WinEvent(0x800D);
        public static final WinEvent EVENT_OBJECT_VALUECHANGE = new WinEvent(0x800E);
        public static final WinEvent EVENT_OBJECT_PARENTCHANGE = new WinEvent(0x800F);
        public static final WinEvent EVENT_OBJECT_HELPCHANGE = new WinEvent(0x8010);
        public static final WinEvent EVENT_OBJECT_DEFACTIONCHANGE = new WinEvent(0x8011);
        public static final WinEvent EVENT_OBJECT_ACCELERATORCHANGE = new WinEvent(0x8012);

        WinEvent(int value)
        {
            super(value);
        }
    }

    /**
     * The <code>SetWinEventHook</code> function sets an event hook function for a range of events.
     *
     * @param eventMin  - WinEvents constant with specifies the lowest event value of the range.
     * @param eventMax  - WinEvents constant with specifies the highest event value of the range.
     * @param dllModule - handle to the DLL with the hook function.
     * @param function  - the event hook function. An instance of a class nested from the HookFunction.
     * @param processID - the ID of the process from which the hook function receives events.
     * @param threadID  - the ID of the thread from which the hook function receives events.
     * @param eventFlag - an instance of <code>WinEventFlag</code>.
     * @return identificator of this event hook, or 0 if function fails.
     */
    public static int setWinEventHook(WinEvent eventMin,
                                      WinEvent eventMax,
                                      Handle dllModule,
                                      HookFunction function,
                                      int processID,
                                      int threadID,
                                      WinEventFlag eventFlag)
    {
        Function func = User32.getInstance().getFunction(FUNCTION_SET_WIN_EVENT_HOOK);
        UInt32 result = new UInt32();
        func.invoke(result, new Parameter[]
        {
            new UInt32(eventMin.getValue()),
            new UInt32(eventMax.getValue()),
            dllModule,
            function,
            new UInt32(processID),
            new UInt32(threadID),
            new UInt(eventFlag.getFlags())});
        return (int)result.getValue();
    }

    /**
     * The <code>UnhookWinEvent</code> function removes specified event hook function.
     *
     * @param hookID - handle to the event hook.
     * @return true is succed and false otherwise
     */
    public static boolean unhookWinEvent(int hookID)
    {
        Function func = User32.getInstance().getFunction(FUNCTION_UNHOOK_WIN_EVENT);
        Bool result = new Bool();
        func.invoke(result, new UInt32(hookID));
        return result.getValue();
    }
}
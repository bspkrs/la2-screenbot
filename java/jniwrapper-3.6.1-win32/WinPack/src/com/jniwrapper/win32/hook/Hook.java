/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.hook;

import com.jniwrapper.*;
import com.jniwrapper.util.EnumItem;
import com.jniwrapper.util.Logger;
import com.jniwrapper.win32.hook.data.HooksData;
import com.jniwrapper.win32.system.EventObject;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * This class enables to install various Windows hooks and retrieve information from them
 * using {@link HookEventListener}.
 * <p/>
 * All avaiable hooks are represented by the {@link Descriptor} class.
 *
 * @author Serge Piletsky
 */
public class Hook
{
    private static final Logger LOG = Logger.getInstance(Hook.class);

    /**
     * This class represents the enumeraton of available Windows hooks.
     *
     * @author Serge Piletsky
     */
    public static class Descriptor extends EnumItem
    {
        /**
         * Records input messages posted to the system message queue.
         */
        public static final Descriptor JOURNALRECORD = new Descriptor(0, "JOURNALRECORD", true);

        /**
         * Posts messages previously recorded by a {@link #JOURNALRECORD} hook procedure.
         */
        public static final Descriptor JOURNALPLAYBACK = new Descriptor(1, "JOURNALPLAYBACK", true);

        /**
         * Monitors keystroke messages.
         */
        public static final Descriptor KEYBOARD = new Descriptor(2, "KEYBOARD");

        /**
         * Monitors messages posted to a message queue.
         */
        public static final Descriptor GETMESSAGE = new Descriptor(3, "GETMESSAGE");

        /**
         * Monitors messages before the system sends them to the destination window procedure.
         */
        public static final Descriptor CALLWNDPROC = new Descriptor(4, "CALLWNDPROC");

        /**
         * Receives notifications useful to a computer-based training (CBT) application.
         */
        public static final Descriptor CBT = new Descriptor(5, "CBT");

        /**
         * Monitors messages generated as a result of an input event in a dialog box, message box, menu, or scroll bar.
         */
        public static final Descriptor SYSMSGFILTER = new Descriptor(6, "SYSMSGFILTER", true);

        /**
         * Monitors mouse messages.
         */
        public static final Descriptor MOUSE = new Descriptor(7, "MOUSE");

        /**
         *
         */
        public static final Descriptor HARDWARE = new Descriptor(8, "HARDWARE");

        /**
         * Useful for debugging other hook procedures.
         */
        public static final Descriptor DEBUG = new Descriptor(9, "DEBUG");

        /**
         * Receives notifications useful to shell applications.
         */
        public static final Descriptor SHELL = new Descriptor(10, "SHELL");

        /**
         * A hook procedure that will be called when the application's foreground thread is about to become idle.
         * This hook is useful for performing low priority tasks during idle time.
         */
        public static final Descriptor FOREGROUNDIDLE = new Descriptor(11, "FOREGROUNDIDLE");

        /**
         * Monitors messages after they have been processed by the destination window procedure.
         */
        public static final Descriptor CALLWNDPROCRET = new Descriptor(12, "CALLWNDPROCRET");

        /**
         * Monitors low-level keyboard input events.
         * Available for WinNT systems only.
         */
        public static final Descriptor KEYBOARD_LL = new Descriptor(13, "KEYBOARD_LL", true);

        /**
         * Monitors low-level mouse input events.
         */
        public static final Descriptor MOUSE_LL = new Descriptor(14, "MOUSE_LL", true);

        private static final String NAME_PREFIX = "JNIWrapperHook.";

        private boolean _globalOnly;
        private String _name = null;

        private Descriptor(int value, String name)
        {
            this(value, name, false);
        }

        private Descriptor(int value, String name, boolean globalOnly)
        {
            super(value);
            _globalOnly = globalOnly;
            _name = NAME_PREFIX + name;
        }

        /**
         * Returns true if the HookDescriptor, which is determinated by this item, is global only.
         *
         * @return true if the HookDescriptor is global only.
         */
        public boolean isGlobalOnly()
        {
            return _globalOnly;
        }

        /**
         * Returns the string descriptor.
         *
         * @return the string descriptor.
         */
        public String getName()
        {
            return _name;
        }
    }

    private static final String FUNCTION_InstallHook = "InstallHook";
    private static final String FUNCTION_UninstallHook = "UninstallHook";

    private static Library _library;

    private final List _listeners = Collections.synchronizedList(new LinkedList());

    private Thread _eventsThread = null;
    private HookEventLoop _eventLoop = null;
    private Descriptor _descriptor;
    private boolean _installed = false;

    /**
     * Hook described by {@link Descriptor#JOURNALRECORD} descriptor.
     */
    public static final Hook JOURNALRECORD = new Hook(Descriptor.JOURNALRECORD);

    /**
     * Hook described by {@link Descriptor#KEYBOARD} descriptor.
     */
    public static final Hook KEYBOARD = new Hook(Descriptor.KEYBOARD);

    /**
     * Hook described by {@link Descriptor#GETMESSAGE} descriptor.
     */
    public static final Hook GETMESSAGE = new Hook(Descriptor.GETMESSAGE);

    /**
     * Hook described by {@link Descriptor#CALLWNDPROC} descriptor.
     */
    public static final Hook CALLWNDPROC = new Hook(Descriptor.CALLWNDPROC);

    /**
     * Hook described by {@link Descriptor#SYSMSGFILTER} descriptor.
     */
    public static final Hook SYSMSGFILTER = new Hook(Descriptor.SYSMSGFILTER);

    /**
     * Hook described by {@link Descriptor#MOUSE} descriptor.
     */
    public static final Hook MOUSE = new Hook(Descriptor.MOUSE);

    /**
     * Hook described by {@link Descriptor#SHELL} descriptor.
     */
    public static final Hook SHELL = new Hook(Descriptor.SHELL);

    /**
     * Hook described by {@link Descriptor#FOREGROUNDIDLE} descriptor.
     */
    public static final Hook FOREGROUNDIDLE = new Hook(Descriptor.FOREGROUNDIDLE);

    /**
     * Hook described by {@link Descriptor#CALLWNDPROCRET} descriptor.
     */
    public static final Hook CALLWNDPROCRET = new Hook(Descriptor.CALLWNDPROCRET);

    /**
     * Creates a new instance of the hook by a specified hook descriptor.
     *
     * @param descriptor specifies a hook to install.
     */
    private Hook(Descriptor descriptor)
    {
        _descriptor = descriptor;
    }

    /**
     * Verifies if the hook is installed.
     *
     * @return true if the hook is installed.
     */
    public boolean isInstalled()
    {
        return _installed;
    }

    /**
     * Adds a hook event listener.
     *
     * @param listener a hook event listener.
     */
    public void addListener(HookEventListener listener)
    {
        _listeners.add(listener);
    }

    /**
     * Removes a hook event listener.
     *
     * @param listener a hook event listener.
     */
    public void removeListener(HookEventListener listener)
    {
        _listeners.remove(listener);
    }

    /**
     * Installs the hook.
     */
    public void install()
    {
        if (isInstalled())
            throw new IllegalStateException("Hook is already instaled.");

        _eventLoop = new HookEventLoop();
        _eventsThread = new Thread(_eventLoop);
        _eventsThread.start();
    }

    /**
     * Uninstalls the hook.
     */
    public void uninstall()
    {
        if (!isInstalled())
            throw new IllegalStateException("Hook is not installed.");

        _eventLoop.uninstall();
        try
        {
            _eventsThread.join();
            _eventsThread = null;
        }
        catch (InterruptedException e)
        {
            LOG.error("", e);
        }
    }

    private static Library getLibrary()
    {
        if (_library == null)
        {
            _library = new Library(Library.NATIVE_CODE);
        }
        return _library;
    }

    /**
     * This class provides a hook events loop.
     */
    private class HookEventLoop implements Runnable
    {
        private final HooksData _hooksData = new HooksData();
        private EventObject _eventObject;
        private boolean _messageThreadAlive = false;

        /**
         * Installs the hook.
         */
        private void installHook()
        {
            final Function install = getLibrary().getFunction(FUNCTION_InstallHook);
            install.setCallingConvention(Function.CDECL_CALLING_CONVENTION);

            Pointer hookDataPtr = new Pointer(_hooksData);
            install.invoke(hookDataPtr,
                    new Int(_descriptor.getValue()),
                    new AnsiString(_descriptor.getName()));

            _installed = true;
        }

        /**
         * Notifies listeners about a hook event.
         */
        private void notifyListeners(HookEventObject event)
        {
            for (Iterator i = _listeners.iterator(); i.hasNext() && _messageThreadAlive;)
            {
                HookEventListener listener = (HookEventListener)i.next();
                listener.onHookEvent(event);
            }
        }

        /**
         * Starts the event loop.
         */
        public void run()
        {
            _eventObject = new EventObject(_descriptor.getName());
            _eventObject.reset();

            installHook();
            _messageThreadAlive = true;
            while (_messageThreadAlive)
            {
                _eventObject.waitFor();
                if (_messageThreadAlive) {
                    HookEventObject event = _hooksData.readEvent(_descriptor);
                    notifyListeners(event);
                    _eventObject.reset();
                }
            }

            final Function uninstall = getLibrary().getFunction(FUNCTION_UninstallHook);
            uninstall.setCallingConvention(Function.CDECL_CALLING_CONVENTION);
            uninstall.invoke(null, new UInt(_descriptor.getValue()));

            _eventObject.reset();
            _eventObject.close();

            _installed = false;
        }

        /**
         * Uninstalls the hook.
         */
        private void uninstall()
        {
            _messageThreadAlive = false;
            _eventObject.notifyEvent();
        }
    }
}

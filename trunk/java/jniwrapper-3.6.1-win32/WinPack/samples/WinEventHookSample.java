/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
import com.jniwrapper.win32.Handle;
import com.jniwrapper.win32.Msg;
import com.jniwrapper.win32.system.HookFunction;
import com.jniwrapper.win32.system.WinEventHook;
import com.jniwrapper.win32.ui.WindowProc;
import com.jniwrapper.win32.ui.Wnd;
import com.jniwrapper.win32.ui.WndClass;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * This sample demonstrates the WinEventHook class ability to receive
 * a program switch event. Press ALT+TAB to generate SWITCHSTART event
 * and release keys to generate SWITCHEND event
 *
 * @author Vladimir Kondrashchenko
 */
public class WinEventHookSample
{
    public static class MyHookFunction extends HookFunction
    {
        public void callback()
        {
            // Here we can process all hook events

            int event = getEvent();
            System.out.println(event);
            if (event == WinEventHook.WinEvent.EVENT_SYSTEM_SWITCHSTART.getValue())
            {
                System.out.println("Event = You have pressed ALT+TAB. The switch window activated.");
            }
            else if (event == WinEventHook.WinEvent.EVENT_SYSTEM_SWITCHEND.getValue())
            {
                System.out.println("Event = You have released ALT+TAB.The switch window disactivated.");
            }
        }
    }

    /**
     * Our window procedure, which handles WM_CLOSE event and calls WinEventHook.unhookWinEvent.
     */
    static class MyWindowProc extends WindowProc
    {
        private int _hookID;

        public MyWindowProc(int hookID)
        {
            _hookID = hookID;
        }

        public void callback()
        {
            switch ((int)_msg.getValue())
            {
                case Msg.WM_CLOSE:
                    {
                        WinEventHook.unhookWinEvent(_hookID);
                        System.out.println("The hook is uninstalled.");
                        super.callback();
                        break;
                    }
                default:
                    {
                        super.callback();
                        break;
                    }
            }
        }
    }

    /**
     * This callback is used to create a thread with message queue.
     */
    public static class MessageQueueThread extends Thread
    {
        private Wnd _wnd;
        private static final String CLASS_NAME = "EmptyWindow";

        public void run()
        {
            WinEventHook.WinEventFlag wef = new WinEventHook.WinEventFlag();
            wef.setOutOfContext(true);
            // TODO [kopijka]: HookFunction is not create from win32 application.
            MyHookFunction myHookFunction = new MyHookFunction();
            int hookID = WinEventHook.setWinEventHook(WinEventHook.WinEvent.EVENT_SYSTEM_SWITCHSTART,
                    WinEventHook.WinEvent.EVENT_SYSTEM_SWITCHEND,
                    new Handle(),
                    myHookFunction,
                    0, // Receive events from all processes.
                    0, // Associate the hook with all existing threads. 
                    wef);
            System.out.println("Installed HookID = " + hookID);

            // Register our window class, using default WindowsProcedure
            WndClass wndClass = new WndClass(new MyWindowProc(hookID), CLASS_NAME);
            wndClass.register();

            // Create a window of our window class
            _wnd = Wnd.createWindow(CLASS_NAME);
            _wnd.eventLoop();
            System.out.println("Finished message queue.");
        }

        public void terminate()
        {
            if (_wnd != null)
            {
                _wnd.postMessage(Msg.WM_CLOSE, 0, 0);
            }
        }
    }

    public static void main(String[] args)
    {
        System.out.println("Installing the hook...");
        MessageQueueThread hookThread = new MessageQueueThread();
        hookThread.start();
        System.out.println("The hook is installed.");
        System.out.println("Press 'Enter' to terminate the sample.");
        try
        {
            System.in.read();
            System.out.println("Stopping the hook...");
            hookThread.terminate();
            hookThread.join();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        System.out.println("Stopped.");
    }
}

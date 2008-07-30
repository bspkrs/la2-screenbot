/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
import com.jniwrapper.win32.hook.*;

import java.io.IOException;

/**
 * This sample demonstrates how to install mouse and keyboard hooks and listen to its events.
 *
 * @author Serge Piletsky
 */
public class HookSample
{
    public static void main(String[] args)
    {
        Hook.MOUSE.addListener(new HookEventListener()
        {
            public void onHookEvent(HookEventObject event)
            {
                MouseEvent mouseHookEvent = (MouseEvent)event;
                System.out.println("Source = " + mouseHookEvent.getSource());
                System.out.println("\tMouseMessageID = " + mouseHookEvent.getMouseMessageID());
                System.out.println("\tpoint.x = " + mouseHookEvent.getPoint().getX());
                System.out.println("\tpoint.y = " + mouseHookEvent.getPoint().getY());
                System.out.println("\tWnd = " + mouseHookEvent.getWindow());
            }
        });

        Hook.KEYBOARD.addListener(new HookEventListener()
        {
            public void onHookEvent(HookEventObject event)
            {
                KeyboardEvent keyboardHookEvent = (KeyboardEvent)event;
                System.out.println("Source = " + keyboardHookEvent.getSource());
                System.out.println("\tVirtualKeyCode = " + keyboardHookEvent.getVirtualKeyCode());
                System.out.println("\tScanCode = " + keyboardHookEvent.getScanCode());
                System.out.println("\tRepeatCount = " + keyboardHookEvent.getRepeatCount());
            }
        });

        Hook.KEYBOARD.install();
        Hook.MOUSE.install();
        System.out.println("Hooks are successfully installed.");

        System.out.println("Press 'Enter' to terminate the sample.");
        try
        {
            System.in.read();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Hook.KEYBOARD.uninstall();
        Hook.MOUSE.uninstall();

        System.out.println("Hooks are successfully uninstalled.");
    }
}

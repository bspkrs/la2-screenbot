/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
import com.jniwrapper.win32.Rect;
import com.jniwrapper.win32.gdi.Icon;
import com.jniwrapper.win32.ui.Wnd;

import javax.swing.JFrame;

/**
 * This sample demonstrates several commonly used features of the {@link Wnd} class.
 *
 * @author Serge Piletsky
 */
public class WndSample
{
    public static void main(String[] args)
    {
        // Disable DirectDraw
        System.getProperties().setProperty("sun.java2d.noddraw", "True");

        // Create sample JFmame
        JFrame testWindow = new JFrame("Test Window");
        testWindow.setSize(640, 480);
        testWindow.setLocationRelativeTo(null);
        testWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        testWindow.setVisible(true);

        // Create child JFrame
        JFrame childWindow = new JFrame("Child Window");
        childWindow.setSize(200, 200);
        childWindow.setLocationRelativeTo(testWindow);
        childWindow.setVisible(true);

        Wnd testWnd = new Wnd(testWindow);
        Wnd childWnd = new Wnd(childWindow);

        // Set the parent window
        childWnd.setParent(testWnd);

        // Get various window attributes, using Wnd class
        String windowText = childWnd.getWindowText();
        System.out.println("windowText = " + windowText);

        String windowClassName = childWnd.getWindowClassName();
        System.out.println("windowClassName = " + windowClassName);

        Wnd parent = testWnd.getParent();
        System.out.println("parent = " + parent);

        Rect windowRect = childWnd.getWindowRect();
        System.out.println("windowRect = " + windowRect);

        // Modify window transparency
        testWnd.setTransparent((byte)128);

        // Change the window icon
        Icon.IconType small = Icon.IconType.SMALL;
        testWnd.setWindowIcon(Icon.SystemIcon.BANG.getIcon(small), small);

        childWnd.setWindowIcon(Icon.SystemIcon.QUES.getIcon(small), small);
    }
}

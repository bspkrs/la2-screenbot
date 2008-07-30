/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
import com.jniwrapper.util.Logger;
import com.jniwrapper.win32.Msg;
import com.jniwrapper.win32.gdi.Icon;
import com.jniwrapper.win32.shell.TrayIcon;
import com.jniwrapper.win32.shell.TrayIconListener;
import com.jniwrapper.win32.shell.TrayMessage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URL;

/**
 * This class demonstrates TrayIcon class features.
 *
 * @author Alexander Kireyev
 * @author Vladimir Kondrashchenko
 */
public class SampleTrayApp
{
    private static final Logger LOG = Logger.getInstance(SampleTrayApp.class);

    private static TrayIcon _trayIcon;
    private static final String MENU_ITEM_SHOW_STANDARD_ICON = "Show Stadard System Icon";
    private static final String MENU_ITEM_SHOW_CUSTOM_ICON = "Show Custom Icon";
    private static final String MENU_ITEM_SHOW_CALLOUT = "Show Tray Balloon";
    private static final String MENU_ITEM_HIDE_ICON = "Hide Tray Icon (hides tray icon for a second)";
    private static final String MENU_ITEM_EXIT = "Exit";

    static
    {
        System.setProperty("javax.swing.adjustPopupLocationToFit", "false");
    }

    public static void main(String[] args)
    {
        new SampleTrayApp();
    }

    public SampleTrayApp() {
        init();
    }

    private void init() {
        _trayIcon = new TrayIcon(Icon.SystemIcon.BANG.getSmall());
        _trayIcon.setToolTip("Tray Tooltip");
        _trayIcon.addTrayListener(new TrayIconListener()
        {
            public void trayActionPerformed(long message, int x, int y)
            {
                if (message == Msg.WM_LBUTTONDBLCLK)
                {
                    _trayIcon.setIcon(Icon.SystemIcon.HAND.getSmall());
                }
                else if (message == Msg.WM_MOUSELEAVE)
                {
                    System.out.println("Mouse leave event");
                }
                else if (message == Msg.WM_MOUSEHOVER)
                {
                    System.out.println("Mouse hover event");
                }
             }
        });

        _trayIcon.setPopupMenu(createPopupMenu());
    }

    private JPopupMenu createPopupMenu()
    {
        JPopupMenu popupMenu = new JPopupMenu("My Menu");
        popupMenu.add(new JMenuItem(new AbstractAction(MENU_ITEM_SHOW_STANDARD_ICON)
        {
            public void actionPerformed(ActionEvent e)
            {
                _trayIcon.setIcon(Icon.SystemIcon.QUES.getSmall());
            }
        }));
        popupMenu.add(new JMenuItem(new AbstractAction(MENU_ITEM_SHOW_CUSTOM_ICON)
        {
            public void actionPerformed(ActionEvent e)
            {
                URL url = SampleTrayApp.class.getResource("/winpack.ico");
                File iconFile = new File(url.getFile());
                Icon customIcon = new Icon(iconFile);
                _trayIcon.setIcon(customIcon);
            }
        }));
        popupMenu.addSeparator();
        popupMenu.add(new JMenuItem(new AbstractAction(MENU_ITEM_SHOW_CALLOUT)
        {
            public void actionPerformed(ActionEvent e)
            {
                _trayIcon.showMessage(new TrayMessage.Warning("Tray Balloon",
                        "This program demonstrates the WinPack for JNIWrapper library abilities:\n" +
                        "\t - tray icon\n" +
                        "\t - menu for tray icon\n" +
                        "\t - balloons from tray icon\n" +
                        "etc."));
            }
        }));
        popupMenu.addSeparator();
        popupMenu.add(new JMenuItem(new AbstractAction(MENU_ITEM_HIDE_ICON)
        {
            public void actionPerformed(ActionEvent e)
            {
                _trayIcon.setVisible(false);
                try
                {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e1)
                {
                    LOG.error("", e1);
                }
                _trayIcon.setVisible(true);
            }
        }));
        popupMenu.addSeparator();
        popupMenu.add(new JMenuItem(new AbstractAction(MENU_ITEM_EXIT)
        {
            public void actionPerformed(ActionEvent e)
            {
                _trayIcon.dispose();
                System.exit(0);
            }
        }));

        return popupMenu;
    }
}

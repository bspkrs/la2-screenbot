/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
import com.jniwrapper.win32.gdi.Region;
import com.jniwrapper.win32.ui.Wnd;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * This sample demonstates how to create custom shape window.
 *
 * @author Serge Piletsky
 */
public class CustomShapeWindowSample
{
    public static void main(String[] args)
    {
        final ImageIcon imageIcon = new ImageIcon(CustomShapeWindowSample.class.getResource("res/cup.gif").getFile());
        final Region windowShape = Region.createFromImage(imageIcon.getImage());

        final JWindow frame = new JWindow();
        frame.setSize(imageIcon.getIconWidth(), imageIcon.getIconHeight());
        frame.setLocationRelativeTo(null);

        JPanel newContentPane = new JPanel()
        {
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                imageIcon.paintIcon(frame.getContentPane(), g, 0, 0);
            }
        };
        newContentPane.setOpaque(true);

        frame.setContentPane(newContentPane);
        frame.setCursor(new Cursor(Cursor.MOVE_CURSOR));

        final Point mousePosition = new Point(0, 0);
        frame.addMouseMotionListener(new MouseMotionListener()
        {
            public void mouseDragged(MouseEvent e)
            {
                int dx = e.getX() - mousePosition.x;
                int dy = e.getY() - mousePosition.y;
                frame.setLocation(frame.getX() + dx, frame.getY() + dy);
            }

            public void mouseMoved(MouseEvent e)
            {
                mousePosition.setLocation(e.getX(), e.getY());
            }
        });

        frame.setVisible(true);

        Wnd wnd = new Wnd(frame);
        wnd.setRegion(windowShape, true);
    }
}

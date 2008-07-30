/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.ui.controls;

import com.jniwrapper.win32.ui.dialogs.ChooseColorDialog;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * ChooseColorField class is a component for selecting colors that uses
 * ChooseColorDialog.
 * 
 * @author Serge Piletsky
 */
public class ChooseColorField extends AbstractChooserField
{
    public static final String PROPERTY_COLOR = "color";

    private JPanel _colorPanel = new JPanel();
    private ChooseColorDialog _dialog;

    public ChooseColorField()
    {
        super();
        remove(getTextField());

        Dimension size = new Dimension(60, 20);
        _colorPanel.setPreferredSize(size);
        _colorPanel.setMinimumSize(size);
        _colorPanel.setMaximumSize(size);
        _colorPanel.setBackground(Color.black);
        _colorPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, Color.white, Color.gray));

        add(_colorPanel, BorderLayout.CENTER);
        _colorPanel.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e)
            {
                //handle double click mouse event
                if (e.getClickCount() == 2)
                {
                    actionPerformed(null);
                }
            }
        });
    }

    public ChooseColorField(Color color)
    {
        this();
        setColor(color);
    }

    public void actionPerformed(ActionEvent e)
    {
        ChooseColorDialog dialog = getDialog();
        Color oldColor = getColor();
        dialog.setColor(oldColor);
        if (dialog.execute())
        {
            Color newColor = dialog.getColor();
            setColor(newColor);
            firePropertyChange(PROPERTY_COLOR, oldColor, newColor);
        }
    }

    /**
     * Returns ChooseColorDialog.
     * 
     * @return dialog instance.
     */
    public ChooseColorDialog getDialog()
    {
        if (_dialog == null)
        {
            Window parent = SwingUtilities.getWindowAncestor(this);
            _dialog = new ChooseColorDialog(parent);
        }
        return _dialog;
    }

    /**
     * Returns a selected color.
     * 
     * @return selected color.
     */
    public Color getColor()
    {
        return _colorPanel.getBackground();
    }

    /**
     * Sets a color.
     * 
     * @param color
     */
    public void setColor(Color color)
    {
        _colorPanel.setBackground(color);
    }
}

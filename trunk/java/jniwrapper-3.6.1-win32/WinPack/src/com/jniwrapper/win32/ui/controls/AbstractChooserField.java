/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.ui.controls;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class represents combined megawidget which contains
 * text field and button.
 *
 * @author Serge Piletsky
 */
public class AbstractChooserField extends JPanel implements ActionListener
{
    private JTextField _textField = new JTextField();
    private JButton _selectButton = new JButton("...");

    public AbstractChooserField()
    {
        Dimension preferredSize = new Dimension(150, 20);
        _textField.setPreferredSize(preferredSize);
        _textField.setMinimumSize(preferredSize);
        _selectButton.setPreferredSize(new Dimension(20, 20));

        setLayout(new BorderLayout());
        add(_textField, BorderLayout.CENTER);
        add(_selectButton, BorderLayout.EAST);
        _selectButton.addActionListener(this);
    }

    public JTextField getTextField()
    {
        return _textField;
    }

    public JButton getSelectButton()
    {
        return _selectButton;
    }

    public void setEnabled(boolean enabled)
    {
        _textField.setEnabled(enabled);
        _selectButton.setEnabled(enabled);
    }

    public void actionPerformed(ActionEvent e)
    {
    }
}

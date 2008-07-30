/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.ui.controls;

import com.jniwrapper.win32.ui.dialogs.SelectIconDialog;
import com.jniwrapper.win32.shell.ShellIcon;
import com.jniwrapper.win32.gdi.Icon;
import com.jniwrapper.win32.LastErrorException;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

/**
 * ChooseIconField class is a component for selecting icon that uses
 * SelectIconDialog.
 *
 * @author Vladimir Kondrashchenko
 */
public class ChooseIconField extends AbstractChooserField
{
    public static final String PROPERTY_ICON = "icon";

    private JLabel _lblIcon = new JLabel();
    private SelectIconDialog _dialog;

    public ChooseIconField()
    {
        super();
        remove(getTextField());

        JPanel bevel1 = new JPanel();
        bevel1.setBorder(BorderFactory.createEtchedBorder());
        bevel1.setLayout(new GridBagLayout());
        getSelectButton().setBorder(BorderFactory.createEtchedBorder());

        Dimension size = new Dimension(36, 38);
        _lblIcon.setPreferredSize(size);
        _lblIcon.setMinimumSize(size);
        _lblIcon.setMaximumSize(size);
        bevel1.setBackground(Color.WHITE);
        _lblIcon.addMouseListener(new MouseAdapter()
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
        bevel1.add(_lblIcon, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                , GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 3, 0, 0), 0, 0));
        add(bevel1, BorderLayout.CENTER);
    }

    public ChooseIconField(File iconFile)
    {
        this();
        if (iconFile.exists())
        {
            setIconImage(new ShellIcon(iconFile));
            setIconFile(iconFile);
        }
    }

    public ChooseIconField(File iconFile, int iconIndex)
    {
        this();
        if (iconFile.exists())
        {
            setIconImage(new ShellIcon(iconFile, iconIndex));
            setIconFile(iconFile);
            setIconIndex(iconIndex);
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        SelectIconDialog dialog = getDialog();
        if (dialog.getOwner() == null)
            dialog.setOwner(SwingUtilities.getWindowAncestor(this));
        String oldIcon = dialog.getIconFile().getAbsolutePath()+Integer.toString(dialog.getIconIndex());
        if (dialog.execute())
        {
            setIconImage(dialog.getShellIcon());
            String newIcon = dialog.getIconFile().getAbsolutePath()+Integer.toString(dialog.getIconIndex());
            firePropertyChange(PROPERTY_ICON, oldIcon, newIcon);
        }
    }

    private void setIconImage(ShellIcon shellIcon)
    {
        try
        {
            final Icon icon = shellIcon;
            final ImageIcon imageIcon = new ImageIcon(icon.toImage());
            _lblIcon.setIcon(imageIcon);
        }
        catch (LastErrorException ex)
        {

        }
    }

    public void setIconImage(File iconFile, int iconIndex)
    {
        setIconFile(iconFile);
        setIconIndex(iconIndex);
        setIconImage(getShellIcon());
    }

    /**
     * Returns ChooseIconDialog.
     *
     * @return dialog instance.
     */
    public SelectIconDialog getDialog()
    {
        if (_dialog == null)
        {
            _dialog = new SelectIconDialog();
        }
        return _dialog;
    }

    public void setIconFile(File value)
    {
        getDialog().setIconFile(value);
    }

    public File getIconFile()
    {
        return getDialog().getIconFile();
    }

    public void setIconIndex(int value)
    {
        getDialog().setIconIndex(value);
    }

    public int getIconIndex()
    {
        return getDialog().getIconIndex();
    }

    public ShellIcon getShellIcon()
    {
        return getDialog().getShellIcon();
    }
}

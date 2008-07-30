/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.ui.controls;

import com.jniwrapper.win32.ui.dialogs.OpenSaveFileDialog;

import javax.swing.SwingUtilities;
import java.awt.Window;
import java.awt.event.ActionEvent;

/**
 * SelectFileField class is a component for selecting a file that uses
 * OpenSaveFileDialog.
 * 
 * @author Serge Piletsky
 */
public class SelectFileField extends AbstractChooserField
{
    public static final String PROPERTY_FILE = "file";

    public static final int MODE_OPEN_FILE = 0;
    public static final int MODE_SAVE_FILE = 1;

    private int _mode = MODE_OPEN_FILE;
    private OpenSaveFileDialog _dialog;

    public SelectFileField()
    {
    }

    public SelectFileField(int mode)
    {
        _mode = mode;
    }

    /**
     * Returns the dialog mode.
     * 
     * @return dialog mode.
     */
    public int getMode()
    {
        return _mode;
    }

    /**
     * Sets the dialog mode.
     * 
     * @param mode is a dialog mode.
     */
    public void setMode(int mode)
    {
        _mode = mode;
    }

    /**
     * Returns a selected file.
     * 
     * @return selected file.
     */
    public String getFileName()
    {
        return getTextField().getText();
    }

    /**
     * Sets the file name.
     * 
     * @param value is file name.
     */
    public void setFileName(String value)
    {
        getTextField().setText(value);
    }

    public void actionPerformed(ActionEvent e)
    {
        OpenSaveFileDialog dialog = getDialog();
        boolean executed;
        if (getMode() == MODE_OPEN_FILE)
        {
            executed = dialog.getOpenFileName();
        }
        else if (getMode() == MODE_SAVE_FILE)
        {
            executed = dialog.getSaveFileName();
        }
        else
            throw new IllegalArgumentException();

        if (executed)
        {
            String oldFileName = getFileName();
            String newFileName = dialog.getFileName();
            setFileName(newFileName);
            firePropertyChange(PROPERTY_FILE, oldFileName, "");
            firePropertyChange(PROPERTY_FILE, oldFileName, newFileName);
        }
    }

    /**
     * Returns the dialog.
     * 
     * @return dialog.
     */
    public OpenSaveFileDialog getDialog()
    {
        if (_dialog == null)
        {
            Window parent = SwingUtilities.getWindowAncestor(this);
            _dialog = new OpenSaveFileDialog(parent);
        }
        return _dialog;
    }
}

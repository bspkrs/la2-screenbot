/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.ui.dialogs;

import com.jniwrapper.win32.ui.Wnd;
import com.jniwrapper.win32.shell.Shell32;
import com.jniwrapper.win32.shell.ShellIcon;
import com.jniwrapper.win32.system.Kernel32;
import com.jniwrapper.*;

import java.io.File;
import java.awt.Window;

/**
 * @author Vladimir Kondrashchenko
 */
public class SelectIconDialog
{
    private static final String FUNCTION_PICK_ICON_DIALOG = "PickIconDlg";

    private static final String DEFAULT_ICON_FILE_NAME = Kernel32.getSystemDirectory() + "shell32.dll";
    private File _iconFile = new File(DEFAULT_ICON_FILE_NAME);
    private int _iconIndex = 0;
    private Window _owner = null;

    public SelectIconDialog()
    {

    }

    public SelectIconDialog(File iconFile)
    {
        setIconFile(iconFile);
    }

    public SelectIconDialog(File iconFile, int iconIndex)
    {
        setIconFile(iconFile);
        setIconIndex(iconIndex);
    }

    public SelectIconDialog(Window owner)
    {
        setOwner(owner);
    }

    public SelectIconDialog(Window owner, File iconFile)
    {
        setOwner(owner);
        setIconFile(iconFile);
    }

    public SelectIconDialog(Window owner, File iconFile, int iconIndex)
    {
        setOwner(owner);
        setIconFile(iconFile);
        setIconIndex(iconIndex);
    }

    public void setIconFile(File value)
    {
        if (value.isFile())
        {
            _iconFile = value;
        }
        else
        {
            _iconFile = new File(DEFAULT_ICON_FILE_NAME);
        }
    }

    public File getIconFile()
    {
        return _iconFile;
    }

    public void setIconIndex(int value)
    {
        _iconIndex = value;
    }

    public int getIconIndex()
    {
        return _iconIndex;
    }

    public ShellIcon getShellIcon()
    {
        if (_iconFile.exists())
            return new ShellIcon(_iconFile, _iconIndex);
        else
            return null;
    }

    public void setOwner(Window value)
    {
        _owner = value;
    }

    public Window getOwner()
    {
        return _owner;
    }

    public boolean execute()
    {
        Wnd owner = _owner != null ? new Wnd(_owner) : new Wnd();

        final Function pickIconDialog = Shell32.getInstance().getFunction(FUNCTION_PICK_ICON_DIALOG);

        Int result = new Int();
        Str sFileName = new Str();
        sFileName.setValue(getIconFile().getAbsolutePath());
        LongInt nBuf = new LongInt(sFileName.getMaxLength());
        LongInt nIconIndex = new LongInt(getIconIndex());

        if (_owner != null)
            DialogHelper.invokeDialog(_owner, pickIconDialog, result, new Parameter[]{owner, new Pointer(sFileName),
                                                                                      nBuf, new Pointer(nIconIndex)});
        else
            pickIconDialog.invoke(result, owner, new Pointer(sFileName), nBuf, new Pointer(nIconIndex));

        if (result.getValue() != 0)
        {
            setIconFile(new File(sFileName.getValue()));
            setIconIndex((int)nIconIndex.getValue());
            if (getIconFile().isFile())
                return true;
            else
                return false;
        }
        else
        {
            return false;
        }
    }
}

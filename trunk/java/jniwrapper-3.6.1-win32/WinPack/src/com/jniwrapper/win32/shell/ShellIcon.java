/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.shell;

import com.jniwrapper.*;
import com.jniwrapper.win32.FunctionName;
import com.jniwrapper.win32.gdi.Icon;
import com.jniwrapper.win32.system.Module;

import java.io.File;

/**
 * This class provides functionality for extracting icons for file associations.
 * 
 * @author Alexander Evsukov
 */
public class ShellIcon extends Icon
{
    private static final FunctionName FUNCTION_EXTRACT_ICON_EX = new FunctionName("ExtractIconEx");
    private static final FunctionName FUNCTION_EXTRACT_ASSOCIATED_ICON = new FunctionName("ExtractAssociatedIcon");

    private int _index = -1;

    static Function getFunction(Object functionName)
    {
        return Shell32.getInstance().getFunction(functionName.toString());
    }

    /**
     * Retrieves an icon from the specified executable file.
     * 
     * @param file is a file name to extract the icon from.
     * @param iconIndex a zero-based index of the icon to retrieve.
     */
    public ShellIcon(File file, int iconIndex)
    {
        this(file, iconIndex, Icon.IconType.BIG);
    }

    public ShellIcon(File file, int iconIndex, Icon.IconType iconType)
    {
        if (iconType == null)
        {
            throw new IllegalArgumentException("Illegal icon type.");
        }

        final Function extractIconFunctionEx = getFunction(FUNCTION_EXTRACT_ICON_EX.toString());
        if (iconType.equals(Icon.IconType.SMALL))
        {
            extractIconFunctionEx.invoke(null, new Parameter[]
                    {
                            new Str(file.getAbsolutePath()),
                            new UInt(iconIndex),
                            new Pointer.Void(),
                            new Pointer(this),
                            new UInt(1)
                    });
        }
        else
        {
            extractIconFunctionEx.invoke(null, new Parameter[]
                    {
                            new Str(file.getAbsolutePath()),
                            new UInt(iconIndex),
                            new Pointer(this),
                            new Pointer.Void(),
                            new UInt(1)
                    });
        }
    }

    /**
     * Extracts an associated icon found in a file or an icon found in an
     * associated executable file.
     * 
     * @param iconPath a full path that contains the icon.
     */
    public ShellIcon(File iconPath)
    {
        final Function extractIconFunction = getFunction(FUNCTION_EXTRACT_ASSOCIATED_ICON.toString());
        extractIconFunction.invoke(this,
                Module.getCurrent(),
                new Str(iconPath.getAbsolutePath()),
                new Pointer(new UInt16(_index)));
    }

    /**
     * Extracts a small associated icon.
     * 
     * @param iconPath a full path that contains the icon.
     */
    public ShellIcon(String iconPath)
    {
        SHFileInfo fileInfo = SHFileInfo.getFileInfo(iconPath, SHFileInfo.SHGFI_SMALLICON | SHFileInfo.SHGFI_ICON);
        this.setValue(fileInfo.getIcon().getValue());
    }

    /**
     * Returns the index of the extracted icon.
     * 
     * @return the index of the extracted icon.
     */
    public int getIndex()
    {
        return _index;
    }

}

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
import com.jniwrapper.win32.Handle;
import com.jniwrapper.win32.gdi.Icon;

/**
 * SHFileInfo class represents SHFILEINFO structure, both ANSI and Unicode
 * kinds.
 * 
 * @author Serge Piletsky
 */
public class SHFileInfo extends Structure
{
    static final FunctionName FUNCTION_GET_FILE_INFO = new FunctionName("SHGetFileInfo");

    public static final int SHGFI_ICON              = 0x000000100; // get icon
    public static final int SHGFI_DISPLAYNAME       = 0x000000200; // get display name
    public static final int SHGFI_TYPENAME          = 0x000000400; // get type name
    public static final int SHGFI_ATTRIBUTES        = 0x000000800; // get attributes
    public static final int SHGFI_ICONLOCATION      = 0x000001000; // get icon location
    public static final int SHGFI_EXETYPE           = 0x000002000; // return exe type
    public static final int SHGFI_SYSICONINDEX      = 0x000004000; // get system icon index
    public static final int SHGFI_LINKOVERLAY       = 0x000008000; // put a link overlay on icon
    public static final int SHGFI_SELECTED          = 0x000010000; // show icon in selected state
    public static final int SHGFI_ATTR_SPECIFIED    = 0x000020000; // get only specified attributes
    public static final int SHGFI_LARGEICON         = 0x000000000; // get large icon
    public static final int SHGFI_SMALLICON         = 0x000000001; // get small icon
    public static final int SHGFI_OPENICON          = 0x000000002; // get open icon
    public static final int SHGFI_SHELLICONSIZE     = 0x000000004; // get shell size icon
    public static final int SHGFI_PIDL              = 0x000000008; // pszPath is a pidl
    public static final int SHGFI_USEFILEATTRIBUTES = 0x000000010; // use passed dwFileAttribute

    static final int MAX_PATH = 260;

    private Icon _icon = new Icon();
    private Int _iconIndex = new Int();
    private UInt32 _attributes = new UInt32();
    private Str _displayName = new Str(MAX_PATH);
    private Str _typeName = new Str(80);

    private SHFileInfo()
    {
        init(new Parameter[]{_icon, _iconIndex, _attributes, _displayName, _typeName});
    }

    private SHFileInfo(SHFileInfo that)
    {
        this();
        initFrom(that);
    }


    /**
     * Returns shell icon.
     *
     * @return icon
     */
    public Icon getIcon()
    {
        return _icon;
    }

    /**
     * Returns icon index.
     *
     * @return icon index
     */
    public int getIconIndex()
    {
        return (int)_iconIndex.getValue();
    }

    /**
     * Returns display name.
     *
     * @return display name
     */
    public String getDisplayName()
    {
        return _displayName.getValue();
    }

    public Object clone()
    {
        return new SHFileInfo(this);
    }

    /**
     * Retrieves file information.
     *
     * @param path is a file path
     * @param fileAttributes
     * @param flags
     * @return SHFileInfo
     */
    public static SHFileInfo getFileInfo(String path, int fileAttributes, int flags)
    {
        SHFileInfo result = new SHFileInfo();
        final Shell32 shell32 = Shell32.getInstance();
        Function function = shell32.getFunction(FUNCTION_GET_FILE_INFO.toString());
        function.invoke(null, new Parameter[]
        {
            new Str(path),
            new UInt32(fileAttributes),
            new Pointer(result),
            new UInt(result.getLength()),
            new UInt(flags)
        });
        return result;
    }
    
    /**
     * Retrieves file information. Should be used for system folders.
     * 
     * @param handle
     * @param fileAttributes
     * @param flags flags used to fill result structure. NOTE: SHFileInfo.SHGFI_PIDL added automatically
     * @return
     */
    public static SHFileInfo getFileInfo(Handle handle,
            int fileAttributes, int flags)
    {
        flags = flags | SHFileInfo.SHGFI_PIDL;
        SHFileInfo result = new SHFileInfo();
        final Shell32 shell32 = Shell32.getInstance();
        Function function = shell32.getFunction(FUNCTION_GET_FILE_INFO.toString());
        UInt32 ret = new UInt32();
        function.invoke(ret, new Parameter[] { 
                handle,
                new UInt32(fileAttributes), 
                new Pointer(result),
                new UInt(result.getLength()), 
                new UInt(flags) 
        });
        return result;
    }

    /**
     * Retrieves file information.
     * 
     * @param path is a file path
     * @param flags
     * @return SHFileInfo
     */
    public static SHFileInfo getFileInfo(String path, int flags)
    {
        return getFileInfo(path, 0, flags);
    }
}

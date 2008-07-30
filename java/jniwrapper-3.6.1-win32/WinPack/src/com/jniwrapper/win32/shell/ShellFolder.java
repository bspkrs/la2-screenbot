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
import com.jniwrapper.util.EnumItem;
import com.jniwrapper.win32.FunctionName;
import com.jniwrapper.win32.Handle;

/**
 * This class provides access to standard Windows shell folders.
 * <p><b>Note</b>: This functionality is available for Windows 98 / 2000 / XP.<br>
 * Under Windows NT 4.0 or Windows 95 Microsoft Internet Explorer with <b>Desktop
 * Update</b> must be installed.
 * 
 * @author Serge Piletsky
 */
public class ShellFolder extends EnumItem
{
    static final FunctionName FUNCTION_GET_PATH_FROM_IDLIST = new FunctionName("SHGetPathFromIDList");
    static final FunctionName FUNCTION_GET_SPECIAL_FOLDER_PATH = new FunctionName("SHGetSpecialFolderPath");
    static final String FUNCTION_GET_SPECIAL_FOLDER_LOCATION = "SHGetSpecialFolderLocation";

    static final int MAX_PATH = 260;

    public static final ShellFolder ADMINTOOLS = new ShellFolder(0x0030);
    public static final ShellFolder ALTSTARTUP = new ShellFolder(0x001d);
    public static final ShellFolder APPDATA = new ShellFolder(0x001a);
    public static final ShellFolder BITBUCKET = new ShellFolder(0x000a, true);
    public static final ShellFolder CDBURN_AREA = new ShellFolder(0x003b);
    public static final ShellFolder COMMON_ADMINTOOLS = new ShellFolder(0x002f);
    public static final ShellFolder COMMON_ALTSTARTUP = new ShellFolder(0x001e);
    public static final ShellFolder COMMON_APPDATA = new ShellFolder(0x0023);
    public static final ShellFolder COMMON_DESKTOPDIRECTORY = new ShellFolder(0x0019);
    public static final ShellFolder COMMON_DOCUMENTS = new ShellFolder(0x002e);
    public static final ShellFolder COMMON_FAVORITES = new ShellFolder(0x001f);
    public static final ShellFolder COMMON_MUSIC = new ShellFolder(0x0035);
    public static final ShellFolder COMMON_PICTURES = new ShellFolder(0x0036);
    public static final ShellFolder COMMON_PROGRAMS = new ShellFolder(0x0017);
    public static final ShellFolder COMMON_STARTMENU = new ShellFolder(0x0016);
    public static final ShellFolder COMMON_STARTUP = new ShellFolder(0x0018);
    public static final ShellFolder COMMON_TEMPLATES = new ShellFolder(0x002d);
    public static final ShellFolder COMMON_VIDEO = new ShellFolder(0x0037);
    public static final ShellFolder CONTROLS = new ShellFolder(0x0003, true);
    public static final ShellFolder COOKIES = new ShellFolder(0x0021);
    public static final ShellFolder DESKTOP = new ShellFolder(0x0000, true);
    public static final ShellFolder DESKTOPDIRECTORY = new ShellFolder(0x0010);
    public static final ShellFolder DRIVES = new ShellFolder(0x0011, true);
    public static final ShellFolder FAVORITES = new ShellFolder(0x0006);
    public static final ShellFolder FONTS = new ShellFolder(0x0014);
    public static final ShellFolder HISTORY = new ShellFolder(0x0022);
    public static final ShellFolder INTERNET = new ShellFolder(0x0001, true);
    public static final ShellFolder INTERNET_CACHE = new ShellFolder(0x0020);
    public static final ShellFolder LOCAL_APPDATA = new ShellFolder(0x001c);
    public static final ShellFolder MYDOCUMENTS = new ShellFolder(0x000c, true);
    public static final ShellFolder MYMUSIC = new ShellFolder(0x000d);
    public static final ShellFolder MYPICTURES = new ShellFolder(0x0027);
    public static final ShellFolder MYVIDEO = new ShellFolder(0x000e);
    public static final ShellFolder NETHOOD = new ShellFolder(0x0013);
    public static final ShellFolder NETWORK = new ShellFolder(0x0012);
    public static final ShellFolder PERSONAL = new ShellFolder(0x0005);
    public static final ShellFolder PRINTERS = new ShellFolder(0x0004, true);
    public static final ShellFolder PROFILE = new ShellFolder(0x0028);
    public static final ShellFolder PROFILES = new ShellFolder(0x003e);
    public static final ShellFolder PROGRAM_FILES = new ShellFolder(0x0026);
    public static final ShellFolder PROGRAM_FILES_COMMON = new ShellFolder(0x002b);
    public static final ShellFolder PROGRAMS = new ShellFolder(0x0002);
    public static final ShellFolder RECENT = new ShellFolder(0x0008);
    public static final ShellFolder SEND_TO = new ShellFolder(0x0009);
    public static final ShellFolder START_MENU = new ShellFolder(0x000b);
    public static final ShellFolder STARTUP = new ShellFolder(0x0007);
    public static final ShellFolder SYSTEM = new ShellFolder(0x0025);
    public static final ShellFolder TEMPLATES = new ShellFolder(0x0015);
    public static final ShellFolder WINDOWS = new ShellFolder(0x0024);
    public static final ShellFolder COMPUTER = new ShellFolder(0x0011);
    public static final ShellFolder NET_CONNECTIONS = new ShellFolder(0x0031);

    private boolean _virtual;
    private String _absolutePath;

    /**
     * 
     * @param folderID identifying a standard shell folder.
     * @param virtual specifies that the folder is virtual.
     */
    private ShellFolder(int folderID, boolean virtual)
    {
        super(folderID);
        try
        {
            _absolutePath = getSpecialFolderPath(folderID);
        }
        catch (Exception e)
        {
        }
        _virtual = virtual;
    }

    /**
     * 
     * @param folderID identifying a standard shell folder.
     */
    private ShellFolder(int folderID)
    {
        this(folderID, false);
    }

    /**
     * 
     * @return folder ID.
     */
    public int getFolderID()
    {
        return getValue();
    }

    /**
     * 
     * @return true, if the folder is virtual; otherwise false.
     */
    public boolean isVirtual()
    {
        return _virtual;
    }

    /**
     * Returns absolute path of the shell folder. If the folder does not exist,
     * returns empty string.
     * 
     * @return absolute path of the shell folder.
     */
    public String getAbsolutePath()
    {
        return _absolutePath;
    }

    /**
     * Retrieves ID list for the shell folder specified by folderID.
     * 
     * @param folderID is a shell folder ID.
     * @return handle to ID list.
     */
    public static Handle getFolderIDList(int folderID)
    {
        Handle result = new Handle();
        final Function getSpecialFolderLocation = Shell32.getInstance().getFunction(FUNCTION_GET_SPECIAL_FOLDER_LOCATION);
        getSpecialFolderLocation.invoke(null, new Handle(), new Int32(folderID), new Pointer.OutOnly(result));
        return result;
    }

    /**
     * Retrieves absolute folder path from ID list.
     * 
     * @param idList is a handle to ID list.
     * @return absolute path retrieved from a specified ID list.
     */
    public static String getPathFromIDList(Handle idList)
    {
        if (idList.isNull())
            throw new IllegalArgumentException("Folder id list is null");

        final Function getPathFromIDList = Shell32.getInstance().getFunction(FUNCTION_GET_PATH_FROM_IDLIST.toString());
        Str folderPath = new Str("", MAX_PATH);
        Bool retValue = new Bool();
        getPathFromIDList.invoke(retValue, idList, new Pointer(folderPath));
        if (!retValue.getValue())
        {
            return "";
        }
        return folderPath.getValue();
    }

    /**
     * Retrieves absolute path of shell folder. If the folder specified by
     * folderID is virtual, the returned path is empty.
     * 
     * @param folderID shell folder ID.
     * @param create flag to create a folder.
     * @return absolute path of the shell folder.
     */
    public static String getSpecialFolderPath(int folderID, boolean create)
    {
        final Function getSpecialFolderPath = Shell32.getInstance().getFunction(FUNCTION_GET_SPECIAL_FOLDER_PATH.toString());
        Bool result = new Bool();
        Str folderPath = new Str("", MAX_PATH);
        getSpecialFolderPath.invoke(result, new Handle(), folderPath, new Int32(folderID), new Bool(create));
        return folderPath.getValue();
    }

    /**
     * Retrieves absolute path of the shell folder. If the folder specified by
     * folderID is virtual, the returned path is empty.
     * 
     * @param folderID shell folder ID.
     * @return absolute path of the shell folder.
     */
    public static String getSpecialFolderPath(int folderID)
    {
        return getSpecialFolderPath(folderID, false);
    }

    /**
     * Returns true if the shell folder functionality is available for current
     * operating system. The functionality is available for Windows 98 / 2000 /
     * XP,<br> Windows NT 4.0 or Windows 95 (installed Microsoft Internet
     * Explorer with <b>Desktop Update</b>).
     * 
     * @return true, if the shell folder functionality is available for the
     * current operating system.
     */
    public static boolean isFunctionalityAvailable()
    {
        try
        {
            Shell32.getInstance().getFunction(FUNCTION_GET_SPECIAL_FOLDER_PATH.toString());
            return true;
        }
        catch (NoSuchFunctionException e)
        {
            return false;
        }
    }
}

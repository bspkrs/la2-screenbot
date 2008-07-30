/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.shell;

import com.jniwrapper.Function;
import com.jniwrapper.LongInt;
import com.jniwrapper.Pointer;
import com.jniwrapper.UInt;
import com.jniwrapper.win32.registry.RegistryKey;
import com.jniwrapper.win32.registry.RegistryKeyType;
import com.jniwrapper.win32.registry.RegistryKeyValues;
import com.jniwrapper.win32.registry.RegistryException;

import java.io.File;

/**
 * This class provides functionality for creating file type associations.
 *
 * @author Vladimir Kondrashchenko
 */
public class FileTypeAssociation
{
    private static final String FUNCTION_SHCHANGENOTIFY = "SHChangeNotify";

    private static final long SHCNE_ASSOCCHANGED = 0x08000000L;
    private static final long SHCNF_IDLIST = 0x0000;

    private String _extension;

    /**
     * Creates a class instance for associating files of the specified type.
     *
     * @param extension specifies the file type by its extension.
     */
    public FileTypeAssociation(String extension)
    {
        if (extension.startsWith("."))
        {
            _extension = extension;
        }
        else
        {
            _extension = "." + extension;
        }
    }

    /**
     * Creates a file type association.
     *
     * @param executable an associated file will be passed as a parameter of this executable file.
     * @param progID the program identifier in the registry. If the specified program identifier does not exist, it
     *               will be created.
     */
    public void createAssociation(File executable, String progID)
    {
        if (!executable.isFile())
        {
            throw new IllegalArgumentException("File not found.");
        }
        String commandLine = "\"" + executable.getAbsolutePath() + "\" \"%1\"";
        createAssociation(commandLine, progID);
    }

    /**
     * Creates a file type association.
     *
     * @param commandLine specifies an executable command.
     * @param progID the executable program identifier in the registry. If the specified program identifier does not exist, it
     *               will be created.
     */
    public void createAssociation(String commandLine, String progID)
    {
        RegistryKey registryKey = RegistryKey.CLASSES_ROOT.createSubKey(progID+"\\shell\\open\\command", true);
        RegistryKeyValues values = registryKey.values();
        if (commandLine != null)
        {
            values.put("", commandLine);
        }
        else
        {
            values.put("", "");
        }
        registryKey.close();
        registryKey = RegistryKey.CLASSES_ROOT.createSubKey(getExtension(), true);
        values = registryKey.values();
        values.put("", progID, RegistryKeyType.SZ);
        registryKey.close();
        changeNotify();
    }

    /**
     * Removes all associations for the file extension.
     */
    public void removeAssociation()
    {
        try
        {
            RegistryKey.CLASSES_ROOT.deleteSubKey(getExtension());
        }
        catch(RegistryException e)
        {
        }
        try
        {
            RegistryKey.CURRENT_USER.openSubKey("Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\FileExts").
                    deleteSubKey(getExtension());
        }
        catch(RegistryException e)
        {
        }
        try
        {
            RegistryKey.LOCAL_MACHINE.openSubKey("SOFTWARE\\Classes").deleteSubKey(getExtension());
        }
        catch (RegistryException e)
        {
        }
        changeNotify();
    }

    /**
     * Returns the default executable command for the file extension.
     *
     * @return the default executable command for the file extension.
     */
    public String getDefaultCommand()
    {
        RegistryKey key = RegistryKey.CLASSES_ROOT.openSubKey(getProgID());
        key = key.openSubKey("\\shell\\open\\command");
        RegistryKeyValues values = key.values();
        return values.get("").toString();
    }

    /**
     * Returns the program identifier of the default executable program.
     *
     * @return the program identifier of the default executable program.
     */
    public String getProgID()
    {
        RegistryKeyValues values = RegistryKey.CLASSES_ROOT.openSubKey(getExtension()).values();
        return values.get("").toString();
    }

    /**
     * Returns <code>true</code> if a file type association for the specified extension is registered.
     *
     * @return <code>true</code> if a file type association for the specified extension is registered.
     */
    public boolean isRegistered()
    {
        try
        {
            RegistryKey.CLASSES_ROOT.openSubKey(getExtension());
        }
        catch (RegistryException e)
        {
            return false;
        }
        return true;
    }

    /**
     * Returns the file extension.
     *
     * @return the file extension.
     */
    public String getExtension()
    {
        return _extension;
    }

    private void changeNotify()
    {
        Function function = Shell32.getInstance().getFunction(FUNCTION_SHCHANGENOTIFY.toString());
        function.invoke(null,
                new LongInt(SHCNE_ASSOCCHANGED),
                new UInt(SHCNF_IDLIST),
                new Pointer(null, true),
                new Pointer(null, true));
    }
}

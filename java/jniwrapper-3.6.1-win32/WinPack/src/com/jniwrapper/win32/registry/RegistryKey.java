/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.registry;

import com.jniwrapper.Str;
import com.jniwrapper.UInt32;
import com.jniwrapper.util.Logger;
import com.jniwrapper.win32.Handle;

import java.util.*;

/**
 * Class RegistryKey represents a key level node in the Windows registry.
 * Values for a registry key are managed by an {@link RegistryKeyValues} instance
 * obtained from the {@link #values()} method.
 *
 * @author Serge Piletsky
 */
public class RegistryKey extends Handle
{
    /**
     * Windows registry base key HKEY_CLASSES_ROOT.
     */
    public static final RegistryKey CLASSES_ROOT = new RegistryKey(0x80000000, "HKEY_CLASSES_ROOT");

    /**
     * Windows registry base key HKEY_CURRENT_USER.
     */
    public static final RegistryKey CURRENT_USER = new RegistryKey(0x80000001, "HKEY_CURRENT_USER");

    /**
     * Windows registry base key HKEY_LOCAL_MACHINE.
     */
    public static final RegistryKey LOCAL_MACHINE = new RegistryKey(0x80000002, "HKEY_LOCAL_MACHINE");

    /**
     * Windows registry base key HKEY_USERS.
     */
    public static final RegistryKey USERS = new RegistryKey(0x80000003, "HKEY_USERS");

    /**
     * Windows registry base key HKEY_PERFORMANCE_DATA.
     */
    public static final RegistryKey PERFORMANCE_DATA = new RegistryKey(0x80000004, "HKEY_PERFORMANCE_DATA");

    /**
     * Windows registry base key HKEY_CURRENT_CONFIG.
     */
    public static final RegistryKey CURRENT_CONFIG = new RegistryKey(0x80000005, "HKEY_CURRENT_CONFIG");

    /**
     * Windows registry base key HKEY_DYN_DATA.
     */
    public static final RegistryKey DYN_DATA = new RegistryKey(0x80000006, "HKEY_DYN_DATA");

    private static final Logger LOG = Logger.getInstance(RegistryKey.class);

    public static final int REG_NOTIFY_CHANGE_NAME = 0x00000001;
    public static final int REG_NOTIFY_CHANGE_ATTRIBUTES = 0x00000002;
    public static final int REG_NOTIFY_CHANGE_LAST_SET = 0x00000004;
    public static final int REG_NOTIFY_CHANGE_SECURITY = 0x00000008;

    static final int DELETE = 0x00010000;
    static final int READ_CONTROL = 0x00020000;
    static final int WRITE_DAC = 0x00040000;
    static final int WRITE_OWNER = 0x00080000;
    static final int SYNCHRONIZE = 0x00100000;
    static final int STANDARD_RIGHTS_REQUIRED = 0x000F0000;
    static final int STANDARD_RIGHTS_READ = READ_CONTROL;
    static final int STANDARD_RIGHTS_WRITE = READ_CONTROL;
    static final int STANDARD_RIGHTS_EXECUTE = READ_CONTROL;
    static final int STANDARD_RIGHTS_ALL = 0x001F0000;
    static final int SPECIFIC_RIGHTS_ALL = 0x0000FFFF;
    static final int ACCESS_SYSTEM_SECURITY = 0x01000000;
    static final int MAXIMUM_ALLOWED = 0x02000000;
    static final int GENERIC_READ = 0x80000000;
    static final int GENERIC_WRITE = 0x40000000;
    static final int GENERIC_EXECUTE = 0x20000000;
    static final int GENERIC_ALL = 0x10000000;

    static final int KEY_QUERY_VALUE = 0x0001;
    static final int KEY_SET_VALUE = 0x0002;
    static final int KEY_CREATE_SUB_KEY = 0x0004;
    static final int KEY_ENUMERATE_SUB_KEYS = 0x0008;
    static final int KEY_NOTIFY = 0x0010;
    static final int KEY_CREATE_LINK = 0x0020;

    static final int KEY_READ = (STANDARD_RIGHTS_READ | KEY_QUERY_VALUE | KEY_ENUMERATE_SUB_KEYS | KEY_NOTIFY) & ~SYNCHRONIZE;
    static final int KEY_WRITE = (STANDARD_RIGHTS_WRITE | KEY_SET_VALUE | KEY_CREATE_SUB_KEY) & ~SYNCHRONIZE;
    static final int KEY_EXECUTE = KEY_READ & ~SYNCHRONIZE;
    static final int KEY_ALL_ACCESS = (STANDARD_RIGHTS_ALL | KEY_QUERY_VALUE | KEY_SET_VALUE | KEY_CREATE_SUB_KEY |
            KEY_ENUMERATE_SUB_KEYS | KEY_NOTIFY | KEY_CREATE_LINK) & ~SYNCHRONIZE;

    static final int REG_OPTION_RESERVED = 0x00000000;
    static final int REG_OPTION_NON_VOLATILE = 0x00000000;
    static final int REG_OPTION_VOLATILE = 0x00000001;
    static final int REG_OPTION_CREATE_LINK = 0x00000002;
    static final int REG_OPTION_BACKUP_RESTORE = 0x00000004;
    static final int REG_OPTION_OPEN_LINK = 0x00000008;
    static final int REG_LEGAL_OPTION = REG_OPTION_RESERVED | REG_OPTION_NON_VOLATILE |
            REG_OPTION_VOLATILE | REG_OPTION_CREATE_LINK |
            REG_OPTION_BACKUP_RESTORE | REG_OPTION_OPEN_LINK;

    static final int NO_ERROR = 0;
    static final int ERROR_FILE_NOT_FOUND = 2;

    static final long REG_CREATED_NEW_KEY = 0x00000001L;
    static final long REG_OPENED_EXISTING_KEY = 0x00000002L;

    static final int MAX_PATH = 260;

    private String _path = "";
    private String _name = "";
    private RegistryKey _rootKey;
    private int _errorCode = NO_ERROR;
    private RegistryKeyValues _keyValues;

    private boolean _listening = false;
    private List _registryEventListeners = new LinkedList();

    private RegistryKey()
    {
    }

    private RegistryKey(long value)
    {
        super(value);
    }

    private RegistryKey(long key, String name)
    {
        super(key);
        _name = name;
        _rootKey = new RegistryKey(this.getValue());
    }

    /**
     * Retrieves the name of the key.
     *
     * @return The absolute (qualified) name of the key.
     */
    public String getName()
    {
        return _name;
    }

    /**
     * Returns the path of the key.
     *
     * @return the path of the key.
     */
    public String getPath()
    {
        return _path;
    }

    /**
     * Returns abolute path of the key. Absolute path is a concatenation of the
     * path and name.
     *
     * @return abolute path of the key. Absolute path is a concatenation of the
     *         path and name.
     */
    public String getAbsolutePath()
    {
        String path = getPath();
        StringBuffer result = new StringBuffer(path);
        if (path.length() > 0 && !path.endsWith("\\"))
        {
            result.append('\\');
        }
        result.append(getName());
        return result.toString();
    }

    protected void checkError()
    {
        if (_errorCode != NO_ERROR)
            throw new RegistryException(_errorCode);
    }

    protected void checkError(long errorCode)
    {
        _errorCode = (int)errorCode;
        checkError();
    }

    /**
     * Retrieves the count of subkeys at the base level for the current key.
     *
     * @return count of subkeys for the current key.
     */
    public int getSubKeyCount()
    {
        UInt32 subKeyCount = new UInt32();
        checkError(WinRegistry.queryInfoKey(this, null, null, subKeyCount, null, null, null, null, null));
        final int result = (int)subKeyCount.getValue();
        return result;
    }

    /**
     * Closes the key and flushes it to disk if the contents have been modified.
     */
    public void close()
    {
        checkError(WinRegistry.closeKey(this));
    }

    /**
     * Creates a new subkey or opens an existing one, with write access as
     * specified.
     *
     * @param subKey   the name of the subkey to create.
     * @param writable set to true if you need write access to the key.
     * @return the subkey.
     */
    public RegistryKey createSubKey(String subKey, boolean writable)
    {
        final RegistryKey result = new RegistryKey();
        checkError(WinRegistry.createKey(this,
                subKey,
                REG_OPTION_NON_VOLATILE,
                writable ? (KEY_WRITE | KEY_READ) : KEY_READ,
                result));
        result._path = getAbsolutePath();
        result._name = subKey;
        result._rootKey = getRootKey();
        return result;
    }

    /**
     * Creates a new subkey or opens an existing one.
     *
     * @param subKey a sub-key path string.
     * @return the subkey instance.
     */
    public RegistryKey createSubKey(String subKey)
    {
        return createSubKey(subKey, false);
    }

    /**
     * Deletes the specified subkey is it doesn't include any subkeys.
     * If it is necessary to delete whole subtree call {@link #deleteSubTree}.
     *
     * @param subKey the name of the subkey to delete.
     */
    public void deleteSubKey(String subKey)
    {
        checkError(WinRegistry.deleteKey(this, subKey));
    }

    /**
     * Deletes the specified subkey and all its subkeys.
     *
     * @param subKey the name of the subkey to delete.
     */
    public void deleteSubTree(String subKey)
    {
        RegistryKey key = openSubKey(subKey, true);
        int index = 0;
        Str name = new Str(MAX_PATH);

        List list = new LinkedList();
        while (WinRegistry.enumKeyEx(key, index++, name, new UInt32(MAX_PATH), null, null) == NO_ERROR)
        {
            list.add(name.getValue());
        }
        for (int i = 0; i < list.size(); i++)
        {
            key.deleteSubTree((String)list.get(i));
        }
        key.close();
        deleteSubKey(subKey);
    }

    /**
     * Renames opened registry key.
     *
     * @param newName
     */
    public void rename(String newName)
    {
        String absolutePath = getAbsolutePath();
        String parent = "";
        try
        {
            parent = absolutePath.substring(absolutePath.indexOf("\\") + 1, absolutePath.lastIndexOf("\\"));
        }
        catch (Exception e)
        {
        }

        RegistryKey parentKey = getRootKey();
        if (parent.length() != 0)
        {
            parentKey = parentKey.openSubKey(parent);
        }

        RegistryKey target = copy(parentKey, newName, false);
        String name;
        if (getName().lastIndexOf("\\") != -1)
        {
            name = getName().substring(getName().lastIndexOf("\\") + 1);
        }
        else
        {
            name = getName();
        }
        parentKey.deleteSubTree(name);
        parentKey.close();
        setValue(target.getValue());
        _path = target._path;
        _name = target._name;
        _rootKey = target.getRootKey();
    }

    /**
     * Returns the Root of this registry key.
     *
     * @return the Root of this registry key
     */
    public RegistryKey getRootKey()
    {
        return _rootKey;
    }

    /**
     * Determines whether two Object instances are equal.
     *
     * @param obj The Object to compare with the current Object.
     * @return true if the specified Object is equal to the current Object;
     *         otherwise, false.
     */
    public boolean equals(Object obj)
    {
        if (obj == null || !(obj instanceof RegistryKey))
        {
            return false;
        }
        else
        {
            RegistryKey registryKey = (RegistryKey)obj;
            return getValue() == registryKey.getValue();
        }
    }

    public int hashCode()
    {
        return (int) getValue();
    }

    /**
     * Writes all the attributes of the specified open RegistryKey into the
     * registry.
     */
    public void flush()
    {
        checkError(WinRegistry.flushKey(this));
    }

    /**
     * Retrieves List of strings that contains all the subkey names.
     *
     * @return a list containing the names of the subkeys for the current key.
     */
    public List getSubKeyNames()
    {
        final int subKeyCount = getSubKeyCount();
        List result = new ArrayList(subKeyCount);
        if (subKeyCount > 0)
        {
            final Handle handle = this;
            for (int i = 0, errorCode = NO_ERROR; errorCode == NO_ERROR; i++)
            {
                Str valueName = new Str("", MAX_PATH);
                errorCode = (int)WinRegistry.enumKeyEx(handle, i, valueName, new UInt32(MAX_PATH), null, null);
                if (errorCode == NO_ERROR)
                {
                    result.add(valueName.getValue());
                }
            }
        }
        return result;
    }

    /**
     * @return a string representing the key.
     */
    public String toString()
    {
        return getName();
    }

    /**
     * Opens a subkey by the specified name (as read-only).
     *
     * @param name the name of the subkey to open.
     * @return subkey requested.
     * @throws RegistryException if a sub key cannot be opened.
     */
    public RegistryKey openSubKey(String name)
    {
        return openSubKey(name, false, false);
    }

    /**
     * Opens a subkey by the specified name.
     *
     * @param name     the name of the subkey to open.
     * @param writable set to true if you need write access to the key.
     * @return subkey requested.
     * @throws RegistryException if a sub key cannot be opened.
     */
    public RegistryKey openSubKey(String name, boolean writable)
    {
        return openSubKey(name, writable, false);
    }

    /**
     * Opens a subkey by the specified name.
     *
     * @param name     the name of the subkey to open.
     * @param writable set to true if you need write access to the key.
     * @param quietly  if true, instructs the function not to throw
     *                 RegistryException if there is an error during opening the key; if false,
     *                 the exception is thrown.
     * @return subkey requested.
     * @throws RegistryException if a sub key cannot be opened and <code>parameter</code> is false.
     */
    public RegistryKey openSubKey(String name, boolean writable, boolean quietly)
    {
        RegistryKey result = new RegistryKey();
        result._errorCode = (int)WinRegistry.openKey(this,
                name.startsWith("\\\\") ? name.replaceFirst("\\\\+", "") : name,
                writable ? (KEY_READ | KEY_WRITE) : KEY_READ,
                result);
        result._path = getAbsolutePath();
        result._name = name;
        result._rootKey = getRootKey();

        if (!quietly)
        {
            result.checkError();
        }

        return result;
    }

    /**
     * Checks if the specified subkey exists and can be opened.
     *
     * @param name the name of the subkey to check.
     * @return true if subkey exists; false if otherwise.
     */
    public boolean exists(String name)
    {
        _errorCode = (int)WinRegistry.openKey(this,
                name,
                KEY_READ,
                new Handle());
        return _errorCode != ERROR_FILE_NOT_FOUND;
    }

    /**
     * Copies opened key to another base with new name.
     *
     * @param newBase is a target registry base or <code>null</code> to leave key in the current base.
     * @param newName is a target key name.
     * @param quietly if true, instructs the function not to throw
     *                RegistryException if there the specified key already exists; if false,
     *                the exception is thrown.
     * @return handle to the copy of registry key.
     */
    public RegistryKey copy(RegistryKey newBase, String newName, boolean quietly)
    {
        if (newBase == null)
        {
            throw new IllegalArgumentException("newBase parameter cannot be null.");
        }
        if (newName == null)
        {
            throw new IllegalArgumentException("newName parameter cannot be null.");
        }
        RegistryKey target = new RegistryKey();
        UInt32 disposition = new UInt32();
        Handle hTarget = newBase;

        checkError(WinRegistry.createKey(hTarget,
                newName,
                REG_OPTION_NON_VOLATILE,
                KEY_WRITE | KEY_READ,
                target, disposition));
        target._path = _path;
        target._name = newName;
        target._rootKey = getRootKey();

        if (disposition.getValue() == REG_OPENED_EXISTING_KEY && !quietly)
        {
            throw new RegistryException("The specified registry key already exists.");
        }

        int index = 0;
        Str name = new Str(MAX_PATH);

        while (WinRegistry.enumKeyEx(this, index++, name, new UInt32(MAX_PATH), null, null) == NO_ERROR)
        {
            openSubKey(name.getValue()).copy(newBase, target.getName() + "\\" + name.getValue(), true);
        }

        List srcValues = values().getEntries();
        RegistryKeyValues tagValues = target.values();

        for (int i = 0; i < srcValues.size(); i++)
        {
            RegistryKeyValues.RegistryValueEntry entry = (RegistryKeyValues.RegistryValueEntry)srcValues.get(i);
            tagValues.put(entry.getKey(), entry.getValue(), entry.getType());
        }

        return target;
    }

    /**
     * Returns a list of RegistryKey that are subkeys of the current
     * RegistryKey.
     *
     * @return a list of RegistryKey that are subkeys of the current
     *         RegistryKey.
     */
    public List getSubkeys()
    {
        List subkeyNames = getSubKeyNames();
        List result = new LinkedList();
        for (Iterator i = subkeyNames.iterator(); i.hasNext();)
        {
            String name = (String)i.next();
            try
            {
                RegistryKey subkey = openSubKey(name);
                result.add(subkey);
            }
            catch (RegistryException e)
            {
                LOG.error("Failed to read the registry key " + getAbsolutePath() + '\\' + name, e);
                RegistryKey subkey = openSubKey(name, false, true);
                result.add(subkey);
            }
        }
        return result;
    }

    /**
     * Returns RegistryKeyValues.
     *
     * @return values map.
     */
    public RegistryKeyValues values()
    {
        checkHandle();

        if (_keyValues == null)
        {
            _keyValues = new RegistryKeyValues(this);
        }
        return _keyValues;
    }

    /**
     * Returns handle of the RegistryKey.
     *
     * @return handle of the RegistryKey.
     * @deprecated use <code>this</code> instead of this function
     */
    public Handle getHandle()
    {
        return this;
    }

    private void checkHandle()
    {
        if (isNull())
        {
            checkError(WinRegistry.openKey(new Handle(getValue()), "", KEY_READ, this));
        }
    }

    /**
     * Adds RegistryKey event listener.
     *
     * @param listener
     */
    public void addRegistryEventListener(RegistryEventListener listener)
    {
        if (!_registryEventListeners.contains(listener))
            _registryEventListeners.add(listener);
    }

    /**
     * Removes RegistryKey event listener.
     *
     * @param listener
     */
    public void removeRegistryEventListener(RegistryEventListener listener)
    {
        _registryEventListeners.remove(listener);
    }

    /**
     * Notifies listeners about RegistryKey event.
     *
     * @param event
     */
    protected void fireRegistryEvent(EventObject event)
    {
        List listeners;
        synchronized (this)
        {
            listeners = new LinkedList(_registryEventListeners);
        }
        for (Iterator i = listeners.iterator(); i.hasNext();)
        {
            RegistryEventListener listener = (RegistryEventListener)i.next();
            listener.handle(event);
        }
    }

    /**
     * Starts listening for changes in the RegistryKey.
     *
     * @param watchSubtree
     * @param filter
     */
    public void startChangeListening(final boolean watchSubtree, final int filter)
    {
        Thread listener = new Thread(new Runnable()
        {
            public void run()
            {
                _listening = true;
                while (_listening)
                {
                    checkError(WinRegistry.notifyChangeValue(RegistryKey.this, watchSubtree, filter, new Handle(), false));
                    if (_listening)
                    {
                        fireRegistryEvent(new EventObject(this));
                    }
                }
            }
        });
        listener.start();
    }

    /**
     * Starts listening for changes in the RegistryKey. It watches subtree and
     * listens for all the changes.
     */
    public void startChangeListening()
    {
        int filter = REG_NOTIFY_CHANGE_NAME | REG_NOTIFY_CHANGE_ATTRIBUTES | REG_NOTIFY_CHANGE_LAST_SET | REG_NOTIFY_CHANGE_SECURITY;
        startChangeListening(true, filter);
    }

    /**
     * Stops listening for events in the RegistryKey.
     */
    public void stopChangeListening()
    {
        _listening = false;
        close();
    }
}

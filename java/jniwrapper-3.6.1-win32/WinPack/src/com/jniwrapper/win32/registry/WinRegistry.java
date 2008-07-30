/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.registry;

import com.jniwrapper.*;
import com.jniwrapper.win32.FunctionName;
import com.jniwrapper.win32.Handle;
import com.jniwrapper.win32.system.AdvApi32;

/**
 * This class provides functions from Advapi32 library for working with the
 * registry.
 *
 * @author Serge Piletsky
 */
class WinRegistry extends AdvApi32
{
    private static final String FUNCTION_CLOSE_KEY = "RegCloseKey";
    private static final FunctionName FUNCTION_CREATE_KEY_EX = new FunctionName("RegCreateKeyEx");
    private static final FunctionName FUNCTION_DELETE_KEY = new FunctionName("RegDeleteKey");
    private static final FunctionName FUNCTION_DELETE_VALUE = new FunctionName("RegDeleteValue");
    private static final FunctionName FUNCTION_ENUM_KEY_EX = new FunctionName("RegEnumKeyEx");
    private static final FunctionName FUNCTION_ENUM_VALUE = new FunctionName("RegEnumValue");
    private static final String FUNCTION_FLUSH_KEY = "RegFlushKey";
    private static final FunctionName FUNCTION_LOAD_KEY = new FunctionName("RegLoadKey");
    private static final String FUNCTION_NOTIFY_CHANGE_KEY_VALUE = "RegNotifyChangeKeyValue";
    private static final FunctionName FUNCTION_OPEN_KEY_EX = new FunctionName("RegOpenKeyEx");
    private static final String FUNCTION_OVERRIDE_PREDEF_KEY = "RegOverridePredefKey";
    private static final FunctionName FUNCTION_QUERY_INFO_KEY = new FunctionName("RegQueryInfoKey");
    private static final FunctionName FUNCTION_QUERY_VALUE_EX = new FunctionName("RegQueryValueEx");
    private static final FunctionName FUNCTION_REPLACE_KEY = new FunctionName("RegReplaceKey");
    private static final FunctionName FUNCTION_RESTORE_KEY = new FunctionName("RegRestoreKey");
    private static final FunctionName FUNCTION_SAVE_KEY = new FunctionName("RegSaveKey");
    private static final FunctionName FUNCTION_SET_VALUE_EX = new FunctionName("RegSetValueEx");
    private static final FunctionName FUNCTION_UNLOAD_KEY = new FunctionName("RegUnLoadKey");

    /**
     * Releases a handle to the specified registry key.
     *
     * @param key the key to release.
     * @return function execution result.
     */
    public static long closeKey(Handle key)
    {
        final Function function = get(FUNCTION_CLOSE_KEY);
        Int result = new Int();
        function.invoke(result, key);
        return result.getValue();
    }

    public static long createKey(int key, String subKey, long options, long accessType, Handle resultKey)
    {
        return createKey(new Handle(key), subKey, options, accessType, resultKey);
    }

    public static long createKey(Handle key, String subKey, long options, long accessType, Handle resultKey)
    {
        return createKey(key, subKey, options, accessType, resultKey, null);
    }

    /**
     * Creates the specified registry key.
     *
     * @param key
     * @param subKey
     * @param options
     * @param accessType
     * @param resultKey
     * @param disposition
     * @return errorCode
     */
    public static long createKey(Handle key, String subKey, long options, long accessType, Handle resultKey, UInt32 disposition)
    {
        final Function function = get(FUNCTION_CREATE_KEY_EX);
        Int result = new Int();
        function.invoke(result, new Parameter[]
        {
            key,
            new Pointer.Const(new Str(subKey)),
            new UInt32(0),
            new Pointer(null, true), /* new Pointer.Const(new CString("SomeClass")),*/ // class string. This parameter is ignored if the key already exists.
            new UInt32(options),
            new UInt32(accessType),
            new Pointer(null, true), /*new Pointer(new SecurityAttributes(UInt32.ZERO, new Pointer(null, true), Bool.TRUE)), */ // inheritance
            new Pointer(resultKey),
            new Pointer(disposition, disposition == null),
        });
        return result.getValue();
    }

    /**
     * Deletes a subkey.
     *
     * @param key
     * @param subKey
     * @return errorCode
     */
    public static long deleteKey(Handle key, String subKey)
    {
        final Function function = get(FUNCTION_DELETE_KEY);
        Int result = new Int();
        function.invoke(result, key, new Str(subKey));
        return result.getValue();
    }

    /**
     * Removes a named value from the specified registry key.
     *
     * @param key
     * @param valueName
     * @return errorCode
     */
    public static long deleteValue(Handle key, String valueName)
    {
        final Function function = get(FUNCTION_DELETE_VALUE);
        Int result = new Int();
        function.invoke(result, key, new Str(valueName));
        return result.getValue();
    }

    /**
     * Enumerates subkeys of the specified open registry key.
     * Signature changed. The Str class is used instead ZeroTerminatedString.
     *
     * @param key
     * @param index
     * @param name
     * @param cName
     * @param className
     * @param cClassName
     * @return errorCode
     */
    public static long enumKeyEx(Handle key,
                                 int index,
                                 Str name,
                                 UInt32 cName,
                                 Str className,
                                 UInt32 cClassName)
    {
        final Function function = get(FUNCTION_ENUM_KEY_EX);
        Int result = new Int();
        function.invoke(result, new Parameter[]
        {
            key,
            new UInt32(index),
            new Pointer.OutOnly(name),
            new Pointer(cName),
            new Pointer(null, true), // Reserved; must be NULL
            new Pointer(className, className == null),
            new Pointer(cClassName, cClassName == null),
            new Handle()
        });
        return result.getValue();
    }

    /**
     * Enumerates the values for the specified open registry key.
     *
     * @param key
     * @param index
     * @param valueName
     * @param cValueName
     * @param type
     * @param data
     * @param cData
     * @return erroeCode
     */
    public static long enumValue(Handle key,
                                 int index,
                                 Str valueName,
                                 UInt32 cValueName,
                                 UInt32 type,
                                 Pointer data,
                                 UInt32 cData)
    {
        final Function function = get(FUNCTION_ENUM_VALUE);
        Int result = new Int();
        function.invoke(result, new Parameter[]
        {
            key,
            new UInt32(index),
            new Pointer.OutOnly(valueName),
            new Pointer(cValueName),
            new Pointer(null, true), // Reserved; must be NULL
            new Pointer(type, type == null),
            data == null ? (Parameter)new Pointer.Void() : data,
            new Pointer(cData, cData == null)
        });
        return result.getValue();
    }

    /**
     * Retrieves the type and data for a specified value name associated with an open registry key.
     *
     * @param key
     * @param name
     * @param type
     * @param value
     * @return errorCode
     */
    public static long getValue(Handle key, String name, int type, Parameter value)
    {
        final Function function = get(FUNCTION_QUERY_VALUE_EX);
        Int result = new Int();
        function.invoke(result, new Parameter[]
        {
            key,
            new Pointer(new Str(name), name == null),
            new Pointer(null, true), //Reserved; must be NULL
            new Pointer(new Int32(type)),
            new Pointer(value),
            new Pointer(new Int32(value.getLength())),
        });
        return result.getValue();
    }

    /**
     * Sets the data and type of a specified value under a registry key.
     *
     * @param key
     * @param name
     * @param type
     * @param value
     * @param valueSize
     * @return errorCode
     */
    public static long setValue(Handle key, String name, int type, Parameter value, int valueSize)
    {
        final Function function = get(FUNCTION_SET_VALUE_EX);
        Int result = new Int();
        function.invoke(result, new Parameter[]
        {
            key,
            new Pointer(new Str(name), name == null),
            new UInt32(0),
            new UInt32(type),
            new Pointer.Const(value),
            new Int32(valueSize),
        });
        return result.getValue();
    }

    /**
     * Writes all the attributes of the specified open registry key into the registry.
     *
     * @param key
     * @return errorCode
     */
    public static long flushKey(Handle key)
    {
        final Function function = get(FUNCTION_FLUSH_KEY);
        Int result = new Int();
        function.invoke(result, key);
        return result.getValue();
    }

    /**
     * Creates a subkey under HKEY_USERS or HKEY_LOCAL_MACHINE and stores registration information from a specified file into that subkey.
     *
     * @param key
     * @param subKey
     * @param file
     * @return errorCode
     */
    public static long loadKey(Handle key, String subKey, String file)
    {
        final Function function = get(FUNCTION_LOAD_KEY);
        Int result = new Int();
        function.invoke(result,
                key,
                new Str(subKey),
                new Str(file));
        return result.getValue();
    }

    /**
     * Saves the specified key and all of its subkeys and values to a new file.
     *
     * @param key
     * @param file
     * @return errorCode
     */
    public static long saveKey(Handle key, String file)
    {
        final Function function = get(FUNCTION_SAVE_KEY);
        Int result = new Int();
        function.invoke(result,
                key,
                new Str(file),
                new Pointer(null, true));
        return result.getValue();
    }

    /**
     * Unloads the specified registry key and its subkeys from the registry.
     *
     * @param key
     * @param subKey
     * @return errorCode
     */
    public static long unloadKey(Handle key, String subKey)
    {
        final Function function = get(FUNCTION_UNLOAD_KEY);
        Int result = new Int();
        function.invoke(result,
                key,
                new Str(subKey));
        return result.getValue();
    }

    /**
     * Notifies the caller about changes to the attributes or contents of a specified registry key.
     *
     * @param key
     * @param watchSubtree
     * @param notifyFilter
     * @param event
     * @param asynchronous
     * @return errorCode
     */
    public static long notifyChangeValue(Handle key, boolean watchSubtree, int notifyFilter, Handle event, boolean asynchronous)
    {
        final Function function = get(FUNCTION_NOTIFY_CHANGE_KEY_VALUE);
        Int result = new Int();
        function.invoke(result, new Parameter[]
        {
            key, new Bool(watchSubtree), new UInt32(notifyFilter), event, new Bool(asynchronous)
        });
        return result.getValue();
    }

    /**
     * Opens the specified registry key.
     *
     * @param key
     * @param subKey
     * @param accessMask
     * @param resultKey
     * @return errorCode
     */
    public static long openKey(Handle key, String subKey, int accessMask, Handle resultKey)
    {
        final Function function = get(FUNCTION_OPEN_KEY_EX);
        Int result = new Int();
        function.invoke(result, new Parameter[]
        {
            key,
            new Pointer.Const(new Str(subKey), subKey == null),
            new UInt32(0),
            new UInt32(accessMask),
            new Pointer.OutOnly(resultKey),
        });
        return result.getValue();
    }

    /**
     * Maps a predefined registry key to a specified registry key.
     *
     * @param key
     * @param newKey
     * @return errorCode
     */
    public static long overridePredefinedKey(Handle key, Handle newKey)
    {
        final Function function = get(FUNCTION_OVERRIDE_PREDEF_KEY);
        Int result = new Int();
        function.invoke(result, key, newKey);
        return result.getValue();
    }

    /**
     * Retrieves information about the specified registry key.
     * Signature changed. The Str class is used instead ZeroTerminatedString.
     *
     * @param key
     * @param className
     * @param cClassName
     * @param subKeys
     * @param cMaxSubKeyLen
     * @param cMaxClassLen
     * @param cValues
     * @param cMaxValueNameLen
     * @param cMaxValueLen
     * @return errorCode
     */
    public static long queryInfoKey(Handle key, //in
                                    Str className, //out
                                    UInt32 cClassName, //in/out
                                    UInt32 subKeys, //out
                                    UInt32 cMaxSubKeyLen, //out
                                    UInt32 cMaxClassLen, //out
                                    UInt32 cValues, //out
                                    UInt32 cMaxValueNameLen, //out
                                    UInt32 cMaxValueLen //out
                                    )
    {
        final Function function = get(FUNCTION_QUERY_INFO_KEY);
        Int32 result = new Int32();
        function.invoke(result, new Parameter[]
        {
            key,
            new Pointer(className, className == null),
            new Pointer(cClassName, cClassName == null),
            new Pointer.Void(),
            new Pointer(subKeys, subKeys == null),
            new Pointer(cMaxSubKeyLen, cMaxSubKeyLen == null),
            new Pointer(cMaxClassLen, cMaxClassLen == null),
            new Pointer(cValues, cValues == null),
            new Pointer(cMaxValueNameLen, cMaxValueNameLen == null),
            new Pointer(cMaxValueLen, cMaxValueLen == null),
            new Pointer.Void(),
            new Pointer.Void()
        });
        return result.getValue();
    }

    /**
     * Replaces the file backing a registry key and all its subkeys with another file.
     *
     * @param key
     * @param subKey
     * @param newFile
     * @param oldFile
     * @return errorCode
     */
    public static long replaceKey(Handle key, //in
                                  String subKey, //in
                                  String newFile, //in
                                  String oldFile //in
                                  )
    {
        final Function function = get(FUNCTION_REPLACE_KEY);
        Int result = new Int();
        function.invoke(result,
                key,
                new Str(subKey),
                new Str(newFile),
                new Str(oldFile));
        return result.getValue();
    }

    /**
     * Reads the registry information in a specified file and copies it over the specified key.
     *
     * @param key
     * @param file
     * @param flags
     * @return errorCode
     */
    public static long restoreKey(Handle key, //in
                                  String file, //in
                                  int flags //in
                                  )
    {
        final Function function = get(FUNCTION_RESTORE_KEY);
        Int result = new Int();
        function.invoke(result,
                key,
                new Str(file),
                new UInt32(flags));
        return result.getValue();
    }
}

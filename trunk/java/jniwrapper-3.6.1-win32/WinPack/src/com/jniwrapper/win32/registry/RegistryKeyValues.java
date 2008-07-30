/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.registry;

import com.jniwrapper.Parameter;
import com.jniwrapper.Str;
import com.jniwrapper.UInt32;
import com.jniwrapper.util.Enums;
import com.jniwrapper.util.Logger;

import java.util.*;

/**
 * This class provides access to values of a registry key.
 * A value can be obtained via the {@link #get(Object key)}. To get a default value for a key,
 * pass an empty string to {@link #get(Object key)} method.
 * For example, the following code obtains the command for the default browser:<br>
 * <pre>
 * RegistryKey rkey = RegistryKey.CLASSES_ROOT.openSubKey("http\\shell\\open\\command");
 * String browserCommand = rkey.values().get("").toString();
 * </pre>
 *
 * @author Serge Piletsky
 */
public class RegistryKeyValues implements Map
{
    private static final Logger LOG = Logger.getInstance(RegistryKeyValues.class);

    private RegistryKey _registryKey;
    private List _entries = new ArrayList();

    private static final Map TYPE_ASSOCIATIONS = new HashMap();

    static
    {
        registerAssociation(RegistryKeyType.SZ, RegistryValueTransformer.STRING_TRANSFORMER);
        registerAssociation(RegistryKeyType.EXPAND_SZ, RegistryValueTransformer.STRING_TRANSFORMER);

        registerAssociation(RegistryKeyType.DWORD, RegistryValueTransformer.INTEGER_TRANSFORMER);

        registerAssociation(RegistryKeyType.BINARY, RegistryValueTransformer.BINARY_TRANSFORMER);
        registerAssociation(RegistryKeyType.RESOURCE_LIST, RegistryValueTransformer.BINARY_TRANSFORMER);
        registerAssociation(RegistryKeyType.FULL_RESOURCE_DESCRIPTOR, RegistryValueTransformer.BINARY_TRANSFORMER);
        registerAssociation(RegistryKeyType.RESOURCE_REQUIREMENTS_LIST, RegistryValueTransformer.BINARY_TRANSFORMER);

        registerAssociation(RegistryKeyType.MULTI_SZ, RegistryValueTransformer.MULTISTRING_TRANSFORMER);
    }

    /**
     * Associates a value transformer with a specified registry type.
     *
     * @param type        is a registry value type.
     * @param transformer is a custom transformer.
     */
    public static void registerAssociation(RegistryKeyType type, RegistryValueTransformer transformer)
    {
        TYPE_ASSOCIATIONS.put(type, transformer);
    }

    public RegistryKeyValues(RegistryKey registryKey)
    {
        _registryKey = registryKey;
        load();
    }

    private void load()
    {
        _entries.clear();
        for (int i = 0, errorCode = RegistryKey.NO_ERROR; errorCode == RegistryKey.NO_ERROR; i++)
        {
            Str valueName = new Str("", RegistryKey.MAX_PATH);
            UInt32 type = new UInt32();
            UInt32 size = new UInt32();
            errorCode = (int)WinRegistry.enumValue(_registryKey, i, valueName, new UInt32(RegistryKey.MAX_PATH), type, null, size);
            if (errorCode == RegistryKey.NO_ERROR)
            {
                final RegistryKeyType registryKeyType = (RegistryKeyType)Enums.getItem(RegistryKeyType.class, (int)type.getValue());
                final RegistryValueEntry registryValueEntry =
                        new RegistryValueEntry(valueName.getValue(), registryKeyType, (int)size.getValue());
                _entries.add(registryValueEntry);
            }
        }
    }

    private RegistryValueEntry find(Object key)
    {
        if (!_entries.isEmpty())
        {
            for (Iterator i = _entries.iterator(); i.hasNext();)
            {
                RegistryValueEntry entry = (RegistryValueEntry)i.next();
                if (entry.getKey().equals(key))
                {
                    return entry;
                }
            }
        }
        return null;
    }

    public int size()
    {
        UInt32 valueCount = new UInt32();
        try
        {
            _registryKey.checkError(WinRegistry.queryInfoKey(_registryKey, (Str)null, null, null, null, null, valueCount, null, null));
        }
        catch (Exception e)
        {
            return 0;
        }
        if (valueCount.getValue() != _entries.size())
        {
            load();
        }
        return _entries.size();
    }

    public boolean isEmpty()
    {
        return size() == 0;
    }

    public boolean containsKey(Object key)
    {
        return find(key) != null;
    }

    public boolean containsValue(Object value)
    {
        return values().contains(value);
    }

    public Object get(Object key)
    {
        RegistryValueEntry entry = find(key);
        if (entry != null)
        {
            return entry.getValue();
        }
        return null;
    }

    public Object put(Object key, Object value)
    {
        RegistryValueEntry entry = find(key);
        if (entry == null)
        {
            entry = new RegistryValueEntry(key.toString());
            entry.setType(entry.getValueType(value));
            entry.setValue(value);
            _entries.add(entry);
            return null;
        }
        else
        {
            Object prevValue = entry.getValue();
            entry.setValue(value);
            return prevValue;
        }
    }

    public Object put(Object key, Object value, RegistryKeyType valueType)
    {
        RegistryValueEntry entry = find(key);
        if (entry == null)
        {
            entry = new RegistryValueEntry(key.toString());
            entry.setType(valueType);
            entry.setValue(value);
            _entries.add(entry);
            return null;
        }
        else
        {
            Object prevValue = entry.getValue();
            entry.setValue(value);
            return prevValue;
        }
    }

    public Object remove(Object key)
    {
        Entry entry = find(key);
        if (entry != null)
        {
            Object prevValue = entry.getValue();
            _registryKey.checkError(WinRegistry.deleteValue(_registryKey, key.toString()));
            _entries.remove(entry);
            return prevValue;
        }
        return null;
    }

    public void putAll(Map t)
    {
        for (Iterator i = t.entrySet().iterator(); i.hasNext();)
        {
            Entry entry = (Entry)i.next();
            put(entry.getKey(), entry.getValue());
        }
    }

    public void clear()
    {
        size();
        for (Iterator i = _entries.iterator(); i.hasNext();)
        {
            Entry entry = (Entry)i.next();
            _registryKey.checkError(WinRegistry.deleteValue(_registryKey, entry.getKey().toString()));
        }
        _entries.clear();
    }

    public Set keySet()
    {
        final int size = size();
        Set result = new HashSet(size);
        for (Iterator i = _entries.iterator(); i.hasNext();)
        {
            Entry entry = (Entry)i.next();
            result.add(entry.getKey());
        }
        return result;
    }

    public Collection values()
    {
        final int size = size();
        List result = new ArrayList(size);
        for (Iterator i = _entries.iterator(); i.hasNext();)
        {
            Entry entry = (Entry)i.next();
            final Object value = entry.getValue();
            result.add(value);
        }
        return result;
    }

    public Set entrySet()
    {
        return new HashSet(_entries);
    }

    public List getEntries()
    {
        return _entries;
    }

    // TODO [Sanders]: Why cannot have this class private?
    public class RegistryValueEntry implements Map.Entry
    {
        private String _name;
        private RegistryKeyType _type = RegistryKeyType.NONE;
        private int _size;
        private Object _value;

        public RegistryValueEntry(String name, RegistryKeyType type, int size)
        {
            _name = name;
            _type = type;
            _size = size;
        }

        public RegistryValueEntry(String name)
        {
            _name = name;
        }

        public Object getKey()
        {
            return _name;
        }

        private RegistryValueTransformer getValueTransformer(RegistryKeyType type)
        {
            for (Iterator i = TYPE_ASSOCIATIONS.entrySet().iterator(); i.hasNext();)
            {
                Map.Entry entry = (Map.Entry)i.next();
                RegistryKeyType keyType = (RegistryKeyType)entry.getKey();
                RegistryValueTransformer valueTranstormer = (RegistryValueTransformer)entry.getValue();
                if (keyType.equals(type))
                    return valueTranstormer;
            }
            throw new IllegalArgumentException("Unable to get value transformer for given value type: " + type);
        }

        public Object getValue()
        {
            if (_value == null)
            {
                if (!_registryKey.isNull())
                {
                    final RegistryKeyType type = getType();
                    RegistryValueTransformer transformer = getValueTransformer(type);
                    _value = transformer.createRegistryValueParameter(_size);
                    _registryKey.checkError(WinRegistry.getValue(_registryKey, _name, type.getValue(), (Parameter)_value));
                    _value = transformer.fromRegistryValue((Parameter)_value);
                }
            }
            return _value;
        }

        public Object setValue(Object value)
        {
            final RegistryKeyType type = getType();
            RegistryValueTransformer transformer = getValueTransformer(type);
            Parameter result = transformer.toRegistryValue(value);
            _registryKey.checkError(WinRegistry.setValue(_registryKey, _name, type.getValue(), result, result.getLength()));
            _value = value;
            return result;
        }

        private RegistryKeyType getValueType(Object value)
        {
            RegistryKeyType result = RegistryKeyType.NONE;
            for (Iterator i = TYPE_ASSOCIATIONS.entrySet().iterator(); i.hasNext();)
            {
                Map.Entry entry = (Map.Entry)i.next();
                RegistryKeyType type = (RegistryKeyType)entry.getKey();
                RegistryValueTransformer transformer = (RegistryValueTransformer)entry.getValue();
                if (transformer.isTypeSupported(value))
                {
                    result = type;
                    break;
                }
            }
            return result;
        }

        public RegistryKeyType getType()
        {
            return _type;
        }

        public void setType(RegistryKeyType type)
        {
            _type = type;
        }

        public int getSize()
        {
            return _size;
        }

        public String toString()
        {
            StringBuffer result = new StringBuffer("RegistryValueEntry [Name=");
            result.append(_name).append(";type=").append(_type).append(']');
            return result.toString();
        }
    }
}

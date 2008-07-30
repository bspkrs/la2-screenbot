/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.registry.ui;

import com.jniwrapper.win32.registry.RegistryKey;
import com.jniwrapper.win32.registry.RegistryKeyType;
import com.jniwrapper.win32.registry.RegistryKeyValues;
import com.jniwrapper.win32.registry.RegistryException;

import javax.swing.table.AbstractTableModel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RegistryKey values table model.
 *
 * @author Serge Piletsky
 */
public class RegistryKeyValuesTableModel extends AbstractTableModel
{
    private RegistryKey _registryKey;
    private static final Map _typeNames = new HashMap();
    static
    {
        _typeNames.put(RegistryKeyType.NONE, "REG_NONE");
        _typeNames.put(RegistryKeyType.SZ, "REG_SZ");
        _typeNames.put(RegistryKeyType.EXPAND_SZ, "REG_EXPAND_SZ");
        _typeNames.put(RegistryKeyType.BINARY, "REG_BINARY");
        _typeNames.put(RegistryKeyType.DWORD, "REG_DWORD");
        _typeNames.put(RegistryKeyType.MULTI_SZ,  "REG_MULTI_SZ");
        _typeNames.put(RegistryKeyType.LINK, "REG_LINK");
        _typeNames.put(RegistryKeyType.RESOURCE_LIST,  "REG_RESOURCE_LIST");
        _typeNames.put(RegistryKeyType.FULL_RESOURCE_DESCRIPTOR, "REG_FULL_RESOURCE_DESCRIPTOR");
        _typeNames.put(RegistryKeyType.RESOURCE_REQUIREMENTS_LIST, "REG_RESOURCE_REQUIREMENTS_LIST");
    }

    public RegistryKeyValuesTableModel()
    {
    }

    public RegistryKeyValuesTableModel(RegistryKey registryKey)
    {
        _registryKey = registryKey;
    }

    public int getRowCount()
    {
        if (_registryKey == null)
        {
            return 0;
        }
        else
        {
            try
            {
                RegistryKeyValues registryKeyValues = _registryKey.values();
                return registryKeyValues.size();
            }
            catch (RegistryException e)
            {
                return 0;
            }
        }
    }

    public int getColumnCount()
    {
        return 3;
    }

    public String getColumnName(int column)
    {
        switch (column)
        {
            case 0:
                return "Name";
            case 1:
                return "Type";
            case 2:
                return "Value";
            default:
                throw new IllegalArgumentException("Invalid column index");
        }
    }

    protected RegistryKeyValues.RegistryValueEntry getEntry(int rowIndex)
    {
        List entries = _registryKey.values().getEntries();
        RegistryKeyValues.RegistryValueEntry entry = (RegistryKeyValues.RegistryValueEntry)entries.get(rowIndex);
        return entry;
    }

    private static String toHexString(byte value)
    {
        int lb = value & 0x0F;
        int hb = (value & 0xF0) >> 4;
        int lc = lb > 9?'A' + lb % 10 :'0' + lb;
        int hc = hb > 9?'A' + hb % 10 :'0' + hb;
        StringBuffer result = new StringBuffer();
        result.append((char)hc).append((char)lc);
        return result.toString();
    }

    public Object getValueAt(int rowIndex, int columnIndex)
    {
        if (_registryKey == null)
            return null;

        RegistryKeyValues.RegistryValueEntry entry = getEntry(rowIndex);
        switch (columnIndex)
        {
            case 0:
                String key = entry.getKey().toString();
                return key.length() == 0? "(Default)":key;
            case 1:
                final Object typeName = _typeNames.get(entry.getType());
                return typeName;
            case 2:
                if (entry.getType().equals(RegistryKeyType.BINARY) ||
                    entry.getType().equals(RegistryKeyType.RESOURCE_LIST) ||
                    entry.getType().equals(RegistryKeyType.FULL_RESOURCE_DESCRIPTOR) ||
                    entry.getType().equals(RegistryKeyType.RESOURCE_REQUIREMENTS_LIST))
                {
                    byte[] binaryData = (byte[])entry.getValue();
                    StringBuffer result = new StringBuffer();
                    for (int i = 0; i < binaryData.length; i++)
                    {
                        result.append(toHexString(binaryData[i])).append(' ');
                    }
                    return result.toString();
                }
                return entry.getValue();
            default:
                throw new IllegalArgumentException("Invalid column index");
        }
    }

    public RegistryKey getRegistryKey()
    {
        return _registryKey;
    }

    public void setRegistryKey(RegistryKey registryKey)
    {
        _registryKey = registryKey;
        fireTableDataChanged();
    }
}

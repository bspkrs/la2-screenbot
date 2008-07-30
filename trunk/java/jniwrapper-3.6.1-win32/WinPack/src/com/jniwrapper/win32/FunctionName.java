/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32;

import com.jniwrapper.win32.system.Kernel32;

/**
 * This class stores general Windows API function name and returns the
 * encoding-specific name on demand.
 *
 * @author Serge Piletsky
 */
public class FunctionName
{
    static final char SUFFIX_ANSI = 'A';
    static final char SUFFIX_UNICODE = 'W';

    private String _name;
    private boolean _hasANSISuffix = true;

    private static boolean _useUnicode = Kernel32.getInstance().isUnicode();

    public static void useUnicodeNames(boolean value)
    {
        _useUnicode = value;
    }

    public static boolean usesUnicodeNames()
    {
        return _useUnicode;
    }

    public FunctionName(String name)
    {
        _name = name;
    }

    public FunctionName(String name, boolean hasANSISuffix)
    {
        _name = name;
        _hasANSISuffix = hasANSISuffix;
    }

    public boolean hasANSISuffix()
    {
        return _hasANSISuffix;
    }

    public String getName()
    {
        return _name;
    }

    /**
     * Returns the encoding-specific function name.
     *
     * @param unicode if true, Unicode name will be returned.
     * @return function name with encoding-specific suffix.
     */
    public String getEncodingSpecificName(boolean unicode)
    {
        StringBuffer result = new StringBuffer(getName());
        if (unicode)
        {
            result.append(SUFFIX_UNICODE);
        }
        else if (_hasANSISuffix)
        {
            result.append(SUFFIX_ANSI);
        }
        return result.toString();
    }

    public String toString()
    {
        return getEncodingSpecificName(_useUnicode);
    }
}

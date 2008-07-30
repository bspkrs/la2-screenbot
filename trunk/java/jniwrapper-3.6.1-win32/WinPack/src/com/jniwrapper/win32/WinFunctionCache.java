/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32;

import com.jniwrapper.Library;
import com.jniwrapper.win32.system.Kernel32;
import com.jniwrapper.util.FunctionCache;

/**
 * This class provides functionality for selecting appropriate function instance
 * between ANSI and Unicode analogs available in Win32 API depending on the
 * configuration of the cache.
 * <p>In addition, this class is a factory for string parameters, which are
 * created also according to whether Unicode or ANSI functions should be used.
 *
 * @author Alexander Evsukov
 */
public class WinFunctionCache extends FunctionCache
{
    private boolean _unicode = true;

    public WinFunctionCache(String libraryName)
    {
        super(libraryName);
        setupEncoding();
    }

    public WinFunctionCache(Library library)
    {
        super(library);
        setupEncoding();
    }

    protected void setupEncoding()
    {
        setUnicode(Kernel32.getInstance().isUnicode());
    }

    public boolean isUnicode()
    {
        return _unicode;
    }

    /**
     * Instructs the cache to use Unicode or ANSI functions and parameter types.
     *
     * @param unicode if true, Unicode names and types will be used; otherwise
     *                ANSI.
     */
    public void setUnicode(boolean unicode)
    {
        _unicode = unicode;
    }
}

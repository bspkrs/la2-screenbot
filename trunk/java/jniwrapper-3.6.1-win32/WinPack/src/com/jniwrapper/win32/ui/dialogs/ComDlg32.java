/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.ui.dialogs;

import com.jniwrapper.Function;
import com.jniwrapper.UInt32;
import com.jniwrapper.win32.WinFunctionCache;

/**
 * This class provides functions from comdlg32 library.
 *
 * @author Serge Piletsky
 */
public class ComDlg32 extends WinFunctionCache
{
    public static final int CDERR_GENERALCODES = 0x0000;
    /**
     * The lStructSize member of the initialization structure is invalid.
     */
    public static final int CDERR_STRUCTSIZE = 0x0001;
    /**
     * The dialog is failed during initialization.
     */
    public static final int CDERR_INITIALIZATION = 0x0002;
    /**
     * The ENABLETEMPLATE flag was set but attepmt failed to provide a corresponding template.
     */
    public static final int CDERR_NOTEMPLATE = 0x0003;
    /**
     * The ENABLETEMPLATE flag was set but attepmt failed to provide a corresponding instance handle.
     */
    public static final int CDERR_NOHINSTANCE = 0x0004;
    /**
     * The function function failed to load a specified string.
     */
    public static final int CDERR_LOADSTRFAILURE = 0x0005;
    /**
     * The function failed to find a specified resource.
     */
    public static final int CDERR_FINDRESFAILURE = 0x0006;
    /**
     * The function failed to load a specified resource.
     */
    public static final int CDERR_LOADRESFAILURE = 0x0007;
    /**
     * The function failed to lock a specified resource.
     */
    public static final int CDERR_LOCKRESFAILURE = 0x0008;
    /**
     * The function was unable to allocate memory for internal structures.
     */
    public static final int CDERR_MEMALLOCFAILURE = 0x0009;
    /**
     * The function was unable to lock the memory associated with a handle.
     */
    public static final int CDERR_MEMLOCKFAILURE = 0x000A;
    /**
     * The ENABLEHOOK flag was set but attempt failed to provide a pointer to a
     * corresponding hook procedure.
     */
    public static final int CDERR_NOHOOK = 0x000B;
    /**
     * The RegisterWindowMessage function returned an error code
     * when it was called by the function.
     */
    public static final int CDERR_REGISTERMSGFAIL = 0x000C;

    static final String FUNCTION_COMMON_DLG_EXTENDED_ERROR = "CommDlgExtendedError";

    private static ComDlg32 _instance;

    public static ComDlg32 getInstance()
    {
        if (_instance == null)
        {
            _instance = new ComDlg32();
        }

        return _instance;
    }

    ComDlg32()
    {
        super("comdlg32");
    }

    /**
     * Method returns extnded error code.
     *
     * @return error code
     */
    public static long getCommDlgExtendedError()
    {
        final Function function = getInstance().getFunction(FUNCTION_COMMON_DLG_EXTENDED_ERROR);
        UInt32 result = new UInt32();
        function.invoke(result);
        return result.getValue();
    }
}

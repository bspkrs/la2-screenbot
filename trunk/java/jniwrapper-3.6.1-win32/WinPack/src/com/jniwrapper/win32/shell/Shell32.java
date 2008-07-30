/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.shell;

import com.jniwrapper.win32.WinFunctionCache;
import com.jniwrapper.win32.FunctionName;
import com.jniwrapper.win32.Handle;
import com.jniwrapper.win32.LastErrorException;
import com.jniwrapper.win32.ui.Wnd;
import com.jniwrapper.Function;
import com.jniwrapper.Int;
import com.jniwrapper.Parameter;
import com.jniwrapper.Str;

import java.io.File;

/**
 * This class provides functions from shell32 library.
 *
 * @author Alexander Evsukov
 */
public class Shell32 extends WinFunctionCache
{
    private static Shell32 _instance;

    public static Shell32 getInstance()
    {
        if (_instance == null)
        {
            _instance = new Shell32();
        }
        return _instance;
    }

    Shell32()
    {
        super("shell32");
    }

    private static final FunctionName FUNCTION_ShellExecute = new FunctionName("ShellExecute");

    public static final String VERB_Open = "open";
    public static final String VERB_Edit = "edit";
    public static final String VERB_Find = "find";
    public static final String VERB_Print = "print";

    /**
     * Opens the specified file using the <code>ShellExecute</code> function.
     *
     * @param file file to open
     */
    public static final void open(File file)
    {
        shellExecute(null, VERB_Open, file.getAbsolutePath(), null, null, Wnd.ShowWindowCommand.SHOWNORMAL);
    }

    /**
     * The wrapper for <code>ShellExecute</code> API function.
     */
    public static final void shellExecute(Wnd wnd, String operation, String fileName, String parameters,
                                          String directory, Wnd.ShowWindowCommand showCommand)
    {
        Function shellExecute = getInstance().getFunction(FUNCTION_ShellExecute.toString());
        Int result = new Int();
        Parameter _null = new Handle();
        long errorCode = shellExecute.invoke(result, new Parameter[]{
                wnd != null ? wnd : _null,
                new Str(operation),
                new Str(fileName),
                parameters != null ? new Str(parameters) : _null,
                directory != null ? new Str(directory) : _null,
                new Int(showCommand.getValue())
        });
        if (result.getValue() <= 32)
        {
            throw new LastErrorException(errorCode);
        }
    }
}

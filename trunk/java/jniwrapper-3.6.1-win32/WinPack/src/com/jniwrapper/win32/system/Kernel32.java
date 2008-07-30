/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.system;

import com.jniwrapper.*;
import com.jniwrapper.win32.FunctionName;
import com.jniwrapper.win32.Handle;
import com.jniwrapper.win32.LastErrorException;
import com.jniwrapper.win32.WinFunctionCache;

import java.io.File;

/**
 * This class provides functions from the Kernel32 library.
 *
 * @author Alexander Evsukov
 */
public class Kernel32 extends WinFunctionCache
{

    private static final String FUNCTION_MultiByteToWideChar = "MultiByteToWideChar";
    private static final String FUNCTION_lstrlen = "lstrlen";
    private static final String FUNCTION_MulDiv = "MulDiv";
    private static final String FUNCTION_GetCurrentThreadId = "GetCurrentThreadId";
    private static final String FUNCTION_GetCurrentProcessId = "GetCurrentProcessId";
    private static final String FUNCTION_ProcessIdToSessionId = "ProcessIdToSessionId";
    private static final String FUNCTION_GetProcessId = "GetProcessId";
    private static final FunctionName FUNCTION_GetWindowsDirectory = new FunctionName("GetWindowsDirectory");
    private static final FunctionName FUNCTION_GetSystemDirectory = new FunctionName("GetSystemDirectory");
    private static final FunctionName FUNCTION_GlobalAddAtom = new FunctionName("GlobalAddAtom");
    private static final FunctionName FUNCTION_GET_CURRENT_DIRECTORY = new FunctionName("GetCurrentDirectory");
    private static final FunctionName FUNCTION_SET_CURRENT_DIRECTORY = new FunctionName("SetCurrentDirectory");

    /**
     * Guard to check if detection of the platform version was passed or using
     * Unicode or not was explicitly required.
     */
    private boolean _encodingSpecified;

    private static Kernel32 _instance;

    private Kernel32()
    {
        super("kernel32");
    }

    protected void setupEncoding()
    {
    }

    /**
     * Says if ANSI or Unicode functions should be used.
     * <p>Detects the platform if this wasn't done before, or property wasn't
     * explicitly modified directly via <code>setUnicode()</code>. If the
     * property wasn't set explicitly, assumes that Unicode should be used if
     * the underlying OS is NT-based.
     * <p>NOTE: Since the Unicode support detection is based on the platform version
     * detection, which itself requires <code>Kernel32</code> instance, the
     * detection cannot be performed in the constructor to prevent recursion,
     * and should be delayed until actually queried.
     *
     * @return true if Unicode versions of functions should be used.
     */
    public boolean isUnicode()
    {
        if (!_encodingSpecified)
        {
            final VersionInfo verInfo = new VersionInfo();
            setUnicode(verInfo.isNT());
            _encodingSpecified = true;
        }

        return super.isUnicode();
    }

    public void setUnicode(boolean unicode)
    {
        _encodingSpecified = true;
        super.setUnicode(unicode);
    }

    public static Kernel32 getInstance()
    {
        if (_instance == null)
        {
            _instance = new Kernel32();
        }

        return _instance;
    }

    /**
     * Translates a multibyte string to a Unicode string.
     *
     * @param codePage code page.
     * @param dwFlags  options
     * @param srcStr   address multibyte string.
     * @param srcLen   the length of multibyte string.
     * @param destStr  address for the Unicode string.
     * @param destLen  the number of bytes allocated for the Unicode string.
     * @return if succeeded, returns the number of characters written to buffer,
     *         else returns 0.
     */
    public static int multiByteToWideChar(int codePage,
                                          int dwFlags,
                                          Pointer.Void srcStr,
                                          int srcLen,
                                          Pointer.Void destStr,
                                          int destLen)
    {
        Function function = getInstance().getFunction(FUNCTION_MultiByteToWideChar);
        Int result = new Int();
        function.invoke(result, new Parameter[]{
                new UInt(codePage),
                new UInt32(dwFlags),
                srcStr,
                new Int(srcLen),
                destStr,
                new Int(destLen)});

        return (int) result.getValue();
    }

    /**
     * Returns the length of a zero-terminated string.
     *
     * @param lpStr the address of a zero-terminated string.
     * @return the length of a zero-terminated string.
     */
    public static int lstrlen(Pointer.Void lpStr)
    {
        Function function = getInstance().getFunction(FUNCTION_lstrlen);
        Int result = new Int();
        function.invoke(result, lpStr);

        return (int) result.getValue();
    }

    /**
     * Multiplies two 32-bit values and then divides the 64-bit result by a third 32-bit value.
     * The return value is rounded up or down to the nearest integer.
     *
     * @param number      Multiplicand.
     * @param numerator   Multiplier.
     * @param denominator Number by which the result of the multiplication (nNumber * nNumerator) is to be divided
     * @return If the function succeeds, the return value is the result of the multiplication and division. If either an overflow occurred or nDenominator was 0, the return value is –1.
     */
    public static int mulDiv(int number, int numerator, int denominator)
    {
        final Function function = getInstance().getFunction(FUNCTION_MulDiv);
        Int result = new Int();
        function.invoke(result, new Int(number), new Int(numerator), new Int(denominator));
        return (int) result.getValue();
    }

    /**
     * Returns the identifier of the current thread.
     *
     * @return the identifier of the current thread.
     */
    public static int getCurrentThreadId()
    {
        UInt32 result = new UInt32();
        final Function getCurrentThreadID = getInstance().getFunction(FUNCTION_GetCurrentThreadId);
        getCurrentThreadID.invoke(result);
        return (int) result.getValue();
    }

    /**
     * Returns the identifier of the current process.
     *
     * @return the identifier of the current process.
     */
    public static int getCurrentProcessId()
    {
        UInt32 result = new UInt32();
        final Function getCurrentProcessID = getInstance().getFunction(FUNCTION_GetCurrentProcessId);
        getCurrentProcessID.invoke(result);
        return (int) result.getValue();
    }

    /**
     * Returns the absolute path to the Windows directory.
     *
     * @return the absolute path to the Windows directory.
     */
    public static String getWindowsDirectory()
    {
        Str lpBuffer = new Str();
        UInt32 uSize = new UInt32(lpBuffer.getMaxLength());
        UInt32 res = new UInt32();
        final Function getWindowsDirectory = getInstance().getFunction(FUNCTION_GetWindowsDirectory.toString());
        getWindowsDirectory.invoke(res, new Pointer(lpBuffer), uSize);
        if (res.getValue() != 0 && res.getValue() <= 256)
        {
            return lpBuffer.getValue().endsWith("\\") ? lpBuffer.getValue() : lpBuffer.getValue() + "\\";
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns the absolute path to the Windows system directory.
     *
     * @return the absolute path to the Windows system directory.
     */
    public static String getSystemDirectory()
    {
        Str lpBuffer = new Str();
        UInt32 uSize = new UInt32(lpBuffer.getMaxLength());
        UInt32 res = new UInt32();
        final Function getWindowsDirectory = getInstance().getFunction(FUNCTION_GetSystemDirectory.toString());
        getWindowsDirectory.invoke(res, new Pointer(lpBuffer), uSize);
        if (res.getValue() != 0 && res.getValue() <= 256)
        {
            return lpBuffer.getValue().endsWith("\\") ? lpBuffer.getValue() : lpBuffer.getValue() + "\\";
        }
        else
        {
            return null;
        }
    }

    /**
     * Creates a globally unique identifier that corresponds to the specified string.
     *
     * @param value a string value not longer than 255 bytes.
     * @return a globally unique identifier.
     */
    public static int globalAddAtom(String value)
    {
        if (value == null)
        {
            throw new IllegalArgumentException("value cannot be null.");
        }

        Function function = getInstance().getFunction(FUNCTION_GlobalAddAtom.toString());
        UInt16 atom = new UInt16();
        long errorCode = function.invoke(atom, new Str(value));
        if (atom.getValue() == 0)
        {
            throw new LastErrorException(errorCode);
        }

        return (int) atom.getValue();
    }

    public static String getCurrentDirectory()
    {
        final int MAX_PATH = 255;

        Function getCurrentDirectory = getInstance().getFunction(FUNCTION_GET_CURRENT_DIRECTORY.toString());
        Str currentDirectory = new Str(MAX_PATH);
        UInt32 res = new UInt32();
        getCurrentDirectory.invoke(res, new UInt32(currentDirectory.getLength()), new Pointer(currentDirectory));
        if (res.getValue() == 0)
        {
            currentDirectory.setValue("");
        }
        else if (res.getValue() >= MAX_PATH)
        {
            currentDirectory = new Str((int) res.getValue());
            getCurrentDirectory.invoke(res, new UInt32(currentDirectory.getLength()), new Pointer(currentDirectory));
            if (res.getValue() == 0)
            {
                currentDirectory.setValue("");
            }
        }

        return currentDirectory.getValue();
    }

    public static void setCurrentDirectory(String directory)
    {
        if (directory == null || !new File(directory).isDirectory())
        {
            throw new IllegalArgumentException("The specified directory is invalid.");
        }
        Function setCurrentDirectory = getInstance().getFunction(FUNCTION_SET_CURRENT_DIRECTORY.toString());
        setCurrentDirectory.invoke(new IntBool(), new Str(directory));
    }

    public static long getProcessId(com.jniwrapper.win32.process.Process process)
    {
        UInt32 processId = new UInt32();
        Function getProcessId = getInstance().getFunction(FUNCTION_GetProcessId);
        long errorCode = getProcessId.invoke(processId, process);

        if (processId.getValue() == 0)
        {
            throw new LastErrorException(errorCode);
        }

        return processId.getValue();
    }

    public static long processIdToSessionId(UInt32 processId)
    {
        Handle result = new Handle();
        UInt32 sessionId = new UInt32();
        Function processIdToSessionId = getInstance().getFunction(FUNCTION_ProcessIdToSessionId);
        long errorCode = processIdToSessionId.invoke(result, processId, new Pointer(sessionId));

        if (result.getValue() == 0)
        {
            throw new LastErrorException(errorCode);
        }

        return sessionId.getValue();
    }
}

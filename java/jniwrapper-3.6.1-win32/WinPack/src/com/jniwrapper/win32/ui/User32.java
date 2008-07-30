/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.ui;

import com.jniwrapper.*;
import com.jniwrapper.win32.FunctionName;
import com.jniwrapper.win32.Handle;
import com.jniwrapper.win32.LastErrorException;
import com.jniwrapper.win32.WinFunctionCache;
import com.jniwrapper.win32.gdi.GdiObject;

/**
 * This class wraps functionality provided by user32 library.
 *
 * @author Alexander Evsukov
 */
public class User32 extends WinFunctionCache
{
    public static final FunctionName FUNCTION_GET_MESSAGE = new FunctionName("GetMessage");
    public static final FunctionName FUNCTION_POST_THREAD_MESSAGE = new FunctionName("PostThreadMessage");
    public static final String FUNCTION_TRANSLATE_MESSAGE = "TranslateMessage";
    public static final FunctionName FUNCTION_DISPATCH_MESSAGE = new FunctionName("DispatchMessage");
    public static final String FUNCTION_POST_QUIT_MESSAGE = "PostQuitMessage";
    public static final String FUNCTION_CREATE_ELLIPRIC_RGN = "CreateEllipticRgn";
    public static final FunctionName FUNCTION_LOAD_IMAGE = new FunctionName("LoadImage");

    public static final String FUNCTION_CREATE_MENU = "CreateMenu";
    public static final String FUNCTION_CREATE_POPUP_MENU = "CreatePopupMenu";
    public static final String FUNCTION_SET_MENU = "SetMenu";
    public static final String FUNCTION_GET_MENU = "GetMenu";
    public static final FunctionName FUNCTION_REGISTER_CLIPBOARD_FORMAT = new FunctionName("RegisterClipboardFormat");

    public static final String FUNCTION_ATTACH_THREAD_INPUT = "AttachThreadInput";
    public static final String FUNCTION_GET_WINDOW_THREAD_PROCESS_ID = "GetWindowThreadProcessId";

    private static User32 _instance;

    private User32()
    {
        super("user32");
    }

    public static User32 getInstance()
    {
        if (_instance == null)
        {
            _instance = new User32();
        }

        return _instance;
    }

    public static void postQuitMessage(int exitCode)
    {
        final Function function = getInstance().getFunction(FUNCTION_POST_QUIT_MESSAGE);
        function.invoke(null, new Int(exitCode));
    }

    public static Handle loadResourceFromFile(String fileName, int resourceType)
    {
        return loadResourceFromFile(fileName, resourceType, 0, 0);
    }

    public static Handle loadResourceFromFile(String fileName, int resourceType, int width, int height)
    {
        final Function function = getInstance().getFunction(FUNCTION_LOAD_IMAGE.toString());
        final Handle result = new Handle();
        long errorCode = function.invoke(result, new Parameter[]
        {
            new Pointer(null, true),
            new Str(fileName),
            new UInt(resourceType),
            new Int(width),
            new Int(height),
            new UInt(GdiObject.ImageLoadParameters.SHARED | GdiObject.ImageLoadParameters.LOADFROMFILE)
        });
        if (result.isNull())
        {
            throw new LastErrorException(errorCode, "Failed to load resource.", true);
        }
        return result;
    }

    public static void setMenu(Wnd wnd, Handle hmenu)
    {
        Function function = User32.getInstance().getFunction(FUNCTION_SET_MENU);
        function.invoke(null, wnd, hmenu);
    }

    public static Handle getMenu(Wnd wnd)
    {
        Handle result = new Handle();

        Function function = User32.getInstance().getFunction(FUNCTION_GET_MENU);
        function.invoke(result, wnd);

        return result;
    }

    public static Handle createMenu()
    {
        Handle result = new Handle();

        Function function = User32.getInstance().getFunction(FUNCTION_CREATE_MENU);
        function.invoke(result);

        return result;
    }

    public static Handle createPopupMenu()
    {
        Handle result = new Handle();

        Function function = getInstance().getFunction(FUNCTION_CREATE_POPUP_MENU);
        function.invoke(result, new Parameter[]{});

        return result;
    }

    public static long registerClipboardFormat(String format)
    {
        Function function = getInstance().getFunction(FUNCTION_REGISTER_CLIPBOARD_FORMAT.toString());

        UInt result = new UInt();
        function.invoke(result, new Str(format));

        return result.getValue();
    }

    /**
     * Attaches or detaches input processing mechanism of one thread to another thread.
     *
     * @param threadIdAttach   identifier of attached thread
     * @param threadIdAttachTo identifier of thread to which attached another thread
     * @param attach           if true then attach else detach input processing mechanism
     */
    private static void attachThreadInput(int threadIdAttach, int threadIdAttachTo, boolean attach)
    {
        Int result = new Int();

        Function function = User32.getInstance().getFunction(FUNCTION_ATTACH_THREAD_INPUT);
        function.invoke(result, new UInt32(threadIdAttach), new UInt32(threadIdAttachTo), new Bool(attach));
        if (result.getValue() == 0)
        {
            throw new RuntimeException("Failed to attach thread input. Tried to attach thread: " + threadIdAttach + " to thread: " + threadIdAttachTo);
        }
    }

    /**
     * Attaches input processing mechanism of one thread to another thread.
     *
     * @param threadIdAttach   identifier of attached thread
     * @param threadIdAttachTo identifier of thread to which attached another thread
     * @see #detachThreadInput(int, int) to detach the thread input
     */
    public static void attachThreadInput(int threadIdAttach, int threadIdAttachTo)
    {
        attachThreadInput(threadIdAttach, threadIdAttachTo, true);
    }

    /**
     * Detaches input processing mechanism of one thread from another thread.
     *
     * @param threadIdAttach   identifier of detached thread
     * @param threadIdAttachTo identifier of thread from which detached another thread
     * @see #attachThreadInput(int, int) to attach the thread input
     */
    public static void detachThreadInput(int threadIdAttach, int threadIdAttachTo)
    {
        attachThreadInput(threadIdAttach, threadIdAttachTo, false);
    }
}

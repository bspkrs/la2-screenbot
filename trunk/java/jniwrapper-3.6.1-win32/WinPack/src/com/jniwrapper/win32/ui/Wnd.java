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
import com.jniwrapper.util.EnumItem;
import com.jniwrapper.util.FlagSet;
import com.jniwrapper.win32.*;
import com.jniwrapper.win32.gdi.ColorRef;
import com.jniwrapper.win32.gdi.DC;
import com.jniwrapper.win32.gdi.Icon;
import com.jniwrapper.win32.gdi.Region;

import java.awt.Component;
import java.util.LinkedList;
import java.util.List;

/**
 * This class provides methods and constants to work with native windows. It
 * corresponds to <code>HWND</code> native Windows data type.
 *
 * @author Serge Piletsky
 */
public class Wnd extends Handle
{
    private WindowProc _windowProc;
    private WindowMessageListener _windowMessageListener;

    public static final int GW_HWNDFIRST = 0;
    public static final int GW_HWNDLAST = 1;
    public static final int GW_HWNDNEXT = 2;
    public static final int GW_HWNDPREV = 3;
    public static final int GW_OWNER = 4;
    public static final int GW_CHILD = 5;

    private static final FunctionName FUNCTION_FIND_WINDOW = new FunctionName("FindWindow");
    private static final FunctionName FUNCTION_FIND_WINDOW_EX = new FunctionName("FindWindowEx");
    private static final FunctionName FUNCTION_CREATE_WINDOW_EX = new FunctionName("CreateWindowEx");
    private static final FunctionName FUNCTION_GetClassName = new FunctionName("GetClassName");
    private static final String FUNCTION_GET_WINDOW = "GetWindow";
    private static final String FUNCTION_GET_DESKTOP_WINDOW = "GetDesktopWindow";
    private static final String FUNCTION_UPDATE_WINDOW = "UpdateWindow";
    private static final String FUNCTION_DESTROY_WINDOW = "DestroyWindow";
    private static final String FUNCTION_GET_WINDOW_RECT = "GetWindowRect";
    private static final String FUNCTION_GET_CLIENT_RECT = "GetClientRect";
    private static final String FUNCTION_SET_WINDOW_RGN = "SetWindowRgn";
    private static final String FUNCTION_GET_WINDOW_RGN = "GetWindowRgn";
    private static final String FUNCTION_SET_WINDOW_POS = "SetWindowPos";
    private static final String FUNCTION_MOVE_WINDOW = "MoveWindow";
    private static final String FUNCTION_BEGIN_PAINT = "BeginPaint";
    private static final String FUNCTION_END_PAINT = "EndPaint";
    private static final String FUNCTION_SHOW_WINDOW = "ShowWindow";
    private static final FunctionName FUNCTION_SET_WINDOW_LONG = new FunctionName("SetWindowLong");
    private static final FunctionName FUNCTION_GET_WINDOW_LONG = new FunctionName("GetWindowLong");
    private static final String FUNCTION_SET_LAYERED_WINDOW_ATTRIBUTES = "SetLayeredWindowAttributes";
    private static final String FUNCTION_GET_LAYERED_WINDOW_ATTRIBUTES = "GetLayeredWindowAttributes";
    private static final String FUNCTION_REDRAW_WINDOW = "RedrawWindow";
    private static final FunctionName FUNCTION_PEEK_MESSAGE = new FunctionName("PeekMessage");
    private static final FunctionName FUNCTION_SEND_MESSAGE = new FunctionName("SendMessage");
    private static final FunctionName FUNCTION_POST_MESSAGE = new FunctionName("PostMessage");
    private static final String FUNCTION_SET_PARENT = "SetParent";
    private static final String FUNCTION_GET_PARENT = "GetParent";
    private static final String FUNCTION_BRING_WINDOW_TO_TOP = "BringWindowToTop";
    private static final String FUNCTION_SetForegroundWindow = "SetForegroundWindow";
    private static final FunctionName FUNCTION_CALL_WINDOW_PROC = new FunctionName("CallWindowProc");
    private static final String FUNCTION_GET_UPDATE_RECT = "GetUpdateRect";
    private static final String FUNCTION_InvalidateRect = "InvalidateRect";

    private static final String FUNCTION_FLASH_WINDOW = "FlashWindowEx";

    private static final String FUNCTION_ScreenToClient = "ScreenToClient";
    private static final String FUNCTION_ClientToScreen = "ClientToScreen";

    private static final String FUNCTION_EnumWindows = "EnumWindows";
    private static final String FUNCTION_EnumChildWindows = "EnumChildWindows";
    private static final String FUNCTION_EnumThreadWindows = "EnumThreadWindows";

    private static final FunctionName FUNCTION_GetWindowText = new FunctionName("GetWindowText");
    private static final String FUNCTION_SetFocus = "SetFocus";
    private static final String FUNCTION_GetFocus = "GetFocus";
    private static final String FUNCTION_IsZoomed = "IsZoomed";

    public static final String FUNCTION_GET_WINDOW_THREAD_PROCESS_ID = "GetWindowThreadProcessId";
    private static final String FUNCTION_IsWindow = "IsWindow";

    /*
    * Window Styles
    */
    public static final int WS_OVERLAPPED = 0x00000000;
    public static final int WS_POPUP = 0x80000000;
    public static final int WS_CHILD = 0x40000000;
    public static final int WS_MINIMIZE = 0x20000000;
    public static final int WS_VISIBLE = 0x10000000;
    public static final int WS_DISABLED = 0x08000000;
    public static final int WS_CLIPSIBLINGS = 0x04000000;
    public static final int WS_CLIPCHILDREN = 0x02000000;
    public static final int WS_MAXIMIZE = 0x01000000;
    public static final int WS_CAPTION = 0x00C00000;     /* WS_BORDER | WS_DLGFRAME  */
    public static final int WS_BORDER = 0x00800000;
    public static final int WS_DLGFRAME = 0x00400000;
    public static final int WS_VSCROLL = 0x00200000;
    public static final int WS_HSCROLL = 0x00100000;
    public static final int WS_SYSMENU = 0x00080000;
    public static final int WS_THICKFRAME = 0x00040000;
    public static final int WS_GROUP = 0x00020000;
    public static final int WS_TABSTOP = 0x00010000;

    public static final int WS_MINIMIZEBOX = 0x00020000;
    public static final int WS_MAXIMIZEBOX = 0x00010000;

    /*
     * Common Window Styles
     */
    public static final int WS_OVERLAPPEDWINDOW = WS_OVERLAPPED | WS_CAPTION | WS_SYSMENU | WS_THICKFRAME | WS_MINIMIZEBOX | WS_MAXIMIZEBOX;
    public static final int WS_POPUPWINDOW = WS_POPUP | WS_BORDER | WS_SYSMENU;
    public static final int WS_CHILDWINDOW = WS_CHILD;

    public static final int WS_TILED = WS_OVERLAPPED;
    public static final int WS_ICONIC = WS_MINIMIZE;
    public static final int WS_SIZEBOX = WS_THICKFRAME;
    public static final int WS_TILEDWINDOW = WS_OVERLAPPEDWINDOW;

    /*
     * Extended Window Styles
     */
    public static final int WS_EX_DLGMODALFRAME = 0x00000001;
    public static final int WS_EX_NOPARENTNOTIFY = 0x00000004;
    public static final int WS_EX_TOPMOST = 0x00000008;
    public static final int WS_EX_ACCEPTFILES = 0x00000010;
    public static final int WS_EX_TRANSPARENT = 0x00000020;
    public static final int WS_EX_MDICHILD = 0x00000040;
    public static final int WS_EX_TOOLWINDOW = 0x00000080;
    public static final int WS_EX_WINDOWEDGE = 0x00000100;
    public static final int WS_EX_CLIENTEDGE = 0x00000200;
    public static final int WS_EX_CONTEXTHELP = 0x00000400;
    public static final int WS_EX_RIGHT = 0x00001000;
    public static final int WS_EX_LEFT = 0x00000000;
    public static final int WS_EX_RTLREADING = 0x00002000;
    public static final int WS_EX_LTRREADING = 0x00000000;
    public static final int WS_EX_LEFTSCROLLBAR = 0x00004000;
    public static final int WS_EX_RIGHTSCROLLBAR = 0x00000000;
    public static final int WS_EX_CONTROLPARENT = 0x00010000;
    public static final int WS_EX_STATICEDGE = 0x00020000;
    public static final int WS_EX_APPWINDOW = 0x00040000;
    public static final int WS_EX_OVERLAPPEDWINDOW = (WS_EX_WINDOWEDGE | WS_EX_CLIENTEDGE);
    public static final int WS_EX_PALETTEWINDOW = (WS_EX_WINDOWEDGE | WS_EX_TOOLWINDOW | WS_EX_TOPMOST);
    public static final int WS_EX_LAYERED = 0x00080000;

    public static final int LWA_COLORKEY = 0x00000001;
    public static final int LWA_ALPHA = 0x00000002;

    /*
     * Flags for SetWindowPos() function
     */
    public static final int SWP_NOSIZE = 0x0001;
    public static final int SWP_NOMOVE = 0x0002;
    public static final int SWP_NOZORDER = 0x0004;
    public static final int SWP_NOREDRAW = 0x0008;
    public static final int SWP_NOACTIVATE = 0x0010;
    public static final int SWP_FRAMECHANGED = 0x0020;  /* The frame changed: send WM_NCCALCSIZE */
    public static final int SWP_SHOWWINDOW = 0x0040;
    public static final int SWP_HIDEWINDOW = 0x0080;
    public static final int SWP_NOCOPYBITS = 0x0100;
    public static final int SWP_NOOWNERZORDER = 0x0200;  /* Don't do owner Z ordering */
    public static final int SWP_NOSENDCHANGING = 0x0400;  /* Don't send WM_WINDOWPOSCHANGING */
    public static final int SWP_DRAWFRAME = SWP_FRAMECHANGED;
    public static final int SWP_NOREPOSITION = SWP_NOOWNERZORDER;
    public static final int SWP_DEFERERASE = 0x2000;
    public static final int SWP_ASYNCWINDOWPOS = 0x4000;

    public static final int HWND_TOP = 0;
    public static final int HWND_BOTTOM = 1;
    public static final int HWND_TOPMOST = -1;
    public static final int HWND_NOTOPMOST = -2;

    /*
     * Window field offsets for GetWindowLong() and GetWindowLong() funcnctions
     */
    public static final int GWL_WNDPROC = -4;
    public static final int GWL_HINSTANCE = -6;
    public static final int GWL_HWNDPARENT = -8;
    public static final int GWL_STYLE = -16;
    public static final int GWL_EXSTYLE = -20;
    public static final int GWL_USERDATA = -21;
    public static final int GWL_ID = -12;

    /*
     * Redraw Window Flags for RedrawWindow() API function
     */
    public static final int RDW_INVALIDATE = 0x0001;
    public static final int RDW_INTERNALPAINT = 0x0002;
    public static final int RDW_ERASE = 0x0004;
    public static final int RDW_VALIDATE = 0x0008;
    public static final int RDW_NOINTERNALPAINT = 0x0010;
    public static final int RDW_NOERASE = 0x0020;
    public static final int RDW_NOCHILDREN = 0x0040;
    public static final int RDW_ALLCHILDREN = 0x0080;
    public static final int RDW_UPDATENOW = 0x0100;
    public static final int RDW_ERASENOW = 0x0200;
    public static final int RDW_FRAME = 0x0400;
    public static final int RDW_NOFRAME = 0x0800;

    public static final int OPAQUE = 255;
    public static final int TRANSPARENT = 0;

    /**
     * ShowWindowCommand class represents enumeration of commands to use in
     * ShowWindow function.
     */
    public static class ShowWindowCommand extends EnumItem
    {
        public static final ShowWindowCommand HIDE = new ShowWindowCommand(0);
        public static final ShowWindowCommand SHOWNORMAL = new ShowWindowCommand(1);
        public static final ShowWindowCommand NORMAL = new ShowWindowCommand(1);
        public static final ShowWindowCommand SHOWMINIMIZED = new ShowWindowCommand(2);
        public static final ShowWindowCommand SHOWMAXIMIZED = new ShowWindowCommand(3);
        public static final ShowWindowCommand MAXIMIZE = new ShowWindowCommand(3);
        public static final ShowWindowCommand SHOWNOACTIVATE = new ShowWindowCommand(4);
        public static final ShowWindowCommand SHOW = new ShowWindowCommand(5);
        public static final ShowWindowCommand MINIMIZE = new ShowWindowCommand(6);
        public static final ShowWindowCommand SHOWMINNOACTIVE = new ShowWindowCommand(7);
        public static final ShowWindowCommand SHOWNA = new ShowWindowCommand(8);
        public static final ShowWindowCommand RESTORE = new ShowWindowCommand(9);
        public static final ShowWindowCommand SHOWDEFAULT = new ShowWindowCommand(10);
        public static final ShowWindowCommand FORCEMINIMIZE = new ShowWindowCommand(11);
        public static final ShowWindowCommand MAX = new ShowWindowCommand(11);

        protected ShowWindowCommand(int value)
        {
            super(value);
        }
    }

    public Wnd()
    {
        super();
    }

    /**
     * Constructs a new instance with the passed handle value.
     *
     * @param value native window handle.
     */
    public Wnd(long value)
    {
        super(value);
    }

    /**
     * Constructs an instance getting the handle from the passed AWT component.
     *
     * @param component the component to get the handle from.
     * @see WindowTools#getWindowHandle(Component)
     */
    public Wnd(Component component)
    {
        this(WindowTools.getWindowHandle(component));
    }

    static Function getFunction(Object functionName)
    {
        return User32.getInstance().getFunction(functionName.toString());
    }

    /**
     * Executes event loop for itself.
     */
    public void eventLoop()
    {
        eventLoop(getValue());
    }

    /**
     * Executes infinite event loop handling cycle for the window specified by
     * the passed handle value.
     *
     * @param hwnd a window handle to run the event loop for.
     */
    public static void eventLoop(final long hwnd)
    {
        if (hwnd == 0)
        {
            throw new IllegalArgumentException("Event processing window is not available");
        }

        final Function getMessage = getFunction(User32.FUNCTION_GET_MESSAGE);
        final Function translateMessage = getFunction(User32.FUNCTION_TRANSLATE_MESSAGE);
        final Function dispatchMessage = getFunction(User32.FUNCTION_DISPATCH_MESSAGE);
        final Wnd hWnd = new Wnd(hwnd);

        final ShortInt result = new ShortInt(1);
        final Msg msg = new Msg();
        final IntPtr nullValue = new IntPtr(0);

        while (result.getValue() != 0)
        {
            final Pointer msgPointer = new Pointer(msg);

            getMessage.invoke(result, msgPointer, hWnd, nullValue, nullValue);
            if (result.getValue() == -1)
            {
                break;
            }
            else
            {
                translateMessage.invoke(null, msgPointer);
                dispatchMessage.invoke(null, msgPointer);
            }
        }
    }

    /**
     * Searches the window by a specified class name and its title.
     *
     * @param className
     * @param windowName
     * @return handle of the found window if succeeds.
     */
    public static Wnd findWindow(String className, String windowName)
    {
        final Function function = getFunction(FUNCTION_FIND_WINDOW);
        Wnd result = new Wnd();
        Parameter wndName = (windowName == null) ?
                (Parameter)new Handle() : (Parameter)new Str(windowName);
        final Str cName = new Str(className);
        function.invoke(result, new Pointer(cName), wndName);
        return result;
    }

    /**
     * Searches a child window by specified class name and its title.
     *
     * @param parent     handle of the parent window.
     * @param className
     * @param windowName
     * @return handle of the found window if succeeds.
     */
    public static Wnd findWindowEx(Wnd parent, String className, String windowName)
    {
        final Function function = getFunction(FUNCTION_FIND_WINDOW_EX);
        Wnd result = new Wnd();
        Parameter wndName = (windowName == null) ?
                (Parameter)new Handle() : (Parameter)new Str(windowName);
        final Str cName = new Str(className);
        function.invoke(result,
                parent != null ? parent : new Handle(),
                new Handle(),
                new Pointer(cName), new Pointer(wndName));
        return result;
    }

    /**
     * Searches a window by a specified class name.
     *
     * @param className
     * @return handle of the found window if succeeds.
     */
    public static Wnd findWindow(String className)
    {
        return findWindow(className, null);
    }

    /**
     * Searches a window by a specified name.
     *
     * @param windowName
     * @return handle of the found window if succeeds.
     */
    public static Wnd findWindowByName(String windowName)
    {
        final Function function = getFunction(FUNCTION_FIND_WINDOW);
        Wnd result = new Wnd();
        function.invoke(result, new Pointer(null, true), new Str(windowName));
        return result;
    }

    /**
     * Returns a desktop window handle.
     *
     * @return desktop window handle.
     */
    public static Wnd getDesktopWindow()
    {
        final Function function = getFunction(FUNCTION_GET_DESKTOP_WINDOW);
        Wnd result = new Wnd();
        function.invoke(result);
        return result;
    }

    private static Wnd createWindow(Object functionName, Parameter[] parameters)
    {
        Wnd result = new Wnd();
        final Function function = getFunction(functionName);
        long errorCode = function.invoke(result, parameters);
        if (result.getValue() == 0)
        {
            throw new LastErrorException(errorCode, "Cannot create window.");
        }
        return result;
    }

    /**
     * Creates a new window.
     *
     * @param className   a registered window class name.
     * @param windowName  the window name.
     * @param windowStyle
     * @param x           the horizontal position of the window.
     * @param y           the vertical position of the window.
     * @param nWidth      the window width.
     * @param nHeight     the window height.
     * @param hWndParent  a handle to a parent or an owner window.
     * @param hMenu       a menu handle or a child identifier.
     * @param hInstance   a handle to an application instance.
     * @param lpParam     window-creation data.
     * @return instance of a newly created window.
     */
    public static Wnd createWindow(String className,
                                   String windowName,
                                   int windowStyle,
                                   long x,
                                   long y,
                                   long nWidth,
                                   long nHeight,
                                   Wnd hWndParent,
                                   Handle hMenu,
                                   Handle hInstance,
                                   Handle lpParam)
    {
        return createWindow(FUNCTION_CREATE_WINDOW_EX,
                new Parameter[]{
                    new UInt(0),
                    new Str(className), // registered class name
                    new Str(windowName),
                    new Int(windowStyle), // window style
                    new Int(x), // horizontal position of window
                    new Int(y), // vertical position of window
                    new Int(nWidth), // window nWidth
                    new Int(nHeight), // window nHeight
                    hWndParent, // handle to parent or owner window
                    hMenu, // menu handle or child identifier
                    hInstance, // handle to application instance
                    lpParam  // window-creation data
                });
    }

    /**
     * Creates a new window with extended style.
     *
     * @param windowStyleEx extended window style.
     * @param className     a registered window class name.
     * @param windowName    the window name.
     * @param windowStyle
     * @param x             the horizontal position of the window.
     * @param y             the vertical position of the window.
     * @param nWidth        the window width.
     * @param nHeight       the window height.
     * @param hWndParent    a handle to a parent or an owner window.
     * @param hMenu         a menu handle or a child identifier.
     * @param hInstance     a handle to an application instance.
     * @param lpParam       window-creation data.
     * @return instance of a newly created window.
     */
    public static Wnd createWindow(int windowStyleEx,
                                   String className,
                                   String windowName,
                                   int windowStyle,
                                   long x,
                                   long y,
                                   long nWidth,
                                   long nHeight,
                                   Wnd hWndParent,
                                   Handle hMenu,
                                   Handle hInstance,
                                   Handle lpParam)
    {
        return createWindow(FUNCTION_CREATE_WINDOW_EX,
                new Parameter[]{
                    new Int32(windowStyleEx),
                    new Str(className),
                    new Str(windowName),
                    new Int(windowStyle),
                    new Int(x),
                    new Int(y),
                    new Int(nWidth),
                    new Int(nHeight),
                    hWndParent,
                    hMenu,
                    hInstance,
                    lpParam
                });
    }

    /**
     * Creates a new window by its class name.
     *
     * @param className a new window class name.
     * @return a new window instance.
     */
    public static Wnd createWindow(String className)
    {
        Handle NULL = new Handle();
        Wnd parent = new Wnd();
        return createWindow(0, className, className, 0, 0, 0, 0, 0, parent, NULL, NULL, NULL);
    }

    /**
     * Updates the client area of the window.
     */
    public void update()
    {
        final Function function = getFunction(FUNCTION_UPDATE_WINDOW);
        Int result = new Int();
        long errorCode = function.invoke(result, this);
        if (result.getValue() == 0)
        {
            throw new LastErrorException(errorCode, "Error updating window.");
        }
    }

    /**
     * Destroys a window.
     */
    public void destroy()
    {
        final Function function = getFunction(FUNCTION_DESTROY_WINDOW);
        Int result = new Int();
        long errorCode = function.invoke(result, this);
        if (result.getValue() == 0)
        {
            throw new LastErrorException(errorCode, "Error destroying window.");
        }
    }

    /**
     * Returns window size (width and height).
     *
     * @return window size (width and height).
     */
    public Size getSize()
    {
        Size result = new Size();
        final Rect bounds = getWindowRect();
        result.setCx(bounds.getRightAsInt() - bounds.getLeftAsInt());
        result.setCy(bounds.getBottomAsInt() - bounds.getTopAsInt());
        return result;
    }

    /**
     * Returns window's client size (width and height).
     *
     * @return window's client size (width and height).
     */
    public Size getClientSize()
    {
        Size result = new Size();
        final Rect bounds = getClientRect();
        result.setCx(bounds.getRightAsInt() - bounds.getLeftAsInt());
        result.setCy(bounds.getBottomAsInt() - bounds.getTopAsInt());
        return result;
    }


    /**
     * Returns window bounds.
     *
     * @return window bounds.
     */
    public Rect getBounds()
    {
        Rect result = new Rect();
        final Function function = getFunction(FUNCTION_GET_WINDOW_RECT);
        function.invoke(null, this,
                new Pointer.OutOnly(result));
        return result;
    }

    /**
     * Returns the bounding rectangle of the window.
     *
     * @return the bounding rectangle of the window.
     */
    public Rect getWindowRect()
    {
        final Function function = getFunction(FUNCTION_GET_WINDOW_RECT);
        Rect result = new Rect();
        function.invoke(null, this, new Pointer.OutOnly(result));
        return result;
    }

    /**
     * Centers the window over the desktop.
     */
    public void centerInDesktop()
    {
        Rect desktop = getDesktopWindow().getWindowRect();
        Rect windowRect = getWindowRect();

        int left = (desktop.getWidth() - windowRect.getWidth()) / 2;
        int top = (desktop.getHeight() - windowRect.getHeight()) / 2;

        windowRect.moveTo(left, top);

        moveWindow((int)windowRect.getLeft(), (int)windowRect.getTop(),
                windowRect.getWidth(),
                windowRect.getHeight(),
                true);
    }

    /**
     * Returns a window client rectangle.
     *
     * @return window client rectangle.
     */
    public Rect getClientRect()
    {
        Rect result = new Rect();
        final Function function = getFunction(FUNCTION_GET_CLIENT_RECT);
        function.invoke(null, this, new Pointer.OutOnly(result));
        return result;
    }

    /**
     * Sets a new window region.
     *
     * @param region a new region.
     * @param redraw if true, the window will be redrawn.
     */
    public void setRegion(Region region, boolean redraw)
    {
        final Function function = getFunction(FUNCTION_SET_WINDOW_RGN);
        function.invoke(null,
                this,
                region == null ? new Handle() : region,
                new Bool(redraw));
    }

    /**
     * Returns a copy of the window region of a window.
     *
     * @return window region of the window.
     */
    public Region getRegion()
    {
        final Region result = new Region();
        final Function function = getFunction(FUNCTION_GET_WINDOW_RGN);
        function.invoke(null, this, result);
        return result;
    }

    /**
     * Changes the size, position, and Z order of a window.
     *
     * @param wndInsertAfter a window to precede the positioned window in the Z
     *                       order.
     * @param x              position of the left side of the window.
     * @param y              position of the top of the window.
     * @param cx             a new width of the window.
     * @param cy             a new height of the window.
     * @param flags          window sizing and positioning flags.
     */
    public boolean setPosition(Wnd wndInsertAfter,
                               long x,
                               long y,
                               long cx,
                               long cy,
                               long flags)
    {
        final Function function = getFunction(FUNCTION_SET_WINDOW_POS);
        IntBool result = new IntBool();
        long errorCode = function.invoke(result, new Parameter[]
        {
            this,
            wndInsertAfter,
            new Int(x),
            new Int(y),
            new Int(cx),
            new Int(cy),
            new UInt(flags)
        });

        if (result.getValue() == 0)
        {
            throw new LastErrorException(errorCode, "Failed to show splash.", true);
        }

        return true;
    }

    /**
     * Prepares a specified window for painting.
     *
     * @param paintStruct paint data
     * @return DC if succeeds.
     */
    public DC beginPaint(PaintStruct paintStruct)
    {
        final Function beginPaint = getFunction(FUNCTION_BEGIN_PAINT);
        DC result = new DC();
        long errorCode = beginPaint.invoke(result, this, new Pointer.OutOnly(paintStruct));
        if (result.isNull())
        {
            throw new LastErrorException(errorCode, "Failed to get DC.", true);
        }
        return result;
    }

    /**
     * Marks the end of painting.
     *
     * @param paintStruct paint data.
     */
    public void endPaint(PaintStruct paintStruct)
    {
        final Function endPaint = User32.getInstance().getFunction(FUNCTION_END_PAINT);
        endPaint.invoke(null, this, new Pointer.Const(paintStruct));
    }

    /**
     * Sends a message to a window.
     *
     * @param msg    a message to be sent.
     * @param wParam WPARAM
     * @param lParam LPARAM
     * @return If the function succeeds, the return value is nonzero.
     * @deprecated use sendMessageEx(int, long, long) instead.
     */
    public boolean sendMessage(int msg, long wParam, long lParam)
    {
        final Function sendMessage = getFunction(FUNCTION_SEND_MESSAGE);
        Bool result = new Bool();
        sendMessage.invoke(result, this, new UInt(msg), new IntPtr(wParam), new IntPtr(lParam));
        return result.getValue();
    }

    /**
     * Sends a message to a window.
     *
     * @param msg    a message to be sent.
     * @param wParam WPARAM
     * @param lParam LPARAM
     * @return the return value meaning depends on the message.
     */
    public long sendMessageEx(int msg, long wParam, long lParam)
    {
        final Function sendMessage = getFunction(FUNCTION_SEND_MESSAGE);
        IntPtr result = new IntPtr();
        sendMessage.invoke(result, this, new UInt(msg), new IntPtr(wParam), new IntPtr(lParam));
        return result.getValue();
    }

    public boolean peekMessage(long msg, boolean remove)
    {
        return peekMessage(new Msg(), msg, msg, remove);
    }

    public boolean peekMessage(Msg msg, long filterMin, long filterMax, boolean remove)
    {
        final Function peekMessage = getFunction(FUNCTION_PEEK_MESSAGE);
        Bool result = new Bool();
        peekMessage.invoke(result, new Parameter[]{
            new Pointer(msg),
            this,
            new UInt(filterMin),
            new UInt(filterMax),
            new UInt(remove ? 1 : 0)
        });
        return result.getValue();
    }

    /**
     * Posts a message in the message queue.
     *
     * @param msg    a message to be posted.
     * @param wParam WPARAM
     * @param lParam LPARAM
     * @return If the function succeeds, the return value is nonzero.
     */
    public boolean postMessage(int msg, long wParam, long lParam)
    {
        final Function sendMessage = getFunction(FUNCTION_POST_MESSAGE);
        IntBool result = new IntBool();
        sendMessage.invoke(result, this, new UInt(msg), new IntPtr(wParam), new IntPtr(lParam));
        return result.getBooleanValue();
    }

    /**
     * Sets a specified window's show state.
     *
     * @param command Specifies how the window is to be shown.
     */
    public void show(ShowWindowCommand command)
    {
        final Function show = getFunction(FUNCTION_SHOW_WINDOW);
        show.invoke(null, this, new Int(command.getValue()));
    }

    /**
     * Sets an attribute of the window.
     *
     * @param nIndex    nIndex the zero-based offset to the value to be retrieved.
     * @param dwNewLong specifies a new long value.
     */
    public void setWindowLong(int nIndex, long dwNewLong)
    {
        setWindowLong(nIndex, new IntPtr(dwNewLong));
    }

    /**
     * Sets an attribute of the window.
     *
     * @param nIndex   nIndex the zero-based offset to the value to be retrieved.
     * @param newValue specifies a new value.
     * @return if function succeeds, the return result is the previous value;
     *         otherwise zero.
     */
    public long setWindowLong(int nIndex, Parameter newValue)
    {
        UInt result = new UInt();
        final Function setWindowLong = getFunction(FUNCTION_SET_WINDOW_LONG);
        LastError.setValue(0);
        long errorCode = setWindowLong.invoke(result, this, new Int(nIndex), newValue);
        if (result.getValue() == 0 && errorCode != 0)
        {
            throw new LastErrorException(errorCode);
        }
        return result.getValue();
    }

    /**
     * Returns an attribute of the window.
     *
     * @param nIndex a zero-based offset to the value to be retrieved.
     * @return a requested value.
     */
    public long getWindowLong(int nIndex)
    {
        IntPtr result = new IntPtr();
        final Function getWindowLong = getFunction(FUNCTION_GET_WINDOW_LONG);
        LastError.setValue(0);
        long errorCode = getWindowLong.invoke(result, this, new Int(nIndex));
        return result.getValue();
    }

    /**
     * Sets opacity and transparency of a layered window.
     *
     * @param crKey   a value that specifies the transparency color key to be used
     *                when composing the layered window.
     * @param bAlpha  opacity of the layered window.
     * @param dwFlags an action to take.
     */
    public void setLayeredWindowAttributes(ColorRef crKey, byte bAlpha, int dwFlags)
    {
        final Function setLayeredWindowAttr = getFunction(FUNCTION_SET_LAYERED_WINDOW_ATTRIBUTES);
        setLayeredWindowAttr.invoke(null, this,
                crKey == null ? new ColorRef(0) : crKey,
                new Int8(bAlpha),
                new UInt32(dwFlags));
    }

    /**
     * Retrieves the opacity and transparency color key of a layered window.
     *
     * @param crKey   Receives the transparency color key to be used when composing the layered window.
     *                All pixels painted by the window in this color will be transparent.
     *                This can be <code>null</code> if the argument is not needed.
     * @param bAlpha  Receives the Alpha value used to describe the opacity of the layered window.
     *                When the variable referred to by pbAlpha is 0, the window is completely transparent.
     *                When the variable referred to by pbAlpha is 255, the window is opaque.
     *                This can be <code>null</code> if the argument is not needed.
     * @param dwFlags Receives a layering flag.
     *                This can be <code>null</code> if the argument is not needed.
     * @return If the function succeeds, the return value is <code>true</code>.
     *         If the function fails, the return value is <code>false</code>.
     */
    public boolean getLayeredWindowAttributes(ColorRef crKey, Int8 bAlpha, UInt32 dwFlags)
    {
        final Function getLayeredWindowAttr = getFunction(FUNCTION_GET_LAYERED_WINDOW_ATTRIBUTES);
        Int32 res = new Int32();
        getLayeredWindowAttr.invoke(res, this,
                crKey == null ? (Parameter)new Pointer.Void(0) : new Pointer.OutOnly(crKey),
                bAlpha == null ? (Parameter)new Pointer.Void(0) : new Pointer.OutOnly(bAlpha),
                dwFlags == null ? (Parameter)new Pointer.Void(0) : new Pointer.OutOnly(dwFlags));
        return res.getValue() != 0;
    }

    /**
     * Updates a specified rectangle or region in the window's client area.
     *
     * @param updateRect   update rectangle handle.
     * @param updateRegion update region handle.
     * @param flags        redraw flags.
     */
    public void redraw(Rect updateRect, Region updateRegion, int flags)
    {
        final Function redrawWindow = getFunction(FUNCTION_REDRAW_WINDOW);
        redrawWindow.invoke(null, this,
                new Pointer(updateRect, updateRect == null),
                updateRegion == null ? new Region() : updateRegion,
                new UInt(flags));
    }

    /**
     * Changes the parent of a window.
     *
     * @param parent a new parent window.
     */
    public void setParent(Wnd parent)
    {
        final Function setParent = getFunction(FUNCTION_SET_PARENT);
        Wnd previousParent = new Wnd();
        LastError.clearLastErrorCode();
        long lastError = setParent.invoke(previousParent, this, parent);
        if (previousParent.isNull() && lastError != LastError.NO_ERROR)
        {
            throw new LastErrorException(lastError);
        }
    }

    /**
     * Returns the window parent.
     *
     * @return window parent.
     */
    public Wnd getParent()
    {
        final Function getParent = getFunction(FUNCTION_GET_PARENT);
        Wnd result = new Wnd();
        getParent.invoke(result, this);
        return result;
    }

    /**
     * Brings the window to the top of the Z order.
     * <b>NOTE:</b>this function does not make a window a top-level window.
     */
    public void bringToTop()
    {
        final Function function = getFunction(FUNCTION_BRING_WINDOW_TO_TOP);
        function.invoke(null, this);
    }

    /**
     * Puts the thread that created the specified window into the foreground and activates
     * the window. Keyboard input is directed to the window, and various visual cues are
     * changed for the user.
     */
    public void setForeground()
    {
        final Function function = getFunction(FUNCTION_SetForegroundWindow);
        function.invoke(null, this);
    }

    /**
     * Passes message information to a specified window procedure.
     *
     * @param wndProc a window procedure.
     * @param wnd     handle to window procedure to receive the message.
     * @param msg     message
     * @param wParam  WPARAM
     * @param lParam  LPARAM
     * @return value that specifies the result of the message processing and
     *         depends on the message sent.
     */
    public static long callWindowProc(Handle wndProc, Wnd wnd, UInt msg, Pointer.Void wParam, Pointer.Void lParam)
    {
        final Function function = getFunction(FUNCTION_CALL_WINDOW_PROC);
        IntPtr result = new IntPtr();
        function.invoke(result, new Parameter[]
        {
            wndProc,
            wnd,
            msg,
            wParam,
            lParam
        });
        return result.getValue();
    }

    /**
     * Changes the position and dimensions of a specified window.
     *
     * @param x       a new left side.
     * @param y       a new top side.
     * @param width   a new width.
     * @param height  a new height.
     * @param repaint specifies whether the window is to be repainted.
     */
    public void moveWindow(int x, int y, int width, int height, boolean repaint)
    {
        final Function function = getFunction(FUNCTION_MOVE_WINDOW);
        Bool result = new Bool();
        function.invoke(result, new Parameter[]
        {
            this,
            new Int(x),
            new Int(y),
            new Int(width),
            new Int(height),
            new Bool(repaint),
        });
    }

    /**
     * Changes the position and dimensions of a specified window.
     *
     * @param x      a new left side.
     * @param y      a new top side.
     * @param width  a new width.
     * @param height a new height.
     */
    public void moveWindow(int x, int y, int width, int height)
    {
        this.moveWindow(x, y, width, height, false);
    }

    /**
     * Retrieves the coordinates of the smallest rectangle that completely
     * encloses the update region of the specified window.
     *
     * @param erase specifies whether the background in the update region is to
     *              be erased.
     * @return the coordinates of the smallest rectangle that completely
     *         encloses the update region of the specified window.
     */
    public boolean getUpdateRect(Rect rect, boolean erase)
    {
        final Function function = getFunction(FUNCTION_GET_UPDATE_RECT);
        Bool result = new Bool();
        function.invoke(result, this, new Pointer.OutOnly(rect), new Bool(erase));
        return result.getValue();
    }

    /**
     * Retrieves the coordinates of the smallest rectangle that completely
     * encloses the update region of the specified window.
     *
     * @return the coordinates of the smallest rectangle that completely
     *         encloses the update region of the specified window.
     */
    public boolean getUpdateRect(Rect rect)
    {
        return getUpdateRect(rect, false);
    }

    /**
     * Starts or stops window flashing depending on a passed parameter.
     *
     * @param flashInfo a structure that configures the flashing.
     * @return true if the function succeeds; otherwise false.
     */
    public boolean flashWindow(FlashInfo flashInfo)
    {
        final Function function = getFunction(FUNCTION_FLASH_WINDOW);
        Bool result = new Bool();
        function.invoke(result, new Pointer(flashInfo));
        return result.getValue();
    }

    /**
     * Returns the name of the window class.
     *
     * @return window class name.
     */
    public String getWindowClassName()
    {
        final Str className = new Str();
        final Function function = User32.getInstance().getFunction(FUNCTION_GetClassName.toString());
        function.invoke(null, this, className, new Int(className.getMaxLength()));
        return className.getValue();
    }

    /**
     * Returns a child, parent or sibling window for the current window.
     *
     * @param uCmd type of the returned window, one of GW_ constants.
     * @return child, parent or sibling window for the current window.
     */
    public Wnd getWindow(int uCmd)
    {
        Wnd result = new Wnd();
        User32.getInstance().getFunction(FUNCTION_GET_WINDOW).invoke(result, this, new UInt(uCmd));

        return result;
    }

    public Object clone()
    {
        return new Wnd(this.getValue());
    }

    /**
     * Converts the screen coordinates of a specified point on the screen to
     * client-area coordinates. The method does not modify the passed point
     * instance.
     *
     * @param point screen coordinates.
     * @return converted point of the client-area coordinates.
     */
    public Point screenToClient(Point point)
    {
        final Function function = User32.getInstance().getFunction(FUNCTION_ScreenToClient);
        Bool result = new Bool();
        Point resultPoint = new Point(point);
        long errorCode = function.invoke(result, this, new Pointer(resultPoint));
        if (!result.getValue())
        {
            throw new LastErrorException(errorCode);
        }
        return resultPoint;
    }

    /**
     * Converts the client-area coordinates of a specified point to screen
     * coordinates. The method does not modify the passed point instance.
     *
     * @param point contains the client coordinates to be converted.
     * @return converted point of the sreen coordinates.
     */
    public Point clientToScreen(Point point)
    {
        final Function function = User32.getInstance().getFunction(FUNCTION_ClientToScreen);
        Bool result = new Bool();
        Point resultPoint = new Point(point);
        long errorCode = function.invoke(result, this, new Pointer(resultPoint));
        if (!result.getValue())
        {
            throw new LastErrorException(errorCode);
        }
        return resultPoint;
    }

    /**
     * Returns all top-level windows on the screen.
     *
     * @return all top-level windows on the screen.
     */
    public static List getAllWindows()
    {
        final IntBool result = new IntBool();
        final IntPtr lParam = new IntPtr();
        final EnumWindowsCallback callback = new EnumWindowsCallback();
        final Function enumWindows = User32.getInstance().getFunction(FUNCTION_EnumWindows);
        enumWindows.invoke(result, callback, lParam);
        callback.dispose();
        return callback.getWindows();
    }

    /**
     * Returns child windows that belong to a specified parent window.
     *
     * @param parent a parent window whose child windows are to be enumerated .
     *               If this parameter is NULL, this function is equivalent to {@link
     *               #getAllWindows()}
     * @return child windows that belong to the specified parent window.
     */
    public static List getChildWindows(Wnd parent)
    {
        final IntBool result = new IntBool();
        final IntPtr lParam = new IntPtr();
        final EnumWindowsCallback callback = new EnumWindowsCallback();
        final Function enumChildWindows = User32.getInstance().getFunction(FUNCTION_EnumChildWindows);
        enumChildWindows.invoke(result, parent, callback, lParam);
        callback.dispose();
        return callback.getWindows();
    }

    /**
     * Returns child windows that belong to the current window.
     *
     * @return child windows that belong to the current window.
     */
    public List getChildWindows()
    {
        return getChildWindows(this);
    }

    /**
     * Returns all non-child windows associated with a specified thread.
     *
     * @param threadID specifies the thread whose windows are to be enumerated.
     * @return all non-child windows associated with the specified thread.
     */
    public static List getThreadWindows(long threadID)
    {
        final IntBool result = new IntBool();
        final IntPtr lParam = new IntPtr();
        final EnumWindowsCallback callback = new EnumWindowsCallback();
        final Function enumChildWindows = User32.getInstance().getFunction(FUNCTION_EnumThreadWindows);
        enumChildWindows.invoke(result, new UInt32(threadID), callback, lParam);
        callback.dispose();
        return callback.getWindows();
    }

    private static class EnumWindowsCallback extends Callback
    {
        private Wnd _hwnd = new Wnd();
        private IntPtr _parameter = new IntPtr();
        private Bool _returnVal = new Bool();

        private List _windows = new LinkedList();

        public EnumWindowsCallback()
        {
            init(new Parameter[]
            {
                _hwnd,
                _parameter
            }, _returnVal);
        }

        public void callback()
        {
            _windows.add(new Wnd(_hwnd.getValue()));
            _returnVal.setValue(true);
        }

        public List getWindows()
        {
            return _windows;
        }
    }

    /**
     * Returns text of a specified window's title bar (if it has one). If the
     * specified window is a control, the text of the control is returned.
     *
     * @return text of the specified window's title bar.
     */
    public String getWindowText()
    {
        final Function function = User32.getInstance().getFunction(FUNCTION_GetWindowText.toString());
        final Str text = new Str(1024);
        final Int resultLength = new Int();
        function.invoke(resultLength, this, new Pointer(text), new Int(1024));
        return text.getValue();
    }

    /**
     * Shows or hides the window.
     *
     * @param visible if <code>true</code>, shows this window; otherwise, hides this window.
     */
    public void setVisible(boolean visible)
    {
        long windowStyle = getWindowLong(Wnd.GWL_STYLE);

        if (!visible)
            windowStyle &= ~Wnd.WS_VISIBLE;
        else
            windowStyle |= Wnd.WS_VISIBLE;

        setWindowLong(Wnd.GWL_STYLE, windowStyle);
    }

    /**
     * Returns true if the window is visible.
     *
     * @return true if the window is visible.
     */
    public boolean isVisible()
    {
        long windowStyle = getWindowLong(Wnd.GWL_STYLE);
        return (windowStyle & Wnd.WS_VISIBLE) != 0;
    }

    /**
     * Sets window style.
     *
     * @param flags
     */
    public void setWindowStyle(long flags)
    {
        setWindowLong(Wnd.GWL_STYLE, flags);
        setPosition(this, 0, 0, 0, 0, Wnd.SWP_NOSIZE | Wnd.SWP_NOMOVE | Wnd.SWP_NOZORDER | Wnd.SWP_FRAMECHANGED);
    }

    /**
     * @return window style flags.
     */
    public long getWindowStyle()
    {
        return getWindowLong(Wnd.GWL_STYLE);
    }

    /**
     * Sets extended window style.
     *
     * @param flags
     */
    public void setWindowExStyle(long flags)
    {
        setWindowLong(Wnd.GWL_EXSTYLE, flags);
        setPosition(this, 0, 0, 0, 0, Wnd.SWP_NOSIZE | Wnd.SWP_NOMOVE | Wnd.SWP_NOZORDER | Wnd.SWP_FRAMECHANGED);
    }

    /**
     * @return extended window style flags.
     */
    public long getWindowExStyle()
    {
        return getWindowLong(Wnd.GWL_EXSTYLE);
    }

    /**
     * @return true, if the window is transparent; otherwise false.
     */
    public boolean isTransparent()
    {
        final long windowStyle = getWindowExStyle();
        return (windowStyle & Wnd.WS_EX_LAYERED) != 0;
    }

    /**
     * Sets or removes window transparancy.
     * <br>
     * <b>NOTE:</b> The <code>sun.java2d.noddraw=true</code> JVM option is required for transparency feature.
     *
     * @param transparent specifies whether or not to make window transparent
     */
    public void setTransparent(boolean transparent)
    {
        long oldWindowStyle = getWindowExStyle();
        long newWindowStyle;
        if (transparent)
        {
            newWindowStyle = oldWindowStyle | Wnd.WS_EX_LAYERED;
            setWindowExStyle(newWindowStyle);
            setLayeredWindowAttributes(new ColorRef(0), getTransparency(), Wnd.LWA_ALPHA);
        }
        else
        {
            newWindowStyle = oldWindowStyle & ~Wnd.WS_EX_LAYERED;
            setWindowExStyle(newWindowStyle);
        }
        redraw(null, null,
                Wnd.RDW_UPDATENOW | Wnd.RDW_NOERASE);
    }

    /**
     * Sets or remove window trancparency.
     * Added for compatibility with Windows 2000.
     *
     * @param transparency 0 makes the window completely opaque, 255 makes
     *                     it completely transparent.
     */
    public void setTransparent(byte transparency)
    {
        long oldWindowStyle = getWindowExStyle();
        long newWindowStyle;
        if (transparency != 0)
        {
            newWindowStyle = oldWindowStyle | Wnd.WS_EX_LAYERED;
            setWindowExStyle(newWindowStyle);
            setLayeredWindowAttributes(new ColorRef(0), transparency, Wnd.LWA_ALPHA);
        }
        else
        {
            newWindowStyle = oldWindowStyle & ~Wnd.WS_EX_LAYERED;
            setWindowExStyle(newWindowStyle);
        }
        redraw(null, null,
                Wnd.RDW_UPDATENOW | Wnd.RDW_NOERASE);
    }

    /**
     * @return a window transparency value.
     */
    public byte getTransparency()
    {
        Int8 alpha = new Int8();
        getLayeredWindowAttributes(null, alpha, null);
        return (byte)alpha.getValue();
    }

    /**
     * Sets window transparency.
     * <br>
     * <b>NOTE:</b> The <code>sun.java2d.noddraw=true</code> JVM option is required for transparency feature.
     *
     * @param transparency 255 makes the window completely transparent, 0 makes
     *                     it completely opaque.
     */
    public void setTransparency(byte transparency)
    {
        setLayeredWindowAttributes(null, transparency, Wnd.LWA_ALPHA);
    }

    /**
     * @return true, if the window has pallete style; otherwise false.
     */
    public boolean isPalleteWindow()
    {
        final long windowStyle = getWindowExStyle();
        return (windowStyle & Wnd.WS_EX_PALETTEWINDOW) != 0;
    }

    /**
     * Sets a pallete window style.
     *
     * @param palleteWindow
     */
    public void setPalleteWindow(boolean palleteWindow)
    {
        long oldWindowStyle = getWindowExStyle();
        long newWindowStyle;
        if (palleteWindow)
        {
            newWindowStyle = oldWindowStyle | Wnd.WS_EX_PALETTEWINDOW;
        }
        else
        {
            newWindowStyle = oldWindowStyle & ~Wnd.WS_EX_PALETTEWINDOW;
        }

        setWindowExStyle(newWindowStyle);
        redraw(null, null,
                Wnd.RDW_FRAME |
                        Wnd.RDW_INVALIDATE);
    }

    /**
     * Makes window topmost.
     *
     * @param topmost
     */
    public void setTopmost(boolean topmost)
    {
        int zOrder = Wnd.HWND_NOTOPMOST;
        if (topmost)
        {
            zOrder = Wnd.HWND_TOPMOST;
        }
        setPosition(new Wnd(zOrder), 0, 0, 0, 0, Wnd.SWP_NOSIZE | Wnd.SWP_NOMOVE);
    }

    /**
     * Makes the window rounded (with rounded corners).
     *
     * @param rounded
     * @param ellipseWidth
     * @param ellipseHeight
     */
    public void setRounded(boolean rounded, int ellipseWidth, int ellipseHeight)
    {
        Region region = null;
        if (rounded)
        {
            Rect windowRect = getWindowRect();
            // normalize rectangle coordinates
            windowRect.moveTo(0, 0);
            region = Region.createRoundRectRegion((int)windowRect.getLeft(),
                    (int)windowRect.getTop(),
                    (int)windowRect.getRight(),
                    (int)windowRect.getBottom(),
                    ellipseWidth,
                    ellipseHeight);
        }
        setRegion(region, true);
    }

    static final int DEFAULT_ELLIPSE_HEIGHT = 30;
    static final int DEFAULT_ELLIPSE_WIDTH = 30;

    /**
     * Makes the window rounded (with rounded corners).
     *
     * @param rounded
     */
    public void setRounded(boolean rounded)
    {
        setRounded(rounded, DEFAULT_ELLIPSE_WIDTH, DEFAULT_ELLIPSE_HEIGHT);
    }

    /**
     * Assigns a specified icon to the window.
     *
     * @param icon
     */
    public void setWindowIcon(Icon icon, Icon.IconType iconType)
    {
        sendMessageEx(Msg.WM_SETICON, iconType.getValue(), icon.getValue());
    }

    /**
     * Shows or hides the window.
     *
     * @param visible
     */
//    public void setVisible(boolean visible)
//    {
//        long windowStyle = getWindowStyle();
//        if (!visible)
//            windowStyle &= ~Wnd.WS_VISIBLE;
//        else
//            windowStyle |= Wnd.WS_VISIBLE;
//
//        setWindowStyle(windowStyle);
//    }

    /**
     * Shows or hides the window caption.
     *
     * @param value
     */
    public void setCaptionVisible(boolean value)
    {
        long oldWindowStyle = getWindowStyle();
        long newWindowStyle;
        if (value)
        {
            newWindowStyle = oldWindowStyle | Wnd.WS_CAPTION;
        }
        else
        {
            newWindowStyle = oldWindowStyle & ~Wnd.WS_CAPTION;
        }
        setWindowStyle(newWindowStyle);
        redraw(null, null,
                Wnd.RDW_ERASE |
                        Wnd.RDW_INTERNALPAINT);
    }

    /**
     * Checks if the window caption is visible.
     *
     * @return true, if the window caption is visible.
     */
    public boolean isCaptionVisible()
    {
        final long windowStyle = getWindowStyle();
        return (windowStyle & Wnd.WS_CAPTION) != 0;
    }

    /**
     * Shows or hides the window border.
     *
     * @param value
     */
    public void setBorderVisible(boolean value)
    {
        long oldWindowStyle = getWindowStyle();
        long newWindowStyle;
        if (value)
        {
            newWindowStyle = oldWindowStyle | Wnd.WS_BORDER;
        }
        else
        {
            newWindowStyle = oldWindowStyle & ~Wnd.WS_THICKFRAME;
        }
        setWindowStyle(newWindowStyle);
    }

    /**
     * Checks if the window border is visible.
     *
     * @return true, if the window border is visible.
     */
    public boolean isBorderVisible()
    {
        final long windowStyle = getWindowStyle();
        return (windowStyle & Wnd.WS_BORDER) != 0;
    }

    /**
     * Sets a custom window region. Returns a previously assigned region in
     * order to restore it later.
     *
     * @param region
     * @return region previously assigned to the window.
     */
    public Region setRegion(Region region)
    {
        Region prevRegion = getRegion();
        setRegion(region, true);
        return prevRegion;
    }

    /**
     * Forces continuous window repainting.
     *
     * @param value if true, continuous window repainting; if false - default.
     */
    public void setContinuousRepainting(boolean value)
    {
        final WindowMessageListener windowMessageListener = getWindowMessageListener();
        final WindowProc windowProc = getWindowProc();
        if (value)
        {
            windowProc.addMessageListener(windowMessageListener);
            windowProc.substitute();
        }
        else
        {
            windowProc.removeMessageListener(windowMessageListener);
            windowProc.restoreNative();
        }
    }

    private WindowProc getWindowProc()
    {
        if (_windowProc == null)
        {
            _windowProc = new WindowProc(this);
        }
        return _windowProc;
    }

    private WindowMessageListener getWindowMessageListener()
    {
        if (_windowMessageListener == null)
        {
            _windowMessageListener = new WindowMessageListener()
            {
                public boolean canHandle(WindowMessage message, boolean beforeWindowProc)
                {
                    return message.getMsg() == Msg.WM_SIZE && beforeWindowProc;
                }

                public int handle(WindowMessage message)
                {
                    sendMessageEx(Msg.WM_EXITSIZEMOVE, 0, 0);
                    return 0;
                }
            };
        }
        return _windowMessageListener;
    }

    /**
     * Sets the keyboard focus to the specified window. The window must be attached to
     * the calling thread's message queue.
     *
     * @return If the function succeeds, the return value is the handle to the window
     *         that previously had the keyboard focus.
     * @link User32#attachThreadInput(int, int, boolean)
     */
    public Wnd setFocus()
    {
        final Function function = User32.getInstance().getFunction(FUNCTION_SetFocus);
        Wnd previouslyFocusedWindow = new Wnd();
        function.invoke(previouslyFocusedWindow, this);
        return previouslyFocusedWindow;
    }

    /**
     * Retrieves window that has keyboard focus if the window is attached to the message
     * queue of calling thread.
     *
     * @return window that has keyboard focus.
     * @link User32#attachThreadInput(int, int, boolean)
     */
    public static Wnd getFocus()
    {
        final Function function = User32.getInstance().getFunction(FUNCTION_GetFocus);
        Wnd focusedWindow = new Wnd();
        function.invoke(focusedWindow);
        return focusedWindow;
    }

    /**
     * Returns the identifier of the thread that created this window.
     *
     * @return the identifier of the thread that created this window
     */
    public int getThreadId()
    {
        Function function = User32.getInstance().getFunction(FUNCTION_GET_WINDOW_THREAD_PROCESS_ID);
        UInt32 result = new UInt32();
        function.invoke(result, this, new Handle());
        return (int)result.getValue();
    }

    /**
     * Returns the process identifier.
     *
     * @return the process identifier
     */
    public int getProcessId()
    {
        Function function = User32.getInstance().getFunction(FUNCTION_GET_WINDOW_THREAD_PROCESS_ID);
        UInt32 result = new UInt32();
        UInt32 processID = new UInt32();
        function.invoke(result, this, new Pointer(processID));
        return (int)processID.getValue();
    }

    /**
     * Adds a rectangle to the specified window's update region.
     *
     * @param rect  the client coordinates of the rectangle to be added to the update region. If it is <code>null</code>, the entire client area is added to the update region.
     * @param erase Specifies whether the background within the update region is to be erased when the update region is processed
     */
    public void invalidateRect(Rect rect, boolean erase)
    {
        Function function = User32.getInstance().getFunction(FUNCTION_InvalidateRect);
        Bool result = new Bool();
        function.invoke(result, this, new Pointer(rect, rect == null), new Bool(erase));
    }

    /**
     * Registers a system-wide hot key, which activates the window, that corresponds to this handle.
     *
     * @param hotKey specifies the hot key.
     * @throws RuntimeException if the method is unsuccessful.
     */
    public void setHotKey(HotKey hotKey)
    {
        long wparam = (hotKey.getKeyCode() & 0xFF) | ((hotKey.getFlags() & 0xFF) * 0x100);
        long res = sendMessageEx(Msg.WM_SETHOTKEY, wparam, 0);
        if (res <= 0)
        {
            throw new RuntimeException("The window handle or hot key is invalid.");
        }
    }

    /**
     * Returns the currently set window hot key or <code>null</code> if hot key isn't set.
     *
     * @return the currently set window hot key or <code>null</code> if hot key isn't set.
     */
    public HotKey getHotKey()
    {
        long res = sendMessageEx(Msg.WM_GETHOTKEY, 0, 0);
        if (res == 0)
        {
            return null;
        }
        long lowWord = res & 0xFF;
        long hiWord = res >> 8;
        HotKey key = new HotKey((int) lowWord);
        key.setupFlag(hiWord, true);
        return key;
    }

    /**
     * The options class for determining a combination of hot key modifiers.
     */
    public static class HotKey extends FlagSet
    {
        private static final int MOD_ALT = 0x0001;
        private static final int MOD_CONTROL = 0x0002;
        private static final int MOD_SHIFT = 0x0004;

        private int _keyCode;

        public HotKey(int keyCode)
        {
            super();

            _keyCode = keyCode;
        }

        public void setAlt(boolean set)
        {
            setupFlag(MOD_ALT, set);
        }

        public boolean isAlt()
        {
            return contains(MOD_ALT);
        }

        public void setControl(boolean set)
        {
            setupFlag(MOD_CONTROL, set);
        }

        public boolean isControl()
        {
            return contains(MOD_CONTROL);
        }

        public void setShift(boolean set)
        {
            setupFlag(MOD_SHIFT, set);
        }

        public boolean isShift()
        {
            return contains(MOD_SHIFT);
        }

        public int getKeyCode()
        {
            return _keyCode;
        }
    }

    /**
     * Checks if this handle is real pointer to an existing window.
     *
     * @return true is this handle is real window; false otherwise
     */
    public boolean isWindow()
    {
        Function isWindowFunction = User32.getInstance().getFunction(FUNCTION_IsWindow);
        Bool result = new Bool();
        isWindowFunction.invoke(result, this);
        return result.getValue();
    }

    /**
     * Returns a handle to the foreground window (the window with which the user is currently working).
     *
     * @return a handle to the foreground window
     */
    public static Wnd getForegroundWindow() {
        Wnd wnd = new Wnd();
        Function getForegroundWindow = User32.getInstance().getFunction("GetForegroundWindow");
        getForegroundWindow.invoke(wnd);
        return wnd;
    }

    /**
     * Returns a handle to the window that contains the specified point.
     *
     * @param x specifies the x-coordinate of the point
     * @param y specifies the y-coordinate of the point
     * @return a handle to the window that contains the specified point
     */
    public static Wnd getWindowFromPoint(int x, int y) {
        Wnd wnd = new Wnd();
        Function getWindowFromPoint = User32.getInstance().getFunction("WindowFromPoint");
        getWindowFromPoint.invoke(wnd, new Point(x, y));
        return wnd;
    }

    /**
     * Checks if the window is maximized.
     *
     * @return true, if the window is maximized
     */
    public boolean isMaximized() {
        Function isWindowFunction = User32.getInstance().getFunction(FUNCTION_IsZoomed);
        Bool result = new Bool();
        isWindowFunction.invoke(result, this);
        return result.getValue();
    }
}

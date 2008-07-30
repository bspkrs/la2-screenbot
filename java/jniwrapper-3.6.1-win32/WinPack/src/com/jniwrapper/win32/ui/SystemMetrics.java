/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.ui;

import com.jniwrapper.Function;
import com.jniwrapper.Int;
import com.jniwrapper.win32.LastErrorException;

public class SystemMetrics
{
    /**
     * Flags specifying how the system arranges minimized windows. For more information about minimized windows, see
     * section "Remarks".
     */
    public static int SM_ARRANGE = 56;

    /**
     * Value that specifies how the system was started:
     * 0 Normal boot
     * 1 Fail-safe boot
     * 2 Fail-safe with network boot
     * <p/>
     * Fail-safe boot (also called SafeBoot) bypasses the user's startup files.
     */
    public static int SM_CLEANBOOT = 67;

    /**
     * Windows 98, Windows 2000: Number of display monitors on the desktop. See Remarks for more information.
     */
    public static int SM_CMONITORS = 80;

    /**
     * Number of buttons on mouse, or zero if no mouse is installed.
     */
    public static int SM_CMOUSEBUTTONS = 43;

    /**
     * Width and height, in pixels, of the window border. This is equivalent to the SM_CXEDGE value for windows with the 3-D look.
     */
    public static int SM_CXBORDER = 5;
    public static int SM_CYBORDER = 6;

    /**
     * Width and height, in pixels, of the cursor. The system cannot create cursors of other sizes.
     */
    public static int SM_CXCURSOR = 13;
    public static int SM_CYCURSOR = 14;

    /**
     * Same as SM_CXFIXEDFRAME and SM_CYFIXEDFRAME.
     */
    public static int SM_CXDLGFRAME = 7;
    public static int SM_CYDLGFRAME = 8;

    /**
     * Width and height, in pixels, of the rectangle around the location of a first click in a double-click sequence.
     * The second click must occur within this rectangle for the system to consider the two clicks as a double-click.
     * (The two clicks must also occur within a specified time.
     * Set the width and height of the double-click rectangle, call SystemParametersInfo with the SPI_SETDOUBLECLKHEIGHT
     * and SPI_SETDOUBLECLKWIDTH flags.
     */
    public static int SM_CXDOUBLECLK = 36;
    public static int SM_CYDOUBLECLK = 37;

    /**
     * Width and height, in pixels, of the rectangle centered on a drag point to allow limited movement of the mouse
     * pointer before a drag operation begins. This allows the user to click and release the mouse button easily without
     * unintentionally starting a drag operation.
     */
    public static int SM_CXDRAG = 68;
    public static int SM_CYDRAG = 69;

    /**
     * Dimensions, in pixels, of a 3-D border. These are the 3-D counterparts of SM_CXBORDER and SM_CYBORDER.
     */
    public static int SM_CXEDGE = 45;
    public static int SM_CYEDGE = 46;

    /**
     * Thickness, in pixels, of the frame around the perimeter of a window that has a caption but is not sizable.
     * SM_CXFIXEDFRAME is the height of the horizontal border and SM_CYFIXEDFRAME is the width of the vertical border.
     * <p/>
     * Same as SM_CXDLGFRAME and SM_CYDLGFRAME.
     */
    public static int SM_CXFIXEDFRAME = SM_CXDLGFRAME;
    public static int SM_CYFIXEDFRAME = SM_CYDLGFRAME;

    /**
     * Same as SM_CXSIZEFRAME and SM_CYSIZEFRAME.
     */
    public static int SM_CXFRAME = 32;
    public static int SM_CYFRAME = 33;

    /**
     * Width and height of the client area for a full-screen window on the primary display monitor. To get the
     * coordinates of the portion of the screen not obscured by the system taskbar or by application desktop toolbars,
     * call the SystemParametersInfo function with the SPI_GETWORKAREA value.
     */
    public static int SM_CXFULLSCREEN = 16;
    public static int SM_CYFULLSCREEN = 17;

    /**
     * Width, in pixels, of the arrow bitmap on a horizontal scroll bar; and height, in pixels, of a horizontal scroll bar.
     */
    public static int SM_CXHSCROLL = 21;
    public static int SM_CYHSCROLL = 3;

    /**
     * Width, in pixels, of the thumb box in a horizontal scroll bar.
     */
    public static int SM_CXHTHUMB = 10;

    /**
     * The default width and height, in pixels, of an icon. The LoadIcon function can load only icons of these dimensions.
     */
    public static int SM_CXICON = 11;
    public static int SM_CYICON = 12;

    /**
     * Dimensions, in pixels, of a grid cell for items in large icon view. Each item fits into a rectangle of this size
     * when arranged. These values are always greater than or equal to SM_CXICON and SM_CYICON.
     */
    public static int SM_CXICONSPACING = 38;
    public static int SM_CYICONSPACING = 39;

    /**
     * Default dimensions, in pixels, of a maximized top-level window on the primary display monitor.
     */
    public static int SM_CXMAXIMIZED = 61;
    public static int SM_CYMAXIMIZED = 62;

    /**
     * Default maximum dimensions, in pixels, of a window that has a caption and sizing borders. This metric refers
     * to the entire desktop. The user cannot drag the window frame to a size larger than these dimensions. A window
     * can override these values by processing the WM_GETMINMAXINFO message.
     */
    public static int SM_CXMAXTRACK = 59;
    public static int SM_CYMAXTRACK = 60;

    /**
     * Dimensions, in pixels, of the default menu check-mark bitmap.
     */
    public static int SM_CXMENUCHECK = 71;
    public static int SM_CYMENUCHECK = 72;

    /**
     * Dimensions, in pixels, of menu bar buttons, such as the child window close button used in the multiple document
     * interface.
     */
    public static int SM_CXMENUSIZE = 54;
    public static int SM_CYMENUSIZE = 55;

    /**
     * Minimum width and height, in pixels, of a window.
     */
    public static int SM_CXMIN = 28;
    public static int SM_CYMIN = 29;

    /**
     * Dimensions, in pixels, of a normal minimized window.
     */
    public static int SM_CXMINIMIZED = 57;
    public static int SM_CYMINIMIZED = 58;

    /**
     * Dimensions, in pixels, of a grid cell for minimized windows. Each minimized window fits into a rectangle this
     * size when arranged. These values are always greater than or equal to SM_CXMINIMIZED and SM_CYMINIMIZED.
     */
    public static int SM_CXMINSPACING = 47;
    public static int SM_CYMINSPACING = 48;

    /**
     * Minimum tracking width and height, in pixels, of a window. The user cannot drag the window frame to a size
     * smaller than these dimensions. A window can override these values by processing the WM_GETMINMAXINFO message.
     */
    public static int SM_CXMINTRACK = 34;
    public static int SM_CYMINTRACK = 35;

    /**
     * Width and height, in pixels, of the screen of the primary display monitor. These are the same values you obtain
     * by calling GetDeviceCaps(hdcPrimaryMonitor, HORZRES/VERTRES.
     */
    public static int SM_CXSCREEN = 0;
    public static int SM_CYSCREEN = 1;

    /**
     * Width and height, in pixels, of a button in a window's caption or title bar.
     */
    public static int SM_CXSIZE = 30;
    public static int SM_CYSIZE = 31;

    /**
     * Thickness, in pixels, of the sizing border around the perimeter of a window that can be resized.
     * SM_CXSIZEFRAME is the width of the horizontal border, and SM_CYSIZEFRAME is the height of the vertical border.
     * <p/>
     * Same as SM_CXFRAME and SM_CYFRAME.
     */
    public static int SM_CXSIZEFRAME = SM_CXFRAME;
    public static int SM_CYSIZEFRAME = SM_CYFRAME;

    /**
     * Recommended dimensions, in pixels, of a small icon. Small icons typically appear in window captions and in small
     * icon view.
     */
    public static int SM_CXSMICON = 49;
    public static int SM_CYSMICON = 50;

    /**
     * Dimensions, in pixels, of small caption buttons.
     */
    public static int SM_CXSMSIZE = 52;
    public static int SM_CYSMSIZE = 53;

    /**
     * Windows 98, Windows 2000: Width and height, in pixels, of the virtual screen. The virtual screen is the bounding
     * rectangle of all display monitors. The SM_XVIRTUALSCREEN, SM_YVIRTUALSCREEN metrics are the coordinates of
     * the top-left corner of the virtual screen.
     */
    public static int SM_CXVIRTUALSCREEN = 78;
    public static int SM_CYVIRTUALSCREEN = 79;

    /**
     * Width, in pixels, of a vertical scroll bar; and height, in pixels, of the arrow bitmap on a vertical scroll bar.
     */
    public static int SM_CXVSCROLL = 2;
    public static int SM_CYVSCROLL = 20;

    /**
     * Height, in pixels, of a normal caption area.
     */
    public static int SM_CYCAPTION = 4;

    /**
     * For double byte character set versions of the system, this is the height, in pixels, of the Kanji window at the
     * bottom of the screen.
     */
    public static int SM_CYKANJIWINDOW = 18;

    /**
     * Height, in pixels, of a single-line menu bar.
     */
    public static int SM_CYMENU = 15;

    /**
     * Height, in pixels, of a small caption.
     */
    public static int SM_CYSMCAPTION = 51;

    /**
     * Height, in pixels, of the thumb box in a vertical scroll bar.
     */
    public static int SM_CYVTHUMB = 9;

    /**
     * TRUE or nonzero, if the double-byte character set (DBCS version of User.exe is installed; FALSE or zero otherwise.
     */
    public static int SM_DBCSENABLED = 42;

    /**
     * TRUE or nonzero, if the debugging version of User.exe is installed; FALSE or zero otherwise.
     */
    public static int SM_DEBUG = 22;

    /**
     * TRUE or nonzero if drop-down menus are right-aligned with the corresponding menu bar item; FALSE or zero, if the
     * menus are left-aligned.
     */
    public static int SM_MENUDROPALIGNMENT = 40;

    /**
     * TRUE, if the system is enabled for Hebrew and Arabic languages.
     */
    public static int SM_MIDEASTENABLED = 74;

    /**
     * TRUE or nonzero, if a mouse is installed; FALSE or zero otherwise.
     */
    public static int SM_MOUSEPRESENT = 19;

    /**
     * Windows NT 4.0 and later, Windows 98: TRUE or nonzero, if a mouse with a wheel is installed; FALSE or zero
     * otherwise.
     */
    public static int SM_MOUSEWHEELPRESENT = 75;

    /**
     * The least significant bit is set if a network is present; otherwise, it is cleared. The other bits are reserved
     * for future use.
     */
    public static int SM_NETWORK = 63;

    /**
     * TRUE or nonzero, if the Microsoft Windows for Pen computing extensions are installed; FALSE or zero otherwise.
     */
    public static int SM_PENWINDOWS = 41;

    /**
     * TRUE, if security is present; FALSE otherwise.
     */
    public static int SM_SECURE = 44;

    /**
     * Windows 98, Windows 2000: TRUE, if all the display monitors have the same color format, FALSE otherwise. Note that
     * two displays can have the same bit depth, but different color formats. For example, the red, green, and blue
     * pixels can be encoded with different numbers of bits, or those bits can be located in different places in
     * a pixel's color value.
     */
    public static int SM_SAMEDISPLAYFORMAT = 81;

    /**
     * TRUE or nonzero, if the user requires an application to present information visually in situations where it would
     * otherwise present the information only in audible form; FALSE or zero otherwise.
     */
    public static int SM_SHOWSOUNDS = 70;

    /**
     * TRUE, if the computer has a low-end (slow processor; FALSE otherwise.
     */
    public static int SM_SLOWMACHINE = 73;

    /**
     * TRUE or nonzero, if the meanings of the left and right mouse buttons are swapped; FALSE or zero otherwise.
     */
    public static int SM_SWAPBUTTON = 23;

    /**
     * Windows 98, Windows 2000: Coordinates for the left side and the top of the virtual screen. The virtual screen is
     * the bounding rectangle of all display monitors. The SM_CXVIRTUALSCREEN, SM_CYVIRTUALSCREEN metrics are the width
     * and height of the virtual screen.
     */
    public static int SM_XVIRTUALSCREEN = 76;
    public static int SM_YVIRTUALSCREEN = 77;


    static final String FUNCTION_GET_SYSTEM_METRICS = "GetSystemMetrics";

    /**
     * Retrieves system metrics or system configuration settings.
     *
     * @param index is a matric or configuration setting to retrieve.
     * @return required system metric.
     */
    public static int getSystemMetrics(int index)
    {
        Int metric = new Int();
        final Function function = User32.getInstance().getFunction(FUNCTION_GET_SYSTEM_METRICS);
        long errorCode = function.invoke(metric, new Int(index));
        if (metric.getValue() == 0)
        {
            throw new LastErrorException(errorCode, "There is an error while retrieving metric");
        }
        return (int)metric.getValue();
    }
}

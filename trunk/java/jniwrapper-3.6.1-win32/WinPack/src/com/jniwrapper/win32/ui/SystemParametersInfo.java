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

/**
 * Use this class for return / update a system-wide parameter.
 * 
 * @see <a
 * href="http://msdn.microsoft.com/library/default.asp?url=/library/en-us/sysinfo/base/systemparametersinfo.asp">Platform
 * SDK: Windows System Information</a>
 */
public class SystemParametersInfo
{
    public static final int SPI_GETBEEP = 0x0001;
    public static final int SPI_SETBEEP = 0x0002;
    public static final int SPI_GETMOUSE = 0x0003;
    public static final int SPI_SETMOUSE = 0x0004;
    public static final int SPI_GETBORDER = 0x0005;
    public static final int SPI_SETBORDER = 0x0006;
    public static final int SPI_GETKEYBOARDSPEED = 0x000A;
    public static final int SPI_SETKEYBOARDSPEED = 0x000B;
    public static final int SPI_LANGDRIVER = 0x000C;
    public static final int SPI_ICONHORIZONTALSPACING = 0x000D;
    public static final int SPI_GETSCREENSAVETIMEOUT = 0x000E;
    public static final int SPI_SETSCREENSAVETIMEOUT = 0x000F;
    public static final int SPI_GETSCREENSAVEACTIVE = 0x0010;
    public static final int SPI_SETSCREENSAVEACTIVE = 0x0011;
    public static final int SPI_GETGRIDGRANULARITY = 0x0012;
    public static final int SPI_SETGRIDGRANULARITY = 0x0013;
    public static final int SPI_SETDESKWALLPAPER = 0x0014;
    public static final int SPI_SETDESKPATTERN = 0x0015;
    public static final int SPI_GETKEYBOARDDELAY = 0x0016;
    public static final int SPI_SETKEYBOARDDELAY = 0x0017;
    public static final int SPI_ICONVERTICALSPACING = 0x0018;
    public static final int SPI_GETICONTITLEWRAP = 0x0019;
    public static final int SPI_SETICONTITLEWRAP = 0x001A;
    public static final int SPI_GETMENUDROPALIGNMENT = 0x001B;
    public static final int SPI_SETMENUDROPALIGNMENT = 0x001C;
    public static final int SPI_SETDOUBLECLKWIDTH = 0x001D;
    public static final int SPI_SETDOUBLECLKHEIGHT = 0x001E;
    public static final int SPI_GETICONTITLELOGFONT = 0x001F;
    public static final int SPI_SETDOUBLECLICKTIME = 0x0020;
    public static final int SPI_SETMOUSEBUTTONSWAP = 0x0021;
    public static final int SPI_SETICONTITLELOGFONT = 0x0022;
    public static final int SPI_GETFASTTASKSWITCH = 0x0023;
    public static final int SPI_SETFASTTASKSWITCH = 0x0024;

    /**
     * winver &gt;= 0x0400
     */
    public static final int SPI_SETDRAGFULLWINDOWS = 0x0025;
    public static final int SPI_GETDRAGFULLWINDOWS = 0x0026;
    public static final int SPI_GETNONCLIENTMETRICS = 0x0029;
    public static final int SPI_SETNONCLIENTMETRICS = 0x002A;
    public static final int SPI_GETMINIMIZEDMETRICS = 0x002B;
    public static final int SPI_SETMINIMIZEDMETRICS = 0x002C;
    public static final int SPI_GETICONMETRICS = 0x002D;
    public static final int SPI_SETICONMETRICS = 0x002E;
    public static final int SPI_SETWORKAREA = 0x002F;
    public static final int SPI_GETWORKAREA = 0x0030;
    public static final int SPI_SETPENWINDOWS = 0x0031;
    public static final int SPI_GETHIGHCONTRAST = 0x0042;
    public static final int SPI_SETHIGHCONTRAST = 0x0043;
    public static final int SPI_GETKEYBOARDPREF = 0x0044;
    public static final int SPI_SETKEYBOARDPREF = 0x0045;
    public static final int SPI_GETSCREENREADER = 0x0046;
    public static final int SPI_SETSCREENREADER = 0x0047;
    public static final int SPI_GETANIMATION = 0x0048;
    public static final int SPI_SETANIMATION = 0x0049;
    public static final int SPI_GETFONTSMOOTHING = 0x004A;
    public static final int SPI_SETFONTSMOOTHING = 0x004B;
    public static final int SPI_SETDRAGWIDTH = 0x004C;
    public static final int SPI_SETDRAGHEIGHT = 0x004D;
    public static final int SPI_SETHANDHELD = 0x004E;
    public static final int SPI_GETLOWPOWERTIMEOUT = 0x004F;
    public static final int SPI_GETPOWEROFFTIMEOUT = 0x0050;
    public static final int SPI_SETLOWPOWERTIMEOUT = 0x0051;
    public static final int SPI_SETPOWEROFFTIMEOUT = 0x0052;
    public static final int SPI_GETLOWPOWERACTIVE = 0x0053;
    public static final int SPI_GETPOWEROFFACTIVE = 0x0054;
    public static final int SPI_SETLOWPOWERACTIVE = 0x0055;
    public static final int SPI_SETPOWEROFFACTIVE = 0x0056;
    public static final int SPI_SETCURSORS = 0x0057;
    public static final int SPI_SETICONS = 0x0058;
    public static final int SPI_GETDEFAULTINPUTLANG = 0x0059;
    public static final int SPI_SETDEFAULTINPUTLANG = 0x005A;
    public static final int SPI_SETLANGTOGGLE = 0x005B;
    public static final int SPI_GETWINDOWSEXTENSION = 0x005C;
    public static final int SPI_SETMOUSETRAILS = 0x005D;
    public static final int SPI_GETMOUSETRAILS = 0x005E;
    public static final int SPI_SETSCREENSAVERRUNNING = 0x0061;
    public static final int SPI_SCREENSAVERRUNNING = SPI_SETSCREENSAVERRUNNING;

    public static final int SPI_GETFILTERKEYS = 0x0032;
    public static final int SPI_SETFILTERKEYS = 0x0033;
    public static final int SPI_GETTOGGLEKEYS = 0x0034;
    public static final int SPI_SETTOGGLEKEYS = 0x0035;
    public static final int SPI_GETMOUSEKEYS = 0x0036;
    public static final int SPI_SETMOUSEKEYS = 0x0037;
    public static final int SPI_GETSHOWSOUNDS = 0x0038;
    public static final int SPI_SETSHOWSOUNDS = 0x0039;
    public static final int SPI_GETSTICKYKEYS = 0x003A;
    public static final int SPI_SETSTICKYKEYS = 0x003B;
    public static final int SPI_GETACCESSTIMEOUT = 0x003C;
    public static final int SPI_SETACCESSTIMEOUT = 0x003D;

    /**
     * winver &gt;= 0x0400
     */
    public static final int SPI_GETSERIALKEYS = 0x003E;
    public static final int SPI_SETSERIALKEYS = 0x003F;

    public static final int SPI_GETSOUNDSENTRY = 0x0040;
    public static final int SPI_SETSOUNDSENTRY = 0x0041;
    /**
     * _WIN32_WINNT &gt;= 0x0400
     */
    public static final int SPI_GETSNAPTODEFBUTTON = 0x005F;
    public static final int SPI_SETSNAPTODEFBUTTON = 0x0060;

    /**
     * (_WIN32_WINNT &gt;= 0x0400) || (_WIN32_WINDOWS &gt;= 0x0400)
     */
    public static final int SPI_GETMOUSEHOVERWIDTH = 0x0062;
    public static final int SPI_SETMOUSEHOVERWIDTH = 0x0063;
    public static final int SPI_GETMOUSEHOVERHEIGHT = 0x0064;
    public static final int SPI_SETMOUSEHOVERHEIGHT = 0x0065;
    public static final int SPI_GETMOUSEHOVERTIME = 0x0066;
    public static final int SPI_SETMOUSEHOVERTIME = 0x0067;
    public static final int SPI_GETWHEELSCROLLLINES = 0x0068;
    public static final int SPI_SETWHEELSCROLLLINES = 0x0069;
    public static final int SPI_GETMENUSHOWDELAY = 0x006A;
    public static final int SPI_SETMENUSHOWDELAY = 0x006B;
    public static final int SPI_GETSHOWIMEUI = 0x006E;
    public static final int SPI_SETSHOWIMEUI = 0x006F;

    /**
     * WINVER &gt;= 0x0500
     */
    public static final int SPI_GETMOUSESPEED = 0x0070;
    public static final int SPI_SETMOUSESPEED = 0x0071;
    public static final int SPI_GETSCREENSAVERRUNNING = 0x0072;
    public static final int SPI_GETDESKWALLPAPER = 0x0073;

    /**
     * WINVER &gt;= 0x0500
     */
    public static final int SPI_GETACTIVEWINDOWTRACKING = 0x1000;
    public static final int SPI_SETACTIVEWINDOWTRACKING = 0x1001;
    public static final int SPI_GETMENUANIMATION = 0x1002;
    public static final int SPI_SETMENUANIMATION = 0x1003;
    public static final int SPI_GETCOMBOBOXANIMATION = 0x1004;
    public static final int SPI_SETCOMBOBOXANIMATION = 0x1005;
    public static final int SPI_GETLISTBOXSMOOTHSCROLLING = 0x1006;
    public static final int SPI_SETLISTBOXSMOOTHSCROLLING = 0x1007;
    public static final int SPI_GETGRADIENTCAPTIONS = 0x1008;
    public static final int SPI_SETGRADIENTCAPTIONS = 0x1009;
    public static final int SPI_GETKEYBOARDCUES = 0x100A;
    public static final int SPI_SETKEYBOARDCUES = 0x100B;
    public static final int SPI_GETMENUUNDERLINES = SPI_GETKEYBOARDCUES;
    public static final int SPI_SETMENUUNDERLINES = SPI_SETKEYBOARDCUES;
    public static final int SPI_GETACTIVEWNDTRKZORDER = 0x100C;
    public static final int SPI_SETACTIVEWNDTRKZORDER = 0x100D;
    public static final int SPI_GETHOTTRACKING = 0x100E;
    public static final int SPI_SETHOTTRACKING = 0x100F;
    public static final int SPI_GETMENUFADE = 0x1012;
    public static final int SPI_SETMENUFADE = 0x1013;
    public static final int SPI_GETSELECTIONFADE = 0x1014;
    public static final int SPI_SETSELECTIONFADE = 0x1015;
    public static final int SPI_GETTOOLTIPANIMATION = 0x1016;
    public static final int SPI_SETTOOLTIPANIMATION = 0x1017;
    public static final int SPI_GETTOOLTIPFADE = 0x1018;
    public static final int SPI_SETTOOLTIPFADE = 0x1019;
    public static final int SPI_GETCURSORSHADOW = 0x101A;
    public static final int SPI_SETCURSORSHADOW = 0x101B;

    /**
     * WINVER &gt;= 0x0501
     */
    public static final int SPI_GETMOUSESONAR = 0x101C;
    public static final int SPI_SETMOUSESONAR = 0x101D;
    public static final int SPI_GETMOUSECLICKLOCK = 0x101E;
    public static final int SPI_SETMOUSECLICKLOCK = 0x101F;
    public static final int SPI_GETMOUSEVANISH = 0x1020;
    public static final int SPI_SETMOUSEVANISH = 0x1021;
    public static final int SPI_GETFLATMENU = 0x1022;
    public static final int SPI_SETFLATMENU = 0x1023;
    public static final int SPI_GETDROPSHADOW = 0x1024;
    public static final int SPI_SETDROPSHADOW = 0x1025;
    public static final int SPI_GETBLOCKSENDINPUTRESETS = 0x1026;
    public static final int SPI_SETBLOCKSENDINPUTRESETS = 0x1027;

    /**
     * WINVER &gt;= 0x0501
     */
    public static final int SPI_GETUIEFFECTS = 0x103E;
    public static final int SPI_SETUIEFFECTS = 0x103F;

    public static final int SPI_GETFOREGROUNDLOCKTIMEOUT = 0x2000;
    public static final int SPI_SETFOREGROUNDLOCKTIMEOUT = 0x2001;
    public static final int SPI_GETACTIVEWNDTRKTIMEOUT = 0x2002;
    public static final int SPI_SETACTIVEWNDTRKTIMEOUT = 0x2003;
    public static final int SPI_GETFOREGROUNDFLASHCOUNT = 0x2004;
    public static final int SPI_SETFOREGROUNDFLASHCOUNT = 0x2005;
    public static final int SPI_GETCARETWIDTH = 0x2006;
    public static final int SPI_SETCARETWIDTH = 0x2007;

    /**
     * WINVER &gt;= 0x0501
     */
    public static final int SPI_GETMOUSECLICKLOCKTIME = 0x2008;
    public static final int SPI_SETMOUSECLICKLOCKTIME = 0x2009;
    public static final int SPI_GETFONTSMOOTHINGTYPE = 0x200A;
    public static final int SPI_SETFONTSMOOTHINGTYPE = 0x200B;

    /**
     * Does not update user profile.
     */
    public static final int SPIF_DONT_UPDATE_PROFILE = 0;

    /**
     * Saves a new value of the system parameter to the user profile.
     */
    public static final int SPIF_UPDATEINIFILE = 0x0001;

    /**
     * Saves a new value of the system parameter to the user profile, and then
     * sends broadcast WM_SETTINGCHANGE message.
     */
    public static final int SPIF_SENDCHANGE = 0x0002;

    /**
     * Same as SPIF_SENDCHANGE.
     */
    public static final int SPIF_SENDWININICHANGE = SPIF_SENDCHANGE;

    private static final FunctionName FUNCTION_SYSTEM_PARAMETERS_INFO = new FunctionName("SystemParametersInfo");

    /**
     * Returns or sets a system-wide parameter.
     * 
     * @param type SPI_ constant that describes returned / updated information.
     * @param value value for setting.
     * @param lpReturnValue pointer to a returned value of the system parameter.
     * @param fWinIni one of the SPIF_ constants. If the system parameter is
     * set, specifies type of updating user profile.
     * @return true, if the function succeded, otherwise false.
     */
    public static boolean systemParametersInfo(UInt type, UInt value, Pointer lpReturnValue, UInt fWinIni)
    {
        Bool result = new Bool();
        final Function function = User32.getInstance().getFunction(FUNCTION_SYSTEM_PARAMETERS_INFO.toString());
        function.invoke(result, new Parameter[]{type, value, lpReturnValue, fWinIni});
        return result.getValue();
    }
}

/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32;

import com.jniwrapper.Parameter;
import com.jniwrapper.Structure;
import com.jniwrapper.UInt;
import com.jniwrapper.UInt32;
import com.jniwrapper.win32.ui.Wnd;

/**
 * This class represents <code>MSG</code> native structure. It also provides
 * constants for Window Messages.
 * 
 * @author Serge Piletsky
 */
public class Msg extends Structure
{
    public static final int WM_NULL                         = 0x0000;
    public static final int WM_CREATE                       = 0x0001;
    public static final int WM_DESTROY                      = 0x0002;
    public static final int WM_MOVE                         = 0x0003;
    public static final int WM_SIZE                         = 0x0005;
    public static final int WM_ACTIVATE                     = 0x0006;
    public static final int WM_SETFOCUS                     = 0x0007;
    public static final int WM_KILLFOCUS                    = 0x0008;
    public static final int WM_ENABLE                       = 0x000A;
    public static final int WM_SETREDRAW                    = 0x000B;
    public static final int WM_SETTEXT                      = 0x000C;
    public static final int WM_GETTEXT                      = 0x000D;
    public static final int WM_GETTEXTLENGTH                = 0x000E;
    public static final int WM_PAINT                        = 0x000F;
    public static final int WM_CLOSE                        = 0x0010;
    public static final int WM_QUERYENDSESSION              = 0x0011;
    public static final int WM_QUIT                         = 0x0012;
    public static final int WM_QUERYOPEN                    = 0x0013;
    public static final int WM_ERASEBKGND                   = 0x0014;
    public static final int WM_SYSCOLORCHANGE               = 0x0015;
    public static final int WM_ENDSESSION                   = 0x0016;
    public static final int WM_SHOWWINDOW                   = 0x0018;
    public static final int WM_WININICHANGE                 = 0x001A;
    public static final int WM_SETTINGCHANGE                = WM_WININICHANGE;
    public static final int WM_DEVMODECHANGE                = 0x001B;
    public static final int WM_ACTIVATEAPP                  = 0x001C;
    public static final int WM_FONTCHANGE                   = 0x001D;
    public static final int WM_TIMECHANGE                   = 0x001E;
    public static final int WM_CANCELMODE                   = 0x001F;
    public static final int WM_SETCURSOR                    = 0x0020;
    public static final int WM_MOUSEACTIVATE                = 0x0021;
    public static final int WM_CHILDACTIVATE                = 0x0022;
    public static final int WM_QUEUESYNC                    = 0x0023;
    public static final int WM_MOUSEMOVE                    = 0x0200;
    public static final int WM_LBUTTONDOWN                  = 0x0201;
    public static final int WM_LBUTTONUP                    = 0x0202;
    public static final int WM_LBUTTONDBLCLK                = 0x0203;
    public static final int WM_RBUTTONDOWN                  = 0x0204;
    public static final int WM_RBUTTONUP                    = 0x0205;
    public static final int WM_RBUTTONDBLCLK                = 0x0206;
    public static final int WM_MBUTTONDOWN                  = 0x0207;
    public static final int WM_MBUTTONUP                    = 0x0208;
    public static final int WM_MBUTTONDBLCLK                = 0x0209;
    public static final int WM_USER                         = 0x0400;
    public static final int WM_GETICON                      = 0x007F;
    public static final int WM_SETICON                      = 0x0080;
    public static final int WM_INITDIALOG                   = 0x0110;
    public static final int WM_NOTIFY                       = 0x004E;
    public static final int WM_MOUSEHOVER                   = 0x02A1;
    public static final int WM_MOUSELEAVE                   = 0x02A3;
    public static final int WM_GETMINMAXINFO                = 0x0024;
    public static final int WM_PAINTICON                    = 0x0026;
    public static final int WM_ICONERASEBKGND               = 0x0027;
    public static final int WM_NEXTDLGCTL                   = 0x0028;
    public static final int WM_SPOOLERSTATUS                = 0x002A;
    public static final int WM_DRAWITEM                     = 0x002B;
    public static final int WM_MEASUREITEM                  = 0x002C;
    public static final int WM_DELETEITEM                   = 0x002D;
    public static final int WM_VKEYTOITEM                   = 0x002E;
    public static final int WM_CHARTOITEM                   = 0x002F;
    public static final int WM_SETFONT                      = 0x0030;
    public static final int WM_GETFONT                      = 0x0031;
    public static final int WM_SETHOTKEY                    = 0x0032;
    public static final int WM_GETHOTKEY                    = 0x0033;
    public static final int WM_QUERYDRAGICON                = 0x0037;
    public static final int WM_COMPAREITEM                  = 0x0039;
    public static final int WM_GETOBJECT                    = 0x003D;
    public static final int WM_COMPACTING                   = 0x0041;
    public static final int WM_COMMNOTIFY                   = 0x0044;
    public static final int WM_WINDOWPOSCHANGING            = 0x0046;
    public static final int WM_WINDOWPOSCHANGED             = 0x0047;
    public static final int WM_POWER                        = 0x0048;
    public static final int WM_COPYDATA                     = 0x004A;
    public static final int WM_CANCELJOURNAL                = 0x004B;
    public static final int WM_INPUTLANGCHANGEREQUEST       = 0x0050;
    public static final int WM_INPUTLANGCHANGE              = 0x0051;
    public static final int WM_TCARD                        = 0x0052;
    public static final int WM_HELP                         = 0x0053;
    public static final int WM_USERCHANGED                  = 0x0054;
    public static final int WM_NOTIFYFORMAT                 = 0x0055;
    public static final int WM_CONTEXTMENU                  = 0x007B;
    public static final int WM_STYLECHANGING                = 0x007C;
    public static final int WM_STYLECHANGED                 = 0x007D;
    public static final int WM_DISPLAYCHANGE                = 0x007E;
    public static final int WM_NCCREATE                     = 0x0081;
    public static final int WM_NCDESTROY                    = 0x0082;
    public static final int WM_NCCALCSIZE                   = 0x0083;
    public static final int WM_NCHITTEST                    = 0x0084;
    public static final int WM_NCPAINT                      = 0x0085;
    public static final int WM_NCACTIVATE                   = 0x0086;
    public static final int WM_GETDLGCODE                   = 0x0087;
    public static final int WM_SYNCPAINT                    = 0x0088;
    public static final int WM_NCMOUSEMOVE                  = 0x00A0;
    public static final int WM_NCLBUTTONDOWN                = 0x00A1;
    public static final int WM_NCLBUTTONUP                  = 0x00A2;
    public static final int WM_NCLBUTTONDBLCLK              = 0x00A3;
    public static final int WM_NCRBUTTONDOWN                = 0x00A4;
    public static final int WM_NCRBUTTONUP                  = 0x00A5;
    public static final int WM_NCRBUTTONDBLCLK              = 0x00A6;
    public static final int WM_NCMBUTTONDOWN                = 0x00A7;
    public static final int WM_NCMBUTTONUP                  = 0x00A8;
    public static final int WM_NCMBUTTONDBLCLK              = 0x00A9;
    public static final int WM_KEYFIRST                     = 0x0100;
    public static final int WM_KEYDOWN                      = 0x0100;
    public static final int WM_KEYUP                        = 0x0101;
    public static final int WM_CHAR                         = 0x0102;
    public static final int WM_DEADCHAR                     = 0x0103;
    public static final int WM_SYSKEYDOWN                   = 0x0104;
    public static final int WM_SYSKEYUP                     = 0x0105;
    public static final int WM_SYSCHAR                      = 0x0106;
    public static final int WM_SYSDEADCHAR                  = 0x0107;
    public static final int WM_KEYLAST                      = 0x0108;
    public static final int WM_IME_STARTCOMPOSITION         = 0x010D;
    public static final int WM_IME_ENDCOMPOSITION           = 0x010E;
    public static final int WM_IME_COMPOSITION              = 0x010F;
    public static final int WM_IME_KEYLAST                  = 0x010F;
    public static final int WM_COMMAND                      = 0x0111;
    public static final int WM_SYSCOMMAND                   = 0x0112;
    public static final int WM_TIMER                        = 0x0113;
    public static final int WM_HSCROLL                      = 0x0114;
    public static final int WM_VSCROLL                      = 0x0115;
    public static final int WM_INITMENU                     = 0x0116;
    public static final int WM_INITMENUPOPUP                = 0x0117;
    public static final int WM_MENUSELECT                   = 0x011F;
    public static final int WM_MENUCHAR                     = 0x0120;
    public static final int WM_ENTERIDLE                    = 0x0121;
    public static final int WM_MENURBUTTONUP                = 0x0122;
    public static final int WM_MENUDRAG                     = 0x0123;
    public static final int WM_MENUGETOBJECT                = 0x0124;
    public static final int WM_UNINITMENUPOPUP              = 0x0125;
    public static final int WM_MENUCOMMAND                  = 0x0126;
    public static final int WM_CTLCOLORMSGBOX               = 0x0132;
    public static final int WM_CTLCOLOREDIT                 = 0x0133;
    public static final int WM_CTLCOLORLISTBOX              = 0x0134;
    public static final int WM_CTLCOLORBTN                  = 0x0135;
    public static final int WM_CTLCOLORDLG                  = 0x0136;
    public static final int WM_CTLCOLORSCROLLBAR            = 0x0137;
    public static final int WM_CTLCOLORSTATIC               = 0x0138;
    public static final int WM_MOUSEFIRST                   = 0x0200;
    public static final int WM_MOUSEWHEEL                   = 0x020A;
    public static final int WM_PARENTNOTIFY                 = 0x0210;
    public static final int WM_ENTERMENULOOP                = 0x0211;
    public static final int WM_EXITMENULOOP                 = 0x0212;
    public static final int WM_NEXTMENU                     = 0x0213;
    public static final int WM_SIZING                       = 0x0214;
    public static final int WM_CAPTURECHANGED               = 0x0215;
    public static final int WM_MOVING                       = 0x0216;
    public static final int WM_POWERBROADCAST               = 0x0218;
    public static final int WM_DEVICECHANGE                 = 0x0219;
    public static final int WM_MDICREATE                    = 0x0220;
    public static final int WM_MDIDESTROY                   = 0x0221;
    public static final int WM_MDIACTIVATE                  = 0x0222;
    public static final int WM_MDIRESTORE                   = 0x0223;
    public static final int WM_MDINEXT                      = 0x0224;
    public static final int WM_MDIMAXIMIZE                  = 0x0225;
    public static final int WM_MDITILE                      = 0x0226;
    public static final int WM_MDICASCADE                   = 0x0227;
    public static final int WM_MDIICONARRANGE               = 0x0228;
    public static final int WM_MDIGETACTIVE                 = 0x0229;
    public static final int WM_MDISETMENU                   = 0x0230;
    public static final int WM_ENTERSIZEMOVE                = 0x0231;
    public static final int WM_EXITSIZEMOVE                 = 0x0232;
    public static final int WM_DROPFILES                    = 0x0233;
    public static final int WM_MDIREFRESHMENU               = 0x0234;
    public static final int WM_IME_SETCONTEXT               = 0x0281;
    public static final int WM_IME_NOTIFY                   = 0x0282;
    public static final int WM_IME_CONTROL                  = 0x0283;
    public static final int WM_IME_COMPOSITIONFULL          = 0x0284;
    public static final int WM_IME_SELECT                   = 0x0285;
    public static final int WM_IME_CHAR                     = 0x0286;
    public static final int WM_IME_REQUEST                  = 0x0288;
    public static final int WM_IME_KEYDOWN                  = 0x0290;
    public static final int WM_IME_KEYUP                    = 0x0291;
    public static final int WM_CUT                          = 0x0300;
    public static final int WM_COPY                         = 0x0301;
    public static final int WM_PASTE                        = 0x0302;
    public static final int WM_CLEAR                        = 0x0303;
    public static final int WM_UNDO                         = 0x0304;
    public static final int WM_RENDERFORMAT                 = 0x0305;
    public static final int WM_RENDERALLFORMATS             = 0x0306;
    public static final int WM_DESTROYCLIPBOARD             = 0x0307;
    public static final int WM_DRAWCLIPBOARD                = 0x0308;
    public static final int WM_PAINTCLIPBOARD               = 0x0309;
    public static final int WM_VSCROLLCLIPBOARD             = 0x030A;
    public static final int WM_SIZECLIPBOARD                = 0x030B;
    public static final int WM_ASKCBFORMATNAME              = 0x030C;
    public static final int WM_CHANGECBCHAIN                = 0x030D;
    public static final int WM_HSCROLLCLIPBOARD             = 0x030E;
    public static final int WM_QUERYNEWPALETTE              = 0x030F;
    public static final int WM_PALETTEISCHANGING            = 0x0310;
    public static final int WM_PALETTECHANGED               = 0x0311;
    public static final int WM_HOTKEY                       = 0x0312;
    public static final int WM_PRINT                        = 0x0317;
    public static final int WM_PRINTCLIENT                  = 0x0318;
    public static final int WM_HANDHELDFIRST                = 0x0358;
    public static final int WM_HANDHELDLAST                 = 0x035F;
    public static final int WM_AFXFIRST                     = 0x0360;
    public static final int WM_AFXLAST                      = 0x037F;
    public static final int WM_PENWINFIRST                  = 0x0380;
    public static final int WM_PENWINLAST                   = 0x038F;
    public static final int WM_APP                          = 0x8000;

    /*
     * WM_ACTIVATE state values
     */
    public static final int WA_INACTIVE = 0;
    public static final int WA_ACTIVE = 1;
    public static final int WA_CLICKACTIVE = 2;

    /*
     * Message fields
     */
    private Wnd _wnd;
    private UInt _message;
    private IntPtr _wParam;
    private IntPtr _lParam;
    private UInt32 _time;
    private Point _point;

    public Msg()
    {
        _wnd = new Wnd();
        _message = new UInt();
        _wParam = new IntPtr();
        _lParam = new IntPtr();
        _time = new UInt32();
        _point = new Point();

        init(new Parameter[]{_wnd, _message, _wParam, _lParam, _time, _point}, (short)8);
    }

    public Msg(Msg that)
    {
        this();
        initFrom(that);
    }

    public Wnd getWnd()
    {
        return _wnd;
    }

    public UInt getMessage()
    {
        return _message;
    }

    public IntPtr getWParam()
    {
        return _wParam;
    }

    public /*UInt32*/ IntPtr getLParam()
    {
        return _lParam;
    }

    public UInt32 getTime()
    {
        return _time;
    }

    public Point getPoint()
    {
        return _point;
    }

    public Object clone()
    {
        return new Msg(this);
    }
}

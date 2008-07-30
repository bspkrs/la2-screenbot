/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.shell;

import com.jniwrapper.*;
import com.jniwrapper.win32.gdi.Icon;
import com.jniwrapper.win32.system.VersionInfo;

/**
 * This class represents NOTIFYICONDATA structure.
 * <p/>
 * Note: In the previous version of WinPack, there were two implementations of this structure (NotifyData and NotifyDataExt)
 * for different API versions; now they are merged into one structure that represents this class.
 *
 * @author AKireyev
 * @author Serge Piletsky
 */
class NotifyIconData extends Structure
{
    private UInt32 _cbSize = new UInt32();
    private Pointer.Void _hWnd = new Pointer.Void();
    private UInt _uID = new UInt();
    private UInt _uFlags = new UInt();
    private UInt _uCallbackMessage = new UInt();

    private Icon _hIcon = new Icon();
    private Str _szTip;
    private UInt32 _dwState = new UInt32();
    private UInt32 _dwStateMask = new UInt32();
    private Str _szInfo;
    private UInt _uTimeout = new UInt();
    private UInt _uVersion = new UInt();
    private Union _union = new Union(new Parameter[]{_uTimeout, _uVersion});
    private Str _szInfoTitle;
    private UInt32 _dwInfoFlags = new UInt32();

    public NotifyIconData()
    {
        initStringParameters();
        VersionInfo versionInfo = new VersionInfo();
        if (versionInfo.isWin2k() | versionInfo.isWinMe())
        {
            init(new Parameter[]{_cbSize, _hWnd, _uID, _uFlags, _uCallbackMessage, _hIcon, _szTip,
                    _dwState, _dwStateMask, _szInfo, _union, _szInfoTitle, _dwInfoFlags}, (short)8);
        }
        else
        {
            init(new Parameter[]{_cbSize, _hWnd, _uID, _uFlags, _uCallbackMessage, _hIcon, _szTip}, (short)8);
        }
        _cbSize.setValue(getLength());
    }

    public NotifyIconData(long hwnd, int id)
    {
        this();
        _hWnd.setValue(hwnd);
        _uID.setValue(id);
    }

    public NotifyIconData(NotifyIconData that)
    {
        this();
        initFrom(that);
    }

    private void initStringParameters()
    {
        _szTip = new Str("", 128);
        _szInfo = new Str("", 256);
        _szInfoTitle = new Str("", 64);
    }

    public void setCallbackMessage(int callbackMessage)
    {
        _uCallbackMessage.setValue(callbackMessage);
    }

    public void setFlags(long flags)
    {
        _uFlags.setValue(flags);
    }

    public void setIcon(Icon icon)
    {
        _hIcon.setValue(icon.getValue());
    }

    public void setToolTip(String tip)
    {
        _szTip.setValue(tip);
    }

    public void setState(long value)
    {
        _dwState.setValue(value);
    }

    public void setStateMask(long value)
    {
        _dwStateMask.setValue(value);
    }

    public void setInfo(String value)
    {
        _szInfo.setValue(value);
    }

    public void setTimeout(long value)
    {
        _uTimeout.setValue(value);
    }

    public void setInfoTitle(String value)
    {
        _szInfoTitle.setValue(value);
    }

    public void setInfoFlags(long value)
    {
        _dwInfoFlags.setValue(value);
    }

    public Object clone()
    {
        return new NotifyIconData(this);
    }
}

/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.shell;

/**
 * Defines interface for tray icon listener classes.
 * 
 * @author Alexander Evsukov
 */
public interface TrayIconListener
{
    /**
     * 
     * @param message is a window message. Can be one of the following:
     * WM_MOUSEMOVE, WM_LBUTTONDOWN, WM_LBUTTONUP, WM_LBUTTONDBLCLK,
     * WM_RBUTTONDOWN, WM_RBUTTONUP, WM_RBUTTONDBLCLK, WM_MBUTTONDOWN,
     * WM_MBUTTONUP, WM_MBUTTONDBLCLK, WM_MOUSELEAVE, WM_MOUSEHOVER
     * @param x the x-coordinate of the cursor position when an event occurred.
     * @param y the y-coordinate of the cursor position when an event occurred.
     */
    public void trayActionPerformed(long message, int x, int y);
}

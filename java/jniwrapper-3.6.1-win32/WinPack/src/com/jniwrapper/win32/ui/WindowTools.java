/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.ui;

import com.jniwrapper.Pointer;
import com.jniwrapper.jawt.JAWT;
import com.jniwrapper.jawt.JAWT_DrawingSurface;
import com.jniwrapper.jawt.JAWT_DrawingSurfaceInfo;
import com.jniwrapper.util.*;
import sun.awt.windows.WToolkit;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * This class contains utility functions for accessing native windowing system
 * data. Functions use JAWT or internal implementation (sun.*) classes as
 * appropriate.
 *
 * @author Alexander Kireyev
 */
public class WindowTools
{
    private static final Logger LOG = Logger.getInstance(WindowTools.class);

    private WindowTools() {}
    
    /**
     * Returns a native window handle of the given component. If the passed
     * component doesn't have a native window, attempts to locate the nearest
     * parent that does.
     *
     * @param component the component to get the native window handle for.
     * @return handle of the native window for the given component tree, or zero
     * if not found.
     */
    public static long getWindowHandle(final Component component, boolean runInCurrentThread)
    {
        final long[] result = new long[1];
        Runnable runnable = new Runnable()
        {
            public void run()
            {
                if (JAWT.JDK_1_4)
                {
                    // Locate parent if we don't have native window
                    Component heavyComp = component;
                    while (heavyComp instanceof JComponent)
                    {
                        heavyComp = heavyComp.getParent();
                        if (heavyComp == null)
                        {
                            result[0] = 0;
                            return;
                        }
                    }

                    JAWT_DrawingSurface ds = JAWT.getDrawingSurface(heavyComp);
                    if ((ds.lock() & 1) != 0)
                    {
                        // Lock error - return null handle
                        result[0] = 0;
                    }
                    else
                    {
                        Win32DSI win32DSI = new Win32DSI();
                        JAWT_DrawingSurfaceInfo dsi = new JAWT_DrawingSurfaceInfo(win32DSI);
                        Pointer pDsi = new Pointer(dsi);
                        ds.getDrawingSurfaceInfo(pDsi);
                        result[0] = (int)win32DSI.getHandle().getValue();
                        ds.freeDrawingSurfaceInfo(pDsi);
                        ds.unlock();
                        JAWT.freeDrawingSurface(ds);
                    }
                }
                else
                {
                    try
                    {
                        Method method = WToolkit.class.getMethod("getNativeWindowHandleFromComponent", new Class[]{Component.class});
                        result[0] = ((Integer)method.invoke(WToolkit.getWToolkit(), new Object[]{component})).intValue();
                    }
                    catch (Exception e)
                    {
                        throw new RuntimeException(e.toString());
                    }
                }
            }
        };
        runInDispatchThread(runnable, runInCurrentThread);
        return result[0];
    }

    /**
     * Returns a native window handle of the given component. If the passed
     * component doesn't have a native window, attempts to locate the nearest
     * parent that does.
     *
     * @param component the component to get the native window handle for.
     * @return handle of the native window for the given component tree, or zero
     * if not found.
     */
    public static long getWindowHandle(final Component component)
    {
        return getWindowHandle(component, false);
    }

    private static void runInDispatchThread(Runnable runnable, boolean runInCurrentThread)
    {
        if (runInCurrentThread)
        {
            runnable.run();
        }
        else if (SwingUtilities.isEventDispatchThread()) {
            runnable.run();
        } else {
            try
            {
                SwingUtilities.invokeAndWait(runnable);
            }
            catch (InterruptedException e)
            {
                LOG.error("", e);
            }
            catch (InvocationTargetException e)
            {
                if (e.getTargetException() instanceof RuntimeException)
                {
                    RuntimeException runtimeException = (RuntimeException)e.getTargetException();
                    throw runtimeException;
                }
                else
                {
                    LOG.error("", e);
                }
            }
        }
    }

    /**
     * Retrurns an AWT component that corresponds to a given native handle.
     */
    public static Component getComponentFromHandle(final int handle)
    {
        final Component[] result = new Component[1];
        Runnable runnable = new Runnable()
        {
            public void run()
            {
                if (JAWT.JDK_1_4)
                {
                    result[0] = JAWT.getComponentFromHandle(new Pointer.Void(handle));
                }
                else
                {
                    try
                    {
                        Method method = WToolkit.class.getMethod("getComponentFromNativeWindowHandle", new Class[]{Component.class});
                        result[0] = (Component)method.invoke(WToolkit.getWToolkit(), new Object[]{new Integer(handle)});
                    }
                    catch (Exception e)
                    {
                        throw new RuntimeException(e.toString());
                    }
                }
            }
        };
        runInDispatchThread(runnable, false);
        return result[0];
    }
}

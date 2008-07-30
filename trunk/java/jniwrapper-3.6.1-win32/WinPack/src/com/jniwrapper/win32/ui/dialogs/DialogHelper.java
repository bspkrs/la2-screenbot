/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.ui.dialogs;

import com.jniwrapper.Function;
import com.jniwrapper.Parameter;
import com.jniwrapper.util.Logger;
import com.jniwrapper.win32.Msg;
import com.jniwrapper.win32.ui.WindowMessage;
import com.jniwrapper.win32.ui.WindowMessageListener;
import com.jniwrapper.win32.ui.WindowProc;
import com.jniwrapper.win32.ui.Wnd;

import java.awt.Frame;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author Serge Piletsky
 */
public class DialogHelper
{
    private static final Logger LOG = Logger.getInstance(DialogHelper.class);

    private DialogHelper()
    {
    }

    private static class FramePainter implements WindowMessageListener
    {
        private Window _window;

        public FramePainter(Window window)
        {
            _window = window;
        }

        public boolean canHandle(WindowMessage message, boolean beforeWindowProc)
        {
            return !beforeWindowProc && message.getMsg() == Msg.WM_PAINT;
        }

        public int handle(WindowMessage message)
        {
            _window.paint(_window.getGraphics());
            return 0;
        }
    }

    public static void invokeDialog(final Window owner,
                                    final Function function,
                                    final Parameter returnValue,
                                    final Parameter[] parameters)
    {
        List frames = new ArrayList(Arrays.asList(Frame.getFrames()));
        if (!frames.contains(owner))
        {
            frames.add(owner);
        }
        List windowProcedures = new ArrayList(frames.size());

        for (Iterator i = frames.iterator(); i.hasNext();)
        {
            Window window = (Window)i.next();
            Wnd wnd = new Wnd(window);
            if (wnd.getValue() != 0)
            {
                WindowProc windowProc = new WindowProc(wnd);
                FramePainter framePainter = new FramePainter(window);
                windowProc.addMessageListener(framePainter);
                windowProc.substitute();
                windowProcedures.add(windowProc);
            }
        }
        try
        {
            Thread dialogThread = new Thread(new Runnable()
            {
                public void run()
                {
                    function.invoke(returnValue, parameters);
                }
            });
            dialogThread.start();
            try
            {
                dialogThread.join();
            }
            catch (InterruptedException e)
            {
                LOG.error("", e);
            }
        }
        finally
        {
            for (Iterator i = windowProcedures.iterator(); i.hasNext();)
            {
                WindowProc windowProcedure = (WindowProc)i.next();
                windowProcedure.restoreNative();
            }
        }
    }
}

/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
import com.jniwrapper.Callback;
import com.jniwrapper.Pointer;
import com.jniwrapper.win32.Handle;
import com.jniwrapper.win32.Msg;
import com.jniwrapper.win32.Size;
import com.jniwrapper.win32.gdi.*;
import com.jniwrapper.win32.system.Beeper;
import com.jniwrapper.win32.system.Module;
import com.jniwrapper.win32.system.Mutex;
import com.jniwrapper.win32.ui.*;

import java.io.File;

/**
 * This samples demonstrates how to display a custom shape window using the
 * {@link Wnd} and {@link Region} classes.
 *
 * @author Alexander Kireyev
 */
public class Buzzer
{
    private static final int TIMER_ID = 1;
    private static final String WINDOW_CLASS_NAME = "BuzzerWindowClass";
    private Wnd _hSplash;
    private Bitmap _hSplashImage;
    private static final int SPLASH_WIDTH = 122;
    private Timer _timer;

    public Buzzer()
    {
        Handle bgBrush = Brush.getStockObject(Brush.StockBrush.NULL_BRUSH);
        WndClass wndClass = new BuzzerWndClass(new BuzzerWindowProc(), WINDOW_CLASS_NAME, bgBrush);
        wndClass.register();
        Module hModule = Module.getCurrent();

        Wnd nullParent = new Wnd();
        Handle NULL = new Handle();
        _hSplash = Wnd.createWindow(WINDOW_CLASS_NAME,
                "Buzzer",
                Wnd.WS_POPUP,
                0,
                0,
                0,
                0,
                nullParent,
                NULL,
                hModule,
                NULL);
    }

    public static void main(String[] args)
    {
        Buzzer buzzer = new Buzzer();
        if (!buzzer.checkOneInstance())
        {
            buzzer.error("Buzzer is already running");
            System.exit(0);
        }
        buzzer.startTimer(10000);
        buzzer.run();
    }

    private void run()
    {
        Wnd hDesktop = DesktopWindow.getInstance();
        Size size = hDesktop.getSize();

        Region hRgn = Region.createElliptic(0, 0, SPLASH_WIDTH, SPLASH_WIDTH);
        _hSplash.setRegion(hRgn, true);

        _hSplash.setPosition(new Wnd(Wnd.HWND_TOPMOST),
                (size.getCx() - SPLASH_WIDTH) / 2,
                (size.getCy() - SPLASH_WIDTH) / 2,
                SPLASH_WIDTH,
                SPLASH_WIDTH,
                Wnd.SWP_SHOWWINDOW);

        _hSplash.eventLoop();
    }

    private void paintSplash()
    {
        PaintStruct paintStruct = new PaintStruct();
        DC hDC = _hSplash.beginPaint(paintStruct);
        DC splashDC = DC.createCompatibleDC(hDC);
        splashDC.selectObject(getSplashImage());
        DC.bitBlt(hDC, 0, 0, SPLASH_WIDTH, SPLASH_WIDTH, splashDC, 0, 0, DC.RasterOperation.SRCCOPY);
        splashDC.release();
        _hSplash.endPaint(paintStruct);
    }

    private Bitmap getSplashImage()
    {
        if (_hSplashImage == null)
        {
            String fileName = Buzzer.class.getResource("res/Buzzer.bmp").getFile();
            _hSplashImage = new DIBitmap(new File(fileName).getAbsolutePath());
        }
        return _hSplashImage;
    }

    private void startTimer(long timeout)
    {
        TimeOutCallback timeOutCallback = new TimeOutCallback();
        _timer = new Timer(_hSplash, TIMER_ID, timeout, timeOutCallback);
        _timer.start();
    }

    private void showMessageBox(String message, int flags)
    {
        MessageBox.show("Buzzer", message, flags);
    }

    private void message(String message)
    {
        showMessageBox(message, MessageBox.ICONEXCLAMATION);
    }

    private void error(String message)
    {
        showMessageBox(message, MessageBox.ICONHAND);
    }

    private boolean checkOneInstance()
    {
        String mutexName = "BuzzerMutex";
        Mutex mutex = Mutex.openMutex(0, false, mutexName);
        if (mutex.exists())
        {
            // Mutex exists - one instance is already running
            return false;
        }

        // Not yet running - lock by creating mutex
        Mutex.createMutex(false, mutexName);

        return true;
    }

    private void buzz()
    {
        // Do beeping
        Beeper.beep(600, 100);
        Beeper.beep(400, 200);
    }

    private void hideSplash()
    {
        _hSplash.show(Wnd.ShowWindowCommand.HIDE);
    }

    private class TimeOutCallback extends Timer.Callback
    {
        public void callback()
        {
            _timer.stop();
            buzz();
            hideSplash();
            message("Timer has elapsed!");
            System.exit(0);
        }
    }

    private static class BuzzerWndClass extends WndClass
    {
        BuzzerWndClass(Callback windowProc, String className, Pointer.Void bgBrushHandle)
        {
            super(windowProc, className);
            _style.setValue(WndClass.CS_HREDRAW | WndClass.CS_VREDRAW);
            _hbrBackground.setValue(bgBrushHandle.getValue());
        }
    }

    private class BuzzerWindowProc extends WindowProc
    {
        public void callback()
        {
            switch ((int)_msg.getValue())
            {
                case Msg.WM_PAINT:
                    paintSplash();
                    break;
                default:
                    super.callback();
                    break;
            }
        }
    }
}

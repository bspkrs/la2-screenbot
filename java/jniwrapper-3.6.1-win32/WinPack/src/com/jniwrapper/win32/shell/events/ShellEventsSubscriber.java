package com.jniwrapper.win32.shell.events;

import com.jniwrapper.*;
import com.jniwrapper.win32.Handle;
import com.jniwrapper.win32.LastErrorException;
import com.jniwrapper.win32.MessageLoopThread;
import com.jniwrapper.win32.Msg;
import com.jniwrapper.win32.shell.Shell32;
import com.jniwrapper.win32.ui.WindowMessage;
import com.jniwrapper.win32.ui.WindowMessageListener;
import com.jniwrapper.win32.ui.WindowProc;
import com.jniwrapper.win32.ui.Wnd;

import javax.swing.*;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * This class provides the convenient way for listening of various shell events that may occur in a system.
 * <p/>
 * All possible shell events are described in {@link ShellEvent} class.
 * <p/>
 * To receive and process a particular shell event you need to add {@link ShellEventsListener} listener.
 */
public class ShellEventsSubscriber
{
    private static final String FUNCTION_SHChangeNotifyRegister = "SHChangeNotifyRegister";
    private static final String FUNCTION_SHChangeNotifyDeregister = "SHChangeNotifyDeregister";

    private static final int WM_NOTIFY = Msg.WM_USER;

    /**
     * All events have occurred.
     */
    private static final int SHCNE_ALLEVENTS = 0x7FFFFFFF;

    /**
     * Shell-level notifications from the shell.
     */
    private static final int SHCNRF_ShellLevel = 0x0002;

    private List _shellEventsListeners = Collections.synchronizedList(new LinkedList());

    private Handle _handle;
    private Wnd _wnd;
    private WindowProc _windowProc;
    private MessageLoopThread _messageLoopThread;

    public ShellEventsSubscriber()
    {
        this(new Handle(), false);
    }

    public ShellEventsSubscriber(final Handle idList, final boolean recursive)
    {
        JWindow hiddenWindow = new JWindow();
        hiddenWindow.setVisible(true);
        _wnd = new Wnd(hiddenWindow);
        _wnd.show(Wnd.ShowWindowCommand.HIDE);

        _windowProc = new WindowProc(_wnd);
        _windowProc.substitute();
        _windowProc.addMessageListener(new WindowMessageListener()
        {
            public boolean canHandle(WindowMessage windowMessage, boolean beforeWindowProc)
            {
                return windowMessage.getMsg() == WM_NOTIFY && beforeWindowProc;
            }

            public int handle(WindowMessage windowMessage)
            {
                handleShellEvent(windowMessage.getLParam(), windowMessage.getWParam());
                return 0;
            }
        });

        _messageLoopThread = new MessageLoopThread();
        _messageLoopThread.doStart();
        subscribe(idList, recursive);
    }

    private void subscribe(final Handle idList, final boolean recursive)
    {
        try
        {
            _messageLoopThread.doInvokeAndWait(new Runnable()
            {
                public void run()
                {
                    SHChangeNotifyEntry entry = new SHChangeNotifyEntry(idList, recursive);
                    _handle = SHChangeNotifyRegister(_wnd, SHCNRF_ShellLevel, SHCNE_ALLEVENTS, WM_NOTIFY, entry);

                    NativeResourceCollector.getInstance().addNativeResource(ShellEventsSubscriber.this, new ShellResource(_handle));
                }
            });
        }
        catch (Exception e)
        {
        }
    }

    private static class ShellResource implements NativeResource
    {
        private Handle _handle;

        public ShellResource(Handle handle)
        {
            _handle = new Handle(handle.getValue());
        }

        public void release() throws Throwable
        {
            if (!_handle.isNull())
            {
                SHChangeNotifyDeregister(_handle);
            }
        }
    }

    /**
     * Adds the specified listener to receive shell events from this subscriber.
     */
    public void addShellEventsListener(ShellEventsListener listener)
    {
        _shellEventsListeners.add(listener);
    }

    /**
     * Removes the specified listener so that it no longer receives shell events.
     */
    public void removeShellEventsListener(ShellEventsListener listener)
    {
        _shellEventsListeners.remove(listener);
    }

    private void handleShellEvent(long lParam, long wParam)
    {
        ShellEvent event = new ShellEvent(this, lParam, wParam);
        for (Iterator i = _shellEventsListeners.iterator(); i.hasNext();)
        {
            ShellEventsListener listener = (ShellEventsListener) i.next();
            listener.processEvent(event);
        }
    }

    /**
     * Registers a window that receives notifications from the file system or shell.
     *
     * @param wnd    Handle to the window that receives the change or notification messages
     * @param flags  One or more of the following values that indicate the type of events for which to receive
     *               notifications
     * @param events Change notification events for which to receive notification
     * @param msg    Message to be posted to the window procedure
     * @param entry  SHChangeNotifyEntry structure that contain the notifications
     * @return registration identifier (ID)
     */
    private static Handle SHChangeNotifyRegister(Wnd wnd, int flags, long events, int msg, SHChangeNotifyEntry entry)
    {
        Function SHChangeNotifyRegister = Shell32.getInstance().getFunction(FUNCTION_SHChangeNotifyRegister);

        Handle result = new Handle();
        UInt32 entries = new UInt32(1);
        long errorCode = SHChangeNotifyRegister.invoke(result, new Parameter[]{
                wnd,
                new UInt32(flags),
                new LongInt(events),
                new UInt(msg),
                entries,
                new Pointer(entry)
        });
        if (result.isNull())
        {
            throw new LastErrorException(errorCode);
        }
        return result;
    }

    /**
     * Unregisters the client's window process from receiving notify events.
     *
     * @param id Registration identifier (ID)
     * @return true if the specified client was found and removed; returns false otherwise
     */
    private static boolean SHChangeNotifyDeregister(Handle id)
    {
        Function SHChangeNotifyDeregister = Shell32.getInstance().getFunction(FUNCTION_SHChangeNotifyDeregister);
        Bool result = new Bool();
        SHChangeNotifyDeregister.invoke(result, id);
        return result.getValue();
    }
}

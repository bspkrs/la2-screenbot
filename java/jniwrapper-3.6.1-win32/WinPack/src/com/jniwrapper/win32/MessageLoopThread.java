/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32;

import com.jniwrapper.*;
import com.jniwrapper.util.Logger;
import com.jniwrapper.win32.system.Kernel32;
import com.jniwrapper.win32.ui.User32;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author Serge Piletsky
 */
public class MessageLoopThread
{
    protected static final Logger _log = Logger.getInstance(MessageLoopThread.class);

    private static final FunctionName FUNCTION_GetMessage = new FunctionName("GetMessage");
    private static final FunctionName FUNCTION_PeekMessage = new FunctionName("PeekMessage");
    private static final String FUNCTION_TranslateMessage = "TranslateMessage";
    private static final FunctionName FUNCTION_DispatchMessage = new FunctionName("DispatchMessage");
    private static final FunctionName FUNCTION_PostThreadMessage = new FunctionName("PostThreadMessage");
    private static final String FUNCTION_GetCurrentThreadId = "GetCurrentThreadId";

    private static final int PM_NOREMOVE = 0;
    private static int _threadIndex = 1;

    private static Function _getMessageFunction;
    private static Function _peekMessageFunction;
    private static Function _translateMessage;
    private static Function _dispatchMessage;
    private static Function _postThreadMessageFunction;
    private static Function _getCurrentThreadId;

    private String _name;
    private boolean _daemon = true;
    private boolean _processing;
    private boolean _isRunning;

    static
    {
        User32 user32 = User32.getInstance();
        _peekMessageFunction = user32.getFunction(FUNCTION_PeekMessage.toString());
        _getMessageFunction = user32.getFunction(FUNCTION_GetMessage.toString());
        _translateMessage = user32.getFunction(FUNCTION_TranslateMessage);
        _dispatchMessage = user32.getFunction(FUNCTION_DispatchMessage.toString());
        _postThreadMessageFunction = user32.getFunction(FUNCTION_PostThreadMessage.toString());
        _getCurrentThreadId = Kernel32.getInstance().getFunction(FUNCTION_GetCurrentThreadId);
    }

    private List _actionsQueue = Collections.synchronizedList(new LinkedList());
    private List _messageListeners = new LinkedList();
    private LoopThread _messageLoopThread;

    private static Map _messageLoops = new HashMap();

    /**
     * Creates message loop instance.
     */
    public MessageLoopThread()
    {
        this("Main message loop");
    }

    /**
     * Creates message loop instance.
     */
    public MessageLoopThread(String name)
    {
        _name = name;
    }

    /**
     * Creates a message loop instance.
     *
     * @param daemon determines if the message loop thread is a daemon thread or not.
     */
    public MessageLoopThread(boolean daemon)
    {
        _name = "Main message loop";
        _daemon = daemon;
    }

    private void put(MessageLoopThread loop, Thread thread)
    {
        _messageLoops.put(thread, loop);
    }

    protected static Map getMessageLoops()
    {
        return _messageLoops;
    }

    /**
     * Starts the message loop.
     */
    public synchronized void doStart()
    {
        if (_messageLoopThread == null)
        {
            _messageLoopThread = new LoopThread(_name, _daemon);
            put(this, _messageLoopThread);
        }

        if (_messageLoopThread.isAlive())
        {
            return;
        }

        _messageLoopThread.start();
        _isRunning = _messageLoopThread.isAlive();
        try
        {
            Thread.sleep(50);
        }
        catch (InterruptedException e)
        {
        }
        NativeResourceCollector.getInstance().addShutdownAction(new Runnable()
        {
            public void run()
            {
                doStop();
            }
        });
    }

    /**
     * Stops the message loop.
     */
    public synchronized void doStop()
    {
        if (_messageLoopThread == null)
        {
            return;
        }

        _isRunning = false;

        try
        {
            _messageLoopThread.stopThread();
            _messageLoopThread.join();
            _messageLoopThread = null;
            put(this, null);
        }
        catch (InterruptedException e)
        {
            _log.error("", e);
            _isRunning = _messageLoopThread.isAlive();
        }
    }

    public boolean isStarted()
    {
        return _isRunning;
    }

    /**
     * Returns true if current thread is main message loop.
     *
     * @return true if current thread is main message loop else returns false
     */
    public synchronized boolean isDispatchThread()
    {
        return Thread.currentThread() == _messageLoopThread;
    }

    /**
     * Invokes the action in the message loop and waits until action executes.
     *
     * @param action action to execute
     *
     * @throws InterruptedException
     * @throws InvocationTargetException
     */
    public void doInvokeAndWait(Runnable action) throws InterruptedException, InvocationTargetException
    {
        checkRunning();
        if (isDispatchThread())
        {
            try
            {
                action.run();
            }
            catch (Exception e)
            {
                throw new InvocationTargetException(e);
            }
        }
        else
        {
            final boolean[] lock = new boolean[1];

            ThreadSynchronizedAction synchronizedAction;
            if (action instanceof MethodInvocationAction)
            {
                synchronizedAction = (ThreadSynchronizedAction)action;
                synchronizedAction._lock = lock;
            }
            else
            {
                synchronizedAction = new ThreadSynchronizedAction(lock, action);
            }
            synchronized (lock)
            {
                _actionsQueue.add(synchronizedAction);
                pingMessageLoopThread();
                int cycleCount = 0;
                while (!lock[0])
                {
                    cycleCount++;
                    lock.wait(0, 1);
                    if (cycleCount % 100 == 0)
                    {
                        pingMessageLoopThread();
                    }
                }
            }
            Exception exception = synchronizedAction.getException();
            if (exception != null)
            {
                throw new InvocationTargetException(exception);
            }
        }
    }

    protected synchronized void pingMessageLoopThread()
    {
        if (_messageLoopThread != null)
        {
            _messageLoopThread.pingThread();
        }
    }

    /**
     * Invokes the action in the message loop.
     *
     * @param action action to execute
     */
    public void doInvokeLater(Runnable action)
    {
        checkRunning();
        ThreadAction threadAction = new ThreadAction(action);
        _actionsQueue.add(threadAction);
        if (!_processing)
        {
            pingMessageLoopThread();
        }
    }

    private void checkRunning()
    {
        if (!_isRunning)
        {
            throw new IllegalStateException(_name + " thread is already stopped.");
        }
    }

    /**
     * Invokes the method in the message loop and waits until the method executes.
     *
     * @param object object which method is called
     * @param methodName name of called method
     *
     * @throws InterruptedException
     * @throws InvocationTargetException
     */
    public Object doInvokeMethod(Object object, String methodName) throws InterruptedException, InvocationTargetException
    {
        return doInvokeMethod(object, methodName, null);
    }

    /**
     * Invokes the method in the message loop and waits until the method executes.
     *
     * @param object which method is called
     * @param methodName name of called method
     * @param parameters method parameters
     *
     * @throws InterruptedException
     * @throws InvocationTargetException
     */
    public Object doInvokeMethod(Object object, String methodName, Object[] parameters) throws InterruptedException, InvocationTargetException
    {
        MethodInvocationAction action = new MethodInvocationAction(object, methodName, parameters);
        doInvokeAndWait(action);
        return action.getResult();
    }

    /**
     * Adds message listener for the message loop.
     *
     * @param listener message listener
     */
    public void doAddMessageListener(MessageLoopListener listener)
    {
        synchronized (_messageListeners)
        {
            if (!_messageListeners.contains(listener))
            {
                _messageListeners.add(listener);
            }
        }
    }

    /**
     * Removes message listener for the message loop.
     *
     * @param listener message listener
     */
    public void doRemoveMessageListener(MessageLoopListener listener)
    {
        synchronized (_messageListeners)
        {
            _messageListeners.remove(listener);
        }
    }

    /**
     * @deprecated This method is incompatible with 64-bit architectures. Use {@link #postSyncThreadMessage(int message, long wParam, long lParam)} instead.
     */
    protected synchronized void postSyncThreadMessage(int message, int wParam, int lParam)
    {
        postSyncThreadMessage(message, (long)wParam, (long)lParam);
    }

    protected synchronized void postSyncThreadMessage(int message, long wParam, long lParam)
    {
        if (_messageLoopThread != null)
        {
            _messageLoopThread.postThreadMessage(message, wParam, lParam);
        }
    }

    protected void onStart()
    {
    }

    protected void onStop()
    {
    }

    private class LoopThread extends Thread
    {
        private com.jniwrapper.win32.system.EventObject _messageQueueIsReady;
        private long _threadID;
        private boolean _running;

        LoopThread()
        {
            this(MessageLoopThread.class.getName(), true);
        }

        LoopThread(String name, boolean daemon)
        {
            setName(name);
            setPriority(Thread.NORM_PRIORITY);
            setDaemon(daemon);
            _messageQueueIsReady = new com.jniwrapper.win32.system.EventObject("MessageQueueIsReady." + _threadIndex++);
        }

        /**
         * @deprecated This method is incompatible with 64-bit architectures. Use {@link #postThreadMessage(int message, long wParam, long lParam)} instead.
         */
        public void postThreadMessage(int message, int wParam, int lParam)
        {
            postThreadMessage(message, (long)wParam, (long)lParam);
        }

        public void postThreadMessage(int message, long wParam, long lParam)
        {
            _messageQueueIsReady.waitFor();
            IntBool result = new IntBool();
            long errorCode = _postThreadMessageFunction.invoke(result, new Parameter[]{
                new UInt32(_threadID),
                new UInt(message),
                new IntPtr(wParam),
                new IntPtr(lParam)
            });

            if (result.getValue() == 0)
            {
                _log.error("Failed to post the message to the thread. ThreadID = " + _threadID +
                        ", errorCode = " + errorCode,
                        new LastErrorException(errorCode));
            }
        }

        public long getThreadID()
        {
            return _threadID;
        }

        private boolean notifyListeners(Msg msg)
        {
            boolean isProcessed = false;
            synchronized (_messageListeners)
            {
                for (Iterator i = _messageListeners.iterator(); i.hasNext();)
                {
                    MessageLoopListener listener = (MessageLoopListener)i.next();
                    if (listener.onMessage(msg))
                    {
                        isProcessed = true;
                    }
                }
            }
            return isProcessed;
        }

        public void stopThread()
        {
            _running = false;
            pingMessageLoopThread();
        }

        void pingThread()
        {
            postSyncThreadMessage(Msg.WM_USER, 0, 0);
        }

        public void run()
        {
            _log.debug("MessageLoopThread.run()");

            MessageLoopThread.this.onStart();

            UInt32 threadID = new UInt32();
            _getCurrentThreadId.invoke(threadID);
            _threadID = threadID.getValue();
            _log.debug("MessageLoopThread.run(): got threadID = " + _threadID);

            Handle wnd = new Handle();

            ShortInt result = new ShortInt();
            Msg msg = new Msg();
            Pointer msgPointer = new Pointer(msg);
            UInt32 nullValue = new UInt32();

            _peekMessageFunction.invoke(result,
                    new Parameter[]{
                        msgPointer,
                        wnd,
                        new UInt32(Msg.WM_USER),
                        new UInt32(Msg.WM_USER),
                        new UInt32(PM_NOREMOVE)
                    });
            // wait a while before notifying
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                _log.error("", e);
            }

            _messageQueueIsReady.notifyEvent();

            _log.debug("MessageLoopThread.run(), messageQueueIsReady");
            result.setValue(1);
            _running = true;
            while (_running && result.getValue() != 0)
            {
                _processing = false;

                _getMessageFunction.invoke(result, msgPointer, wnd, nullValue, nullValue);
                if (result.getValue() == -1)
                {
                    break;
                }

                boolean isProcessed = notifyListeners(msg);
                if (!isProcessed)
                {
                    _translateMessage.invoke(null, msgPointer);
                    try
                    {
                        _dispatchMessage.invoke(null, msgPointer);
                    }
                    catch (Throwable e)
                    {
                        String stringMessage = "Unexpected runtime error has occured in the \"" + this.getName() +
                                "\" thread during message dispatching.";
                        _log.error(stringMessage, e);
                    }
                }

                _processing = true;

                while (!_actionsQueue.isEmpty())
                {
                    ThreadAction action = (ThreadAction)_actionsQueue.remove(0);
                    if (action instanceof ThreadSynchronizedAction)
                    {
                        ThreadSynchronizedAction synchronizedAction = (ThreadSynchronizedAction)action;
                        synchronized (synchronizedAction._lock)
                        {
                            try
                            {
                                synchronizedAction.run();
                            }
                            finally
                            {
                                ((boolean[])synchronizedAction._lock)[0] = true;
                                synchronizedAction._lock.notify();
                            }
                        }
                    }
                    else
                    {
                        try
                        {
                            action.run();
                        }
                        catch (Exception e)
                        {
                            _log.error("Failed to execute an asynchronous action in the MessageLoopThread.", e);
                        }
                    }
                }
            }
            _messageQueueIsReady.close();

            MessageLoopThread.this.onStop();
        }
    }

    private static class ThreadAction implements Runnable
    {
        protected Runnable _action;

        ThreadAction(Runnable action)
        {
            _action = action;
        }

        public void run()
        {
            _action.run();
        }
    }

    private static class ThreadSynchronizedAction extends ThreadAction
    {
        protected Exception _exception;
        protected Object _lock;

        ThreadSynchronizedAction(Object lock, Runnable action)
        {
            super(action);
            _lock = lock;
        }

        public void run()
        {
            try
            {
                _action.run();
            }
            catch (Exception e)
            {
                _exception = e;
            }
        }

        public Exception getException()
        {
            return _exception;
        }
    }

    private static class MethodInvocationAction extends ThreadSynchronizedAction
    {
        private Object _object;
        private Method _method;
        private Object[] _parameters;
        private Object _result;

        MethodInvocationAction(Object object, String methodName, Object[] parameters)
        {
            super(null, null);

            _object = object;
            _parameters = parameters == null ? new Object[]{} : parameters;

            Class[] argumentClasses = parameters == null ? null : new Class[_parameters.length];
            if (argumentClasses != null)
            {
                for (int i = 0; i < _parameters.length; i++)
                {
                    argumentClasses[i] = _parameters[i].getClass();
                }
            }
            try
            {
                _method = object.getClass().getMethod(methodName, argumentClasses);
            }
            catch (Exception e)
            {
                _exception = e;

                _method = findMethod(object, methodName, argumentClasses);
                if (_method != null)
                {
                    _exception = null;
                }
            }
        }

        private Method findMethod(Object object, String methodName, Class[] argumentClasses)
        {
            final Method[] methods = object.getClass().getMethods();

            for (int i = 0; i < methods.length; i++)
            {
                final Method method = methods[i];
                if (method.getName().equals(methodName))
                {
                    final Class[] parameterTypes = method.getParameterTypes();

                    if (argumentClasses != null && parameterTypes.length == argumentClasses.length)
                    {
                        boolean parametersMatched = true;

                        for (int j = 0; j < parameterTypes.length; j++)
                        {
                            if (!parameterTypes[j].isAssignableFrom(argumentClasses[j]))
                            {
                                parametersMatched = false;
                                break;
                            }
                        }

                        if (parametersMatched)
                        {
                            return method;
                        }
                    }
                }
            }
            return null;
        }

        public void run()
        {
            // already got exception
            if (_exception != null)
            {
                return;
            }
            try
            {
                _method.setAccessible(true);
                _result = _method.invoke(_object, _parameters);
            }
            catch (Exception e)
            {
                _exception = e;
            }
        }

        public Object getResult()
        {
            return _result;
        }
    }
}

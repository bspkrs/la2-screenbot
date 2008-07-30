/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.hook.data;

import com.jniwrapper.Parameter;
import com.jniwrapper.Structure;
import com.jniwrapper.win32.hook.*;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents structure which contains a hook events info.
 *
 * @author Serge Piletsky
 */
public class HooksData extends Structure
{
    private MouseHookData _mouseHookData = new MouseHookData();
    private KeyboardHookData _keyboardHookData = new KeyboardHookData();
    private WndProcHookData _wndProcHookData = new WndProcHookData();
    private WndProcRetHookData _wndProcRetHookData = new WndProcRetHookData();
    private GetMsgHookData _getMsgHookData = new GetMsgHookData();
    private SysMsgProcHookData _sysMsgProcHookData = new SysMsgProcHookData();
    private ShellHookData _shellHookData = new ShellHookData();
    private ForegroungIdleHookData _foregroungIdleHookData = new ForegroungIdleHookData();
    private JournalRecordHookData _journalRecordHookData = new JournalRecordHookData();

    private interface HookEventReader
    {
        HookEventObject readEvent(HooksData hookData);
    }

    private static final Map DESCRIPTOR2READER = new HashMap();

    static
    {
        DESCRIPTOR2READER.put(Hook.Descriptor.MOUSE, new MouseHookEventReader());
        DESCRIPTOR2READER.put(Hook.Descriptor.KEYBOARD, new KeyboardHookEventReader());
        DESCRIPTOR2READER.put(Hook.Descriptor.CALLWNDPROC, new CallWndProcEventReader());
        DESCRIPTOR2READER.put(Hook.Descriptor.CALLWNDPROCRET, new CallWndProcRetEventReader());
        DESCRIPTOR2READER.put(Hook.Descriptor.GETMESSAGE, new GetMsgHookEventReader());
        DESCRIPTOR2READER.put(Hook.Descriptor.SYSMSGFILTER, new SysMsgProcHookEventReader());
        DESCRIPTOR2READER.put(Hook.Descriptor.SHELL, new ShellHookEventReader());
        DESCRIPTOR2READER.put(Hook.Descriptor.FOREGROUNDIDLE, new ForegroungIdleHookEventReader());
        DESCRIPTOR2READER.put(Hook.Descriptor.JOURNALRECORD, new JournalRecordHookEventReader());
    }

    public HooksData()
    {
        init(new Parameter[]
        {
            _mouseHookData,
            _keyboardHookData,
            _wndProcHookData,
            _wndProcRetHookData,
            _getMsgHookData,
            _sysMsgProcHookData,
            _shellHookData,
            _foregroungIdleHookData,
            _journalRecordHookData,
        });
    }

    public HooksData(HooksData that)
    {
        this();
        initFrom(that);
    }

    public Object clone()
    {
        return new HooksData(this);
    }

    /**
     * Reads event data depending on a specified hook descriptor.
     * 
     * @param descriptor describes what data to read.
     * @return a hook event.
     */
    public HookEventObject readEvent(Hook.Descriptor descriptor)
    {
        final HookEventReader reader = (HookEventReader)DESCRIPTOR2READER.get(descriptor);
        return reader.readEvent(this);
    }

    /**
     * MouseHookEventReader class represents a mouse hook events data reader
     */
    private static class MouseHookEventReader implements HookEventReader
    {
        public HookEventObject readEvent(HooksData hookData)
        {
            final MouseHookData mouseHookData = hookData._mouseHookData;
            final MouseHookStructure mouseHookStruct = mouseHookData.getMouseHookStruct();
            MouseEvent result = new MouseEvent(mouseHookData.getEventDescriptor(),
                    mouseHookData.getWParam(),
                    mouseHookStruct.getPoint(),
                    mouseHookStruct.getWnd(),
                    mouseHookStruct.getHitTestCode(),
                    mouseHookStruct.getExtraInfo());
            return result;
        }
    }

    /**
     * KeyboardHookEventReader class represents a keyboard hook events data reader
     */
    private static class KeyboardHookEventReader implements HookEventReader
    {
        public HookEventObject readEvent(HooksData hookData)
        {
            final KeyboardHookData keyboardHookData = hookData._keyboardHookData;
            KeyboardEvent result = new KeyboardEvent(keyboardHookData.getEventDescriptor(),
                    keyboardHookData.getWParam(),
                    keyboardHookData.getLParam());
            return result;
        }
    }

    /**
     * CallWndProcEventReader represents a hook events data reader for CallWndProc hook procedure
     * which is an application-defined callback function.
     */
    private static class CallWndProcEventReader implements HookEventReader
    {
        public HookEventObject readEvent(HooksData hookData)
        {
            final WndProcHookData wndProcHookData = hookData._wndProcHookData;
            final CWndProcStructure cwpStruct = wndProcHookData.getCwpStruct();

            CallWndProcEvent result = new CallWndProcEvent(wndProcHookData.getEventDescriptor(),
                    cwpStruct.getLParam(),
                    cwpStruct.getWParam(),
                    cwpStruct.getMessage(),
                    cwpStruct.getWnd(),
                    cwpStruct.wParam.getValue() != 0);
            return result;
        }
    }

    /**
     * CallWndProcRetEventReader represents a hook events data reader for CallWndRetProc hook procedure
     * which is an application-defined callback function and called after the SendMessage function call.
     */
    private static class CallWndProcRetEventReader implements HookEventReader
    {
        public HookEventObject readEvent(HooksData hookData)
        {
            final WndProcRetHookData wndProcRetHookData = hookData._wndProcRetHookData;
            final CWndProcRetStructure cwpRetStruct = wndProcRetHookData.getCwpRetStruct();
            CallWndProcRetEvent result = new CallWndProcRetEvent(wndProcRetHookData.getEventDescriptor(),
                    cwpRetStruct.getResult(),
                    cwpRetStruct.getLParam(),
                    cwpRetStruct.getWParam(),
                    cwpRetStruct.getMessage(),
                    cwpRetStruct.getWnd(),
                    cwpRetStruct.wParam.getValue() != 0);
            return result;
        }
    }

    /**
     * GetMsgHookEventReader represents a hook events data reader for GetMsgProc hook procedure
     * which is an application-defined callback function and called whenever the GetMessage or
     * PeekMessage function has retrieved a message from an application message queue.
     */
    private static class GetMsgHookEventReader implements HookEventReader
    {
//        private final static int PM_NOREMOVE = 0x0000;
        private final static int PM_REMOVE = 0x0001;

        public HookEventObject readEvent(HooksData hookData)
        {
            final GetMsgHookData getMsgHookData = hookData._getMsgHookData;
            GetMsgEvent result = new GetMsgEvent(getMsgHookData.getEventDescriptor(),
                    getMsgHookData.getWParam() == PM_REMOVE,
                    getMsgHookData.getMsg());
            return result;
        }
    }

    /**
     * SysMsgProcHookEventReader represents a hook events data reader for SysMsgProc hook procedure
     * which is an application-defined callback function and called after an input event occurs in
     * a dialog box, message box, menu, or scroll bar, but before the message generated by the input
     * event is processed.
     */
    private static class SysMsgProcHookEventReader implements HookEventReader
    {
        public HookEventObject readEvent(HooksData hookData)
        {
            final SysMsgProcHookData sysMsgProcHookData = hookData._sysMsgProcHookData;
            SysMsgProcEvent result = new SysMsgProcEvent(sysMsgProcHookData.getEventDescriptor(),
                    sysMsgProcHookData.getCode(),
                    sysMsgProcHookData.getMsg());
            return result;
        }
    }

    /**
     * ShellHookEventReader represents a hook events data reader for ShellProc hook procedure
     * which is an application-defined callback function and called when notifications of
     * shell events are received from the system.
     */
    private static class ShellHookEventReader implements HookEventReader
    {
        public HookEventObject readEvent(HooksData hookData)
        {
            final ShellHookData shellHookData = hookData._shellHookData;
            ShellEvent result = new ShellEvent(shellHookData.getEventDescriptor(),
                    shellHookData.getCode(),
                    shellHookData.getWParam(),
                    shellHookData.getLParam());
            return result;
        }
    }

    /**
     * ForegroungIdleHookEventReader represents a hook events data reader for ForegroundIdleProc hook
     * procedure which is an application-defined callback function and called whenever
     * the foreground thread is about to become idle.
     */
    private static class ForegroungIdleHookEventReader implements HookEventReader
    {
        public HookEventObject readEvent(HooksData hookData)
        {
            final ForegroungIdleHookData foregroungIdleHookData = hookData._foregroungIdleHookData;
            ForegroungIdleEvent result = new ForegroungIdleEvent(foregroungIdleHookData.getEventDescriptor());
            return result;
        }
    }

    /**
     * JournalRecordHookEventReader represents a hook events data reader for JournalRecordProc hook
     * procedure which is an application-defined callback function and called whenever
     * system removes messages from the system message queue.
     */
    private static class JournalRecordHookEventReader implements HookEventReader
    {
        public HookEventObject readEvent(HooksData hookData)
        {
            final JournalRecordHookData jornalRecordHookData = hookData._journalRecordHookData;

            JournalRecordEvent result = null;
            final long code = jornalRecordHookData.getCode();
            if (code == 0)
            {
                final EventMessageStructure eventMessage = jornalRecordHookData.getEventMessage();
                result = new JournalRecordEvent(jornalRecordHookData.getEventDescriptor(),
                        code,
                        eventMessage.getMessage(),
                        eventMessage.getParamL(),
                        eventMessage.getParamH(),
                        eventMessage.getTime(),
                        eventMessage.getHwnd());
            }
            else
            {
                result = new JournalRecordEvent(jornalRecordHookData.getEventDescriptor(), code);
            }
            return result;
        }
    }
}

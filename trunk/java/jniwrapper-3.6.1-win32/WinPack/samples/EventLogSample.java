/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
import com.jniwrapper.win32.system.eventlog.EventLog;
import com.jniwrapper.win32.system.eventlog.EventLogMessage;

import java.util.List;

/**
 * This sample prints out all messages from the
 * "Application" event log.
 *
 * @author Vladimir Kondrashchenko
 */
public class EventLogSample
{
    public static void main(String[] args)
    {
        EventLog eventLog = new EventLog("Application");
        List messages = eventLog.getMessages();
        for (int i = 0; i < messages.size(); i++)
        {
            EventLogMessage eventLogMessage = (EventLogMessage)messages.get(i);
            System.out.println(eventLogMessage.getRecordNumber() + "\t" + eventLogMessage.getDate());
        }
    }
}

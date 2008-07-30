/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.system.eventlog;

import com.jniwrapper.*;
import com.jniwrapper.win32.FunctionName;
import com.jniwrapper.win32.Handle;
import com.jniwrapper.win32.LastError;
import com.jniwrapper.win32.LastErrorException;
import com.jniwrapper.win32.system.AdvApi32;

import java.io.File;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * This class provides functionality for working with system event logs.
 *
 * @author Vladimir Kondrashchenko
 */
public class EventLog extends Handle
{
    private static final FunctionName FUNCTION_OPENEVENTLOG = new FunctionName("OpenEventLog");
    private static final FunctionName FUNCTION_BACKUPEVENTLOG = new FunctionName("BackupEventLog");
    private static final FunctionName FUNCTION_CLEAREVENTLOG = new FunctionName("ClearEventLog");
    private static final String FUNCTION_CLOSEEVENTLOG = "CloseEventLog";
    private static final String FUNCTION_GETNUMBEROFRECORDS = "GetNumberOfEventLogRecords";
    private static final String FUNCTION_GETOLDESTRECORD = "GetOldestEventLogRecord";
    private static final FunctionName FUNCTION_OPENBACKUPEVENTLOG = new FunctionName("OpenBackupEventLog");
    private static final FunctionName FUNCTION_READEVENTLOG = new FunctionName("ReadEventLog");
    private static final FunctionName FUNCTION_REGISTEREVENTSOURCE = new FunctionName("RegisterEventSource");
    private static final FunctionName FUNCTION_REPORTEVENT = new FunctionName("ReportEvent");
    private static final FunctionName FUNCTION_LOOKUPACCOUNTSID = new FunctionName("LookupAccountSid");

    private static final long EVENTLOG_SEQUENTIAL_READ = 0x0001;
    private static final long EVENTLOG_SEEK_READ = 0x0002;
    private static final long EVENTLOG_FORWARDS_READ = 0x0004;

    private static final int BUFFER_SIZE = 65536;

    private String _name;
    private String _server;

    private EventLog()
    {
    }

    /**
     * Opens an event log for reading. If the specified event log cannot be found,
     * the Application log will be opened.
     *
     * @param logName specifies the name of the log to be opened.
     */
    public EventLog(String logName)
    {
        this(null, logName);
    }

    /**
     * Opens a remote event log for reading. If the specified event log cannot be found,
     * the Application log will be opened.
     *
     * @param serverName is the UNC name of the server.
     * @param logName specifies the name of the log to be opened.
     */
    public EventLog(String serverName, String logName)
    {
        Function function = AdvApi32.getInstance().getFunction(FUNCTION_OPENEVENTLOG.toString());

        Parameter server;
        if (serverName == null)
        {
            server = new Pointer(null, true);
        }
        else
        {
            server = new Str(serverName);
        }

        long result = function.invoke(this, server, new Str(logName));

        if (getValue() == 0)
        {
            throw new RuntimeException(LastError.getMessage(result));
        }
        _name = logName;
        _server = serverName;
    }

    /**
     * Returns the name of the opened log.
     *
     * @return the name of the opened log.
     */
    public String getLogName()
    {
        return _name;
    }

    /**
     * If a remote event log has been opened, then the function returns the name of the remote server.
     * Otherwise, the function returns <code>null</code>.
     *
     * @return the function returns the name of the remote server.
     */
    public String getServerName()
    {
        return _server;
    }

    /**
     * Opens the event log that was backed up.
     *
     * @param backup specifies the backup file.
     * @return opened event log.
     */
    public static EventLog openBackup(File backup)
    {
        if (backup == null || !backup.isFile())
        {
            throw new IllegalArgumentException("Illegal backup file.");
        }

        Function function = AdvApi32.getInstance().getFunction(FUNCTION_OPENBACKUPEVENTLOG.toString());

        EventLog eventLog = new EventLog();

        long result = function.invoke(eventLog, new Pointer(null, true), new Str(backup.getAbsolutePath()));

        if (result != 0)
        {
            throw new RuntimeException(LastError.getMessage(result));
        }

        return eventLog;
    }

    /**
     * Backs up the opened event log to a file.
     *
     * @param file specifies the backup file.
     */
    public void backup(File file)
    {
        if (file == null)
        {
            throw new IllegalArgumentException("Illegal backup file.");
        }

        Function function = AdvApi32.getInstance().getFunction(FUNCTION_BACKUPEVENTLOG.toString());

        long result = function.invoke(null, this, new Str(file.getAbsolutePath()));

        if (result != 0)
        {
            throw new LastErrorException(result);
        }
    }

    /**
     * Closes the event log handle.
     */
    public void close()
    {
        Function function = AdvApi32.getInstance().getFunction(FUNCTION_CLOSEEVENTLOG);

        long result = function.invoke(null, this);

        if (result != 0 && result != 38)
        {
            throw new LastErrorException(result);
        }
    }

    /**
     * Clears the opened event log.
     */
    public void clear()
    {
        Function function = AdvApi32.getInstance().getFunction(FUNCTION_CLEAREVENTLOG.toString());

        long result = function.invoke(null, this, new Pointer(null, true));

        if (result != 0 && result != 38)
        {
            throw new LastErrorException(result);
        }
    }

    /**
     * Returns the number of records in the event log.
     *
     * @return the number of records in the event log.
     */
    public int getRecordsCount()
    {
        Function function = AdvApi32.getInstance().getFunction(FUNCTION_GETNUMBEROFRECORDS);

        UInt32 count = new UInt32();
        IntBool retVal = new IntBool();
        long result = function.invoke(retVal, this, new Pointer(count));

        if (retVal.getValue() == 0)
        {
            throw new LastErrorException(result);
        }

        return (int) count.getValue();
    }

    private int getOldestRecordNumber()
    {
        Function function = AdvApi32.getInstance().getFunction(FUNCTION_GETOLDESTRECORD);

        UInt32 count = new UInt32();
        IntBool retVal = new IntBool();
        long result = function.invoke(retVal, this, new Pointer(count));
        if (retVal.getValue() != 0)
        {
            // TODO [Jazz]: investigate why this exception can be thrown
            //throw new LastErrorException(result);
        }

        return (int) count.getValue();
    }

    private void flush()
    {
        Function function = AdvApi32.getInstance().getFunction(FUNCTION_READEVENTLOG.toString());

        function.invoke(null, new Parameter[] {
            this,
            new UInt32(EVENTLOG_SEEK_READ | EVENTLOG_FORWARDS_READ),
            new UInt32(getOldestRecordNumber()),
            new Pointer(new UInt8()),
            new UInt32(0),
            new Pointer(new UInt32()),
            new Pointer(new UInt32())});
    }

    /**
     * Returns the list of event log messages. Each element of the list is an
     * instance of {@link EventLogMessage}.
     *
     * @return the list of event log messages.
     * @see EventLogMessage
     */
    public List getMessages()
    {
        flush();

        /* reads a whole number of entries from the specified event log.
          BOOL ReadEventLog(
          HANDLE hEventLog, // Handle to the event log to be read.
          DWORD dwReadFlags, // Options for how the read operation is to proceed.
          DWORD dwRecordOffset, // Log-entry record number at which the read operation should start.
          LPVOID lpBuffer, // Pointer to a buffer for the data read from the event log. The buffer will be filled with an EVENTLOGRECORD structure.
          DWORD nNumberOfBytesToRead, // Size of the buffer, in bytes.
          DWORD* pnBytesRead, // Pointer to a variable that receives the number of bytes read by the function.
          DWORD* pnMinNumberOfBytesNeeded // Pointer to a variable that receives the number of bytes required for the next log entry.
        );*/
        Function function = AdvApi32.getInstance().getFunction(FUNCTION_READEVENTLOG.toString());
        List result = new LinkedList();
        UInt32 read;

        if (getRecordsCount() == 0)
        {
            return result;
        }

        do
        {
            read = new UInt32(); // the number of bytes read by the function.
            PrimitiveArray array = new PrimitiveArray(UInt8.class, BUFFER_SIZE); // buffer for the data read from the event log.

            int offset = 0;
            IntBool retVal = new IntBool();
            long error = function.invoke(retVal, new Parameter[] {
                this,
                new UInt32(EVENTLOG_SEQUENTIAL_READ | EVENTLOG_FORWARDS_READ),
                new UInt32(0),
                new Pointer(array),
                new UInt32(BUFFER_SIZE),
                new Pointer(read),
                new Pointer(new UInt32())});
            if (retVal.getValue() == 0 && error != 0 && error != 38 && error != 122)
            {
                throw new LastErrorException(error);
            }
            if (read.getValue() == 0)
            {
                break;
            }
            byte[] bytes = array.getBytes();
            int total = (int) read.getValue();
            UInt32 dwSize = new UInt32();
            do
            {
                byte[] bSize = new byte[4];
                System.arraycopy(bytes, offset, bSize, 0, 4);
                dwSize.read(bSize, 0);  // read length of current record (record is a EVENTLOGRECORD structure).
                int size = (int) dwSize.getValue();
                byte[] data = new byte[size];
                System.arraycopy(bytes, offset, data, 0, size);
                result.add(parseBytes(data));
                offset += size;
            } while (offset != total);
        } while (true);

        return result;
    }

    private EventLogMessage parseBytes(byte[] bytes)
    {
        Function accountSID = AdvApi32.getInstance().getFunction(FUNCTION_LOOKUPACCOUNTSID.toString());

        EventLogMessageInt logMessageInt = new EventLogMessageInt();
        logMessageInt.read(bytes, 0);

        EventLogMessage eventLogMessage = new EventLogMessage();

        eventLogMessage.setRecordNumber((int) logMessageInt.getRecordNumber());
        eventLogMessage.setEventID(logMessageInt.getEventID() & 0xFFFF);
        eventLogMessage.setDate(new Date(logMessageInt.getTimeWritten()*1000));
        eventLogMessage.setCategory((int) logMessageInt.getEventCategory());
        eventLogMessage.setEventType(new EventLogMessage.Type((int) logMessageInt.getEventType()));

        // TODO [kopijka]: investigate why getDataOffset() method for LogMessageInt structure returns incorrect value.
        //int messageSize = logMessageInt.getDataOffset() - logMessageInt.getStringOffset();
        int messageSize = bytes.length - logMessageInt.getStringOffset();

        // number of strings in message
        int stringsCount = (int)logMessageInt._numStrings.getValue();
        if (stringsCount > 0)
        {
            byte[] message = new byte[messageSize];
            System.arraycopy(bytes, logMessageInt.getStringOffset(), message, 0, messageSize);
            String unicodeMessage = retrieveMessage(message, stringsCount);
            eventLogMessage.setMessage(unicodeMessage.replaceAll("\0",""));
        }

        // dataLength - Size of the event-specific data (at the position indicated by DataOffset), in bytes.
        int dataLength = logMessageInt.getDataLength();

        if (dataLength > 0)
        {
            byte[] data = new byte[dataLength];
            System.arraycopy(bytes, logMessageInt.getDataOffset(), data, 0, dataLength);
            eventLogMessage.setData(data);
        }

        int sourceSize;
        if (logMessageInt.getUserSidOffset() != 0)
        {
            sourceSize = logMessageInt.getUserSidOffset() - 1 - logMessageInt.getLength();
        }
        else
        {
            sourceSize = logMessageInt.getStringOffset() - 1 - logMessageInt.getLength();
        }

        byte[] bSource = new byte[sourceSize];
        System.arraycopy(bytes, logMessageInt.getLength(), bSource, 0, sourceSize);

        String source = new String(bSource);
        int bound = getStringSeparatorPos(source);
        String strSource = source.substring(0, bound).replaceAll("\0", "");
        eventLogMessage.setSource(strSource);

        String computerName = getNullTerminatedString(source.substring(bound + 2));
        eventLogMessage.setComputer(computerName.replaceAll("\0",""));

        byte[] userSid = new byte[logMessageInt.getUserSidLength()];
        System.arraycopy(bytes, logMessageInt.getUserSidOffset(), userSid, 0, logMessageInt.getUserSidLength());

        if (userSid.length != 0)
        {
            Str name = new Str(255);
            Int id = new Int();
            accountSID.invoke(null, new Parameter[] {
                new Pointer(null, true),
                new Pointer(new PrimitiveArray(userSid)),
                new Pointer(name),
                new Pointer(new UInt32(255)),
                new Pointer(new Str(255)),
                new Pointer(new UInt32(255)),
                new Pointer(id)});
            eventLogMessage.setUser(name.getValue());
        }
        else
        {
            eventLogMessage.setUser(null);
        }

        return eventLogMessage;
    }

    /**
     * Gets the first founded null-terminated string in specified string.
     * @param unicodeStr strng in unicode form.
     * @return null-terminated string in unicode form.
     */
    private String getNullTerminatedString(String unicodeStr)
    {
        int indexOfNull = unicodeStr.indexOf("\0\0");
        return unicodeStr.substring(0, indexOfNull);
    }

    /**
     * Retrieves message of merged null-terminated strings from byte array.
     * @param message byte array of message data.
     * @param stringsCount number of strings in message.
     * @return message of merged strings.
     */
    private String retrieveMessage(byte[] message, int stringsCount)
    {
        StringBuffer retrievedMessage = new StringBuffer();
        String msg = new String(message);
        int beginIndex = 0;
        int endIndex;
        int curStringIndex = 0;
        while(curStringIndex < stringsCount)
        {
            endIndex = msg.indexOf("\0\0", beginIndex);
            retrievedMessage.append(msg.substring(beginIndex, endIndex));
            beginIndex = endIndex + 1;
            curStringIndex++;
        }

        return retrievedMessage.toString();
    }

    private int getStringSeparatorPos(String s)
    {
        int pos = 0;
        while ((pos = s.indexOf('\0', pos)) != -1)
        {
            if (s.charAt(pos - 1) == '\0' && s.charAt(pos + 1) == '\0')
            {
                return pos;
            }
            pos++;
        }
        return pos;
    }

    /**
     * Posts the message to an event log.
     *
     * @param message specifies the message to be posted.
     */ 
    public static void reportEvent(EventLogMessage message)
    {
        if (message == null)
        {
            throw new IllegalArgumentException("Illegal message argument.");
        }

        EventLog eventLog = EventLog.registerSource(message.getSource());

        Function function = AdvApi32.getInstance().getFunction(FUNCTION_REPORTEVENT.toString());

        PrimitiveArray array = new PrimitiveArray(Str.class, 1);
        if (message.getMessage() != null)
        {
            array.setElement(0, new Pointer(new Str(message.getMessage())));
        }
        else
        {
            array.setElement(0, new Pointer(new Str()));
        }

        int dataSize = 0;
        Pointer.Void pData = new Pointer.Void();

        if (message.getData() != null)
        {
            dataSize = message.getData().length;
            new Pointer(new PrimitiveArray(message.getData())).castTo(pData);
        }

        function.invoke(null, new Parameter[] {
                eventLog,
                new UInt16(message.getEventType() == null ?
                           EventLogMessage.Type.INFORMATION.getValue() : message.getEventType().getValue()),
                new UInt16(message.getCategory()),
                new UInt32(message.getEventID()),
                new Pointer(null, true),
                new UInt16(1),
                new UInt32(dataSize),
                new Pointer(array),
                pData});

        eventLog.close();
    }

    private static EventLog registerSource(String logName)
    {
        Function function = AdvApi32.getInstance().getFunction(FUNCTION_REGISTEREVENTSOURCE.toString());

        EventLog eventLog = new EventLog();

        long result = function.invoke(eventLog, new Pointer(null, true), new Str(logName));

        if (result != 0)
        {
            throw new LastErrorException(result);
        }

        return eventLog;
    }

    /**
     * EVENTLOGRECORD native structure.
     */
    private static class EventLogMessageInt extends Structure
    {
        private UInt32 _length = new UInt32();
        private UInt32 _reserved= new UInt32();
        private UInt32 _recordNumber = new UInt32();
        private UInt32 _timeGenerated = new UInt32();
        private UInt32 _timeWritten = new UInt32();
        private UInt32 _eventID = new UInt32();
        private UInt16 _eventType = new UInt16();
        private UInt16 _numStrings = new UInt16();
        private UInt16 _eventCategory = new UInt16();
        private UInt16 _reservedFlags = new UInt16();
        private UInt32 _closingRecordNumber = new UInt32();
        private UInt32 _stringOffset = new UInt32();
        private UInt32 _userSidLength = new UInt32();
        private UInt32 _userSidOffset = new UInt32();
        private UInt32 _dataLength = new UInt32();
        private UInt32 _dataOffset = new UInt32();

        public EventLogMessageInt()
        {
            init(new Parameter[] {
                _length,
                _reserved,
                _recordNumber,
                _timeGenerated,
                _timeWritten,
                _eventID,
                _eventType,
                _numStrings,
                _eventCategory,
                _reservedFlags,
                _closingRecordNumber,
                _stringOffset,
                _userSidLength,
                _userSidOffset,
                _dataLength,
                _dataOffset
            });

        }

        public EventLogMessageInt(EventLogMessageInt that)
        {
            this();
            initFrom(that);
        }

        public long getRecordNumber()
        {
            return _recordNumber.getValue();
        }

        public long getTimeGenerated()
        {
            return _timeGenerated.getValue();
        }

        public long getEventID()
        {
            return _eventID.getValue();
        }

        public long getEventType()
        {
            return _eventType.getValue() ;
        }

        public long getEventCategory()
        {
            return _eventCategory.getValue();
        }

        public long getRecordLength()
        {
            return _length.getValue();
        }

        public long getTimeWritten()
        {
            return _timeWritten.getValue();
        }

        public int getUserSidLength()
        {
            return (int) _userSidLength.getValue();
        }

        public int getUserSidOffset()
        {
            return (int) _userSidOffset.getValue();
        }

        public int getStringOffset()
        {
            return (int) _stringOffset.getValue();
        }

        public int getDataLength()
        {
            return (int) _dataLength.getValue();
        }

        public int getDataOffset()
        {
            return (int) _dataOffset.getValue();
        }

        public Object clone()
        {
            return new EventLogMessageInt(this);
        }
    }
}

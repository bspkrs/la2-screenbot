/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
import com.jniwrapper.win32.dde.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

/**
 * This sample implements a simple DDE server that supports a single service "MyService".
 *
 * The service contains two topics: System and Data.
 *
 * The System topic supports the following item names: Topics (a list of supported topics),
 * Items (a list of non-System item names), Formats (a list of supported clipboard formats)
 * and SysItems (a list of System item names).
 *
 * The Data topic is designed for data management. It supports the following item names:
 * CurrentDate ([out] sends a client a string representation of the current date),
 * Spool ([in, out] sends or recieves text data),
 * Timer ([out] the item value changes every second and equals the time in seconds since the
 * service has been started).
 * The Data topic also supports the "reverse" command that replaces the Spool text by the reverse of the text.
 *
 * @author Vladimir Kondrashchenko
 */
public class DdeServiceSample
{
    private static String _currentDate;
    private static StringBuffer _spool = new StringBuffer();
    private static int _timer;
    private static DdeService _service;

    private static class EventHandler implements DdeServiceEventHandler
    {
        EventHandler()
        {
            Date date = new Date();
            _currentDate = date.toString();
            new Timer(1000, new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    _timer++;
                    if (_service != null && _service.isRegistered())
                    {
                        try
                        {
                            _service.postAdvise("Data", "Timer");
                        }
                        catch (DdeException ex)
                        {
                            ex.printStackTrace();
                        }
                    }
                }
            }).start();
        }

        public byte[] adviseRequest(String topic, DdeItem item)
        {
            if (!"Data".equalsIgnoreCase(topic) | !"Timer".equalsIgnoreCase(item.getName()) |
                    item.getFormat() != DdeItem.CF_TEXT)
            {
                return null;
            }
            String result = new Integer(_timer).toString();

            System.out.println("Advise request : " + result);

            return result.getBytes();
        }

        public boolean adviseStart(String topic, DdeItem item)
        {
            if (!"Data".equalsIgnoreCase(topic) | !"Timer".equalsIgnoreCase(item.getName()) |
                    item.getFormat() != DdeItem.CF_TEXT)
            {
                System.out.println("Advise start : refused");
                return false;
            }
            else
            {
                System.out.println("Advise start : confirmed");
                return true;
            }
        }

        public void adviseStop(String topic, DdeItem item)
        {
            System.out.println("Advise stoped");
        }

        public DdeResponse execute(String topic, String command)
        {
            if (!"Data".equalsIgnoreCase(topic) | !"reverse".equalsIgnoreCase(command))
            {
                System.out.println("Execute [" + topic + ", " + command + "] : Not processed");
                return DdeResponse.NOTPROCESSED;
            }
            _spool = _spool.reverse();
            System.out.println("Execute [" + topic + ", " + command + "] : Processed");
            return DdeResponse.PROCESSED;
        }

        public boolean beforeConnect(String topic, boolean sameApplication)
        {
            if (!"System".equalsIgnoreCase(topic) && !"Data".equalsIgnoreCase(topic))
            {
                if (sameApplication)
                {
                    System.out.println("Connection to " + topic + " has been refused (the same application)");
                }
                else
                {
                    System.out.println("Connection to " + topic + " has been refused");
                }
                return false;
            }
            else
            {
                if (sameApplication)
                {
                    System.out.println("Connection to " + topic + " has been established (the same application)");

                }
                else
                {
                    System.out.println("Connection to " + topic + " has been established");
                }
                return true;
            }
        }

        public DdeResponse pokeData(String topic, DdeItem item, byte[] data)
        {
            if (!"Data".equalsIgnoreCase(topic) | !"Spool".equalsIgnoreCase(item.getName()) |
                    item.getFormat() != DdeItem.CF_TEXT)
            {
                System.out.println("Poke data [" + new String(data) + "] : Not processed");
                return DdeResponse.NOTPROCESSED;
            }
            _spool = new StringBuffer(new String(data));
            System.out.println("Poke data [" + new String(data) + "] : Processed");
            return DdeResponse.PROCESSED;
        }

        public byte[] requestData(String topic, DdeItem item)
        {
            if ("System".equalsIgnoreCase(topic))
            {
                if ("Topics".equalsIgnoreCase(item.getName()))
                {
                    String result = "System\tData";
                    System.out.println("Request data [" + topic + ", " + item + "] : " + result);
                    return result.getBytes();
                }
                else if ("Items".equalsIgnoreCase(item.getName()))
                {
                    String result = "CurrentDate\tSpool\tTimer";
                    System.out.println("Request data [" + topic + ", " + item + "] : " + result);
                    return result.getBytes();
                }
                else if ("Formats".equalsIgnoreCase(item.getName()))
                {
                    String result = "CF_TEXT";
                    System.out.println("Request data [" + topic + ", " + item + "] : " + result);
                    return result.getBytes();
                }
                else if ("SysItems".equalsIgnoreCase(item.getName()))
                {
                    String result = "Topics\tItems\tFormats\tSysItems";
                    System.out.println("Request data [" + topic + ", " + item + "] : " + result);
                    return result.getBytes();
                }
                else
                {
                    System.out.println("Request data : item \"" + item + "\" is not supported by System topic");
                    return null;
                }
            }
            else if ("Data".equalsIgnoreCase(topic))
            {
                if ("CurrentDate".equalsIgnoreCase(item.getName()))
                {
                    System.out.println("Request data [" + topic + ", " + item + "] : " + _currentDate);
                    return _currentDate.getBytes();
                }
                else if ("Spool".equalsIgnoreCase(item.getName()))
                {
                    System.out.println("Request data [" + topic + ", " + item + "] : " + _spool.toString());
                    return _spool.toString().getBytes();
                }
                else if ("Timer".equalsIgnoreCase(item.getName()))
                {
                    String result = new Integer(_timer).toString();
                    System.out.println("Request data [" + topic + ", " + item + "] : " + result);
                    return result.getBytes();
                }
                else
                {
                    System.out.println("Request data : item \"" + item + "\" is not supported by Data topic");
                    return null;
                }
            }
            else
            {
                System.out.println("Request data : topic \"" + topic + "\" is not supported");
                return null;
            }
        }

        public void disconnect(boolean sameApplication)
        {
            if (sameApplication)
            {
                System.out.println("Disconnected (the same application)");
            }
            else
            {
                System.out.println("Disconnected");
            }
        }

        public void serviceRegister(String service, String instanceName)
        {
            System.out.println("A service registered : " + instanceName);
        }

        public void serviceUnregister(String service, String instanceName)
        {
            System.out.println("A service unregistered : " + instanceName);
        }
    }

    private static void runService() throws DdeException
    {
        _service = new DdeService("MyService");
        EventHandler eventHandler = new EventHandler();
        _service.setEventHandler(eventHandler);
        _service.register();
        System.out.println("Service \"MyService\" is running...");
    }

    private static void shutdownService() throws DdeException
    {
        _service.unregister();
        System.out.println("Service \"MyService\" is stopped.");
    }

    public static void main(String[] args) throws Exception
    {
        runService();

        System.in.read();

        shutdownService();
    }
}

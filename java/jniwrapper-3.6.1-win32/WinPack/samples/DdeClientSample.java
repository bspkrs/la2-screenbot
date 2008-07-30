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
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * This sample demonstrates a simple conversation between a DDE client and "MyService" service.
 *
 * @see  DdeServiceSample for MyService description.
 *
 * @author Vladimir Kondrashchenko
 */
public class DdeClientSample extends JFrame
{
    private DdeClient _systemClient;
    private DdeClient _dataClient;

    private JTextArea _logArea;
    private JToggleButton _btnConnect;
    private JButton _btnQuerySysInfo;
    private JButton _btnCurrentDate;
    private JToggleButton _btnAdviseLoop;
    private JTextField _textField;
    private JButton _btnGetSpool;
    private JButton _btnSetSpool;
    private JButton _btnReverseSpool;

    private long _timeout = 1000;
    private long _transactionID;

    public DdeClientSample()
    {
        super("DDE client sample");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        _systemClient = new DdeClient("MyService", "System");

        _dataClient = new DdeClient("MyService", "Data");
        _dataClient.setEventHandler(new EventHandler());

        _logArea = new JTextArea();
        _logArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        _logArea.setEditable(false);
        _logArea.append("DDE conversation log:\n");

        _btnConnect = new JToggleButton("Connect to MyService");
        _btnConnect.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if (_btnConnect.isSelected())
                {
                    try
                    {
                        _systemClient.connect();
                        _dataClient.connect();
                        _logArea.append("The connection to MyService is established successfully\n");
                        _btnConnect.setText("Disconnect from MyService");
                    }
                    catch (DdeException ex)
                    {
                        _logArea.append("Unable to connect to MyService\n");
                        _btnConnect.setSelected(false);
                    }
                }
                else
                {
                    try
                    {
                        _systemClient.connect();
                        _dataClient.connect();
                        _logArea.append("The client is disconnected from MyService\n");
                        _btnConnect.setText("Connect to MyService");
                    }
                    catch (DdeException ex)
                    {
                        _logArea.append("Unable to disconnect from MyService\n");
                    }
                }
            }
        });

        _btnQuerySysInfo = new JButton("Query system info");
        _btnQuerySysInfo.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    _logArea.append("\nMyService system information:\n");
                    byte[] data = _systemClient.get(new DdeItem("Topics", DdeItem.CF_TEXT), _timeout);
                    _logArea.append("\tTopics: " + new String(data) + "\n");
                    data = _systemClient.get(new DdeItem("Items", DdeItem.CF_TEXT), _timeout);
                    _logArea.append("\tNon-system items: " + new String(data) + "\n");
                    data = _systemClient.get(new DdeItem("SysItems", DdeItem.CF_TEXT), _timeout);
                    _logArea.append("\tSystem items: " + new String(data) + "\n");
                    data = _systemClient.get(new DdeItem("Formats", DdeItem.CF_TEXT), _timeout);
                    _logArea.append("\tSupported data formats: " + new String(data) + "\n");
                }
                catch (DdeException ex)
                {
                    _logArea.append("Unable to query system information\n");
                }
            }
        });

        _btnCurrentDate = new JButton("Get current date");
        _btnCurrentDate.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    _transactionID = _dataClient.getAsync(new DdeItem("CurrentDate", DdeItem.CF_TEXT));
                }
                catch (DdeException ex)
                {
                    _logArea.append("Unable to get current date\n");
                }
            }
        });

        _btnAdviseLoop = new JToggleButton("Start advise loop");
        _btnAdviseLoop.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if (_btnAdviseLoop.isSelected())
                {
                    try
                    {
                        _dataClient.startAdviseLoop(new DdeItem("Timer", DdeItem.CF_TEXT), true, true, _timeout);
                        _logArea.append("An advise loop is started\n");
                        _btnAdviseLoop.setText("Stop advise loop");
                    }
                    catch (DdeException ex)
                    {
                        _btnAdviseLoop.setSelected(false);
                        _logArea.append("Unable to start an advise loop\n");
                    }
                }
                else
                {
                    try
                    {
                        _dataClient.stopAdviseLoop(new DdeItem("Timer"), _timeout);
                        _logArea.append("An advise loop is stopped\n");
                        _btnAdviseLoop.setText("Start advise loop");
                    }
                    catch (DdeException ex)
                    {
                        _logArea.append("Unable to stop the advise loop\n");
                    }
                }
            }
        });

        _textField = new JTextField(15);
        _textField.setFont(new Font("", Font.PLAIN, 14));

        _btnGetSpool = new JButton("Receive a spool data");
        _btnGetSpool.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    byte[] data = _dataClient.get(new DdeItem("Spool"), _timeout);
                    _logArea.append("The spool data : " + new String(data) + "\n");
                }
                catch (DdeException ex)
                {
                    _logArea.append("Unable to receive a spool data\n");
                }
            }
        });

        _btnSetSpool = new JButton("Send the spool data");
        _btnSetSpool.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    _dataClient.send(new DdeItem("Spool"), _textField.getText().getBytes(), _timeout);
                    _logArea.append("\"" + _textField.getText() + "\" was sent to Spool\n");
                }
                catch (DdeException ex)
                {
                    _logArea.append("Unable to send data to Spool\n");
                }
            }
        });

        _btnReverseSpool = new JButton("Reverse the spool data");
        _btnReverseSpool.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    _dataClient.execute("Reverse", _timeout);
                    _logArea.append("The command was successfully executed\n");
                }
                catch (DdeException ex)
                {
                    _logArea.append("Unable to execute the command\n");
                }
            }
        });

        JPanel navPanel = new JPanel();
        navPanel.setPreferredSize(new Dimension(200, 400));
        navPanel.setLayout(new GridBagLayout());
        navPanel.add(_btnConnect, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                , GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        navPanel.add(_btnQuerySysInfo, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
                , GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        navPanel.add(_btnCurrentDate, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
                , GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        navPanel.add(_btnAdviseLoop, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
                , GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        navPanel.add(new JLabel("The spool data : "), new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
                , GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 0, 0), 0, 0));
        navPanel.add(_textField, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
                , GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        navPanel.add(_btnGetSpool, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
                , GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        navPanel.add(_btnSetSpool, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0
                , GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        navPanel.add(_btnReverseSpool, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0
                , GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        navPanel.add(new JPanel(), new GridBagConstraints(0, 9, 1, 1, 1.0, 1.0
                , GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(navPanel, BorderLayout.WEST);
        cp.add(new JScrollPane(_logArea), BorderLayout.CENTER);
    }

    private class EventHandler implements DdeClientEventHandler
    {
        public void error(int errorCode)
        {
            _logArea.append("Error message : error code " + new Integer(errorCode) + "\n");
        }

        public DdeResponse itemChanged(DdeItem item, byte[] data)
        {
            _logArea.append("Advise loop message : \nMyService was started " + new String(data) +
                    " seconds ago [item=" + item.getName() + ";format=" + item.getFormat() +"]\n");
            return DdeResponse.PROCESSED;
        }

        public void asyncActionComplete(DdeItem item, byte[] data, long transactionID)
        {
            if (transactionID == _transactionID)
            {
                _logArea.append("\nCurrent date [asynchronous action complete] : \n\t" + new String(data) + "\n");
            }
        }

        public void disconnect(boolean sameApplication)
        {
            _logArea.append("MyService is disconnected\n");
        }

        public void serviceRegister(String service, String instanceName)
        {
        }

        public void serviceUnregister(String service, String instanceName)
        {
        }
    }

    public static void main(String[] args) throws Exception
    {
        DdeClientSample ddeClientSample = new DdeClientSample();
        ddeClientSample.setVisible(true);
    }
}

/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.mapi;

import com.jniwrapper.*;
import com.jniwrapper.win32.Handle;
import com.jniwrapper.win32.system.Kernel32;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is designed for creating and sending email messages using MAPI.
 *
 * @author Alexey Razoryonov
 */
public class MapiMessage
{
    private static final Library LIB = new Library("mapi32");

    static
    {
        LIB.load();
    }

    private static final int MAPI_LOGON_UI = 1;
    private static final int MAPI_NEW_SESSION = 2;
    private static final int MAPI_DIALOG = 8;

    private MapiMessageStructure _mapiMessageStructure = new MapiMessageStructure();
    private List _receivers = new LinkedList();
    private List _files = new LinkedList();
    private boolean _logonUI;

    /**
     * Creates a new empty message.
     */
    public MapiMessage()
    {
    }

    /**
     * Creates a new empty message.
     *
     * @param to is the receiver of the message.
     * @throws IllegalArgumentException if the specified mail receiver is
     * of the {@link MailContact.Type#SENDER SENDER} type.
     */
    public MapiMessage(MailContact to)
    {
        addRecipient(to);
    }

    /**
     * Creates a new empty message.
     *
     * @param from specifies the sender of the message.
     * @param to   is the receiver of the message.
     *
     * @throws IllegalArgumentException if the specified mail receiver is of the {@link MailContact.Type#SENDER SENDER} type or
     *                                  the specified mail sender is not if the {@link MailContact.Type#SENDER SENDER} type.
     */
    public MapiMessage(MailContact from, MailContact to)
    {
        this(to);
        setSender(from);
    }

    /**
     * Creates a new empty message.
     *
     * @param to a list of message receivers. Each item of the list is an instance of the {@link MailContact} class.
     *
     * @throws IllegalArgumentException if one the specified mail receivers is of the {@link MailContact.Type#SENDER SENDER} type.
     */
    public MapiMessage(List to)
    {
        addRecipients(to);
    }

    /**
     * Creates a new empty message.
     *
     * @param from is the sender of the message.
     * @param to   a list of message receivers. Each item of the list is an instance of the {@link MailContact} class.
     *
     * @throws IllegalArgumentException if the one of the specified mail receivers is of the {@link MailContact.Type#SENDER SENDER} type or
     *                                  the specified mail sender is not if the {@link MailContact.Type#SENDER SENDER} type.
     */
    public MapiMessage(MailContact from, List to)
    {
        this(to);
        setSender(from);
    }

    /**
     * Sends the current message.
     *
     * @param showDialog if true, the dialog window will be shown.
     * @return true if the message was successfully sent.
     *
     * @throws MapiException 
     */
    public boolean send(boolean showDialog) throws MapiException
    {
        Function mapiSendMail = LIB.getFunction("MAPISendMail");

        UInt result = new UInt();

        fillStructure();

        long options = showDialog ? MAPI_DIALOG : MAPI_NEW_SESSION;
        if (_logonUI)
        {
            options |= MAPI_LOGON_UI;
        }

        String currentDirectory = Kernel32.getCurrentDirectory();

        mapiSendMail.invoke(result, new Parameter[]
        {
            new Handle(),
            new ULongInt(),
            new Pointer(_mapiMessageStructure),
            new Int(options),
            new ULongInt()
        });

        Kernel32.setCurrentDirectory(currentDirectory);        

        int resultCode = (int) result.getValue();

        if (resultCode != MapiException.SUCCESS && resultCode != MapiException.MAPI_E_USER_ABORT)
        {
            throw new MapiException(resultCode);
        }
        return  resultCode == MapiException.SUCCESS;
    }

    private void fillStructure()
    {
        List mapiRecipDesc = new LinkedList();
        for (int i = 0; i < _receivers.size(); i++)
        {
            MailContact mailContact = (MailContact) _receivers.get(i);
            mapiRecipDesc.add(mailContact.getStructure());
        }
        _mapiMessageStructure.setRecips(mapiRecipDesc);

        List mapiFileDesc = new LinkedList();
        for (int i = 0; i < _files.size(); i++)
        {
            File file = (File) _files.get(i);
            mapiFileDesc.add(new MapiFileDesc(file));
        }
        _mapiMessageStructure.setFiles(mapiFileDesc);
    }

    /**
     * Returns the subject of the message.
     *
     * @return the subject of the message.
     */
    public String getSubject()
    {
        return _mapiMessageStructure.getSubject();
    }

    /**
     * Specifies the subject of the message.
     *
     * @param subject is the subject of the message.
     */
    public void setSubject(String subject)
    {
        _mapiMessageStructure.setSubject(subject == null ? "" : subject);
    }

    /**
     * Returns the content of the message.
     *
     * @return the content of the message.
     */
    public String getText()
    {
        return _mapiMessageStructure.getText();
    }

    /**
     * Specifies the content of the message.
     *
     * @param text is the content of the message.
     */
    public void setText(String text)
    {
        _mapiMessageStructure.setText(text == null ? "": text);
    }

    /**
     * Returns the list of recipients of the message. Each item of the list
     * is an instance of the {@link MailContact} class.
     *
     * @return the list of the message receivers.
     */
    public List getRecipients()
    {
        return _receivers;
    }

    /**
     * Add the list of recipients of the message. Each item of the list is an instance of the {@link MailContact} class.
     * Each mail receiver cannot be of the {@link MailContact.Type#SENDER SENDER} type.
     *
     * @param recipients is the list of message receivers.
     *
     * @throws IllegalArgumentException if one the specified mail receivers is of the {@link MailContact.Type#SENDER SENDER} type.
     */
    public void addRecipients(List recipients)
    {
        for (int i = 0; i < recipients.size(); i++)
        {
            MailContact mailContact = (MailContact)recipients.get(i);
            _receivers.add(mailContact);
        }
    }

    /**
     * Adds the specified recipient to the list of the message receivers.
     * Note that <code>receiver</code> cannot be of the {@link MailContact.Type#SENDER SENDER} type.
     *
     * @param recipient is the recipient to be added.
     * @throws IllegalArgumentException if the specified mail receiver is of the {@link MailContact.Type#SENDER SENDER} type.
     */
    public void addRecipient(MailContact recipient)
    {
        if (recipient.getType().equals(MailContact.Type.SENDER))
        {
            throw new IllegalArgumentException("The specified mail receiver is of the SENDER type.");
        }
        _receivers.add(recipient);
    }

    /**
     * Removes the specified mail recipient from the list of the message receivers.
     *
     * @param recipient is the mail receiver to be removed.
     */
    public boolean removeRecipient(MailContact recipient)
    {
        return _receivers.remove(recipient);
    }

    /**
     * Returns the sender of the message.
     */
    public MailContact getSender()
    {
        return new MailContact(_mapiMessageStructure.getOriginator());
    }

    /**
     * Specifies the sender of the message.
     * Note that <code>sender</code> should be of the {@link MailContact.Type#SENDER SENDER} type.
     *
     * @param sender is the sender of the message.
     *
     * @throws IllegalArgumentException if the specified mail sender is not if the {@link MailContact.Type#SENDER SENDER} type.
     */
    public void setSender(MailContact sender)
    {
        if (!sender.getType().equals(MailContact.Type.SENDER))
        {
            throw new IllegalArgumentException("The specified mail sender is not of the SENDER type.");
        }
        _mapiMessageStructure.setOriginator(sender.getStructure());
    }

    /**
     * Returns the list of files attached to the message. Each item is an instance of the {@link java.io.File} class.
     *
     * @return the list of files attached to the message. Each item is an instance of the {@link java.io.File} class.
     */
    public List getAttachedFiles()
    {
        return _mapiMessageStructure.getFiles();
    }

    /**
     * Attaches the list of files to the message. Each item is an instance of the {@link java.io.File} class.
     *
     * @param files is the list of files to be attached to the message.
     */
    public void attachFiles(List files)
    {
        for (int i = 0; i < files.size(); i++)
        {
            File file = (File)files.get(i);
            _files.add(file);
        }
    }

    /**
     * Attaches the specified file to the message.
     *
     * @param file is the file to be attached.
     */
    public void attachFile(File file)
    {
        _files.add(file);
    }

    /**
     * Removes the specified first the message attachements.
     *
     * @param file is the file to be removed.
     */
    public boolean removeAttachedFile(File file)
    {
        return _files.remove(file);
    }

    /**
     * Returns true if the logon dialog window will appear in case the e-mail account login/password is incorrect.
     */
    public boolean isLogonUI()
    {
        return _logonUI;
    }

    /**
     * Determines if the logon dialog window will appear in case the e-mail account login/password is incorrect.
     *
     * @param logonUI if true, the logon dialog window will appear
     *                in case the e-mail account login/password is incorrect.
     */
    public void setLogonUI(boolean logonUI)
    {
        _logonUI = logonUI;
    }
}
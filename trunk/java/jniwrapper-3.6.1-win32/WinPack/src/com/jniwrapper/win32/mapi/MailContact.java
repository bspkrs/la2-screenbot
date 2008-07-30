/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.mapi;

import com.jniwrapper.util.EnumItem;

/**
 * This class represents mail contacts such as senders and recipients.
 *
 * @author Alexey Razoryonov
 */
public class MailContact
{
    private MapiRecipDesc _mapiRecipDesc = new MapiRecipDesc();

    /**
     * Creates a new instance of the MailContact class.
     *
     * @param contactType Defines the type of the mail contact such as the sender,
     *                    direct recipient, carbon copy (CC) recipient or blind carbon copy (BCC) recipient.
     */
    public MailContact(Type contactType)
    {
        _mapiRecipDesc.setRecipClass(contactType.getValue());
    }

    /**
     * Creates a new instance of the MailContact class by its type and address.
     *
     * @param contactType Defines the type of the mail contact such as the sender,
     *                    direct recipient, carbon copy (CC) recipient or blind carbon copy (BCC) recipient.
     * @param address     Is the e-mail address or the inbound name. The address format is [address type][e-mail address].
     *                    For example, SMTP:M@X.COM.
     */
    public MailContact(Type contactType, String address)
    {
        this(contactType);
        setName(address);
        if (address.indexOf("@") != -1)
        {
            setAddress(address);
        }
    }

    MailContact(MapiRecipDesc structure)
    {
        _mapiRecipDesc = structure;
    }

    MapiRecipDesc getStructure()
    {
        return _mapiRecipDesc;
    }

    /**
     * Returns the name of the contact.
     */
    public String getName()
    {
        return _mapiRecipDesc.getName();
    }

    /**
     * Sets the name of the contact.
     *
     * @param name Is the e-mail adress or the inbound name.
     */
    public void setName(String name)
    {
        _mapiRecipDesc.setName(name);
    }

    /**
     * Returns the address of the contact.
     */
    public String getAddress()
    {
        return _mapiRecipDesc.getAddress();
    }

    /**
     * Sets the address of the contact.
     *
     * @param address Is the e-mail address. The address format is [address type][e-mail address].
     *                For example, SMTP:M@X.COM.
     */
    public void setAddress(String address)
    {
        _mapiRecipDesc.setAddress(address);
    }

    /**
     * Returns the type of the contact.
     *
     * @see MailContact.Type
     */
    public MailContact.Type getType()
    {
        return new MailContact.Type(_mapiRecipDesc.getRecipClass());
    }

    /**
     * Specifies the type of the contact.
     *
     * @param type Is one of the {@link MailContact.Type} constants.
     */
    public void setType(MailContact.Type type)
    {
        _mapiRecipDesc.setRecipClass(type.getValue());
    }

    /**
     * This class specifies all possible roles of the mail contact such as the sender,
     * direct recipient, carbon copy (CC) recipient or blind carbon copy (BCC) recipient.
     */
    public static class Type extends EnumItem
    {
        /**
         * Represents the sender role of the mail contact.
         */
        public static final Type SENDER = new Type(0);

        /**
         * Represents the recipient role of the mail contact.
         */
        public static final Type RECIPIENT = new Type(1);

        /**
         * Represents the carbon copy (CC) recipient role of the mail contact.
         */
        public static final Type COPY_RECIPIENT = new Type(2);

        /**
         * Represents the blind carbon copy recipient role of the mail contact.
         */
        public static final Type BLIND_COPY_RECIPIENT = new Type(3);


        public Type(int i)
        {
            super(i);
        }
    }
}

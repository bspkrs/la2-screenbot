/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.shell;

import com.jniwrapper.*;
import com.jniwrapper.util.EnumItem;
import com.jniwrapper.util.Enums;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Represents <code>COMPONENT</code> native structure.
 */
public class ActiveDesktopItem extends Structure
{
    private static final int MAX_PATH = 260;
    private static final int INTERNET_MAX_SCHEME_LENGTH = 32;
    private static final int INTERNET_MAX_PATH_LENGTH = 2048;

    private static final int INTERNET_MAX_URL_LENGTH = INTERNET_MAX_SCHEME_LENGTH + "://\0".length() + INTERNET_MAX_PATH_LENGTH;

    private UInt32 dwSize = new UInt32();
    private UInt32 dwID = new UInt32();
    private Int iComponentType = new Int();
    private IntBool fChecked = new IntBool();
    private IntBool fDirty = new IntBool();
    private IntBool fNoScroll = new IntBool();
    private ComponentLocation cpPos = new ComponentLocation();
    private WideString wszFriendlyName = new WideString(MAX_PATH);
    private WideString wszSource = new WideString(INTERNET_MAX_URL_LENGTH);
    private WideString wszSubscribedURL = new WideString(INTERNET_MAX_URL_LENGTH);

    public static class ComponentType extends EnumItem
    {
        public static ComponentType HTMLDOC = new ComponentType(0);
        public static ComponentType PICTURE = new ComponentType(1);
        public static ComponentType WEBSITE = new ComponentType(2);
        public static ComponentType CONTROL = new ComponentType(3);
        public static ComponentType CFHTML = new ComponentType(4);


        private ComponentType(int value)
        {
            super(value);
        }
    }

    public ActiveDesktopItem()
    {
        init(new Parameter[]{dwSize, dwID, iComponentType, fChecked, fDirty, fNoScroll, cpPos, wszFriendlyName, wszSource, wszSubscribedURL});
        dwSize.setValue(getLength());
        dwID.setValue(0);
    }

    public ActiveDesktopItem(ActiveDesktopItem that)
    {
        this();
        initFrom(that);
    }

    /**
     * @return the component type
     */
    public ComponentType getComponentType()
    {
        return (ComponentType)Enums.getItem(ComponentType.class, (int)iComponentType.getValue());
    }

    /**
     * @param value the component type
     */
    public void setComponentType(ComponentType value)
    {
        iComponentType.setValue(value.getValue());
    }

    /**
     * @return Value that is set to TRUE if the component is enabled, or FALSE if it's not
     */
    public boolean getChecked()
    {
        return fChecked.getBooleanValue();
    }

    /**
     * @param checked Value that is set to TRUE if the component is enabled, or FALSE if it's not
     */
    public void setChecked(boolean checked)
    {
        fChecked.setBooleanValue(checked);
    }

    /**
     * @return Value that is set to TRUE if the component has been modified and not yet saved to disk. It will be set to FALSE if the component has not been modified, or if it has been modified and saved to disk
     */
    public boolean getDirty()
    {
        return fDirty.getBooleanValue();
    }

    /**
     * @param dirty that is set to TRUE if the component has been modified and not yet saved to disk. It will be set to FALSE if the component has not been modified, or if it has been modified and saved to disk
     */
    public void setDirty(boolean dirty)
    {
        fDirty.setBooleanValue(dirty);
    }

    /**
     * @return Value that is set to TRUE if the component is scrollable, or FALSE if not
     */
    public boolean getScroll()
    {
        return fNoScroll.getBooleanValue();
    }

    /**
     * @param scroll Value that is set to TRUE if the component is scrollable, or FALSE if not
     */
    public void setScroll(boolean scroll)
    {
        fNoScroll.setBooleanValue(scroll);
    }

    /**
     * @return {@link com.jniwrapper.win32.shell.ComponentLocation} structure containing position and size information.
     */
    public ComponentLocation getLocation()
    {
        return cpPos;
    }

    /**
     * @param location {@link com.jniwrapper.win32.shell.ComponentLocation} structure containing position and size information.
     */
    public void setLocation(ComponentLocation location)
    {
        cpPos.copyFrom(location);
    }

    /**
     * @return Component's friendly name
     */
    public String getFriendlyName()
    {
        return wszFriendlyName.getValue();
    }

    /**
     * @param friendlyName Component's friendly name
     */
    public void setFriendlyName(String friendlyName)
    {
        wszFriendlyName.setValue(friendlyName);
    }

    /**
     * @return Component's URL
     */
    public URL getSource() throws MalformedURLException
    {
        return new URL(wszSource.getValue());
    }

    /**
     * @param source Component's URL
     */
    public void setSource(URL source)
    {
        wszSource.setValue(source.toString());
    }

    /**
     * @return Subscribed URL
     */
    public URL getSubscribedURL() throws MalformedURLException
    {
        return new URL(wszSubscribedURL.getValue());
    }

    /**
     * @param subscribedURL Subscribed URL
     */
    public void setSubscribedURL(URL subscribedURL)
    {
        wszSubscribedURL.setValue(subscribedURL.toString());
    }

    public Object clone()
    {
        return new ActiveDesktopItem(this);
    }
}

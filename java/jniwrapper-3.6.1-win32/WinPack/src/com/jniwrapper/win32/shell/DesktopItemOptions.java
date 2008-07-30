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

/**
 * Represents <code>COMPONENTSOPT</code> native structure.
 */
public class DesktopItemOptions extends Structure
{
    private UInt32 dwSize = new UInt32();
    private IntBool fEnableComponents = new IntBool();
    private IntBool fActiveDesktop = new IntBool();

    public DesktopItemOptions()
    {
        init(new Parameter[]{dwSize, fEnableComponents, fActiveDesktop});
        dwSize.setValue(getLength());
    }

    public DesktopItemOptions(boolean enableComponents, boolean activeDesktop)
    {
        setEnableComponents(enableComponents);
        setActiveDesktop(activeDesktop);
    }

    public DesktopItemOptions(DesktopItemOptions that)
    {
        this();
        initFrom(that);
    }

    public boolean getEnableComponents()
    {
        return fEnableComponents.getBooleanValue();
    }

    public void setEnableComponents(boolean enableComponents)
    {
        fEnableComponents.setBooleanValue(enableComponents);
    }

    public boolean getActiveDesktop()
    {
        return fActiveDesktop.getBooleanValue();
    }

    public void setActiveDesktop(boolean activeDesktop)
    {
        fActiveDesktop.setBooleanValue(activeDesktop);
    }

    public Object clone()
    {
        return new DesktopItemOptions(this);
    }
}

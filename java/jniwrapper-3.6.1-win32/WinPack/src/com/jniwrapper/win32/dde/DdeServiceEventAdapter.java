/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.dde;

/**
 * An abstract adapter class for processing DDE service events.
 *
 * @author Vladimir Kondrashchenko
 */
public abstract class DdeServiceEventAdapter extends DdeEventAdapter implements DdeServiceEventHandler
{
    public byte[] adviseRequest(String topic, DdeItem item)
    {
        return new byte[0];
    }

    public boolean adviseStart(String topic, DdeItem item)
    {
        return false;
    }

    public void adviseStop(String topic, DdeItem item)
    {
    }

    public DdeResponse execute(String topic, String command)
    {
        return null;
    }

    public boolean beforeConnect(String topic, boolean sameApplication)
    {
        return false;
    }

    public DdeResponse pokeData(String topic, DdeItem item, byte[] data)
    {
        return null;
    }

    public byte[] requestData(String topic, DdeItem item)
    {
        return new byte[0];
    }
}

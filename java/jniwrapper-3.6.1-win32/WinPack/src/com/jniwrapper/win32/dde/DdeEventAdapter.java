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
 * An abstract adapter class for processing DDE events that are common for clients and services.
 *
 * @author Vladimir Kondrashchenko
 */
abstract class DdeEventAdapter implements DdeEventHandler
{
    public void disconnect(boolean sameApplication)
    {
    }

    public void serviceRegister(String service, String instanceName)
    {
    }

    public void serviceUnregister(String service, String instanceName)
    {
    }
}

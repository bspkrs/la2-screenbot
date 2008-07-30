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
 * The interface for processing DDE events that are common for clients and services.
 *
 * @author Vladimir Kondrashchenko
 */
interface DdeEventHandler
{
    /**
     * Occurs on disconnect.
     *
     * @param sameApplication specifies if the client and the service are the same application.
     */
    public void disconnect(boolean sameApplication);

    /**
     * Occurs when a service is registered.
     *
     * @param service is the basic name of the service.
     * @param instanceName is an instance-specific service name.
     */
    public void serviceRegister(String service, String instanceName);

    /**
     * Occurs when a service is unregistered.
     *
     * @param service is the basic name of the service.
     * @param instanceName is an instance-specific service name.
     */
    public void serviceUnregister(String service, String instanceName);
}

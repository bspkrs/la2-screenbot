/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
import com.jniwrapper.win32.service.ServiceManager;
import com.jniwrapper.win32.service.Service;

/**
 * This sample demonstrates how to use the {@link ServiceManager} class for
 * receiving the list of services from a remote computer.
 */
public class AccessServicesRemoteComputer
{
    public static void main(String[] args)
    {
        ServiceManager serviceManager = new ServiceManager();
        serviceManager.setComputerName("OLYMPUS");

        ServiceManager.AccessRights accessRights = new ServiceManager.AccessRights();
        accessRights.setEnumerateService(true);

        serviceManager.setAccess(accessRights);

        Service.AccessRights serviceAccessRights = new Service.AccessRights();
        serviceAccessRights.setQueryStatus(true);

        Service[] services = serviceManager.getServices(serviceAccessRights);

        try
        {
            for (int i = 0; i < services.length; i++)
            {
                Service service = services[i];

                String name = service.getName();
                Service.CurrentState currentState = service.getCurrentState();

                System.out.println(name + ": " + currentState);
            }
        }
        finally
        {
            for (int i = 0; i < services.length; i++)
            {
                Service service = services[i];
                service.close();
            }
        }

        serviceManager.close();
    }
}

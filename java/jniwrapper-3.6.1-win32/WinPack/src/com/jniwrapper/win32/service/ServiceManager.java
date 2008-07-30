/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.service;

import com.jniwrapper.*;
import com.jniwrapper.util.EnumItem;
import com.jniwrapper.util.FlagSet;
import com.jniwrapper.util.Logger;
import com.jniwrapper.win32.FunctionName;
import com.jniwrapper.win32.Handle;
import com.jniwrapper.win32.LastErrorException;
import com.jniwrapper.win32.system.*;

import java.io.File;
import java.util.List;
import java.util.ArrayList;

/**
 * This class represents a service manager that contains operations for working with services.
 * <ul>
 * <li>creates service</li>
 * <li>opens existing sevice</li>
 * <li>returns names of existing services</li>
 * </ul>
 * <p><b>Note:</b> Service objects obtained from this class must be closed when done.
 * <pre><code>
 *     Service service = serviceManager.open("myservice");
 * <p/>
 *     // last usage of service object
 *     service.start();
 * <p/>
 *     service.close();
 * </code></pre>
 * <p/>
 * <p>Note: Available for Windows NT, Windows 2000, Windows XP, Windows Server 2003.</p>
 *
 * @author Alexei Orischenko
 */
public class ServiceManager extends ScHandle
{
    private static final FunctionName FUNCTION_OPEB_SC_MANAGER = new FunctionName("OpenSCManager");
    private static final FunctionName FUNCTION_ENUM_SERVICES_STATUS = new FunctionName("EnumServicesStatus");

    private static final FunctionName FUNCTION_CREATE_SERVICE = new FunctionName("CreateService");
    private static final FunctionName FUNCTION_OPEN_SERVICE = new FunctionName("OpenService");

    private static Logger LOG = Logger.getInstance(ServiceManager.class);

    /**
     * Name of the computer to where create, enumerate or open services.
     */
    private String _computerName = null;

    /**
     * Access to the service manager.
     */
    private AccessRights _access = null;

    /**
     * Creates a service manager with full access.
     */
    public ServiceManager()
    {
        checkPlatform();

        _access = new AccessRights();
        _access.setAll(true);
    }

    /**
     * Returns the name of the computer to where create, enumerate or open services.
     */
    public String getComputerName()
    {
        return _computerName;
    }

    /**
     * Sets the name of the computer to where create, enumerate or open services.
     */
    public void setComputerName(String computerName)
    {
        _computerName = computerName;
    }

    /**
     * Returns access to the service manager.
     *
     * @return access to the service manager.
     * @see <a href="http://msdn.microsoft.com/library/default.asp?url=/library/en-us/dllproc/base/service_security_and_access_rights.asp">
     *      Platform SDK: DLLs, Processes, and Threads</a>
     */
    public AccessRights getAccess()
    {
        return _access;
    }

    /**
     * Sets access to the service manager.
     *
     * <p>To access services on remote computer use query status access right shown in the sample below:
     * <pre><code>
     *     ServiceManager.AccessRights accessRights = new ServiceManager.AccessRights();
     *     accessRights.setEnumerateService(true);
     *
     *     serviceManager.setAccess(accessRights);
     * </code></pre>
     * </p>
     *
     * @param access access to the service manager.
     */
    public void setAccess(AccessRights access)
    {
        _access = access;
    }

    /**
     * Creates a service with given attributes in the service database.
     * You can specify a computer where the service database resides if
     * the <code>computerName</code> property is set.
     *
     * @param serviceName service name.
     * @param displayName display name.
     * @param executable  binary path of the executable file for the service.
     * @return Service object.
     * @throws RuntimeException if fails to create a service.
     */
    public Service create(String serviceName, String displayName, File executable)
    {
        return create(serviceName,
                displayName,
                executable,
                Service.StartupType.DEMAND_START,
                null);
    }

    /**
     * Creates a service with given attributes in the service database.
     * You can specify a computer where the service database resides if
     * the <code>computerName</code> property is set.
     *
     * @param serviceName service name.
     * @param displayName display name.
     * @param executable  binary path of the executable file for the service.
     * @param startupType startup type of service.
     * @return handle to service object.
     * @throws RuntimeException if fails to create a service.
     */
    public Service create(String serviceName,
                          String displayName,
                          File executable,
                          Service.StartupType startupType)
    {
        return create(serviceName,
                displayName,
                executable,
                startupType,
                null);
    }

    /**
     * Creates a service with given attributes in the service database.
     * You can specify computer where the service database resides if
     * the <code>computerName</code> property is set.
     *
     * @param serviceName  service name.
     * @param displayName  display name.
     * @param executable   binary path of the executable file for the service.
     * @param startupType  startup type of the service.
     * @param dependencies names of services that the system must start before this service.
     * @return handle to service object.
     * @throws RuntimeException if fails to create a service.
     */
    public Service create(String serviceName,
                          String displayName,
                          File executable,
                          Service.StartupType startupType,
                          String[] dependencies)
    {
        Service.AccessRights access = new Service.AccessRights();
        access.setAll(true);

        return create(serviceName,
                displayName,
                executable,
                startupType,
                dependencies,
                access,
                Service.Type.WIN32_OWN_PROCESS,
                Service.ErrorControl.NORMAL,
                null,
                null);
    }

    /**
     * Creates a service with the given attributes in the service database.
     * You can specify a computer where the service database resides if
     * the <code>computerName</code> property is set.
     *
     * @param serviceName    service name.
     * @param displayName    display name.
     * @param executableFile executable file for the service.
     * @param startupType    startup type of the service.
     * @param dependencies   names of services that the system must start before this service.
     * @param access         access to the service.
     * @param serviceType    service type.
     * @param errorControl   error control for the service.
     * @param startAccount   Account under which the service starts:
     *                       in the form domainName\\userName or null for LocalSystem account.
     * @param password       password for start account.
     * @return handle to the service object.
     * @throws RuntimeException if fails to create service.
     */
    public Service create(String serviceName,
                          String displayName,
                          File executableFile,
                          Service.StartupType startupType,
                          String[] dependencies,
                          Service.AccessRights access,
                          Service.Type serviceType,
                          Service.ErrorControl errorControl,
                          String startAccount,
                          String password)
    {
        open();

        try
        {
            if (executableFile == null)
            {
                throw new ServiceException("Executable file is required.");
            }

            Function function = AdvApi32.getInstance().getFunction(FUNCTION_CREATE_SERVICE.toString());

            Handle handle = new Handle();

            function.invoke(handle,
                    new Parameter[]{
                        this,
                        new Str(serviceName),
                        displayName != null ? new Str(displayName) : new Str(""),
                        new UInt32(access.getFlags()),
                        new UInt32(serviceType.getValue()),
                        new UInt32(startupType.getValue()),
                        new UInt32(errorControl.getValue()),
                        new Str(executableFile.getAbsolutePath()),
                        new Handle(),
                        new Handle(),
                        dependencies != null ? (Parameter)new Pointer(new StringArray(dependencies)) : new Handle(),
                        startAccount != null ? (Parameter)new Str(startAccount) : new Handle(),
                        password != null ? (Parameter)new Str(password) : new Handle(),
                    });

            if (handle.isNull())
            {
                throw new ServiceException("Cannot create the service: " + serviceName);
            }

            Service result = new Service(serviceName);
            result.setValue(handle.getValue());

            return result;
        }
        finally
        {
            close();
        }
    }

    /**
     * Opens an existing service.
     *
     * @param serviceName service name.
     * @return handle to an existing service if succeeded; otherwise null.
     * @throws RuntimeException if the service is not found.
     */
    public Service open(String serviceName)
    {
        Service.AccessRights access = new Service.AccessRights();
        access.setAll(true);

        return open(serviceName, access);
    }

    /**
     * Opens an existing service.
     *
     * @param serviceName service name.
     * @param access      access to the service.
     * @return handle to an existing service.
     * @throws RuntimeException if the service is not found.
     */
    public Service open(String serviceName, Service.AccessRights access)
    {
        Handle handle = openService(serviceName, access);

        if (handle.isNull())
        {
            throw new ServiceException("Cannot find service: " + serviceName);
        }

        Service result = new Service(serviceName, access);
        result.setValue(handle.getValue());

        result.setOpened(true);

        return result;
    }

    /**
     * Returns available services.
     *
     * <p>Use getServices(Service.AccessRights) method for getting services on remote computer.</p>
     *
     * <p><b>Note:</b> Returned services are in open state.
     * So you should call <code>service.close()</code> for each returned service.</p>
     *
     * @return available services.
     */
    public Service[] getServices()
    {
        Service.AccessRights serviceAccessRightsAll = new Service.AccessRights();
        serviceAccessRightsAll.setAll(true);

        return getServices(serviceAccessRightsAll);
    }

    /**
     * Returns available services.
     *
     * <p>To access services on remote computer use query status access right shown in the sample below:
     * <pre><code>
     *     Service.AccessRights serviceAccessRights = new Service.AccessRights();
     *     serviceAccessRights.setQueryStatus(true);
     *     Service[] remoteServices = serviceManager.getServices(serviceAccessRights);
     * </code></pre>
     * </p>
     *
     * <p><b>Note:</b> Returned services are in open state.
     * So you should call <code>service.close()</code> for each returned service.</p>
     *
     * @param serviceAccessRight access rights for opened services
     *
     * @return available services.
     */
    public Service[] getServices(Service.AccessRights serviceAccessRight)
    {
        String[] serviceNames = getServiceNames();

        open();
        lock();

        try
        {
            List servicesList = new ArrayList();
            for (int i = 0; i < serviceNames.length; i++)
            {
                try
                {
                    String name = serviceNames[i];
                    Service service = open(name, serviceAccessRight);
                    servicesList.add(service);
                }
                catch (Exception e)
                {
                     LOG.error("Can't open service.", e);
                }
            }

            Service[] result = new Service[servicesList.size()];
            servicesList.toArray(result);
            return result;
        }
        finally
        {
            unlock();
            close();
        }
    }

    /**
     * Returns names of all registered services.
     *
     * @return service names.
     */
    public String[] getServiceNames()
    {
        open();

        try
        {
            UInt32 bytesNeeded = new UInt32();
            UInt32 servicesReturned = new UInt32();
            UInt32 resumeHandler = new UInt32();

            enumServicesStatus(new Handle(), 0, bytesNeeded, servicesReturned, resumeHandler);

            int bufferSize = (int)bytesNeeded.getValue();
            LocalMemoryBlock lpBuffer = new LocalMemoryBlock(bufferSize, MemoryAllocationAttributes.LPTR);
            String[] result;
            try
            {
                enumServicesStatus(lpBuffer, bufferSize, bytesNeeded, servicesReturned, resumeHandler);
                int countServices = (int)servicesReturned.getValue();

                ComplexArray servicesArray = new ComplexArray(new EnumServiceStatus(), countServices);
                Pointer pServices = new Pointer(servicesArray);
                lpBuffer.castTo(pServices);

                result = new String[countServices];
                for (int i = 0; i < result.length; i++)
                {
                    Parameter status = servicesArray.getElement(i);

                    result[i] = ((EnumServiceStatus)status).getServiceName();
                }
            }
            finally
            {
                lpBuffer.free();
            }

            return result;
        }
        finally
        {
            close();
        }
    }

    /**
     * Opens the service manager.
     */
    private void open()
    {
        if (isOpened())
        {
            return;
        }

        Function function = AdvApi32.getInstance().getFunction(FUNCTION_OPEB_SC_MANAGER.toString());

        long errorCode = function.invoke(this, new Parameter[]{
            _computerName != null ? (Parameter)new Str(_computerName) : new Handle(),
            new Handle(),
            new UInt32(_access.getFlags())
        });

        if (!isNull())
        {
            setOpened(true);
        }
        else
        {
            setOpened(false);
            throw new LastErrorException(errorCode, "Failed to open service manager");
        }
    }

    Handle openService(String serviceName, Service.AccessRights access)
    {
        open();

        try
        {

            Function function = AdvApi32.getInstance().getFunction(FUNCTION_OPEN_SERVICE.toString());

            Handle result = new Handle();
            function.invoke(result, new Parameter[]{
                this,
                new Str(serviceName),
                new UInt32(access.getFlags())
            });

            return result;
        }
        finally
        {
            close();
        }
    }

    private boolean enumServicesStatus(Pointer.Void lpServices,
                                       int bufferSize,
                                       UInt32 bytesNeeded,
                                       UInt32 servicesReturned,
                                       UInt32 resumeHandler)
    {
        checkOpened();

        Function function = AdvApi32.getInstance().getFunction(FUNCTION_ENUM_SERVICES_STATUS.toString());
        Bool result = new Bool();

        function.invoke(result, new Parameter[]{
            this,
            new UInt32(Service.Type.WIN32.getValue()),
            new UInt32(ServiceState.ALL.getValue()),
            lpServices,
            new UInt32(bufferSize),
            new Pointer(bytesNeeded),
            new Pointer(servicesReturned),
            new Pointer(resumeHandler)
        });

        return result.getValue();
    }

    /**
     * This class represents access to the service manager.
     */
    public static class AccessRights extends FlagSet
    {
        private static final int CONNECT = 0x0001;
        private static final int CREATE_SERVICE = 0x0002;
        private static final int ENUMERATE_SERVICE = 0x0004;
        private static final int LOCK = 0x0008;
        private static final int QUERY_LOCK_STATUS = 0x0010;
        private static final int MODIFY_BOOT_CONFIG = 0x0020;

        private static final int ALL = WinNT.STANDARD_RIGHTS_REQUIRED |
                CONNECT |
                CREATE_SERVICE |
                ENUMERATE_SERVICE |
                LOCK |
                QUERY_LOCK_STATUS |
                MODIFY_BOOT_CONFIG;


        public void setConnect(boolean val)
        {
            if (val)
            {
                add(CONNECT);
            }
            else
            {
                remove(CONNECT);
            }
        }

        public boolean isConnect()
        {
            return contains(CONNECT);
        }

        public void setCreateService(boolean val)
        {
            if (val)
            {
                add(CREATE_SERVICE);
            }
            else
            {
                remove(CREATE_SERVICE);
            }
        }

        public boolean isCreateService()
        {
            return contains(CREATE_SERVICE);
        }

        public void setEnumerateService(boolean val)
        {
            if (val)
            {
                add(ENUMERATE_SERVICE);
            }
            else
            {
                remove(ENUMERATE_SERVICE);
            }
        }

        public boolean isEnumerateService()
        {
            return contains(ENUMERATE_SERVICE);
        }

        public void setLock(boolean val)
        {
            if (val)
            {
                add(LOCK);
            }
            else
            {
                remove(LOCK);
            }
        }

        public boolean isLock()
        {
            return contains(LOCK);
        }

        public void setQueryLockStatus(boolean val)
        {
            if (val)
            {
                add(QUERY_LOCK_STATUS);
            }
            else
            {
                remove(QUERY_LOCK_STATUS);
            }
        }

        public boolean isQueryLockStatus()
        {
            return contains(QUERY_LOCK_STATUS);
        }

        public void setModifyBootConfig(boolean val)
        {
            if (val)
            {
                add(MODIFY_BOOT_CONFIG);
            }
            else
            {
                remove(MODIFY_BOOT_CONFIG);
            }
        }

        public boolean isModifyBootConfig()
        {
            return contains(MODIFY_BOOT_CONFIG);
        }

        public void setAll(boolean val)
        {
            if (val)
            {
                add(ALL);
            }
            else
            {
                remove(ALL);
            }
        }

        public boolean isAll()
        {
            return contains(ALL);
        }
    }

    private static class ServiceState extends EnumItem
    {
        public static final ServiceState ACTIVE = new ServiceState(0x00000001);
        public static final ServiceState INACTIVE = new ServiceState(0x00000002);
        public static final ServiceState ALL = new ServiceState(ACTIVE.getValue() | INACTIVE.getValue());

        ServiceState(int value)
        {
            super(value);
        }
    }

    private void checkPlatform()
    {
        VersionInfo versionInfo = new VersionInfo();
        if (versionInfo.isNT())
        {
        }
        else
        {
            String osVersion = "0x" + Long.toHexString(versionInfo.getMajor()) + Long.toHexString(versionInfo.getMinor());
            throw new ServiceException("The current platform does not support services: os version = " + osVersion);
        }
    }
}

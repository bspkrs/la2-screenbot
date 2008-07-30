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
import com.jniwrapper.util.Enums;
import com.jniwrapper.util.FlagSet;
import com.jniwrapper.win32.FunctionName;
import com.jniwrapper.win32.Handle;
import com.jniwrapper.win32.LastError;
import com.jniwrapper.win32.registry.RegistryKey;
import com.jniwrapper.win32.system.*;

/**
 * This class represents Windows service application, which is either
 * started automatically at system startup, or by a user through Services application, or programmatically.
 * <p/>
 * <p>Note: Available for Windows NT, Windows 2000, Windows XP, Windows Server 2003.</p>
 *
 * @author Alexei Orischenko
 * @see <a href="http://msdn.microsoft.com/library/default.asp?url=/library/en-us/dllproc/base/services.asp">
 *      Platform SDK: DLLs, Processes, and Threads</a>
 */
public class Service extends ScHandle
{
    public static final String ACCOUNT_LOCAL_SYSTEM = "LocalSystem";

    private static final int ONE_SECOND = 1000;
    private static final int TEN_SECONDS = 10000;

    private static final int DESCRIPTION_MAX_LENGTH = 2048;

    private static final int CONTROL_SERVICE_STOP = 0x00000001;
    private static final int CONTROL_SERVICE_PAUSE = 0x00000002;
    private static final int CONTROL_SERVICE_CONTINUE = 0x00000003;

    private static final int SERVICE_CONFIG_DESCRIPTION = 1;

    private static final FunctionName FUNCTION_START_SERVICE = new FunctionName("StartService");
    private static final String FUNCTION_DELETE_SERVICE = "DeleteService";
    private static final String FUNCTION_CONTROL_SERVICE = "ControlService";
    private static final FunctionName FUNCTION_CHANGE_SERVICE_CONFIG = new FunctionName("ChangeServiceConfig");
    private static final FunctionName FUNCTION_QUERY_SERVICE_CONFIG = new FunctionName("QueryServiceConfig");
    private static final FunctionName FUNCTION_QUERY_SERVICE_CONFIG2 = new FunctionName("QueryServiceConfig2");
    private static final String FUNCTION_QUERY_SERVICE_STATUS = "QueryServiceStatus";

    private static final String SERVICES_PATH = "SYSTEM\\CurrentControlSet\\Services\\";

    private String _name;
    private AccessRights _accessRights = new AccessRights(AccessRights.ALL);
    private String _displayName = "";
    private Type _serviceType = Type.WIN32_OWN_PROCESS;

    private boolean _isInteractive = false;
    private StartupType _startupType = StartupType.DEMAND_START;

    /**
     * Severity of the error that describes the action performed if this service failed to start.
     */
    private ErrorControl _errorControl = ErrorControl.NORMAL;

    /**
     * The absolute path to an executable file for the service.
     * <p/>
     * Note: If the path contains spaces it must be quoted: "c:\Program files\myservice.exe".
     */
    private String _binaryPath;

    /**
     * The account under which the service starts in the form:
     * <code>domainName\\userName</code> or <code>null</code>
     * for <code>LocalSystem</code> account.
     */
    private String _startAccount;

    /**
     * The password for the account or <code>null</code> for
     * the <code>LocalSystem</code> account.
     */
    private String _password;

    /**
     * Names of the services that the system must start before starting this service.
     */
    private String[] _dependencies;

    private boolean _isDataLoaded = false;

    /**
     * Creates a service in open state.
     *
     * @param name service name used for opening.
     * @param accessRights access rights to service.
     */
    protected Service(String name, AccessRights accessRights)
    {
        _name = name;
        _accessRights = accessRights;

        setOpened(true);
    }

    /**
     * Creates a service in open state.
     *
     * @param name service name used for opening
     */
    protected Service(String name)
    {
        this(name, new AccessRights(AccessRights.ALL));
    }

    /**
     * Returns the service name used for openning the service.
     * @return the service name.
     */
    public String getName()
    {
        return _name;
    }

    /**
     * Sets service name.
     *
     * @param name service name.
     */
    public void setName(String name)
    {
        _name = name;
    }

    /**
     * Returns the display name of the service.
     *
     * @return the display name.
     */
    public String getDisplayName()
    {
        loadConfiguration();
        return _displayName;
    }

    /**
     * Sets the display name.
     *
     * @param displayName display name.
     */
    public void setDisplayName(String displayName)
    {
        loadConfiguration();
        _displayName = displayName;
        saveConfiguration();
    }

    /**
     * Returns a type of the service.
     *
     * @return service type.
     */
    public Type getServiceType()
    {
        loadConfiguration();
        return _serviceType;
    }

    /**
     * Sets a type of the service.
     *
     * @param serviceType service type.
     */
    public void setServiceType(Type serviceType)
    {
        loadConfiguration();
        _serviceType = serviceType;
        saveConfiguration();
    }

    /**
     * Returns true, if the service can interact with desktop.
     *
     * @see <a href="http://msdn.microsoft.com/library/default.asp?url=/library/en-us/dllproc/base/interactive_services.asp">
     *      Platform SDK: DLLs, Processes, and Threads</a>
     *
     * @return true, if service is interactive; otherwise false.
     */
    public boolean isInteractive()
    {
        loadConfiguration();
        return _isInteractive;
    }

    /**
     * Allows / disallows interaction of the service with desktop.
     *
     * @see <a href="http://msdn.microsoft.com/library/default.asp?url=/library/en-us/dllproc/base/interactive_services.asp">
     *      Platform SDK: DLLs, Processes, and Threads</a>
     *
     * @param isInteractive if true, the service can interact with desktop; otherwise the service can't interact with desktop.
     */
    public void setInteractive(boolean isInteractive)
    {
        loadConfiguration();
        _isInteractive = isInteractive;
        saveConfiguration();
    }

    /**
     * Returns severity of the error that describes the action performed if the service failed to start:<br>
     *
     * <table>
     * <tr>
     *      <td>ErrorControl.IGNORE</td>
     *      <td>Logs error and continues startup</td>
     * </tr>
     * <tr>
     *      <td>ErrorControl.NORMAL</td>
     *      <td>Logs error, shows message box with error message and continues startup</td>
     * </tr>
     * <tr>
     *      <td>ErrorControl.SEVERE</td>
     *      <td>Logs error. If the last-known good configuration is started then continues startup
     *          else system is restarted with last-known good configuration</td>
     * </tr>
     * <tr>
     *      <td>ErrorControl.CRITICAL</td>
     *      <td>Logs error if possible. If the last-known good configuration is started then startup is failed
     *          else system is restarted with last-known good configuration</td>
     * </tr>
     * </table>
     *
     * @return severity of the error.
     */
    public ErrorControl getErrorControl()
    {
        loadConfiguration();
        return _errorControl;
    }

    /**
     * Sets severity of the error that describes the action performed if the service failed to start:<br>
     *
     * <table>
     * <tr>
     *      <td>ErrorControl.IGNORE</td>
     *      <td>Logs error and continues startup</td>
     * </tr>
     * <tr>
     *      <td>ErrorControl.NORMAL</td>
     *      <td>Logs error, shows message box with error message and continues startup</td>
     * </tr>
     * <tr>
     *      <td>ErrorControl.SEVERE</td>
     *      <td>Logs error. If the last-known good configuration is started then continues startup
     *          else system is restarted with last-known good configuration</td>
     * </tr>
     * <tr>
     *      <td>ErrorControl.CRITICAL</td>
     *      <td>Logs error if possible. If the last-known good configuration is started then startup is failed
     *          else system is restarted with last-known good configuration</td>
     * </tr>
     * </table>
     *
     * @param errorControl severity of the error.
     */
    public void setErrorControl(ErrorControl errorControl)
    {
        loadConfiguration();
        _errorControl = errorControl;
        saveConfiguration();
    }

    /**
     * Returns startup type of the service.
     *
     * @return service startup type.
     */
    public StartupType getStartupType()
    {
        loadConfiguration();
        return _startupType;
    }

    /**
     * Sets startup type of the service.
     *
     * @param startupType new startup type.
     */
    public void setStartupType(StartupType startupType)
    {
        loadConfiguration();
        _startupType = startupType;
        saveConfiguration();
    }

    /**
     * Returns the absolute path to the executable file for the service.
     * <p/>
     * Note: The path is quoted when the path contains spaces. For example: "c:\Program files\myservice.exe".
     *
     * @return absolute path to the executable file for the service.
     */
    public String getBinaryPath()
    {
        loadConfiguration();
        return _binaryPath;
    }

    /**
     * Sets the absolute path to the executable file for the service.
     * <p/>
     * Note: If the path contains spaces, it must be quoted (for example, "c:\Program files\myservice.exe").
     *
     * @param binaryPath the absolute path to the executable file for the service.
     */
    public void setBinaryPath(String binaryPath)
    {
        loadConfiguration();
        _binaryPath = binaryPath;
        saveConfiguration();
    }

    /**
     * Returns access rights for the service.
     *
     * @return access to service.
     * @see <a href="http://msdn.microsoft.com/library/default.asp?url=/library/en-us/dllproc/base/service_security_and_access_rights.asp">
     *      Platform SDK: DLLs, Processes, and Threads</a>
     */
    public AccessRights getAccessRights()
    {
        loadConfiguration();
        return _accessRights;
    }

    /**
     * Sets access rights to the service.
     *
     * @param accessRights new access rights.
     */
    public void setAccessRights(AccessRights accessRights)
    {
        loadConfiguration();
        _accessRights = accessRights;
        saveConfiguration();
    }

    /**
     * Returns names of the services that the system must start before starting this service.
     *
     * @return names of services dependencies.
     */
    public String[] getDependencies()
    {
        loadConfiguration();
        return _dependencies;
    }

    /**
     * Sets dependencies for the service.
     *
     * @param dependencies names of services that the system must start before this service.
     */
    public void setDependencies(String[] dependencies)
    {
        loadConfiguration();
        _dependencies = dependencies;
        saveConfiguration();
    }

    /**
     * Returns the account under which the service starts in the form:
     * <code>domainName\\userName</code> or <code>LocalSystem</code> for
     * <code>LocalSystem</code> account.
     *
     * @return account under which the service starts.
     */
    public String getStartAccount()
    {
        loadConfiguration();
        return _startAccount != null ? _startAccount : ACCOUNT_LOCAL_SYSTEM;
    }

    /**
     * Sets the account under which the service starts.
     *
     * @param startAccount start account for service in the form:
     * <code>domainName\\userName</code> or <code>LocalSystem</code> for
     * <code>LocalSystem</code> account.
     */
    public void setStartAccount(String startAccount)
    {
        loadConfiguration();
        if (startAccount == null)
        {
            startAccount = ACCOUNT_LOCAL_SYSTEM;
        }
        _startAccount = startAccount;
        saveConfiguration();
    }

    /**
     * @return the password for the start account.
     */
    public String getPassword()
    {
        loadConfiguration();
        return _password;
    }

    /**
     * Sets password for the start account.
     *
     * @param password account password.
     */
    public void setPassword(String password)
    {
        loadConfiguration();
        _password = password;
        saveConfiguration();
    }

    /**
     * Starts the service.
     * <p/>
     * Note: The service start can be still in progress after the method returns.
     * You can use {@link #waitState(Service.CurrentState)} after invoking this method for waiting until the service
     * finishes the start up sequence.
     */
    public void start()
    {
        start(null);
    }

    /**
     * Starts the service.
     *
     * @param parameters startup parameters.
     */
    public void start(String[] parameters)
    {
        Function function = AdvApi32.getInstance().getFunction(FUNCTION_START_SERVICE.toString());

        Bool result = new Bool();

        long errorCode = function.invoke(result, new Parameter[]{
            this,
            parameters != null ? new UInt32(parameters.length) : new UInt32(0),
            makeArgumentsArray(parameters)
        });

        checkResult(LastError.getMessage(errorCode), result);
    }

    /**
     * Stops the service.
     * <p/>
     * Note: The service stopping can be in progress after the method returns.
     * You can use {@link #waitState(Service.CurrentState)} after calling this method for waiting until the service is stopped.
     */
    public void stop()
    {
        controlService(CONTROL_SERVICE_STOP);
    }

    /**
     * Pauses the service.
     */
    public void pause()
    {
        controlService(CONTROL_SERVICE_PAUSE);
    }

    /**
     * Resumes the paused service.
     */
    public void resume()
    {
        controlService(CONTROL_SERVICE_CONTINUE);
    }

    /**
     * Returns the service status.
     *
     * @return service status.
     */
    public Status getStatus()
    {
        Function function = AdvApi32.getInstance().getFunction(FUNCTION_QUERY_SERVICE_STATUS.toString());
        Bool result = new Bool();

        Status status = new Status();
        long errorCode = function.invoke(result, this, new Pointer(status));

        checkResult(LastError.getMessage(errorCode), result);

        return status;
    }

    /**
     * Returns the current state of the service.
     *
     * @return service state.
     */
    public CurrentState getCurrentState()
    {
        Status status = getStatus();
        return status.getCurrentState();
    }

    private boolean queryServiceConfig(Handle lpServiceConfig, int bufferSize, UInt32 bytesNeeded)
    {
        Function function = AdvApi32.getInstance().getFunction(FUNCTION_QUERY_SERVICE_CONFIG.toString());

        Bool result = new Bool();

        function.invoke(result, new Parameter[]{
            this,
            lpServiceConfig,
            new UInt(bufferSize),
            new Pointer(bytesNeeded)
        });

        return result.getValue();
    }

    /**
     * Loads the configuration of the service.
     */
    private void loadConfiguration()
    {
        if (_isDataLoaded)
        {
            return;
        }

        UInt32 bytesNeeded = new UInt32();

        queryServiceConfig(new Handle(), 0, bytesNeeded);
        int bufferSize = (int)bytesNeeded.getValue();

        LocalMemoryBlock lpBuffer = new LocalMemoryBlock(bufferSize, MemoryAllocationAttributes.LPTR);

        try
        {
            if (!queryServiceConfig(lpBuffer, bufferSize, bytesNeeded))
            {
                throw new ServiceException("Can not query status.");
            }

            ServiceConfig serviceConfig = new ServiceConfig();
            Pointer pServiceConfig = new Pointer(serviceConfig);
            lpBuffer.castTo(pServiceConfig);

            _displayName = serviceConfig.getDisplayName();

            _serviceType = Type.create(serviceConfig.getServiceType());
            _isInteractive = Type.isInteractive(serviceConfig.getServiceType());
            _startupType = StartupType.create(serviceConfig.getStartupType());
            _errorControl = ErrorControl.create(serviceConfig.getErrorControl());

            _binaryPath = serviceConfig.getBinaryPath();

            _startAccount = serviceConfig.getServiceStartName();

            _dependencies = serviceConfig.getDependencies();

            _password = null;
        }
        finally
        {
            lpBuffer.free();
        }
    }

    /**
     * Returns description for the service.
     * This method returns the empty string when running under Windows NT as
     * the corresponding OS function is not available.
     *
     * @return service description, <code>""</code> when running under Windows NT.
     */
    public String getDescription()
    {
        Function function;
        try
        {
            function = AdvApi32.getInstance().getFunction(FUNCTION_QUERY_SERVICE_CONFIG2.toString());
        }
        catch (NoSuchFunctionException e)
        {
            return "";
        }

        int charSize = PlatformContext.isUnicode() ? PlatformContext.getWideCharLength() : PlatformContext.getCharLength();

        final int numBytes = DESCRIPTION_MAX_LENGTH * charSize;
        LocalMemoryBlock buffer = new LocalMemoryBlock(numBytes, MemoryAllocationAttributes.LPTR);

        try
        {
            Bool res = new Bool();
            long errorCode = function.invoke(res, new Parameter[] {
                this,
                new UInt32(SERVICE_CONFIG_DESCRIPTION),
                buffer,
                new UInt32(numBytes),
                new Pointer(new UInt32())
            });

            checkResult(LastError.getMessage(errorCode), res);

            Handle description = new Handle();
            Pointer pDescription = new Pointer(description);
            buffer.castTo(pDescription);

            if (description.isNull())
            {
                return "";
            }
            Str result = new Str(DESCRIPTION_MAX_LENGTH);
            Pointer pResult = new Pointer(result);
            description.castTo(pResult);

            return result.getValue();
        }
        finally
        {
            buffer.free();
        }
    }

    /**
     * Saves configuration of the service.
     */
    private void saveConfiguration()
    {
        Function function = AdvApi32.getInstance().getFunction(FUNCTION_CHANGE_SERVICE_CONFIG.toString());

        Bool result = new Bool();
        int serviceType = _isInteractive ? _serviceType.getValue() | Type.INTERACTIVE_PROCESS : _serviceType.getValue();

        long errorCode = function.invoke(result, new Parameter[]{
            this,
            new UInt32(serviceType),
            new UInt32(_startupType.getValue()),
            new UInt32(_errorControl.getValue()),
            new Str(_binaryPath),
            new Handle(),
            new Handle(),
            _dependencies != null && _dependencies.length > 0 ? (Parameter)new Pointer(new StringArray(_dependencies)) : new Handle(),
            _startAccount != null ? (Parameter)new Str(_startAccount) : new Handle(),
            _password != null ? (Parameter)new Str(_password) : new Handle(),
            _displayName != null ? new Str(_displayName) : new Str(""),
        });

        checkResult(LastError.getMessage(errorCode), result);
    }

    /**
     * Marks the service as deleted. The service is not deleted until all service handles have been closed by
     * the close call.
     */
    public void delete()
    {
        Function function = AdvApi32.getInstance().getFunction(FUNCTION_DELETE_SERVICE);

        Bool result = new Bool();
        long errorCode = function.invoke(result, this);

        checkResult(LastError.getMessage(errorCode), result);
    }

    private Parameter makeArgumentsArray(String[] args)
    {
        if (args != null && args.length > 0)
        {
            int length = args.length;
            Parameter params[] = new Parameter[length];
            for (int i = 0; i < length; i++)
            {
                Str str = new Str(args[i]);
                params[i] = new Pointer(str);
            }
            PrimitiveArray result = new PrimitiveArray(params);
            return new Pointer(result);
        }
        else
        {
            return new Handle();
        }
    }

    private Status controlService(int dwControl)
    {
        Function function = AdvApi32.getInstance().getFunction(FUNCTION_CONTROL_SERVICE);

        Bool result = new Bool();
        Status serviceStatus = new Status();

        long errorCode = function.invoke(result, new Parameter[]{
            this,
            new UInt32(dwControl),
            new Pointer(serviceStatus)
        });

        checkResult(LastError.getMessage(errorCode), result);

        return serviceStatus;
    }

    private static boolean inState(CurrentState[] expectedStates, CurrentState currentState)
    {
        for (int i = 0; i < expectedStates.length; i++)
        {
            CurrentState expectedState = expectedStates[i];
            if (expectedState.equals(currentState))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Periodically checks the current state of the service and returns from the method
     * when the current state is equal to the expected state.
     *
     * @param expectedState expected states
     * @throws InterruptedException
     */
    public void waitState(CurrentState expectedState) throws InterruptedException
    {
        waitState(new CurrentState[]{expectedState}, null);
    }

    /**
     * Periodically checks the current state of the service and returns from the method
     * when the current state is equal to the expected state.
     *
     * @param expectedState expected states
     * @param listener      listener that notified when is acquired current state of service
     * @throws InterruptedException
     */
    public void waitState(CurrentState expectedState, StatusListener listener) throws InterruptedException
    {
        waitState(new CurrentState[]{expectedState}, listener);
    }

    /**
     * Periodically checks the current state of the service and returns from the method
     * when the current state is equal to one of the expected states.
     *
     * @param expectedStates expected states
     * @throws InterruptedException
     */
    public void waitState(CurrentState[] expectedStates) throws InterruptedException
    {
        waitState(expectedStates, null);
    }

    /**
     * Periodically checks the current state of the service and returns from the method
     * when the current state is equal to one of the expected states.
     *
     * @param expectedStates expected states
     * @param listener       listener that notified when is acquired current state of service
     * @throws InterruptedException
     */
    public void waitState(CurrentState[] expectedStates, StatusListener listener) throws InterruptedException
    {
        for (Status status = getStatus();
             !inState(expectedStates, status.getCurrentState());
             status = getStatus())
        {
            if (listener != null)
            {
                listener.statusChecked(status);
            }

            long waitTime = status.getWaitHint() / 10;
            if (waitTime < ONE_SECOND)
                waitTime = ONE_SECOND;
            if (waitTime > TEN_SECONDS)
                waitTime = TEN_SECONDS;

            Thread.sleep(waitTime);
        }
    }

    /**
     * Creates parameters subkey in registry. This key can be used for storing service settings:
     * <pre><code>
     *     RegistryKey parametersKey = service.createParametersKey();
     *     parametersKey.values().put("logfile", "D:/tomcat/logs/error.error");
     *     parametersKey.close();
     * </code></pre>
     * <p/>
     * <p>Location of created subkey:
     * <pre><code>
     *     HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\service_name
     * </code></pre>
     *
     * @return created parameters subkey in registry.
     */
    public RegistryKey createParametersKey()
    {
        final String keyPath = SERVICES_PATH + getName();
        final String keyName = "Parameters";
        RegistryKey registryKey = RegistryKey.LOCAL_MACHINE.openSubKey(keyPath);
        RegistryKey result = registryKey.createSubKey(keyName, true);
        registryKey.close();

        return result;
    }

    public static interface StatusListener
    {
        public void statusChecked(Status status);
    }


    private static class ServiceEnumItem extends EnumItem
    {
        private String _stringValue;

        ServiceEnumItem(int value, String stringValue)
        {
            super(value);
            _stringValue = stringValue;
        }

        public String toString()
        {
            return _stringValue;
        }
    }

    /**
     * This class represents the current state of the service.
     */
    public static class CurrentState extends ServiceEnumItem
    {
        public static final CurrentState STOPPED = new CurrentState(0x00000001, "Stopped");
        public static final CurrentState START_PENDING = new CurrentState(0x00000002, "Starting...");
        public static final CurrentState STOP_PENDING = new CurrentState(0x00000003, "Stopping...");
        public static final CurrentState RUNNING = new CurrentState(0x00000004, "Running");
        public static final CurrentState CONTINUE_PENDING = new CurrentState(0x00000005, "Resuming...");
        public static final CurrentState PAUSE_PENDING = new CurrentState(0x00000006, "Pausing...");
        public static final CurrentState PAUSED = new CurrentState(0x00000007, "Paused");

        private CurrentState(int value, String stringValue)
        {
            super(value, stringValue);
        }

        static CurrentState create(int type)
        {
            return (CurrentState)Enums.getItem(CurrentState.class, type);
        }
    }

    /**
     * This class represents access rights to a service.
     */
    public static class AccessRights extends FlagSet
    {
        private static final int QUERY_CONFIG = 0x0001;
        private static final int CHANGE_CONFIG = 0x0002;
        private static final int QUERY_STATUS = 0x0004;
        private static final int ENUMERATE_DEPENDENTS = 0x0008;
        private static final int START = 0x0010;
        private static final int STOP = 0x0020;
        private static final int PAUSE_CONTINUE = 0x0040;
        private static final int INTERROGATE = 0x0080;
        private static final int USER_DEFINED_CONTROL = 0x0100;

        private static final int ALL =
                WinNT.STANDARD_RIGHTS_REQUIRED |
                QUERY_CONFIG |
                CHANGE_CONFIG |
                QUERY_STATUS |
                ENUMERATE_DEPENDENTS |
                START |
                STOP |
                PAUSE_CONTINUE |
                INTERROGATE |
                USER_DEFINED_CONTROL;

        public AccessRights()
        {
        }

        public AccessRights(long flags)
        {
            super(flags);
        }

        public void setQueryConfig(boolean queryConfig)
        {
            setupFlag(QUERY_CONFIG, queryConfig);
        }

        public boolean isQueryConfig()
        {
            return contains(QUERY_CONFIG);
        }

        public void setChangeConfig(boolean changeConfig)
        {
            setupFlag(CHANGE_CONFIG, changeConfig);
        }

        public boolean isChangeConfig()
        {
            return contains(CHANGE_CONFIG);
        }

        public void setQueryStatus(boolean queryStatus)
        {
            setupFlag(QUERY_STATUS, queryStatus);
        }

        public boolean isQueryStatus()
        {
            return contains(QUERY_STATUS);
        }

        public void setEnumerateDependents(boolean enumerateDependents)
        {
            setupFlag(ENUMERATE_DEPENDENTS, enumerateDependents);
        }

        public boolean isEnumerateDependents()
        {
            return contains(ENUMERATE_DEPENDENTS);
        }

        public void setStart(boolean start)
        {
            setupFlag(START, start);
        }

        public boolean isStart()
        {
            return contains(START);
        }

        public void setStop(boolean stop)
        {
            setupFlag(STOP, stop);
        }

        public boolean isStop()
        {
            return contains(STOP);
        }

        public void setPauseContinue(boolean pauseContinue)
        {
            setupFlag(PAUSE_CONTINUE, pauseContinue);
        }

        public boolean isPauseContinue()
        {
            return contains(PAUSE_CONTINUE);
        }

        public void setInterrogate(boolean interrogate)
        {
            setupFlag(INTERROGATE, interrogate);
        }

        public boolean isInterrogate()
        {
            return contains(INTERROGATE);
        }

        public void setUserDefinedControl(boolean userDefinedControl)
        {
            setupFlag(USER_DEFINED_CONTROL, userDefinedControl);
        }

        public boolean isUserDefinedControl()
        {
            return contains(USER_DEFINED_CONTROL);
        }

        public void setAll(boolean all)
        {
            setupFlag(ALL, all);
        }

        public boolean isAll()
        {
            return contains(ALL);
        }
    }

    /**
     * This class represents type of the service.
     */
    public static class Type extends ServiceEnumItem
    {
        /**
         * Driver service.
         */
        public static final Type KERNEL_DRIVER = new Type(0x00000001, "Kernel driver");

        /**
         * File system driver service.
         */
        public static final Type FILE_SYSTEM_DRIVER = new Type(0x00000002, "File system driver");

        /**
         * Service running in its own process.
         */
        public static final Type WIN32_OWN_PROCESS = new Type(0x00000010, "Win32 own process");

        /**
         * Service shares the process with other services.
         */
        public static final Type WIN32_SHARE_PROCESS = new Type(0x00000020, "Win32 share process");

        /**
         * Service can interact with desktop.
         */
        private static final int INTERACTIVE_PROCESS = 0x00000100;

        public static final Type WIN32 = new Type(WIN32_OWN_PROCESS.getValue() | WIN32_SHARE_PROCESS.getValue(), "Win32 process");

        private Type(int value, String stringValue)
        {
            super(value, stringValue);
        }

        public static Type create(int type)
        {
            FlagSet flagSet = new FlagSet(type);
            flagSet.remove(INTERACTIVE_PROCESS);
            type = (int) flagSet.getFlags();

            return (Type)Enums.getItem(Type.class, type);
        }

        public static boolean isInteractive(int type)
        {
            return (type & INTERACTIVE_PROCESS) != 0;
        }
    }

    /**
     * This class represents startup type of service.
     */
    public static class StartupType extends ServiceEnumItem
    {
        /**
         * Device driver is started by the IoInitSystem function.
         */
        public static final StartupType BOOT_START = new StartupType(0x00000000, "Boot start");

        /**
         * Device driver is started by the system loader.
         */
        public static final StartupType SYSTEM_START = new StartupType(0x00000001, "System start");

        /**
         * Service is started at system startup.
         */
        public static final StartupType AUTO_START = new StartupType(0x00000002, "Automatic");

        /**
         * Service is started by the user in Controls application or programmatically.
         */
        public static final StartupType DEMAND_START = new StartupType(0x00000003, "Manual");

        /**
         * Service is disabled.
         */
        public static final StartupType DISABLED = new StartupType(0x00000004, "Disabled");

        public StartupType(int value, String stringValue)
        {
            super(value, stringValue);
        }

        public static StartupType create(int type)
        {
            return (StartupType)Enums.getItem(StartupType.class, type);
        }
    }

    public static class ErrorControl extends ServiceEnumItem
    {
        /**
         * The startup program logs error only.
         */
        public static final ErrorControl IGNORE = new ErrorControl(0x00000000, "Ignore");

        /**
         * The startup program logs error and displays a message box with the error message.
         */
        public static final ErrorControl NORMAL = new ErrorControl(0x00000001, "Normal");

        public static final ErrorControl SEVERE = new ErrorControl(0x00000002, "Severe");
        public static final ErrorControl CRITICAL = new ErrorControl(0x00000003, "Critical");

        private ErrorControl(int value, String stringValue)
        {
            super(value, stringValue);
        }

        public static ErrorControl create(int type)
        {
            return (ErrorControl)Enums.getItem(ErrorControl.class, type);
        }
    }

    /**
     * This class represents the service status.
     *
     * @author Alexei Orischenko
     */
    public static class Status extends Structure
    {
        private UInt32 _serviceType = new UInt32();
        private UInt32 _currentState = new UInt32();
        private UInt32 _controlsAccepted = new UInt32();
        private UInt32 _win32ExitCode = new UInt32();
        private UInt32 _serviceSpecificExitCode = new UInt32();
        private UInt32 _checkPoint = new UInt32();
        private UInt32 _waitHint = new UInt32();

        public Status(Status that)
        {
            _serviceType = (UInt32)that._serviceType.clone();
            _currentState = (UInt32)that._currentState.clone();
            _controlsAccepted = (UInt32)that._controlsAccepted.clone();
            _win32ExitCode = (UInt32)that._win32ExitCode.clone();
            _serviceSpecificExitCode = (UInt32)that._serviceSpecificExitCode.clone();
            _checkPoint = (UInt32)that._checkPoint.clone();
            _waitHint = (UInt32)that._waitHint.clone();

            init();
        }

        public Status()
        {
            init();
        }

        private void init()
        {
            init(new Parameter[]{
                _serviceType, _currentState, _controlsAccepted,
                _win32ExitCode, _serviceSpecificExitCode,
                _checkPoint, _waitHint
            });
        }

        /**
         * Returns type of the service.
         *
         * @return service type.
         */
        public Type getServiceType()
        {
            final Type type = (Type)Enums.getItem(Type.class, (int)_serviceType.getValue());
            return type;
        }

        /**
         * Sets type of the service.
         *
         * @param serviceType type of service.
         */
        public void setServiceType(Type serviceType)
        {
            _serviceType.setValue(serviceType.getValue());
        }

        /**
         * Returns the current state of the service.
         *
         * @return current state of service.
         */
        public Service.CurrentState getCurrentState()
        {
            return (Service.CurrentState.create((int)_currentState.getValue()));
        }

        /**
         * Returns control codes the service accepts and processes in its handler function.
         *
         * @return control codes accepted by the service.
         */
        public ControlsAccepted getControlsAccepted()
        {
            return new ControlsAccepted(_controlsAccepted.getValue());
        }

        /**
         * Returns error code the service uses to report an error that occurs when it is starting or stopping.
         *
         * @return error code of the service.
         */
        public long getWin32ExitCode()
        {
            return _win32ExitCode.getValue();
        }

        /**
         * Returns service-specific error code that the service returns when an error occurs while the service is starting or stopping.
         *
         * @return exit code of the service if an error occurred.
         */
        public long getServiceSpecificExitCode()
        {
            return _serviceSpecificExitCode.getValue();
        }

        /**
         * Returns check-point value the service increments periodically to report its progress during a lengthy start, stop, pause, or continue operation.
         *
         * @return value that indicates start, stop or pause progress.
         */
        public long getCheckPoint()
        {
            return _checkPoint.getValue();
        }

        /**
         * Returns estimated time required for a pending start, stop, pause, or continue operation, in milliseconds.
         *
         * @return estimated start, stop, pause time in milliseconds.
         */
        public long getWaitHint()
        {
            return _waitHint.getValue();
        }

        public Object clone()
        {
            return new Status(this);
        }

        public static class ControlsAccepted extends FlagSet
        {
            private static final int SERVICE_ACCEPT_STOP = 0x00000001;
            private static final int SERVICE_ACCEPT_PAUSE_CONTINUE = 0x00000002;
            private static final int SERVICE_ACCEPT_SHUTDOWN = 0x00000004;
            private static final int SERVICE_ACCEPT_PARAMCHANGE = 0x00000008;
            private static final int SERVICE_ACCEPT_NETBINDCHANGE = 0x00000010;
            private static final int SERVICE_ACCEPT_HARDWAREPROFILECHANGE = 0x00000020;
            private static final int SERVICE_ACCEPT_POWEREVENT = 0x00000040;
            private static final int SERVICE_ACCEPT_SESSIONCHANGE = 0x00000080;

            public ControlsAccepted(long flags)
            {
                super(flags);
            }

            public ControlsAccepted()
            {
            }

            public boolean isAcceptStop()
            {
                return contains(SERVICE_ACCEPT_STOP);
            }

            public boolean isAcceptPauseContinue()
            {
                return contains(SERVICE_ACCEPT_PAUSE_CONTINUE);
            }

            public boolean isAcceptShutdown()
            {
                return contains(SERVICE_ACCEPT_SHUTDOWN);
            }

            public boolean isAcceptParamChange()
            {
                return contains(SERVICE_ACCEPT_PARAMCHANGE);
            }

            public boolean isAcceptNetBindChange()
            {
                return contains(SERVICE_ACCEPT_NETBINDCHANGE);
            }

            public boolean isAcceptHardwareProfileChange()
            {
                return contains(SERVICE_ACCEPT_HARDWAREPROFILECHANGE);
            }

            public boolean isAcceptPowerEvent()
            {
                return contains(SERVICE_ACCEPT_POWEREVENT);
            }

            public boolean isAcceptSessionChange()
            {
                return contains(SERVICE_ACCEPT_SESSIONCHANGE);
            }

            public void setAcceptStop(boolean isAccept)
            {
                setupFlag(SERVICE_ACCEPT_STOP, isAccept);
            }

            public void setAcceptPauseContinue(boolean isAccept)
            {
                setupFlag(SERVICE_ACCEPT_PAUSE_CONTINUE, isAccept);
            }

            public void setAcceptShutdown(boolean isAccept)
            {
                setupFlag(SERVICE_ACCEPT_SHUTDOWN, isAccept);
            }

            public void setAcceptParamChange(boolean isAccept)
            {
                setupFlag(SERVICE_ACCEPT_PARAMCHANGE, isAccept);
            }

            public void setAcceptNetBindChange(boolean isAccept)
            {
                setupFlag(SERVICE_ACCEPT_NETBINDCHANGE, isAccept);
            }

            public void setAcceptHardwareProfileChange(boolean isAccept)
            {
                setupFlag(SERVICE_ACCEPT_HARDWAREPROFILECHANGE, isAccept);
            }

            public void setAcceptPowerEvent(boolean isAccept)
            {
                setupFlag(SERVICE_ACCEPT_POWEREVENT, isAccept);
            }

            public void setAcceptSessionChange(boolean isAccept)
            {
                setupFlag(SERVICE_ACCEPT_SESSIONCHANGE, isAccept);
            }
        }
    }
}

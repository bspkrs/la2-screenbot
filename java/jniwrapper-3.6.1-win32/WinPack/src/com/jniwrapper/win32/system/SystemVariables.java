/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.system;

import com.jniwrapper.*;
import com.jniwrapper.win32.FunctionName;
import com.jniwrapper.win32.LastErrorException;
import com.jniwrapper.win32.Msg;
import com.jniwrapper.win32.registry.RegistryKey;
import com.jniwrapper.win32.ui.User32;
import com.jniwrapper.win32.ui.Wnd;

/**
 * This class allows managing system environment variables.
 * 
 * @author Serge Piletsky
 */
public class SystemVariables implements EnvironmentVariables
{
    private static final FunctionName FUNCTION_SendMessageTimeout = new FunctionName("SendMessageTimeout");
    private static final int HWND_BROADCAST = 0xffff;

    private RegistryKey _registryKey;

    public SystemVariables()
    {
        super();
        _registryKey = RegistryKey.LOCAL_MACHINE.openSubKey("System\\CurrentControlSet\\Control\\Session Manager\\Environment", true);
    }

    /**
     * Returns the value of the specified variable from the environment block of
     * the calling process.
     * 
     * @param variable name of the variable to get the value for.
     * @return value for the variable.
     */
    public String getValue(String variable)
    {
        return (String)_registryKey.values().get(variable);
    }

    /**
     * Sets the value of an environment variable for the current process.
     * 
     * @param variable variable name.
     * @param value variable value.
     */
    public void setValue(String variable, String value)
    {
        _registryKey.values().put(variable, value);
        fireSettingChanged();
    }

    /**
     * Sends a broadcast message saying that a variable is changed.
     */
    private void fireSettingChanged()
    {
        final Function function = User32.getInstance().getFunction(FUNCTION_SendMessageTimeout.toString());
        LongInt functionResult = new LongInt();
        long errorCode = function.invoke(functionResult, new Parameter[]
        {
            new Wnd(HWND_BROADCAST),
            new UInt(Msg.WM_SETTINGCHANGE),
            new UInt32(0), // WPARAM
            new UInt32(0), // LPARAM
            new UInt32(), // Flags
            new UInt32(1000), // Timeout
            new Pointer(new UInt32())
        });

        if (functionResult.getValue() == 0)
            throw new LastErrorException(errorCode);
    }

    /**
     * Verifies if there is a specified variable in the environment.
     * 
     * @param variable variable name.
     * @return true, if the specified variable exists in the environment;
     * otherwise false.
     */
    public boolean contains(String variable)
    {
        return _registryKey.values().containsKey(variable);
    }

    /**
     * Returns an array of variable names.
     * 
     * @return an array of variable names.
     */
    public String[] getVariableNames()
    {
        final Object[] objects = _registryKey.values().keySet().toArray();
        String[] result = new String[objects.length];
        for (int i = 0; i < objects.length; i++)
            result[i] = (String)objects[i];
        return result;
    }
}

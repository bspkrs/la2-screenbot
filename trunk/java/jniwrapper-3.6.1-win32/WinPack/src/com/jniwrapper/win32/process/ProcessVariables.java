/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.process;

import com.jniwrapper.*;
import com.jniwrapper.util.Logger;
import com.jniwrapper.win32.FunctionName;
import com.jniwrapper.win32.Handle;
import com.jniwrapper.win32.LastErrorException;
import com.jniwrapper.win32.system.EnvironmentVariables;
import com.jniwrapper.win32.system.Kernel32;

import java.util.*;

/**
 * This class allows managing environment variables for a process.
 * 
 * @author Serge Piletsky
 */
public class ProcessVariables extends Handle implements EnvironmentVariables
{
    private static final Logger LOG = Logger.getInstance(ProcessVariables.class);
    
    private static final FunctionName FUNCTION_GetEnvironmentStrings = new FunctionName("GetEnvironmentStrings", false);
    private static final FunctionName FUNCTION_GetEnvironmentVariable = new FunctionName("GetEnvironmentVariable");
    private static final FunctionName FUNCTION_SetEnvironmentVariable = new FunctionName("SetEnvironmentVariable");
    private static final FunctionName FUNCTION_FreeEnvironmentStrings = new FunctionName("FreeEnvironmentStrings");

    private Pointer _stringArrayPtr;

    /**
     * Create an instance of the <code>ProcessVariables</code> class,
     * which contains all environment variables of the current process.
     */
    public ProcessVariables()
    {
        readStrings();
        registerResource();
    }

    /**
     * Allocates new Environment variables.
     *
     * @param variables
     */
    public ProcessVariables(String[] variables)
    {
        StringArray stringArray = new StringArray(variables);
        _stringArrayPtr = new Pointer(stringArray);
        _stringArrayPtr.castTo(this);
        registerResource();
    }

    protected void registerResource()
    {
        NativeResourceCollector.getInstance().addNativeResource(this, new EnvironmentVariablesResource(getValue()));
    }

    /**
     * Reads Environment variables.
     */
    private void readStrings()
    {
        final Function function = Kernel32.getInstance().getFunction(FUNCTION_GetEnvironmentStrings.toString());
        function.invoke(this);
    }

    /**
     * Returns the strings containing environment variables in key=value format
     * for the calling process.
     *
     * @return environment variables.
     */
    public String[] getStrings()
    {
        ExternalStringArray stringArray = new ExternalStringArray(this);
        return stringArray.getStrings();
    }

    /**
     * Returns a map of the environment variables for the current process.
     *
     * @return environment variables.
     */
    public Map toMap()
    {
        readStrings();
        Map result = new HashMap();
        String[] values = getStrings();
        for (int i = 0; i < values.length; i++)
        {
            StringTokenizer tokenizer = new StringTokenizer(values[i], "=");
            result.put(tokenizer.nextToken(), tokenizer.nextToken());
        }
        return result;
    }

    /**
     * Returns the value of the specified variable from the environment block of
     * the calling process.
     *
     * @param variable name of the variable to get the value for.
     * @return a value for the variable.
     */
    public String getValue(String variable)
    {
        final Function function = Kernel32.getInstance().getFunction(FUNCTION_GetEnvironmentVariable.toString());
        final Str varName = new Str(variable);
        final UInt32 length = new UInt32();
        Str result = new Str();
        long errorCode = function.invoke(length, varName, new Pointer.OutOnly(result), new UInt32(result.getMaxLength()));

        if (length.getValue() == 0)
        {
            throw new LastErrorException(errorCode);
        }

        final int requiredLength = (int)length.getValue();
        if (requiredLength > result.getMaxLength())
        {
            result = new Str(requiredLength);
            errorCode = function.invoke(length, varName, new Pointer.OutOnly(result), new UInt32(requiredLength));
        }
        if (length.getValue() == 0)
        {
            throw new LastErrorException(errorCode);
        }

        return result.getValue();
    }

    /**
     * Sets the value of an environment variable for the current process.
     *
     * @param variable variable name.
     * @param value variable value.
     */
    public void setValue(String variable, String value)
    {
        final Function function = Kernel32.getInstance().getFunction(FUNCTION_SetEnvironmentVariable.toString());
        Bool functionResult = new Bool();
        long errorCode = function.invoke(functionResult, new Str(variable), new Str(value));

        if (!functionResult.getValue())
        {
            throw new LastErrorException(errorCode);
        }
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
        return toMap().containsKey(variable);
    }

    /**
     * Returns an array of variable names.
     *
     * @return an array of variable names.
     */
    public String[] getVariableNames()
    {
        final Map map = toMap();
        List names = new LinkedList();
        for (Iterator i = map.keySet().iterator(); i.hasNext();)
        {
            names.add(i.next());
        }
        return (String[])names.toArray(new String[names.size()]);
    }

    /**
     * Frees a block of environment strings.
     */
    public void freeEnvironmentStrings()
    {
        try
        {
            freeEnvironmentStrings(this);
        }
        catch (Throwable e)
        {
            LOG.error("", e);
        }
    }

    private static void freeEnvironmentStrings(Handle environmentBlock)
    {
        if (!environmentBlock.isNull())
        {
            final Function function = Kernel32.getInstance().getFunction(FUNCTION_FreeEnvironmentStrings.toString());
            Bool functionResult = new Bool();
            long errorCode = function.invoke(functionResult, environmentBlock);

            if (!functionResult.getValue())
            {
                throw new LastErrorException(errorCode);
            }
        }
    }

    /**
     * This class is responsible for destroying a native resource when the
     * instance is collected by garbage-collector.
     */
    protected static class EnvironmentVariablesResource implements NativeResource
    {
        private long _handle;

        protected EnvironmentVariablesResource(long handle)
        {
            _handle = handle;
        }

        /**
         * Frees a block of environment strings.
         *
         * @exception Throwable
         */
        public void release() throws Throwable
        {
            freeEnvironmentStrings(new Handle(_handle));
        }
    }
}

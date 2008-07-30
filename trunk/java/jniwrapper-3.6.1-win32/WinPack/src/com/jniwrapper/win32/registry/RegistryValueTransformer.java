/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.registry;

import com.jniwrapper.*;
import com.jniwrapper.win32.system.Kernel32;

import java.util.*;

/**
 * This interface provides functions for transforming Java or JNIWrapper types
 * to registry types and vice versa.
 *
 * @author Serge Piletsky
 */
public interface RegistryValueTransformer
{
    /**
     * Converts standard Java or JNIWrapper type to registry type.
     *
     * @param value to be converted.
     * @return a converted value.
     */
    public Parameter toRegistryValue(Object value);

    /**
     * Converts a specific registry type to Java type.
     *
     * @param value to be converted.
     * @return a converted value.
     */
    public Object fromRegistryValue(Parameter value);

    /**
     * Creates a registry type parameter.
     *
     * @param size of the parameter.
     * @return registry parameter.
     */
    public Parameter createRegistryValueParameter(int size);

    /**
     * Checks if the transformer supports a specified type.
     *
     * @param value an object to check.
     * @return true if the transformer supports a specified type; false if
     *         otherwise.
     */
    public boolean isTypeSupported(Object value);

    /**
     * Default string transformer. Performs transfering string to/from the
     * registry.
     */
    public static RegistryValueTransformer STRING_TRANSFORMER = new RegistryValueTransformer()
    {
        public Parameter toRegistryValue(Object value)
        {
            Parameter result;
            if (value instanceof String)
            {
                result = new Str((String)value);
            }
            else if (value instanceof StringParameter)
            {
                result = (Parameter)value;
            }
            else
                throw new IllegalArgumentException("Not a string parameter");
            return result;
        }

        public Object fromRegistryValue(Parameter value)
        {
            return ((StringParameter)value).getValue();
        }

        public Parameter createRegistryValueParameter(int size)
        {
            return new Str("", size);
        }

        public boolean isTypeSupported(Object value)
        {
            return (value instanceof String) || (value instanceof StringParameter);
        }
    };

    /**
     * Default integer transformer. Performs transfering of integer values
     * to/from the registry.
     */
    public static RegistryValueTransformer INTEGER_TRANSFORMER = new RegistryValueTransformer()
    {
        public Parameter toRegistryValue(Object value)
        {
            Parameter result;
            if (value instanceof Number)
            {
                result = new UInt32(((Number)value).longValue());
            }
            else if (value instanceof IntegerParameter)
            {
                result = (Parameter)value;
            }
            else
                throw new IllegalArgumentException("Not an integer parameter");
            return result;
        }

        public Object fromRegistryValue(Parameter value)
        {
            UInt32 parameter = (UInt32)value;
            return new Long(parameter.getValue());
        }

        public Parameter createRegistryValueParameter(int size)
        {
            return new UInt32();
        }

        public boolean isTypeSupported(Object value)
        {
            return (value instanceof Number) || (value instanceof IntegerParameter);
        }
    };

    /**
     * Default multi-string transformer. Performs transfering of string array
     * and string collection to/from the registry.
     */
    public static RegistryValueTransformer MULTISTRING_TRANSFORMER = new RegistryValueTransformer()
    {
        public Parameter toRegistryValue(Object value)
        {
            Collection strings;
            if (value instanceof String[])
            {
                strings = Arrays.asList((String[])value);
            }
            else if (value instanceof Collection)
            {
                strings = (Collection)value;
            }
            else
                throw new IllegalArgumentException("Not a string collection");

            final StringBuffer chars = new StringBuffer();
            for (Iterator i = strings.iterator(); i.hasNext();)
            {
                chars.append(i.next()).append('\0');
            }
            chars.append('\0');
            final boolean unicode = Kernel32.getInstance().isUnicode();
            final int length = chars.length();
            final PrimitiveArray array = (PrimitiveArray)createRegistryValueParameter(length);
            for (int i = 0; i < chars.length(); i++)
            {
                final char s = chars.charAt(i);
                final Parameter c = unicode ? (Parameter)new WideChar(s) : new Char(s);
                array.setElement(i, c);
            }
            return array;
        }

        public Object fromRegistryValue(Parameter value)
        {
            List strings = new ArrayList();
            PrimitiveArray charArray = (PrimitiveArray)value;
            StringBuffer string = new StringBuffer();
            final boolean unicode = Kernel32.getInstance().isUnicode();
            final int elementCount = charArray.getElementCount();
            for (int i = 0; i < elementCount; i++)
            {
                final Parameter element = charArray.getElement(i);
                char c = unicode ? ((WideChar)element).getValue() : ((Char)element).getValue();
                if (c == '\0')
                {
                    if (string.length() == 0)
                        break;

                    strings.add(string.toString());
                    string = new StringBuffer();
                }
                else
                {
                    string.append(c);
                }
            }
            return strings;
        }

        public Parameter createRegistryValueParameter(int size)
        {
            final boolean unicode = Kernel32.getInstance().isUnicode();
            return new PrimitiveArray(unicode ? WideChar.class : Char.class, size);
        }

        public boolean isTypeSupported(Object value)
        {
            return (value instanceof String[]) || (value instanceof Collection);
        }
    };

    /**
     * Default binary transformer. Performs transfering of byte and primitive
     * arrays to the registry.
     */
    public static RegistryValueTransformer BINARY_TRANSFORMER = new RegistryValueTransformer()
    {
        public Parameter toRegistryValue(Object value)
        {
            Parameter result = null;
            if (value instanceof byte[])
            {
                byte[] bytes = (byte[])value;
                final PrimitiveArray byteArray = (PrimitiveArray)createRegistryValueParameter(bytes.length);
                for (int i = 0; i < bytes.length; i++)
                {
                    byteArray.setElement(i, new UInt8(bytes[i]));
                }
                result = byteArray;
            }
            else if (value instanceof PrimitiveArray)
            {
                result = (Parameter)value;
            }
            else
                throw new IllegalArgumentException("Not a byte array parameter");

            return result;
        }

        public Object fromRegistryValue(Parameter value)
        {
            PrimitiveArray array = (PrimitiveArray)value;
            byte[] bytes = array.getBytes();
            return bytes;
        }

        public Parameter createRegistryValueParameter(int size)
        {
            return new PrimitiveArray(UInt8.class, size);
        }

        public boolean isTypeSupported(Object value)
        {
            return (value instanceof byte[] || value instanceof PrimitiveArray);
        }
    };
}

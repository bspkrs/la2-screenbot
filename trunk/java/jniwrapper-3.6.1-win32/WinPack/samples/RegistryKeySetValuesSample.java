/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
import com.jniwrapper.win32.registry.RegistryKey;
import com.jniwrapper.win32.registry.RegistryKeyValues;

/**
 * This sample demonstrates how to specify attributes for a registry key.
 *
 * @author Vladimir Kondrashchenko
 */
public class RegistryKeySetValuesSample
{
    public static void main(String[] args)
    {
        RegistryKey CLSID =
                RegistryKey.CLASSES_ROOT.openSubKey("CLSID").createSubKey("{13292FC1-9F07-45ef-A444-152BF217982F}", true);
        RegistryKeyValues values = CLSID.values();
        values.put("", "New default value");
        values.put("comment", "New comment");
        CLSID.close();
    }
}

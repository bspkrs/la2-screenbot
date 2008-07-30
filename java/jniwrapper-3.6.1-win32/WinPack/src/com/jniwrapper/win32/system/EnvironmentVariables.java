/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.system;

/**
 * Class EnvironmentVariables.
 */
public interface EnvironmentVariables
{
    String getValue(String variable);

    void setValue(String variable, String value);

    boolean contains(String variable);

    String[] getVariableNames();
}

/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.system;

import com.jniwrapper.Bool;
import com.jniwrapper.Function;
import com.jniwrapper.UInt;
import com.jniwrapper.UInt32;
import com.jniwrapper.win32.ui.User32;

/**
 * A utility class for making debugging and error handling sounds.
 * 
 * @author Alexander Evsukov
 */
public class Beeper
{
    /**
     * SystemHand sound.
     */
    public static final int MB_ICONHAND = 0x00000010;
    /**
     * SystemQuestion sound.
     */
    public static final int MB_ICONQUESTION = 0x00000020;
    /**
     * SystemExclamation sound.
     */
    public static final int MB_ICONEXCLAMATION = 0x00000030;
    /**
     * SystemAsterisk sound.
     */
    public static final int MB_ICONASTERISK = 0x00000040;
    /**
     * SystemDefault sound.
     */
    public static final int MB_OK = 0x0;

    static final String FUNCTION_BEEP = "Beep";
    static final String FUNCTION_MESSAGE_BEEP = "MessageBeep";

    /**
     * 
     * @param frequency frequency of the sound, in Hertz. Must be in the range between
     * 37 through 32,767.
     * @param duration duration of the sound, in milliseconds.
     * @return true, if the operation succeeds. If false, extended error
     * information can be obtained via {@link
     * com.jniwrapper.win32.LastError#getValue()}
     */
    public static boolean beep(long frequency, long duration)
    {
        final Function function = Kernel32.getInstance().getFunction(FUNCTION_BEEP);
        final Bool returnValue = new Bool();
        function.invoke(returnValue, new UInt32(frequency), new UInt32(duration));
        return returnValue.getValue();
    }

    /**
     * Plays a predefined sound.
     * 
     * @param type sound type, one of the <code>MB_XXX</code> values.
     * @return true, if the operation succeeds. If false, extended error
     * information can be obtained via {@link
     * com.jniwrapper.win32.LastError#getValue()}
     */
    public static boolean messageBeep(int type)
    {
        final Function function = User32.getInstance().getFunction(FUNCTION_MESSAGE_BEEP);
        final Bool returnValue = new Bool();
        function.invoke(returnValue, new UInt(type));
        return returnValue.getValue();
    }
}

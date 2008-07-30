/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.hook;

import com.jniwrapper.util.FlagSet;

/**
 * This class describes events of the {@link
 * com.jniwrapper.win32.hook.Hook.Descriptor#KEYBOARD} hook.
 * 
 * @author Serge Piletsky
 */
public class KeyboardEvent extends HookEventObject
{
    private long _virtualKeyCode;
    private long _info;

    public KeyboardEvent(Object source, long virtualKeyCode, long keyInfo)
    {
        super(source);
        _virtualKeyCode = virtualKeyCode;
        _info = keyInfo;
    }

    public long getVirtualKeyCode()
    {
        return _virtualKeyCode;
    }

    /**
     * Specifies the repeat count. The value is the number of times the
     * keystroke is repeated as a result of the user's holding down the key.
     * 
     * @return the repeat count.
     */
    public int getRepeatCount()
    {
        FlagSet flagSet = new FlagSet(_info);
        return flagSet.getBits(0, 15);
    }

    /**
     * Specifies the scan code. The value depends on the OEM.
     * 
     * @return scan code.
     */
    public int getScanCode()
    {
        FlagSet flagSet = new FlagSet(_info);
        return flagSet.getBits(16, 23);
    }

    /**
     * Specifies whether the key is an extended key, such as a function key or a
     * key on the numeric keypad.
     * 
     * @return true, if the key is an extended key; otherwise, it is false.
     */
    public boolean isExtendedKey()
    {
        FlagSet flagSet = new FlagSet(_info);
        return flagSet.getBit(24);
    }

    /**
     * Specifies the context code.
     * 
     * @return true, if the ALT key is down; otherwise, it is false.
     */
    public boolean isAltPressed()
    {
        FlagSet flagSet = new FlagSet(_info);
        return flagSet.getBit(29);
    }

    /**
     * Specifies the previous key state.
     * 
     * @return true, if the key is down before the message is sent; false if the
     * key is up.
     */
    public boolean getPreviousState()
    {
        FlagSet flagSet = new FlagSet(_info);
        return flagSet.getBit(30);
    }

    /**
     * Specifies the transition state.
     * 
     * @return true, if the key is being pressed; false if it is being released.
     */
    public boolean getTransitionState()
    {
        FlagSet flagSet = new FlagSet(_info);
        return !flagSet.getBit(31);
    }
}

/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.ui.dialogs;

import com.jniwrapper.*;
import com.jniwrapper.win32.Handle;
import com.jniwrapper.win32.gdi.ColorRef;
import com.jniwrapper.win32.ui.Wnd;
import com.jniwrapper.win32.IntPtr;

/**
 * This class represents CHOOSECOLOR structure (both ANSI and UNICODE).
 * 
 * @author Serge Piletsky
 */
class ChooseColorStructure extends Structure
{
    static final int CUSTOM_COLORS_COUNT = 16;

    private UInt32 _structureSize = new UInt32();
    private Wnd _owner = new Wnd();
    private Handle _instance = new Handle();
    private ColorRef _rgbResult = new ColorRef();
    private PrimitiveArray _customColors = new PrimitiveArray(Int32.class, CUSTOM_COLORS_COUNT);
    private UInt32 _flags = new UInt32();
    private IntPtr _custData = new IntPtr();
    private Callback _hook = null;
    private Str _templateName;

    public ChooseColorStructure(Callback dialogCallback)
    {
        _hook = dialogCallback;
        _templateName = new Str();
        init(new Parameter[]{_structureSize, _owner, _instance, _rgbResult,
                             new Pointer(_customColors), _flags, _custData, _hook, new Pointer(_templateName)}, (short)8);
        _structureSize.setValue(getLength());
    }

    public ChooseColorStructure(ChooseColorStructure that)
    {
        this(that._hook);
        initFrom(that);
    }

    public Handle getOwner()
    {
        return _owner;
    }

    public void setOwner(Handle owner)
    {
        _owner.setValue(owner == null ? 0 : owner.getValue());
    }

    public Handle getInstance()
    {
        return _instance;
    }

    public void setInstance(Handle instance)
    {
        _instance.setValue(instance == null ? 0 : instance.getValue());
    }

    public long getFlags()
    {
        return _flags.getValue();
    }

    public void setFlags(long flags)
    {
        _flags.setValue(flags);
    }

    public long getCustData()
    {
        return _custData.getValue();
    }

    public void setCustData(long custData)
    {
        _custData.setValue(custData);
    }

    public ColorRef getColor()
    {
        return _rgbResult;
    }

    public void setColor(ColorRef value)
    {
        _rgbResult.setValue(value.getValue());
    }

    public void setCustomColors(int[] colors)
    {
        for (int i = 0; i < colors.length; i++)
        {
            _customColors.setElement(i, new Int32(colors[i]));
        }
        for (int i = colors.length; i < CUSTOM_COLORS_COUNT; i++)
        {
            _customColors.setElement(i, new Int32());
        }
    }

    public int[] getCustomColors()
    {
        int[] result = new int[CUSTOM_COLORS_COUNT];
        for (int i = 0; i < CUSTOM_COLORS_COUNT; i++)
        {
            final Int32 color = (Int32)_customColors.getElement(i);
            result[i] = (int)color.getValue();
        }
        return result;
    }

    public Object clone()
    {
        return new ChooseColorStructure(this);
    }
}

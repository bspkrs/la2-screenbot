/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.mapi;

import com.jniwrapper.*;

/**
 * Represents the MapiRecipDesc native structure.
 */
class MapiRecipDesc extends Structure
{
    private ULongInt _reserved = new ULongInt(0);
    private ULongInt _recipClass = new ULongInt(0);
    private AnsiString _name = new AnsiString();
    private Pointer _namePtr = new Pointer(_name);
    private AnsiString _address = new AnsiString();
    private Pointer _addressPtr = new Pointer(_address);
    private ULongInt _eidSize = new ULongInt(0);
    private Pointer.Void _entryIDPtr = new Pointer.Void();

    MapiRecipDesc()
    {
        init(new Parameter[]
        {
            _reserved,
            _recipClass,
            _namePtr,
            _addressPtr,
            _eidSize,
            _entryIDPtr}, (short)8);
    }

    String getAddress()
    {
        return _address.getValue();
    }

    void setAddress(String address)
    {
        _address.setValue(address);
    }

    long getEidSize()
    {
        return _eidSize.getValue();
    }

    void setEidSize(long eidSize)
    {
        _eidSize.setValue(eidSize);
    }

    Pointer.Void getEntryIDPtr()
    {
        return _entryIDPtr;
    }

    void setEntryIDPtr(Pointer.Void entryIDPtr)
    {
        _entryIDPtr = entryIDPtr;
    }

    String getName()
    {
        return _name.getValue();
    }

    void setName(String name)
    {
        _name.setValue(name);
    }

    int getRecipClass()
    {
        return (int) _recipClass.getValue();
    }

    void setRecipClass(int recipClass)
    {
        _recipClass.setValue(recipClass);
    }

    public Object clone() {
        MapiRecipDesc copy = new MapiRecipDesc();
        copy.initFrom(this);
        return copy;
    }
}
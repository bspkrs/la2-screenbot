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
import com.jniwrapper.win32.ui.Wnd;
import com.jniwrapper.win32.IntPtr;

/**
 * This class represents BROWSEINFO structure (both Ansi and Unicode).
 * 
 * @author Serge Piletsky
 */
public class BIStructure extends Structure
{
    /**
     * Handle to the dialog box owner window.
     */
    private Wnd _owner = new Wnd();
    /**
     * Pointer to an ITEMIDLIST structure. This member can be Pointer.Void.
     */
    private Parameter _itemIDList;
    /**
     * The display name of the folder that the user selects.
     */
    private Str _displayName = null;
    /**
     * String that is displayed above the tree view control in the dialog box.
     */
    private Str _title = null;
    /**
     * Unsigned integer that specifies the options for the dialog box.
     */
    private UInt _flags = new UInt();
    /**
     * Application-defined function that the dialog box calls when an event occurs.
     */
    private Parameter _callback = null;
    /**
     * Application-defined value that will be passed to the callback function.
     */
    private Pointer _param = new Pointer(null, true);
    /**
     * Integer that receives image index for selected folder.
     */
    private Int _image = new Int();

    static final int MAX_PATH = 260;

    public BIStructure(Parameter callback, Parameter root)
    {
        _callback = callback == null? new Pointer.Void():callback;
        _itemIDList = root != null? root: new Pointer.Void();
        initStringParameters();
        init(new Parameter[]{
                _owner,
                _itemIDList,
                new Pointer(_displayName),
                new Pointer(_title),
                _flags,
                _callback,
                _param,
                _image},
             (short)8
        );

    }

    public BIStructure(BIStructure that)
    {
        this(that._callback, that._itemIDList);
        initFrom(that);
    }

    private void initStringParameters()
    {
        _displayName = new Str("", MAX_PATH);
        _title = new Str();
    }

    public Wnd getOwner()
    {
        return _owner;
    }

    public void setOwner(Wnd owner)
    {
        _owner.setValue(owner.getValue());
    }

    public Parameter getItemIDList()
    {
        return _itemIDList;
    }

    public String getDisplayName()
    {
        return _displayName.getValue();
    }

    public void setDisplayName(String displayName)
    {
        _displayName.setValue(displayName);
    }

    public String getTitle()
    {
        return _title.getValue();
    }

    public void setTitle(String title)
    {
        _title.setValue(title);
    }

    public long getFlags()
    {
        return _flags.getValue();
    }

    public void setFlags(long flags)
    {
        _flags.setValue(flags);
    }

    public void setParam(Parameter value)
    {
        _param.setReferencedObject(value);
    }

    public Object clone()
    {
        return new BIStructure(this);
    }
}

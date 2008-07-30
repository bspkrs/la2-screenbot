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
import com.jniwrapper.win32.gdi.DC;
import com.jniwrapper.win32.Handle;
import com.jniwrapper.win32.IntPtr;

/**
 * Represent the PRINTDLG native structure.
 */
public class PrintDlgStructure extends Structure
{
    private UInt32 _structureSize = new UInt32();
    private Wnd _owner = new Wnd();
    private Handle _devMode = new Handle();
    private Handle _devNames = new Handle();
    private DC _dc = new DC();
    private UInt32 _flags = new UInt32();
    private UInt16 _fromPage = new UInt16();
    private UInt16  _toPage = new UInt16();
    private UInt16  _minPage = new UInt16();
    private UInt16  _maxPage = new UInt16();
    private UInt16  _copies = new UInt16();
    private Handle _instance = new Handle();
    private IntPtr _custData = new IntPtr();
    private Callback _printHook = null;
    private Callback _setupHook = null;
    private Str _printTemplateName = new Str();
    private Str _setupTemplateName = new Str();
    private Handle _printTemplate = new Handle();
    private Handle _setupTemplate = new Handle();

    private void init()
    {
        init(new Parameter[] {
            _structureSize,
            _owner,
            _devMode,
            _devNames,
            _dc,
            _flags,
            _fromPage,
            _toPage,
            _minPage,
            _maxPage,
            _copies,
            _instance,
            _custData,
            _printHook,
            _setupHook,
            new Pointer(_printTemplateName),
            new Pointer(_setupTemplateName),
            _printTemplate,
            _setupTemplate,
        });

        setStructureSize(getLength());
    }

    public PrintDlgStructure(PrintDlgStructure that)
    {
        _structureSize = (UInt32) that._structureSize.clone();
        _owner = (Wnd) that._owner.clone();
        _devMode = (Handle) that._devMode.clone();
        _devNames = (Handle) that._devNames.clone();
        _dc = (DC) that._dc.clone();
        _flags = (UInt32) that._flags.clone();
        _fromPage = (UInt16) that._fromPage.clone();
        _toPage = (UInt16) that._toPage.clone();
        _minPage = (UInt16) that._minPage.clone();
        _maxPage = (UInt16) that._maxPage.clone();
        _copies = (UInt16) that._copies.clone();
        _instance = (Handle) that._instance.clone();
        //_custData = (Int32) that._custData.clone();
        _custData = (IntPtr) that._custData.clone();
        _printHook = (Callback) that._printHook.clone();
        _setupHook = (Callback) that._setupHook.clone();
        _printTemplateName = (Str) that._printTemplateName.clone();
        _setupTemplateName = (Str) that._setupTemplateName.clone();
        _printTemplate = (Handle) that._printTemplate.clone();
        _setupTemplate = (Handle) that._setupTemplate.clone();

        init();
    }

    public PrintDlgStructure(Callback printHookCallback, Callback setupHookCallback)
    {
        _printHook = printHookCallback;
        _setupHook = setupHookCallback;

        init();
    }

    private void setStructureSize(int structureSize)
    {
        _structureSize.setValue(structureSize);
    }

    public Wnd getOwner()
    {
        return _owner;
    }

    public void setOwner(Wnd owner)
    {
        _owner.setValue(owner != null ? owner.getValue() : 0);
    }

    public Handle getDevMode()
    {
        return _devMode;
    }

    public void setDevMode(Handle devMode)
    {
        _devMode.setValue(devMode != null ? devMode.getValue() : 0);
    }

    public Handle getDevNames()
    {
        return _devNames;
    }

    public void setDevNames(Handle devNames)
    {
        _devNames.setValue(devNames != null ? devNames.getValue() : 0);
    }

    public DC getDc()
    {
        return _dc;
    }

    public void setDc(DC dc)
    {
        _dc.setValue(dc != null ? dc.getValue() : 0);
    }

    public long getFlags()
    {
        return _flags.getValue();
    }

    public void setFlags(long flags)
    {
        _flags.setValue(flags);
    }

    public int getFromPage()
    {
        return (int)_fromPage.getValue();
    }

    public void setFromPage(int fromPage)
    {
        _fromPage.setValue(fromPage);
    }

    public int getToPage()
    {
        return (int)_toPage.getValue();
    }

    public void setToPage(int toPage)
    {
        _toPage.setValue(toPage);
    }

    public int getMinPage()
    {
        return (int)_minPage.getValue();
    }

    public void setMinPage(int minPage)
    {
        _minPage.setValue(minPage);
    }

    public int getMaxPage()
    {
        return (int)_maxPage.getValue();
    }

    public void setMaxPage(int maxPage)
    {
        _maxPage.setValue(maxPage);
    }

    public int getCopies()
    {
        return (int)_copies.getValue();
    }

    public void setCopies(int copies)
    {
        _copies.setValue(copies);
    }

    public Handle getInstance()
    {
        return _instance;
    }

    public void setInstance(Handle instance)
    {
        _instance.setValue(instance != null ? instance.getValue() : 0);
    }

    public long getCustData()
    {
        return _custData.getValue();
    }

    public void setCustData(long custData)
    {
        _custData.setValue(custData);
    }

    public Callback getPrintHook()
    {
        return _printHook;
    }

    public void setPrintHook(Callback printHook)
    {
        _printHook = printHook;
    }

    public Callback getSetupHook()
    {
        return _setupHook;
    }

    public void setSetupHook(Callback setupHook)
    {
        _setupHook = setupHook;
    }

    public String getPrintTemplateName()
    {
        return _printTemplateName.getValue();
    }

    public void setPrintTemplateName(String printTemplateName)
    {
        _printTemplateName.setValue(printTemplateName);
    }

    public String getSetupTemplateName()
    {
        return _setupTemplateName.getValue();
    }

    public void setSetupTemplateName(String setupTemplateName)
    {
        _setupTemplateName.setValue(setupTemplateName);
    }

    public Handle getPrintTemplate()
    {
        return _printTemplate;
    }

    public void setPrintTemplate(Handle printTemplate)
    {
        _printTemplate.setValue(printTemplate != null ? printTemplate.getValue() : 0);
    }

    public Handle getSetupTemplate()
    {
        return _setupTemplate;
    }

    public void setSetupTemplate(Handle setupTemplate)
    {
        _setupTemplate.setValue(setupTemplate != null ? setupTemplate.getValue() : 0);
    }

    public Object clone()
    {
        return new PrintDlgStructure(this);
    }
}

/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.ui.dialogs;

import com.jniwrapper.Callback;
import com.jniwrapper.IntBool;
import com.jniwrapper.Pointer;
import com.jniwrapper.win32.FunctionName;
import com.jniwrapper.win32.Handle;
import com.jniwrapper.win32.ui.Wnd;

import java.awt.Window;

public class PrintDialog
{
    private static final int PD_ALLPAGES = 0x00000000;
    private static final int PD_SELECTION = 0x00000001;
    private static final int PD_PAGENUMS = 0x00000002;
    private static final int PD_NOSELECTION = 0x00000004;
    private static final int PD_NOPAGENUMS = 0x00000008;
    private static final int PD_COLLATE = 0x00000010;
    private static final int PD_PRINTTOFILE = 0x00000020;
    private static final int PD_PRINTSETUP = 0x00000040;
    private static final int PD_NOWARNING = 0x00000080;
    private static final int PD_RETURNDC = 0x00000100;
    private static final int PD_RETURNIC = 0x00000200;
    private static final int PD_RETURNDEFAULT = 0x00000400;
    private static final int PD_SHOWHELP = 0x00000800;
    private static final int PD_ENABLEPRINTHOOK = 0x00001000;
    private static final int PD_ENABLESETUPHOOK = 0x00002000;
    private static final int PD_ENABLEPRINTTEMPLATE = 0x00004000;
    private static final int PD_ENABLESETUPTEMPLATE = 0x00008000;
    private static final int PD_ENABLEPRINTTEMPLATEHANDLE = 0x00010000;
    private static final int PD_ENABLESETUPTEMPLATEHANDLE = 0x00020000;
    private static final int PD_USEDEVMODECOPIES = 0x00040000;
    private static final int PD_USEDEVMODECOPIESANDCOLLATE = 0x00040000;
    private static final int PD_DISABLEPRINTTOFILE = 0x00080000;
    private static final int PD_HIDEPRINTTOFILE = 0x00100000;
    private static final int PD_NONETWORKBUTTON = 0x00200000;

    private FunctionName FUNCTION_PrintDlg = new FunctionName("PrintDlg");

    private Window _owner;
    private Handle _devMode = new Handle();
    private Handle _devNames = new Handle();
    private int _fromPage;
    private int _toPage;
    private int _minPage;
    private int _maxPage;
    private int _copies;

    public PrintDialog(Window owner)
    {
        _owner = owner;
    }

    public boolean open()
    {
        PrintDlgStructure printDlgStructure = fillPrintDlgStructure();

        printDlgStructure.setFlags(PD_NOSELECTION | PD_NOPAGENUMS | PD_DISABLEPRINTTOFILE);

        IntBool result = new IntBool();
        ComDlg32.getInstance().getFunction(FUNCTION_PrintDlg.toString()).invoke(result, new Pointer(printDlgStructure));

        _devMode.setValue(printDlgStructure.getDevMode().getValue());
        _devNames.setValue(printDlgStructure.getDevNames().getValue());

        _fromPage = printDlgStructure.getFromPage();
        _toPage = printDlgStructure.getToPage();
        _minPage = printDlgStructure.getMinPage();
        _maxPage = printDlgStructure.getMaxPage();
        _copies = printDlgStructure.getCopies();

        return result.getBooleanValue();
    }

    private PrintDlgStructure fillPrintDlgStructure()
    {
        PrintDlgStructure result = new PrintDlgStructure(
                new Callback() {
                    public void callback()
                    {
                    }
                },
                new Callback() {
                    public void callback()
                    {
                    }
                }                
        );

        if (_owner != null)
        {
            Wnd hwnd = new Wnd(_owner);
            result.setOwner(hwnd);
        }

        result.setFromPage(_fromPage);
        result.setToPage(_toPage);
        result.setMinPage(_minPage);
        result.setMaxPage(_maxPage);
        result.setCopies(_copies);

        return result;
    }

    public Window getOwner()
    {
        return _owner;
    }

    public void setOwner(Window owner)
    {
        _owner = owner;
    }

    public Handle getDevMode()
    {
        return _devMode;
    }

    public Handle getDevNames()
    {
        return _devNames;
    }

    public int getFromPage()
    {
        return _fromPage;
    }

    public void setFromPage(int fromPage)
    {
        _fromPage = fromPage;
    }

    public int getToPage()
    {
        return _toPage;
    }

    public void setToPage(int toPage)
    {
        _toPage = toPage;
    }

    public int getMinPage()
    {
        return _minPage;
    }

    public void setMinPage(int minPage)
    {
        _minPage = minPage;
    }

    public int getMaxPage()
    {
        return _maxPage;
    }

    public void setMaxPage(int maxPage)
    {
        _maxPage = maxPage;
    }

    public int getCopies()
    {
        return _copies;
    }

    public void setCopies(int copies)
    {
        _copies = copies;
    }
}

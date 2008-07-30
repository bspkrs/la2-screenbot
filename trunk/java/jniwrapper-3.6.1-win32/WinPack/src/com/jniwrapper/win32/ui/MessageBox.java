/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.ui;

import com.jniwrapper.Function;
import com.jniwrapper.Int;
import com.jniwrapper.Str;
import com.jniwrapper.UInt;
import com.jniwrapper.win32.FunctionName;

/**
 * 
 * @author Alexander Evsukov
 */
public class MessageBox
{
    /**
     * Makes the message box display the OK button.
     */
    public static final int OK = 0x00000000;

    /**
     * Makes the message box display the OK and Cancel buttons.
     */
    public static final int OKCANCEL = 0x00000001;

    /**
     * Makes the message box display the Abort, Retry, and Ignore buttons.
     */
    public static final int ABORTRETRYIGNORE = 0x00000002;

    /**
     * Makes the message box display the Yes, No, and Cancel buttons.
     */
    public static final int YESNOCANCEL = 0x00000003;

    /**
     * Makes the message box display the Yes and No buttons.
     */
    public static final int YESNO = 0x00000004;

    /**
     * Makes the message box display the Retry and Cancel buttons.
     */
    public static final int RETRYCANCEL = 0x00000005;

    /**
     * Indicates that a hand icon should be displayed on the message box.
     */
    public static final int ICONHAND = 0x00000010;

    /**
     * Makes the message box display the question mark.
     */
    public static final int ICONQUESTION = 0x00000020;

    /**
     * Makes the message box display the exclamation icon.
     */
    public static final int ICONEXCLAMATION = 0x00000030;

    /**
     * Makes the message box display an asterisk icon.
     */
    public static final int ICONASTERISK = 0x00000040;

    /**
     * Makes the message box display the stop icon.
     */
    public static final int ICONSTOP = ICONHAND;

    /**
     * Makes the message box display the error icon.
     */
    public static final int ICONERROR = ICONHAND;

    /**
     * Makes the message box display the warning icon.
     */
    public static final int ICONWARNING = ICONEXCLAMATION;

    /**
     * Makes the message box display the information icon.
     */
    public static final int ICONINFORMATION = ICONASTERISK;

    /* To indicate the default button, specify one of the following values */
    public static final int DEFBUTTON1 = 0x00000000;
    public static final int DEFBUTTON2 = 0x00000100;
    public static final int DEFBUTTON3 = 0x00000200;

    /*
     * Possible return values
     */
    public static final int IDOK = 1;
    public static final int IDCANCEL = 2;
    public static final int IDABORT = 3;
    public static final int IDRETRY = 4;
    public static final int IDIGNORE = 5;
    public static final int IDYES = 6;
    public static final int IDNO = 7;
    public static final int IDCLOSE = 8;
    public static final int IDHELP = 9;

    static final FunctionName FUNCTION_MESSAGE_BOX = new FunctionName("MessageBox");

    public static int show(Wnd hWnd, String title, String message, int flags)
    {
        final Function function = User32.getInstance().getFunction(FUNCTION_MESSAGE_BOX.toString());
        Int result = new Int();
        function.invoke(result, hWnd, new Str(message), new Str(title), new UInt(flags));
        return (int)result.getValue();
    }

    public static int show(String title, String message, int flags)
    {
        Wnd hWnd = new Wnd();
        return show(hWnd, title, message, flags);
    }

    public static void show(String title, String message)
    {
        show(title, message, 0);
    }
}

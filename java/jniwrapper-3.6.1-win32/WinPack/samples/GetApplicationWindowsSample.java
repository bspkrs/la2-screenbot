/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
import com.jniwrapper.win32.ui.Wnd;

import java.util.List;

/**
 * This sample prints out list of running window applications. 
 *
 * @author Vladimir Kondrashchenko
 */
public class GetApplicationWindowsSample
{
    public static void main(String[] args)
    {
        List list = com.jniwrapper.win32.process.Process.getApplicationWindows();
        for (int i = 0; i < list.size(); i++)
        {
            Wnd wnd = (Wnd)list.get(i);
            System.out.println(wnd.getProcessId() + "  " + wnd.getWindowText());
        }
    }
}

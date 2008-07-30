/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
import com.jniwrapper.win32.hook.IdleTracker;
import com.jniwrapper.win32.hook.IdleTrackerListener;

/**
 * This sample demonstrates to track user idleness using hte IdleTracker class.
 *
 * @author Vladimir Kondrashchenko
 */
public class IdleTrackerSample
{

    public static void main(String[] args)
    {
        IdleTracker idle = new IdleTracker(2000);
        idle.addListener(new IdleTrackerListener()
        {
            public void timeoutElapsed()
            {
                System.out.println("Idle tracker listener...");
            }
        });
        idle.start();
    }
}

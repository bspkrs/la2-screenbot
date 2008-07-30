/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.ui;

import com.jniwrapper.Parameter;
import com.jniwrapper.Structure;
import com.jniwrapper.UInt;
import com.jniwrapper.UInt32;
import com.jniwrapper.util.FlagSet;

/**
 * This class represents <code>FLASHWINFO</code> native structure.
 * 
 * @author Serge Piletsky
 */
public class FlashInfo extends Structure
{
    private UInt _size = new UInt();
    private Wnd _wnd;
    private UInt32 _flags = new UInt32();
    private UInt _count = new UInt();
    private UInt32 _timeout = new UInt32();

    /**
     * Creates an instance of flash information used in the {@link Wnd} class for the
     * <code>flashWindow</code> function.
     * 
     * @param wnd a window to be flashed.
     * @param options the flash status for a window.
     * @param count the number of times to flash the window.
     * @param timeout a flash rate in milliseconds; 0 is the default system flash
     * rate.
     */
    public FlashInfo(Wnd wnd, FlashOptions options, long count, long timeout)
    {
        _wnd = wnd;
        _flags.setValue(options.getFlags());
        _count.setValue(count);
        _timeout.setValue(timeout);
        init(new Parameter[] {_size, _wnd, _flags, _count, _timeout}, (short)8);
        _size.setValue(getLength());
    }

    public FlashInfo(FlashInfo that)
    {
        this(that._wnd, new FlashOptions(that._flags.getValue()), that._count.getValue(), that._timeout.getValue());
        initFrom(that);
    }

    public Object clone()
    {
        return new FlashInfo(this);
    }

    public static class FlashOptions extends FlagSet
    {
        public static final int STOP         = 0;
        public static final int CAPTION      = 1;
        public static final int TRAY         = 2;
        public static final int ALL          = CAPTION | TRAY;
        public static final int TIMER        = 4;
        public static final int TIMERNOFG    = 0xC;

        public FlashOptions()
        {
        }

        public FlashOptions(long flags)
        {
            super(flags);
        }

        /**
         * Sets options to stop flashing the window.
         */
        public void setStop()
        {
            clear();
        }

        /**
         * Sets options to flash the window caption.
         * 
         * @param value if true, only the caption flashes; if false,
         * the caption flashing is off.
         */
        public void setFlashCaption(boolean value)
        {
            if (value)
            {
                add(CAPTION);
            }
            else
            {
                remove(CAPTION);
            }
        }

        public boolean isFlashCaption()
        {
            return contains(CAPTION);
        }

        /**
         * Sets options to flash the taskbar button.
         * 
         * @param value if true, the taskbar button flashes in the tray; if false, 
         * the taskbar button flashing is off.
         */
        public void setFlashTray(boolean value)
        {
            if (value)
            {
                add(TRAY);
            }
            else
            {
                remove(TRAY);
            }
        }

        public boolean isFlashTray()
        {
            return contains(TRAY);
        }

        /**
         * Sets options to flash both the window caption and taskbar button.
         * 
         * @param value if true, to flash the caption and taskbar button; if
         * false flashing of the caption and taskbar button is off.
         */
        public void setFlashAll(boolean value)
        {
            if (value)
            {
                add(ALL);
            }
            else
            {
                remove(ALL);
            }
        }

        public boolean isFlashAll()
        {
            return contains(ALL);
        }

        /**
         * Sets options to flash the window continuously until the
         * <code>setStop()</code> flag is set.
         * 
         * @param value if true, to flash the window continuously.
         */
        public void setFlashContinuously(boolean value)
        {
            if (value)
            {
                add(TIMER);
            }
            else
            {
                remove(TIMER);
            }
        }

        public boolean isFlashContinuously()
        {
            return contains(TIMER);
        }


        /**
         * Sets options to flash the window continuously until the window comes
         * to the foreground.
         * 
         * @param value if true, to flash the window continuously.
         */
        public void setFlashContinuouslyNoForeground(boolean value)
        {
            if (value)
            {
                add(TIMERNOFG);
            }
            else
            {
                remove(TIMERNOFG);
            }
        }

        public boolean isFlashContinuouslyNoForeground()
        {
            return contains(TIMERNOFG);
        }
    }
}

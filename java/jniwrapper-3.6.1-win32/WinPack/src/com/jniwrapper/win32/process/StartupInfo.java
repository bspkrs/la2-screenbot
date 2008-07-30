/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.process;

import com.jniwrapper.*;
import com.jniwrapper.util.FlagSet;
import com.jniwrapper.win32.Handle;

/**
 * StartupInfo class represents STARTUPINFO structure.
 * 
 * @author Serge Piletsky
 */
public class StartupInfo extends Structure
{
    private UInt32 cb = new UInt32();
    private Pointer.Void reserved = new Pointer.Void();
    private Str desktop = new Str();
    private Str title = new Str();
    private UInt32 x = new UInt32();
    private UInt32 y = new UInt32();
    private UInt32 xSize = new UInt32();
    private UInt32 ySize = new UInt32();
    private UInt32 xCountChars = new UInt32();
    private UInt32 yCountChars = new UInt32();
    private UInt32 fillAttribute = new UInt32();
    private UInt32 flags = new UInt32();
    private UInt16 showWindow = new UInt16();
    private UInt16 cbReserved2 = new UInt16();
    private Pointer.Void lpReserved2 =  new Pointer.Void();
    private Handle stdInput = new Handle();
    private Handle stdOutput = new Handle();
    private Handle stdError = new Handle();

    public StartupInfo(Options options)
    {
        this();
        flags.setValue(options.getFlags());
    }

    public StartupInfo()
    {
        init(new Parameter[]
        {
            cb, reserved, new Pointer(desktop), new Pointer(title), x, y, xSize,
            ySize, xCountChars, yCountChars, fillAttribute, flags, showWindow,
            cbReserved2, lpReserved2, stdInput, stdOutput, stdError
        }, (short)8);
        cb.setValue(getLength());
    }

    public StartupInfo(StartupInfo that)
    {
        this();
        initFrom(that);
    }

    /**
     * String that specifies either the name of the desktop or the name of both
     * the desktop and window station for this process.
     */
    public Str getDesktop()
    {
        return desktop;
    }

    /**
     * For console processes, this is the title displayed in the title bar if a
     * new console window is created.
     */
    public Str getTitle()
    {
        return title;
    }

    public UInt32 getX()
    {
        return x;
    }

    public UInt32 getY()
    {
        return y;
    }

    public UInt32 getxSize()
    {
        return xSize;
    }

    public UInt32 getySize()
    {
        return ySize;
    }

    public UInt32 getxCountChars()
    {
        return xCountChars;
    }

    public UInt32 getyCountChars()
    {
        return yCountChars;
    }

    public UInt32 getFillAttribute()
    {
        return fillAttribute;
    }

    public UInt32 getFlags()
    {
        return flags;
    }

    public UInt16 getShowWindow()
    {
        return showWindow;
    }

    public Handle getStdInput()
    {
        return stdInput;
    }

    public Handle getStdOutput()
    {
        return stdOutput;
    }

    public Handle getStdError()
    {
        return stdError;
    }

    public Object clone()
    {
        return new StartupInfo(this);
    }

    public static class Options extends FlagSet
    {
        public static final int STARTF_USESHOWWINDOW = 0x00000001;
        public static final int STARTF_USESIZE = 0x00000002;
        public static final int STARTF_USEPOSITION = 0x00000004;
        public static final int STARTF_USECOUNTCHARS = 0x00000008;
        public static final int STARTF_USEFILLATTRIBUTE = 0x00000010;
        public static final int STARTF_RUNFULLSCREEN = 0x00000020;
        public static final int STARTF_FORCEONFEEDBACK = 0x00000040;
        public static final int STARTF_FORCEOFFFEEDBACK = 0x00000080;
        public static final int STARTF_USESTDHANDLES = 0x00000100;
        public static final int STARTF_USEHOTKEY = 0x00000200;

        public Options()
        {
        }

        public Options(long flags)
        {
            super(flags);
        }

        /**
         * If this value is not specified, the wShowWindow member is ignored.
         */
        public void setUseShowWindow(boolean set)
        {
            setupFlag(STARTF_USESHOWWINDOW, set);
        }

        /**
         * If this value is not specified, the wShowWindow member is ignored.
         */
        public boolean isUseShowWindow()
        {
            return contains(STARTF_USESHOWWINDOW);
        }

        /**
         * If this value is not specified, the XSize and YSize members are
         * ignored.
         */
        public void setUseSize(boolean set)
        {
            setupFlag(STARTF_USESIZE, set);
        }

        /**
         * If this value is not specified, the XSize and YSize members are
         * ignored.
         */
        public boolean isUseSize()
        {
            return contains(STARTF_USESIZE);
        }

        /**
         * If this value is not specified, the X and Y members are ignored.
         */
        public void setUsePosition(boolean set)
        {
            setupFlag(STARTF_USEPOSITION, set);
        }

        /**
         * If this value is not specified, the X and Y members are ignored.
         */
        public boolean isUsePosition()
        {
            return contains(STARTF_USEPOSITION);
        }

        /**
         * If this value is not specified, the XCountChars and YCountChars
         * members are ignored.
         */
        public void setUseCountChars(boolean set)
        {
            setupFlag(STARTF_USECOUNTCHARS, set);
        }

        /**
         * If this value is not specified, the XCountChars and YCountChars
         * members are ignored.
         */
        public boolean isUseCountChars()
        {
            return contains(STARTF_USECOUNTCHARS);
        }

        /**
         * If this value is not specified, the FillAttribute member is ignored.
         */
        public void setUseFillAttributes(boolean set)
        {
            setupFlag(STARTF_USEFILLATTRIBUTE, set);
        }

        /**
         * If this value is not specified, the FillAttribute member is ignored.
         */
        public boolean isUseFillAttributes()
        {
            return contains(STARTF_USEFILLATTRIBUTE);
        }

        /**
         * Indicates that the process should be run in full-screen mode rather
         * than in windowed mode.
         */
        public void setRunFullScreen(boolean set)
        {
            setupFlag(STARTF_RUNFULLSCREEN, set);
        }

        /**
         * Indicates that the process should be run in full-screen mode rather
         * than in windowed mode.
         */
        public boolean isRunFullScreen()
        {
            return contains(STARTF_RUNFULLSCREEN);
        }

        /**
         * Indicates that the cursor is in feedback mode for two seconds after
         * Process is created.
         */
        public void setForceOnFeedback(boolean set)
        {
            setupFlag(STARTF_FORCEONFEEDBACK, set);
        }

        /**
         * Indicates that the cursor is in feedback mode for two seconds after
         * Process is created.
         */
        public boolean isForceOnFeedback()
        {
            return contains(STARTF_RUNFULLSCREEN);
        }

        /**
         * Indicates that the feedback cursor is forced off while the process is
         * starting.
         */
        public void setForceOffFeedback(boolean set)
        {
            setupFlag(STARTF_FORCEOFFFEEDBACK, set);
        }

        /**
         * Indicates that the feedback cursor is forced off while the process is
         * starting.
         */
        public boolean isForceOffFeedback()
        {
            return contains(STARTF_FORCEOFFFEEDBACK);
        }

        public void setUseStdHandles(boolean set) {
            setupFlag(STARTF_USESTDHANDLES, set);
        }

        public boolean isUseStdHandles() {
            return contains(STARTF_USESTDHANDLES);
        }
    }
}

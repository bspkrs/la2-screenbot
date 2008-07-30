/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.shell;

import com.jniwrapper.Structure;
import com.jniwrapper.UInt32;
import com.jniwrapper.Parameter;
import com.jniwrapper.util.EnumItem;
import com.jniwrapper.util.Enums;

/**
 * Represents <code>WALLPAPEROPT</code> native structure.
 */
public class WallpaperOptions extends Structure
{
    private UInt32 dwSize = new UInt32();
    private UInt32 dwStyle = new UInt32();

    /**
     * Enumeration of all available wallpaper styles.
     */
    public static class Style extends EnumItem
    {
        public static final Style CENTER = new Style(0);
        public static final Style TILE = new Style(1);
        public static final Style STRETCH = new Style(2);
        public static final Style MAX = new Style(3);

        private Style(int value)
        {
            super(value);
        }
    }

    public WallpaperOptions()
    {
        init(new Parameter[]{dwSize, dwStyle});
        dwSize.setValue(getLength());
    }

    public WallpaperOptions(WallpaperOptions that)
    {
        this();
        initFrom(that);
    }

    public WallpaperOptions(Style value)
    {
        this();
        setStyle(value);
    }

    public void setStyle(Style value)
    {
        dwStyle.setValue(value.getValue());
    }

    public Style getStyle()
    {
        return (Style)Enums.getItem(Style.class, (int)dwStyle.getValue());
    }

    public Object clone()
    {
        return new WallpaperOptions(this);
    }
}

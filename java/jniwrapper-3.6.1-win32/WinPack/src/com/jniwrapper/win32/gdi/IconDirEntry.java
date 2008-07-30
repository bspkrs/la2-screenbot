/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.gdi;

import com.jniwrapper.*;

/**
 * This class represents ICONDIRENTRY structure.
 * 
 * @author Serge Piletsky
 */
class IconDirEntry extends Structure
{
    private UInt8 _width = new UInt8();
    private UInt8 _height = new UInt8();
    private UInt8 _colorCount = new UInt8();
    private UInt8 _reserved = new UInt8();
    private UInt16 _planes = new UInt16();
    private UInt16 _bitCount = new UInt16();
    private UInt32 _bytesInRes = new UInt32();
    private UInt32 _imageOffset = new UInt32();

    public IconDirEntry()
    {
        init(new Parameter[]{_width, _height, _colorCount, _reserved, _planes, _bitCount, _bytesInRes, _imageOffset});
    }

    public int getWidth()
    {
        return (int)_width.getValue();
    }

    public int getHeight()
    {
        return (int)_height.getValue();
    }

    public int getColorCount()
    {
        return (int)_colorCount.getValue();
    }

    public int getPlanes()
    {
        return (int)_planes.getValue();
    }

    public int getBitCount()
    {
        return (int)_bitCount.getValue();
    }

    public int getBytesInRes()
    {
        return (int)_bytesInRes.getValue();
    }

    public int getImageOffset()
    {
        return (int)_imageOffset.getValue();
    }

    public Object clone()
    {
        final IconDirEntry result = new IconDirEntry();
        result._width.setValue(_width.getValue());
        result._height.setValue(_height.getValue());
        result._colorCount.setValue(_colorCount.getValue());
        result._reserved.setValue(_reserved.getValue());
        result._planes.setValue(_planes.getValue());
        result._bitCount.setValue(_bitCount.getValue());
        result._bytesInRes.setValue(_bytesInRes.getValue());
        result._imageOffset.setValue(_imageOffset.getValue());
        return result;
    }
}

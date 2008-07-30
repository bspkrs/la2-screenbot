/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.mapi;

import com.jniwrapper.*;
import com.jniwrapper.util.EnumItem;

import java.io.File;

/**
 * Represents the MapiFileDesc native structure.
 */
class MapiFileDesc extends Structure
{
    private ULongInt _reserved = new ULongInt();
    private ULongInt _flags = new ULongInt();
    private ULongInt _position = new ULongInt(-1);
    private AnsiString _pathName = new AnsiString();
    private Pointer _pathNamePtr = new Pointer(_pathName);
    private AnsiString _fileName = new AnsiString();
    private Pointer _fileNamePtr = new Pointer(_fileName);
    private Pointer.Void _fileType = new Pointer.Void();

    MapiFileDesc()
    {
        init(new Parameter[]{
            _reserved,
            _flags,
            _position,
            _pathNamePtr,
            _fileNamePtr,
            _fileType
        }, (short)8);

    }

    MapiFileDesc(File file)
    {
        this();
        setPathName(file.getAbsolutePath());
        setFileName(file.getName());
    }

    String getFileName()
    {
        return _fileName.getValue();
    }

    void setFileName(String fileName)
    {
        _fileName.setValue(fileName);
    }

    String getPathName()
    {
        return _pathName.getValue();
    }

    void setPathName(String pathName)
    {
        _pathName.setValue(pathName);
    }

    static class MapiFileFlags extends EnumItem
    {
        public static final MapiFileFlags MAPI_OLE = new MapiFileFlags(1);
        public static final MapiFileFlags MAPI_OLE_STATIC = new MapiFileFlags(2);

        MapiFileFlags(int i)
        {
            super(i);
        }
    }

    public Object clone() {
        MapiFileDesc copy = new MapiFileDesc();
        copy.initFrom(this);
        return copy;
    }
}

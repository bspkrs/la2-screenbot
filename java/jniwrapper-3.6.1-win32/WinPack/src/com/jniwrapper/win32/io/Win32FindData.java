/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.io;

import com.jniwrapper.Parameter;
import com.jniwrapper.Str;
import com.jniwrapper.Structure;
import com.jniwrapper.UInt32;

/**
 * Win32FindData class represents WIN32_FIND_DATA data type.
 *
 * @see <a href="http://msdn.microsoft.com/library/en-us/fileio/base/win32_find_data_str.asp?frame=true"/>
 */
public class Win32FindData extends Structure
{
    private UInt32 _dwFileAttributes;
    private FileTime _ftCreationTime;
    private FileTime _ftLastAccessTime;
    private FileTime _ftLastWriteTime;
    private UInt32 _nFileSizeHigh;
    private UInt32 _nFileSizeLow;
    private UInt32 _dwReserved0;
    private UInt32 _dwReserved1;
    private Str _cFileName;
    private Str _cAlternateFileName;

    public Win32FindData()
    {
        _dwFileAttributes = new UInt32();
        _ftCreationTime = new FileTime();
        _ftLastAccessTime = new FileTime();
        _ftLastWriteTime = new FileTime();
        _nFileSizeHigh = new UInt32();
        _nFileSizeLow = new UInt32();
        _dwReserved0 = new UInt32();
        _dwReserved1 = new UInt32();
        _cFileName = new Str(260);
        _cAlternateFileName = new Str(14);

        init();
    }

    public Win32FindData(Win32FindData that)
    {
        _dwFileAttributes = (UInt32)that._dwFileAttributes.clone();
        _ftCreationTime = (FileTime)that._ftCreationTime.clone();
        _ftLastAccessTime = (FileTime)that._ftLastAccessTime.clone();
        _ftLastWriteTime = (FileTime)that._ftLastWriteTime.clone();
        _nFileSizeHigh = (UInt32)that._nFileSizeHigh.clone();
        _nFileSizeLow = (UInt32)that._nFileSizeLow.clone();
        _dwReserved0 = (UInt32)that._dwReserved0.clone();
        _dwReserved0 = (UInt32)that._dwReserved1.clone();
        _cFileName = (Str)that._cFileName.clone();
        _cAlternateFileName = (Str)that._cAlternateFileName.clone();

        init();
    }

    private void init()
    {
        init(new Parameter[]{
            _dwFileAttributes,
            _ftCreationTime,
            _ftLastAccessTime,
            _ftLastWriteTime,
            _nFileSizeHigh,
            _nFileSizeLow,
            _dwReserved0,
            _dwReserved1,
            _cFileName,
            _cAlternateFileName
        });
    }

    public long getDwFileAttributes()
    {
        return _dwFileAttributes.getValue();
    }

    public void setDwFileAttributes(long dwFileAttributes)
    {
        _dwFileAttributes.setValue(dwFileAttributes);
    }

    public FileTime getFtCreationTime()
    {
        return _ftCreationTime;
    }

    public FileTime getFtLastAccessTime()
    {
        return _ftLastAccessTime;
    }

    public FileTime getFtLastWriteTime()
    {
        return _ftLastWriteTime;
    }

    public long getNFileSizeHigh()
    {
        return _nFileSizeHigh.getValue();
    }

    public void setNFileSizeHigh(long nFileSizeHigh)
    {
        _nFileSizeHigh.setValue(nFileSizeHigh);
    }

    public long getNFileSizeLow()
    {
        return _nFileSizeLow.getValue();
    }

    public void setNFileSizeLow(long nFileSizeLow)
    {
        _nFileSizeLow.setValue(nFileSizeLow);
    }

    public long getDwReserved0()
    {
        return _dwReserved0.getValue();
    }

    public void setDwReserved0(long dwReserved0)
    {
        _dwReserved0.setValue(dwReserved0);
    }

    public long getDwReserved1()
    {
        return _dwReserved1.getValue();
    }

    public void setDwReserved1(long dwReserved1)
    {
        _dwReserved1.setValue(dwReserved1);
    }

    public String getCFileName()
    {
        return _cFileName.getValue();
    }

    public void setCFileName(String cFileName)
    {
        _cFileName.setValue(cFileName);
    }

    public String getCAlternateFileName()
    {
        return _cAlternateFileName.getValue();
    }

    public void setCAlternateFileName(String cAlternateFileName)
    {
        _cAlternateFileName.setValue(cAlternateFileName);
    }

    public Object clone()
    {
        return new Win32FindData(this);
    }
}

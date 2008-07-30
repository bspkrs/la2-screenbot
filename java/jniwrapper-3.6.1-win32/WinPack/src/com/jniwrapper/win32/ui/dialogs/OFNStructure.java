/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.ui.dialogs;

import com.jniwrapper.*;
import com.jniwrapper.win32.Handle;
import com.jniwrapper.win32.IntPtr;
import com.jniwrapper.win32.system.VersionInfo;

/**
 * This class represents OPENFILENAME structure (both Ansi and Unicode kinds).
 * 
 * @author Serge Piletsky
 */
public class OFNStructure extends Structure
{
    public static final char FILES_DELIMETER = '\t';

    private UInt32 _structureSize = new UInt32();
    private Handle _owner = new Handle();
    private Handle _instance = new Handle();
    private StringArray _filter = new StringArray();
    private Str _customFilter = new Str();
    private UInt32 _maxCustFilter = new UInt32();
    private UInt32 _filterIndex = new UInt32();
    private StringArray _file = new StringArray();
    private Pointer _pFile = new Pointer(_file);
    private UInt32 _maxFile = new UInt32();
    private Str _fileTitle = new Str();
    private UInt32 _maxFileTitle = new UInt32();
    private Str _initialDir = new Str();
    private Str _title = new Str();
    private UInt32 _flags = new UInt32();
    private UInt16 _fileOffset = new UInt16();
    private UInt16 _fileExtension = new UInt16();
    private Str _defExt = new Str();
    //private UInt32 _custData = new UInt32();
    private IntPtr _custData = new IntPtr();
    private Callback _hook = null;
    private Str _templateName = new Str();
    private Pointer _reserved = new Pointer(null, true);
    private UInt32 _reservedFlags = new UInt32();
    private UInt32 _flagsEx = new UInt32();

    static final char NATIVE_DELIMETER_SPACE = ' ';
    static final char NATIVE_DELIMETER_NULL = '\0';

    public OFNStructure(Callback hook, int maxFile)
    {
        _hook = hook;
        setMaxFile(maxFile);
        VersionInfo versionInfo = new VersionInfo();
        if (versionInfo.isWin2k() | versionInfo.isWinMe())
        {

            init(new Parameter[]{_structureSize, _owner, _instance, new Pointer(_filter), new Pointer(_customFilter),
                _maxCustFilter, _filterIndex, _pFile, _maxFile, new Pointer(_fileTitle), _maxFileTitle,
                new Pointer(_initialDir), new Pointer(_title), _flags, _fileOffset, _fileExtension, new Pointer(_defExt),
                _custData, _hook, new Pointer(_templateName), _reserved, _reservedFlags, _flagsEx}, (short)8);
        }
        else
        {
            init(new Parameter[]{_structureSize, _owner, _instance, new Pointer(_filter), new Pointer(_customFilter),
                _maxCustFilter, _filterIndex, _pFile, _maxFile, new Pointer(_fileTitle), _maxFileTitle,
                new Pointer(_initialDir), new Pointer(_title), _flags, _fileOffset, _fileExtension, new Pointer(_defExt),
                _custData, _hook, new Pointer(_templateName)}, (short)8);
        }
        _structureSize.setValue(getLength());
    }

    public OFNStructure(OFNStructure that)
    {
        this(that._hook, (int) that._maxFile.getValue());
        initFrom(that);
    }

    public Handle getOwner()
    {
        return _owner;
    }

    public void setOwner(Handle owner)
    {
        _owner.setValue(owner==null?0:owner.getValue());
    }

    public Handle getInstance()
    {
        return _instance;
    }

    public void setInstance(Handle instance)
    {
        _instance.setValue(instance==null?0:instance.getValue());
    }

    public void setFilter(StringBuffer filter)
    {
        _filter.setValue(new String[] {filter.toString()});
    }

    public String getCustomFilter()
    {
        return _customFilter.getValue();
    }

    public void setCustomFilter(String customFilter)
    {
        _customFilter.setValue(customFilter);
    }

    public long getMaxCustFilter()
    {
        return _maxCustFilter.getValue();
    }

    public void setMaxCustFilter(long maxCustFilter)
    {
        _maxCustFilter.setValue(maxCustFilter);
    }

    public long getFilterIndex()
    {
        return _filterIndex.getValue();
    }

    public void setFilterIndex(long filterIndex)
    {
        _filterIndex.setValue(filterIndex);
    }

    public String getFile()
    {
        StringBuffer result = new StringBuffer();
        Pointer.Void pVoid = new Pointer.Void();
        _pFile.castTo(pVoid);
        ExternalStringArray stringArray = new ExternalStringArray(pVoid);
        final String[] strings = stringArray.getStrings();
        for (int i = 0; i < strings.length; i++)
        {
            result.append(strings[i]);
            if (i < strings.length - 1)
            {
                result.append(FILES_DELIMETER);
            }
        }
        return result.toString();
    }

    public void setFile(String file)
    {
        if (file.length() > _file.getLength())
        {
            throw new IllegalArgumentException("File name length is greater than allowed");
        }
        _file.setValue(new String[] {file});
    }

    public long getMaxFile()
    {
        return _maxFile.getValue();
    }

    public void setMaxFile(long maxFile)
    {
        _maxFile.setValue(maxFile);
    }

    public String getFileTitle()
    {
        return _fileTitle.getValue();
    }

    public void setFileTitle(String fileTitle)
    {
        _fileTitle.setValue(fileTitle);
    }

    public long getMaxFileTitle()
    {
        return _maxFileTitle.getValue();
    }

    public void setMaxFileTitle(long maxFileTitle)
    {
        _maxFileTitle.setValue(maxFileTitle);
    }

    public String getInitialDir()
    {
        return _initialDir.getValue();
    }

    public void setInitialDir(String initialDir)
    {
        _initialDir.setValue(initialDir);
    }

    public String getTitle()
    {
        return _title.getValue();
    }

    public void setTitle(String title)
    {
        _title.setValue(title);
    }

    public long getFlags()
    {
        return _flags.getValue();
    }

    public void setFlags(long flags)
    {
        _flags.setValue(flags);
    }

    public long getFileOffset()
    {
        return _fileOffset.getValue();
    }

    public void setFileOffset(long fileOffset)
    {
        _fileOffset.setValue(fileOffset);
    }

    public long getFileExtension()
    {
        return _fileExtension.getValue();
    }

    public void setFileExtension(long fileExtension)
    {
        _fileExtension.setValue(fileExtension);
    }

    public String getDefExt()
    {
        return _defExt.getValue();
    }

    public void setDefExt(String defExt)
    {
        _defExt.setValue(defExt);
    }

    public long getCustData()
    {
        return _custData.getValue();
    }

    public void setCustData(long custData)
    {
        _custData.setValue(custData);
    }

    public Callback getHook()
    {
        return _hook;
    }

    public void setHook(Callback hook)
    {
        _hook = hook;
    }

    public String getTemplateName()
    {
        return _templateName.getValue();
    }

    public void setTemplateName(String templateName)
    {
        _templateName.setValue(templateName);
    }

    public long getFlagsEx()
    {
        return _flagsEx.getValue();
    }

    public void setFlagsEx(long flagsEx)
    {
        _flagsEx.setValue(flagsEx);
    }

    public Object clone()
    {
        return new OFNStructure(this);
    }
}

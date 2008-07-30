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

import java.util.LinkedList;
import java.util.List;

/**
 * Represents the MapiMessage native structure.
 */
class MapiMessageStructure extends Structure
{
    private ULongInt _reserved = new ULongInt();

    private AnsiString _subject = new AnsiString();
    private Pointer _subjectPtr = new Pointer(_subject);

    private Pointer _textPtr = new Pointer(AnsiString.class);

    private ULongInt _flags = new ULongInt();

    private MapiRecipDesc _originator = new MapiRecipDesc();
    private Pointer _originatorPtr = new Pointer(_originator);

    private ULongInt _recipCount = new ULongInt();

    private ComplexArray _recips = new ComplexArray(new MapiRecipDesc(), 0);
    private Pointer _recipsPointer = new Pointer(_recips);

    private ULongInt _fileCount = new ULongInt();

    private ComplexArray _files = new ComplexArray(new MapiFileDesc(), 0);
    private Pointer _filesPointer = new Pointer(_files);

    MapiMessageStructure()
    {
        init(new Parameter[]
        {
            _reserved,
            _subjectPtr,
            _textPtr,
            new Pointer.Void(), // messageType
            new Pointer.Void(), // dateReceived
            new Pointer.Void(), // conversationID
            _flags,
            _originatorPtr,
            _recipCount,
            _recipsPointer,
            _fileCount,
            _filesPointer}, (short)8);
    }

    String getSubject()
    {
        return _subject.getValue();
    }

    void setSubject(String subject)
    {
        _subject.setValue(subject);
    }

    String getText()
    {
        if (_textPtr.isNull()) {
            return null;
        } else {
            return ((AnsiString)_textPtr.getReferencedObject()).getValue();
        }
    }

    void setText(String text)
    {
        _textPtr.setReferencedObject(new AnsiString(text));
    }

    MapiRecipDesc getOriginator()
    {
        return _originator;
    }

    void setOriginator(MapiRecipDesc originator)
    {
        _originator = originator;
    }

    long getRecipCount()
    {
        return _recipCount.getValue();
    }

    void setRecipCount(long recipCount)
    {
        _recipCount.setValue(recipCount);
    }

    long getFileCount()
    {
        return _fileCount.getValue();
    }

    void setFileCount(long fileCount)
    {
        _fileCount.setValue(fileCount);
    }

    List getRecips()
    {
        List result = new LinkedList();
        for (int i = 0; i < _recips.getElementCount(); i++)
        {
            result.add(_recips.getElement(i));
        }
        return result;
    }

    void setRecips(List recips)
    {
        _recips.setElementCount(recips.size());
        for (int i = 0; i < recips.size(); i++)
        {
            _recips.setElement(i, (MapiRecipDesc) recips.get(i));
        }
        _recipCount.setValue(recips.size());
    }

    List getFiles()
    {
        List result = new LinkedList();
        for (int i = 0; i < _files.getElementCount(); i++)
        {
            result.add(_files.getElement(i));
        }
        return result;
    }

    void setFiles(List files)
    {
        _files.setElementCount(files.size());
        for (int i = 0; i < files.size(); i++)
        {
            _files.setElement(i, (MapiFileDesc) files.get(i));
        }
        _fileCount.setValue(files.size());
    }

    public Object clone() {
        MapiMessageStructure copy = new MapiMessageStructure();
        copy.initFrom(this);
        return copy;
    }
}

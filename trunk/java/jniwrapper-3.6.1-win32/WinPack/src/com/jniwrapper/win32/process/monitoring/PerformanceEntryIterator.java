/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.process.monitoring;

import com.jniwrapper.Bool;
import com.jniwrapper.Function;
import com.jniwrapper.Pointer;
import com.jniwrapper.win32.Handle;

import java.util.Iterator;

/**
 * PerformanceEntryIterator class represents a base iterator for performance
 * entries.
 */
abstract class PerformanceEntryIterator implements Iterator
{
    protected Bool _result = new Bool();
    protected Handle _snapshot = null;
    protected PerformanceEntry _lastEntry = null;
    protected boolean _firstEntry = true;

    abstract PerformanceEntry createEntry();

    abstract Function getFirstEntryFunction();

    abstract Function getNextEntryFunction();

    void getFirstEntry()
    {
        getFirstEntryFunction().invoke(_result, _snapshot, new Pointer(_lastEntry = createEntry()));
    }

    void getNextEntry()
    {
        getNextEntryFunction().invoke(_result, _snapshot, new Pointer(_lastEntry = createEntry()));
    }

    protected PerformanceEntryIterator()
    {
    }

    public PerformanceEntryIterator(Snapshot snapshot)
    {
        if (snapshot == null || snapshot.isNull())
            throw new IllegalArgumentException("Invalid Snapshot.");

        _snapshot = snapshot;
    }

    public boolean hasNext()
    {
        if (_firstEntry)
        {
            getFirstEntry();
            _firstEntry = false;
        }
        else
        {
            if (_result.getValue())
            {
                getNextEntry();
            }
        }
        return _result.getValue();
    }

    public Object next()
    {
        return _lastEntry;
    }

    public void remove()
    {
        throw new UnsupportedOperationException();
    }
}

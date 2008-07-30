package com.jniwrapper.win32.shell.events;

import com.jniwrapper.IntBool;
import com.jniwrapper.Parameter;
import com.jniwrapper.Structure;
import com.jniwrapper.win32.Handle;

/**
 * This class represents the wrapper for <code>SHChangeNotifyEntry</code> structure.
 */
class SHChangeNotifyEntry extends Structure
{
    private Handle _idList = new Handle();
    private IntBool _recursive = new IntBool();

    public SHChangeNotifyEntry()
    {
        init(new Parameter[]{_idList, _recursive});
    }

    public SHChangeNotifyEntry(Handle idList, boolean recursive)
    {
        this();
        _idList.setValue(idList.getValue());
        _recursive.setBooleanValue(recursive);
    }

    public Handle getItemIDList()
    {
        return _idList;
    }

    public IntBool getRecursive()
    {
        return _recursive;
    }
}

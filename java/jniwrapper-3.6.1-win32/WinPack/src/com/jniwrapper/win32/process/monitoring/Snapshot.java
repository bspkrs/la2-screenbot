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
import com.jniwrapper.NativeResource;
import com.jniwrapper.UInt32;
import com.jniwrapper.util.FlagSet;
import com.jniwrapper.win32.Handle;
import com.jniwrapper.win32.LastErrorException;
import com.jniwrapper.win32.system.Kernel32;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Snapshot class represents a system performance snapshot.
 * Snapshot enables to list all running processes, modules, threads and heaps.
 * <p/>
 * NOTE: Do not forget to clean up the snapshot object using the <code>close()</code> method.
 *
 * @author Serge Piletsky
 */
final public class Snapshot extends Handle implements Runnable
{
    private static final int ALL_PROCESSES = 0;

    // The CreateToolhelp32Snapshot function takes a snapshot of the specified processes in the system, as well as the heaps, modules, and threads used by these processes.
    private static final String FUNCTION_CreateSnapshot = "CreateToolhelp32Snapshot";
    private Options _options;
    private long _processID;

    Snapshot(long value)
    {
        super(value);
    }

    /**
     * Creates a snapshot of specified processes in the system, as well as heaps, modules, and threads used by these processes.
     * This constructor is used for current process.
     *
     * @param options Portions of the system information to include in the snapshot.
     */
    public Snapshot(Options options)
    {
        this(options, ALL_PROCESSES);
    }

    /**
     * Creates a snapshot of specified processes in the system, as well as heaps, modules, and threads used by these processes.
     *
     * @param options   Portions of the system information to include in the snapshot.
     * @param processID Process identifier of the process to be included in the snapshot.
     */
    public Snapshot(Options options, long processID)
    {
        _options = options;
        _processID = processID;
        run();
    }

    /**
     * Returns SnapshotOptions.
     *
     * @return SnapshotOptions.
     */
    public Options getOptions()
    {
        return _options;
    }

    /**
     * Returns a specified processID.
     *
     * @return processID.
     */
    public long getProcessID()
    {
        return _processID;
    }

    /**
     * Sets a new processID.
     *
     * @param processID new processID.
     */
    public void setProcessID(long processID)
    {
        _processID = processID;
    }

    /**
     * Updates the instance with new snapshot data.
     */
    public void run()
    {
        close();
        final Function function = Kernel32.getInstance().getFunction(FUNCTION_CreateSnapshot);
        function.invoke(this, new UInt32(_options.getFlags()), new UInt32(_processID));
    }

    /**
     * Returns an iterator for enumerating processes from the snapshot.
     * Each next element of returned Iterator is an instance of {@link ProcessEntry} class.
     *
     * @return Iterator for enumerating processes from a snapshot.
     */
    public Iterator getProcessIterator()
    {
        return new ProcessEntryIterator(this);
    }

    /**
     * Returns a list of processes from the snapshot.
     * Each element of the list is an instance of {@link ProcessEntry} class.
     *
     * @return List of processes from a snapshot.
     */
    public List getProcesses()
    {
        List result = new LinkedList();
        for (Iterator i = getProcessIterator(); i.hasNext();)
        {
            ProcessEntry processEntry = (ProcessEntry)i.next();
            result.add(processEntry);
        }
        return result;
    }

    /**
     * Returns an iterator for enumerating modules from the snapshot.
     * Each next element of returned Iterator is an instance of {@link ModuleEntry} class.
     *
     * @return Iterator for enumerating modules from a snapshot.
     */
    public Iterator getModuleIterator()
    {
        return new ModuleEntryIterator(this);
    }

    /**
     * Returns a list of modules from the snapshot.
     * Each element of the list is an instance of {@link ModuleEntry} class.
     *
     * @return List of modules from a snapshot.
     */
    public List getModules()
    {
        List result = new LinkedList();
        for (Iterator i = getModuleIterator(); i.hasNext();)
        {
            ModuleEntry moduleEntry = (ModuleEntry)i.next();
            result.add(moduleEntry);
        }
        return result;
    }

    /**
     * Returns a list of heap entries for the given process and the heap.
     * Each element of the list is an instance of {@link HeapEntry} class.
     *
     * @param processID Identifier of the process context that owns the heap.
     * @param heapID    Identifier of the heap to be enumerated.
     * @return List of heap entries for given process and heap.
     */
    public Iterator getHeapEntriesIterator(long processID, long heapID)
    {
        return new HeapEntryIterator(processID, heapID);
    }

    public List getHeapEntries(long processID, long heapID)
    {
        List result = new LinkedList();
        for (Iterator i = getHeapEntriesIterator(processID, heapID); i.hasNext();)
        {
            HeapEntry heapEntry = (HeapEntry)i.next();
            result.add(heapEntry);
        }
        return result;
    }

    /**
     * Returns iterator for enumerating heap list from a snapshot.
     * Each next element of returned Iterator is an instance of {@link HeapList} class.
     *
     * @return Iterator for enumerating heap list from a snapshot.
     */
    public Iterator getHeapListIterator()
    {
        return new HeapListIterator(this);
    }

    /**
     * Returns a list of heap list from a snapshot.
     * Each element of the list is an instance of {@link HeapList} class.
     *
     * @return List of heap list from a snapshot.
     */
    public List getHeapList()
    {
        List result = new LinkedList();
        for (Iterator i = getHeapListIterator(); i.hasNext();)
        {
            HeapList heapList = (HeapList)i.next();
            result.add(heapList);
        }
        return result;
    }

    /**
     * Returns iterator for enumerating threads from a snapshot.
     * Each next element of returned Iterator is an instance of {@link ThreadEntry} class.
     *
     * @return Iterator for enumerating threads from a snapshot.
     */
    public Iterator getThreadIterator()
    {
        return new ThreadEntryIterator(this);
    }

    /**
     * Returns a list of threads from a snapshot.
     * Each element of the list is an instance of {@link ThreadEntry} class.
     *
     * @return List of threads from a snapshot.
     */
    public List getThreadList()
    {
        List result = new LinkedList();
        for (Iterator i = getThreadIterator(); i.hasNext();)
        {
            ThreadEntry threadEntry = (ThreadEntry)i.next();
            result.add(threadEntry);
        }
        return result;
    }

    /**
     * Closes the snapshot.
     */
    public void close()
    {
        close(this);
    }

    private static void close(Snapshot snapshot)
    {
        if (!snapshot.isNull())
        {
            Handle.closeHandle(snapshot);
        }
    }

    /**
     * This class is responsible for destroying a native resource when the instance is collected by GC.
     */
    protected static class SnapshotResource implements NativeResource
    {
        private long _handle;

        public SnapshotResource(long handle)
        {
            _handle = handle;
        }

        /**
         * Frees a snapshot.
         *
         * @throws Throwable
         */
        public void release() throws Throwable
        {
            close(new Snapshot(_handle));
        }
    }

    /**
     * Options class represents options for taking a performance snapshots.
     */
    public static class Options extends FlagSet
    {
        static final int TH32CS_SNAPHEAPLIST = 0x00000001;
        static final int TH32CS_SNAPPROCESS = 0x00000002;
        static final int TH32CS_SNAPTHREAD = 0x00000004;
        static final int TH32CS_SNAPMODULE = 0x00000008;
        static final int TH32CS_SNAPALL = TH32CS_SNAPHEAPLIST | TH32CS_SNAPPROCESS | TH32CS_SNAPTHREAD | TH32CS_SNAPMODULE;
        static final int TH32CS_INHERIT = 0x80000000;

        public Options()
        {
        }

        public Options(long flags)
        {
            super(flags);
        }

        public Options(boolean snapAll)
        {
            setSnapAll(snapAll);
        }

        /**
         * Specifies whether to include all heaps of the process specified in the snapshot.
         *
         * @param set
         */
        public void setSnapHeapList(boolean set)
        {
            setupFlag(TH32CS_SNAPHEAPLIST, set);
        }

        public boolean isSnapHeapList()
        {
            return contains(TH32CS_SNAPHEAPLIST);
        }

        /**
         * Specifies whether to include all processes in the system in the snapshot.
         *
         * @param set
         */
        public void setSnapProcess(boolean set)
        {
            setupFlag(TH32CS_SNAPPROCESS, set);
        }

        public boolean isSnapProcess()
        {
            return contains(TH32CS_SNAPPROCESS);
        }

        /**
         * Specifies whether to include all threads in the system in the snapshot.
         *
         * @param set
         */
        public void setSnapThread(boolean set)
        {
            setupFlag(TH32CS_SNAPTHREAD, set);
        }

        public boolean isSnapThread()
        {
            return contains(TH32CS_SNAPTHREAD);
        }

        /**
         * Specifies whether to include all modules of the process specified in the snapshot.
         *
         * @param set
         */
        public void setSnapModule(boolean set)
        {
            setupFlag(TH32CS_SNAPMODULE, set);
        }

        public boolean isSnapModule()
        {
            return contains(TH32CS_SNAPMODULE);
        }

        /**
         * Specifies whether a snapshot is to be inheritable.
         *
         * @param set
         */
        public void setInherit(boolean set)
        {
            setupFlag(TH32CS_INHERIT, set);
        }

        public boolean isInherit()
        {
            return contains(TH32CS_INHERIT);
        }

        /**
         * Specifies whether to include all processes and threads in the system.
         *
         * @param set
         */
        public void setSnapAll(boolean set)
        {
            setupFlag(TH32CS_SNAPALL, set);
        }

        public boolean isSnapAll()
        {
            return contains(TH32CS_SNAPALL);
        }
    }
}

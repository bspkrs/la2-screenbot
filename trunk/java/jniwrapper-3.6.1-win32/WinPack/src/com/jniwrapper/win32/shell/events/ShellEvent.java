package com.jniwrapper.win32.shell.events;

import com.jniwrapper.ExternalArrayPointer;
import com.jniwrapper.PrimitiveArray;
import com.jniwrapper.win32.Handle;

import java.util.EventObject;

/**
 * An event that indicates that a shell event has occured. This event is passed to every {@link ShellEventsListener}.
 */
public class ShellEvent extends EventObject
{
    private long _id;
    private Handle _item1;
    private Handle _item2;

    /**
     * A file type association has changed.
     */
    public static final int SHCNE_ASSOCCHANGED = 0x08000000;

    /**
     * The attributes of an item or folder have changed. The {@link com.jniwrapper.win32.shell.events.ShellEvent#getItem1()} function returns the item or folder that has changed.
     */
    public static final int SHCNE_ATTRIBUTES = 0x00000800;

    /**
     * A nonfolder item has been created. The {@link com.jniwrapper.win32.shell.events.ShellEvent#getItem1()} function returns the item that was created.
     */
    public static final int SHCNE_CREATE = 0x00000002;

    /**
     * A nonfolder item has been deleted. The {@link com.jniwrapper.win32.shell.events.ShellEvent#getItem1()} function returns the item that was deleted.
     */
    public static final int SHCNE_DELETE = 0x00000004;

    /**
     * A drive has been added. The {@link com.jniwrapper.win32.shell.events.ShellEvent#getItem1()} function returns the root of the drive that was added.
     */
    public static final int SHCNE_DRIVEADD = 0x00000100;

    /**
     * A drive has been added and the Shell should create a new window for the drive. The {@link com.jniwrapper.win32.shell.events.ShellEvent#getItem1()} function returns the root of the drive that was added.
     */
    public static final int SHCNE_DRIVEADDGUI = 0x00010000;

    /**
     * A drive has been removed. The {@link com.jniwrapper.win32.shell.events.ShellEvent#getItem1()} function returns the root of the drive that was removed.
     */
    public static final int SHCNE_DRIVEREMOVED = 0x00000080;

    /**
     * The amount of free space on a drive has changed. The {@link com.jniwrapper.win32.shell.events.ShellEvent#getItem1()} function returns the root of the drive on which the free space changed.
     */
    public static final int SHCNE_FREESPACE = 0x00040000;

    /**
     * Storage media has been inserted into a drive. The {@link com.jniwrapper.win32.shell.events.ShellEvent#getItem1()} function returns the root of the drive that contains the new media.
     */
    public static final int SHCNE_MEDIAINSERTED = 0x00000020;

    /**
     * Storage media has been removed from a drive. The {@link com.jniwrapper.win32.shell.events.ShellEvent#getItem1()} function returns the root of the drive from which the media was removed.
     */
    public static final int SHCNE_MEDIAREMOVED = 0x00000040;

    /**
     * A folder has been created. The {@link com.jniwrapper.win32.shell.events.ShellEvent#getItem1()} function returns the folder that was created.
     */
    public static final int SHCNE_MKDIR = 0x00000008;

    /**
     * A folder on the local computer is being shared via the network. The {@link com.jniwrapper.win32.shell.events.ShellEvent#getItem1()} function returns the folder that is being shared.
     */
    public static final int SHCNE_NETSHARE = 0x00000200;

    /**
     * A folder on the local computer is no longer being shared via the network. The {@link com.jniwrapper.win32.shell.events.ShellEvent#getItem1()} function returns the folder that is no longer being shared.
     */
    public static final int SHCNE_NETUNSHARE = 0x00000400;

    /**
     * The name of a folder has changed. The {@link com.jniwrapper.win32.shell.events.ShellEvent#getItem1()} function returns the previous name of the folder.
     * The {@link com.jniwrapper.win32.shell.events.ShellEvent#getItem2()} function returns the new name of the folder.
     */
    public static final int SHCNE_RENAMEFOLDER = 0x00020000;

    /**
     * The name of a nonfolder item has changed. The {@link com.jniwrapper.win32.shell.events.ShellEvent#getItem1()} function returns the previous name of the item.
     * The {@link com.jniwrapper.win32.shell.events.ShellEvent#getItem2()} function returns the new name of the item.
     */
    public static final int SHCNE_RENAMEITEM = 0x00000001;

    /**
     * A folder has been removed. The {@link com.jniwrapper.win32.shell.events.ShellEvent#getItem1()} function returns the folder that was removed.
     */
    public static final int SHCNE_RMDIR = 0x00000010;

    /**
     * The computer has disconnected from a server. The {@link com.jniwrapper.win32.shell.events.ShellEvent#getItem1()} function returns the server from which the computer was disconnected.
     */
    public static final int SHCNE_SERVERDISCONNECT = 0x00004000;

    /**
     * The contents of an existing folder have changed, but the folder still exists and has not been renamed. The {@link com.jniwrapper.win32.shell.events.ShellEvent#getItem1()} function returns the folder that has changed.
     * If a folder has been created, deleted, or renamed, use <code>SHCNE_MKDIR</code>, <code>SHCNE_RMDIR</code>, or <code>SHCNE_RENAMEFOLDER</code>, instead.
     */
    public static final int SHCNE_UPDATEDIR = 0x00001000;

    /**
     * An image in the system image list has changed.
     * <p/>
     * <b>Note: </b> on Windows 95/98
     * The {@link ShellEvent#getItem1()} function returns the index in the system image list that has changed.
     * <p/>
     * <b>Note: </b> Windows NT/2000/XP
     * The {@link ShellEvent#getItem1()} function returns the index in the system image list that has changed.
     */
    public static final int SHCNE_UPDATEIMAGE = 0x00008000;

    /**
     * An existing nonfolder item has changed, but the item still exists and has not been renamed.
     * The {@link com.jniwrapper.win32.shell.events.ShellEvent#getItem1()} function returns the item that has changed.
     * If a nonfolder item has been created, deleted, or renamed, use <code>SHCNE_CREATE</code>, <code>SHCNE_DELETE</code>, or <code>SHCNE_RENAMEITEM</code> instead.
     */
    public static final int SHCNE_UPDATEITEM = 0x00002000;

    /**
     * Constructs the new event by LPARAM and WPARAM.
     */
    ShellEvent(Object source, long lParam, long wParam)
    {
        super(source);
        _id = lParam;

        PrimitiveArray idListArray = new PrimitiveArray(Handle.class, 0);
        ExternalArrayPointer idListArrayPtr = new ExternalArrayPointer(idListArray);
        Handle wParamHandle = new Handle(wParam);
        wParamHandle.castTo(idListArrayPtr);
        idListArrayPtr.readArray(2);

        _item1 = (Handle) idListArray.getElement(0);
        _item2 = (Handle) idListArray.getElement(1);
    }

    /**
     * Returns an ID of the event.
     *
     * @return ID of the event
     */
    public int getId()
    {
        return (int)_id;
    }

    /**
     * First event-dependent value.
     *
     * @return event-dependent value
     */
    public Handle getItem1()
    {
        return _item1;
    }

    /**
     * Second event-dependent value.
     *
     * @return event-dependent value
     */
    public Handle getItem2()
    {
        return _item2;
    }
}

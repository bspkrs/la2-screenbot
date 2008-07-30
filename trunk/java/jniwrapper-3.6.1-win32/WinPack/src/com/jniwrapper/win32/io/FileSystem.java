/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.io;

import com.jniwrapper.*;
import com.jniwrapper.util.EnumItem;
import com.jniwrapper.util.Enums;
import com.jniwrapper.util.FlagSet;
import com.jniwrapper.win32.FunctionName;
import com.jniwrapper.win32.system.Kernel32;

import java.io.File;

/**
 * @author Alexander Evsukov
 */
public class FileSystem
{
    static final FunctionName FUNCTION_GET_DRIVE_TYPE = new FunctionName("GetDriveType");
    static final FunctionName FUNCTION_GET_DISK_FREE_SPACE = new FunctionName("GetDiskFreeSpaceEx");
    static final FunctionName FUNCTION_GET_FILE_ATTRIBUTES = new FunctionName("GetFileAttributes");
    static final FunctionName FUNCTION_SET_FILE_ATTRIBUTES = new FunctionName("SetFileAttributes");
    static final FunctionName FUNCTION_GetVolumeInformation = new FunctionName("GetVolumeInformation");

    /**
     * FileAttributes class represents a set of file attributes.
     */
    public static class FileAttributes extends FlagSet
    {
        /**
         * The file or directory is read-only.
         */
        public static final int READONLY = 0x00000001;
        /**
         * The file or directory is hidden.
         */
        public static final int HIDDEN = 0x00000002;
        /**
         * The file or directory that the operating system uses a part of, or uses exclusively.
         */
        public static final int SYSTEM = 0x00000004;
        /**
         * The handle that identifies a directory.
         */
        public static final int DIRECTORY = 0x00000010;
        /**
         * The file or directory that is an archive.
         */
        public static final int ARCHIVE = 0x00000020;
        /**
         * The file or directory that is encrypted.
         */
        public static final int ENCRYPTED = 0x00000040;
        /**
         * The file or directory does not have other attributes set.
         */
        public static final int NORMAL = 0x00000080;
        /**
         * The file is used for temporary storage.
         */
        public static final int TEMPORARY = 0x00000100;
        /**
         * The file is a sparse file.
         */
        public static final int SPARSE_FILE = 0x00000200;
        /**
         * The file or directory has an associated reparse point
         */
        public static final int REPARSE_POINT = 0x00000400;
        /**
         * The file or directory is compressed.
         */
        public static final int COMPRESSED = 0x00000800;
        /**
         *  The data of a file is not available immediately.
         */
        public static final int OFFLINE = 0x00001000;
        /**
         * The file is not to be indexed by the content indexing service.
         */
        public static final int NOT_CONTENT_INDEXED = 0x00002000;

        public FileAttributes(long flags)
        {
            super(flags);
        }

        public boolean isReadOnly()
        {
            return contains(READONLY);
        }

        public boolean isArchive()
        {
            return contains(ARCHIVE);
        }

        public boolean isSystem()
        {
            return contains(SYSTEM);
        }

        public boolean isHidden()
        {
            return contains(HIDDEN);
        }
    }

    /**
     * DriveTypes class represents the enumeration of drive types.
     */
    public static class DriveType extends EnumItem
    {
        /**
         * Drive type cannot be determined
         */
        public static final DriveType UNKNOWN = new DriveType(0);
        /**
         * Root path is invalid
         */
        public static final DriveType NO_ROOT_DIR = new DriveType(1);
        /**
         * Drive is removable media (floppy disk, flas card etc.)
         */
        public static final DriveType REMOVABLE = new DriveType(2);
        /**
         * Drive is fixed media; for example a hard drive.
         */
        public static final DriveType FIXED = new DriveType(3);
        /**
         * Drive is network resource.
         */
        public static final DriveType REMOTE = new DriveType(4);
        /**
         * CD/DVD-ROM drive
         */
        public static final DriveType CDROM = new DriveType(5);
        /**
         * Virtual RAM-disk
         */
        public static final DriveType RAMDISK = new DriveType(6);

        private DriveType(int value)
        {
            super(value);
        }
    }

    /**
     * Returns the drive type of a specified disk.
     *
     * @param diskName is a drive letter like A:\, C:\ etc. i.e. root folder. If
     *                 the passed path is not a root folder, the function will not determine the drive type
     *                 and will return DRIVE_NO_ROOT_DIR type.
     * @return the drive type specified in the {@link DriveType} enumeration class.
     */
    public static DriveType getDriveType(String diskName)
    {
        Function function = Kernel32.getInstance().getFunction(FUNCTION_GET_DRIVE_TYPE.toString());
        UInt result = new UInt();
        function.invoke(result, new Str(diskName));
        return (DriveType)Enums.getItem(DriveType.class, (int)result.getValue());
    }

    /**
     * Returns the drive type of a specefied disk.
     *
     * @param drive is a root file like A:\, C:\ etc., otherwise if the passed
     *              file is not a root folder, the function will not determine the drive type and will
     *              return DRIVE_NO_ROOT_DIR type.
     * @return drive type specified in the {@link DriveType} enumeration class.
     */
    public static DriveType getDriveType(File drive)
    {
        return getDriveType(drive.getAbsolutePath());
    }

    /**
     * Returns the amount of space available on the disk in bytes.
     *
     * @param path specifies the directory on the disk.
     * @return the number of free bytes available on the disk.
     */
    public static long getDiskFreeSpace(String path)
    {
        Function function = Kernel32.getInstance().getFunction(FUNCTION_GET_DISK_FREE_SPACE.toString());
        Bool result = new Bool();
        Int64 freeToCaller = new Int64();
        function.invoke(result,
                new Str(path),
                new Pointer(freeToCaller),
                new Pointer(new Int64()),
                new Pointer.Const(new Int64(), true));
        return freeToCaller.getValue();
    }

    /**
     * Returns the amount of space available on the disk in bytes.
     *
     * @param path specifies the directory on the disk.
     * @return the number of free bytes available on the disk.
     */
    public static long getDiskFreeSpace(File path)
    {
        return getDiskFreeSpace(path.getAbsolutePath());
    }

    /**
     * Returns the total size of the disk in bytes.
     *
     * @param path specifies the directory on the disk.
     * @return the number of bytes available on the disk.
     */
    public static long getDiskTotalSize(String path)
    {
        Function function = Kernel32.getInstance().getFunction(FUNCTION_GET_DISK_FREE_SPACE.toString());
        Bool result = new Bool();
        Int64 totalSize = new Int64();
        function.invoke(result,
                new Str(path),
                new Pointer(null, true),
                new Pointer(totalSize),
                new Pointer(null, true));
        return totalSize.getValue();
    }

    /**
     * Returns the total size of the disk in bytes.
     *
     * @param path specifies directory on the disk.
     * @return the number of bytes available on the disk.
     */
    public static long getDiskTotalSize(File path)
    {
        return getDiskTotalSize(path.getAbsolutePath());
    }

    /**
     * Retrieves {@link FileAttributes} from the file specified by fileName.
     *
     * @param fileName is the file name.
     * @return FileAttributes of a specified file.
     */
    public static FileAttributes getFileAttributes(String fileName)
    {
        Function function = Kernel32.getInstance().getFunction(FUNCTION_GET_FILE_ATTRIBUTES.toString());
        UInt32 result = new UInt32();
        final Str fName = new Str(fileName, Kernel32.getInstance().isUnicode());
        function.invoke(result, fName);
        return new FileAttributes(result.getValue());
    }

    /**
     * Retrieves {@link FileAttributes} from the specified file.
     *
     * @param file
     * @return FileAttributes of a specified file.
     */
    public static FileAttributes getFileAttributes(File file)
    {
        return getFileAttributes(file.getAbsolutePath());
    }

    /**
     * Sets attributes for the specified file.
     *
     * @param fileName
     * @param attributes new file attributes
     * @return if the function is executed successfully, the result is true; otherwise
     *         false.
     */
    public static boolean setFileAttributes(String fileName, FileAttributes attributes)
    {
        Function function = Kernel32.getInstance().getFunction(FUNCTION_SET_FILE_ATTRIBUTES.toString());
        Bool result = new Bool();
        final Str fName = new Str(fileName, Kernel32.getInstance().isUnicode());
        function.invoke(result, fName, new UInt32(attributes.getFlags()));
        return result.getValue();
    }

    /**
     * Sets attributes for the specified file.
     *
     * @param file
     * @param attributes new file attributes
     * @return if the function is executed successfully, the result is true; otherwise
     *         false.
     */
    public static boolean setFileAttributes(File file, FileAttributes attributes)
    {
        return setFileAttributes(file.getAbsolutePath(), attributes);
    }

    /**
     * This function retrieves Serial Number of any drive.
     *
     * @param drive drive to retrieve the SN for.
     * @return serial number
     */
    public static long getSerialNumber(File drive)
    {
        final Function getVolumeInformation = Kernel32.getInstance().getFunction(FUNCTION_GetVolumeInformation.toString());
        Bool result = new Bool();
        Pointer nullPtr = new Pointer(null, true);
        UInt32 serialNumber = new UInt32();
        getVolumeInformation.invoke(result, new Parameter[]
        {
            new Str(drive.getAbsolutePath()),
            nullPtr,
            nullPtr,
            new Pointer(serialNumber),
            nullPtr,
            nullPtr,
            nullPtr,
            nullPtr
        });
        if (result.getValue())
        {
            return serialNumber.getValue();
        }
        else
        {
            return 0;
        }
    }

    /**
     * This function retrieves the volume label of the drive.
     *
     * @param drive the drive to retrieve the volume label for.
     * @return the volume label
     */
    public static String getVolumeLabel(File drive)
    {
        final Function getVolumeInformation = Kernel32.getInstance().getFunction(FUNCTION_GetVolumeInformation.toString());
        Bool result = new Bool();
        Pointer nullPtr = new Pointer(null, true);
        Str volumeLabel = new Str(255);
        getVolumeInformation.invoke(result, new Parameter[]
        {
            new Str(drive.getAbsolutePath()),
            new Pointer(volumeLabel),
            new UInt32(255),
            nullPtr,
            nullPtr,
            nullPtr,
            nullPtr,
            nullPtr
        });
        if (result.getValue())
        {
            return volumeLabel.getValue();
        }
        else
        {
            return "";
        }
    }
}

/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
import com.jniwrapper.win32.io.FileSystem;
import com.jniwrapper.win32.shell.ShellIcon;

import java.io.File;
import java.awt.image.BufferedImage;

/**
 * This sample prints out a piece of information about
 * each local drive.
 *  
 * @author Serge Piletsky
 * @author Vladimir Kondrashchenko
 */
public class FileSystemSample
{
    public static final String[] DRIVE_TYPE_NAMES = {"Unknown", "", "Removable", "Fixed", "Remote", "CD/DVD ROM", "RAM"};

    public static void main(String[] args)
    {
        File[] drives = File.listRoots();
        for (int i = 0; i < drives.length; i++)
        {
            File drive = drives[i];
            System.out.println("Analyzing drive " + drive + "...");

            // Determine the drive type
            FileSystem.DriveType driveType = FileSystem.getDriveType(drive);
            System.out.println("\tDrive type = " + DRIVE_TYPE_NAMES[driveType.getValue()]);

            // Determine the free space on each drive
            long diskFreeSpace = FileSystem.getDiskFreeSpace(drive);
            System.out.println("\tFree disk space = " + diskFreeSpace  + " Bytes");

            // Retrieve the serial number for each drive
            long serialNumber = FileSystem.getSerialNumber(drive);
            System.out.println("\tSerial number = " + serialNumber);

            //Getting a drive name
            String driveName = drives[i].getAbsolutePath();
            System.out.println("\tDrive name: " + driveName);

            //Getting a drive label
            String label = FileSystem.getVolumeLabel(drives[i]);
            System.out.println("\tDrive label: " + label);

            //Getting a small drive icon. In the same way can be get a small icon of a folder or a file as well.
            ShellIcon icon = new ShellIcon(drives[i].getAbsolutePath());
            BufferedImage image = icon.toImage();
            System.out.println("\tIcon size: " + image.getWidth() + "x" + image.getHeight());

            System.out.println();
        }
    }
}

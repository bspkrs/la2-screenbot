/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
import com.jniwrapper.win32.io.FileSystemEvent;
import com.jniwrapper.win32.io.FileSystemEventListener;
import com.jniwrapper.win32.io.FileSystemWatcher;

import java.io.File;
import java.text.MessageFormat;

/**
 * Sample for FileSystemWatcher. All watcher messages are put into console until the messagebox is closed
 * First command line parameter is folder to be watched. If it is not specified the current folder will be used.
 *
 * @author Serge Piletsky
 */
public class FileSystemWatcherSample
{
    static final String[] ACTION_NAMES = new String[]{"added", "removed", "modified", "renamed"};
    static final String MESSAGE_TEMPLATE = "File ''{0}'' was {1}";

    private FileSystemWatcherSample()
    {
    }

    public static void main(String[] args)
    {
        String folder = new File("C:/").getAbsolutePath();
        if (args.length > 0)
        {
            File f = new File(args[0]);
            if (f.exists())
            {
                folder = args[0];
            }
        }
        System.out.println("Watched folder = " + folder);
        FileSystemWatcher watcher = new FileSystemWatcher(folder, true);
        watcher.addFileSystemListener(new FileSystemEventListener()
        {
            public void handle(FileSystemEvent event)
            {
                int action = event.getAction();
                String actionName = ACTION_NAMES[action - 1];
                String message = MessageFormat.format(MESSAGE_TEMPLATE, new Object[]{event.getFileInfo(), actionName});
                if (action == FileSystemEvent.FILE_RENAMED)
                {
                    message += " from '" + event.getOldFileInfo() + "'";
                }

                System.out.println(message);
            }
        });

        try
        {
            watcher.start();
            System.out.println("Watching stared. Press 'Enter' to stop the watching.");
            System.in.read();
            watcher.stop();
            System.out.println("Watching stoped");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
import com.jniwrapper.win32.ui.dialogs.SelectFolderDialog;

/**
 * This sample demonstrates how to work with {@link SelectFolderDialog}.
 *
 * @author Serge Piletsky
 */
public class SelectFolderDialogSample
{
    public static void main(String[] args)
    {
        SelectFolderDialog dialog = new SelectFolderDialog("Select folder");
        dialog.setFolder("C:\\");
        dialog.getOptions().setShowNetworkFolders(true);
        final boolean result = dialog.execute();
        System.out.println("result = " + result);
        if (result)
        {
            final String directory = dialog.getFolder();
            System.out.println("Selected folder = " + directory);
        }
    }
}

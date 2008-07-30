/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
import com.jniwrapper.DefaultLibraryLoader;
import com.jniwrapper.win32.ui.dialogs.OpenSaveFileDialog;

/**
 * This sample demonstrates how to work with {@link OpenSaveFileDialog}.
 *
 * @author Serge Piletsky
 */
public class OpenSaveFileDialogSample
{
    public static void main(String[] args)
    {
        OpenSaveFileDialog dialog = new OpenSaveFileDialog("Select File", "C:\\");
        dialog.setFilter("XML Files (*.xml)|*.xml|Pictures|*.bmp;*.gif;*.jpeg|All Files (*.*)|*.*");
        dialog.getOptions().setMultiselectionAllowed(true);
        final boolean result = dialog.getOpenFileName();
        System.out.println("result = " + result);
        if (result)
        {
            System.out.println("dialog.getFileName() = " + dialog.getFileName());
            System.out.println("dialog.getFiles() = " + dialog.getFiles());
        }
    }
}

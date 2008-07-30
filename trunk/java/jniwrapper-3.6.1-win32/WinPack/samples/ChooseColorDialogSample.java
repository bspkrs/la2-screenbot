/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
import com.jniwrapper.DefaultLibraryLoader;
import com.jniwrapper.win32.ui.dialogs.ChooseColorDialog;

import java.awt.*;

/**
 * This sample demonstrates how to work with {@link ChooseColorDialog}.
 *
 * @author Serge Piletsky
 */
public class ChooseColorDialogSample
{
    public static void main(String[] args)
    {
        ChooseColorDialog dialog = new ChooseColorDialog();
        // customize dialog appearance
        dialog.getOptions().setFullOpen(true);
        // customize initially selected color
        dialog.setColor(Color.yellow);
        // customize custom colors
        dialog.getCustomColors().add(Color.red);
        dialog.getCustomColors().add(Color.green);
        dialog.getCustomColors().add(Color.blue);
        final boolean result = dialog.execute();
        System.out.println("Result = " + result);
        if (result)
        {
            System.out.println("The selected color is " + dialog.getColor());
            System.out.println("The custom colors are: " + dialog.getCustomColors());
        }
    }
}

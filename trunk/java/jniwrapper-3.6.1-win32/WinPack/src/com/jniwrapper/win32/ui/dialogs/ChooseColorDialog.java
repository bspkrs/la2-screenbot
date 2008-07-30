/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.ui.dialogs;

import com.jniwrapper.*;
import com.jniwrapper.util.FlagSet;
import com.jniwrapper.win32.FunctionName;
import com.jniwrapper.win32.Msg;
import com.jniwrapper.win32.gdi.ColorRef;
import com.jniwrapper.win32.system.Module;
import com.jniwrapper.win32.ui.Wnd;

import java.awt.Color;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

/**
 * ChooseColorDialog is a wrapper for the ChooseColor native dialog.
 * 
 * @author Serge Piletsky
 */
public class ChooseColorDialog
{
    static final FunctionName FUNCTION_CHOOSE_COLOR = new FunctionName("ChooseColor");

    private Options _options = new Options();
    private Window _owner = null;
    private Color _color = null;
    private List _customColors = new ArrayList(ChooseColorStructure.CUSTOM_COLORS_COUNT);
    private Callback _dialogCallback;

    public ChooseColorDialog()
    {
        this(null);
    }

    public ChooseColorDialog(Window owner)
    {
        setOwner(owner);
    }

    public Window getOwner()
    {
        return _owner;
    }

    /**
     * Sets the owner of the dialog.
     * 
     * @param owner
     */
    public void setOwner(Window owner)
    {
        _owner = owner;
    }

    /**
     * 
     * @return {@link Options} that contains dialog options. These options
     * customize the dialog behavior and style.
     */
    public Options getOptions()
    {
        return _options;
    }

    /**
     * Returns a selected color.
     * 
     * @return selected color.
     */
    public Color getColor()
    {
        return _color;
    }

    /**
     * Sets the default selected color.
     * 
     * @param color is color to be selected by default
     */
    public void setColor(Color color)
    {
        _color = color;
    }

    /**
     * Returns the list of custom colors selected by user. Custom colors can be
     * specified before the dialog execution. Note that only 16 custom colors can be
     * specified and returned.
     * 
     * @return list of custom colors
     */
    public List getCustomColors()
    {
        return _customColors;
    }

    /**
     * Fills the specified structure with custom colors.
     * 
     * @param colorStructure
     */
    private void setCustomColors(ChooseColorStructure colorStructure)
    {
        final int count = _customColors.size();
        int[] customColors = new int[count];
        for (int i = 0; i < count; i++)
        {
            int color = ColorRef.toNativeColor((Color)_customColors.get(i));
            customColors[i] = color;
        }
        colorStructure.setCustomColors(customColors);
    }

    /**
     * Retrieves custom colors and fills the custom color list from the specified
     * structure.
     * 
     * @param colorStructure
     */
    private void getCustomColors(ChooseColorStructure colorStructure)
    {
        _customColors.clear();
        final int[] customColors = colorStructure.getCustomColors();
        for (int i = 0; i < customColors.length; i++)
        {
            int color = customColors[i];
            _customColors.add(ColorRef.fromNativeColor(color));
        }
    }

    protected Callback getDialogCallback()
    {
        if (_dialogCallback == null)
        {
            _dialogCallback = new DefaultDialogCallback()
            {
                public void callback()
                {
                    _returnParam.setValue(0);
                    final long msg = _msg.getValue();
                    if (msg == Msg.WM_INITDIALOG)
                    {
                        if (!_wnd.isNull())
                            _wnd.centerInDesktop();
                    }
                }
            };
        }
        return _dialogCallback;
    }

    /**
     * Opens the dialog.
     * 
     * @return true if color selected, otherwise false
     */
    public boolean execute()
    {
        final ChooseColorStructure colorStructure = new ChooseColorStructure(getDialogCallback());

        colorStructure.setInstance(Module.getCurrent());
        final Window owner = getOwner();
        if (owner != null)
        {
            colorStructure.setOwner(new Wnd(owner));
        }

        if (_color != null)
        {
            final ColorRef colorRef = new ColorRef(_color);
            colorStructure.setColor(colorRef);
        }
        setCustomColors(colorStructure);
        colorStructure.setFlags(_options.getFlags() | Options.ENABLEHOOK);

        final Function function = ComDlg32.getInstance().getFunction(FUNCTION_CHOOSE_COLOR.toString());
        final Bool result = new Bool();

        if (owner == null)
            function.invoke(result, new Pointer(colorStructure));
        else
            DialogHelper.invokeDialog(owner, function, result, new Parameter[]{new Pointer(colorStructure)});

        if (result.getValue())
        {
            final ColorRef colorRef = colorStructure.getColor();
            _color = colorRef.toColor();
            getCustomColors(colorStructure);
        }
        return result.getValue();
    }

    /**
     *  Options class represents options set for ChooseColorDialog
     */
    public class Options extends FlagSet
    {
        public static final int RGBINIT = 0x00000001;
        public static final int FULLOPEN = 0x00000002;
        public static final int PREVENTFULLOPEN = 0x00000004;
        public static final int SHOWHELP = 0x00000008;
        public static final int ENABLEHOOK = 0x00000010;
        public static final int ENABLETEMPLATE = 0x00000020;
        public static final int ENABLETEMPLATEHANDLE = 0x00000040;
        public static final int SOLIDCOLOR = 0x00000080;
        public static final int ANYCOLOR = 0x00000100;

        public Options()
        {
            super();
            reset();
        }

        public void reset()
        {
            clear();
            add(RGBINIT);
        }

        /**
         * Customize the dialog to display additional controls.
         * 
         * @param value
         */
        public void setFullOpen(boolean value)
        {
            if (value)
                add(FULLOPEN);
            else
                remove(FULLOPEN);
        }

        /**
         * Disables or enables the Define Custom Color button.
         * 
         * @param value
         */
        public void setPreventFullOpen(boolean value)
        {
            if (value)
                add(PREVENTFULLOPEN);
            else
                remove(PREVENTFULLOPEN);
        }

        /**
         * Customize the dialog to show only solid colors.
         * 
         * @param value
         */
        public void setSolidColor(boolean value)
        {
            if (value)
                add(SOLIDCOLOR);
            else
                remove(SOLIDCOLOR);
        }

        /**
         * Customize the dialog to show all available colors.
         * 
         * @param value
         */
        public void setAnyColor(boolean value)
        {
            if (value)
                add(ANYCOLOR);
            else
                remove(ANYCOLOR);
        }
    }
}

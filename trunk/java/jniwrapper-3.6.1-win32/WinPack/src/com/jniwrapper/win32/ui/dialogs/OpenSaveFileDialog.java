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
import com.jniwrapper.win32.system.Kernel32;
import com.jniwrapper.win32.ui.Wnd;

import java.awt.Window;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * OpenSaveFileDialog is a wrapper for OpenFileName and SaveFileName native
 * dialogs.
 * 
 * @author Serge Piletsky
 */
public class OpenSaveFileDialog
{
    public static final char DEFAULT_FILTER_SEPARATOR = '|';

    static final FunctionName FUNCTION_OPEN_FILENAME = new FunctionName("GetOpenFileName");
    static final FunctionName FUNCTION_SAVE_FILENAME = new FunctionName("GetSaveFileName");

    // File Edit Styles
    public static final int FES_EDIT = 0;
    public static final int FES_COMBOBOX = 1;

    static final int MAX_PATH = 260;
    static final int MULTI_SELECT_BUFFER_SIZE = 256*256 - 16;

    static final char EXTENSION_SEPARATOR = '.';

    private OpenSaveFileDialogOptions _options = new OpenSaveFileDialogOptions();
    private OpenSaveFileDialogExtOptions _extOptions = new OpenSaveFileDialogExtOptions();
    private String _filter = "";
    private long _filterIndex = 1;
    private long _currentFilterIndex = 1;
    private String _initialDir;
    private String _title;
    private String _defaultExt = "";
    private String _fileName = "";
    private List _files = new ArrayList();
    private int _fileEditStyle = FES_EDIT;
    private boolean _forceCurrentDirectory = true;
    private Window _owner = null;
    private long _errorCode = 0;
    private char _filterSeparator = DEFAULT_FILTER_SEPARATOR;
    private Callback _dialogCallback;

    public OpenSaveFileDialog(Window owner, String title, String initialDirectory)
    {
        setOwner(owner);
        setTitle(title);
        setInitialDir(initialDirectory);
    }

    public OpenSaveFileDialog()
    {
        this(null, "", "");
    }

    public OpenSaveFileDialog(String title)
    {
        this(null, title, "");
    }

    public OpenSaveFileDialog(Window owner)
    {
        this(owner, "");
    }

    public OpenSaveFileDialog(Window owner, String title)
    {
        this(owner, title, "");
    }

    public OpenSaveFileDialog(Window owner, String title, File initialDirectory)
    {
        this(owner, title, initialDirectory==null?"":initialDirectory.getAbsolutePath());
    }

    public OpenSaveFileDialog(String title, String initialDirectory)
    {
        this(null, title, initialDirectory);
    }

    public OpenSaveFileDialog(File initialDirectory)
    {
        this(null, "", initialDirectory);
    }

    public OpenSaveFileDialog(Window owner, File initialDirectory)
    {
        this(owner, "", initialDirectory);
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

    public char getFilterSeparator()
    {
        return _filterSeparator;
    }

    public void setFilterSeparator(char filterSeparator)
    {
        _filterSeparator = filterSeparator;
    }

    /**
     * 
     * @return {@link OpenSaveFileDialogOptions} that contains standard dialog
     * option. These options customize the dialog behavior and style.
     */
    public OpenSaveFileDialogOptions getOptions()
    {
        return _options;
    }

    public String getFilter()
    {
        return _filter;
    }

    /**
     * Sets filter string. Filter string should have FilterName|Extension
     * format. Several filters should be delimeted by filter delimeter character
     * '|'. Several extensions should be delimeted by ';' symbol. Example: "XML
     * Files|*.utils|Pictures|*.bmp;*.gif;*.jpeg"
     * 
     * @param filter
     */
    public void setFilter(String filter)
    {
        _filter = filter;
    }

    /**
     * Translates filter string like XML|*.utils to the native presentation.
     * 
     * @param filter
     * @return translated string.
     */
    StringBuffer translateFilter(String filter)
    {
        StringBuffer newFilter = new StringBuffer(filter);
        if (filter.length() > 0)
        {
            // double \0 terminators
            newFilter.append('\0');
            newFilter.append('\0');
            for (int i = 0; i < filter.length(); i++)
            {
                char c = newFilter.charAt(i);
                if (c == _filterSeparator)
                {
                    newFilter.setCharAt(i, '\0');
                }
            }
        }
        return newFilter;
    }

    /**
     * 
     * @return selected filter index.
     */
    public long getFilterIndex()
    {
        return _filterIndex;
    }

    /**
     * Sets the index of the filter that will be selected in the dialog.
     * 
     * @param filterIndex
     */
    public void setFilterIndex(long filterIndex)
    {
        _filterIndex = filterIndex;
    }

    public long getCurrentFilterIndex()
    {
        return _currentFilterIndex;
    }

    /**
     * 
     * @param currentFilterIndex
     */
    public void setCurrentFilterIndex(long currentFilterIndex)
    {
        _currentFilterIndex = currentFilterIndex;
    }

    public String getInitialDir()
    {
        return _initialDir;
    }

    /**
     * Sets initial directory where the diloag will be opened.
     * 
     * @param initialDir
     */
    public void setInitialDir(String initialDir)
    {
        _initialDir = initialDir;
    }

    public String getTitle()
    {
        return _title;
    }

    /**
     * Sets the title of the dialog.
     * 
     * @param title
     */
    public void setTitle(String title)
    {
        _title = title;
    }

    public String getDefaultExt()
    {
        return _defaultExt;
    }

    public void setDefaultExt(String defaultExt)
    {
        _defaultExt = defaultExt;
    }

    /**
     * 
     * @return selected file name. If multiselection is on, it returns the first
     * file name from the list of selected ones.
     */
    public String getFileName()
    {
        return _fileName;
    }

    /**
     * Sets the predefined file name.
     * 
     * @param fileName
     */
    public void setFileName(String fileName)
    {
        _fileName = fileName;
    }

    /**
     * 
     * @return a list of files. If multiselection is disallowed, it contains
     * only one selected file; otherwise it contains all selected files.
     */
    public List getFiles()
    {
        return _files;
    }

    public int getFileEditStyle()
    {
        return _fileEditStyle;
    }

    public void setFileEditStyle(int fileEditStyle)
    {
        _fileEditStyle = fileEditStyle;
    }

    /**
     * 
     * @return {@link OpenSaveFileDialogExtOptions} contains extended dialog
     * options. These options work only on Win2k, WinMe or greater.
     */
    protected OpenSaveFileDialogExtOptions getExtOptions()
    {
        return _extOptions;
    }

    public boolean isForceCurrentDirectory()
    {
        return _forceCurrentDirectory;
    }

    /**
     * Forces dialog to open the current directory.
     * 
     * @param forceCurrentDirectory
     */
    public void setForceCurrentDirectory(boolean forceCurrentDirectory)
    {
        _forceCurrentDirectory = forceCurrentDirectory;
    }

    /**
     * Returns extension of the file whose name is passed as a parameter.
     */
    static String getFileExtension(String fileName)
    {
      int separatorIndex = fileName.lastIndexOf(EXTENSION_SEPARATOR);
      int firstExtensionSymbolIndex = separatorIndex + 1;
      return fileName.substring(firstExtensionSymbolIndex);
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
                        Wnd handle = new Wnd(_wnd.getWindowLong(Wnd.GWL_HWNDPARENT));
                        if (!handle.isNull())
                            handle.centerInDesktop();
                    }
                }
            };
        }
        return _dialogCallback;
    }

    /**
     * Executes the function passed as a parameter.
     * 
     * @param function
     * @return true if the function is executed successfully.
     */
    protected boolean doExecute(Function function)
    {
        if (function == null)
            throw new IllegalArgumentException("Function is null");

        String currentDirectory = Kernel32.getCurrentDirectory();

        _files.clear();

        int maxFile = _options.isMultiselectionAllowed()?MULTI_SELECT_BUFFER_SIZE:MAX_PATH;
        OFNStructure ofn = new OFNStructure(getDialogCallback(), maxFile);

        ofn.setFilterIndex(_filterIndex);
        ofn.setFilter(translateFilter(_filter));
        _currentFilterIndex = _filterIndex;

        ofn.setFile(_fileName);

        if ((_initialDir.length() == 0) && _forceCurrentDirectory)
            ofn.setInitialDir(".");
        else
            ofn.setInitialDir(_initialDir);

        ofn.setTitle(_title);

        long flags = OpenSaveFileDialogOptions.ENABLEHOOK | _options.getFlags();
        ofn.setFlagsEx(_extOptions.getFlags());

        String extension = _defaultExt;
        if ((extension.length() == 0) && ((flags & OpenSaveFileDialogOptions.EXPLORER) == 0))
        {
            extension = getFileExtension(_fileName);
        }
        if (extension.length() > 0)
        {
            ofn.setDefExt(extension);
        }
        if (_owner != null)
        {
            final Wnd owner = new Wnd(_owner);
            ofn.setOwner(owner);
        }
        if (_options.isMultiselectionAllowed())
        {
            flags |= OpenSaveFileDialogOptions.ALLOWMULTISELECT;
        }
        ofn.setFlags(flags);

        Bool result = new Bool();
        if (_owner != null)
            DialogHelper.invokeDialog(_owner, function, result, new Parameter[] {new Pointer(ofn)});
        else
            function.invoke(result, new Pointer(ofn));
        if (result.getValue())
        {
            getFiles(ofn);
            if ((ofn.getFlags() & OpenSaveFileDialogOptions.EXTENSIONDIFFERENT) != 0)
                _options.add(OpenSaveFileDialogOptions.EXTENSIONDIFFERENT);
            else
                _options.remove(OpenSaveFileDialogOptions.EXTENSIONDIFFERENT);

//            if ((ofn.getFlags() & OpenSaveFileDialogOptions.READONLY) != 0)
//                _options.add(OpenSaveFileDialogOptions.READONLY);
//            else
//                _options.remove(OpenSaveFileDialogOptions.READONLY);

            _filterIndex = ofn.getFilterIndex();
        }
        _errorCode = ComDlg32.getCommDlgExtendedError();

        Kernel32.setCurrentDirectory(currentDirectory);
        
        return result.getValue();
    }

    public long getErrorCode()
    {
        return _errorCode;
    }

    /**
     * Retrieves file names from the parameter.
     * 
     * @param ofnStructure
     */
    protected void getFiles(OFNStructure ofnStructure)
    {
        String fileString = ofnStructure.getFile();

        if(!_options.isShowExplorer() && _options.isMultiselectionAllowed())
        {
            fileString = fileString.replaceAll("\\.\t","\t").replaceAll("\\.$","")
                    .replaceAll("\t.*$","").replaceAll("\\. ","\t").replaceAll(" ","\t");
        }

        StringTokenizer tokenizer = new StringTokenizer(fileString, String.valueOf(OFNStructure.FILES_DELIMETER));

        if (_options.isMultiselectionAllowed())
        {
            String folderName = tokenizer.nextToken();
            while (tokenizer.hasMoreTokens())
            {
                String fileName = tokenizer.nextToken();
                File file = new File(folderName, fileName);
                _files.add(file);
            }
            if (_files.isEmpty())
            {
                _files.add(new File(folderName));
            }
            _fileName = _files.get(0).toString();
        }
        else
        {
            _fileName = tokenizer.nextToken();
            _files.add(new File(_fileName));
        }
    }


    /**
     * Calls the OpenFileName dialog.
     * 
     * @return true if file(s) is selected; otherwise false.
     */
    public boolean getOpenFileName()
    {
        return doExecute(ComDlg32.getInstance().getFunction(FUNCTION_OPEN_FILENAME.toString()));
    }

    /**
     * Calls SaveFileName dialog.
     * 
     * @return true if file(s) is selected; otherwise false.
     */
    public boolean getSaveFileName()
    {
        return doExecute(ComDlg32.getInstance().getFunction(FUNCTION_SAVE_FILENAME.toString()));
    }

    public class OpenSaveFileDialogOptions extends FlagSet
    {
        public static final int READONLY            = 0x00000001;
        public static final int OVERWRITEPROMPT     = 0x00000002;
        public static final int HIDEREADONLY        = 0x00000004;
        public static final int NOCHANGEDIR         = 0x00000008;
        public static final int SHOWHELP            = 0x00000010;
        public static final int ENABLEHOOK          = 0x00000020;
        public static final int ENABLETEMPLATE      = 0x00000040;
        public static final int ENABLETEMPLATEHANDLE= 0x00000080;
        public static final int NOVALIDATE          = 0x00000100;
        public static final int ALLOWMULTISELECT    = 0x00000200;
        public static final int EXTENSIONDIFFERENT  = 0x00000400;
        public static final int PATHMUSTEXIST       = 0x00000800;
        public static final int FILEMUSTEXIST       = 0x00001000;
        public static final int CREATEPROMPT        = 0x00002000;
        public static final int SHAREAWARE          = 0x00004000;
        public static final int NOREADONLYRETURN    = 0x00008000;
        public static final int NOTESTFILECREATE    = 0x00010000;
        public static final int NONETWORKBUTTON     = 0x00020000;
        public static final int NOLONGNAMES         = 0x00040000;     // force no long names for 4.x modules
        public static final int EXPLORER            = 0x00080000;     // new look commdlg
        public static final int NODEREFERENCELINKS  = 0x00100000;
        public static final int LONGNAMES           = 0x00200000;     // force long names for 3.x modules
        public static final int ENABLEINCLUDENOTIFY = 0x00400000;     // send include message to callback
        public static final int ENABLESIZING        = 0x00800000;
        public static final int DONTADDTORECENT     = 0x02000000;
        public static final int FORCESHOWHIDDEN     = 0x10000000;

        public OpenSaveFileDialogOptions()
        {
            super();
            reset();
        }

        public void reset()
        {
            clear();
            add(EXPLORER | HIDEREADONLY | ENABLESIZING);
        }

        /**
         * 
         * @return true if multiple files can be selected.
         */
        public boolean isMultiselectionAllowed()
        {
            return contains(ALLOWMULTISELECT);
        }

        /**
         * Sets the dialog to allow multiple file selections.
         * 
         * @param multiselectionAllowed
         */
        public void setMultiselectionAllowed(boolean multiselectionAllowed)
        {
            if (multiselectionAllowed)
                add(ALLOWMULTISELECT);
            else
                remove(ALLOWMULTISELECT);
        }

        /**
         * 
         * @return true if the dialog is set to hide read-only check box.
         */
        public boolean isHideReadOnly()
        {
            return contains(HIDEREADONLY);
        }

        /**
         * Sets the dialog to show or hide read-only check box.
         * 
         * @param hideReadOnly
         */
        public void setHideReadOnly(boolean hideReadOnly)
        {
            if (hideReadOnly)
                add(HIDEREADONLY);
            else
                remove(HIDEREADONLY);
        }

        /**
         * 
         * @return true if the dialog is set to hide hidden files.
         */
        public boolean isShowHiddenFiles()
        {
            return contains(FORCESHOWHIDDEN);
        }

        /**
         * Sets the dialog to show hidden files.
         * 
         * @param showHiddenFiles
         */
        public void setShowHiddenFiles(boolean showHiddenFiles)
        {
            if (showHiddenFiles)
                add(FORCESHOWHIDDEN);
            else
                remove(FORCESHOWHIDDEN);
        }

        /**
         * 
         * @return true if dialog sizing is enabled.
         */
        public boolean isSizingEnabled()
        {
            return contains(ENABLESIZING);
        }

        /**
         * Sets the dialog to enable sizing.
         * 
         * @param sizingEnabled
         */
        public void setSizingEnabled(boolean sizingEnabled)
        {
            if (sizingEnabled)
                add(ENABLESIZING);
            else
                remove(ENABLESIZING);
        }

        /**
         * 
         * @return true if the dialog is set to show the overwrite prompt.
         */
        public boolean isShowOverwritePrompt()
        {
            return contains(OVERWRITEPROMPT);
        }

        /**
         * If true, causes the Save As dialog box to generate a message box if
         * the selected file already exists. The user must confirm whether to
         * overwrite the file.
         * 
         * @param showOverwritePrompt
         */
        public void setShowOverwritePrompt(boolean showOverwritePrompt)
        {
            if (showOverwritePrompt)
                add(OVERWRITEPROMPT);
            else
                remove(OVERWRITEPROMPT);
        }

        /**
         *
         * @return true if the dialog is set to show the explorer.
         */
        public boolean isShowExplorer()
        {
            return contains(EXPLORER);
        }

        /**
         * If true, the explorer will be shown.
         *
         * @param showExplorer
         */
        public void setShowExplorer(boolean showExplorer)
        {
            if (showExplorer)
                add(EXPLORER);
            else
                remove(EXPLORER);
        }

        /**
         *
         * @return true if the dialog is set to show help button in the system menu.
         */
        public boolean isShowHelp()
        {
            return contains(SHOWHELP);
        }

        /**
         * If true, the help button in the system menu will be shown.
         *
         * @param showHelp
         */
        public void setShowHelp(boolean showHelp)
        {
            if (showHelp)
                add(SHOWHELP);
            else
                remove(SHOWHELP);
        }

    }

    public class OpenSaveFileDialogExtOptions extends FlagSet
    {
        public static final int OFN_EX_NOPLACESBAR      = 0x00000001;

        public OpenSaveFileDialogExtOptions()
        {
            super();
        }

        /**
         * 
         * @return true if the dialog is set to show a place bar.
         */
        public boolean isShowPlacebar()
        {
            return !contains(OFN_EX_NOPLACESBAR);
        }

        /**
         * Sets the dialog to show a place bar.
         * 
         * @param showPlacebar
         */
        public void setShowPlacebar(boolean showPlacebar)
        {
            if (showPlacebar)
                remove(OFN_EX_NOPLACESBAR);
            else
                add(OFN_EX_NOPLACESBAR);
        }
    }
}

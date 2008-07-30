/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.io;

import com.jniwrapper.util.Logger;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * The FolderInfo class is a container of folder information.
 * 
 * @author Serge Piletsky
 */
public class FolderInfo extends FileInfo
{
    private static final Logger LOG = Logger.getInstance(FolderInfo.class);
    
    private List _files = new ArrayList();
    private boolean _recursive = true;
    private int _fileCount = 0;
    private FileFilter _fileFilter = null;

    public FolderInfo(String fileName)
    {
        super(fileName);
    }

    public FolderInfo(String path, FileFilter filter, boolean recursive)
    {
        super(path, FileSystem.getFileAttributes(path).getFlags(), new File(path).length(), new File(path).lastModified());
        _fileFilter = filter;
        _recursive = recursive;
    }

    public FolderInfo(String path, boolean recursive)
    {
        this(path, null, recursive);
    }

    protected void loadFiles(FolderInfo result, File rootFolder)
    {
        File[] files = _fileFilter == null ? rootFolder.listFiles() : rootFolder.listFiles(_fileFilter);
        while (files == null)
        {
            try
            {
                Thread.sleep(10);
            }
            catch (InterruptedException e)
            {
                LOG.error("", e);
            }
            files = _fileFilter == null ? rootFolder.listFiles() : rootFolder.listFiles(_fileFilter);
        }
        for (int i = 0; i < files.length; i++)
        {
            File file = files[i];
            _fileCount++;
            String fileName = file.getAbsolutePath();
            if (_recursive && file.isDirectory())
            {
                FolderInfo subfolder = new FolderInfo(fileName, _recursive);
                loadFiles(subfolder, file);
                result._files.add(subfolder);
                _fileCount += subfolder.getFileCount();
            }
            else
            {
                result._files.add(new FileInfo(fileName,
                        FileSystem.getFileAttributes(fileName).getFlags(),
                        file.length(),
                        file.lastModified()));
            }
        }
    }

    public void load()
    {
        _files.clear();
        loadFiles(this, new File(getFileName()));
    }

    public int getFileCount()
    {
        return _fileCount;
    }

    public List getFiles()
    {
        return _files;
    }
}

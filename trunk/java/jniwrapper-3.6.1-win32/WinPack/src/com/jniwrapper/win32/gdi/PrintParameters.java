/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.gdi;

import com.jniwrapper.win32.PageRange;

/**
 * This class describes set of pages in document to print, number of copies to print.
 */
public class PrintParameters
{
    /**
     * Start page to print.
     */
    private int _fromPage = 1;

    /**
     * Last page to print.
     */
    private int _toPage = 1;

    /**
     * Minimum value that user can enter into "Start page", "last page" controls.
     */
    private int _minPage = 1;

    /**
     * Maximum value that user can enter into fromPage, toPage controls.
     */
    private int _maxPage = 1;

    /**
     * Number of document copies to print.
     */
    private int _numCopies = 1;
    private boolean _isPrintOddPages = true;
    private boolean _isPrintEvenPages = true;
    private PageRange[] _pageRanges;

    private boolean _collate = false;

    public PrintParameters()
    {
        _pageRanges = new PageRange[1];
        _pageRanges[0] = new PageRange();
        _pageRanges[0].setFromPage(1);
        _pageRanges[0].setToPage(1);
    }

    /**
     * Returns start page to print.
     *
     * @return start page to print.
     */
    public int getFromPage()
    {
        return _fromPage;
    }

    /**
     * Sets start page to print.
     *
     * @param fromPage number of page.
     */
    public void setFromPage(int fromPage)
    {
        _fromPage = fromPage;
    }

    /**
     * Sets start page to print.
     */
    public int getToPage()
    {
        return _toPage;
    }

    /**
     * Sets last page to print.
     *
     * @param toPage number of page.
     */
    public void setToPage(int toPage)
    {
        _toPage = toPage;
    }

    public int getMinPage()
    {
        return _minPage;
    }

    public void setMinPage(int minPage)
    {
        _minPage = minPage;
    }

    public int getMaxPage()
    {
        return _maxPage;
    }

    public void setMaxPage(int maxPage)
    {
        _maxPage = maxPage;
    }

    public int getNumCopies()
    {
        return _numCopies;
    }

    public void setNumCopies(int numCopies)
    {
        _numCopies = numCopies;
    }

    public boolean isPrintOddPages()
    {
        return _isPrintOddPages;
    }

    public void setPrintOddPages(boolean printOddPages)
    {
        _isPrintOddPages = printOddPages;
    }

    public boolean isPrintEvenPages()
    {
        return _isPrintEvenPages;
    }

    public void setPrintEvenPages(boolean printEvenPages)
    {
        _isPrintEvenPages = printEvenPages;
    }

    public PageRange[] getPageRanges()
    {
        return _pageRanges;
    }

    public void setPageRanges(PageRange[] pageRanges)
    {
        _pageRanges = pageRanges;
    }

    public boolean isCollate()
    {
        return _collate;
    }

    public void setCollate(boolean collate)
    {
        _collate = collate;
    }

    public static PrintParameters create(int startPage, int endPage, int numCopies)
    {
        PrintParameters result = new PrintParameters();

        result.setMinPage(startPage);
        result.setMaxPage(endPage);
        result.setFromPage(startPage);
        result.setToPage(endPage);

        result.setNumCopies(numCopies);

        result.setPrintOddPages(true);
        result.setPrintEvenPages(true);

        PageRange[] pageRanges = new PageRange[1];
        PageRange pageRange = new PageRange();
        pageRange.setFromPage(startPage);
        pageRange.setToPage(endPage);
        pageRanges[0] = pageRange;

        result.setPageRanges(pageRanges);

        return result;
    }
}

/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.dde;

import com.jniwrapper.util.EnumItem;

/**
 * This class is an enumeration of supported DDE responses.
 *
 * @author Vladimir Kondrashchenko
 */
public class DdeResponse extends EnumItem
{
    private static final int DDE_FACK = 0x8000;
    private static final int DDE_FBUSY = 0x4000;
    private static final int DDE_FNOTPROCESSED = 0x0000;

    /**
     * Notifies the client or service that the transaction has been processed successfully.
     */
    public static final DdeResponse PROCESSED = new DdeResponse(DDE_FACK);

    /**
     * Notifies the client or service that the transaction has not been processed because the process was busy.
     */
    public static final DdeResponse BUSY = new DdeResponse(DDE_FBUSY);

    /**
     * Notifies the client or service that the transaction has not been processed.
     */
    public static final DdeResponse NOTPROCESSED = new DdeResponse(DDE_FNOTPROCESSED);

    public DdeResponse(int value)
    {
        super(value);
    }
}

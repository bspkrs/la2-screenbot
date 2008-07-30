/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.io;

import java.util.EventListener;

/**
 * An interface for listening for file system events.
 *
 * @author Serge Piletsky
 */
public interface FileSystemEventListener extends EventListener
{
    public void handle(FileSystemEvent event);
}

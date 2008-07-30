/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.gdi;

import com.jniwrapper.*;
import com.jniwrapper.util.EnumItem;
import com.jniwrapper.util.FlagSet;
import com.jniwrapper.win32.FunctionName;
import com.jniwrapper.win32.Handle;
import com.jniwrapper.win32.LastErrorException;

// TODO [leha]: Register GdiObjects with NativeResource collector. Make sure that stock objects are not destroyed!!!
/**
 * An abstract base for all GDI objects.
 * 
 * @author Alexander Evsukov
 */
abstract public class GdiObject extends Handle
{
    /*
     * GDI error constants
     */
    public static final int GDI_ERROR = 0xFFFFFFFF;
    public static final int HGDI_ERROR = 0xFFFFFFFF;

    static final FunctionName FUNCTION_GET_OBJECT = new FunctionName("GetObject");
    static final String FUNCTION_DELETE_OBJECT = "DeleteObject";
    static final String FUNCTION_GET_OBJECT_TYPE = "GetObjectType";

    /**
     * Class ImageType represents the enumeration of windows image types.
     */
    public static class ImageType extends EnumItem
    {
        public static final ImageType BITMAP = new ImageType(0);
        public static final ImageType ICON = new ImageType(1);
        public static final ImageType CURSOR = new ImageType(2);
        public static final ImageType ENHMETAFILE = new ImageType(3);

        private ImageType(int value)
        {
            super(value);
        }
    }

    /**
     * ImageLoadParameters class.
     */
    public static class ImageLoadParameters extends FlagSet
    {
        public static final int DEFAULTCOLOR = 0x0000;
        public static final int MONOCHROME = 0x0001;
        public static final int COLOR = 0x0002;
        public static final int COPYRETURNORG = 0x0004;
        public static final int COPYDELETEORG = 0x0008;
        public static final int LOADFROMFILE = 0x0010;
        public static final int LOADTRANSPARENT = 0x0020;
        public static final int DEFAULTSIZE = 0x0040;
        public static final int VGACOLOR = 0x0080;
        public static final int LOADMAP3DCOLORS = 0x1000;
        public static final int CREATEDIBSECTION = 0x2000;
        public static final int COPYFROMRESOURCE = 0x4000;
        public static final int SHARED = 0x8000;
    }

    public GdiObject()
    {
    }

    public GdiObject(long value)
    {
        super(value);
    }

    public void deleteObject()
    {
        final Function function = Gdi32.getInstance().getFunction(FUNCTION_DELETE_OBJECT);
        Bool result = new Bool();
        long errorCode = function.invoke(result, this);
        if (!result.getValue())
        {
            throw new LastErrorException(errorCode, "Failed to delete object");
        }
    }

    public long getObject(GdiObject object, Parameter data)
    {
        final Function function = Gdi32.getInstance().getFunction(FUNCTION_GET_OBJECT.toString());
        Int result = new Int();
        final int length = data.getLength();
        long errorCode = function.invoke(result, object, new Int(length), new Pointer(data));
        if ((result.getValue() == 0) || length != result.getValue())
        {
            throw new LastErrorException(errorCode, "Failed to get object.");
        }
        return result.getValue();
    }

    /**
     * GdiObjectType class represents the enumeration of standard GDI object types.
     */
    public static class Type extends EnumItem
    {
        public static final Type PEN = new Type(1);
        public static final Type BRUSH = new Type(2);
        public static final Type DC = new Type(3);
        public static final Type METADC = new Type(4);
        public static final Type PAL = new Type(5);
        public static final Type FONT = new Type(6);
        public static final Type BITMAP = new Type(7);
        public static final Type REGION = new Type(8);
        public static final Type METAFILE = new Type(9);
        public static final Type MEMDC = new Type(10);
        public static final Type EXTPEN = new Type(11);
        public static final Type ENHMETADC = new Type(12);
        public static final Type ENHMETAFILE = new Type(13);

        private Type(int value)
        {
            super(value);
        }
    }

    /**
     * Returns GDI object type.
     * 
     * @return object type
     */
    public Type getObjectType()
    {
        UInt32 result = new UInt32();
        final Function function = Gdi32.getInstance().getFunction(FUNCTION_GET_OBJECT_TYPE);
        function.invoke(result, this);
        return new Type((int)result.getValue());
    }
}

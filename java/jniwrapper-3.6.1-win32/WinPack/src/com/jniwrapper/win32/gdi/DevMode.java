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
import com.jniwrapper.win32.PointL;
import com.jniwrapper.win32.system.VersionInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * This structure corresponds to a native <code>DEVMODE</code> structure.
 *
 * @see <a href="http://msdn.microsoft.com/library/default.asp?url=/library/en-us/gdi/prntspol_8nle.asp">Printing
 * and print spooler structures</a>
 */
public class DevMode extends Structure
{
    /**
     * Specifies that dmCollate structure member will be initialized.
     */
    public static final long DM_COLLATE = 0x00008000L;
    /**
     * Specifies that dmCopies structure member will be initialized.
     */
    public static final long DM_COPIES = 0x00000100L;
    /**
     * Specifies that dmOrientation structure member will be initialized.
     */
    public static final long DM_ORIENTATION = 0x00000001L;
    /**
     * Do not collate when printing multiple copies.
     */
    public static final int COLLATE_NO = 0;

    private static final int CCHDEVICENAME = 32;
    private static final int CCHFORMNAME = 32;

    private Str _dmDeviceName = new Str(CCHDEVICENAME);

    private UInt16 _dmSpecVersion = new UInt16();
    private UInt16 _dmDriverVersion = new UInt16();
    private UInt16 _dmSize = new UInt16();
    private UInt16 _dmDriverExtra = new UInt16();
    private UInt32 _dmFields = new UInt32();

    private Union _paperUnion;
    private PaperStructure _paperStructure = new PaperStructure();
    private PointL _dmPosition = new PointL();

    private ShortInt _dmColor = new ShortInt();
    private ShortInt _dmDuplex = new ShortInt();
    private ShortInt _dmYResolution = new ShortInt();
    private ShortInt _dmTTOption = new ShortInt();
    private ShortInt _dmCollate = new ShortInt();

    private Str _dmFormName = new Str(CCHFORMNAME);
    private UInt16 _dmLogPixels = new UInt16();
    private UInt32 _dmBitsPerPel = new UInt32();
    private UInt32 _dmPelsWidth = new UInt32();
    private UInt32 _dmPelsHeight = new UInt32();
    private UInt32 _dmDisplayFlags = new UInt32();
    private UInt32 _dmDisplayFrequency = new UInt32();

    private UInt32 _dmICMMethod = new UInt32();
    private UInt32 _dmICMIntent = new UInt32();
    private UInt32 _dmMediaType = new UInt32();
    private UInt32 _dmDitherType = new UInt32();
    private UInt32 _dmReserved1 = new UInt32();
    private UInt32 _dmReserved2 = new UInt32();

    private UInt32 _dmPanningWidth = new UInt32();
    private UInt32 _dmPanningHeight = new UInt32();

    private void init()
    {
        _paperUnion = new Union(new Parameter[]{_paperStructure, _dmPosition, new UInt32(), new UInt32()});

        List parameterList = new ArrayList();

        parameterList.add(_dmDeviceName);
        parameterList.add(_dmSpecVersion);
        parameterList.add(_dmDriverVersion);
        parameterList.add(_dmSize);
        parameterList.add(_dmDriverExtra);
        parameterList.add(_dmFields);

        parameterList.add(_paperUnion);

        parameterList.add(_dmColor);
        parameterList.add(_dmDuplex);

        parameterList.add(_dmYResolution);
        parameterList.add(_dmTTOption);
        parameterList.add(_dmCollate);

        parameterList.add(_dmFormName);
        parameterList.add(_dmLogPixels);
        parameterList.add(_dmBitsPerPel);
        parameterList.add(_dmPelsWidth);
        parameterList.add(_dmPelsHeight);
        parameterList.add(_dmDisplayFlags);
        parameterList.add(_dmDisplayFrequency);

        VersionInfo versionInfo = new VersionInfo();

        if (versionInfo.getMajor() >= 0x04)
        {
            parameterList.add(_dmICMMethod);
            parameterList.add(_dmICMIntent);
            parameterList.add(_dmMediaType);
            parameterList.add(_dmDitherType);
            parameterList.add(_dmReserved1);
            parameterList.add(_dmReserved2);
        }

        if (versionInfo.getMajor() >= 0x05)
        {
            parameterList.add(_dmPanningWidth);
            parameterList.add(_dmPanningHeight);
        }

        Parameter[] parameters = new Parameter[parameterList.size()];
        parameterList.toArray(parameters);

        init(parameters);

        _dmSize.setValue(getLength());
        _paperUnion.setActiveMember(0);
        _dmFields.setValue(DM_COLLATE | DM_COPIES | DM_ORIENTATION);
        setDmCollate(COLLATE_NO);
        setDmCopies(1);
        setDmPageOrientation(2);
    }

    public DevMode()
    {
        init();
    }

    public DevMode(DevMode that)
    {
        this();
        initFrom(that);
    }

    public String getDmDeviceName()
    {
        return _dmDeviceName.getValue();
    }

    public void setDmDeviceName(String dmDeviceName)
    {
        _dmDeviceName.setValue(dmDeviceName);
    }

    public int getDmSpecVersion()
    {
        return (int)_dmSpecVersion.getValue();
    }

    public void setDmSpecVersion(int dmSpecVersion)
    {
        _dmSpecVersion.setValue(dmSpecVersion);
    }

    public int getDmDriverVersion()
    {
        return (int)_dmDriverVersion.getValue();
    }

    public void setDmDriverVersion(int dmDriverVersion)
    {
        _dmDriverVersion.setValue(dmDriverVersion);
    }

    public int getDmSize()
    {
        return (int)_dmSize.getValue();
    }

    public void setDmSize(int dmSize)
    {
        _dmSize.setValue(dmSize);
    }

    public int getDmDriverExtra()
    {
        return (int)_dmDriverExtra.getValue();
    }

    public void setDmDriverExtra(int dmDriverExtra)
    {
        _dmDriverExtra.setValue(dmDriverExtra);
    }

    public long getDmFields()
    {
        return _dmFields.getValue();
    }

    public void setDmFields(long dmFields)
    {
        _dmFields.setValue(dmFields);
    }

    public PaperStructure getPaperStructure()
    {
        return _paperStructure;
    }

    public PointL getDmPosition()
    {
        return _dmPosition;
    }

    public int getDmColor()
    {
        return (int)_dmColor.getValue();
    }

    public void setDmColor(int dmColor)
    {
        _dmColor.setValue(dmColor);
    }

    public int getDmDuplex()
    {
        return (int)_dmDuplex.getValue();
    }

    public void setDmDuplex(int dmDuplex)
    {
        _dmDuplex.setValue(dmDuplex);
    }

    public int getDmYResolution()
    {
        return (int)_dmYResolution.getValue();
    }

    public void setDmYResolution(int dmYResolution)
    {
        _dmYResolution.setValue(dmYResolution);
    }

    public int getDmTTOption()
    {
        return (int)_dmTTOption.getValue();
    }

    public void setDmTTOption(int dmTTOption)
    {
        _dmTTOption.setValue(dmTTOption);
    }

    public int getDmCollate()
    {
        return (int)_dmCollate.getValue();
    }

    public void setDmCollate(int dmCollate)
    {
        _dmCollate.setValue(dmCollate);
    }

    public String getDmFormName()
    {
        return _dmFormName.getValue();
    }

    public void setDmFormName(String dmFormName)
    {
        _dmFormName.setValue(dmFormName);
    }

    public int getDmLogPixels()
    {
        return (int)_dmLogPixels.getValue();
    }

    public void setDmLogPixels(int dmLogPixels)
    {
        _dmLogPixels.setValue(dmLogPixels);
    }

    public long getDmBitsPerPel()
    {
        return _dmBitsPerPel.getValue();
    }

    public void setDmBitsPerPel(long dmBitsPerPel)
    {
        _dmBitsPerPel.setValue(dmBitsPerPel);
    }

    public long getDmPelsWidth()
    {
        return _dmPelsWidth.getValue();
    }

    public void setDmPelsWidth(long dmPelsWidth)
    {
        _dmPelsWidth.setValue(dmPelsWidth);
    }

    public long getDmPelsHeight()
    {
        return _dmPelsHeight.getValue();
    }

    public void setDmPelsHeight(long dmPelsHeight)
    {
        _dmPelsHeight.setValue(dmPelsHeight);
    }

    public long getDmDisplayFlags()
    {
        return _dmDisplayFlags.getValue();
    }

    public void setDmDisplayFlags(long dmDisplayFlags)
    {
        _dmDisplayFlags.setValue(dmDisplayFlags);
    }

    public long getDmDisplayFrequency()
    {
        return _dmDisplayFrequency.getValue();
    }

    public void setDmDisplayFrequency(long dmDisplayFrequency)
    {
        _dmDisplayFrequency.setValue(dmDisplayFrequency);
    }

    public long getDmICMMethod()
    {
        return _dmICMMethod.getValue();
    }

    public void setDmICMMethod(long dmICMMethod)
    {
        _dmICMMethod.setValue(dmICMMethod);
    }

    public long getDmICMIntent()
    {
        return _dmICMIntent.getValue();
    }

    public void setDmICMIntent(long dmICMIntent)
    {
        _dmICMIntent.setValue(dmICMIntent);
    }

    public long getDmMediaType()
    {
        return _dmMediaType.getValue();
    }

    public void setDmMediaType(long dmMediaType)
    {
        _dmMediaType.setValue(dmMediaType);
    }

    public long getDmDitherType()
    {
        return _dmDitherType.getValue();
    }

    public void setDmDitherType(long dmDitherType)
    {
        _dmDitherType.setValue(dmDitherType);
    }

    public long getDmReserved1()
    {
        return _dmReserved1.getValue();
    }

    public void setDmReserved1(long dmReserved1)
    {
        _dmReserved1.setValue(dmReserved1);
    }

    public long getDmReserved2()
    {
        return _dmReserved2.getValue();
    }

    public void setDmReserved2(long dmReserved2)
    {
        _dmReserved2.setValue(dmReserved2);
    }

    public long getDmPanningWidth()
    {
        return _dmPanningWidth.getValue();
    }

    public void setDmPanningWidth(long dmPanningWidth)
    {
        _dmPanningWidth.setValue(dmPanningWidth);
    }

    public long getDmPanningHeight()
    {
        return _dmPanningHeight.getValue();
    }

    public void setDmPanningHeight(long dmPanningHeight)
    {
        _dmPanningHeight.setValue(dmPanningHeight);
    }

    public int getDmScale()
    {
        return _paperStructure.getDmScale();
    }

    public void setDmScale(int dmScale)
    {
        _paperStructure.setDmScale(dmScale);
    }

    public int getDmCopies()
    {
        return _paperStructure.getDmCopies();
    }

    public void setDmCopies(int dmCopies)
    {
        _paperStructure.setDmCopies(dmCopies);
    }

    public int getDmDefaultSource()
    {
        return _paperStructure.getDmDefaultSource();
    }

    public void setDmDefaultSource(int dmDefaultSource)
    {
        _paperStructure.setDmDefaultSource(dmDefaultSource);
    }

    public int getDmPrintQuality()
    {
        return _paperStructure.getDmPrintQuality();
    }

    public void setDmPrintQuality(int dmPrintQuality)
    {
        _paperStructure.setDmPrintQuality(dmPrintQuality);
    }

    public void setDmPageOrientation(int dmPageOrientation)
    {
        _paperStructure.setDmOrientation(dmPageOrientation);
    }

    public int getDmPageOrientation()
    {
        return _paperStructure.getDmOrientation();
    }

    public static class PaperStructure extends Structure
    {
        private ShortInt _dmOrientation = new ShortInt();
        private ShortInt _dmPaperSize = new ShortInt();
        private ShortInt _dmPaperLength = new ShortInt();
        private ShortInt _dmPaperWidth = new ShortInt();
        private ShortInt _dmScale = new ShortInt();
        private ShortInt _dmCopies = new ShortInt();
        private ShortInt _dmDefaultSource = new ShortInt();
        private ShortInt _dmPrintQuality = new ShortInt();

        public PaperStructure()
        {
            init(new Parameter[]
            {
                _dmOrientation,
                _dmPaperSize,
                _dmPaperLength,
                _dmPaperWidth,
                _dmScale,
                _dmCopies,
                _dmDefaultSource,
                _dmPrintQuality
            });
        }

        public PaperStructure(PaperStructure that)
        {
            this();
            initFrom(that);
        }

        public int getDmOrientation()
        {
            return (int)_dmOrientation.getValue();
        }

        public void setDmOrientation(int dmOrientation)
        {
            _dmOrientation.setValue(dmOrientation);
        }

        public int getDmPaperSize()
        {
            return (int)_dmPaperSize.getValue();
        }

        public void setDmPaperSize(int dmPaperSize)
        {
            _dmPaperSize.setValue(dmPaperSize);
        }

        public int getDmPaperLength()
        {
            return (int)_dmPaperLength.getValue();
        }

        public void setDmPaperLength(int dmPaperLength)
        {
            _dmPaperLength.setValue(dmPaperLength);
        }

        public int getDmPaperWidth()
        {
            return (int)_dmPaperWidth.getValue();
        }

        public void setDmPaperWidth(int dmPaperWidth)
        {
            _dmPaperWidth.setValue(dmPaperWidth);
        }

        public int getDmScale()
        {
            return (int) _dmScale.getValue();
        }

        public void setDmScale(int dmScale)
        {
            _dmScale.setValue(dmScale);
        }

        public int getDmCopies()
        {
            return (int) _dmCopies.getValue();
        }

        public void setDmCopies(int dmCopies)
        {
            _dmCopies.setValue(dmCopies);
        }

        public int getDmDefaultSource()
        {
            return (int) _dmDefaultSource.getValue();
        }

        public void setDmDefaultSource(int dmDefaultSource)
        {
            _dmDefaultSource.setValue(dmDefaultSource);
        }

        public int getDmPrintQuality()
        {
            return (int) _dmPrintQuality.getValue();
        }

        public void setDmPrintQuality(int dmPrintQuality)
        {
            _dmPrintQuality.setValue(dmPrintQuality);
        }

        public Object clone()
        {
            return new PaperStructure(this);
        }
    }

    public Object clone()
    {
        return new DevMode(this);
    }
}


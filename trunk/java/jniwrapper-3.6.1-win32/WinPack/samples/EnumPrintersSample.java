package com.jniwrapper.win32.samples;

import com.jniwrapper.*;

/**
 * This samples demonstrates how to get information about all installed printers.
 */
public class EnumPrintersSample {

/*
typedef struct _PRINTER_INFO_5 {
  LPTSTR    pPrinterName;
  LPTSTR    pPortName;
  DWORD     Attributes;
  DWORD     DeviceNotSelectedTimeout;
  DWORD     TransmissionRetryTimeout;
} PRINTER_INFO_5
*/
    public static class PRINTER_INFO_5 extends Structure {
        private Pointer pPrinterName = new Pointer(AnsiString.class);
        private Pointer pPortName = new Pointer(AnsiString.class);
        private UInt32 Attributes = new UInt32();
        private UInt32 DeviceNotSelectedTimeout = new UInt32();
        private UInt32 TransmissionRetryTimeout = new UInt32();

        public PRINTER_INFO_5() {
            init(new Parameter[] {pPrinterName,
                    pPortName,
                    Attributes,
                    DeviceNotSelectedTimeout,
                    TransmissionRetryTimeout
            });
        }
    }

    public static void main(String[] args) {
        Library winspool = new Library("winspool.drv");
        Function function = winspool.getFunction("EnumPrintersA");

        UInt32 Flags = new UInt32(6);
        Pointer lpName = new Pointer(AnsiString.class);
        UInt32 Level = new UInt32(5);
        Pointer pPrinterEnum = new Pointer(null, true);
        UInt32 cbBuf = new UInt32(0);
        UInt32 dwSize = new UInt32(0);

        Pointer pcbNeeded = new Pointer(dwSize);
        UInt32 dwPrinters = new UInt32(0);
        Pointer pcReturned = new Pointer(dwPrinters);

        Parameter[] parameter = new Parameter[]{Flags, lpName, Level, pPrinterEnum, cbBuf, pcbNeeded, pcReturned};
        UInt32 returnValue = new UInt32();
        function.invoke(returnValue, parameter);

        int requiredSize = (int) dwSize.getValue();
        PrimitiveArray val = new PrimitiveArray(UInt8.class, requiredSize);
        pPrinterEnum = new Pointer(val);
        cbBuf = new UInt32(requiredSize);
        parameter = new Parameter[]{Flags, lpName, Level, pPrinterEnum, cbBuf, pcbNeeded, pcReturned};
        function.invoke(returnValue, parameter);

        byte[] byteBuffer = val.getBytes();
        int numberOfPrinters = (int) dwPrinters.getValue();
        for (int i = 0; i < numberOfPrinters; i++) {
            PRINTER_INFO_5 printer_info = new PRINTER_INFO_5();
            printer_info.read(byteBuffer, i * printer_info.getLength());

            String printerName = printer_info.pPrinterName.getReferencedObject().toString();
            System.out.println("printerName = " + printerName);
        }
    }
}

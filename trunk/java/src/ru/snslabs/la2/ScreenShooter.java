package ru.snslabs.la2;

import com.jniwrapper.DefaultLibraryLoader;
import com.jniwrapper.Function;
import com.jniwrapper.Int32;
import com.jniwrapper.Library;
import com.jniwrapper.Parameter;
import com.jniwrapper.Pointer;
import com.jniwrapper.PrimitiveArray;
import com.jniwrapper.UInt32;
import com.jniwrapper.UInt8;

public class ScreenShooter {
    private static final int BUFFER_SIZE = 10;

    public static void main(String[] args) throws InterruptedException {
        DefaultLibraryLoader.getInstance().addPath("c:\\WINDOWS\\system32");
        DefaultLibraryLoader.getInstance().addPath("C:\\Serge\\PROJECT_ROOT\\delphi\\DLLs\\ScreenShooterDLL\\");
        Library screenShooterDll = new Library("ScreenShooterDLL");
        final Pointer.Void hWnd = new Pointer.Void();
        hWnd.setValue(0x00160B3E);
        Function shoot = screenShooterDll.getFunction("ScreenShot");
        final PrimitiveArray primitiveArray = new PrimitiveArray(UInt8.class, BUFFER_SIZE);
        Int32 result = new Int32();

        shoot.invoke(result,
                new Parameter[]{
                        hWnd, new UInt32(1024), new UInt32(1024), new Pointer(primitiveArray), new UInt32(BUFFER_SIZE)
                });

        System.out.println(primitiveArray.getBytes()[0] + ":" + primitiveArray.getBytes()[1] + ":" + primitiveArray.getBytes()[2]);
        System.out.println("Call result is " + result.getValue()+":"+primitiveArray.getBytes().length);
        int cnt = 0;

    }
    /*
    public static void main(String[] args) {
        DefaultLibraryLoader.getInstance().addPath("c:\\WINDOWS\\system32");
        Library _kernel = new Library("kernel32");
        Library _user = new Library("user32");
        Library _gdi = new Library("gdi32");
        Function postMessage = _user.getFunction("PostMessageA");
        Function getDC = _user.getFunction("GetDC");
        hWnd = new Pointer.Void(0x005C0694);

        final Pointer.Void hDC = new Pointer.Void();
        getDC.invoke(hDC, hWnd );
        System.out.println(""+hDC);
        
        Function createCompatibleBitmap = _gdi.getFunction("CreateCompatibleBitmap"); 
        Function deleteObject = _gdi.getFunction("DeleteObject"); 
        Function bitBlt = _gdi.getFunction("BitBlt"); 
        final Pointer.Void hBitmap = new Pointer.Void();
        System.out.println(""+hBitmap);
        createCompatibleBitmap.invoke(hBitmap, hDC, new UInt32(1024),new UInt32(768));
        System.out.println("bitmap created "+hBitmap);
        
        bitBlt.invoke(null, 
                new Parameter[]{
                        hBitmap, new Int(0),new Int(0),new UInt32(1024),new UInt32(768), 
                        hDC, new Int(0),new Int(0),new UInt32(0x00CC0020)
                });
        
        // clearing everything
        deleteObject.invoke(null, hBitmap);
        System.out.println("Bitmap deleted: "+hBitmap);
    }
    */
}

import com.jniwrapper.DefaultLibraryLoader;
import com.jniwrapper.Function;
import com.jniwrapper.Library;
import com.jniwrapper.Pointer;
import com.jniwrapper.UInt;
import com.jniwrapper.UInt32;

public class NativeTest {

    private static final UInt WM_KEY_DOWN = new UInt(0x100);
    private static final UInt WM_KEY_UP = new UInt(0x101);
    private static final UInt WM_CHAR = new UInt(0x102);
    private static Function postMessage;
    private static Function mapKey;
    private static Pointer.Void hWnd;
    /**
     * uCode is a virtual-key code and is translated into a scan code. 
     * If it is a virtual-key code that does not distinguish between 
     * left- and right-hand keys, the left-hand scan code is returned. 
     * If there is no translation, the function returns 0.
     */
    private static final UInt32 MAPVK_VK_TO_VSC = new UInt32(0);
    /**
     * uCode is a scan code and is translated into a virtual-key code that 
     * does not distinguish between left- and right-hand keys. If there is 
     * no translation, the function returns 0.
     */
    private static final UInt32 MAPVK_VSC_TO_VK = new UInt32(1);
    /**
     * uCode is a virtual-key code and is translated into an unshifted 
     * character value in the low-order word of the return value. 
     * Dead keys (diacritics) are indicated by setting the top bit of the 
     * return value. If there is no translation, the function returns 0.
     */
    private static final UInt32 MAPVK_VK_TO_CHAR = new UInt32(2);


    public static void main(String[] args) throws InterruptedException {
        DefaultLibraryLoader.getInstance().addPath("c:\\WINDOWS\\system32");
        Library _kernel = new Library("kernel32");
        Library _user = new Library("user32");
        postMessage = _user.getFunction("PostMessageA");
        mapKey = _user.getFunction("MapVirtualKeyA");

        hWnd = new Pointer.Void(0x004300d6);
        for (int i=1;i<100;i++){
            if(i%2 == 0){
//                type("/target Watchman of the Plains\r");
                type("/target Roughly Hewn Rock Golem\r");
            }
            else{
                type("/target Delu Lizardman Supplier\r");
            }
            Thread.sleep(2000);
            type("/attack\r");
            Thread.sleep(40000);
            type("/pickup\r/pickup\r/pickup\r/pickup\r");
        }

    }

    private static void type(String s) {
        for (char c : s.toCharArray()) {
            sendKey(c);
        }
    }

    private static void sendKey(char key) {
        System.out.print(key);
        long keyCode = key & 0xFF;
        long scanCode = 0;

//        System.out.println(key + "->(" + keyCode + ")");
        if(keyCode == 13){
//            System.out.println("WM_KEY_DOWN( " + Long.toHexString(charToVK(key))+","+ 1);
            postMessage.invoke(null, hWnd, WM_KEY_DOWN, new UInt32(charToVK(key)), new UInt32(1));
        }
//        sleep(50);
//        System.out.println("WM_CHAR( " + Long.toHexString(keyCode)+","+ 1);
        postMessage.invoke(null, hWnd, WM_CHAR, new UInt32(keyCode), new UInt32(1 | (keyCode == 13?0x001C0000:0)));
//        sleep(50);
        if(keyCode == 13){
//            System.out.println("WM_KEY_UP( " + Long.toHexString(charToVK(key))+","+ Integer.toHexString(1 | 0xC0000000));
            postMessage.invoke(null, hWnd, WM_KEY_UP, new UInt32(charToVK(key)), new UInt32(1 | 0xC01e0000));
        }
        sleep(50);
    }

    private static void sleep(int i) {
        try {
            Thread.sleep((long)i);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static long charToVK(char key){
        if(key <= 'z'  && key >='a'){
            return 0xFF & (key + "").toUpperCase().toCharArray()[0];
        }
        else{
            return 0xFF & key;
        }
    }
}

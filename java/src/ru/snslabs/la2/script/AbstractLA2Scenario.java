package ru.snslabs.la2.script;

import com.jniwrapper.DefaultLibraryLoader;
import com.jniwrapper.Function;
import com.jniwrapper.Library;
import com.jniwrapper.Pointer;
import com.jniwrapper.UInt;
import com.jniwrapper.UInt32;
import ru.snslabs.la2.engine.Checker;
import ru.snslabs.la2.Log;
import ru.snslabs.la2.model.Status;

/**
 * Support scenario with environment access - screen shots, window message posting, image processing etc.
 */
public abstract class AbstractLA2Scenario extends Log implements Scenario {
    private static final UInt WM_KEY_DOWN = new UInt(0x100);
    private static final UInt WM_KEY_UP = new UInt(0x101);
    private static final UInt WM_CHAR = new UInt(0x102);

    private int hWnd;
    private Checker checker;
    private Function postMessage;
    private Pointer.Void vpHWND;

    public void setWindowHandle(int hWnd) {
        this.hWnd = hWnd;
        checker = new Checker(hWnd);
        vpHWND = new Pointer.Void(hWnd);
        initJNIWrapper();
    }
    
    protected Status getStatus() throws Exception {
        return checker.checkStatus();
    }
    
    private void initJNIWrapper() {
        DefaultLibraryLoader.getInstance().addPath("c:\\WINDOWS\\system32");
        Library _user = new Library("user32");
        postMessage = _user.getFunction("PostMessageA");
    }

    protected void type(String s) {
        for (char c : s.toCharArray()) {
            sendKey(c);
        }
    }

    private void sendKey(char key) {
        System.out.print(key);
        long keyCode = key & 0xFF;

        if(keyCode == 13){
            postMessage.invoke(null, vpHWND, WM_KEY_DOWN, new UInt32(charToVK(key)), new UInt32(1));
        }
        postMessage.invoke(null, vpHWND, WM_CHAR, new UInt32(keyCode), new UInt32(1 | (keyCode == 13?0x001C0000:0)));
        if(keyCode == 13){
            postMessage.invoke(null, vpHWND, WM_KEY_UP, new UInt32(charToVK(key)), new UInt32(1 | 0xC01e0000));
        }
        sleep(50);
    }

    protected void sleep(int i) {
        try {
            Thread.sleep((long)i);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected long charToVK(char key){
        if(key <= 'z'  && key >='a'){
            return 0xFF & (key + "").toUpperCase().toCharArray()[0];
        }
        else{
            return 0xFF & key;
        }
    }
    
}

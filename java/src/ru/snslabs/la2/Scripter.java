package ru.snslabs.la2;

import com.jniwrapper.DefaultLibraryLoader;
import com.jniwrapper.Function;
import com.jniwrapper.Library;
import com.jniwrapper.Pointer;
import com.jniwrapper.UInt;
import com.jniwrapper.UInt32;

import java.io.BufferedReader;

public class Scripter {
    private static final UInt WM_KEY_DOWN = new UInt(0x100);
    private static final UInt WM_KEY_UP = new UInt(0x101);
    private static final UInt WM_CHAR = new UInt(0x102);
    private Function postMessage;
    private Pointer.Void hWnd;
    private BufferedReader bufferedReader;

    public static void main(String[] args) throws Exception {
        System.out.println("Usage:\n" +
                "Scripter {hWnd} {scriptFilePath}");
        final Scripter scripter = new Scripter();
//        scripter.init(Long.parseLong(args[0]), args[1]);
        scripter.init(0x03880134, null);
        scripter.execute();
    }

    private void execute() throws Exception {
        final Checker checker = new Checker((int) hWnd.getValue());
        for (int i=1;i<100;i++){
            try{
            Status st = checker.checkStatus();
            if(!"AmberBasilisk".equals(st.getTargetName()) && !"AmbemBasilisk".equals(st.getTargetName())){
                type("/target Amber Basilisk\r");
                Thread.sleep(1000);
                type("/target Amber Basilisk\r");
                Thread.sleep(1000);
                type("/useshortcut 1 8\r");
                Status status = checker.checkStatus();
                while(status.isTargetAlive()){
                    Thread.sleep(2000);
                    status = checker.checkStatus();
                    
                    if(status.getHp() < status.getMaxHp()*0.3){
                        type("/useshortcut 2 3\r");
                        Thread.sleep(1000);
                        type("/useshortcut 1 12\r");
                    }
                    if(status.getHp() < status.getMaxHp()*0.6){
                        type("/useshortcut 2 7\r");
                    }
                    if(status.getHp() < status.getMaxHp()*0.8){
                        type("/attack\r");
                    }
                }
                type("/useshortcut 1 9\r");
                type("/pickup\r/pickup\r/pickup\r/pickup\r");
                status = checker.checkStatus();
                if(status.getHp() < status.getMaxHp()*0.6){
                    type("/useshortcut 2 7\r");
                    Thread.sleep(15000);
                }
                Thread.sleep(5000);
            }
            }
            catch(Exception e){
                System.out.println("Cannot process step... :-(" + e.getMessage());
            }
            Thread.sleep(2000);
        }
    }

    private void printScreen() throws InterruptedException {
        System.out.println("Making screenshot");
        postMessage.invoke(null, hWnd, WM_KEY_UP, new UInt32(0x0000002C), new UInt32(0xC1370001));
        Thread.sleep(2000);
    }

    private void init(long hWnd, String scriptFile) {
        DefaultLibraryLoader.getInstance().addPath("c:\\WINDOWS\\system32");
        Library _kernel = new Library("kernel32");
        Library _user = new Library("user32");
        this.postMessage = _user.getFunction("PostMessageA");
        this.hWnd = new Pointer.Void(hWnd);
    }

    private void type(String s) {
        for (char c : s.toCharArray()) {
            sendKey(c);
        }
    }

    private void sendKey(char key) {
        System.out.print(key);
        long keyCode = key & 0xFF;

        if(keyCode == 13){
            postMessage.invoke(null, hWnd, WM_KEY_DOWN, new UInt32(charToVK(key)), new UInt32(1));
        }
        postMessage.invoke(null, hWnd, WM_CHAR, new UInt32(keyCode), new UInt32(1 | (keyCode == 13?0x001C0000:0)));
        if(keyCode == 13){
            postMessage.invoke(null, hWnd, WM_KEY_UP, new UInt32(charToVK(key)), new UInt32(1 | 0xC01e0000));
        }
        sleep(50);
    }

    private void sleep(int i) {
        try {
            Thread.sleep((long)i);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private long charToVK(char key){
        if(key <= 'z'  && key >='a'){
            return 0xFF & (key + "").toUpperCase().toCharArray()[0];
        }
        else{
            return 0xFF & key;
        }
    }

}

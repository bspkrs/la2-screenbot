import java.net.Socket;
import java.io.IOException;
import java.io.InputStream;

public class ClientTest implements Runnable {
    public static void main(String[] args) throws Exception {
        new Thread(new ClientTest()).start();
        Thread.sleep(1000);
        new Thread(new ClientTest()).start();
        Thread.sleep(1000);
        new Thread(new ClientTest()).start();
        Thread.sleep(1000);
        new Thread(new ClientTest()).start();
        Thread.sleep(1000);
    }

    private static int cntr = 0;
    private int id;

    public ClientTest() {
        id = cntr++;
        System.out.println("Created "+id);
    }

    public void run() {
        try {
            Socket s = new Socket("127.0.0.1", 6666);
            
            while(!Thread.currentThread().isInterrupted()){
                
                s.getOutputStream().write(("This is the test message from xxx" + id).getBytes());
                System.out.println("Sended by "+id);

                Thread.sleep(1000);
                receiveData(s);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void receiveData(Socket socket) {
        byte[] inBuffer = new byte[1024];
        StringBuffer sb = new StringBuffer();
        try {
            final InputStream is = socket.getInputStream();
            while (is.available() > 0) {
                final int size = is.read(inBuffer);
                sb.append(new String(inBuffer,0, size));
            }
            if(sb.length() > 0){
                System.out.println("Recieved: \n"+sb.toString());
            }
        }
        catch (IOException e) {
            System.out.println("Error while reading data from socket." + e.getMessage());
        }


    }
    
}

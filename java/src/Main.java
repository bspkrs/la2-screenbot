import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.ArrayList;

public class Main {
    List<ClientHandler> clientHandlers;
    public static void main(String[] args) throws Exception{
        new Main().run();
    }

    private void run() throws Exception {
        ServerSocket serverSocket  = new ServerSocket(6666);
        clientHandlers = new ArrayList<ClientHandler>();
        
        while(!Thread.interrupted()){
            Socket clientSocket = serverSocket.accept();
            System.out.println("Accepted client!");
            final ClientHandler target = new ClientHandler(clientSocket, clientHandlers);
            clientHandlers.add(target);
            new Thread(target).start();
            System.out.println("Total clients: " + clientHandlers.size());
        }
    }
}

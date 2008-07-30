import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.net.Socket;
import java.io.IOException;
import java.io.InputStream;

public class ClientHandler implements Runnable {
    private boolean dead = false;
    private byte[] inBuffer;
    private List<String> sendBuffer;

    private Socket socket;
    private List<ClientHandler> clientHandlers;

    private String clientId;

    private static int cntr = 0;
    private int id;

    public ClientHandler(Socket socket, List<ClientHandler> clientHandlers) {
        this.socket = socket;
        this.clientHandlers = clientHandlers;
        sendBuffer = new ArrayList<String>();
        inBuffer = new byte[1024];
        id = cntr++;
    }

    public void run() {
        while (!Thread.currentThread().isInterrupted() && !dead) {
            sendData();
            receiveData();
        }
    }

    private void receiveData() {
        StringBuffer sb = new StringBuffer();
        List<String> broadcastMsg = new ArrayList<String>();
        try {
            final InputStream is = socket.getInputStream();
            while (is.available() > 0) {
                int size = is.read(inBuffer);
                if (is.available() == 0) {
                    size = size - 1;
                }
                sb.append(new String(inBuffer, 0, size));
            }
            if (sb.length() > 0) {
                final String rcvdMsg = sb.toString();
                System.out.println("Recieved: \n" + rcvdMsg);

                if (rcvdMsg.indexOf("<policy-file-request/>") != -1) {
                    String xmlPolicy = "<?xml version=\"l.0\"?>\n" +
                            "<!DOCTYPE cross-domain-policy SYSTEM\n" +
                            "\"http://www.macromedia.com/xml/dtds/cross-domain-policy.dtd\">\n" +
                            "<cross-domain-policy>\n" +
                            "<allow-access-from domain=\"lineage.localhost\" to-ports=\"6666\"/>\n" +
                            "</cross-domain-policy>\u0000";
                    clientHandlers.remove(this);
                    this.sendMessages(xmlPolicy);
                }
                if(rcvdMsg.indexOf("/login ")!= -1){
                    final String newClientId = rcvdMsg.substring(7).trim();
                    broadcastMsg.add("/add " + newClientId+"\u0000"); 
                    broadcastMsg.add("[server] Welcome '" + newClientId+"'\u0000"); 
                    this.clientId = newClientId;
                    
                    // sending all users to this client
                    List<String> allUsers = new ArrayList<String>();
                    for (ClientHandler ch : clientHandlers) {
                        if(ch != this){
                            allUsers.add("/add " + ch.clientId+"\u0000");
                        }
                    }
                    this.sendMessages(allUsers);
                                        
                    
                }
                else if(rcvdMsg.indexOf("/nick ")!= -1){
                    final String newClientId = rcvdMsg.substring(6).trim();
                    broadcastMsg.add("/rename " + clientId + " " + newClientId+"\u0000"); 
                    broadcastMsg.add("[server] '" + clientId + "' is now known as  '" + newClientId+"'\u0000"); 
                    this.clientId = newClientId;
                }
                else {
                    broadcastMsg.add("["+clientId+"] " + rcvdMsg + "\u0000");
                }
                
                if(!broadcastMsg.isEmpty()){
                    // now sending to everyone
                    for (ClientHandler ch : clientHandlers) {
                        ch.sendMessages(broadcastMsg);
                    }
                }
            }
        }
        catch (IOException e) {
            System.out.println("Error while reading data from socket." + e.getMessage());
        }


    }

    private void sendData() {
        // send data
        synchronized (sendBuffer) {
            while (!sendBuffer.isEmpty()) {
                try {
                    socket.getOutputStream().write(sendBuffer.get(0).getBytes());
                    sendBuffer.remove(0);
                }
                catch (Exception e) {
                    System.out.println(id + " Cannot send " + sendBuffer.get(0));
                    System.out.println(id + " Removing client handler.");
                    clientHandlers.remove(this);
                    sendBuffer.clear();
                    dead = true;
                    for (ClientHandler ch : clientHandlers) {
                        ch.sendMessages("/delete " + this.clientId + "\u0000");
                        ch.sendMessages("[server] '" + this.clientId + "' left our room\u0000");
                    }
                    Thread.currentThread().interrupt();
                }
            }
        }
        try {
            Thread.sleep(500);
        }
        catch (InterruptedException e) {
            System.out.println("Thread interrupted...");
            Thread.currentThread().interrupt();
        }
    }

    public synchronized void sendMessages(List<String> data) {
        System.out.println(id + " about to send(List<String>): " + data);
        sendBuffer.addAll(data);
    }

    public synchronized void sendMessages(String data) {
        System.out.println(id + " about to send(String): " + data);
        sendBuffer.add(data);
    }

    public String getClientId() {
        return clientId;
    }

}

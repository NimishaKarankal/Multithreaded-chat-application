// SERVER CODE
import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final int PORT = 12345;
    // Using a HashSet to efficiently store and manage client connections
    private static Set<ClientHandler> clientHandlers = new HashSet<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is up and running on port: " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("✅New client connected: " + socket.getInetAddress());

                ClientHandler clientHandler = new ClientHandler(socket);
                clientHandlers.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            System.err.println("❌ Server error: " + e.getMessage());
        }
    }

    public static void broadcastMessage(String message, ClientHandler sender) {
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler != sender) {
            try{
                clientHandler.sendMessage(message);
            } catch(Exception e){
              System.out.println("❌ Unable to send message to " + clientHandler + ". Is the client still connected?");
              }
            }
        }
    }

    public static void removeClient(ClientHandler clientHandler) {
        clientHandlers.remove(clientHandler);
    }
    public static int getConnectedUsersCount() {
        return clientHandlers.size();
    }
}

class ClientHandler implements Runnable {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String clientName;
    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

             out.println("Welcome! Please enter your name:");
            clientName = in.readLine();
            System.out.println("👤 " + clientName + " has joined the chat.");
            ChatServer.broadcastMessage("👤 " + clientName + " has joined! Let's welcome them 🎉",this);
            // Log join event
            try (FileWriter logWriter = new FileWriter("server_logs.txt", true)) {
              logWriter.write(clientName + " joined at " + new Date() + "\n");
            } catch (IOException e) {
               System.err.println("❌ Error writing to log file.");
             }
          
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Received: " + message);
            
            // Handle the /list command
        if (message.startsWith("/list")) {
        out.println("Connected users: " + ChatServer.getConnectedUsersCount());
        continue; // Skip broadcasting this message
    }

             ChatServer.broadcastMessage(clientName+":"+message, this);
            }
        } catch (IOException e) {
           System.err.println("❌ Connection error with " + clientName);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("❌ Error closing client socket: " + e.getMessage());
            }
            ChatServer.removeClient(this);
          // Log leave event
try (FileWriter logWriter = new FileWriter("server_logs.txt", true)) {
    logWriter.write(clientName + " left at " + new Date() + "\n");
} catch (IOException e) {
    System.err.println("❌ Error writing to log file.");
}
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }
}

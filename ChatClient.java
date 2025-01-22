// CLIENT CODE
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        System.out.println("💬 Starting chat client...");

        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("✅Connected to the chat server" + SERVER_ADDRESS+":"+SERVER_PORT);

            // Start a thread to listen for messages from the server
           Thread serverListener = new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = in.readLine()) != null) {
                        System.out.println("📩 " +serverMessage);
                    }
                } catch (IOException e) {
                   System.err.println("❌ Connection to the server lost.");
                }
            });
           serverListener.setDaemon(true); // Ensure the thread closes when the main program exits
            serverListener.start();

            // Main thread to send messages to the server
            System.out.println("📝 You can now start typing your messages...");
            while (true) {
                String userMessage = scanner.nextLine();
            if (userMessage.equalsIgnoreCase("/exit")) {
                    System.out.println("👋 Exiting chat. Goodbye!");
                    break;
                }
                out.println(userMessage);
            }

        } catch (IOException e) {
            System.err.println("❌ Unable to connect to the server: " + e.getMessage());
        }
    }
}

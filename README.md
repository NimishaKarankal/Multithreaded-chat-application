# Multithreaded-chat-application

### Overview of the Code

The provided Java code implements a simple **multi-client chat application** that includes both the client-side and server-side components. This application facilitates real-time messaging among multiple users connected to the same server. It demonstrates core concepts of **networking, multithreading, and I/O operations** in Java.

The application includes:

1. **Chat Client (`ChatClient` class)**: A program that connects to the chat server, allowing users to send and receive messages.
2. **Chat Server (`ChatServer` class)**: A program that listens for client connections, facilitates communication between them, and maintains the state of the chat session.

---

### Features

1. **Multi-client Support**:  
   The server can handle multiple clients simultaneously using threads, allowing them to communicate with one another in real-time.

2. **Broadcast Messaging**:  
   Messages sent by a client are broadcast to all other connected clients.

3. **User Commands**:  
   - `/exit`: Allows a client to disconnect gracefully.
   - `/list`: Returns the number of connected users to the requesting client.

4. **User-Friendly Interface**:  
   - Messages from other users are displayed with a prefix indicating their name.  
   - Notifications for events like a new user joining or leaving the chat.

5. **Server Logs**:  
   - User join and leave events are logged in a `server_logs.txt` file with timestamps for future reference.

6. **Connection Resilience**:  
   - If a client disconnects abruptly, the server removes the client from the active connections list.  
   - Error handling ensures the server remains functional even if individual clients fail.

7. **Threaded Design**:  
   - Each client connection is handled in a separate thread, ensuring non-blocking operation on the server.

---

### How It Works

#### **Server Side**
1. The server listens for incoming client connections on a predefined port (`12345`).
2. When a client connects, the server:
   - Accepts the connection.
   - Creates a new `ClientHandler` thread to manage the client.
   - Broadcasts a welcome message to all other clients.
3. The `ClientHandler` manages:
   - Receiving messages from the client.
   - Sending messages to the client.
   - Handling commands (`/exit`, `/list`).
4. Disconnection of a client triggers cleanup operations, including:
   - Removing the client from the active clients list.
   - Logging the event in `server_logs.txt`.

#### **Client Side**
1. The client connects to the server using a socket and provides a username.
2. It starts two concurrent tasks:
   - Listening for messages from the server (handled in a separate thread).
   - Accepting user input and sending it to the server.
3. Commands like `/exit` and `/list` allow users to interact with the server beyond simple chatting.

---

### Code Highlights

1. **Socket Programming**:
   - Used to establish communication between the client and server (`Socket`, `ServerSocket`).

2. **Multithreading**:
   - `Thread` is used to manage individual client connections on the server and to enable simultaneous sending and receiving on the client.

3. **I/O Operations**:
   - `BufferedReader`, `PrintWriter`, and `FileWriter` handle message transmission and logging.

4. **Resource Management**:
   - The `try-with-resources` statement ensures proper cleanup of sockets and streams.

5. **Command Parsing**:
   - The `/list` and `/exit` commands illustrate simple text-based command handling.
  
   ---

  ### Conclusion

The chat application demonstrates the effective use of Java's networking and multithreading capabilities to create a real-time communication system. It facilitates seamless interaction between multiple clients while maintaining robust server operations and proper resource management. This project provides a strong foundation for understanding client-server architecture and serves as a starting point for developing more advanced communication systems.

---





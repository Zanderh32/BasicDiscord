Multi-Client Chat Application
This project is a Java-based multi-client chat system composed of a server (MultiServer) and a client (DiscordClient). The system allows multiple users to connect to the server, send messages, and view chat updates in real time via a graphical user interface (GUI).

Features:
Multi-Client Support: The server supports multiple clients connecting concurrently, each able to send and receive messages from all other connected users.
Client-Server Communication: The client connects to the server using sockets, allowing for message sending and receiving between the client and server.
Graphical User Interface (GUI): The client interface includes a chat display area, user list, and message input field, all built using Java’s Swing library.
Broadcasting Messages: The server broadcasts messages received from one client to all other connected clients.
User List Management: The server maintains and updates a list of currently connected users, which is dynamically updated in the client's GUI.
Multithreaded Architecture: Both the client and server utilize multiple threads to handle concurrent message transmission and reception without blocking other operations.
How It Works:
Server (MultiServer)
Server Setup: The server listens on a specified IP address and port. It accepts client connections via ServerSocket and assigns each client a unique thread to handle its communication.
Broadcasting Messages: When a client sends a message, the server broadcasts it to all connected clients, including system messages such as new connections or user disconnections.
User List Management: The server tracks connected users and regularly broadcasts an updated user list to all clients, allowing the clients to show who is currently online.
Console Interaction: The server operator can send messages to all clients directly from the server's console.
Client (DiscordClient)
Client Connection: Each client connects to the server by specifying the server’s IP address and port. Clients can then enter messages into a text field and send them to the server.
Graphical Interface: The client features a simple GUI with:
A main chat display showing messages from the server and other clients.
A user list that displays the current users connected to the server.
A text field for sending messages.
Multithreading:
The TalkThread sends messages from the client to the server.
The ListenThread listens for messages from the server and updates the chat display and user list.
Project Components:
MultiServer (Server Side)
The MultiServer is responsible for:

Accepting client connections.
Broadcasting messages to all connected clients.
Maintaining and updating the user list.
Handling message input from the server operator.
DiscordClient (Client Side)
The DiscordClient is responsible for:

Connecting to the server and exchanging messages.
Displaying messages from the server and other clients in a GUI.
Allowing the user to send messages via a text input field.
How to Run:
Server:
Compile the server with: javac MultiServer.java
Run the server with: java MultiServer
The server will start listening for connections on a specified port.
Client:
Compile the client with: javac DiscordClient.java
Run the client with: java DiscordClient
Enter the server’s IP address, port number, and your username to start chatting.

import java.io.*;
import java.net.*;

public class UserThread extends Thread//The UserThread class is responsible for reading messages sent from the client and broadcasting messages to all other clients. 
						//First, it sends a list of online users to the new user. 
						//Then it reads the user name and notifies other users about the new user.
{
	private Socket socket;
    private ChatServer server;
    private PrintWriter writer;//Prints formatted representations of objects to a text-output stream
 
    public UserThread(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
    }
 
    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));//	To read form the connected socket
 
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);// To take user input and send it to the connected server
 
            printUsers();
 
            String userName = reader.readLine();//Take as input the user name.
            server.addUserName(userName);
 
            String serverMessage = "New user connected: " + userName;
            server.broadcast(serverMessage, this);//broadcast this message to all the users.
 
 
	    String clientMessage;
            do {
                clientMessage = reader.readLine();
                serverMessage = "[" + userName + "]: " + clientMessage;
                server.broadcast(serverMessage, this);
 
            } while (!clientMessage.equals("bye"));		// IF someone said BYE!................
 
            server.removeUser(userName, this);
            socket.close();
            serverMessage = userName + " has quitted.";
            server.broadcast(serverMessage, this);
 
        } catch (IOException ex) {
            System.out.println("Error in UserThread: " + ex.getMessage());
            ex.printStackTrace();
        }
    }//..........................................................................................
 
    /**
     * Sends a list of online users to the newly connected user.
     */
    void printUsers() {
        if (server.hasUsers()) {//Prints the list of users
            writer.println("Connected users: " + server.getUserNames());
        } else {
            writer.println("No other users connected");
        }
    }
 
    /**
     * Sends a message to the client.
     */
    void sendMessage(String message) {
        writer.println(message);
    }

}

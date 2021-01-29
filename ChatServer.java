import java.io.*;
import java.net.*;
import java.util.*;
public class ChatServer 
{
	private int port; // port no of the server!
    private Set<String> userNames = new HashSet<>(); // list of all user-Names connected!
    private Set<UserThread> userThreads = new HashSet<>();// List of user Threads for each user client. 
 
    public ChatServer(int port) {
        this.port = port;
    }
 // now lets connect to a new Client 
    public void execute() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
 
            System.out.println("Chat Server is listening on port " + port);
 
            while (true) { // to accept Multiple clients ................
                Socket socket = serverSocket.accept();	//create a socket in Server for that client socket and connect both!
                System.out.println("New user connected");
                UserThread newUser = new UserThread(socket, this); // define a User Thread for that new added client. 
                //compiler may stuck here!
                userThreads.add(newUser);// will add this User Thread into the list.
                newUser.start();// set this created thread to run form here now!
                
            }
 
        } catch (IOException ex) {
            System.out.println("Error in the server: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
 //the main function of our server
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Syntax: java ChatServer <port-number>");
            System.exit(0);
        }
 
        int port = Integer.parseInt(args[0]);//port is specified to setup the connection.
 
        ChatServer server = new ChatServer(port);
        server.execute();//it will enter into execute() method! into an infinite while loop. 
    }
 ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Delivers a message from one user to others (broadcasting)
     */
    void broadcast(String message, UserThread excludeUser) {
        for (UserThread aUser : userThreads) {
            if (aUser != excludeUser) {
                aUser.sendMessage(message);
            }
        }
    }
 
    /**
     * Stores user name of the newly connected client.
     */
    void addUserName(String userName) {
        userNames.add(userName);
    }
 
    /**
     * When a client is Disconnected, removes the associated User name and UserThread
     */
    void removeUser(String userName, UserThread aUser) {
        boolean removed = userNames.remove(userName);
        if (removed) {
            userThreads.remove(aUser);
            System.out.println("The user " + userName + " quitted");
        }
    }
 
    Set<String> getUserNames() {//used in user thread
        return this.userNames;
    }
 
    /**
     * Returns true if there are other users connected (not count the currently connected user)
     */
    boolean hasUsers() {// used in user thread
        return !this.userNames.isEmpty();
    }

}

import java.io.*;
import java.net.*;
public class WriteThread extends Thread//WriteThread is responsible for reading input from the user and sending it to the server, 
									   //continuously until the user types ‘bye’ to end the chat.
{
	private PrintWriter writer;
    private Socket socket;
    private ChatClient client;

    public WriteThread(Socket socket, ChatClient client)
    {
        this.socket = socket;
        this.client = client;
        try {
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException ex) {
            System.out.println("Error getting output stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void run() {
        Console console = System.console();
 
        String userName = console.readLine("\nEnter your name: ");
        client.setUserName(userName);
        writer.println(userName);
 
        String text;
        do {
            text = console.readLine(); //will read message from system
            writer.println(text);//will send to other connected socket 
 
        } while (!text.equals("bye"));
 
        try {
            socket.close();
        } catch (IOException ex) {
 
            System.out.println("Error writing to server: " + ex.getMessage());
        }
    }

}

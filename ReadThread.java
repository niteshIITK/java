import java.io.*;
import java.net.*;

public class ReadThread extends Thread //The ReadThread is responsible for reading input from the server and printing it to the console repeatedly, until the client disconnects.
{
	private BufferedReader in;//in will read input coming from connected socket.
    private Socket socket;
    private ChatClient client;
 
    public ReadThread(Socket socket1, ChatClient client) {
        this.socket = socket1;
        this.client = client;
 
        try {
            InputStream input = socket.getInputStream();
            in = new BufferedReader(new InputStreamReader(input)); //this 'in' will read form the connected socket (that is server socket here)
        } catch (IOException ex) {
            System.out.println("Error getting input stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    String response = "";
    public void run() {
        while (true) { 	//continuously open to read messages coming form server side.
            try {
                response = in.readLine();
                System.out.println("\n" + response + "\n" );
                }
             catch (IOException ex) {
                System.out.println("Error reading from server: " + ex.getMessage());
                ex.printStackTrace();
                break;
            }
        }
    }

}

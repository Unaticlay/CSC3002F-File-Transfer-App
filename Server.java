import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class Server {

    private static int port = 10000;

    static ArrayList<ClientHandler> handlers = new ArrayList<>();

    public static void main(String[] args) {
        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            while (true) {
                // Accept the client here and create a new thread to handle the client
                System.out.println("Listening requests from client......");
                Socket client = server.accept();
                System.out.println("Request received from client, establishing connection......");

                /*
                 * DataInputStream input = new DataInputStream(client.getInputStream());
                 * DataOutputStream output = new DataOutputStream(client.getOutputStream());
                 */

                BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter output = new PrintWriter(client.getOutputStream());
                ClientHandler ch = new ClientHandler(client, input, output);

                handlers.add(ch);
                ch.start();

                System.out.println("Connection established.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
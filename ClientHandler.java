import java.net.*;
import java.io.*;

public class ClientHandler extends Thread {

    private boolean check;
    private Socket client;
    /*
     * private final DataInputStream input; private final DataOutputStream output;
     */

    // BufferedReader and PrintWriter creations

    private final BufferedReader input;
    private final PrintWriter output;

    // New constructor utilising BufferedReader and PrintWriter

    public ClientHandler(Socket client, BufferedReader input, PrintWriter output) {
        this.client = client;
        this.input = input;
        this.output = output;
    }

    // Old constructor utilising DataInputStream and DataOutputStream
    /*
     * public ClientHandler(Socket client, DataInputStream input, DataOutputStream
     * output) { this.client = client; this.input = input; this.output = output; }
     */

    public void run() {
        check = true;
        while (check) {
            // Here is where the sequences gonna happen
            String request = input.readLine();

            if (request.startsWith("EXIT") || request.startsWith("exit")) {
                handleExit();
                check = false;
            } else if (request.startsWith("GET") || request.startsWith("get")) {
                receiveFile();
                check = false;
            } else if (request.startsWith("SEND") || request.startsWith("send")) {
                sendFile();
                check = false;
            } else if (request.startsWith("LIST") || request.startsWith("list")) {
                sendList();
                check = false;
            } else {
                handleBadRequest();
            }
        }
        try {
            input.close();
            output.close();
            client.close();
            Server.handlers.remove(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void receiveFile() {
        ;
    }

    void sendList() {
        ;
    }

    void sendFile() {
        ;
    }

    void handleExit() {
        System.exit(0);
    }

    void handleBadRequest() {
        System.out.print("Please enter a valid request (exit, get, send, list)");
    }
}

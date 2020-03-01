import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client {

    private static int port = 1000;

    static Socket client;

    public static void main(String[] args) {
        try {
            // Here need to build the connection and GUI, also where everything is happening
            InetAddress host = InetAddress.getLocalHost();
            client = new Socket(host.getHostAddress(), port);

            // BufferReader and PrintWriter for input and output streams

            BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter output = new PrintWriter(client.getOutputStream());

            /*
             * DataInputStream input = new DataInputStream(client.getInputStream());
             * DataOutputStream output = new DataOutputStream(client.getOutputStream());
             */

            Thread receiver = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            ; // Communication between receiver and the server
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            Thread sender = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            ;// Communication between sender from local and the server
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            receiver.start();
            sender.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void upload() {
        ;
    }

    static void download() {
        ;
    }

    static void getList() {
        ;
    }
}

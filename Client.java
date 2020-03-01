import java.net.*;
import java.io.*;

public class Client {

    private static int port = 1000;

    static Socket client;

    private static boolean check = true;

    public static void main(String[] args) {
        try {
            // Here need to build the connection and GUI, also where everything is happening
            InetAddress host = InetAddress.getLocalHost();
            client = new Socket(host.getHostAddress(), port);

            // BufferReader and PrintWriter for input and output streams

            BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter output = new PrintWriter(client.getOutputStream());

            System.out.println("Please specify what you would like to do");
            System.out.println(" 'Upload', 'Download', or 'GetList'");
            check = true;

            while (check) {

                String request = input.readLine();
                String fileName = null;

                if (request.toLowerCase().equals("upload")) {
                    System.out.println("Please enter the name of the file you want to upload");
                    fileName = input.readLine();
                    upload(fileName);
                    check = false;
                } else if (request.toLowerCase().equals("download")) {
                    System.out.println("Please enter the name of the file you want to download");
                    fileName = input.readLine();
                    download(fileName);
                    check = false;
                } else if (request.toLowerCase().equals("getlist")) {
                    getList();
                    check = false;
                } else {
                    handleBadRequest();
                }
            }

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

    // File name is being passed in here
    // Needs to still ask for access setting
    static void upload(String fileName) {
        ;
    }

    // File name is being passed in here
    static void download(String fileName) {
        ;
    }

    static void getList() {
        ;
    }

    static void handleBadRequest() {
        System.out.println("Please enter a valid request \n Either 'Upload', 'Download', or 'GetList'");
    }
}

import java.net.*;
import java.io.*;

public class Client {

    private static int port = 1000;

    static Socket client;

    private static boolean check = true;
    private static boolean accessProtocolCheck = true;

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
                String accessProtocol = null;

                if (request.toLowerCase().equals("upload")) {
                    System.out.println("Please enter the name of the file you want to upload:");
                    fileName = input.readLine();
                    System.out.println(
                            "Please specify the privacy setting for the file you want to upload ('public' or 'private'):");
                    while (accessProtocolCheck) {
                        accessProtocol = input.readLine();
                        if (accessProtocol.toLowerCase().equals("public")
                                || accessProtocol.toLowerCase().equals("private")) {
                            accessProtocolCheck = false; // Access protocol was set succesfully to either public or
                                                         // private, break out of while loop
                        } else {
                            System.out.println(
                                    "Please enter a valid privacy setting for your file. Either 'private' or 'public'");
                        }
                    }
                    upload(fileName, accessProtocol);
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

    // File name and access protocol (public or private) is being passed in here
    static void upload(String fileName, String accessProtocol) {
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

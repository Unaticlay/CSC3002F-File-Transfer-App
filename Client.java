import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client {

    private static int port = 10000;

    static Socket client;

    private static boolean checkValid = false;
    private static boolean accessCheck = false;

    public static void main(String[] args) {
        try {
            // Here need to build the connection and GUI, also where everything is happening
            InetAddress host = InetAddress.getLocalHost();
            client = new Socket(host.getHostAddress(), port);

            Scanner inputSC = new Scanner(System.in);

            // BufferReader and PrintWriter for input and output streams
            BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter output = new PrintWriter(client.getOutputStream());

            System.out.println("Please specify what you would like to do:");
            System.out.println("'Upload', 'Download', or 'GetList'");
            checkValid = false;

            // Checks if the request is a valid request
            while (!checkValid) {

                System.out.println("Got here. 1 Yay");

                String request = inputSC.nextLine().toLowerCase();
                System.out.println("This is the request: " + request);
                String fileName = null;
                String access = null;
                boolean fileExists = false;

                // Handles the uploading of a file and catches FileNotFoundException
                if (request.equals("upload")) {
                    System.out.println("Please enter the name of the file you want to upload:");
                    while (!fileExists) {
                        fileName = inputSC.nextLine();
                        try {
                            readFile(fileName);
                            fileExists = true;
                        } catch (FileNotFoundException e) {
                            System.out.println(
                                    "This file does not exist. Please check that your file path is correct and \n re-enter the name of the file you want to upload:");

                        } catch (Exception e) {
                            System.out.println("There was an unexpected error. Please try again.");
                        }
                    }
                    // Sets the access protocol of a file to either private or public
                    System.out.println(
                            "Please specify the privacy setting for the file you want to upload ('public' or 'private'):");
                    while (!accessCheck) {
                        access = inputSC.nextLine().toLowerCase();
                        if (access.equals("public") || access.equals("private")) {
                            accessCheck = true; // Access was set succesfully to either public or
                                                // private, break out of while loop
                        } else {
                            System.out.println(
                                    "Please enter a valid privacy setting for your file. Either 'private' or 'public':");
                        }
                    }
                    upload(fileName, access);
                    checkValid = true;
                    // Requests to download a file
                } else if (request.equals("download")) {
                    System.out.println("Please enter the name of the file you want to download:");
                    fileName = inputSC.nextLine();
                    download(fileName);
                    checkValid = true;
                    // Request to retrieve a list of files
                } else if (request.equals("getlist")) {
                    getList();
                    checkValid = true;
                    // Request to exit the program
                } else if (request.equals("exit")) {
                    handleExit();
                    checkValid = true;
                } else {
                    handleBadRequest();
                }
            }

            System.out.println("Got here 2");

            /*
             * DataInputStream input = new DataInputStream(client.getInputStream());
             * DataOutputStream output = new DataOutputStream(client.getOutputStream());
             */

            /*
             * Thread receiver = new Thread(new Runnable() {
             * 
             * @Override public void run() { while (true) { try { ; // Communication between
             * receiver and the server } catch (Exception e) { e.printStackTrace(); } } }
             * });
             * 
             * Thread sender = new Thread(new Runnable() {
             * 
             * @Override public void run() { while (true) { try { ;// Communication between
             * sender from local and the server } catch (Exception e) { e.printStackTrace();
             * } } } });
             * 
             * receiver.start(); sender.start();
             */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // File name and access protocol (public or private) is being passed in here
    static void upload(String fileName, String access) {
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
        System.out.println("Please enter a valid request \n Either 'Upload', 'Download', or 'GetList':");
    }

    static void handleExit() {
        ;
    }

    static void readFile(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
    }
}

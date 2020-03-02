import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client
{

    private static int port = 10000;

    static Socket client;
    static String dest = "C:\\Users\\Laaiqah\\Desktop\\CSC3 NETWORKS\\Client\\";


    private static boolean checkValid = false;
    private static boolean accessCheck = false;

    public static void main(String[] args)
    {
        try
        {
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
            while (!checkValid)
            {

                String request = inputSC.nextLine().toLowerCase();
                String fileName = null;
                String access = null;
                boolean fileExists = false;

                // Handles the uploading of a file and catches FileNotFoundException
                if (request.equals("upload"))
                {
                    System.out.println("Please enter the name of the file you want to upload:");
                    while (!fileExists)
                    {
                        fileName = inputSC.nextLine();
                        try
                        {
                            readFile(fileName);
                            fileExists = true;
                        }
                        catch (FileNotFoundException e)
                        {
                            System.out.println("This file does not exist. Please check that your file path is correct and \n re-enter the name of the file you want to upload:");
                        }
                        catch (Exception e)
                        {
                            System.out.println("There was an unexpected error. Please try again.");
                        }
                    }
                    // Sets the access protocol of a file to either private or public
                    System.out.println("Please specify the privacy setting for the file you want to upload ('public' or 'private'):");

                    while (!accessCheck)
                    {
                        access = inputSC.nextLine().toLowerCase();
                        if (access.equals("public") || access.equals("private"))
                            accessCheck = true; // Access was set succesfully to either public or private, break out of while loop
                        else
                            System.out.println("Please enter a valid privacy setting for your file. Either 'private' or 'public':");
                    }

                    upload(fileName, access);
                    checkValid = true;
                    // Requests to download a file
                }
                else if (request.equals("download"))
                {
                    System.out.println("Please enter the name of the file you want to download:");
                    fileName = inputSC.nextLine();
                    download(fileName);
                    checkValid = true;
                    // Request to retrieve a list of files
                }
                else if (request.equals("getlist"))
                {
                    getList();
                    checkValid = true;
                    // Request to exit the program
                }
                else if (request.equals("exit"))
                {
                    handleExit();
                    checkValid = true;
                }
                else
                    handleBadRequest();
            }

            inputSC.close();

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
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // File name and access protocol (public or private) is being passed in here
    static void upload(String fileName, String access)
    {
         File file = new File(fileName);
         byte[] bytes = new byte[16384];          
         try{
               FileInputStream filein = new FileInputStream(file);
               DataInputStream input = new DataInputStream(filein);          
               try{  
                     input.readFully(bytes, 0, bytes.length);
                     OutputStream clientStream = client.getOutputStream();
                     DataOutputStream output = new DataOutputStream(clientStream);
                           
                /*           byte[] buffer = new byte[BUFFER_SIZE];
                           int read;
                           int totalRead = 0;
                           long size = input.readLong();
                           System.out.println("Reading to server.");
            
                                while ((read = input.read(buffer)) != -1) {
                                    totalRead += read;
                                    output.write(buffer, 0, read);
                                }
                                              
                            System.out.println("File successfully sent to server.");
                            output.flush();
                            System.out.println("Transfer Complete");
                            clientStream.close();
                          
                            client.close();
            */
                     output.writeUTF(file.getName());
                     output.writeLong(bytes.length);
                     output.write(bytes, 0, bytes.length);
                     
                     System.out.println("File successfully uploaded onto server.");
                     output.flush();
                     System.out.println("Transfer Complete");
            
                     clientStream.close();
                     client.close();
                 }catch(IOException e) {
                      e.printStackTrace();
                  }
            }catch (FileNotFoundException ex)  
    {
            System.out.println(ex);
    }
    }

    // File name is being passed in here
    static void download(String fileName)
    {
        int bytesRead;
        try{
           DataInputStream in = new DataInputStream(client.getInputStream());   
           String file = fileName;     
           OutputStream output = new FileOutputStream(dest+file);     
           long size = in.readLong();     
           byte[] buffer = new byte[16384]; 
           System.out.println("Downloading");
          
           while (size > 0 && (bytesRead = in.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1)     
          //  while ((bytesRead = in.read(buffer, 0, (int)Math.min(buffer.length, size))) >0)     
           {     
               output.write(buffer, 0, bytesRead);     
               size -= bytesRead;     
           }  
           
           System.out.println("Download successful.");
         } catch (IOException ie) { 
                       ie.printStackTrace(); 
                     }
    }

    static void getList()
    {
        ;
    }

    static void handleBadRequest()
    {
        System.out.println("Please enter a valid request \n Either 'Upload', 'Download', or 'GetList':");
    }

    static void handleExit()
    {
        System.exit(0);
    }

    static void readFile(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
    }
}

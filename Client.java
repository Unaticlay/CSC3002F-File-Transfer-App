import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client {

    private static int port = 10000;

    static Socket client;
    static String dest = "C:\\Users\\Laaiqah\\Desktop\\CSC3 NETWORKS\\Client";


    public static void main(String[] args)
    {
        try
        {
            // Here need to build the connection and GUI, also where everything is happening
            InetAddress host = InetAddress.getLocalHost();
            client = new Socket(host.getHostAddress(), port);

            DataInputStream input = new DataInputStream(client.getInputStream());
            DataOutputStream output = new DataOutputStream(client.getOutputStream());

            Thread receiver = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true)
                    {
                        try
                        {
                            ; // Communication between receiver and the server
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            });

            Thread sender = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true)
                    {
                        try
                        {
                            ;// Communication between sender from local and the server
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            });

            receiver.start();
            sender.start();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

 static void upload(String fileName){
         File file = new File(fileName);
         byte[] bytes = new byte[(int) file.length()];
           
         try{
            FileInputStream filein = new FileInputStream(file);
            DataInputStream input = new DataInputStream(filein);          
            try{
               input.readFully(bytes, 0, bytes.length);
               OutputStream clientStream = client.getOutputStream();
               DataOutputStream output = new DataOutputStream(clientStream);
               
               //    send file name and size
               output.writeUTF(file.getName());
               output.writeLong(bytes.length);
               output.write(bytes, 0, bytes.length);
               output.flush();
               
               // Send file data
               clientStream.write(bytes, 0, bytes.length);
               clientStream.flush();
                 
               System.out.println("File successfully uploaded onto server.");
               output.flush();
                
               //Closing socket
               clientStream.close();
               client.close();
               System.out.println("Transfer Complete");
             } catch(IOException e) {
                e.printStackTrace();
               }
          } catch (FileNotFoundException e) {
          
              System.out.println(e);
          }
    }


       static void download(String fileName)
    {
      try{
         DataInputStream in = new DataInputStream(client.getInputStream());
         byte[] bytes = new byte[16384];
   
         String file  = fileName;
         FileOutputStream fileOut = new FileOutputStream(dest+file);
         DataOutputStream output = new DataOutputStream(fileOut);
                   
         int bytesRead = in.read(bytes, 0, bytes.length);
         output.write(bytes, 0, bytesRead); 
         System.out.println("File successfully recieved.");
   
         output.close();
         //client.close();
        } catch(IOException e) {
          e.printStackTrace();
        };
    }    static void getList()
    {
        ;
    }
}

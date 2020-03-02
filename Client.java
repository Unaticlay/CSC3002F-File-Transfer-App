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
               
               output.writeUTF(file.getName());
               output.writeLong(bytes.length);
               output.write(bytes, 0, bytes.length);
     
               System.out.println("File successfully uploaded onto server.");
               output.flush();
               clientStream.close();
               System.out.println("Transfer Complete");
               client.close();
               
             } catch(IOException e) {
                e.printStackTrace();
               }
          } catch (FileNotFoundException e) {
              System.out.println(e);
          }
    }


       static void download(String fileName){
      try{
         DataInputStream in = new DataInputStream(client.getInputStream());         
          int bytesRead;
          String file  = fileName;
          OutputStream output = new FileOutputStream(dest+file);     
          long size = in.readLong();     
          byte[] buffer = new byte[(int)file.length()];     
          // while (size > 0 && (bytesRead = in.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1)     
          while ((bytesRead = in.read(buffer, 0, (int)Math.min(buffer.length, size))) >0){     
               output.write(buffer, 0, bytesRead);     
               size -= bytesRead;     
           }  
         System.out.println("Download successful.");                         
         output.close();         
        } catch(IOException e) {
            e.printStackTrace();
        };    
    }    
    
    static void getList()
    {
        ;
    }
}

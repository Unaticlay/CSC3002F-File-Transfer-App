import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class ClientHandler extends Thread{

        private Socket client;
        private final DataInputStream input;
        private final DataOutputStream output;
        String dest = "C:\\Users\\Laaiqah\\Desktop\\CSC3 NETWORKS\\+Server\\";


        private static String filePath = "list.txt";

        private ArrayList<ClientHandler.FileInfo> fileList = new ArrayList<>();

        class FileInfo
        {
            String fileName;
            String key;
            String access;

            FileInfo(String fileName, String access)
            {
                this.fileName = fileName;
                this.access = access;
                key = "";
            }

            FileInfo(String fileName, String access, String key)
            {
                this.fileName = fileName;
                this.access = access;
                this.key = key;
            }

            public String toString()
            {
                return fileName + " " +  access + " " + key;
            }

            public String getFileName()
            {
                return fileName;
            }
        }






        public ClientHandler(Socket client,DataInputStream input, DataOutputStream output)
        {
            this.client = client;
            this.input = input;
            this.output = output;
        }

        public void run()
        {
            while (true)
            {
                // Here is where the sequences gonna happen
                break;
            }
            try
            {
                input.close();
                output.close();
                client.close();
                Server.handlers.remove(this);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

         void receiveFile()
        {
             int bytesRead;   
             
               try{
                   DataInputStream in = new DataInputStream(client.getInputStream());
                   String file = in.readUTF();
                   byte[] bytes = new byte[16384];
                  
                   System.out.println("Writing file to server......");
                  
                   FileOutputStream fileOut = new FileOutputStream(dest+file);
                   DataOutputStream output = new DataOutputStream(fileOut);                  
                   bytesRead = in.read(bytes, 0, bytes.length);
                   output.write(bytes, 0, bytesRead); 
                   
                   System.out.println("File succesfully recieved.");
                   output.close();
                   
                 } catch(IOException e) {
                     e.printStackTrace();
                 }
        }


        void sendList()
        {
            ;
        }

         void sendFile(String fileName)
        {
          File file = new File(fileName);
          byte[] bytes = new byte[16384];
           try {
             DataInputStream in = new DataInputStream(new FileInputStream(file));
             try{
                in.read(bytes, 0, bytes.length);                
                output.write(bytes, 0, bytes.length);
                System.out.println("File successfully sent.");
                output.flush();
              } catch(IOException e) {
                e.printStackTrace();
              }
          } catch (FileNotFoundException e) {
              System.out.println(e);
          }
                
            ;
        }


        void loadFileList() throws IOException
        {
            File fileList = new File(filePath);
            BufferedReader input = new BufferedReader(new FileReader(fileList));
            String info;
            while ((info = input.readLine()) != null)
            {
                StringTokenizer token = new StringTokenizer(info);
                String name = token.nextToken();
                String access = token.nextToken();
                FileInfo file;
                if (access.compareTo("private") == 0)
                {
                    file = new FileInfo(name, access, token.nextToken());
                }
                else
                {
                    file = new FileInfo(name, access);
                }

                this.fileList.add(file);
            }

        }

        String getFileList()
        {
            String temp = "";

            for (int i = 0; i < fileList.size(); i ++)
            {
                temp += (fileList.get(i).getFileName() + "\n");
            }

            return temp;
        }

        boolean fileExist(String fileName)
        {
            for (int i = 0; i < fileList.size(); i ++)
            {
                if (fileList.get(i).getFileName().compareTo(fileName) == 0)
                {
                    return true;
                }
            }

            return false;
        }

        void addFile(String fileName, String access) throws IOException
        {
            FileInfo file = new FileInfo(fileName, access);
            fileList.add(file);
            saveList(file);
        }

        void addFile(String fileName, String access, String key) throws IOException
        {
            FileInfo file = new FileInfo(fileName, access, key);
            fileList.add(file);
            saveList(file);
        }

        void saveList(FileInfo file) throws IOException
        {
            PrintWriter writer = new PrintWriter(new FileWriter(filePath), true);
            writer.write(file.toString());

            writer.close();
        }
}

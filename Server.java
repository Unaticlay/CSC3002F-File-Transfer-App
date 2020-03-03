import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Server
{

    private static int port = 10000;

    static ArrayList<ClientHandler> handlers = new ArrayList<>();

    static String filePath = "list.txt";

    static ArrayList<FileInfo> fileList = new ArrayList<>();

    public static void main(String[] args)
    {
        ServerSocket server = null;
        try
        {
            loadFileList();

            server = new ServerSocket(port);
            while (true)
            {
                // Accept the client here and create a new thread to handle the client
                System.out.println("Listening requests from client......");
                Socket client = server.accept();
                System.out.println("Request received from client, establishing connection......");


                DataInputStream input = new DataInputStream(client.getInputStream());
                DataOutputStream output = new DataOutputStream(client.getOutputStream());

                ClientHandler ch = new ClientHandler(client, input, output);

                handlers.add(ch);
                ch.start();

                System.out.println("Connection established.");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    static void loadFileList()
    {
        try
        {
            File fileList = new File(filePath);
            BufferedReader input = new BufferedReader(new FileReader(fileList));
            String info;
            while ((info = input.readLine()) != null) {
                StringTokenizer token = new StringTokenizer(info);
                String name = token.nextToken();
                String access = token.nextToken();
                FileInfo file;
                if (access.compareTo("PRIVATE") == 0) {
                    file = new FileInfo(name, access, token.nextToken());
                } else
                    file = new FileInfo(name, access);

                Server.fileList.add(file);
            }

            input.close();
        }
        catch (Exception e)
        {
            System.out.println("File cannot be opened.");
        }

    }

    static String getFileList()
    {
        String temp = "";

        for (int i = 0; i < fileList.size(); i++)
            temp += (fileList.get(i).getFileName() + "\n");

        return temp;
    }

    static boolean fileExist(String fileName)
    {
        for (int i = 0; i < fileList.size(); i++)
        {
            if (fileList.get(i).getFileName().compareTo(fileName) == 0)
                return true;
        }
        return false;
    }

    static void addFile(String fileName, String access, String key) throws IOException
    {
        FileInfo file;

        if (key.compareTo("") == 0)
            file = new FileInfo(fileName, access);
        else
            file = new FileInfo(fileName, access, key);

        fileList.add(file);
        saveList(file);
    }

    static void saveList(FileInfo file) throws IOException
    {
        PrintWriter writer = new PrintWriter(new FileWriter(Server.filePath), true);
        writer.write(file.toString());

        writer.close();
    }

    static boolean isPublic(String fileName)
    {
        for (int i = 0; i < fileList.size(); i++)
        {
            if ((fileList.get(i).getFileName().compareTo(fileName) == 0) && (fileList.get(i).getAccess().compareTo("PUBLIC") == 0))
                return true;
        }
        return false;
    }

    static boolean keyCorrect(String fileName, String key)
    {
        for (int i = 0; i < fileList.size(); i++)
        {
            if ((fileList.get(i).getFileName().compareTo(fileName) == 0) && (fileList.get(i).keyCorrect(key)))
                return true;
        }
        return false;
    }
}



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

    public String toString() {
        return fileName + " " + access + " " + key;
    }

    public String getFileName() {
        return fileName;
    }

    public String getAccess() {return access;}

    public boolean keyCorrect(String key)
    {
        if (key.compareTo(this.key) == 0)
            return true;
        return false;
    }
}



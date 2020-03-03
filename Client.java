import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client
{

    private static int port = 10000;

    static Socket client;

    public static void main(String[] args)
    {
        try
        {
            InetAddress host = InetAddress.getLocalHost();
            client = new Socket(host.getHostAddress(), port);

            Scanner keyboard = new Scanner(System.in);

            DataInputStream input = new DataInputStream(client.getInputStream());
            DataOutputStream output = new DataOutputStream(client.getOutputStream());

            String operation = null;

            while (true)
            {
                System.out.println("Please specify what you would like to do:");
                System.out.println("'Upload', 'Download', 'GetList' or 'Quit'");
                operation = keyboard.nextLine();
                operation = operation.toUpperCase();

                if ((operation.compareTo("UPLOAD") == 0) || (operation.compareTo("DOWNLOAD") == 0) || (operation.compareTo("GETLIST") == 0) || (operation.compareTo("QUIT") == 0))
                {
                    if (operation.compareTo("QUIT") == 0)
                    {
                        output.writeUTF(operation);
                        input.close();
                        output.close();
                        break;
                    }

                    output.writeUTF(operation); // Send the command to the client

                    if (operation.compareTo("UPLOAD") == 0)
                        upload(input, output, keyboard);
                    else if (operation.compareTo("DOWNLOAD") == 0)
                        download(input, output, keyboard);
                    else
                        getList(input);
                }
                else
                    System.out.println("Please type in a valid command.");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                client.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }


    static void upload(DataInputStream input, DataOutputStream output, Scanner keyboard) throws IOException
    {
        boolean fileExist = false;
        String fileName = null;
        File file = null;

        while (!fileExist)
        {
            System.out.println("Please enter the file name you want to upload: ");
            fileName = keyboard.nextLine();

            file = new File(fileName);
            if (file.exists())
            {
                output.writeUTF(fileName);
                String exist = input.readUTF();
                if (exist.compareTo("EXIST") == 0)
                    System.out.println("A file with the same name is already on the server, please check if it is the same file, otherwise change the file's name");
                else
                    fileExist = true;
            }
            else
                System.out.println("File cannot be opened. Please type a valid file name.");
        }

        String access = null;
        String key = "";

        boolean valid = false;
        while (!valid)
        {
            System.out.println("Do you want it to be 'public' or 'private'");
            access = keyboard.nextLine();
            access = access.toUpperCase();
            if ((access.compareTo("PUBLIC") == 0) || (access.compareTo("PRIVATE") == 0))
            {
                valid = true;
                if (access.compareTo("PRIVATE") == 0)
                {
                    System.out.println("Please type in a key that other users can used to access and download the file.");
                    key = keyboard.nextLine();
                }
            }
            else
                System.out.println("Please type in a valid access level, 'public' or 'private'.");
        }

        FileInputStream fi = new FileInputStream(file);

        byte[] buffer = new byte[8192];
        long length = file.length();

        output.writeUTF(access);
        output.writeUTF(key);
        output.writeLong(length);
        output.flush();

        int count;
        while ((count = fi.read(buffer)) > 0)
            output.write(buffer, 0, count);

        fi.close();

        System.out.println("Upload file succeed. \n");

    }


    static void download(DataInputStream input, DataOutputStream output, Scanner keyboard) throws IOException
    {
        String fileName = "";
        boolean exist = false;

        while (!exist)
        {
            System.out.println("Please enter the filename you want to download:");

            fileName = keyboard.nextLine();

            output.writeUTF(fileName);

            String existance = input.readUTF();
            if (existance.compareTo("EXIST") == 0)
                exist = true;
            else if (existance.compareTo("KEY") == 0)
            {
                boolean correct = false;
                exist = true;

                while (!correct)
                {
                    System.out.println("Please enter the key for this file: ");
                    String key = keyboard.nextLine();
                    output.writeUTF(key);

                    String temp = input.readUTF();
                    if (temp.compareTo("CORRECT") == 0)
                        correct = true;
                    else
                        System.out.println("The key you just entered to download the key was incorrect.");
                }
            }
            else
                System.out.println("The file you requested does not exist on the server.");
        }

        FileOutputStream fos = new FileOutputStream(fileName);
        long length = input.readLong();

        byte[] buffer = new byte[8192];
        int read = 0;
        while ((read = input.read(buffer, 0, (int)Math.min(buffer.length, length))) > 0)
        {
            length -= read;
            fos.write(buffer, 0, read);
        }

        fos.close();

        System.out.println("Download file succeed. \n");

    }

    static void getList(DataInputStream input) throws IOException
    {
        String list = input.readUTF();
        if (list.compareTo("") == 0)
            System.out.println("Currently there is no file on the server.");
        else
            System.out.println("The list is as following: \n" + list);
    }
}

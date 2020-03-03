import java.net.*;
import java.io.*;

public class ClientHandler extends Thread
{
    private Socket client;

    private final DataInputStream input;
    private final DataOutputStream output;

    public ClientHandler(Socket client, DataInputStream input, DataOutputStream output)
    {
        this.client = client;
        this.input = input;
        this.output = output;
    }


    public void run()
    {
        try
        {
            boolean exit = false;
            while (!exit)
            {
                String operation = input.readUTF();

                if (operation.compareTo("UPLOAD") == 0)
                    receiveFile(input);
                else if (operation.compareTo("DOWNLOAD") == 0)
                    sendFile(input, output);
                else if (operation.compareTo("GETLIST") == 0)
                    sendList(output);
                else
                    exit = true;
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try {
                input.close();
                output.close();
                client.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            Server.handlers.remove(this);
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



    void receiveFile(DataInputStream input) throws IOException
    {
        String fileName;
        boolean exist;
        while (true)
        {
            fileName = input.readUTF();
            exist = Server.fileExist(fileName);

            if (exist)
                output.writeUTF("EXIST");
            else
            {
                output.writeUTF("NOTEXIST");
                break;
            }
        }

        String access = input.readUTF();
        String key = input.readUTF();

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

        Server.addFile(fileName, access, key);

    }

    void sendList(DataOutputStream output) throws IOException
    {
        output.writeUTF(Server.getFileList());
    }

    void sendFile(DataInputStream input, DataOutputStream output) throws IOException
    {
        boolean exist = false;

        String fileName = "";

        while (!exist)
        {
            fileName = input.readUTF();
            if (Server.fileExist(fileName) == true)
            {
                if (Server.isPublic(fileName) == true)
                    output.writeUTF("EXIST");
                else
                {
                    output.writeUTF("KEY");
                    boolean correct = false;

                    while (!correct)
                    {
                        String key = input.readUTF();
                        correct = Server.keyCorrect(fileName, key);
                        if (correct)
                            output.writeUTF("CORRECT");
                        else
                            output.writeUTF("INCORRECT");
                    }
                }
                exist = true;
            }
            else
                output.writeUTF("NOTEXIST");
        }

        File file = new File(fileName);
        FileInputStream fi = new FileInputStream(file);

        byte[] buffer = new byte[8192];
        long length = file.length();

        output.writeLong(length);
        output.flush();

        int count;
        while ((count = fi.read(buffer)) > 0)
            output.write(buffer, 0, count);

        fi.close();

    }

}

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
            // Check for different operations
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
            System.out.println("Client exceptional exited.");
        }
        finally
        {
            try {
                input.close();
                output.close();
                client.close(); // Make sure the program end properly
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            Server.handlers.remove(this);
        }

    }


    /** Receive the file when the client uploads it
     *
     * @param input
     * @throws IOException
     */
    void receiveFile(DataInputStream input) throws IOException
    {
        String fileName;
        boolean exist;
        // Check if the file exists on the server side, if true, then tell the client it already exists on the server, and ask for another file
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

        // Write the data to the file and save
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

        // Add the file to the fileList, and update it accordingly
        Server.addFile(fileName, access, key);

    }

    /** Send the file list to the client
     *
     * @param output
     * @throws IOException
     */
    void sendList(DataOutputStream output) throws IOException
    {
        output.writeUTF(Server.getFileList());
    }

    /** When user requests a download, it sends the file to the user
     *
     * @param input
     * @param output
     * @throws IOException
     */
    void sendFile(DataInputStream input, DataOutputStream output) throws IOException
    {
        boolean exist = false;

        String fileName = "";

        while (!exist)
        {
            fileName = input.readUTF();
            // Check if the file exists at the server side
            if (Server.fileExist(fileName))
            {
                if (Server.isPublic(fileName))
                    output.writeUTF("EXIST");
                else
                {
                    // If the file requires a key, ask for the key until it is correct
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

        // Send the file to the client
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

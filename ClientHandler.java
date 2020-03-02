import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class ClientHandler extends Thread {

    private boolean checkValid;
    private Socket client;
    /*
     * private final DataInputStream input; private final DataOutputStream output;
     */


    
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






    // BufferedReader and PrintWriter creations
    private final BufferedReader input;
    private final PrintWriter output;

    private static String filePath = "list.txt";

    private ArrayList<ClientHandler.FileInfo> fileList = new ArrayList<>();

    class FileInfo {
        String fileName;
        String key;
        String access;

        FileInfo(String fileName, String access) {
            this.fileName = fileName;
            this.access = access;
            key = "";
        }

        FileInfo(String fileName, String access, String key) {
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
    }

    // New constructor utilising BufferedReader and PrintWriter

    public ClientHandler(Socket client, BufferedReader input, PrintWriter output) {
        this.client = client;
        this.input = input;
        this.output = output;
    }

    // Old constructor utilising DataInputStream and DataOutputStream
    /*
     * public ClientHandler(Socket client, DataInputStream input, DataOutputStream
     * output) { this.client = client; this.input = input; this.output = output; }
     */

    public void run() {
        try {
            checkValid = false;
            while (!checkValid) {
                // Here is where the sequences gonna happen
                String request = input.readLine();

                if (request.startsWith("exit")) {
                    handleExit();
                    checkValid = true;
                } else if (request.startsWith("upload")) {
                    receiveFile();
                    checkValid = true;
                } else if (request.startsWith("download")) { // Still need to implement a FileExists check
                    sendFile();
                    checkValid = true;
                } else if (request.startsWith("getlist")) {
                    sendList();
                    checkValid = true;
                } else {
                    handleBadRequest();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            input.close();
            output.close();
            client.close();
            Server.handlers.remove(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void receiveFile() {
        ;
    }

    void sendList() {
        ;
    }

    void sendFile() {
        ;
    }

    void loadFileList() throws IOException {
        File fileList = new File(filePath);
        BufferedReader input = new BufferedReader(new FileReader(fileList));
        String info;
        while ((info = input.readLine()) != null) {
            StringTokenizer token = new StringTokenizer(info);
            String name = token.nextToken();
            String access = token.nextToken();
            FileInfo file;
            if (access.compareTo("private") == 0) {
                file = new FileInfo(name, access, token.nextToken());
            } else {
                file = new FileInfo(name, access);
            }

            this.fileList.add(file);
        }

        input.close();

    }

    String getFileList() {
        String temp = "";

        for (int i = 0; i < fileList.size(); i++) {
            temp += (fileList.get(i).getFileName() + "\n");
        }

        return temp;
    }

    boolean fileExist(String fileName) {
        for (int i = 0; i < fileList.size(); i++) {
            if (fileList.get(i).getFileName().compareTo(fileName) == 0) {
                return true;
            }
        }
        return false;
    }

    void addFile(String fileName, String access) throws IOException {
        FileInfo file = new FileInfo(fileName, access);
        fileList.add(file);
        saveList(file);
    }

    void addFile(String fileName, String access, String key) throws IOException {
        FileInfo file = new FileInfo(fileName, access, key);
        fileList.add(file);
        saveList(file);
    }

    void saveList(FileInfo file) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(filePath), true);
        writer.write(file.toString());

        writer.close();
    }

    void handleExit() {
        System.exit(0);
    }

    void handleBadRequest() {
        System.out.print("Please enter a valid request \n Either 'Upload', 'Download', or 'GetList':");
    }


}

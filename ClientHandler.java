import java.net.*;
import java.io.*;

public class ClientHandler extends Thread{

        private Socket client;
        private final DataInputStream input;
        private final DataOutputStream output;


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
            ;
        }

        void sendList()
        {
            ;
        }

        void sendFile()
        {
            ;
        }
}

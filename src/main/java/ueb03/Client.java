package ueb03;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {

    private Socket clientSocket;
    private OutputStream os;
    private InputStream is;

    public Client(String host, int port) throws IOException {
        clientSocket = new Socket(host, port);
    }

    public void connect() throws IOException {
        System.out.println("trying to connect...");

        os = clientSocket.getOutputStream();
        is = clientSocket.getInputStream();


    }

    public void sendLogMessage(logserver.LogMessageOuterClass.LogMessage logm) throws IOException {

        logm.writeTo(os);
        byte[] breakM = "\n".getBytes();
        os.write(breakM);

        int b;
        while (true) {
            try {
                if ((b = is.read()) == -1) {
                    break;
                }
                System.out.print((char) b);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

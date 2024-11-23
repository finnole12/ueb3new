package ueb03;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {

    public static void startClientConnections () {
        Client client;
        try {
            client = new Client("stud.fh-wedel.de", 80);
            client.connect();
            client.sendLogMessage(getLogm());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static logserver.LogMessageOuterClass.LogMessage getLogm() {
        logserver.LogMessageOuterClass.LogMessage.Builder logmBuilder = logserver.LogMessageOuterClass.LogMessage.newBuilder();

        logmBuilder.setCreator("me");
        logmBuilder.setSeverity(logserver.LogMessageOuterClass.LogMessage.Severity.FATAL);
        logmBuilder.setMessage("hi");
        logmBuilder.setTimestamp(2);

        return logmBuilder.build();
    }

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

package ueb03;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;

/*
 * Test class for client-Server-communication. The client will send a 
 * request to the server and will recieve a html file. the file contents will be
 * printed to the terminal.
 * While the client and the server communicate it is possible to communicate with
 * the server through a webbrowser or telnet at the same time.
 */
public class testStart {

    /*
     * Creates and starts a server instance and a client instance.
     * The client sends a GET request to the server.
     */
    public static void main (String[] args) {

        File logfile = new File("./log.txt");
        if (logfile.exists()) logfile.delete();

        // creating the server
        Server server = new Server();
        try {
            // starting the server
            server.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // creating a client
        final Thread clientThread = new Thread(() -> {
            try {
                Client client = new Client("localhost", 12345);
                client.connect();
                client.sendLogMessage(getLogm("me", "hamburg", "hi"));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        });
        // creating a client
        final Thread clientThread2 = new Thread(() -> {
            try {
                Client client = new Client("localhost", 12345);
                client.connect();
                client.sendLogMessage(getLogm("Finn Ole", "here", "wassup"));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        });

        clientThread.start();
        clientThread2.start();
    }

    private static logserver.LogMessageOuterClass.LogMessage getLogm(String creator, String location, String message) {
        logserver.LogMessageOuterClass.LogMessage.Builder logmBuilder =
                logserver.LogMessageOuterClass.LogMessage.newBuilder();

        logmBuilder.setCreator(creator);
        logmBuilder.setSeverity(
                logserver.LogMessageOuterClass.LogMessage.Severity.forNumber(new Random().nextInt(5)));
        logmBuilder.setMessage(message);
        logmBuilder.setLocation(location);
        logmBuilder.setTimestamp(1);

        return logmBuilder.build();
    }
}

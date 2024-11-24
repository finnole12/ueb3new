package ueb03;

import logserver.LogMessageOuterClass;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * Server that accepts Client requests. Each client will be dealt with in a seperate
 * Thread.
 */
public class Server {

    int port = 12345;
    String filePath = "ueb2\\files\\testseite.html";
    File file = new File(filePath);

    /*
     * Starts the server. Will continuisly listen for incoming client requests.
     * 
     */
    public void startServer() throws IOException {
        System.out.println("starting server...");

        try {
            ServerSocket serverSocket = new ServerSocket(port);

            // Thread tasked with accepting incoming client requests
            final Thread thread = new Thread() {
                public void run() {
                    int i = 0;
                    while(true) {
                        try {
                            Socket clientSocket = serverSocket.accept();
                            System.out.println("client connected");
                            startClientThread(clientSocket, i++);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            thread.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Starts a new Thread tasked with recieving messages from the clientSocket.
     * If the input matches the filepath the file will be send to the client.
     */
    private void startClientThread(Socket clientSocket, int num) {
        final Thread thread = new Thread() {
            public void run() {
                try {
                    BufferedReader iSR = new BufferedReader(
                        new InputStreamReader (clientSocket.getInputStream())
                    );
                    OutputStream oS = clientSocket.getOutputStream();


                    try {
                        System.out.println("readstart");
                        while(true) {
                            byte[] bytes = iSR.readLine().getBytes();
                            LogMessageOuterClass.LogMessage logm =
                                    LogMessageOuterClass.LogMessage.parseFrom(bytes);
                            System.out.println(logm.getMessage());
                            writeToLogFile(logm);
                        }
                        //System.out.println("readend");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    private void writeToLogFile(LogMessageOuterClass.LogMessage logm) {
        File logfile = new File("./log.txt");
        try {
            if (!logfile.exists()) logfile.createNewFile();

            FileOutputStream fIS = new FileOutputStream(logfile, true);
            String logEntry = String.format("%-6s  (%s): Message recieved from %-11s\tlocated at %-10s \t\"%s\"\n",
                    logm.getSeverity(), logm.getTimestamp(), logm.getCreator(), logm.getLocation() + ".", logm.getMessage());

            fIS.write(logEntry.getBytes());
            fIS.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

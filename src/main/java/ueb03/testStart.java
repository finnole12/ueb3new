package ueb03;

import java.io.IOException;

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

        // creating the server
        Server server = new Server();
        try {
            // starting the server
            server.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // creating a client
        final Thread clientThread = new Thread() {
            public void run() {
                try {
                    Client client = new Client("localhost", 12345);
                    client.connect();
                    // sending a request to the server
                    client.sendLogMessage(Client.getLogm());
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        };
        
        // creating another client

        clientThread.start();
    }
}

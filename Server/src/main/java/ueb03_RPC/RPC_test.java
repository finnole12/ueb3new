package ueb03_RPC;

import com.google.protobuf.InvalidProtocolBufferException;
import rpcDatabaseServer.RPCRequest;

import java.io.File;
import java.io.IOException;

/*
 * Test class for client-Server-communication. The client will send a 
 * request to the server and will recieve a html file. the file contents will be
 * printed to the terminal.
 * While the client and the server communicate it is possible to communicate with
 * the server through a webbrowser or telnet at the same time.
 */
public class RPC_test {

    /*
     * Creates and starts a server instance and a client instance.
     * The client sends a GET request to the server.
     */
    public static void main (String[] args) {

        File logfile = new File("./log.txt");
        if (logfile.exists()) logfile.delete();

        // creating the server
        RPC_Server server = new RPC_Server();
        try {
            // starting the server
            server.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

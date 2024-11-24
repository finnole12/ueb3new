package ueb03_RPC;

import rpcDatabaseServer.RPCRequest;
import ueb03.Client;
import ueb03.Server;

import java.io.File;
import java.io.IOException;
import java.util.Random;

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

//        // creating a client
//        final Thread clientThread = new Thread(() -> {
//            try {
//                RPC_Client client = new RPC_Client("localhost", 12345);
//                client.connect();
//                client.sendRPC_Request(buildGetSizeRPC());
//                client.awaitResponse();
//            } catch (IOException e) {
//                System.out.println(e.getMessage());
//            }
//            System.out.println("Client: thread ended");
//        });

        // creating a client
        final Thread clientThread2 = new Thread(() -> {
            try {
                RPC_Client client = new RPC_Client("localhost", 12345);
                client.connect();
//                client.sendRPC_Request(buildGetSizeRPC());
                client.sendRPC_Request(buildAddRecordRPC("newrec", 0));
                client.awaitResponse();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            System.out.println("Client: thread ended");
        });

//        // creating a client
//        final Thread clientThread3 = new Thread(() -> {
//            try {
//                RPC_Client client = new RPC_Client("localhost", 12345);
//                client.connect();
//                client.sendRPC_Request(buildGetSizeRPC());
//                client.awaitResponse();
//            } catch (IOException e) {
//                System.out.println(e.getMessage());
//            }
//            System.out.println("Client: thread ended");
//        });

//        clientThread.start();
        clientThread2.start();
//        clientThread3.start();
    }

    private static RPCRequest.RPC_Request buildGetSizeRPC () {
        RPCRequest.RPC_Request.Builder rpcBuilder = RPCRequest.RPC_Request.newBuilder();

        rpcBuilder.setOperation(RPCRequest.RPC_Request.Operation.GETSIZE);

        return rpcBuilder.build();
    }

    private static RPCRequest.RPC_Request buildAddRecordRPC (String record, int index) {
        RPCRequest.RPC_Request.Builder rpcBuilder = RPCRequest.RPC_Request.newBuilder();

        rpcBuilder.setOperation(RPCRequest.RPC_Request.Operation.ADDRECORD);
        rpcBuilder.setAddRecordRequest(RPCRequest.RPC_Request.AddRecord_Request.newBuilder()
            .setRecord(record)
            .setIndex(index).build());

        return rpcBuilder.build();
    }

    private static RPCRequest.RPC_Request buildGetRecordRPC (int index) {
        RPCRequest.RPC_Request.Builder rpcBuilder = RPCRequest.RPC_Request.newBuilder();

        rpcBuilder.setOperation(RPCRequest.RPC_Request.Operation.GETRECORD);
        rpcBuilder.setGetRecordRequest(RPCRequest.RPC_Request.GetRecord_Request.newBuilder()
            .setIndex(index));

        return rpcBuilder.build();
    }
}

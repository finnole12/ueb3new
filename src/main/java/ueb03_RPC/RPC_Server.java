package ueb03_RPC;

import logserver.LogMessageOuterClass;
import rpcDatabaseServer.RPCRequest;
import rpcDatabaseServer.RPCResponse;

import javax.naming.InvalidNameException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/*
 * Server that accepts Client requests. Each client will be dealt with in a seperate
 * Thread.
 */
public class RPC_Server {

    private final int port = 12345;
    ArrayList<String> database = new ArrayList<>();

    /*
     * Starts the server. Will continuisly listen for incoming client requests.
     * 
     */
    public void startServer() throws IOException {
        System.out.println("Server: starting");
        initilizeDatabase();

        try {
            ServerSocket serverSocket = new ServerSocket(port);

            // Thread tasked with accepting incoming client requests
            final Thread thread = new Thread(() -> {
                int i = 0;
                while(true) {
                    try {
                        Socket clientSocket = serverSocket.accept();
                        clientSocket.setKeepAlive(true);
                        System.out.println("Server: client connected");
                        startClientThread(clientSocket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startClientThread(Socket clientSocket) {
        final Thread thread = new Thread(() -> {
            while (true) {
                try {
                    RPCRequest.RPC_Request rpc_request =
                            RPCRequest.RPC_Request.parseDelimitedFrom(clientSocket.getInputStream());
                    handleResponse(rpc_request, clientSocket);
                } catch (IOException e) {
                    System.out.println("Server: Error: " + e.getMessage());
                    e.printStackTrace();
                    break;
                }
            }
            System.out.println("Server: thread ended");
        });
        thread.start();
    }

    private void handleResponse(RPCRequest.RPC_Request rpc_request, Socket clientSocket) throws IOException {
        String output = distributeRequests(rpc_request);

        // build rpc response
        rpcDatabaseServer.RPCResponse.RPC_Response rpc_response = rpcDatabaseServer.RPCResponse.RPC_Response.newBuilder()
                .setResponse(output)
                .build();

        // send back to client
        OutputStream oS = clientSocket.getOutputStream();
        rpc_response.writeDelimitedTo(oS);
        oS.flush();
    }

    private String distributeRequests(RPCRequest.RPC_Request rpc_request) {
        String output;
        System.out.println("Server: Operation request for: " + rpc_request.getOperation());
        switch(rpc_request.getOperation()) {
            case GETSIZE:
                output = "" + getSize();
                break;
            case GETRECORD:
                output = getRecord(rpc_request.getGetRecordArgs().getIndex());
                break;
            case ADDRECORD:
                output = "" + addRecord(
                        rpc_request.getAddRecordArgs().getRecord(),
                        rpc_request.getAddRecordArgs().getIndex()
                );
                break;
            default:
                output = "Error: Invalid Operation";
        }
        return output;
    }

    private int getSize() {
        return database.size();
    }

    private String getRecord(int index) {
        return database.get(index);
    }

    private boolean addRecord(String record, int index) {
        database.add(index, record);
        return true;
    }

    private void initilizeDatabase() {
        for (int i = 0; i < 5000; i++) {
            database.add("Random Entry number: " + new Random().nextInt());
        }
    }
}

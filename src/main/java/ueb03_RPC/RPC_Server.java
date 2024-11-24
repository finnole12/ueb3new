package ueb03_RPC;

import logserver.LogMessageOuterClass;
import rpcDatabaseServer.RPCRequest;

import javax.naming.InvalidNameException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/*
 * Server that accepts Client requests. Each client will be dealt with in a seperate
 * Thread.
 */
public class RPC_Server {

    private int port = 12345;
    ArrayList<String> database = new ArrayList<>();

    /*
     * Starts the server. Will continuisly listen for incoming client requests.
     * 
     */
    public void startServer() throws IOException {
        System.out.println("Server: starting");

        try {
            ServerSocket serverSocket = new ServerSocket(port);

            // Thread tasked with accepting incoming client requests
            final Thread thread = new Thread() {
                public void run() {
                    int i = 0;
                    while(true) {
                        try {
                            Socket clientSocket = serverSocket.accept();
                            System.out.println("Server: client connected");
                            startClientThread(clientSocket);
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
    private void startClientThread(Socket clientSocket) {
        final Thread thread = new Thread(() -> {
            try {
                BufferedReader iSR = new BufferedReader(new InputStreamReader (clientSocket.getInputStream()));
                try {
                    System.out.println("Server: Incoming Request");
                    while(true) {
                        byte[] bytes = iSR.readLine().getBytes();
                        RPCRequest.RPC_Request rpc_request = RPCRequest.RPC_Request.parseFrom(bytes);
                        handleResponse(rpc_request, clientSocket);
                    }
                    //System.out.println("readend");
                } catch (IOException e) {
                    System.out.println("Server: Error: " + e.getMessage());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Server: thread ended");
        });
        thread.start();
    }

    private void handleResponse(RPCRequest.RPC_Request rpc_request, Socket clientSocket) throws IOException {
        String output = distributeRequests(rpc_request);
        System.out.println("Server: sending: " + output);

        // send back to client
        OutputStream oS = clientSocket.getOutputStream();
        oS.write((output + "\n").getBytes());
        oS.close();
    }

    private String distributeRequests(RPCRequest.RPC_Request rpc_request) {
        String output;
        System.out.println("Server: Operation request for: " + rpc_request.getOperation());
        switch(rpc_request.getOperation()) {
            case GETSIZE:
                output = "" + getSize();
                break;
            case GETRECORD:
                output = getRecord(rpc_request.getGetRecordRequest().getIndex());
                break;
            case ADDRECORD:
                output = "" + addRecord(
                        rpc_request.getAddRecordRequest().getRecord(),
                        rpc_request.getAddRecordRequest().getIndex()
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
}

package ueb03_RPC;

import rpcDatabaseServer.RPCRequest;
import rpcDatabaseServer.RPCResponse;

import java.io.*;
import java.net.Socket;

public class RPC_Client {

    private Socket clientSocket;
    private BufferedOutputStream bOS;
    private InputStream is;

    public RPC_Client(String host, int port) throws IOException {
        clientSocket = new Socket(host, port);
    }

    public void connect() throws IOException {
        System.out.println("Client: trying to connect");

        is = clientSocket.getInputStream();
        bOS = new BufferedOutputStream(clientSocket.getOutputStream());
    }

    public void sendRPC_Request(RPCRequest.RPC_Request[] rpc_requests) throws IOException {
        for (RPCRequest.RPC_Request rpc_request : rpc_requests) {
            rpc_request.writeDelimitedTo(bOS);
            bOS.flush();
            awaitResponse();
        }
    }

    void awaitResponse() throws IOException {

        RPCResponse.RPC_Response rpc_response = RPCResponse.RPC_Response.parseDelimitedFrom(is);

        String response = rpc_response.getResponse();
        System.out.println("Client: recieved: " + response);
    }
}

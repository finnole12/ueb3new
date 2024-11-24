package ueb03_RPC;

import rpcDatabaseServer.RPCRequest;

import java.io.*;
import java.net.Socket;

public class RPC_Client {

    private Socket clientSocket;
    private OutputStream os;
    private InputStream is;

    public RPC_Client(String host, int port) throws IOException {
        clientSocket = new Socket(host, port);
    }

    public void connect() throws IOException {
        System.out.println("Client: trying to connect");

        os = clientSocket.getOutputStream();
        is = clientSocket.getInputStream();
    }

    public void sendRPC_Request(RPCRequest.RPC_Request rpc_request) throws IOException {
        rpc_request.writeTo(os);
        byte[] breakM = "\n".getBytes();
        os.write(breakM);
    }

    public void awaitResponse() throws IOException {
        System.out.println("Client: awaiting response");
        BufferedReader bISR = new BufferedReader(new InputStreamReader(is));

        String response = bISR.readLine();
        System.out.println("Client: recieved: " + response);

        os.close();
        is.close();
    }
}

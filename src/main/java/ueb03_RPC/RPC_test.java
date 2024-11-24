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
    public static void main (String[] args) throws InterruptedException {

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

        // creating a client
        final Thread clientThread = new Thread(() -> {
            try {
                RPC_Client client = new RPC_Client("localhost", 12345);
                client.connect();
                client.sendRPC_Request(getRequestArr());
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Client: thread ended");
        });

        clientThread.start();
    }

    private static RPCRequest.RPC_Request buildGetSizeRPC () {
        RPCRequest.RPC_Request.Builder rpcBuilder = RPCRequest.RPC_Request.newBuilder();

        rpcBuilder.setOperation(RPCRequest.RPC_Request.Operation.GETSIZE);

        return rpcBuilder.build();
    }

    private static RPCRequest.RPC_Request buildAddRecordRPC (String record, int index) throws InvalidProtocolBufferException {

        RPCRequest.AddRecord_Request addRecord_request =
                RPCRequest.AddRecord_Request.newBuilder().setRecord(record).setIndex(index).build();

        RPCRequest.RPC_Request request =
                RPCRequest.RPC_Request.newBuilder().setOperation(RPCRequest.RPC_Request.Operation.ADDRECORD).setAddRecordArgs(addRecord_request).build();

        RPCRequest.RPC_Request rpc = RPCRequest.RPC_Request.parseFrom(request.toByteArray());

        return request;
    }

    private static RPCRequest.RPC_Request buildGetRecordRPC (int index) {
        RPCRequest.RPC_Request.Builder rpcBuilder = RPCRequest.RPC_Request.newBuilder();

        rpcBuilder.setOperation(RPCRequest.RPC_Request.Operation.GETRECORD);
        rpcBuilder.setGetRecordArgs(RPCRequest.GetRecord_Request.newBuilder()
            .setIndex(index));

        return rpcBuilder.build();
    }

    private static RPCRequest.RPC_Request[] getRequestArr() throws InvalidProtocolBufferException {
        RPCRequest.RPC_Request[] requests = new RPCRequest.RPC_Request[9];
        requests[0] = buildAddRecordRPC("Appen", 4101);
        requests[1] = buildAddRecordRPC("Ahrensburg", 4102);
        requests[2] = buildAddRecordRPC("Wedel", 4103);
        requests[3] = buildAddRecordRPC("Aumühle", 4104);
        requests[4] = buildAddRecordRPC("Seevetal", 4105);
        requests[5] = buildAddRecordRPC("Quickborn", 4106);
        requests[6] = buildGetRecordRPC(4103);
        requests[7] = buildGetRecordRPC(4107);
        requests[8] = buildGetSizeRPC();
        return requests;
    }
}

package ueb4;

import gRPCServer.GrpcServer;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class ClientMain {

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8999).usePlaintext().build();
        gRPCServer.ServiceGrpc.ServiceBlockingStub serviceStub = gRPCServer.ServiceGrpc.newBlockingStub(channel);

        gRPCServer.GrpcServer.RPC_Response getSize_response =
                serviceStub.getSize(GrpcServer.GetSize_Request.newBuilder().build());
        System.out.println(getSize_response.getResponse());
    }
}

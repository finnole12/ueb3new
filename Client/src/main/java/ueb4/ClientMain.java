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
        System.out.println("Size: " + getSize_response.getResponse());

        serviceStub.addRecord(
                GrpcServer.AddRecord_Request.newBuilder().setRecord("Appen").setIndex(4101).build()
        );
        serviceStub.addRecord(
                GrpcServer.AddRecord_Request.newBuilder().setRecord("Ahrensburg").setIndex(4102).build()
        );
        serviceStub.addRecord(
                GrpcServer.AddRecord_Request.newBuilder().setRecord("Wedel").setIndex(4103).build()
        );
        serviceStub.addRecord(
                GrpcServer.AddRecord_Request.newBuilder().setRecord("Aumühle").setIndex(4104).build()
        );
        serviceStub.addRecord(
                GrpcServer.AddRecord_Request.newBuilder().setRecord("Seevetal").setIndex(4105).build()
        );
        serviceStub.addRecord(
                GrpcServer.AddRecord_Request.newBuilder().setRecord("Quickborn").setIndex(4106).build()
        );

        gRPCServer.GrpcServer.RPC_Response getRecord_response =
                serviceStub.getRecord(GrpcServer.GetRecord_Request.newBuilder().setIndex(4103).build());
        System.out.println("Record at 4103: " + getRecord_response.getResponse());

        gRPCServer.GrpcServer.RPC_Response getRecord_response2 =
                serviceStub.getRecord(GrpcServer.GetRecord_Request.newBuilder().setIndex(4103).build());
        System.out.println("Record at 4107: " + getRecord_response2.getResponse());

        gRPCServer.GrpcServer.RPC_Response getSize_response2 =
                serviceStub.getSize(GrpcServer.GetSize_Request.newBuilder().build());
        System.out.println("Size: " + getSize_response2.getResponse());
    }
}

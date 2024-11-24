package ueb4;


import gRPCServer.GrpcServer;

import java.util.ArrayList;
import java.util.Random;

import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

public class ServerImpl extends gRPCServer.ServiceGrpc.ServiceImplBase {

    ArrayList<String> arrayList = new ArrayList<>();

    public ServerImpl() {
        initilizeDB();
    }

    @Override
    public void getSize(gRPCServer.GrpcServer.GetSize_Request request,
                        io.grpc.stub.StreamObserver<gRPCServer.GrpcServer.RPC_Response> responseObserver) {
        responseObserver.onNext(gRPCServer.GrpcServer.RPC_Response.newBuilder().setResponse("" + arrayList.size()).build());
        responseObserver.onCompleted();
    }

    @Override
    public void addRecord(gRPCServer.GrpcServer.AddRecord_Request request,
                          io.grpc.stub.StreamObserver<gRPCServer.GrpcServer.RPC_Response> responseObserver) {

        arrayList.add( request.getIndex(), request.getRecord());

        responseObserver.onNext(gRPCServer.GrpcServer.RPC_Response.newBuilder().setResponse("record added").build());
        responseObserver.onCompleted();
    }

    @Override
    public void getRecord(gRPCServer.GrpcServer.GetRecord_Request request,
                          io.grpc.stub.StreamObserver<gRPCServer.GrpcServer.RPC_Response> responseObserver) {

        String record = arrayList.get(request.getIndex());

        responseObserver.onNext(GrpcServer.RPC_Response.newBuilder().setResponse(record).build());
        responseObserver.onCompleted();
    }

    private void initilizeDB() {
        for (int i = 0; i < 5000; i++) {
            arrayList.add(i, "Random entry: " + new Random().nextInt());
        }
    }
}

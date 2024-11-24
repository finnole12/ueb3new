package ueb4;


public class ServerImpl extends gRPCServer.ServiceGrpc.ServiceImplBase {

    @Override
    public void getSize(gRPCServer.GrpcServer.GetSize_Request request,
                        io.grpc.stub.StreamObserver<gRPCServer.GrpcServer.RPC_Response> responseObserver) {
        responseObserver.onNext(gRPCServer.GrpcServer.RPC_Response.newBuilder().setResponse("testresponse").build());
        responseObserver.onCompleted();
    }
}

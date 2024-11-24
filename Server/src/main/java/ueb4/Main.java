package ueb4;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class Main {

    public static void main (String[] args) {
        try {
            Server server = ServerBuilder.forPort(8999).addService(new ServerImpl()).build();
            server.start();
            System.out.println("Server: started");
            server.awaitTermination();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

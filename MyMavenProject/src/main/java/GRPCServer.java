import io.grpc.Server;
import io.grpc.ServerBuilder;
//import user.UserService;
import image.imageService;

import java.io.IOException;

public class GRPCServer {
    public static void main(String[] args) throws IOException, InterruptedException {

        System.out.println("Starting grpc Server");
        //Server server = ServerBuilder.forPort(9090).addService(new UserService()).build();
        Server server = ServerBuilder.forPort(9090).addService(new imageService()).build();
        server.start();

        System.out.println("Server started at "+ server.getPort());
        server.awaitTermination();
    }
}

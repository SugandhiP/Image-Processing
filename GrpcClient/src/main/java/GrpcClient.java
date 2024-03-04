/*import com.yrrhelp.grpc.User;
import com.yrrhelp.grpc.userGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GrpcClient {

    public static void main(String[] args){

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",9090).usePlaintext().build();
        userGrpc.userBlockingStub userStub = userGrpc.newBlockingStub(channel);
        User.LoginRequest loginRequest = User.LoginRequest.newBuilder().setUsername("RAM").setPassword("RAM").build();
        User.APIResponse response = userStub.login(loginRequest);

        System.out.println(response.getResponsemessage());
    }
}*/

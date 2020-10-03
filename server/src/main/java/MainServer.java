
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class MainServer {
    public static void main(String[] args) {
        new Server();
//        try(ServerSocket serverSocket = new ServerSocket(8085)){
//            System.out.println("--> Server listening");
//            AuthService.connect();
//            int id = AuthService.getUserIdWithPassAuthorized("Fedia","fedia");
//            System.out.println("feria id is - "+ id);
//            binaryVersion(serverSocket);
//
//        }catch (IOException e){
//            e.printStackTrace();
//        }
    }

    private static void binaryVersion(ServerSocket serverSocket){
        try(Socket socket = serverSocket.accept()){
            FileMessage fileMessage = new FileMessage();
            fileMessage.acceptFileMessage(socket);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}

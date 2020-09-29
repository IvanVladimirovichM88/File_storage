import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;
import java.sql.SQLOutput;

public class MainServer {
    public static void main(String[] args) {

        try(ServerSocket serverSocket = new ServerSocket(8085)){
            System.out.println("--> Server listening");
            binaryVersion(serverSocket);
        }catch (IOException e){
            e.printStackTrace();
        }
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

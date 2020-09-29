import java.io.IOException;
import java.net.Socket;
import java.nio.file.Paths;

public class MainClient {
    public static void main(String[] args) {
        FileMessage fileMessage = new FileMessage(Paths.get("client/newFile.txt"));
        binaryVersion(fileMessage);
    }

    private static void binaryVersion( FileMessage fileMessage ) {
        try(Socket socket = new Socket("127.0.0.1",8085)){
            fileMessage.sendFileMessage(socket);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}

import java.io.IOException;
import java.net.Socket;
import java.nio.file.Paths;

public class MainClient {
    public static void main(String[] args) {
        Client client = new Client();
        //подключение иавторизация клиента
        client.connect();

        client.showAllFiles();

        System.out.println();

        client.deleteFile(Paths.get("client/newFile.txt"));

        System.out.println();

        client.showAllFiles();

       // client.sendFile(Paths.get("client/newFile.txt"));



//        FileMessage fileMessage = new FileMessage(Paths.get("client/newFile.txt"));
//        binaryVersion(fileMessage);
    }

}

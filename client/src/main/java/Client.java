import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Path;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private DataInputStream inStream;
    private DataOutputStream outStream;
    private int idClient;

    final String IP_ADDRESS = "localhost";
    final int PORT = 8189;

    public Client(){
        idClient = -1;
        try {
            socket = new Socket(IP_ADDRESS, PORT);
            inStream = new DataInputStream(socket.getInputStream());
            outStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connect(){

        while(true){
            Scanner scanner = new Scanner(System.in);
            String name = new String();
            String password =new String();
            System.out.println( "input Name");
            name = scanner.nextLine();
            System.out.println("input Password");
            password = scanner.nextLine();
            CommandMessage.sendAuthorized(name,password,outStream);

            try {
                idClient =  inStream.readInt();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (idClient > 0){
                System.out.println( "-->  client id " + idClient);
                break;
            }else{
                System.out.println("-->  invalid password/name");
            }
        }

    }

    public void sendFile(Path path){
        FileMessage fileMessage = new FileMessage(path);
        fileMessage.sendFileMessage(socket);
    }

    public void showAllFiles(){
        CommandMessage.requestAllFiles(outStream);

        try {
            String[] allFiles = CommandMessage.acceptAllFiles(inStream);
            for (String s :
                    allFiles) {
                System.out.println(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

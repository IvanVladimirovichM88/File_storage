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

        binaryVersion();
    }

    private static void binaryVersion(){
        try(ServerSocket sc = new ServerSocket(8085)){
            System.out.println("Server is listening");
            try(Socket socket= sc.accept();
                BufferedInputStream in = new BufferedInputStream(socket.getInputStream())){
                ProtocolSendFile receivedFile = new ProtocolSendFile();
                receivedFile.init(in);
                FileMessage fileMessage = new FileMessage(receivedFile);

                System.out.println(" file name -> " + fileMessage.getFileName());
                System.out.println(" file size -> " + fileMessage.getSize());
                System.out.println( " file content : ");
                fileMessage.printContent();

                try (FileOutputStream fileOut = new FileOutputStream("server/newFile.txt")){
                    fileOut.write(fileMessage.getBytes());
                }

//                int n ;
//                while ((n=in.read())!=-1){
//                    System.out.print((char)n);
//                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private static void serializableVersion(){
        try(ServerSocket sc = new ServerSocket(8085)){
            System.out.println("Server is listening");
            try(Socket socket= sc.accept(); ObjectInputStream in = new ObjectInputStream(socket.getInputStream())){
                FileMessage cp = (FileMessage)in.readObject();
                byte[] bytes = cp.getBytes();
                for(byte b: bytes){
                    System.out.print((char)b);
                }
            }
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}

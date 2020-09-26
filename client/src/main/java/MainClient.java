import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.util.Arrays;

public class MainClient {
    public static void main(String[] args) {

        FileMessage fileMessage = new FileMessage(Paths.get("client/newFile.txt"));
        byte[] buffer = fileMessage.toByteArray();
        System.out.print("command ->  ");
        for (int i=0; i<4; i++){
            System.out.print( (char) buffer[i]);
        }
        System.out.println();
        System.out.print("name file ->  ");
        for (int i=4; i<4+128; i++){
            System.out.print( (char) buffer[i]);
        }
        System.out.println();
        System.out.print("size of file ->  ");
        for (int i=4+128;i<4+128+4;i++){
            System.out.print( buffer[i]);
        }
        byte[] byteSize = Arrays.copyOfRange(buffer,4+128,4+128+4);
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteSize);

        System.out.println("\n"+byteBuffer.getInt());

        System.out.println();
        System.out.print("file  ->  ");
        for (int i=4+128+4;i<4+128+4+fileMessage.getSize();i++){
            System.out.print( (char) buffer[i]);
        }

        binaryVersion(buffer);
    }

    private static void serializableVersion(){
        try(Socket socket = new Socket("127.0.0.1",8085)){
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            FileMessage fm = new FileMessage(Paths.get("client/newFile.txt"));
            out.writeObject(fm);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private static void binaryVersion( byte [] bytes ){
        try(Socket socket = new Socket("127.0.0.1",8085)){
            //byte [] bytes={65,66,67,68,69,49,50,51};
            socket.getOutputStream().write(bytes);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}

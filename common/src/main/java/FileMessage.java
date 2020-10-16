
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;


public class FileMessage implements Serializable {
    private String fileName;
    private Path path;
    private long size;
    private final int bufferLen = 1024;
    private final String serverMainCatalog = "server/";
    private final String clientMainCatalog = "client/";

    public FileMessage(){}

    public FileMessage(Path path) {
        try {
            this.path = path;
            this.fileName = path.getFileName().toString();
            this.size = (int)Files.size(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendFileMessage(Socket socket){
        try {
            DataOutputStream out = new DataOutputStream((socket.getOutputStream()));
            //send command byte
            out.write(70);
            //send len of file name
            out.writeShort((short)fileName.length());
            //send file name
            out.write(fileName.getBytes());
            //send len of file
            out.writeLong(size);
            //send file content
            byte[] buffer = new byte[bufferLen];
            try (InputStream in = new FileInputStream(path.toFile())){
                int n;
                while ((n = in.read(buffer)) != -1){
                    out.write(buffer, 0, n);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String acceptFileMessage(DataInputStream inputStream, int userId){
        try {
            short lenFileName = inputStream.readShort();
            byte[] bytesFilename = new byte[lenFileName];
            inputStream.read(bytesFilename);
            fileName = new String(bytesFilename);
            size = inputStream.readLong();
            //проверяем существует ли каталог для пользователя

            String fullFileName = serverMainCatalog + userId + "/" + fileName;
            try(OutputStream outFile = new BufferedOutputStream(new FileOutputStream( fullFileName ))){
                for(int i=0; i<size; i++ ){
                    outFile.write(inputStream.read());
                }
            }
            return fullFileName;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void acceptClientFileMessage(DataInputStream inputStream, Path clientPath){
        try {
            short lenFileName = inputStream.readShort();
            byte[] bytesFilename = new byte[lenFileName];
            inputStream.read(bytesFilename);
            fileName = new String(bytesFilename);
            size = inputStream.readLong();

            try(OutputStream outFile = new BufferedOutputStream(new FileOutputStream( clientPath.toString() ))){
                for(int i=0; i<size; i++ ){
                    outFile.write(inputStream.read());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getFileName() {
        return fileName;
    }

    public long getSize() {
        return size;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setSize(long size) {
        this.size = size;
    }
}

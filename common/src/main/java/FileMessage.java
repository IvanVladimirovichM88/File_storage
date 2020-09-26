
import java.io.IOException;

import java.io.Serializable;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;


public class FileMessage implements Serializable {
    private String fileName;
    private int size;
    private byte[] bytes;

    public FileMessage(Path path) {
        try {
            this.fileName = path.getFileName().toString();
            this.size = (int)Files.size(path);
            this.bytes = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public FileMessage(ProtocolSendFile sendFile){

        char[] name = new char[ sendFile.getByteFileName().length];
        int i = 0;
        for (byte b : sendFile.getByteFileName()){
            name[i] = (char)b;
            i++;
        }

        this.fileName = String.valueOf(name);
        this.size = convertByteArrayToInt( sendFile.getByteSizeFile() );
        this.bytes = sendFile.getByteContent();
    }

    public void printContent(){
        for(byte b: bytes){
            System.out.print((char)b);
        }
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] toByteArray(){
        byte[] byteCommand = {70, 73, 76, 69};  // command "FILE" - send file
        byte[] byteFileName = new byte[128];
        byte[] byteSizeFile;

        //convert fileName to byteArray
        int countForConvert = 0;
        for(char c: fileName.toCharArray()){
            byteFileName[countForConvert] = (byte) c;
            countForConvert++;
        }

        //convert size to byteArray
        byteSizeFile = this.convertIntToByteArray(size);

        byte[] sendBuffer = new byte[
                        byteCommand.length+
                        byteFileName.length+
                        byteSizeFile.length+
                        size];


        //fill byte array for send
        int countForSendBuffer=0;

        for(int i=0; i<byteCommand.length; i++, countForSendBuffer++){
            sendBuffer[countForSendBuffer] = byteCommand[i];
        }

        for(int i=0; i<byteFileName.length; i++, countForSendBuffer++){
            sendBuffer[countForSendBuffer] = byteFileName[i];
        }

        for(int i=0; i<byteSizeFile.length; i++, countForSendBuffer++){
            sendBuffer[countForSendBuffer] = byteSizeFile[i];
        }

        for(int i=0; i<size; i++,countForSendBuffer++){
            sendBuffer[countForSendBuffer] = bytes[i];
        }

        return sendBuffer;

    }

    public int getSize() {
        return size;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    private byte[] convertIntToByteArray(int value){
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        byteBuffer.putInt(value);
        byte[] rez = byteBuffer.array();
        return rez;
    }

    private int convertByteArrayToInt(byte[] buff){
        ByteBuffer byteBuffer = ByteBuffer.wrap(buff);
        return byteBuffer.getInt();
    }
}

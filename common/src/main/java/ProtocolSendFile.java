import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class ProtocolSendFile {
    byte[] byteCommand = {70, 73, 76, 69};     // command - 4 byte
    byte[] byteFileName = new byte[128];    // name file - 128 byte
    byte[] byteSizeFile = new byte[4];    // len file - 4 byte
    byte[] byteContent;     // max len - 2,147,483,647 byte

    public ProtocolSendFile(){}


    public ProtocolSendFile(FileMessage fileMessage){

        char[] tmp = fileMessage.getFileName().toCharArray();
        System.arraycopy( tmp, 0, byteFileName,0, tmp.length);

        byteSizeFile = Arrays.copyOf( convertIntToByteArray( fileMessage.getSize() ), 4 );

        byteContent = fileMessage.getBytes();

    }

    public byte[] getFullBuffer(){

        byte[] sendBuffer = new byte[
                        byteCommand.length+
                        byteFileName.length+
                        byteSizeFile.length+
                        byteContent.length];
        System.arraycopy(byteCommand,0, sendBuffer,0,byteCommand.length);
        System.arraycopy(byteFileName,0, sendBuffer, byteCommand.length, byteFileName.length);
        System.arraycopy(byteSizeFile,0, sendBuffer, byteCommand.length + byteFileName.length, byteSizeFile.length);
        System.arraycopy(byteContent,0, sendBuffer, byteCommand.length + byteFileName.length + byteSizeFile.length,  byteContent.length);

        return sendBuffer;
    }

    public void init(BufferedInputStream in){
        try {
            in.read(byteCommand,0,4);
            in.read(byteFileName,0,128);
            in.read(byteSizeFile,0,4);
            int sizeFile = convertByteArrayToInt(byteSizeFile);
            byteContent = new byte[sizeFile];
            in.read(byteContent,0,sizeFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] getByteCommand() {
        return byteCommand;
    }

    public byte[] getByteFileName() {
        return byteFileName;
    }

    public byte[] getByteSizeFile() {
        return byteSizeFile;
    }

    public byte[] getByteContent() {
        return byteContent;
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

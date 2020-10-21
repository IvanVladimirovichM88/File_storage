import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

public class CommandMessage {

    private static String ENCODING = "windows-1251";

    public static void sendAuthorized(String userName, String userPassword, DataOutputStream out){
        try {
            //send command byte
            out.write(41);
            //send len user name
            out.writeShort((short)userName.length());
            //send user name
            out.write(userName.getBytes());
            //send len password
            out.writeShort((short)userPassword.length());
            //send password
            out.write(userPassword.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String[] acceptAuthorized(DataInputStream in){
        String[] ret = new String[2];
        try {
            short lenUserName = in.readShort();
            byte[] byteUserName = new byte[lenUserName];
            in.read(byteUserName);
            ret[0] = new String(byteUserName);

            short lenUserPassword = in.readShort();
            byte[] byteUserPassword = new byte[lenUserPassword];
            in.read(byteUserPassword);
            ret[1] = new String(byteUserPassword);

        }catch (IOException e){
            e.printStackTrace();
        }
        return ret;
    }

    public static void sendAllFiles(List<String> fileNames, DataOutputStream out) throws IOException {

        out.writeInt(fileNames.size());

        for (String s : fileNames) {
            out.writeInt(s.length());
            out.write(s.getBytes(ENCODING));
        }
    }

    public static String[] acceptAllFiles(DataInputStream in) throws IOException {

        int size = in.readInt();
        int lenFileName;
        if ( size != 0) {
            String[] allFileNames = new String[size];
            for (int i =0; i< size; i++){
                lenFileName = in.readInt();
                byte[] byteFileName = new byte [lenFileName];
                in.read(byteFileName);
                allFileNames[i] = new String(byteFileName,ENCODING);
            }
            return allFileNames;
        }
        return null;
    }

    public static void requestAllFiles(DataOutputStream out){

        try {
            out.write(83);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void requestFileDelete(DataOutputStream out, String fileName){
        try {
            out.write(68);
            out.writeInt(fileName.length());
            out.write(fileName.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String acceptDeleteFile(DataInputStream in){
        int lenFileName = 0;
        String fileName = null;
        try {
            lenFileName = in.readShort();
            byte[] byteFileName = new byte[lenFileName];
            in.read(byteFileName);
            fileName = new String(byteFileName,ENCODING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }

    public static void sendDeleteFile(DataOutputStream out, String fileName){
        try {

            out.writeByte(68);
            out.writeShort((short) fileName.length());
            out.write(fileName.getBytes(ENCODING));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void requestDownloadFile(DataOutputStream out,  String fileName){
        try {
            //send command to download file "87"
            out.writeByte(87);
            out.writeShort((short) fileName.length());
            out.write(fileName.getBytes(ENCODING));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String acceptDownloadFile(DataInputStream in){
        int lenFileName = 0;
        String fileName = null;
        try {
            lenFileName = in.readShort();
            byte[] byteFileName = new byte[lenFileName];
            in.read(byteFileName);
            fileName = new String(byteFileName,ENCODING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }


    public static void sendDisconnect(DataOutputStream out){
        try {
            out.write(69);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

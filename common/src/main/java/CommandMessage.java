import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class CommandMessage {

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
}

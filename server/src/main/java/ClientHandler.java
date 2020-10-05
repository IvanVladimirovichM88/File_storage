import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ClientHandler {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private Server server;
    private int userId;

    public ClientHandler(Server server, Socket socket) {
        try {
            this.socket = socket;
            this.server = server;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            userId = -1;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            byte command = in.readByte();
                            if(command == 41){  //user name and password come
                                String[] NameWithPassword = CommandMessage.acceptAuthorized(in);
                                userId = AuthService.getUserIdWithPassAuthorized(
                                        NameWithPassword[0],
                                        NameWithPassword[1]
                                );
                                if (userId > 0){
                                    out.writeInt(userId);
                                    server.addClient( ClientHandler.this );

                                    //проверяем существует ли для данного клиента каталог
                                    if ( !(Files.exists(Paths.get("server/"+userId))) ){
                                        // создаем отдельную директорию для каждого клиета
                                        Files.createDirectory(Paths.get("server/"+userId));
                                    }
                                    break;
                                }else {
                                    out.writeInt(-1);
                                }
                            }
                        }
                        // come here when authorize is OK
                        while (true) {
                            byte command = in.readByte();
                            if (command == 70){ // command for send file
                                FileMessage fileMessage= new FileMessage();
                                String fileName = fileMessage.acceptFileMessage(in,userId);

                                if (fileName != null) {
                                    AuthService.putFileNameInTable(fileName, userId);
                                }

                            }else if( command == 83){ // command for show all files

                                CommandMessage.sendAllFiles( AuthService.getAllFileForUser(userId),out );
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        server.removeUser(ClientHandler.this);
                    }

                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

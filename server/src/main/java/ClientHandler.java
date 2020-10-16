import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
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
                        while (userId == -1) {
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
                                    if ( !(Files.exists(Paths.get("server/" + userId))) ){
                                        // создаем отдельную директорию для каждого клиета
                                        Files.createDirectory(Paths.get("server/" + userId));
                                    }
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

                            }else if( command == 68){ //command delete file

                                String fileName = CommandMessage.acceptDeleteFile(in);
                                String fileNameWithCatalog = "server/" + userId + "/" + fileName;
                                if (AuthService.deleteFileInTable(fileNameWithCatalog,userId) > 0) {
                                    File file = new File(fileNameWithCatalog);
                                    file.delete();
                                    System.out.println("--> file - "+fileNameWithCatalog+" was deleted");
                                }else{
                                    System.out.println( "-->  file - " + fileNameWithCatalog +" not fount");
                                }

                            }else if(command == 69){  // command download file

                                String fileName = CommandMessage.acceptDownloadFile(in);
                                String fileNameWithCatalog = "server/" + userId + "/" + fileName;
                                FileMessage fileMessage = new FileMessage(Paths.get(fileNameWithCatalog));
                                fileMessage.sendFileMessage(socket);

                            }else if (command == 69) { // command exit
                                break;
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

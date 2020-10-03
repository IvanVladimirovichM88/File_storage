import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {
    private Vector<ClientHandler> clients;

    final int PORT = 8189;

    public Server(){
        ServerSocket server = null;
        Socket socket = null;

        try {
            server = new ServerSocket(PORT);
            System.out.println("-->  Server Started");
            AuthService.connect();
            System.out.println("--> Server connect DB");
            clients = new Vector<>();

            while (true){
                socket = server.accept();
                System.out.println("-->  Client connection");
                new ClientHandler(this, socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        AuthService.disconnect();
    }

    }

    public void addClient(ClientHandler clientHandler) {
        clients.add(clientHandler);
    }

    public void removeUser(ClientHandler clientHandler) {
        System.out.println("->  User remove "+clientHandler.toString());
        clients.remove(clientHandler);
    }
}

import java.awt.*;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.Scanner;

import  javax.swing.*;

public class MainClient {
    public static void main(String[] args) {

        Client client = new Client();
        new MyWindow(client);

//        Client client = new Client();
//        //подключение и авторизация клиента
//        client.connect();
//
//        Scanner scanner = new Scanner(System.in);
//        String userString = new String();
//        String[] userCommand;
//        // command cycle
//        do {
//            userString = scanner.nextLine();
//            userCommand = userString.split(" ", 2);
//
//            System.out.println(userCommand[0]);
//
//            switch (userCommand[0]){
//                case "show":
//                    client.showAllFiles();
//                    break;
//                case "exit":
//                    client.disconnect();
//                    break;
//                default:
//                    System.out.println( "this command not found");
//            }
//
//        }while( ! userCommand[0].equals("exit") );

    }



}

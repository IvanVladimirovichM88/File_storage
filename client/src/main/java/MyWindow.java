import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.file.Paths;


public class MyWindow extends JFrame {

    private boolean authorized = false;
    private int idClient = -1;
    private DefaultListModel model;
    private JList<String> fileStorageList;
    private JScrollPane listScrollPane;

    public MyWindow(Client client){
        setTitle("My storage");


        JPanel jPanelAuthorized = new JPanel(new GridLayout(0,2,5,0));
        JPanel jPanel2 = new JPanel(new GridLayout(2,0));
        JTextField tfLogin = new JTextField();
        JPasswordField pfPassword = new JPasswordField();

        tfLogin.setText("Fedia");
        pfPassword.setText("fedia");

        jPanel2.add(tfLogin);
        jPanel2.add(pfPassword);

        Button btnConnect = new Button("Connect");
        jPanelAuthorized.add(jPanel2);
        jPanelAuthorized.add(btnConnect);



        add(jPanelAuthorized,BorderLayout.NORTH);

        model = new DefaultListModel<>();

        model.addElement("Введите login и пароль");

        fileStorageList = new JList( model);
        fileStorageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listScrollPane = new JScrollPane(fileStorageList);

        add(listScrollPane,BorderLayout.CENTER);


        JPanel jPanelAction = new JPanel(new GridLayout(0,3,5,0));
        JButton btnSaveInStorage  = new JButton("Save in Storage");
        JButton btnDownload = new JButton("Download");
        JButton btnDelete = new JButton("Delete");

        jPanelAction.add(btnSaveInStorage);
        jPanelAction.add(btnDownload);
        jPanelAction.add(btnDelete);

        add(jPanelAction,BorderLayout.SOUTH);

        jPanelAction.setVisible(false);

        btnConnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = tfLogin.getText();
                String password = new String(pfPassword.getPassword());
                idClient = client.connect(name, password);

                if (idClient > -1){
                    jPanelAuthorized.setVisible(false);
                    jPanelAction.setVisible(true);
                    String[] allFilesName = client.getAllFilesInStorage();
                    rewriteList(model,allFilesName);
                }else{
                    rewriteList(model,new String[]{"Неверный логин или пароль", "повторите попытку"});
                }
            }
        });

        btnSaveInStorage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

                int ret = fileChooser.showOpenDialog(MyWindow.this);
                if(ret == JFileChooser.APPROVE_OPTION){
                    File file = fileChooser.getSelectedFile();
                    client.sendFile(file.toPath());
                    System.out.println(file.getAbsolutePath());
                    // обновляем список файлов
                    String[] allFilesName = client.getAllFilesInStorage();
                    rewriteList(model,allFilesName);
                }
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String selectFileName = fileStorageList.getSelectedValue();
                if (selectFileName!= null){
                    client.deleteFile(Paths.get(selectFileName));
                }

                // обновляем список файлов
                String[] allFilesName = client.getAllFilesInStorage();
                rewriteList(model,allFilesName);
            }
        });

        btnDownload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String selectFileName = fileStorageList.getSelectedValue();
                if( selectFileName != null ){
                    //create new fileChooser
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

                    int ret = fileChooser.showSaveDialog(MyWindow.this);
                    if(ret == JFileChooser.APPROVE_OPTION){
                        //select file to save
                        File file = fileChooser.getSelectedFile();
                        //download file from storage
                        client.downloadFile(Paths.get(selectFileName) , file.toPath());
                    }
                }
            }
        });



        setBounds(800,300,400,400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                client.disconnect();
            }
        });

        setVisible(true);

    }



    private void rewriteList(DefaultListModel<String> model, String[] strings){
        model.clear();
        for (String s: strings) {
            model.addElement(s);
        }
    }



}

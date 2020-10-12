import javax.swing.*;
import java.awt.*;

public class MyWindow extends JFrame {

    private boolean authorized = false;

    public MyWindow(){
        setTitle("My storage");

        JPanel jPanel1 = new JPanel(new GridLayout(0,2,5,0));
        JPanel jPanel2 = new JPanel(new GridLayout(2,0));
        JTextField tfLogin = new JTextField();
        JTextField tfPassword = new JTextField();

        jPanel2.add(tfLogin);
        jPanel2.add(tfPassword);

        Button btnConnect = new Button("Connect");
        jPanel1.add(jPanel2);
        jPanel1.add(btnConnect);

        jPanel2.setVisible(false);

        add(jPanel1,BorderLayout.NORTH);

        setBounds(800,300,400,400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setVisible(true);



    }

}

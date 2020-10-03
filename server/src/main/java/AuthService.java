import java.sql.*;

public class AuthService {
    private static Connection connection;
    private static Statement statement;

    public static void connect(){
        try {

            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            String userName = "root";
            String password = "root";
            String url = "jdbc:MySQL://localhost:3306/file_storage_db?serverTimezone=Europe/Moscow";
            connection = DriverManager.getConnection(url,userName,password);
            statement = connection.createStatement();
            System.out.println("Database Connection ");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static int getUserIdWithPassAuthorized(String name, String pass){
        String sql = String.format("SELECT user_id " +
                    "FROM user_tbl " +
                    "WHERE name_fld = '%s' AND password_fld = '%s';", name, pass );
        try {
            ResultSet rs = statement.executeQuery(sql);
            if (rs.next()){
                return rs.getInt("user_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void disconnect(){
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
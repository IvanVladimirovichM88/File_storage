import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public static void putFileNameInTable(String fileName, int userId){
        String sql = String.format("INSERT INTO `file_storage_db`.`file_name_tbl` (`file_name_fld`, `user_id`) " +
                "VALUES ('%s', '%d');", fileName, userId);

        try {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getAllFileForUser(int userId){
        ArrayList<String> fileNames = new ArrayList<>();

        String sql = String.format("SELECT file_name_fld " +
                " FROM file_name_tbl " +
                " WHERE user_id = '%d';", userId);

        try {
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next()){
                fileNames.add(rs.getString("file_name_fld"));
            }

            return fileNames;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static int  deleteFileInTable(String fileName, int userId){

        String sql = String.format("DELETE FROM file_name_tbl" +
                " WHERE user_id = '%d' AND file_name_fld = '%s';",
                userId, fileName
        );

        try {
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void disconnect(){
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

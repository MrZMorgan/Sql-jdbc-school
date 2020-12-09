package ua.com.foxminded.dao;

import ua.com.foxminded.interfaces.DAO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class GroupsDAO implements DAO {

    private static final String user = "postgres";
    private static final String password = "1234";
    private static final String url = "jdbc:postgresql://localhost:5432/school";

    public void create(String groupName) {
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
            try {
                statement.executeQuery("INSERT INTO groups (group_name) VALUES ('" + groupName +"');");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

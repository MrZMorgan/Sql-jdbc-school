package ua.com.foxminded.dao;

import ua.com.foxminded.interfaces.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Random;

public class StudentsDAO implements DAO {

    private static final String user = "postgres";
    private static final String password = "1234";
    private static final String url = "jdbc:postgresql://localhost:5432/school";

    public void create(String firstName, String lastName) {
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
            try {
                statement.executeQuery("INSERT INTO students (group_id, first_name, last_name) " +
                        "VALUES (random(10), first_name, last_name);");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

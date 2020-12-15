package ua.com.foxminded.dao;

import ua.com.foxminded.interfaces.StudentsDAOInterface;

import java.io.FileInputStream;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class StudentsDAO implements StudentsDAOInterface {

    private static final String user = "postgres";
    private static final String password = "1234";
    private static final String url = "jdbc:postgresql://localhost:5432/school";

    @Override
    public <T> void create(T fullName) {
        Connection connection = null;
        Statement statement = null;

        try (FileInputStream stream = new FileInputStream("resources/connection.properties")) {
            Class.forName("org.postgresql.Driver");
            Properties properties = new Properties();
            properties.load(stream);
            connection = DriverManager.getConnection(properties.getProperty("url"),
                    properties.getProperty("user"), properties.getProperty("password"));
            String[] data = fullName.toString().split(" ");
            statement = connection.createStatement();
            try {
                statement.executeQuery("INSERT INTO students (first_name, last_name) " +
                                       "VALUES ('" + data[0] + "', '" + data[1] + "');");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public void deleteById(int studentId) {
        Connection connection = null;
        Statement statement = null;
        try (FileInputStream stream = new FileInputStream("resources/connection.properties")) {
            Class.forName("org.postgresql.Driver");
            Properties properties = new Properties();
            properties.load(stream);
            connection = DriverManager.getConnection(properties.getProperty("url"),
                    properties.getProperty("user"), properties.getProperty("password"));
            statement = connection.createStatement();
            try {
                statement.executeQuery("DELETE FROM students " +
                                       "WHERE id = "+ studentId +";");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public void assignStudentToGroup(int groupId, int studentId) {
        Connection connection = null;
        Statement statement = null;
        try (FileInputStream stream = new FileInputStream("resources/connection.properties")) {
            Class.forName("org.postgresql.Driver");
            Properties properties = new Properties();
            properties.load(stream);
            connection = DriverManager.getConnection(properties.getProperty("url"),
                    properties.getProperty("user"), properties.getProperty("password"));
            statement = connection.createStatement();
            try {
                statement.executeQuery("UPDATE students " +
                                       "SET group_id = " + groupId + " " +
                                       "WHERE id = " + studentId + ";");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}

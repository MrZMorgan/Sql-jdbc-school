package ua.com.foxminded.dao;

import ua.com.foxminded.connection.ConnectionFactory;
import ua.com.foxminded.exceptions.DAOException;
import ua.com.foxminded.interfaces.StudentsDAOInterface;
import java.io.FileInputStream;
import java.sql.*;

public class StudentsDAO implements StudentsDAOInterface {

    public final static String RESOURCE_FILE_PATH = "resources/connection.properties";
    private static final String FAILED_CONNECTION_MESSAGE = "Database connection failed";

    @Override
    public <T> void create(T fullName) throws DAOException {
        Connection connection = null;
        Statement statement = null;

        try (FileInputStream stream = new FileInputStream(RESOURCE_FILE_PATH)) {
            connection = new ConnectionFactory().connect(stream);
            statement = connection.createStatement();
            String[] data = fullName.toString().split(" ");
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
                throw new DAOException(FAILED_CONNECTION_MESSAGE);
            }
        }
    }

    public void deleteById(int studentId) throws DAOException {
        Connection connection = null;
        Statement statement = null;
        try (FileInputStream stream = new FileInputStream(RESOURCE_FILE_PATH)) {
            connection = new ConnectionFactory().connect(stream);
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
                throw new DAOException(FAILED_CONNECTION_MESSAGE);
            }
        }
    }

    public void assignStudentToGroup(int groupId, int studentId) throws DAOException {
        Connection connection = null;
        Statement statement = null;
        try (FileInputStream stream = new FileInputStream(RESOURCE_FILE_PATH)) {
            connection = new ConnectionFactory().connect(stream);
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
                throw new DAOException(FAILED_CONNECTION_MESSAGE);
            }
        }
    }
}

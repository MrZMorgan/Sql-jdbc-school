package ua.com.foxminded.dao;

import ua.com.foxminded.connection.ConnectionFactory;
import ua.com.foxminded.exceptions.DAOException;
import ua.com.foxminded.interfaces.StudentsDAOInterface;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class StudentsDAO implements StudentsDAOInterface {

    public static final String SQL_RESOURCES = "resources/sql.properties";
    private static final String FAILED_CONNECTION_MESSAGE = "Database connection failed";

    @Override
    public <T> void create(T fullName) throws DAOException {
        Connection connection = null;
        Statement statement = null;
        Properties properties = new Properties();
        try {
            FileInputStream stream = new FileInputStream(SQL_RESOURCES);
            properties.load(stream);
            stream.close();

            connection = new ConnectionFactory().connect();
            statement = connection.createStatement();
            String[] data = fullName.toString().split(" ");
            statement.executeQuery(String.format(properties.getProperty("CREATE_STUDENT"), data[0], data[1]));
        } catch (SQLException | IOException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
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
        Properties properties = new Properties();
        try {
            FileInputStream stream = new FileInputStream(SQL_RESOURCES);
            properties.load(stream);
            stream.close();

            connection = new ConnectionFactory().connect();
            statement = connection.createStatement();
            statement.executeQuery(String.format(properties.getProperty("DELETE_STUDENT_BY_ID"), studentId));
        } catch (SQLException | IOException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
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
        Properties properties = new Properties();
        try {
            FileInputStream stream = new FileInputStream(SQL_RESOURCES);
            properties.load(stream);
            stream.close();

            connection = new ConnectionFactory().connect();
            statement = connection.createStatement();
            statement.executeQuery(
                    String.format(properties.getProperty("ASSIGN_STUDENT_TO_GROUP"), groupId, studentId));
        } catch (SQLException | IOException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throw new DAOException(FAILED_CONNECTION_MESSAGE);
            }
        }
    }
}

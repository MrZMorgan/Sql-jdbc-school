package ua.com.foxminded.dao;

import ua.com.foxminded.connection.ConnectionFactory;
import ua.com.foxminded.exceptions.DAOException;
import ua.com.foxminded.interfaces.StudentsDAOInterface;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

public class StudentsDAO implements StudentsDAOInterface {

    String resourceFilePath;

    public StudentsDAO(String resourceFilePath) {
        this.resourceFilePath = resourceFilePath;
    }

    public static final String SQL_RESOURCES = "resources/sql.properties";
    public final static String SPACE = " ";
    private final static Logger logger = Logger.getLogger(StudentsDAO.class.getName());

    @Override
    public void create(String fullName) throws DAOException {
        Connection connection = null;
        Statement statement = null;
        Properties properties = new Properties();
        ConnectionFactory factory = new ConnectionFactory(resourceFilePath);
        try {
            FileInputStream stream = new FileInputStream(SQL_RESOURCES);
            properties.load(stream);
            stream.close();

            connection = factory.connect();
            statement = connection.createStatement();
            String[] data = fullName.split(SPACE);
            statement.execute(String.format(properties.getProperty("create.student"), data[0], data[1]));
        } catch (SQLException | IOException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
            logger.info(throwables.getMessage());
        } finally {
            factory.close(connection);
        }
    }

    public void deleteById(int studentId) throws DAOException {
        Connection connection = null;
        Statement statement = null;
        Properties properties = new Properties();
        ConnectionFactory factory = new ConnectionFactory(resourceFilePath);
        try {
            FileInputStream stream = new FileInputStream(SQL_RESOURCES);
            properties.load(stream);
            stream.close();

            connection = factory.connect();
            statement = connection.createStatement();
            statement.execute(String.format(properties.getProperty("delete.student.by.id"), studentId));
        } catch (SQLException | IOException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
            logger.info(throwables.getMessage());
        } finally {
            factory.close(connection);
        }
    }

    public void assignStudentToGroup(int groupId, int studentId) throws DAOException {
        Connection connection = null;
        Statement statement = null;
        Properties properties = new Properties();
        ConnectionFactory factory = new ConnectionFactory(resourceFilePath);
        try {
            FileInputStream stream = new FileInputStream(SQL_RESOURCES);
            properties.load(stream);
            stream.close();

            connection = factory.connect();
            statement = connection.createStatement();
            statement.execute(
                    String.format(properties.getProperty("assign.student.to.group"), groupId, studentId));
        } catch (SQLException | IOException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
            logger.info(throwables.getMessage());
        } finally {
            factory.close(connection);
        }
    }
}

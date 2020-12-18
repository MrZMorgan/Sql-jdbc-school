package ua.com.foxminded.dao;

import ua.com.foxminded.connection.ConnectionFactory;
import ua.com.foxminded.exceptions.DAOException;
import ua.com.foxminded.interfaces.StudentsDAOInterface;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StudentsDAO implements StudentsDAOInterface {

    public static final String SQL_RESOURCES = "resources/sql.properties";
    private final static Logger logger = Logger.getLogger(StudentsDAO.class.getName());
    private static final String logFilePath = "/home/egor/Документы/repositorys/sql--jdbc-school/Sql-Jdbc-School/logs/students/students_log.log";

    @Override
    public void create(String fullName) throws DAOException, IOException {
        Connection connection = null;
        Statement statement = null;
        Properties properties = new Properties();
        ConnectionFactory factory = new ConnectionFactory();
        try {
            FileInputStream stream = new FileInputStream(SQL_RESOURCES);
            properties.load(stream);
            stream.close();

            connection = factory.connect();
            statement = connection.createStatement();
            String[] data = fullName.split(" ");
            statement.executeQuery(String.format(properties.getProperty("create.students"), data[0], data[1]));
        } catch (SQLException | IOException | ClassNotFoundException throwables) {
            factory.log(logFilePath, logger, throwables);
        } finally {
            factory.close(connection);
        }
    }

    public void deleteById(int studentId) throws DAOException, IOException {
        Connection connection = null;
        Statement statement = null;
        Properties properties = new Properties();
        ConnectionFactory factory = new ConnectionFactory();
        try {
            FileInputStream stream = new FileInputStream(SQL_RESOURCES);
            properties.load(stream);
            stream.close();

            connection = factory.connect();
            statement = connection.createStatement();
            statement.executeQuery(String.format(properties.getProperty("delete.student.by.id"), studentId));
        } catch (SQLException | IOException | ClassNotFoundException throwables) {
            factory.log(logFilePath, logger, throwables);
        } finally {
            factory.close(connection);
        }
    }

    public void assignStudentToGroup(int groupId, int studentId) throws DAOException, IOException {
        Connection connection = null;
        Statement statement = null;
        Properties properties = new Properties();
        ConnectionFactory factory = new ConnectionFactory();
        try {
            FileInputStream stream = new FileInputStream(SQL_RESOURCES);
            properties.load(stream);
            stream.close();

            connection = factory.connect();
            statement = connection.createStatement();
            statement.executeQuery(
                    String.format(properties.getProperty("assign.student.to.group"), groupId, studentId));
        } catch (SQLException | IOException | ClassNotFoundException throwables) {
            factory.log(logFilePath, logger, throwables);
        } finally {
            factory.close(connection);
        }
    }
}

package ua.com.foxminded.dao;

import ua.com.foxminded.connection.ConnectionFactory;
import ua.com.foxminded.dao.data.Student;
import ua.com.foxminded.exceptions.DAOException;
import ua.com.foxminded.interfaces.StudentsDAO;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public class StudentsJdbcDao implements StudentsDAO {

    public final static String SQL_RESOURCES = "resources/sql.properties";
    public final static String SPACE = " ";

    ConnectionFactory factory;

    public StudentsJdbcDao(ConnectionFactory factory) {
        this.factory = factory;
    }

    private final static Logger logger = Logger.getLogger(StudentsJdbcDao.class.getName());

    @Override
    public void create(String fullName) throws DAOException {
        Connection connection = null;
        Statement statement = null;
        Properties properties = new Properties();
        try {
            FileInputStream stream = new FileInputStream(SQL_RESOURCES);
            properties.load(stream);
            stream.close();

            connection = factory.connect();
            statement = connection.createStatement();
            String[] data = fullName.split(SPACE);
            statement.execute(String.format(properties.getProperty("create.student"), data[0], data[1]));
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
            logger.info(throwables.getMessage());
        } finally {
            factory.close(connection);
        }
    }



    @Override
    public void deleteById(int studentId) throws DAOException {
        Connection connection = null;
        Statement statement = null;
        Properties properties = new Properties();
        try {
            FileInputStream stream = new FileInputStream(SQL_RESOURCES);
            properties.load(stream);
            stream.close();

            connection = factory.connect();
            statement = connection.createStatement();
            statement.execute(String.format(properties.getProperty("delete.student.by.id"), studentId));
        } catch (SQLException | IOException throwables) {
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
        try {
            FileInputStream stream = new FileInputStream(SQL_RESOURCES);
            properties.load(stream);
            stream.close();

            connection = factory.connect();
            statement = connection.createStatement();
            statement.execute(
                    String.format(properties.getProperty("assign.student.to.group"), groupId, studentId));
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
            logger.info(throwables.getMessage());
        } finally {
            factory.close(connection);
        }
    }

    @Override
    public List<Student> readAllData() throws DAOException {
        Connection connection = null;
        List<Student> courseList = new LinkedList<>();
        Properties properties = new Properties();
        try {
            FileInputStream stream = new FileInputStream(SQL_RESOURCES);
            properties.load(stream);
            stream.close();

            connection = factory.connect();
            PreparedStatement statement = connection.prepareStatement(properties.getProperty("get.students.list"));
            final ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Student student = new Student(
                        resultSet.getInt("id"),
                        resultSet.getInt("group_id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name")
                );
                courseList.add(student);
            }
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
            logger.info(throwables.getMessage());
        } finally {
            factory.close(connection);
        }
        return courseList;
    }

    @Override
    public void update(int studentId, String fullNameRawData) throws DAOException {
        Connection connection = null;
        Statement statement = null;
        Properties properties = new Properties();
        try {
            FileInputStream stream = new FileInputStream(SQL_RESOURCES);
            properties.load(stream);
            stream.close();

            connection = factory.connect();
            statement = connection.createStatement();

            String[] fullName = fullNameRawData.split(SPACE);
            statement.execute(
                    String.format(properties.getProperty("update.student.name"), fullName[0], fullName[1], studentId));
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
            logger.info(throwables.getMessage());
        } finally {
            factory.close(connection);
        }
    }
}

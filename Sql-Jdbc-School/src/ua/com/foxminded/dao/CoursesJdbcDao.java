package ua.com.foxminded.dao;

import ua.com.foxminded.connection.ConnectionFactory;
import ua.com.foxminded.dao.data.Course;
import ua.com.foxminded.exceptions.DAOException;
import ua.com.foxminded.interfaces.CourseDAO;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.logging.Logger;

public class CoursesJdbcDao implements CourseDAO {

    public final static String SQL_RESOURCES = "resources/sql.properties";
    private final static Logger logger = Logger.getLogger(CoursesJdbcDao.class.getName());

    ConnectionFactory factory;

    public CoursesJdbcDao(ConnectionFactory factory) {
        this.factory = factory;
    }

    @Override
    public void create(String courseName) throws DAOException {
        Connection connection = null;
        Statement statement = null;
        Properties properties = new Properties();
        try {
            FileInputStream stream = new FileInputStream(SQL_RESOURCES);
            properties.load(stream);
            stream.close();
            connection = factory.connect();
            statement = connection.createStatement();
            statement.execute(String.format(properties.getProperty("create.course"), courseName));
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
            logger.info(throwables.getMessage());
        } finally {
            factory.close(connection);
        }
    }

    @Override
    public List<Course> readAllData() throws DAOException {
        Connection connection = null;
        List<Course> courseList = new LinkedList<>();
        Properties properties = new Properties();
        try {
            FileInputStream stream = new FileInputStream(SQL_RESOURCES);
            properties.load(stream);
            stream.close();

            connection = factory.connect();
            PreparedStatement statement = connection.prepareStatement(properties.getProperty("get.courses.list"));
            final ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                courseList.add(new Course(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                ));
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
    public void deleteById(int courseId) throws DAOException {
        Connection connection = null;
        Statement statement = null;
        Properties properties = new Properties();
        try {
            FileInputStream stream = new FileInputStream(SQL_RESOURCES);
            properties.load(stream);
            stream.close();

            connection = factory.connect();
            statement = connection.createStatement();
            statement.execute(String.format(properties.getProperty("delete.course.by.id"), courseId));
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
            logger.info(throwables.getMessage());
        } finally {
            factory.close(connection);
        }
    }

    @Override
    public void update(int courseId, String courseName) throws DAOException {
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
                    String.format(properties.getProperty("update.course.name"), courseName, courseId));
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
            logger.info(throwables.getMessage());
        } finally {
            factory.close(connection);
        }
    }
}

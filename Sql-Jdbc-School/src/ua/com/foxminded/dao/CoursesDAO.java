package ua.com.foxminded.dao;

import ua.com.foxminded.connection.ConnectionFactory;
import ua.com.foxminded.exceptions.DAOException;
import ua.com.foxminded.interfaces.CourseDAOInterface;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

public class CoursesDAO implements CourseDAOInterface {

    public static final String FAILED_CONNECTION_MESSAGE = "Database connection failed";
    public static final String SQL_RESOURCES = "resources/sql.properties";

    @Override
    public <T> void create(T courseName) throws DAOException {
        Connection connection = null;
        Statement statement = null;
        Properties properties = new Properties();
        try {
            FileInputStream stream = new FileInputStream(SQL_RESOURCES);
            properties.load(stream);
            stream.close();

            connection = new ConnectionFactory().connect();
            statement = connection.createStatement();
            statement.executeQuery(String.format(properties.getProperty("CREATE_COURSE"), courseName));
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

    public Map<Integer, String> getCoursesList() throws DAOException {
        Connection connection = null;
        Map<Integer, String> courseList = new LinkedHashMap<>();
        Properties properties = new Properties();
        try {
            FileInputStream stream = new FileInputStream(SQL_RESOURCES);
            properties.load(stream);
            stream.close();

            connection = new ConnectionFactory().connect();
            PreparedStatement statement = connection.prepareStatement(properties.getProperty("GET_COURSES_LIST"));
            final ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                courseList.put(resultSet.getInt("id"), resultSet.getString("name"));
            }
        } catch (SQLException | IOException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throw new DAOException(FAILED_CONNECTION_MESSAGE);
            }
        }
        return courseList;
    }
}

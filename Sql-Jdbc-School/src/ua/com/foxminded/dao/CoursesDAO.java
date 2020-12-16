package ua.com.foxminded.dao;

import ua.com.foxminded.connection.ConnectionFactory;
import ua.com.foxminded.exceptions.DAOException;
import ua.com.foxminded.interfaces.CourseDAOInterface;

import java.io.FileInputStream;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class CoursesDAO implements CourseDAOInterface {

    public final static String RESOURCE_FILE_PATH = "resources/connection.properties";
    private static final String FAILED_CONNECTION_MESSAGE = "Database connection failed";

    @Override
    public <String> void create(String courseName) throws DAOException {
        Connection connection = null;
        Statement statement = null;
        try (FileInputStream stream = new FileInputStream(RESOURCE_FILE_PATH)){
            connection = new ConnectionFactory().connect(stream);
            statement = connection.createStatement();
            try {
                statement.executeQuery("INSERT INTO courses (name) " +
                                       "VALUES ('" + courseName + "');");
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

    public Map<Integer, String> getCoursesList() throws DAOException {
        final String sql = "SELECT * FROM courses;";
        Connection connection = null;
        Map<Integer, String> courseList = new LinkedHashMap<>();
        try (FileInputStream stream = new FileInputStream(RESOURCE_FILE_PATH)) {
            connection = new ConnectionFactory().connect(stream);
            PreparedStatement statement = connection.prepareStatement(sql);
            final ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                courseList.put(resultSet.getInt("id"), resultSet.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException throwable) {
                throw new DAOException(FAILED_CONNECTION_MESSAGE);
            }
        }
        return courseList;
    }
}

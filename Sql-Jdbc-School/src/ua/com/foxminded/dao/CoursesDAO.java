package ua.com.foxminded.dao;

import ua.com.foxminded.interfaces.CourseDAOInterface;

import java.io.FileInputStream;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

public class CoursesDAO implements CourseDAOInterface {

    @Override
    public <String> void create(String courseName) {
        Connection connection = null;
        Statement statement = null;
        try (FileInputStream stream = new FileInputStream("resources/connection.properties")){
            Class.forName("org.postgresql.Driver");
            Properties properties = new Properties();
            properties.load(stream);
            connection = DriverManager.getConnection(properties.getProperty("url"),
                    properties.getProperty("user"), properties.getProperty("password"));
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
                throwables.printStackTrace();
            }
        }
    }

    public Map<Integer, String> getCoursesList() {
        final String sql = "SELECT * FROM courses;";
        Connection connection = null;
        Map<Integer, String> courseList = new LinkedHashMap<>();
        try (FileInputStream stream = new FileInputStream("resources/connection.properties")) {
            Class.forName("org.postgresql.Driver");
            Properties properties = new Properties();
            properties.load(stream);
            connection = DriverManager.getConnection(properties.getProperty("url"),
                    properties.getProperty("user"), properties.getProperty("password"));
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
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return courseList;
    }
}

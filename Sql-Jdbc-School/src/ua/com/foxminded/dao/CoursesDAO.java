package ua.com.foxminded.dao;

import ua.com.foxminded.interfaces.CourseDAOInterface;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class CoursesDAO implements CourseDAOInterface {

    private static final String user = "postgres";
    private static final String password = "1234";
    private static final String url = "jdbc:postgresql://localhost:5432/school";

    @Override
    public <String> void create(String courseName) {
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
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
        try  {
            connection = DriverManager.getConnection(url, user, password);
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

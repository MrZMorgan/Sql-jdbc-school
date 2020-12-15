package ua.com.foxminded.dao;

import ua.com.foxminded.interfaces.StudentsDAOInterface;

import java.io.FileInputStream;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class StudentsCoursesDAO implements StudentsDAOInterface {

    @Override
    public <T> void create(T rowData) {
        Connection connection = null;
        Statement statement = null;
        try (FileInputStream stream = new FileInputStream("resources/connection.properties")) {
            Class.forName("org.postgresql.Driver");
            Properties properties = new Properties();
            properties.load(stream);
            connection = DriverManager.getConnection(properties.getProperty("url"),
                    properties.getProperty("user"), properties.getProperty("password"));
            String[] data = rowData.toString().split(" ");
            statement = connection.createStatement();
            try {
                statement.executeQuery("INSERT INTO students_courses (student_id , course_id) " +
                                       "VALUES (" + Integer.parseInt(data[0]) + ", " + Integer.parseInt(data[1]) + ");");
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

    public void deleteById(int studentId) {
        Connection connection = null;
        Statement statement = null;
        try (FileInputStream stream = new FileInputStream("resources/connection.properties")) {
            Class.forName("org.postgresql.Driver");
            Properties properties = new Properties();
            properties.load(stream);
            connection = DriverManager.getConnection(properties.getProperty("url"),
                    properties.getProperty("user"), properties.getProperty("password"));
            statement = connection.createStatement();
            try {
                statement.executeQuery("DELETE FROM students_courses WHERE student_id = "+ studentId +";");
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

    public void deleteFromCourse(int studentId, int courseId) {
        Connection connection = null;
        Statement statement = null;
        try (FileInputStream stream = new FileInputStream("resources/connection.properties")) {
            Class.forName("org.postgresql.Driver");
            Properties properties = new Properties();
            properties.load(stream);
            connection = DriverManager.getConnection(properties.getProperty("url"),
                    properties.getProperty("user"), properties.getProperty("password"));
            statement = connection.createStatement();
            try {
                statement.executeQuery("DELETE FROM students_courses " +
                                       "WHERE student_id = " + studentId + " " +
                                       "AND course_id = " + courseId + "");
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

    public List<String[]> getStudentsRelatedToCourses(String courseName) {
        final String sql = "SELECT first_name, last_name FROM students\n" +
                "JOIN students_courses ON students.id = students_courses.student_id\n" +
                "JOIN courses ON students_courses.course_id = courses.id WHERE name = '" + courseName + "'";
        Connection connection = null;
        List<String[]> names = new LinkedList<>();
        try (FileInputStream stream = new FileInputStream("resources/connection.properties")) {
            Class.forName("org.postgresql.Driver");
            Properties properties = new Properties();
            properties.load(stream);
            connection = DriverManager.getConnection(properties.getProperty("url"),
                    properties.getProperty("user"), properties.getProperty("password"));
            PreparedStatement statement = connection.prepareStatement(sql);
            final ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String[] groupSize = new String[2];
                groupSize[0] = resultSet.getString("first_name");
                groupSize[1] = resultSet.getString("last_name");
                names.add(groupSize);
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
        return names;
    }
}

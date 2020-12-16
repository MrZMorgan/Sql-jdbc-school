package ua.com.foxminded.dao;

import ua.com.foxminded.connection.ConnectionFactory;
import ua.com.foxminded.exceptions.DAOException;
import ua.com.foxminded.interfaces.StudentsDAOInterface;
import java.io.FileInputStream;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;;

public class StudentsCoursesDAO implements StudentsDAOInterface {

    public final static String RESOURCE_FILE_PATH = "resources/connection.properties";
    private static final String FAILED_CONNECTION_MESSAGE = "Database connection failed";

    @Override
    public <T> void create(T rowData) throws DAOException {
        Connection connection = null;
        Statement statement = null;
        try (FileInputStream stream = new FileInputStream(RESOURCE_FILE_PATH)) {
            connection = new ConnectionFactory().connect(stream);
            statement = connection.createStatement();
            String[] data = rowData.toString().split(" ");
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
                throw new DAOException(FAILED_CONNECTION_MESSAGE);
            }
        }
    }

    public void deleteById(int studentId) throws DAOException {
        Connection connection = null;
        Statement statement = null;
        try (FileInputStream stream = new FileInputStream(RESOURCE_FILE_PATH)) {
            connection = new ConnectionFactory().connect(stream);
            statement = connection.createStatement();
            try {
                statement.executeQuery("DELETE FROM students_courses " +
                                       "WHERE student_id = "+ studentId +";");
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

    public void deleteFromCourse(int studentId, int courseId) throws DAOException {
        Connection connection = null;
        Statement statement = null;
        try (FileInputStream stream = new FileInputStream(RESOURCE_FILE_PATH)) {
            connection = new ConnectionFactory().connect(stream);
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
                throw new DAOException(FAILED_CONNECTION_MESSAGE);
            }
        }
    }

    public List<String[]> getStudentsRelatedToCourses(String courseName) throws DAOException {
        final String sql = "SELECT first_name, last_name FROM students\n" +
                           "JOIN students_courses ON students.id = students_courses.student_id\n" +
                           "JOIN courses ON students_courses.course_id = courses.id " +
                           "WHERE name = '" + courseName + "'";
        Connection connection = null;
        List<String[]> names = new LinkedList<>();
        try (FileInputStream stream = new FileInputStream(RESOURCE_FILE_PATH)) {
            connection = new ConnectionFactory().connect(stream);
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
                throw new DAOException(FAILED_CONNECTION_MESSAGE);
            }
        }
        return names;
    }
}

package ua.com.foxminded.dao;

import ua.com.foxminded.connection.ConnectionFactory;
import ua.com.foxminded.exceptions.DAOException;
import ua.com.foxminded.interfaces.StudentsDAOInterface;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class StudentsCoursesDAO implements StudentsDAOInterface {

    private static final String FAILED_CONNECTION_MESSAGE = "Database connection failed";
    public static final String SQL_RESOURCES = "resources/sql.properties";

    @Override
    public <T> void create(T rowData) throws DAOException {
        Connection connection = null;
        Statement statement = null;
        Properties properties = new Properties();
        try {
            FileInputStream stream = new FileInputStream(SQL_RESOURCES);
            properties.load(stream);
            stream.close();

            connection = new ConnectionFactory().connect();
            statement = connection.createStatement();
            String[] data = rowData.toString().split(" ");
            statement.executeQuery(
                    String.format(properties.getProperty(
                            "CREATE_STUDENT_TO_COURSES"), Integer.parseInt(data[0]), Integer.parseInt(data[1])));
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

    public void deleteById(int studentId) throws DAOException {
        Connection connection = null;
        Statement statement = null;
        Properties properties = new Properties();
        try {
            FileInputStream stream = new FileInputStream(SQL_RESOURCES);
            properties.load(stream);
            stream.close();

            connection = new ConnectionFactory().connect();
            statement = connection.createStatement();
            statement.executeQuery(
                    String.format(properties.getProperty("DELETE_FROM_STUDENTS_COURSES_BY_STUDENT_ID"), studentId));
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

    public void deleteFromCourse(int studentId, int courseId) throws DAOException {
        Properties properties = new Properties();
        Connection connection = null;
        Statement statement = null;
        try {
            FileInputStream stream = new FileInputStream(SQL_RESOURCES);
            properties.load(stream);
            stream.close();

            connection = new ConnectionFactory().connect();
            statement = connection.createStatement();
            statement.executeQuery(
                    String.format(properties.getProperty("DELETE_FROM_COURSE"), studentId, courseId));
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

    public List<String[]> getStudentsRelatedToCourses(String courseName) throws DAOException {
        Properties properties = new Properties();
        Connection connection = null;
        List<String[]> names = new LinkedList<>();
        try {
            FileInputStream stream = new FileInputStream(SQL_RESOURCES);
            properties.load(stream);
            stream.close();

            connection = new ConnectionFactory().connect();
            PreparedStatement statement = connection.prepareStatement(
                    String.format(properties.getProperty("GET_STUDENTS_RELATED_TO_COURSES"), courseName));
            final ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String[] groupSize = new String[2];
                groupSize[0] = resultSet.getString("first_name");
                groupSize[1] = resultSet.getString("last_name");
                names.add(groupSize);
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
        return names;
    }
}

package ua.com.foxminded.dao;

import ua.com.foxminded.connection.ConnectionFactory;
import ua.com.foxminded.exceptions.DAOException;
import ua.com.foxminded.interfaces.StudentsCoursesDAO;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public class StudentsCoursesJdbcDao implements StudentsCoursesDAO {

    ConnectionFactory factory;

    public StudentsCoursesJdbcDao(ConnectionFactory factory) {
        this.factory = factory;
    }

    public final static String SQL_RESOURCES = "resources/sql.properties";
    public final static String SPACE = " ";
    private final static Logger logger = Logger.getLogger(StudentsCoursesJdbcDao.class.getName());

    @Override
    public void create(String rowData) throws DAOException {
        Connection connection = null;
        Statement statement = null;
        Properties properties = new Properties();
        try {
            FileInputStream stream = new FileInputStream(SQL_RESOURCES);
            properties.load(stream);
            stream.close();

            connection = factory.connect();
            statement = connection.createStatement();
            String[] data = rowData.split(SPACE);
            statement.execute(
                    String.format(
                            properties.getProperty("create.student.to.course"),
                            Integer.parseInt(data[0]), Integer.parseInt(data[1]))
            );
        } catch (SQLException | IOException | ClassNotFoundException throwables) {
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
            statement.execute(
                    String.format(properties.getProperty("delete.from.students.courses.by.student.id"), studentId));
        } catch (SQLException | IOException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
            logger.info(throwables.getMessage());
        } finally {
            factory.close(connection);
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

            connection = factory.connect();
            statement = connection.createStatement();
            statement.execute(
                    String.format(properties.getProperty("delete.from.course"), studentId, courseId));
        } catch (SQLException | IOException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
            logger.info(throwables.getMessage());
        } finally {
            factory.close(connection);
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

            connection = factory.connect();
            PreparedStatement statement = connection.prepareStatement(
                    String.format(properties.getProperty("get.students.to.courses"), courseName));
            final ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String[] groupSize = new String[2];
                groupSize[0] = resultSet.getString("first_name");
                groupSize[1] = resultSet.getString("last_name");
                names.add(groupSize);
            }
        } catch (SQLException | IOException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
            logger.info(throwables.getMessage());
        } finally {
            factory.close(connection);
        }
        return names;
    }

    @Override
    public List<String[]> readAllData() throws DAOException {
        Connection connection = null;
        List<String[]> courseList = new LinkedList<>();
        Properties properties = new Properties();
        try {
            FileInputStream stream = new FileInputStream(SQL_RESOURCES);
            properties.load(stream);
            stream.close();

            connection = factory.connect();
            PreparedStatement statement = connection.prepareStatement(properties.getProperty("get.students.courses.list"));
            final ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                courseList.add(new String[]{
                        resultSet.getString("student_id"),
                        resultSet.getString("course_id")
                });
            }
        } catch (SQLException | IOException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
            logger.info(throwables.getMessage());
        } finally {
            factory.close(connection);
        }
        return courseList;
    }

    @Override
    public void update(int studentId, String courseId) throws DAOException {
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
                    String.format(
                            properties.getProperty("update.group.name"), Integer.parseInt(courseId), studentId));
        } catch (SQLException | IOException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
            logger.info(throwables.getMessage());
        } finally {
            factory.close(connection);
        }
    }
}

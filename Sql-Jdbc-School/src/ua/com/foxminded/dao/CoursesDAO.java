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
import java.util.logging.Logger;

public class CoursesDAO implements CourseDAOInterface {

    String resourceFilePath;

    public CoursesDAO(String resourceFilePath) {
        this.resourceFilePath = resourceFilePath;
    }

    public static final String SQL_RESOURCES = "src/resources/sql.properties";
    private final static Logger logger = Logger.getLogger(CoursesDAO.class.getName());

    @Override
    public void create(String courseName) throws DAOException {
        Connection connection = null;
        Statement statement = null;
        Properties properties = new Properties();
        ConnectionFactory factory = new ConnectionFactory(resourceFilePath);
        try {
            FileInputStream stream = new FileInputStream(SQL_RESOURCES);
            properties.load(stream);
            stream.close();
            connection = factory.connect();
            statement = connection.createStatement();
            statement.executeQuery(String.format(properties.getProperty("create.course"), courseName));
        } catch (SQLException | IOException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
            logger.info(throwables.getMessage());
        } finally {
            factory.close(connection);
        }
    }

    public Map<Integer, String> getCoursesList() throws DAOException {
        Connection connection = null;
        Map<Integer, String> courseList = new LinkedHashMap<>();
        Properties properties = new Properties();
        ConnectionFactory factory = new ConnectionFactory(resourceFilePath);
        try {
            FileInputStream stream = new FileInputStream(SQL_RESOURCES);
            properties.load(stream);
            stream.close();

            connection = factory.connect();
            PreparedStatement statement = connection.prepareStatement(properties.getProperty("get.courses.list"));
            final ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                courseList.put(resultSet.getInt("id"), resultSet.getString("name"));
            }
        } catch (SQLException | IOException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
            logger.info(throwables.getMessage());
        } finally {
            factory.close(connection);
        }
        return courseList;
    }
}

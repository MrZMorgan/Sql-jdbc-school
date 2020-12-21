package ua.com.foxminded;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.foxminded.connection.ConnectionFactory;
import ua.com.foxminded.dao.CoursesDAO;
import ua.com.foxminded.dao.GroupsDAO;
import ua.com.foxminded.exceptions.DAOException;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class CoursesDAOTest {
    private final static String CONNECTION_PROPERTIES = "resources/h2_connection.properties";
    public static final String SQL_RESOURCES = "resources/sql.properties";
    private final CoursesDAO dao = new CoursesDAO(CONNECTION_PROPERTIES);
    private final ConnectionFactory factory = new ConnectionFactory(CONNECTION_PROPERTIES);
    Connection connection = null;
    Statement statement = null;

    @BeforeEach
    void createTable() {
        DataGenerator generator = new DataGenerator(CONNECTION_PROPERTIES);
        Properties properties = new Properties();
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(SQL_RESOURCES);
            properties.load(stream);
            generator.generateTable(
                    properties.getProperty("drop.courses.table"),
                    properties.getProperty("create.courses.table.h2.database"));
        } catch (DAOException | IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void shouldCreateNewCourse() {
        try {
            dao.create("math");

            connection = factory.connect();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM courses;");
            String id = "";
            String name = "";
            while (resultSet.next()) {
                id = resultSet.getString("ID");
                name = resultSet.getString("NAME");
            }
            assertEquals("1", id);
            assertEquals("math", name);
            connection.close();
        } catch (DAOException | IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    void shouldGetCoursesList() {
        List<String> expectedGroupsList = new LinkedList<>();
        expectedGroupsList.add("1 math");
        expectedGroupsList.add("2 geometry");
        expectedGroupsList.add("3 biology");
        try {
            dao.create("math");
            dao.create("geometry");
            dao.create("biology");

            connection = factory.connect();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM courses;");
            List<String> actualCoursesList = new LinkedList<>();
            while (resultSet.next()) {
                actualCoursesList.add(resultSet.getString("ID") + " "
                        + resultSet.getString("NAME"));
            }
            assertEquals(expectedGroupsList, actualCoursesList);
            connection.close();
        } catch (DAOException | IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
package ua.com.foxminded;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.foxminded.connection.ConnectionFactory;
import ua.com.foxminded.dao.CoursesDAO;
import ua.com.foxminded.exceptions.DAOException;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class CoursesDAOTest {
    private final static String CONNECTION_PROPERTIES = "resources/h2_connection.properties";
    public static final String SQL_RESOURCES = "resources/sql.properties";
    private CoursesDAO dao = new CoursesDAO(SQL_RESOURCES);
    ConnectionFactory factory = new ConnectionFactory(CONNECTION_PROPERTIES);

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
        Connection connectionToCreate = null;
        Connection connectionToCheck = null;
        Statement statementToCreate = null;
        Statement statementToCheck = null;
        Properties properties = new Properties();
        try {
            FileInputStream stream = new FileInputStream(SQL_RESOURCES);
            properties.load(stream);
            connectionToCreate = factory.connect();
            statementToCreate = connectionToCreate.createStatement();
            statementToCreate.execute(String.format(properties.getProperty("create.course.h2"), "math"));
            connectionToCreate.close();

            connectionToCheck = factory.connect();
            statementToCheck = connectionToCheck.createStatement();
            ResultSet resultSet = statementToCheck.executeQuery("SELECT * FROM courses;");
            String id = "";
            String name = "";
            while (resultSet.next()) {
                id = resultSet.getString("ID");
                name = resultSet.getString("NAME");
            }
            assertEquals(id, "1");
            assertEquals(name, "math");
        } catch (ClassNotFoundException | SQLException | IOException e) {
            e.printStackTrace();
        }
    }

//    @Test
//    void getCoursesList() {
//    }
}
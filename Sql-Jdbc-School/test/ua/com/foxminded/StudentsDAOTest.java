package ua.com.foxminded;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.foxminded.connection.ConnectionFactory;
import ua.com.foxminded.dao.GroupsDAO;
import ua.com.foxminded.dao.StudentsDAO;
import ua.com.foxminded.exceptions.DAOException;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class StudentsDAOTest {
    private final static String CONNECTION_PROPERTIES = "resources/h2_connection.properties";
    public static final String SQL_RESOURCES = "resources/sql.properties";
    private final StudentsDAO dao = new StudentsDAO(CONNECTION_PROPERTIES);
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
                    properties.getProperty("drop.students.table"),
                    properties.getProperty("create.students.table.h2.database"));
        } catch (DAOException | IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void shouldCreateStudent() {
        try {
            dao.create("Egor Anchutin");

            connection = factory.connect();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM students;");
            String id = "";
            String firstName = "";
            String lastName = "";
            while (resultSet.next()) {
                id = resultSet.getString("ID");
                firstName = resultSet.getString("FIRST_NAME");
                lastName = resultSet.getString("LAST_NAME");
            }
            assertEquals("1", id);
            assertEquals("Egor", firstName);
            assertEquals("Anchutin", lastName);
            connection.close();
        } catch (DAOException | IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
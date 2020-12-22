package ua.com.foxminded.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.foxminded.DataGenerator;
import ua.com.foxminded.connection.ConnectionFactory;
import ua.com.foxminded.exceptions.DAOException;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class StudentsJdbcDaoTest {
    private final static String CONNECTION_PROPERTIES = "resources/h2_connection.properties";
    public static final String SQL_RESOURCES = "resources/sql.properties";
    private final ConnectionFactory factory = new ConnectionFactory(CONNECTION_PROPERTIES);
    private final StudentsJdbcDao dao = new StudentsJdbcDao(factory);
    private final DataGenerator generator = new DataGenerator(factory);
    Connection connection = null;
    Statement statement = null;

    @BeforeEach
    void createTable() {
        Properties properties = new Properties();
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(SQL_RESOURCES);
            properties.load(stream);
            generator.generateTable(
                    properties.getProperty("drop.students.table"),
                    properties.getProperty("create.students.table.h2.database"));
            connection = factory.connect();
            statement = connection.createStatement();
        } catch (DAOException | IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    void shouldCreateStudent() {
        try {
            dao.create("Egor Anchutin");

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
        } catch (DAOException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void shouldDeleteStudentById() {
        try {
            dao.create("Egor Anchutin");
            dao.create("Zach Morgan");
            dao.create("York Morgan");

            dao.deleteById(1);

            int expectedTableSize = 2;

            ResultSet resultSet = statement.executeQuery("SELECT * FROM students;");

            int actualTableSize = 0;
            while (resultSet.next()) {
                actualTableSize++;
            }
            assertEquals(expectedTableSize, actualTableSize);
            connection.close();
        } catch (DAOException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void shouldAssignStudentToGroup() {
        try {
            int groupId = 1;
            int studentId = 1;
            dao.create("Egor Anchutin");
            dao.assignStudentToGroup(groupId, studentId);

            ResultSet resultSet = statement.executeQuery("SELECT * FROM students;");

            int actualGroupId = 0;
            while (resultSet.next()) {
                actualGroupId = resultSet.getInt("GROUP_ID");
                System.out.println(groupId);
            }
            assertEquals(groupId, actualGroupId);
            connection.close();
        } catch (DAOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
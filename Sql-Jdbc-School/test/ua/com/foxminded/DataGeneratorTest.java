package ua.com.foxminded;

import org.junit.jupiter.api.*;
import ua.com.foxminded.connection.ConnectionFactory;
import ua.com.foxminded.exceptions.DAOException;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DataGeneratorTest {
    private final static String CONNECTION_PROPERTIES = "resources/h2_connection.properties";
    private final static String SQL_RESOURCES = "resources/sql.properties";
    private final ConnectionFactory factory = new ConnectionFactory(CONNECTION_PROPERTIES);

    @BeforeEach
    void createTable() {
        DataGenerator generator = new DataGenerator(factory);
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
    void shouldVerifyTableCreated() {
        ConnectionFactory factory = new ConnectionFactory(CONNECTION_PROPERTIES);
        Statement statement = null;
        Connection connection = null;
        try {
            connection = factory.connect();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM courses;");
            ResultSetMetaData metaData = resultSet.getMetaData();
            assertEquals("ID", metaData.getColumnName(1));
            assertEquals("NAME", metaData.getColumnName(2));
            assertEquals("DESCRIPTION", metaData.getColumnName(3));
            connection.close();
        } catch (SQLException | DAOException e) {
            e.printStackTrace();
        }
    }
}
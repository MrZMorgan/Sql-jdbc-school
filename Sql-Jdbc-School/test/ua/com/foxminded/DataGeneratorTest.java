package ua.com.foxminded;

import org.junit.jupiter.api.*;
import ua.com.foxminded.exceptions.DAOException;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DataGeneratorTest {
    private final static String CONNECTION_PROPERTIES = "resources/h2_connection.properties";
    public static final String SQL_RESOURCES = "resources/sql.properties";


    @Test
    void shouldCreateTable() {
        DataGenerator generator = new DataGenerator(CONNECTION_PROPERTIES);
        Properties properties = new Properties();
        FileInputStream stream = null;
        Statement statement = null;
        Connection connection = null;
        try {
            stream = new FileInputStream(SQL_RESOURCES);
            properties.load(stream);
            generator.generateTable(
                    properties.getProperty("drop.courses.table"),
                    properties.getProperty("create.courses.table.h2.database"));
            connection = DriverManager.getConnection("jdbc:h2:~/schooltest", "user", "1234");
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM courses");
            ResultSetMetaData metaData = resultSet.getMetaData();

            assertEquals("ID", metaData.getColumnName(1));
            assertEquals("NAME", metaData.getColumnName(2));
            assertEquals("DESCRIPTION", metaData.getColumnName(3));
            connection.close();
        } catch (IOException | DAOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
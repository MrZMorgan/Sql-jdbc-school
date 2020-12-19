package ua.com.foxminded;

import org.junit.jupiter.api.*;
import ua.com.foxminded.exceptions.DAOException;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class DataGeneratorTest {
    private final static String connectionProperties = "resources/h2_connection.properties";
    public static final String SQL_RESOURCES = "resources/sql.properties";

    @Test
    void generateTable() throws IOException, DAOException, SQLException {
//        DataGenerator generator = new DataGenerator(connectionProperties);
//        Properties properties = new Properties();
//        FileInputStream stream = new FileInputStream(SQL_RESOURCES);
//        properties.load(stream);
//
//        generator.generateTable(properties.getProperty("drop.groups.table"),
//                properties.getProperty("create.groups.table"));


    }
}
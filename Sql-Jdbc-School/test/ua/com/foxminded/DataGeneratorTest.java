package ua.com.foxminded;

import org.junit.jupiter.api.*;
import ua.com.foxminded.exceptions.DAOException;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import static org.junit.jupiter.api.Assertions.*;

class DataGeneratorTest {
    private final static String connectionProperties = "resources/h2_connection.properties";
    public static final String SQL_RESOURCES = "resources/sql.properties";


    @Test
    void shouldCreateTable() throws SQLException {



    }
}
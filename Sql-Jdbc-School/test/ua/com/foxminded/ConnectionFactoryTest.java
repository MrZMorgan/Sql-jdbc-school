package ua.com.foxminded;

import org.junit.jupiter.api.Test;
import ua.com.foxminded.connection.ConnectionFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionFactoryTest {
    private final static String CONNECTION_PROPERTIES = "resources/h2_connection.properties";

    @Test
    void testConnection() {
        ConnectionFactory factory = new ConnectionFactory(CONNECTION_PROPERTIES);
        Connection connection = null;
        try {
            connection = factory.connect();
            connection.close();
        } catch (SQLException | IOException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        assertNotNull(connection);
    }
}
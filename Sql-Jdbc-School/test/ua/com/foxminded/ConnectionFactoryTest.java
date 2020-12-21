package ua.com.foxminded;

import org.junit.jupiter.api.Test;
import ua.com.foxminded.connection.ConnectionFactory;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionFactoryTest {

    private final static String connectionProperties = "resources/h2_connection.properties";

    @Test
    void testConnection() throws IOException, ClassNotFoundException, SQLException {
        ConnectionFactory factory = new ConnectionFactory(connectionProperties);
        Connection connection = factory.connect();
        assertNotNull(connection);
        connection.close();
    }
}
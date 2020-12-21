package ua.com.foxminded;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionFactoryTest {

    @Test
    void testConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(
                    "jdbc:h2:~/schooltest;DATABASE_TO_UPPER=false;",
                    "user",
                    "1234");
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        assertNotNull(connection);
    }
}
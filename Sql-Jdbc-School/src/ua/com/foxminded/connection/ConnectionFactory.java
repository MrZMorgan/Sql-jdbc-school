package ua.com.foxminded.connection;

import ua.com.foxminded.exceptions.DAOException;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {

    private static final String FAILED_CONNECTION_MESSAGE = "Database connection failed";

    private final String resourceFilePath;

    public ConnectionFactory(String resourceFilePath) {
        this.resourceFilePath = resourceFilePath;
    }

    public Connection connect() throws ClassNotFoundException, IOException {
        Properties properties = new Properties();
        FileInputStream stream = new FileInputStream(resourceFilePath);
        properties.load(stream);
        stream.close();
        Class.forName(properties.getProperty("driver"));
        try {
            return DriverManager.getConnection(properties.getProperty("url"),
                    properties.getProperty("user"), properties.getProperty("password"));
        } catch (SQLException throwables) {
            return null;
        }
    }

    public void close(Connection connection) throws DAOException {
        try {
            connection.close();
        } catch (SQLException throwables) {
            throw new DAOException(FAILED_CONNECTION_MESSAGE);
        }
    }
}

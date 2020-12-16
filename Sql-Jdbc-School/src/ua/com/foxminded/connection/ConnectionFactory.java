package ua.com.foxminded.connection;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory implements SchoolDBConnection {
    @Override
    public Connection connect(FileInputStream stream) throws ClassNotFoundException, SQLException, IOException {
        Class.forName("org.postgresql.Driver");
        Properties properties = new Properties();
        properties.load(stream);
        return DriverManager.getConnection(properties.getProperty("url"),
                properties.getProperty("user"), properties.getProperty("password"));
    }
}

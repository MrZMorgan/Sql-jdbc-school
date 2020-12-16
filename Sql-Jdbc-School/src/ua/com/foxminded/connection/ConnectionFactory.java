package ua.com.foxminded.connection;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {

    public final static String RESOURCE_FILE_PATH = "resources/connection.properties";

    public Connection connect() throws ClassNotFoundException, SQLException, IOException {
        Properties properties = new Properties();
        FileInputStream stream = new FileInputStream(RESOURCE_FILE_PATH);
        properties.load(stream);
        stream.close();
        Class.forName(properties.getProperty("driver"));
        return DriverManager.getConnection(properties.getProperty("url"),
                properties.getProperty("user"), properties.getProperty("password"));
    }
}

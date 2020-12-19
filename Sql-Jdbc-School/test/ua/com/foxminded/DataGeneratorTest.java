package ua.com.foxminded;

import org.junit.jupiter.api.*;;
import ua.com.foxminded.exceptions.DAOException;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import static org.junit.jupiter.api.Assertions.*;

class DataGeneratorTest {
    private final static String connectionProperties = "src/resources/h2_connection.properties";
    public static final String SQL_RESOURCES = "src/resources/sql.properties";


    @Test
    void shouldCreateTable() throws IOException, DAOException, ClassNotFoundException, SQLException {
//        DataGenerator generator = new DataGenerator(connectionProperties);
//
//        Properties properties = new Properties();
//        FileInputStream stream = new FileInputStream(SQL_RESOURCES);
//        properties.load(stream);


//        generator.generateTable(properties.getProperty("drop.course.table.h2.database"),
//                properties.getProperty("create.course.table.h2.database"));

        Properties properties2 = new Properties();
        FileInputStream stream2 = new FileInputStream(connectionProperties);
        properties2.load(stream2);

        Class.forName("org.h2.Driver");
        Connection connection = null;
        Statement statement = null;

        connection = DriverManager.getConnection("jdbc:h2:mem:school", "user", "1234");

        statement = connection.createStatement();
        statement.executeQuery("DROP TABLE COURSES");
//        ResultSet resultSet = statement.getResultSet();
    }
}
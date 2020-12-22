package ua.com.foxminded;

import ua.com.foxminded.connection.ConnectionFactory;
import ua.com.foxminded.dao.CoursesJdbcDao;
import ua.com.foxminded.dao.GroupsJdbcDao;
import ua.com.foxminded.dao.StudentsCoursesJdbcDao;
import ua.com.foxminded.dao.StudentsJdbcDao;
import ua.com.foxminded.exceptions.DAOException;
import ua.com.foxminded.facade.Facade;
import java.io.IOException;
import java.util.logging.Logger;

public class Solution {
    public static void main(String[] args) {
        Logger logger = Logger.getLogger(Facade.class.getName());
        String connectionProperties = "resources/postrgres_connection.properties";
        ConnectionFactory factory = new ConnectionFactory(connectionProperties);

        Facade facade = new Facade(new DataGenerator(factory),
                                   new CoursesJdbcDao(factory),
                                   new GroupsJdbcDao(factory),
                                   new StudentsJdbcDao(factory),
                                   new StudentsCoursesJdbcDao(factory));
        try {
            facade.createTable();
            facade.fillTableWithTestData();
            facade.workWithDataBase();
        } catch (DAOException | IOException e) {
            e.printStackTrace();
            logger.info(e.getMessage());
        }
    }
}

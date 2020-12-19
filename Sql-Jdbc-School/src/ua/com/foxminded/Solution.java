package ua.com.foxminded;

import ua.com.foxminded.dao.CoursesDAO;
import ua.com.foxminded.dao.GroupsDAO;
import ua.com.foxminded.dao.StudentsCoursesDAO;
import ua.com.foxminded.dao.StudentsDAO;
import ua.com.foxminded.exceptions.DAOException;
import ua.com.foxminded.facade.Facade;
import java.io.IOException;
import java.util.logging.Logger;

public class Solution {
    public static void main(String[] args) {
        Logger logger = Logger.getLogger(Facade.class.getName());
        String connectionProperties = "resources/postrgres_connection.properties";

        Facade facade = new Facade(new DataGenerator(connectionProperties),
                                   new CoursesDAO(connectionProperties),
                                   new GroupsDAO(connectionProperties),
                                   new StudentsDAO(connectionProperties),
                                   new StudentsCoursesDAO(connectionProperties));
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

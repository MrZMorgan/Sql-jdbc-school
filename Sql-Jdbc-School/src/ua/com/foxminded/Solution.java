package ua.com.foxminded;

import ua.com.foxminded.connection.ConnectionFactory;
import ua.com.foxminded.dao.CoursesJdbcDao;
import ua.com.foxminded.dao.GroupsJdbcDao;
import ua.com.foxminded.dao.StudentsCoursesJdbcDao;
import ua.com.foxminded.dao.StudentsJdbcDao;
import ua.com.foxminded.facade.Facade;

public class Solution {
    public static void main(String[] args) {
        String connectionProperties = "resources/postrgres_connection.properties";
        ConnectionFactory factory = new ConnectionFactory(connectionProperties);

        Facade facade = new Facade(
                new DataGenerator(factory),
                new CoursesJdbcDao(factory),
                new GroupsJdbcDao(factory),
                new StudentsJdbcDao(factory),
                new StudentsCoursesJdbcDao(factory)
        );

        facade.workWithDataBase();
    }
}

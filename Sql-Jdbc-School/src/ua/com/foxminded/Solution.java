package ua.com.foxminded;

import ua.com.foxminded.connection.ConnectionFactory;
import ua.com.foxminded.dao.CoursesDAO;
import ua.com.foxminded.dao.GroupsDAO;
import ua.com.foxminded.dao.StudentsCoursesDAO;
import ua.com.foxminded.dao.StudentsDAO;
import ua.com.foxminded.exceptions.DAOException;
import ua.com.foxminded.facade.Facade;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Solution {
    public static void main(String[] args) {
        Logger logger = Logger.getLogger(Facade.class.getName());
        String logFilePath = "/home/egor/Документы/repositorys/sql--jdbc-school/Sql-Jdbc-School/logs/solution/solution_log.log";

        Facade facade = new Facade(new DataGenerator(),
                                   new CoursesDAO(),
                                   new GroupsDAO(),
                                   new StudentsDAO(),
                                   new StudentsCoursesDAO());
        try {
            facade.createTable();
            facade.fillTableWithTestData();
            facade.workWithDataBase();
        } catch (DAOException | IOException e) {
            new ConnectionFactory().log(logFilePath, logger, e);
        }
    }
}

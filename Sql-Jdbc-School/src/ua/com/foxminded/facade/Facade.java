package ua.com.foxminded.facade;

import ua.com.foxminded.DataGenerator;
import ua.com.foxminded.dao.CoursesJdbcDao;
import ua.com.foxminded.dao.GroupsJdbcDao;
import ua.com.foxminded.dao.StudentsCoursesJdbcDao;
import ua.com.foxminded.dao.StudentsJdbcDao;
import ua.com.foxminded.exceptions.DAOException;
import ua.com.foxminded.ui.UI;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

public class Facade {

    public final static String SQL_RESOURCES = "resources/sql.properties";
    private final static Logger logger = Logger.getLogger(Facade.class.getName());

    private final DataGenerator dataGenerator;
    private final CoursesJdbcDao coursesJdbcDao;
    private final GroupsJdbcDao groupsJdbcDao;
    private final StudentsJdbcDao studentsJdbcDao;
    private final StudentsCoursesJdbcDao studentsCoursesJdbcDao;

    public Facade(DataGenerator dataGenerator,
                  CoursesJdbcDao coursesJdbcDao,
                  GroupsJdbcDao groupsJdbcDao,
                  StudentsJdbcDao studentsJdbcDao,
                  StudentsCoursesJdbcDao studentsCoursesJdbcDao) {
        this.dataGenerator = dataGenerator;
        this.coursesJdbcDao = coursesJdbcDao;
        this.groupsJdbcDao = groupsJdbcDao;
        this.studentsJdbcDao = studentsJdbcDao;
        this.studentsCoursesJdbcDao = studentsCoursesJdbcDao;
    }

    public void createTable() throws DAOException {
        Properties properties = new Properties();
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(SQL_RESOURCES);
            properties.load(stream);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
            logger.info(e.getMessage());
        }

        dataGenerator.generateTable(properties.getProperty("drop.groups.table"),
                properties.getProperty("create.groups.table"));
        dataGenerator.generateTable(properties.getProperty("drop.courses.table"),
                properties.getProperty("create.courses.table"));
        dataGenerator.generateTable(properties.getProperty("drop.students.table"),
                properties.getProperty("create.students.table"));
        dataGenerator.generateTable(properties.getProperty("drop.students.courses.table"),
                properties.getProperty("create.students.courses.table"));
    }

    public void fillTableWithTestData() throws DAOException, IOException {
        List<String> groups = dataGenerator.generateGroupsNamesList();
        List<String> courses = dataGenerator.readFile("resources/rawdata/courses");
        List<String> firstNames = dataGenerator.readFile("resources/rawdata/first_names");
        List<String> lastNames = dataGenerator.readFile("resources/rawdata/last_names");
        List<String> fullNamesList = dataGenerator.generateFullNamesList(firstNames, lastNames);
        List<int[]> studentsJournal = dataGenerator.assignStudentsToGroups(groups, fullNamesList);
        List<String> assertions = dataGenerator.assignStudentsToCourses(fullNamesList, courses);

        for (String group : groups) {
            groupsJdbcDao.create(group);
        }

        for (String course : courses) {
            coursesJdbcDao.create(course);
        }

        for (String fullName : fullNamesList) {
            studentsJdbcDao.create(fullName);
        }

        for (int[] s : studentsJournal) {
            studentsJdbcDao.assignStudentToGroup(s[1], s[0]);
        }

        for (String assertion : assertions) {
            studentsCoursesJdbcDao.create(assertion);
        }
    }

    public void workWithDataBase() {
        try {
            createTable();
            fillTableWithTestData();

            UI ui = new UI(
                    coursesJdbcDao,
                    groupsJdbcDao,
                    studentsJdbcDao,
                    studentsCoursesJdbcDao
            );
            ui.runProgram();
        } catch (DAOException | IOException e) {
            e.printStackTrace();
        }
    }
}

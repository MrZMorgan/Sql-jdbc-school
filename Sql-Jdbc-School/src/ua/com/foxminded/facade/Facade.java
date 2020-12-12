package ua.com.foxminded.facade;

import ua.com.foxminded.DataGenerator;
import ua.com.foxminded.dao.CoursesDao;
import ua.com.foxminded.dao.GroupsDao;

import java.util.List;
import java.util.Map;

import static ua.com.foxminded.sql.Queries.*;

public class Facade {
    private final DataGenerator dataGenerator;
    private final CoursesDao coursesDao;
    private final GroupsDao groupsDao;

    public Facade(DataGenerator dataGenerator,
                  CoursesDao coursesDao,
                  GroupsDao groupsDao) {
        this.dataGenerator = dataGenerator;
        this.coursesDao = coursesDao;
        this.groupsDao = groupsDao;
    }

    public void generateTestData() {
        dataGenerator.generateTable(SQL_DROP_GROUPS_TABLE, SQL_CREATE_GROUPS_TABLE);
        dataGenerator.generateTable(SQL_DROP_COURSES_TABLE,SQL_CREATE_COURSES_TABLE);
        dataGenerator.generateTable(SQL_DROP_STUDENTS_TABLE, SQL_CREATE_STUDENTS_TABLE);

        List<String> groups = dataGenerator.generateGroupsNamesList();
        List<String> courses = dataGenerator.readFile("src/ua/com/foxminded/rawdata/courses");
        List<String> firstNames = dataGenerator.readFile("src/ua/com/foxminded/rawdata/first_names");
        List<String> lastNames = dataGenerator.readFile("src/ua/com/foxminded/rawdata/last_names");
        List<String[]> fullNamesList = dataGenerator.generateFullNamesList(firstNames, lastNames);
        List<String[]> studentsJournal = dataGenerator.assignStudentsToGroups(groups, fullNamesList);

        groups.forEach(g -> groupsDao.create(g));
        courses.forEach(c -> coursesDao.create(c));

        courses.forEach(System.out::println);

    }
}

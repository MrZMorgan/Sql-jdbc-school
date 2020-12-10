package ua.com.foxminded.facade;

import ua.com.foxminded.DataGenerator;
import ua.com.foxminded.dao.CoursesDAO;
import ua.com.foxminded.dao.GroupsDAO;
import ua.com.foxminded.dao.StudentsDAO;

import static ua.com.foxminded.sql.Queries.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Facade {
    final static String courses = "src/ua/com/foxminded/rawdata/courses";
    final static String first_names = "src/ua/com/foxminded/rawdata/first_names";
    final static String last_names = "src/ua/com/foxminded/rawdata/last_names";


    public void generateTestData() {
        DataGenerator dataGenerator = new DataGenerator();

        dataGenerator.generateTable(SQL_DROP_GROUPS_TABLE , SQL_CREATE_GROUPS_TABLE);
        dataGenerator.generateTable(SQL_DROP_COURSES_TABLE, SQL_CREATE_COURSES_TABLE);
        dataGenerator.generateTable(SQL_DROP_STUDENTS_TABLE, SQL_CREATE_STUDENTS_TABLE);

        CoursesDAO coursesDAO = new CoursesDAO();
        GroupsDAO groupsDAO = new GroupsDAO();
        StudentsDAO studentsDAO = new StudentsDAO();

        Map<String, Integer> groupNames = dataGenerator.generateGroupsNamesList();
        List<String[]> namesList = dataGenerator.generateNamesList(first_names, last_names);
        List<String> coursesList = dataGenerator.read(courses);
        List<String[]> namesGroups = dataGenerator.assignStudentsToGroups(groupNames, namesList);

        AtomicInteger groupId = new AtomicInteger(1);
        groupNames.forEach((groupName, groupSize) -> groupsDAO.create(groupId.getAndIncrement(), groupName));
        AtomicInteger courseId = new AtomicInteger(1);
        coursesList.forEach(course -> coursesDAO.create(courseId.getAndIncrement(), course));
        studentsDAO.fillTable(namesGroups, namesList);

//        dataGenerator.generateTable(SQL_DROP_STUDENTS_COURSES_TABLE, SQL_CREATE_STUDENTS_COURSES_TABLE);
    }
}

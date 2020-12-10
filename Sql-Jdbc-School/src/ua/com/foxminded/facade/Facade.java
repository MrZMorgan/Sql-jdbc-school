package ua.com.foxminded.facade;

import ua.com.foxminded.DataGenerator;
import ua.com.foxminded.dao.CoursesDAO;
import ua.com.foxminded.dao.GroupsDAO;
import ua.com.foxminded.dao.StudentsDAO;

import static ua.com.foxminded.sql.Queries.*;
import java.util.List;
import java.util.Map;

public class Facade {
    final static String courses = "src/ua/com/foxminded/rawdata/courses";
    final static String first_names = "src/ua/com/foxminded/rawdata/first_names";
    final static String last_names = "src/ua/com/foxminded/rawdata/last_names";

    public void generateTestData() {
        DataGenerator dataGenerator = new DataGenerator();
        CoursesDAO coursesDAO = new CoursesDAO();
        GroupsDAO groupsDAO = new GroupsDAO();
        StudentsDAO studentsDAO = new StudentsDAO();

        dataGenerator.generateTable(SQL_DROP_GROUPS_TABLE , SQL_CREATE_GROUPS_TABLE);
        dataGenerator.generateTable(SQL_DROP_COURSES_TABLE, SQL_CREATE_COURSES_TABLE);
        dataGenerator.generateTable(SQL_DROP_STUDENTS_TABLE, SQL_CREATE_STUDENTS_TABLE);

        Map<String, Integer> groupNames = dataGenerator.generateGroupsNamesList();
        List<String[]> namesList = dataGenerator.generateNamesList(first_names, last_names);
        List<String> coursesList = dataGenerator.read(courses);
        List<String[]> namesGroups = dataGenerator.assignStudentsToGroups(groupNames, namesList);

        groupNames.forEach((groupName, groupSize) -> groupsDAO.create(groupName));
        coursesList.forEach(course -> coursesDAO.create(course));


//        namesGroups.forEach(data -> studentsDAO.create(Integer.parseInt(data[0]), data[1], data[2]));

        int j = 0;
        if (namesGroups.size() == 200) {
            for (; j < 200; j++) {
                System.out.println(namesList.get(j)[0] + " " + namesList.get(j)[1] + " : "
                        + namesGroups.get(j)[0] + " : " + namesGroups.get(j)[1] + " " + namesGroups.get(j)[2]);
            }
        } else {
            if (namesGroups.size() < 200) {
                for (int i = 0; i < namesGroups.size(); i++) {
                    System.out.println(namesList.get(j)[0] + " " + namesList.get(j)[1] + " : "
                            + namesGroups.get(j)[0] + " : " + namesGroups.get(j)[1] + " " + namesGroups.get(j)[2]);
                    j++;
                }

                while (j < 200) {
                    System.out.println(namesList.get(j)[0] + " " + namesList.get(j)[1] + "нет группы");
                    j++;
                }
            }
        }
    }
}

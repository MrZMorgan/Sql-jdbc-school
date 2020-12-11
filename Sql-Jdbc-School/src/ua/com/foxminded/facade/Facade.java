package ua.com.foxminded.facade;

import ua.com.foxminded.DataGenerator;
import ua.com.foxminded.dao.CoursesDAO;
import ua.com.foxminded.dao.GroupsDAO;
import ua.com.foxminded.dao.StudentsCoursesDAO;
import ua.com.foxminded.dao.StudentsDAO;
import static ua.com.foxminded.sql.Queries.*;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class Facade {

    CoursesDAO coursesDAO ;
    GroupsDAO groupsDAO;
    StudentsDAO studentsDAO;
    StudentsCoursesDAO studentsCoursesDAO;
    DataGenerator dataGenerator;

    public Facade(CoursesDAO coursesDAO,
                  GroupsDAO groupsDAO,
                  StudentsDAO studentsDAO,
                  StudentsCoursesDAO studentsCoursesDAO,
                  DataGenerator dataGenerator) {
        this.coursesDAO = coursesDAO;
        this.groupsDAO = groupsDAO;
        this.studentsDAO = studentsDAO;
        this.studentsCoursesDAO = studentsCoursesDAO;
        this.dataGenerator = dataGenerator;
    }

    private final static String courses = "src/ua/com/foxminded/rawdata/courses";
    private final static String first_names = "src/ua/com/foxminded/rawdata/first_names";
    private final static String last_names = "src/ua/com/foxminded/rawdata/last_names";

    public void generateTestData() {
        dataGenerator.generateTable(SQL_DROP_GROUPS_TABLE , SQL_CREATE_GROUPS_TABLE);
        dataGenerator.generateTable(SQL_DROP_COURSES_TABLE, SQL_CREATE_COURSES_TABLE);
        dataGenerator.generateTable(SQL_DROP_STUDENTS_TABLE, SQL_CREATE_STUDENTS_TABLE);

        Map<String, Integer> groupNames = dataGenerator.generateGroupsNamesList();
        List<String[]> namesList = dataGenerator.generateNamesList(first_names, last_names);
        List<String> coursesList = dataGenerator.read(courses);
        List<String[]> namesGroups = dataGenerator.assignStudentsToGroups(groupNames, namesList);

        AtomicInteger groupId = new AtomicInteger(1);
        groupNames.forEach((groupName, groupSize) -> groupsDAO.create(groupId.getAndIncrement(), groupName));
        AtomicInteger courseId = new AtomicInteger(1);
        coursesList.forEach(course -> coursesDAO.create(courseId.getAndIncrement(), course));
        studentsDAO.fillTable(namesGroups, namesList);

        dataGenerator.generateTable(SQL_DROP_STUDENTS_COURSES_TABLE, SQL_CREATE_STUDENTS_COURSES_TABLE);
//        dataGenerator.assignCoursesToStudents(namesList, studentsCoursesDAO, coursesList.size());
    }

    public void workWithDatabase() {
        System.out.println("To delete student from DB type \"delete\"" + "\n" +
                           "To add new student type \"add\"" + "\n" +
                           "To assign student to course type \"assign\"" + "\n" +
                           "To find all students related to course with given name type \"student to courses\"" + "\n" +
                           "To delete student from courseType \"delete from\"" + "\n" +
                           "To find all groups with less or equals student count type expected group size \"group\"" + "\n" + "\n" +
                           "To close app close type \"exit\"");
                Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();
        while (!command.equals("exit")) {
            switch (command) {
                case "delete" :
                    System.out.println("Type student id and press \"Enter\" to delete student");
                    command = scanner.nextLine();
                    studentsCoursesDAO.deleteStudent(Integer.parseInt(command));
                    studentsDAO.deleteStudent(Integer.parseInt(command));
                    break;
                case "add":
                    System.out.println("Type students \"first_name\"");
                    String firstName = scanner.nextLine();
                    System.out.println("Type students \"last_name\"" );
                    String lastName = scanner.nextLine();
                    studentsDAO.create(studentsDAO.getStudentsTableSize() + 1, 0,firstName, lastName);
                    break;
                case "assign":
                    System.out.println("Type \"students_id\"");
                    int studentsId = scanner.nextInt();
                    System.out.println("Type students \"course_id\"" );
                    int courseId = scanner.nextInt();
                    studentsCoursesDAO.create(studentsId, courseId);
                    break;
                case "delete from":
                    System.out.println("Type \"students_id\"");
                    studentsId = scanner.nextInt();
                    System.out.println("Type students \"course_id\"");
                    courseId = scanner.nextInt();
                    studentsCoursesDAO.deleteStudentFromCourse(studentsId, courseId);
                    break;
                case "student to courses" :
                    System.out.println("Type \"course_name\"");
                    courseId = scanner.nextInt();
                    int courseName = coursesDAO.getCourseId(courseId);
                    List<Integer> list = studentsCoursesDAO.getStudentsIdListRelatedToCourseId(courseName);
                    for (Integer i : list) {
                        String[] fullName = studentsDAO.getStudentById(i);
                        System.out.println(fullName[0] + " " + fullName[1]);
                    }
                    break;
                case "group":
                    System.out.println("Type \"expected_group_size\"");
                    int expectedGroupSize = scanner.nextInt();
                    List<int[]> groupsSizes = studentsDAO.getGroupsBySize(expectedGroupSize);
                    List<String> groupNames = groupsDAO.getGroupNameList();
                    for (int[] groupsSize : groupsSizes) {
                        for (int j = 0; j < groupNames.size(); j++) {
                            if (groupsSize[0] == j + 1) {
                                System.out.println(groupNames.get(j) + " : " + groupsSize[1]);
                            }
                        }
                    }
                    break;
                default:
                    System.out.println("Incorrect command");
                    command = scanner.nextLine();
                    break;
            }
        }
    }
}

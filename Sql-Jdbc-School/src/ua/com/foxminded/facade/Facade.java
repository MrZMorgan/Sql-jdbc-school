package ua.com.foxminded.facade;

import ua.com.foxminded.DataGenerator;
import ua.com.foxminded.dao.CoursesDAO;
import ua.com.foxminded.dao.GroupsDAO;
import ua.com.foxminded.dao.StudentsCoursesDAO;
import ua.com.foxminded.dao.StudentsDAO;

import java.util.*;

import static ua.com.foxminded.sql.Queries.*;

public class Facade {

    private final DataGenerator dataGenerator;
    private final CoursesDAO coursesDao;
    private final GroupsDAO groupsDao;
    private final StudentsDAO studentsDAO;
    private final StudentsCoursesDAO studentsCoursesDAO;




    private final static String INTRO_MESSAGE = "To find all groups with less or equals student count type \"groups\"\n" +
                                                "To find all students related to course with given name type \"courses\"\n" +
                                                "To add new student to database type \"add\"\n" +
                                                "To delete student by id type \"delete\"\n" +
                                                "To add student to course type \"assign\"\n" +
                                                "To remove student to course type \"remove\"\n\n" +
                                                "To close app type \"exit\"";
    private final static String ADD_NEW_STUDENT_ID_MESSAGE =
            "Type the number of course to which you want to assign the student and press \"Enter\" button\n" +
            "To not assign a student to the course type \"0\" and press \"Enter\" button";
    private final static String ADD_NEW_STUDENT_FIRST_NAME_MESSAGE =
            "Type student first name and press \"Enter\" button";
    private final static String ADD_NEW_STUDENT_LAST_NAME_MESSAGE =
            "Type student last name and press \"Enter\" button";
    private final static String DELETE_STUDENT_BY_ID_MESSAGE =
            "Type the id of student you wand delete from database and press \"Enter\" button";
    private final static String STUDENT_ID_MESSAGE =
            "Type the student id and press \"Enter\" button";
    private final static String COURSE_ID_MESSAGE =
            "Type the course id and press \"Enter\" button";
    private final static String GROUPS_WITH_LESS_OR_EQUALS_COUNT_MESSAGE =
            "Type expected group size and press \"Enter\" button";
    private final static String STUDENTS_RELATED_TO_COURSES_MESSAGE =
            "Type course name press \"Enter\" button";

    public Facade(DataGenerator dataGenerator,
                  CoursesDAO coursesDao,
                  GroupsDAO groupsDao,
                  StudentsDAO studentsDAO,
                  StudentsCoursesDAO studentsCoursesDAO) {
        this.dataGenerator = dataGenerator;
        this.coursesDao = coursesDao;
        this.groupsDao = groupsDao;
        this.studentsDAO = studentsDAO;
        this.studentsCoursesDAO = studentsCoursesDAO;
    }

    public void generateTestData() {
        dataGenerator.generateTable(SQL_DROP_GROUPS_TABLE, SQL_CREATE_GROUPS_TABLE);
        dataGenerator.generateTable(SQL_DROP_COURSES_TABLE,SQL_CREATE_COURSES_TABLE);
        dataGenerator.generateTable(SQL_DROP_STUDENTS_TABLE, SQL_CREATE_STUDENTS_TABLE);
        dataGenerator.generateTable(SQL_DROP_STUDENTS_COURSES_TABLE, SQL_CREATE_STUDENTS_COURSES_TABLE);

        List<String> groups = dataGenerator.generateGroupsNamesList();
        List<String> courses = dataGenerator.readFile("src/ua/com/foxminded/rawdata/courses");
        List<String> firstNames = dataGenerator.readFile("src/ua/com/foxminded/rawdata/first_names");
        List<String> lastNames = dataGenerator.readFile("src/ua/com/foxminded/rawdata/last_names");
        List<String> fullNamesList = dataGenerator.generateFullNamesList(firstNames, lastNames);

        groups.forEach(groupsDao::create); // заполнили таблицу группами
        courses.forEach(coursesDao::create); // заполнили таблицукурсами

        fullNamesList.forEach(student -> studentsDAO.create(student)); // заполнили таблицу студентов

        List<int[]> studentsJournal = dataGenerator.assignStudentsToGroups(groups, fullNamesList, studentsDAO); // приписали студентов у группам
        studentsJournal.forEach(s -> studentsDAO.assignStudentToGroup(s[1], s[0]));

        List<int[]> assertions = dataGenerator.assignStudentsToCourses(fullNamesList, courses);
        assertions.forEach(a -> studentsCoursesDAO.create(a[0], a[1]));
    }

    public void workWithDataBase() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(INTRO_MESSAGE);
        String command = scanner.nextLine();
        while (!command.equals("exit")) {
            switch (command) {
                case "groups" :
                    findGroupsWithLessOrEqualsStudentCount();
                    System.out.println(INTRO_MESSAGE);
                    command = scanner.nextLine();
                    break;
                case "courses":
                    findStudentsRelatedToCourses();
                    System.out.println(INTRO_MESSAGE);
                    command = scanner.nextLine();
                    break;
                case "add" :
                    addNewStudent();
                    System.out.println(INTRO_MESSAGE);
                    command = scanner.nextLine();
                    break;
                case "delete" :
                    deleteStudentById();
                    System.out.println(INTRO_MESSAGE);
                    command = scanner.nextLine();
                    break;
                case "assign" :
                    assignStudentToCourse();
                    System.out.println(INTRO_MESSAGE);
                    command = scanner.nextLine();
                    break;
                case "remove" :
                    removeStudentFromCourse();
                    System.out.println(INTRO_MESSAGE);
                    command = scanner.nextLine();
                    break;
                default:
                    System.out.println("Incorrect command\n");
                    System.out.println(INTRO_MESSAGE);
                    command = scanner.nextLine();
            }
        }
    }

    private void findGroupsWithLessOrEqualsStudentCount() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(GROUPS_WITH_LESS_OR_EQUALS_COUNT_MESSAGE);
        int expectedGroupSize = scanner.nextInt();
        List<int[]> groups = groupsDao.getGroupsBySize(expectedGroupSize);
        groups.forEach(g -> System.out.println("Group " + g[0] + " : " + g[1] + " students"));
        System.out.println();
    }

    private void findStudentsRelatedToCourses() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(STUDENTS_RELATED_TO_COURSES_MESSAGE);
        String courseName = scanner.nextLine();
        List<String[]> strings = studentsCoursesDAO.getStudentsRelatedToCourses(courseName);
        strings.forEach(n -> System.out.println(n[0] + " " + n[1]));
        System.out.println();
    }

    private void addNewStudent() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(ADD_NEW_STUDENT_ID_MESSAGE);
        int groupId = Integer.parseInt(scanner.nextLine());
        System.out.println(ADD_NEW_STUDENT_FIRST_NAME_MESSAGE);
        String firstName = scanner.nextLine();
        System.out.println(ADD_NEW_STUDENT_LAST_NAME_MESSAGE);
        String lastName = scanner.nextLine();
        studentsDAO.create(firstName + " " + lastName);
    }

    private void deleteStudentById() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(DELETE_STUDENT_BY_ID_MESSAGE);
        int studentId = scanner.nextInt();
        studentsCoursesDAO.deleteById(studentId);
        studentsDAO.deleteById(studentId);
    }

    private void assignStudentToCourse() {
        int[] studentInfo = collectInfoForQuery();
        studentsCoursesDAO.create(studentInfo[0], studentInfo[1]);
    }

    private void removeStudentFromCourse() {
        int[] studentInfo = collectInfoForQuery();
        studentsCoursesDAO.deleteFromCourse(studentInfo[0], studentInfo[1]);
    }

    private int[] collectInfoForQuery() {
        int[] info = new int[2];
        Scanner scanner = new Scanner(System.in);
        System.out.println(STUDENT_ID_MESSAGE);
        info[0] = scanner.nextInt();
        coursesDao.getCoursesList().forEach((id, name) -> System.out.println("Course id: " + id + " - " + name));
        System.out.println(COURSE_ID_MESSAGE);
        info[1] = scanner.nextInt();
        return info;
    }
}

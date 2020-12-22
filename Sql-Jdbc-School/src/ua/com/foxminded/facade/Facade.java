package ua.com.foxminded.facade;

import ua.com.foxminded.DataGenerator;
import ua.com.foxminded.dao.CoursesJdbcDao;
import ua.com.foxminded.dao.GroupsJdbcDao;
import ua.com.foxminded.dao.StudentsCoursesJdbcDao;
import ua.com.foxminded.dao.StudentsJdbcDao;
import ua.com.foxminded.exceptions.DAOException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

public class Facade {

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

    public final static String INTRO_MESSAGE = "To find all groups with less or equals student count type \"groups\"\n" +
                                                "To find all students related to course with given name type \"courses\"\n" +
                                                "To add new student to database type \"add\"\n" +
                                                "To delete student by id type \"delete\"\n" +
                                                "To add student to course type \"assign\"\n" +
                                                "To remove student to course type \"remove\"\n\n" +
                                                "To close app type \"exit\"";
    public final static String ADD_NEW_STUDENT_ID_MESSAGE =
            "Type the number of course to which you want to assign the student and press \"Enter\" button\n" +
            "To not assign a student to the course type \"0\" and press \"Enter\" button";
    public final static String ADD_NEW_STUDENT_FIRST_NAME_MESSAGE =
            "Type student first name and press \"Enter\" button";
    public final static String ADD_NEW_STUDENT_LAST_NAME_MESSAGE =
            "Type student last name and press \"Enter\" button";
    public final static String DELETE_STUDENT_BY_ID_MESSAGE =
            "Type the id of student you wand delete from database and press \"Enter\" button";
    public final static String STUDENT_ID_MESSAGE =
            "Type the student id and press \"Enter\" button";
    public final static String COURSE_ID_MESSAGE =
            "Type the course id and press \"Enter\" button";
    public final static String GROUPS_WITH_LESS_OR_EQUALS_COUNT_MESSAGE =
            "Type expected group size and press \"Enter\" button";
    public final static String STUDENTS_RELATED_TO_COURSES_MESSAGE =
            "Type course name press \"Enter\" button";
    public static final String SQL_RESOURCES = "resources/sql.properties";
    public final static String SPACE = " ";
    private final static Logger logger = Logger.getLogger(Facade.class.getName());

    public void workWithDataBase() throws DAOException, IOException {
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

    private void findGroupsWithLessOrEqualsStudentCount() throws DAOException, IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println(GROUPS_WITH_LESS_OR_EQUALS_COUNT_MESSAGE);
        int expectedGroupSize = scanner.nextInt();
        List<int[]> groups = null;
        groups = groupsJdbcDao.getGroupsBySize(expectedGroupSize);
        groups.forEach(g -> System.out.println("Group " + g[0] + " : " + g[1] + " students"));
        System.out.println();
    }

    private void findStudentsRelatedToCourses() throws DAOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println(STUDENTS_RELATED_TO_COURSES_MESSAGE);
        String courseName = scanner.nextLine();
        List<String[]> strings = null;
        strings = studentsCoursesJdbcDao.getStudentsRelatedToCourses(courseName);
        strings.forEach(n -> System.out.println(n[0] + SPACE + n[1]));
        System.out.println();
    }

    private void addNewStudent() throws DAOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println(ADD_NEW_STUDENT_ID_MESSAGE);
        int groupId = Integer.parseInt(scanner.nextLine());
        System.out.println(ADD_NEW_STUDENT_FIRST_NAME_MESSAGE);
        String firstName = scanner.nextLine();
        System.out.println(ADD_NEW_STUDENT_LAST_NAME_MESSAGE);
        String lastName = scanner.nextLine();
        studentsJdbcDao.create(firstName + SPACE + lastName);
    }

    private void deleteStudentById() throws DAOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println(DELETE_STUDENT_BY_ID_MESSAGE);
        int studentId = scanner.nextInt();
        studentsCoursesJdbcDao.deleteById(studentId);
        studentsJdbcDao.deleteById(studentId);
    }

    private void assignStudentToCourse() throws DAOException, IOException {
        int[] studentInfo = collectInfoForQuery();
        studentsCoursesJdbcDao.create(studentInfo[0] + " " + studentInfo[1]);
    }

    private void removeStudentFromCourse() throws DAOException, IOException {
        int[] studentInfo = collectInfoForQuery();
        studentsCoursesJdbcDao.deleteFromCourse(studentInfo[0], studentInfo[1]);
    }

    private int[] collectInfoForQuery() throws DAOException {
        int[] info = new int[2];
        Scanner scanner = new Scanner(System.in);
        System.out.println(STUDENT_ID_MESSAGE);
        info[0] = scanner.nextInt();
        coursesJdbcDao.getCoursesList().forEach((id, name) -> System.out.println("Course id: " + id + " - " + name));
        System.out.println(COURSE_ID_MESSAGE);
        info[1] = scanner.nextInt();
        return info;
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
}

package ua.com.foxminded.facade;

import ua.com.foxminded.DataGenerator;
import ua.com.foxminded.dao.CoursesDao;
import ua.com.foxminded.dao.GroupsDao;
import ua.com.foxminded.dao.StudentsCoursesDAO;
import ua.com.foxminded.dao.StudentsDAO;

import java.util.*;

import static ua.com.foxminded.sql.Queries.*;

public class Facade {

    private final DataGenerator dataGenerator;
    private final CoursesDao coursesDao;
    private final GroupsDao groupsDao;
    private final StudentsDAO studentsDAO;
    private final StudentsCoursesDAO studentsCoursesDAO;

    private final static String INTRO_MESSAGE = "To add new student to database type \"add\"\n" +
                                                "To delete student by id type \"delete\"\n" +
                                                "To add student to course type \"assign\"\n" +
                                                "To close app type \"exit\"";
    private final static String ADD_NEW_STUDENT_ID_MESSAGE =
            "Type the number to which you want to assign the student and press \"Enter\" button\n" +
            "To not assign a student to the course type \"0\" and press \"Enter\" button";
    private final static String ADD_NEW_STUDENT_FIRST_NAME_MESSAGE =
            "Type the student first name and press \"Enter\" button";
    private final static String ADD_NEW_STUDENT_LAST_NAME_MESSAGE =
            "Type the student last name and press \"Enter\" button";
    private final static String DELETE_STUDENT_BY_ID_MESSAGE =
            "Type the id of student you wand delete from database and press \"Enter\" button";
    private final static String ADD_STUDENT_TO_COURSE_STUDENT_ID_MESSAGE =
            "Type the student id and press \"Enter\" button";
    private final static String ADD_STUDENT_TO_COURSE_COURSE_ID_MESSAGE =
            "Type the course id and press \"Enter\" button";

    public Facade(DataGenerator dataGenerator,
                  CoursesDao coursesDao,
                  GroupsDao groupsDao,
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
        List<String[]> fullNamesList = dataGenerator.generateFullNamesList(firstNames, lastNames);
        List<String[]> studentsJournal = dataGenerator.assignStudentsToGroups(groups, fullNamesList);
        List<int[]> assignations = dataGenerator.assignStudentsToCourses(studentsJournal, courses);

        groups.forEach(groupsDao::create);
        courses.forEach(coursesDao::create);
        studentsJournal.forEach(s -> studentsDAO.create(Integer.parseInt(s[0]), s[1], s[2]));
        assignations.forEach(a -> studentsCoursesDAO.create(a[0], a[1]));
    }

    public void workWithDataBase() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(INTRO_MESSAGE);
        String command = scanner.nextLine();
        while (!command.equals("exit")) {
            switch (command) {
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
            }
        }
    }

    private void addNewStudent() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(ADD_NEW_STUDENT_ID_MESSAGE);
        int groupId = Integer.parseInt(scanner.nextLine());
        System.out.println(ADD_NEW_STUDENT_FIRST_NAME_MESSAGE);
        String firstName = scanner.nextLine();
        System.out.println(ADD_NEW_STUDENT_LAST_NAME_MESSAGE);
        String lastName = scanner.nextLine();
        studentsDAO.create(groupId, firstName, lastName);
    }

    private void deleteStudentById() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(DELETE_STUDENT_BY_ID_MESSAGE);
        int studentId = scanner.nextInt();
        studentsCoursesDAO.deleteById(studentId);
        studentsDAO.deleteById(studentId);
    }

    private void assignStudentToCourse() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(ADD_STUDENT_TO_COURSE_STUDENT_ID_MESSAGE);
        int studentsId = scanner.nextInt();
        coursesDao.getCoursesList().forEach((id, name) -> System.out.println("Course id: " + id + " - " + name));
        System.out.println(ADD_STUDENT_TO_COURSE_COURSE_ID_MESSAGE);
        int courseId = scanner.nextInt();
        studentsCoursesDAO.create(studentsId, courseId);
    }
}

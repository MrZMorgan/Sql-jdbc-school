package ua.com.foxminded.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.foxminded.DataGenerator;
import ua.com.foxminded.connection.ConnectionFactory;
import ua.com.foxminded.exceptions.DAOException;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class StudentsCoursesJdbcDaoTest {

    private final static String CONNECTION_PROPERTIES = "resources/h2_connection.properties";
    public static final String SQL_RESOURCES = "resources/sql.properties";
    private final ConnectionFactory factory = new ConnectionFactory(CONNECTION_PROPERTIES);
    private final StudentsCoursesJdbcDao studentsCoursesJdbcDao = new StudentsCoursesJdbcDao(factory);
    private final CoursesJdbcDao coursesJdbcDao = new CoursesJdbcDao(factory);
    private final StudentsJdbcDao studentsJdbcDao = new StudentsJdbcDao(factory);
    private final DataGenerator generator = new DataGenerator(factory);
    Connection connection = null;
    Statement statement = null;

    @BeforeEach
    void createTable() {
        Properties properties = new Properties();
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(SQL_RESOURCES);
            properties.load(stream);
            generator.generateTable(
                    properties.getProperty("drop.courses.table"),
                    properties.getProperty("create.courses.table.h2.database"));
            generator.generateTable(
                    properties.getProperty("drop.students.table"),
                    properties.getProperty("create.students.table.h2.database"));
            generator.generateTable(
                    properties.getProperty("drop.students.courses.table"),
                    properties.getProperty("create.students.courses.table"));

            connection = factory.connect();
            statement = connection.createStatement();
        } catch (DAOException | IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void shouldCreateRecordInTable() {
        try {
            generateTestData();

            List<String[]> studentsCourses = studentsCoursesJdbcDao.readAllData();
            int expectedStudentId = Integer.parseInt(studentsCourses.get(0)[0]);
            int expectedCourseId = Integer.parseInt(studentsCourses.get(0)[1]);;

            int actualStudentId = 1;
            int actualCourseId = 1;

            assertEquals(expectedStudentId, actualStudentId);
            assertEquals(expectedCourseId, actualCourseId);
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void shouldDeleteById() {
        try {
            generateTestData();
            studentsCoursesJdbcDao.deleteById(1);

            int expectedTableSize = 0;

            List<String[]> studentsCourses = studentsCoursesJdbcDao.readAllData();
            int actualTableSize = studentsCourses.size();

            assertEquals(expectedTableSize, actualTableSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void deleteFromCourse() {
        try {
            generateTestData();
            studentsCoursesJdbcDao.deleteFromCourse(1, 1);

            ResultSet resultSet = statement.executeQuery("SELECT * FROM students_courses;");

            String actualCourseId = "";
            while (resultSet.next()) {
                actualCourseId = String.valueOf(resultSet.getInt("COURSE_ID"));
            }

            assertEquals("", actualCourseId);
            connection.close();
        } catch (DAOException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getStudentsRelatedToCourses() {
        try {
            generateDataForGetStudentsRelatedToCourses();
            List<String[]> data = studentsCoursesJdbcDao.getStudentsRelatedToCourses("math");

            String[] student1 = {"Mikel", "Legg"};
            String[] student2 = {"Mikel", "Deetlefs"};

            assertEquals(student1[0] + student1[1], data.get(0)[0] + data.get(0)[1]);
            assertEquals(student2[0] + student2[1], data.get(1)[0] + data.get(1)[1]);

        } catch (DAOException e) {
            e.printStackTrace();
        }
    }

    void generateTestData() throws DAOException {
        coursesJdbcDao.create("math");
        studentsJdbcDao.create("Egor Anchutin");
        studentsCoursesJdbcDao.create("1 1");
    }

    void generateDataForGetStudentsRelatedToCourses() throws DAOException {
        studentsJdbcDao.create("Mikel Legg");
        studentsJdbcDao.create("Fania Battram");
        studentsJdbcDao.create("Mikel Deetlefs");
        studentsJdbcDao.create("Gunther Skedgell");
        studentsJdbcDao.create("Reed Rentoll");
        studentsJdbcDao.create("Kylie Godfroy");
        studentsJdbcDao.create("Enrique Laurence");
        studentsJdbcDao.create("Enrique Soal");
        studentsJdbcDao.create("Jerrie Josefsson");
        studentsJdbcDao.create("Leyla Skedgell");

        coursesJdbcDao.create("math");
        coursesJdbcDao.create("economy");

        studentsCoursesJdbcDao.create("1 1");
        studentsCoursesJdbcDao.create("2 2");
        studentsCoursesJdbcDao.create("3 1");
        studentsCoursesJdbcDao.create("4 2");
        studentsCoursesJdbcDao.create("5 2");
        studentsCoursesJdbcDao.create("6 2");
        studentsCoursesJdbcDao.create("7 2");
        studentsCoursesJdbcDao.create("8 2");
        studentsCoursesJdbcDao.create("9 2");
        studentsCoursesJdbcDao.create("10 2");
    }
}
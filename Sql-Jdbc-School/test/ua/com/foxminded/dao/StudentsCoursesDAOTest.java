package ua.com.foxminded.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.foxminded.DataGenerator;
import ua.com.foxminded.connection.ConnectionFactory;
import ua.com.foxminded.dao.CoursesDAO;
import ua.com.foxminded.dao.StudentsCoursesDAO;
import ua.com.foxminded.dao.StudentsDAO;
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

class StudentsCoursesDAOTest {

    private final static String CONNECTION_PROPERTIES = "resources/h2_connection.properties";
    public static final String SQL_RESOURCES = "resources/sql.properties";
    private final StudentsCoursesDAO studentsCoursesDAO = new StudentsCoursesDAO(CONNECTION_PROPERTIES);
    private final CoursesDAO coursesDAO = new CoursesDAO(CONNECTION_PROPERTIES);
    private final StudentsDAO studentsDAO = new StudentsDAO(CONNECTION_PROPERTIES);
    private final ConnectionFactory factory = new ConnectionFactory(CONNECTION_PROPERTIES);
    private final DataGenerator generator = new DataGenerator(CONNECTION_PROPERTIES);
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
            ResultSet resultSet = statement.executeQuery("SELECT * FROM students_courses;");
            String student_id = "";
            String course_id = "";
            while (resultSet.next()) {
                student_id = String.valueOf(resultSet.getInt("STUDENT_ID"));
                course_id = String.valueOf(resultSet.getInt("COURSE_ID"));
            }
            assertEquals("1", student_id);
            assertEquals("1", course_id);
            connection.close();
        } catch (DAOException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void shouldDeleteById() {
        try {
            generateTestData();
            studentsCoursesDAO.deleteById(1);

            int expectedTableSize = 0;

            ResultSet resultSet = statement.executeQuery("SELECT * FROM students_courses;");

            int actualTableSize = 0;
            while (resultSet.next()) {
                actualTableSize++;
            }

            assertEquals(expectedTableSize, actualTableSize);
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void deleteFromCourse() {
        try {
            generateTestData();
            studentsCoursesDAO.deleteFromCourse(1, 1);

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
            List<String[]> data = studentsCoursesDAO.getStudentsRelatedToCourses("math");

            String[] student1 = {"Mikel", "Legg"};
            String[] student2 = {"Mikel", "Deetlefs"};

            assertEquals(student1[0] + student1[1], data.get(0)[0] + data.get(0)[1]);
            assertEquals(student2[0] + student2[1], data.get(1)[0] + data.get(1)[1]);

        } catch (DAOException e) {
            e.printStackTrace();
        }
    }

    void generateTestData() throws DAOException {
        coursesDAO.create("math");
        studentsDAO.create("Egor Anchutin");
        studentsCoursesDAO.create("1 1");
    }

    void generateDataForGetStudentsRelatedToCourses() throws DAOException {
        studentsDAO.create("Mikel Legg");
        studentsDAO.create("Fania Battram");
        studentsDAO.create("Mikel Deetlefs");
        studentsDAO.create("Gunther Skedgell");
        studentsDAO.create("Reed Rentoll");
        studentsDAO.create("Kylie Godfroy");
        studentsDAO.create("Enrique Laurence");
        studentsDAO.create("Enrique Soal");
        studentsDAO.create("Jerrie Josefsson");
        studentsDAO.create("Leyla Skedgell");

        coursesDAO.create("math");
        coursesDAO.create("economy");

        studentsCoursesDAO.create("1 1");
        studentsCoursesDAO.create("2 2");
        studentsCoursesDAO.create("3 1");
        studentsCoursesDAO.create("4 2");
        studentsCoursesDAO.create("5 2");
        studentsCoursesDAO.create("6 2");
        studentsCoursesDAO.create("7 2");
        studentsCoursesDAO.create("8 2");
        studentsCoursesDAO.create("9 2");
        studentsCoursesDAO.create("10 2");
    }
}
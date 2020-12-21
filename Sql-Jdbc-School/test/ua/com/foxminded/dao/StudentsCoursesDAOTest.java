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
        } catch (DAOException | IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void shouldCreateRecordInTable() {
        try {
            coursesDAO.create("math");
            studentsDAO.create("Egor Anchutin");
            studentsCoursesDAO.create("1 1");

            connection = factory.connect();
            statement = connection.createStatement();
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
        } catch (DAOException | IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

//    @Test
//    void deleteById() {
//    }
//
//    @Test
//    void deleteFromCourse() {
//    }
//
//    @Test
//    void getStudentsRelatedToCourses() {
//    }
}
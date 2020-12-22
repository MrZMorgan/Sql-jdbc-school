package ua.com.foxminded.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.foxminded.DataGenerator;
import ua.com.foxminded.connection.ConnectionFactory;
import ua.com.foxminded.exceptions.DAOException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CoursesJdbcDaoTest {
    private final ConnectionFactory factory = new ConnectionFactory(CONNECTION_PROPERTIES);
    private final CoursesJdbcDao courseDao = new CoursesJdbcDao(factory);
    private final DataGenerator generator = new DataGenerator(factory);
    private final static String CONNECTION_PROPERTIES = "resources/h2_connection.properties";
    private final static String SQL_RESOURCES = "resources/sql.properties";
    private final static String COURSE_NAME_MATH = "math";
    private final static String COURSE_NAME_GEOMETRY = "geometry";
    private final static String COURSE_NAME_BIOLOGY = "biology";

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
        } catch (DAOException | IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void shouldCreateNewCourse() {
        try {
            courseDao.create(COURSE_NAME_MATH);

            int courseId = 0;
            String courseName = "";
            List<String[]> courses = courseDao.readAllData();
            for (String[] course : courses) {
                courseId = Integer.parseInt(course[0]);
                courseName = course[1];
            }

            assertEquals(1, courseId);
            assertEquals(COURSE_NAME_MATH, courseName);
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void shouldGetCoursesList() {
        try {
            courseDao.create(COURSE_NAME_MATH);
            courseDao.create(COURSE_NAME_GEOMETRY);
            courseDao.create(COURSE_NAME_BIOLOGY);

            List<String[]> actualCoursesList = courseDao.readAllData();

            assertEquals(1, Integer.parseInt(actualCoursesList.get(0)[0]));
            assertEquals(COURSE_NAME_MATH, actualCoursesList.get(0)[1]);

            assertEquals(2, Integer.parseInt(actualCoursesList.get(1)[0]));
            assertEquals(COURSE_NAME_GEOMETRY, actualCoursesList.get(1)[1]);

            assertEquals(3, Integer.parseInt(actualCoursesList.get(2)[0]));
            assertEquals(COURSE_NAME_BIOLOGY, actualCoursesList.get(2)[1]);
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }
}
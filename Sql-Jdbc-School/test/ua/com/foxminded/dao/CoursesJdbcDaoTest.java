package ua.com.foxminded.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.foxminded.DataGenerator;
import ua.com.foxminded.connection.ConnectionFactory;
import ua.com.foxminded.dao.data.Course;
import ua.com.foxminded.exceptions.DAOException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CoursesJdbcDaoTest {
    private final static String CONNECTION_PROPERTIES = "resources/h2_connection.properties";
    private final static String SQL_RESOURCES = "resources/sql.properties";
    private final static String COURSE_NAME_MATH = "math";
    private final static String COURSE_NAME_GEOMETRY = "geometry";
    private final static String COURSE_NAME_BIOLOGY = "biology";
    private final ConnectionFactory factory = new ConnectionFactory(CONNECTION_PROPERTIES);
    private final CoursesJdbcDao courseDao = new CoursesJdbcDao(factory);
    private final DataGenerator generator = new DataGenerator(factory);

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
            int actualCourseId = 1;
            courseDao.create(COURSE_NAME_MATH);

            List<Course> courses = courseDao.readAllData();
            int expectedCourseId = courses.get(0).getId();
            String expectedCourseName = courses.get(0).getName();

            assertEquals(actualCourseId, expectedCourseId);
            assertEquals(COURSE_NAME_MATH, expectedCourseName);
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

            List<Course> actualCoursesList = courseDao.readAllData();

            Course actualCourse1 = actualCoursesList.get(0);
            Course actualCourse2 = actualCoursesList.get(1);
            Course actualCourse3 = actualCoursesList.get(2);

            int actualCourse1Id = actualCourse1.getId();
            int actualCourse2Id = actualCourse2.getId();
            int actualCourse3Id = actualCourse3.getId();

            String actualCourse1Name = actualCourse1.getName();
            String actualCourse2Name = actualCourse2.getName();
            String actualCourse3Name = actualCourse3.getName();

            int expectedCourse1Id = 1;
            int expectedCourse2Id = 2;
            int expectedCourse3Id = 3;

            assertEquals(expectedCourse1Id, actualCourse1Id);
            assertEquals(COURSE_NAME_MATH, actualCourse1Name);

            assertEquals(expectedCourse2Id, actualCourse2Id);
            assertEquals(COURSE_NAME_GEOMETRY, actualCourse2Name);

            assertEquals(expectedCourse3Id, actualCourse3Id);
            assertEquals(COURSE_NAME_BIOLOGY, actualCourse3Name);
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }
}
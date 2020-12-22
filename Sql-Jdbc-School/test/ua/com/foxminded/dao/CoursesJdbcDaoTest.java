package ua.com.foxminded.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.foxminded.DataGenerator;
import ua.com.foxminded.connection.ConnectionFactory;
import ua.com.foxminded.exceptions.DAOException;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CoursesJdbcDaoTest {
    private final ConnectionFactory factory = new ConnectionFactory(CONNECTION_PROPERTIES);
    private final CoursesJdbcDao courseDao = new CoursesJdbcDao(factory);
    private final DataGenerator generator = new DataGenerator(factory);
    private Connection connection = null;
    private Statement statement = null;
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
            connection = factory.connect();
            statement = connection.createStatement();

            int courseId = 0;
            String courseName = "";
            Map<Integer, String> courses = courseDao.getCoursesList();
            for (Map.Entry<Integer, String> entry : courses.entrySet()) {
                courseId = entry.getKey();
                courseName = entry.getValue();
            }

            assertEquals(1, courseId);
            assertEquals(COURSE_NAME_MATH, courseName);
            connection.close();
        } catch (DAOException | IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    void shouldGetCoursesList() {
        Map<Integer, String> expectedGroupsMap = new LinkedHashMap<>();
        expectedGroupsMap.put(1, COURSE_NAME_MATH);
        expectedGroupsMap.put(2, COURSE_NAME_GEOMETRY);
        expectedGroupsMap.put(3, COURSE_NAME_BIOLOGY);
        try {
            courseDao.create(COURSE_NAME_MATH);
            courseDao.create(COURSE_NAME_GEOMETRY);
            courseDao.create(COURSE_NAME_BIOLOGY);

            Map<Integer, String> actualCoursesMap = courseDao.getCoursesList();
            assertEquals(expectedGroupsMap, actualCoursesMap);
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }
}
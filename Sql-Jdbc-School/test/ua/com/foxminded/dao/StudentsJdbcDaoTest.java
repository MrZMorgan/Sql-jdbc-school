package ua.com.foxminded.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.foxminded.DataGenerator;
import ua.com.foxminded.connection.ConnectionFactory;
import ua.com.foxminded.exceptions.DAOException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class StudentsJdbcDaoTest {
    private final static String CONNECTION_PROPERTIES = "resources/h2_connection.properties";
    private final static String SQL_RESOURCES = "resources/sql.properties";
    private final static String FIRST_NAME_FOR_TEST = "Egor";
    private final static String LAST_NAME_FOR_TEST = "Anchutin";
    private final static String FULL_NAME_1_FOR_TEST = "Egor Anchutin";
    private final static String FULL_NAME_2_FOR_TEST = "Zach Morgan";
    private final static String FULL_NAME_3_FOR_TEST = "York Morgan";
    private final ConnectionFactory factory = new ConnectionFactory(CONNECTION_PROPERTIES);
    private final StudentsJdbcDao studentsDao = new StudentsJdbcDao(factory);
    private final DataGenerator generator = new DataGenerator(factory);

    @BeforeEach
    void createTable() {
        Properties properties = new Properties();
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(SQL_RESOURCES);
            properties.load(stream);
            generator.generateTable(
                    properties.getProperty("drop.students.table"),
                    properties.getProperty("create.students.table.h2.database"));
        } catch (DAOException | IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void shouldCreateStudent() {
        try {
            int actualId = 1;
            studentsDao.create(FULL_NAME_1_FOR_TEST);

            List<String[]> students = studentsDao.readAllData();

            int expectedId = Integer.parseInt(students.get(0)[0]);
            String expectedFirstName = students.get(0)[2];
            String expectedLastName = students.get(0)[3];

            assertEquals(actualId, expectedId);
            assertEquals(FIRST_NAME_FOR_TEST, expectedFirstName);
            assertEquals(LAST_NAME_FOR_TEST, expectedLastName);
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void shouldDeleteStudentById() {
        try {
            studentsDao.create(FULL_NAME_1_FOR_TEST);
            studentsDao.create(FULL_NAME_2_FOR_TEST);
            studentsDao.create(FULL_NAME_3_FOR_TEST);
            studentsDao.deleteById(1);

            int expectedTableSize = 2;
            List<String[]> students = studentsDao.readAllData();

            assertEquals(expectedTableSize, students.size());
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void shouldAssignStudentToGroup() {
        try {
            int expectedGroupId = 1;
            int studentId = 1;

            studentsDao.create(FULL_NAME_1_FOR_TEST);
            studentsDao.assignStudentToGroup(expectedGroupId, studentId);

            List<String[]> students = studentsDao.readAllData();
            int actualGroupId = Integer.parseInt(students.get(0)[1]);

            assertEquals(expectedGroupId, actualGroupId);
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }
}
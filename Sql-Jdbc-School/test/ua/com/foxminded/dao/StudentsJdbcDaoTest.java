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

class StudentsJdbcDaoTest {
    private final ConnectionFactory factory = new ConnectionFactory(CONNECTION_PROPERTIES);
    private final StudentsJdbcDao studentsDao = new StudentsJdbcDao(factory);
    private final DataGenerator generator = new DataGenerator(factory);
    private final static String CONNECTION_PROPERTIES = "resources/h2_connection.properties";
    private final static String SQL_RESOURCES = "resources/sql.properties";
    private final static String FIRST_NAME_FOR_TEST = "Egor";
    private final static String LAST_NAME_FOR_TEST = "Anchutin";
    private final static String FULL_NAME_1_FOR_TEST = "Egor Anchutin";
    private final static String FULL_NAME_2_FOR_TEST = "Zach Morgan";
    private final static String FULL_NAME_3_FOR_TEST = "York Morgan";
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
                    properties.getProperty("drop.students.table"),
                    properties.getProperty("create.students.table.h2.database"));
            connection = factory.connect();
            statement = connection.createStatement();
        } catch (DAOException | IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    void shouldCreateStudent() {
        try {
            studentsDao.create(FULL_NAME_1_FOR_TEST);

            int id = 0;
            String firstName = "";
            String lastName = "";

            List<String[]> students = studentsDao.readAllData();

            for (String[] studentInfo : students) {
                id = Integer.parseInt(studentInfo[0]);
                firstName = studentInfo[2];
                lastName = studentInfo[3];
            }

            assertEquals(1, id);
            assertEquals(FIRST_NAME_FOR_TEST, firstName);
            assertEquals(LAST_NAME_FOR_TEST, lastName);
            connection.close();
        } catch (DAOException | SQLException e) {
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
            connection.close();
        } catch (DAOException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void shouldAssignStudentToGroup() {
        try {
            int groupId = 1;
            int studentId = 1;
            studentsDao.create(FULL_NAME_1_FOR_TEST);
            studentsDao.assignStudentToGroup(groupId, studentId);

            ResultSet resultSet = statement.executeQuery("SELECT * FROM students;");

            int actualGroupId = 0;
            while (resultSet.next()) {
                actualGroupId = resultSet.getInt("GROUP_ID");
                System.out.println(groupId);
            }
            assertEquals(groupId, actualGroupId);
            connection.close();
        } catch (DAOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
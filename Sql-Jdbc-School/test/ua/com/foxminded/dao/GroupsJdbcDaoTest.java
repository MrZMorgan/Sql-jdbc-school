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

class GroupsJdbcDaoTest {
    private final static String CONNECTION_PROPERTIES = "resources/h2_connection.properties";
    public static final String SQL_RESOURCES = "resources/sql.properties";
    private final GroupsJdbcDao groupsJdbcDao = new GroupsJdbcDao(CONNECTION_PROPERTIES);
    private final StudentsJdbcDao studentsJdbcDao = new StudentsJdbcDao(CONNECTION_PROPERTIES);
    private final StudentsCoursesJdbcDao studentsCoursesJdbcDao = new StudentsCoursesJdbcDao(CONNECTION_PROPERTIES);
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
                    properties.getProperty("drop.groups.table"),
                    properties.getProperty("create.groups.table.h2.database"));
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
    void shouldCreateGroup() {
        try {
            groupsJdbcDao.create("gs-58");

            connection = factory.connect();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM groups;");
            String id = "";
            String name = "";
            while (resultSet.next()) {
                id = resultSet.getString("ID");
                name = resultSet.getString("NAME");
            }
            assertEquals("1", id);
            assertEquals("gs-58", name);
            connection.close();
        } catch (DAOException | IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getGroupsBySize() {
        try {
            generateData();

            List<int[]> actualGroupsSizeList = groupsJdbcDao.getGroupsBySize(3);

            int groupId = 2;
            int groupSize = 2;

            assertEquals(groupId, actualGroupsSizeList.get(0)[0]);
            assertEquals(groupSize, actualGroupsSizeList.get(0)[1]);
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void generateData() throws DAOException {
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

        studentsJdbcDao.assignStudentToGroup(1, 1);
        studentsJdbcDao.assignStudentToGroup(1, 2);
        studentsJdbcDao.assignStudentToGroup(1, 3);
        studentsJdbcDao.assignStudentToGroup(2, 4);
        studentsJdbcDao.assignStudentToGroup(2, 5);
        studentsJdbcDao.assignStudentToGroup(3, 6);
        studentsJdbcDao.assignStudentToGroup(3, 7);
        studentsJdbcDao.assignStudentToGroup(3, 8);
        studentsJdbcDao.assignStudentToGroup(3, 9);
        studentsJdbcDao.assignStudentToGroup(1, 10);
    }
}
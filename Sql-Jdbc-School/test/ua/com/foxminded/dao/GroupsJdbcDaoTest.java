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

class GroupsJdbcDaoTest {
    private final ConnectionFactory factory = new ConnectionFactory(CONNECTION_PROPERTIES);
    private final GroupsJdbcDao groupsJdbcDao = new GroupsJdbcDao(factory);
    private final StudentsJdbcDao studentsJdbcDao = new StudentsJdbcDao(factory);
    private final DataGenerator generator = new DataGenerator(factory);
    private final static String CONNECTION_PROPERTIES = "resources/h2_connection.properties";
    private final static String SQL_RESOURCES = "resources/sql.properties";
    private final static String GROUP_NAME_FOR_TEST = "gs-58";

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
            groupsJdbcDao.create(GROUP_NAME_FOR_TEST);
            int actualGroupId = 1;

            List<String[]> groups = groupsJdbcDao.readAllData();
            int expectedGroupsId = Integer.parseInt(groups.get(0)[0]);
            String expectedGroupsName = groups.get(0)[1];

            assertEquals(actualGroupId, expectedGroupsId);
            assertEquals(GROUP_NAME_FOR_TEST, expectedGroupsName);
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getGroupsBySize() {
        try {
            generateData();
            int actualGroupId = 2;
            int actualGroupSize = 2;
            int expectedGroupSizeForTest = 3;

            List<int[]> actualGroupsSizeList = groupsJdbcDao.getGroupsBySize(expectedGroupSizeForTest);
            int expectedGroupId = actualGroupsSizeList.get(0)[0];
            int expectedGroupSize = actualGroupsSizeList.get(0)[1];

            assertEquals(actualGroupId, expectedGroupId);
            assertEquals(actualGroupSize, expectedGroupSize);
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
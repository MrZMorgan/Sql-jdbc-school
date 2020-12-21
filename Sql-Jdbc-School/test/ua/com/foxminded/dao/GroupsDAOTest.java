package ua.com.foxminded.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.foxminded.DataGenerator;
import ua.com.foxminded.connection.ConnectionFactory;
import ua.com.foxminded.dao.CoursesDAO;
import ua.com.foxminded.dao.GroupsDAO;
import ua.com.foxminded.exceptions.DAOException;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class GroupsDAOTest {
    private final static String CONNECTION_PROPERTIES = "resources/h2_connection.properties";
    public static final String SQL_RESOURCES = "resources/sql.properties";
    private final GroupsDAO groupsDAO = new GroupsDAO(CONNECTION_PROPERTIES);
    private final StudentsDAO studentsDAO = new StudentsDAO(CONNECTION_PROPERTIES);
    private final StudentsCoursesDAO studentsCoursesDAO = new StudentsCoursesDAO(CONNECTION_PROPERTIES);
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
            groupsDAO.create("gs-58");

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

            studentsDAO.assignStudentToGroup(1, 1);
            studentsDAO.assignStudentToGroup(1, 2);
            studentsDAO.assignStudentToGroup(1, 3);
            studentsDAO.assignStudentToGroup(2, 4);
            studentsDAO.assignStudentToGroup(2, 5);
            studentsDAO.assignStudentToGroup(3, 6);
            studentsDAO.assignStudentToGroup(3, 7);
            studentsDAO.assignStudentToGroup(3, 8);
            studentsDAO.assignStudentToGroup(3, 9);
            studentsDAO.assignStudentToGroup(1, 10);


            List<int[]> actual = groupsDAO.getGroupsBySize(3);
            List<int[]> expected = new LinkedList<>();
            expected.add(new int[] {2, 2});

            assertEquals(expected, actual);
            connection.close();
        } catch (DAOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
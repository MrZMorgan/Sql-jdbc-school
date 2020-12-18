package ua.com.foxminded;

import ua.com.foxminded.connection.ConnectionFactory;
import ua.com.foxminded.dao.StudentsCoursesDAO;
import ua.com.foxminded.dao.StudentsDAO;
import ua.com.foxminded.exceptions.DAOException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class DataGenerator {

    private static final String HYPHEN = "-";
    public static final String SQL_RESOURCES = "resources/sql.properties";
    private final static Logger logger = Logger.getLogger(DataGenerator.class.getName());
    private static final String logFilePath = "/home/egor/Документы/repositorys/sql--jdbc-school/Sql-Jdbc-School/logs/data_generator/dg_log.log";


    public void generateTable(String sqlDrop, String sqlCreate) throws DAOException, IOException {
        Connection connection = null;
        Statement statement = null;
        Properties properties = new Properties();
        ConnectionFactory factory = new ConnectionFactory();
        try {
            FileInputStream stream = new FileInputStream(SQL_RESOURCES);
            properties.load(stream);
            stream.close();

            connection = factory.connect();
            statement = connection.createStatement();
            try {
                statement.executeQuery(sqlDrop);
            } catch (Exception e) {
                e.printStackTrace();
            }
            statement.executeQuery(sqlCreate);
        } catch (ClassNotFoundException | SQLException | IOException e) {
            factory.log(logFilePath, logger, e);
        } finally {
            factory.close(connection);
        }
    }

    private String generateGroupName(int digit) {
        return String.valueOf(generateRandomLetter()) +
                generateRandomLetter() +
                HYPHEN +
                generateRandomDigit(digit) +
                generateRandomDigit(digit);
    }

    public List<String> generateGroupsNamesList() {
        List<String> groups = new LinkedList<>();

        for (int i = 0; i < 10; i++) {
            groups.add(generateGroupName(10));
        }

        return groups;
    }

    public int generateRandomDigit(int digit) {
        return new Random().nextInt(digit);
    }

    private char generateRandomLetter() {
        return (char) (new Random().nextInt(26) + 'a');
    }

    public List<String> readFile(String fileName) throws IOException {
        return Files.lines(Paths.get(fileName)).collect(Collectors.toList());
    }

    public List<String> generateFullNamesList(List<String> firstNames, List<String> lastNames) {
        List<String> fullNamesList = new LinkedList<>();

        for (int i = 0; i < 200; i++) {
            String firstName = firstNames.get(generateRandomDigit(firstNames.size()));
            String lastName = lastNames.get(generateRandomDigit(lastNames.size()));
            String fullName = firstName + " " + lastName;
            fullNamesList.add(fullName);
        }

        return fullNamesList;
    }

    public List<int[]> assignStudentsToGroups(List<String> groups,
                                       List<String> fullNamesList) {
        List<int[]> data = new LinkedList<>();
        int totalGroupSize = 0;

        for (int i = 0; i < groups.size(); i++) {
            int groupSize = generateRandomDigit(21) + 10;
            for (int j = 0; j < groupSize; j++) {
                if (totalGroupSize == 200) {
                    break ;
                } else {
                    data.add(new int[] {totalGroupSize + 1, i + 1});
                    totalGroupSize++;
                }
            }
        }

        if (totalGroupSize < fullNamesList.size()) {
            while (totalGroupSize < 200) {
                data.add(new int[] {totalGroupSize + 1, 0});
                totalGroupSize++;
            }
        }

        return data;
    }

    public List<String> assignStudentsToCourses(List<String> studentsJournal, List<String> courses) {
        List<String> assignations = new LinkedList<>();

        for (int i = 0; i < studentsJournal.size(); i++) {
            int numberOfCourses = new Random().nextInt(3) + 1;
            int[] list = new int[numberOfCourses];
            int course = new Random().nextInt(courses.size() + 1);
            list[0] = course;
            assignations.add(i + 1 + " " + course);

            for (int j = 0; j < numberOfCourses - 1; j++) {
                course = new Random().nextInt(courses.size() + 1);
                while (course == list[j]) {
                    course = new Random().nextInt(courses.size() + 1);
                }
                list[j + 1] = course;
                assignations.add(i + 1 + " " + course);
            }
        }

        return assignations;
    }
}

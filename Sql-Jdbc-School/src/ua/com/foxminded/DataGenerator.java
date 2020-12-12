package ua.com.foxminded;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

public class DataGenerator {

    private static final String HYPHEN = "-";
    private static final String user = "postgres";
    private static final String password = "1234";
    private static final String url = "jdbc:postgresql://localhost:5432/school";

    public void generateTable(String sqlDrop, String sqlCreate) {
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
            try {
                statement.executeQuery(sqlDrop);
            } catch (Exception e) {
                e.printStackTrace();
            }
            statement.executeQuery(sqlCreate);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    private String generateGroupName(int digit) {
        StringBuilder group = new StringBuilder();
        group.append(generateRandomLetter());
        group.append(generateRandomLetter());
        group.append(HYPHEN);
        group.append(generateRandomDigit(digit));
        group.append(generateRandomDigit(digit));
        return group.toString();
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

    public List<String> readFile(String fileName) {
        List<String> strings = new LinkedList<>();

        try {
            strings = Files.lines(Paths.get(fileName)).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return strings;
    }

    public List<String[]> generateFullNamesList(List<String> firstNames, List<String> lastNames) {
        List<String[]> fullNamesList = new LinkedList<>();

        for (int i = 0; i < 200; i++) {
            String firstName = firstNames.get(generateRandomDigit(firstNames.size()));
            String lastName = lastNames.get(generateRandomDigit(lastNames.size()));
            String[] fullName = {firstName, lastName};
            fullNamesList.add(fullName);
        }

        return fullNamesList;
    }

    public List<String[]> assignStudentsToGroups(List<String> groups, List<String[]> fullNamesList) {
        List<String[]> studentsJournal = new ArrayList<>();
        int totalGroupSize = 0;

        for (int i = 0; i < groups.size(); i++) {
            int groupSize = generateRandomDigit(21) + 10;
            for (int j = 0; j < groupSize; j++) {
                if (totalGroupSize == 200) {
                    break ;
                } else {
                    String[] studentData = {String.valueOf(i + 1),
                            fullNamesList.get(totalGroupSize)[0], fullNamesList.get(totalGroupSize)[1]};
                    studentsJournal.add(studentData);
                    totalGroupSize++;
                }
            }
        }

        if (totalGroupSize < fullNamesList.size()) {
            while (totalGroupSize < 200) {
                String[] studentData = {String.valueOf(0),
                        fullNamesList.get(totalGroupSize)[0], fullNamesList.get(totalGroupSize)[1]};
                studentsJournal.add(studentData);
                totalGroupSize++;
            }
        }

        return studentsJournal;
    }
}

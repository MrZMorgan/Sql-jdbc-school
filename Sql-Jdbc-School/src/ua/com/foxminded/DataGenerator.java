package ua.com.foxminded;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class DataGenerator {

    private static String HYPHEN = "-";
    private static String SPACE = " ";
    private static String user = "postgres";
    private static String password = "1234";
    private static String url = "jdbc:postgresql://localhost:5432/school";

    public void generateTables(String sqlCreate, String sqlDrop) throws SQLException {
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }

    public List<String> generateGroupsNamesList() {
        List<String> groups = new LinkedList<>();

        for (int i = 0; i < 10; i++) {
            groups.add(generateGroupName(10));
        }

        return groups;
    }

    public String generateGroupName(int digit) {
        StringBuilder group = new StringBuilder();
        group.append(generateRandomLetter());
        group.append(generateRandomLetter());
        group.append(HYPHEN);
        group.append(generateRandomDigit(digit));
        group.append(generateRandomDigit(digit));
        return group.toString();
    }

    public int generateRandomDigit(int digit) {
        return new Random().nextInt(digit);
    }

    public char generateRandomLetter() {
        return (char) (new Random().nextInt(26) + 'a');
    }

    public List<String> generateNamesList(String firstNamesFileName, String lastNamesFileName) {
        List<String> firstNames = read(firstNamesFileName);
        List<String> lastNames = read(lastNamesFileName);
        List<String> names = new LinkedList<>();

        for (int i = 0; i < 200; i++) {
            names.add(firstNames.get(generateRandomDigit(firstNames.size())) + SPACE +
                    lastNames.get(generateRandomDigit(lastNames.size())));
        }

        return names;
    }

    public List<String> read(String filename) {
        List<String> strings = new LinkedList<>();

        try {
            strings = Files.lines(Paths.get(filename)).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return strings;
    }
}

package ua.com.foxminded;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, Integer> generateGroupsNamesList() {
        Map<String, Integer> groups = new LinkedHashMap<>();

        for (int i = 0; i < 10; i++) {
            groups.put(generateGroupName(10), new Random().nextInt(21) + 10);
        }

        return groups;
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

    public int generateRandomDigit(int digit) {
        return new Random().nextInt(digit);
    }

    private char generateRandomLetter() {
        return (char) (new Random().nextInt(26) + 'a');
    }

    public List<String[]> generateNamesList(String firstNamesFileName, String lastNamesFileName) {
        List<String> firstNames = read(firstNamesFileName);
        List<String> lastNames = read(lastNamesFileName);
        List<String[]> names = new LinkedList<>();

        for (int i = 0; i < 200; i++) {
            String[] fullName = {firstNames.get(generateRandomDigit(firstNames.size())),
                    lastNames.get(generateRandomDigit(lastNames.size()))};
            names.add(fullName);
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

    public Map<Integer, String[]> assignStudentsToGroups(Map<String, Integer> groupNames, List<String[]> namesList) {
        Map<Integer, String[]> namesGroups = new LinkedHashMap<>();

        int groupId = 1;
        int counter = 0;

        for (Map.Entry<String, Integer> entry : groupNames.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                if (counter == 200) {
                    break;
                }
                namesGroups.put(groupId, namesList.get(counter));
                System.out.println(groupId + "(" + (counter + 1) + ")" + " : " + namesList.get(counter)[0] + " " + namesList.get(counter)[1]);
                counter++;
            }
            groupId++;
        }

        return namesGroups;
    }
}

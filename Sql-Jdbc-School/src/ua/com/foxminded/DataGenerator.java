package ua.com.foxminded;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class DataGenerator {
    public void generateTables(String tableName, String sqlCreate, String sqlDrop) throws SQLException {
        String user = "postgres";
        String password = "1234";
        String url = "jdbc:postgresql://localhost:5432/school";
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
            groups.add(generateGroupName());
        }

        return groups;
    }

    public String generateGroupName() {
        StringBuilder group = new StringBuilder();
        group.append(generateRandomLetter());
        group.append(generateRandomLetter());
        group.append("-");
        group.append(generateRandomDigit());
        group.append(generateRandomDigit());
        return group.toString();
    }

    public int generateRandomDigit() {
        return new Random().nextInt(10);
    }

    public char generateRandomLetter() {
        return (char) (new Random().nextInt(26) + 'a');
    }
}

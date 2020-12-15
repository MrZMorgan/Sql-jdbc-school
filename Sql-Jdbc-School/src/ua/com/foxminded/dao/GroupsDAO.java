package ua.com.foxminded.dao;

import ua.com.foxminded.interfaces.GroupsDAOInterface;

import java.io.FileInputStream;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class GroupsDAO implements GroupsDAOInterface {

    @Override
    public <String> void create(String groupName) {
        Connection connection = null;
        Statement statement = null;
        try (FileInputStream stream = new FileInputStream("src/connection.properties")){
            Class.forName("org.postgresql.Driver");
            Properties properties = new Properties();
            properties.load(stream);
            connection = DriverManager.getConnection(properties.getProperty("url"),
                    properties.getProperty("user"), properties.getProperty("password"));
            statement = connection.createStatement();
            try {
                statement.executeQuery("INSERT INTO groups (name) " +
                                       "VALUES ('"  + groupName +"');");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public List<int[]> getGroupsBySize(int expectedGroupSize) {
        final String sql = "SELECT group_id, COUNT (*) " +
                           "FROM students GROUP BY group_id " +
                           "HAVING COUNT(*) <= " + expectedGroupSize + " " +
                           "ORDER BY group_id;\n";
        Connection connection = null;
        List<int[]> groupsSizes = new LinkedList<>();
        try (FileInputStream stream = new FileInputStream("src/connection.properties")) {
            Class.forName("org.postgresql.Driver");
            Properties properties = new Properties();
            properties.load(stream);
            connection = DriverManager.getConnection(properties.getProperty("url"),
                    properties.getProperty("user"), properties.getProperty("password"));
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int[] groupSize = new int[2];
                groupSize[0] = resultSet.getInt("group_id");
                groupSize[1] = resultSet.getInt("count");
                groupsSizes.add(groupSize);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return groupsSizes;
    }
}

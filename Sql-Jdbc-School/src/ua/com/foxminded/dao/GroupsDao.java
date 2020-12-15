package ua.com.foxminded.dao;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class GroupsDao {

    private static final String user = "postgres";
    private static final String password = "1234";
    private static final String url = "jdbc:postgresql://localhost:5432/school";

    public void create(String groupName) {
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
            try {
                statement.executeQuery("INSERT INTO groups (name) VALUES ('"  + groupName +"');");
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
        try  {
            connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement(sql);
            final ResultSet resultSet = statement.executeQuery();
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

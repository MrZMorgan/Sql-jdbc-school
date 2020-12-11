package ua.com.foxminded.dao;

import ua.com.foxminded.interfaces.DAO;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class GroupsDAO implements DAO {

    private static final String user = "postgres";
    private static final String password = "1234";
    private static final String url = "jdbc:postgresql://localhost:5432/school";

    public void create(int id, String groupName) {
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
            try {
                statement.executeQuery("INSERT INTO groups (id ,group_name) VALUES (" + id + ", '"  + groupName +"');");
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

    public List<String> getGroupNameList() {
        final String sql = "SELECT group_name FROM groups;";
        Connection connection = null;
        List<String> groupNames = new LinkedList<>();
        try {
            connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement(sql);
            final ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                groupNames.add(resultSet.getString("group_name"));
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
        return groupNames;
    }
}

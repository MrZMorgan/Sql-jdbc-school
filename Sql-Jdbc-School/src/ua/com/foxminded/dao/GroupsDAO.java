package ua.com.foxminded.dao;

import ua.com.foxminded.connection.ConnectionFactory;
import ua.com.foxminded.exceptions.DAOException;
import ua.com.foxminded.interfaces.GroupsDAOInterface;
import java.io.FileInputStream;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class GroupsDAO implements GroupsDAOInterface {

    public final static String RESOURCE_FILE_PATH = "resources/connection.properties";
    private static final String FAILED_CONNECTION_MESSAGE = "Database connection failed";

    @Override
    public <String> void create(String groupName) throws DAOException {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = new ConnectionFactory().connect();
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
                throw new DAOException(FAILED_CONNECTION_MESSAGE);
            }
        }
    }

    public List<int[]> getGroupsBySize(int expectedGroupSize) throws DAOException {
        final String sql = "SELECT group_id, COUNT (*) " +
                           "FROM students GROUP BY group_id " +
                           "HAVING COUNT(*) <= " + expectedGroupSize + " " +
                           "ORDER BY group_id;\n";
        Connection connection = null;
        List<int[]> groupsSizes = new LinkedList<>();
        try {
            connection = new ConnectionFactory().connect();
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
                throw new DAOException(FAILED_CONNECTION_MESSAGE);
            }
        }
        return groupsSizes;
    }
}

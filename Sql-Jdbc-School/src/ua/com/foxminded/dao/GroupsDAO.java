package ua.com.foxminded.dao;

import ua.com.foxminded.connection.ConnectionFactory;
import ua.com.foxminded.exceptions.DAOException;
import ua.com.foxminded.interfaces.GroupsDAOInterface;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class GroupsDAO implements GroupsDAOInterface {

    private static final String FAILED_CONNECTION_MESSAGE = "Database connection failed";
    public static final String SQL_RESOURCES = "resources/sql.properties";

    @Override
    public <T> void create(T groupName) throws DAOException {
        Connection connection = null;
        Statement statement = null;
        Properties properties = new Properties();
        try {
            FileInputStream stream = new FileInputStream(SQL_RESOURCES);
            properties.load(stream);
            stream.close();

            connection = new ConnectionFactory().connect();
            statement = connection.createStatement();
            statement.executeQuery(String.format(properties.getProperty("CREATE_GROUP"), groupName));
        } catch (SQLException | IOException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throw new DAOException(FAILED_CONNECTION_MESSAGE);
            }
        }
    }

    public List<int[]> getGroupsBySize(int expectedGroupSize) throws DAOException {
        Connection connection = null;
        Properties properties = new Properties();
        List<int[]> groupsSizes = new LinkedList<>();
        try {
            FileInputStream stream = new FileInputStream(SQL_RESOURCES);
            properties.load(stream);
            stream.close();

            connection = new ConnectionFactory().connect();
            PreparedStatement statement = connection.prepareStatement(
                    String.format(properties.getProperty("GET_GROUPS_BY_SIZE"), expectedGroupSize)
            );
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int[] groupSize = new int[2];
                groupSize[0] = resultSet.getInt("group_id");
                groupSize[1] = resultSet.getInt("count");
                groupsSizes.add(groupSize);
            }
        } catch (SQLException | IOException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
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

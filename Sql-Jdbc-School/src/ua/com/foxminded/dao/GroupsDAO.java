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
import java.util.logging.Logger;

public class GroupsDAO implements GroupsDAOInterface {

    String resourceFilePath;

    public GroupsDAO(String resourceFilePath) {
        this.resourceFilePath = resourceFilePath;
    }

    public static final String SQL_RESOURCES = "src/resources/sql.properties";
    private final static Logger logger = Logger.getLogger(GroupsDAO.class.getName());

    @Override
    public void create(String groupName) throws DAOException {
        Connection connection = null;
        Statement statement = null;
        Properties properties = new Properties();
        ConnectionFactory factory = new ConnectionFactory(resourceFilePath);
        try {
            FileInputStream stream = new FileInputStream(SQL_RESOURCES);
            properties.load(stream);
            stream.close();

            connection = factory.connect();
            statement = connection.createStatement();
            statement.executeQuery(String.format(properties.getProperty("create.group"), groupName));
        } catch (SQLException | IOException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
            logger.info(throwables.getMessage());
        } finally {
            factory.close(connection);
        }
    }

    public List<int[]> getGroupsBySize(int expectedGroupSize) throws DAOException {
        Connection connection = null;
        Properties properties = new Properties();
        ConnectionFactory factory = new ConnectionFactory(resourceFilePath);
        List<int[]> groupsSizes = new LinkedList<>();
        try {
            FileInputStream stream = new FileInputStream(SQL_RESOURCES);
            properties.load(stream);
            stream.close();

            connection = factory.connect();
            PreparedStatement statement = connection.prepareStatement(
                    String.format(properties.getProperty("get.groups.by.size"), expectedGroupSize)
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
            logger.info(throwables.getMessage());
        } finally {
            factory.close(connection);
        }
        return groupsSizes;
    }
}

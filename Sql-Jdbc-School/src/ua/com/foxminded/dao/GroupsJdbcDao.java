package ua.com.foxminded.dao;

import ua.com.foxminded.connection.ConnectionFactory;
import ua.com.foxminded.dao.data.Group;
import ua.com.foxminded.exceptions.DAOException;
import ua.com.foxminded.interfaces.GroupsDAO;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.logging.Logger;

public class GroupsJdbcDao implements GroupsDAO {

    public final static String SQL_RESOURCES = "resources/sql.properties";
    private final static Logger logger = Logger.getLogger(GroupsJdbcDao.class.getName());

    ConnectionFactory factory;

    public GroupsJdbcDao(ConnectionFactory factory) {
        this.factory = factory;
    }

    @Override
    public void create(String groupName) throws DAOException {
        Connection connection = null;
        Statement statement = null;
        Properties properties = new Properties();
        try {
            FileInputStream stream = new FileInputStream(SQL_RESOURCES);
            properties.load(stream);
            stream.close();

            connection = factory.connect();
            statement = connection.createStatement();
            statement.execute(String.format(properties.getProperty("create.group"), groupName));
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
            logger.info(throwables.getMessage());
        } finally {
            factory.close(connection);
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
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
            logger.info(throwables.getMessage());
        } finally {
            factory.close(connection);
        }
        return groupsSizes;
    }

    @Override
    public List<Group> readAllData() throws DAOException {
        Connection connection = null;
        List<Group> courseList = new LinkedList<>();
        Properties properties = new Properties();
        try {
            FileInputStream stream = new FileInputStream(SQL_RESOURCES);
            properties.load(stream);
            stream.close();

            connection = factory.connect();
            PreparedStatement statement = connection.prepareStatement(properties.getProperty("get.groups.list"));
            final ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                courseList.add(new Group(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                ));
            }
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
            logger.info(throwables.getMessage());
        } finally {
            factory.close(connection);
        }
        return courseList;
    }

    @Override
    public void deleteById(int groupId) throws DAOException {
        Connection connection = null;
        Statement statement = null;
        Properties properties = new Properties();
        try {
            FileInputStream stream = new FileInputStream(SQL_RESOURCES);
            properties.load(stream);
            stream.close();

            connection = factory.connect();
            statement = connection.createStatement();
            statement.execute(String.format(properties.getProperty("delete.group.by.id"), groupId));
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
            logger.info(throwables.getMessage());
        } finally {
            factory.close(connection);
        }
    }

    @Override
    public void update(int groupId, String groupName) throws DAOException {
        Connection connection = null;
        Statement statement = null;
        Properties properties = new Properties();
        try {
            FileInputStream stream = new FileInputStream(SQL_RESOURCES);
            properties.load(stream);
            stream.close();

            connection = factory.connect();
            statement = connection.createStatement();
            statement.execute(
                    String.format(properties.getProperty("update.group.name"), groupName, groupId));
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
            logger.info(throwables.getMessage());
        } finally {
            factory.close(connection);
        }
    }
}

package ua.com.foxminded.dao;

import ua.com.foxminded.interfaces.DAO;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class StudentsDAO implements DAO {

    private static final String user = "postgres";
    private static final String password = "1234";
    private static final String url = "jdbc:postgresql://localhost:5432/school";

    public void create(int id, int groupId, String firstName, String lastName) {
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
            try {
                statement.executeQuery("INSERT INTO students (id, group_id, first_name, last_name) VALUES (" + id + ", + " + groupId + ", '" + firstName  + "', '" + lastName + "');");
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

    public void deleteStudent(int studentId) {
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
            try {
                statement.executeQuery( "DELETE FROM students WHERE id =" + studentId +";");
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

    public void fillTable(List<String[]> namesGroups, List<String[]> namesList) {
        int j = 0;
        if (namesGroups.size() == 200) {
            while (j < 200) {
                create(j + 1, Integer.parseInt(namesGroups.get(j)[0]), namesGroups.get(j)[1], namesGroups.get(j)[2]);
                j++;
            }
        } else {
            if (namesGroups.size() < 200) {
                for (int i = 0; i < namesGroups.size(); i++) {
                    create(j + 1, Integer.parseInt(namesGroups.get(j)[0]), namesGroups.get(j)[1], namesGroups.get(j)[2]);
                    j++;
                }

                while (j < 200) {
                    create(j + 1, 0, namesList.get(j)[0], namesList.get(j)[1]);
                    j++;
                }
            }
        }
    }

    public int getStudentsTableSize() {
        final String sql = "SELECT * FROM students";
        Connection connection = null;
        int id = 0;
        try  {
            connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement(sql);
            final ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                id++;
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
        return id;
    }

    public String[] getStudentById(int studentId) {
        final String sql = "SELECT * FROM students WHERE id = "  + studentId + ";";
        Connection connection = null;
        String[] fullName = new String[2];
        try  {
            connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement(sql);
            final ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                fullName[0] = resultSet.getString("first_name");
                fullName[1] = resultSet.getString("last_name");
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
        return fullName;
    }

    public List<int[]> getGroupsBySize(int expectedGroupSize) {
        final String sql = "SELECT group_id, COUNT(*) FROM students GROUP BY group_id HAVING COUNT(*) >= " + expectedGroupSize + ";";
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


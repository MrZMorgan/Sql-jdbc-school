package ua.com.foxminded.dao;

import ua.com.foxminded.interfaces.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class StudentsDAO implements DAO {

    private static final String user = "postgres";
    private static final String password = "1234";
    private static final String url = "jdbc:postgresql://localhost:5432/school";

    public void create(int groupId, String firstName, String lastName) {
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
            try {
                statement.executeQuery("INSERT INTO students (group_id, first_name, last_name) VALUES (" + groupId + ", '" + firstName  + "', '" + lastName + "');");
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
            create(Integer.parseInt(namesGroups.get(j)[0]), namesGroups.get(j)[1], namesGroups.get(j)[2]);
        } else {
            if (namesGroups.size() < 200) {
                for (int i = 0; i < namesGroups.size(); i++) {
                    create(Integer.parseInt(namesGroups.get(j)[0]), namesGroups.get(j)[1], namesGroups.get(j)[2]);
                    j++;
                }

                while (j < 200) {
                    create(0, namesList.get(j)[0], namesList.get(j)[1]);
                    j++;
                }
            }
        }
    }
}


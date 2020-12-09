package ua.com.foxminded;

import java.sql.*;

public class DataGenerator {
    static String tableName = "groups";
    static String sqlCreate = "CREATE TABLE groups (\n" +
            "    group_id BIGSERIAL NOT NULL PRIMARY KEY,\n" +
            "    group_name VARCHAR(50) NOT NULL\n" +
            ")";
    static String sqlDrop = "DROP TABLE groups;";

    public static void generateTables(String tableName, String sqlCreate, String sqlDrop) throws SQLException {
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
                System.out.println("Table " + tableName + " dropped");
            } catch (Exception e) {
                e.printStackTrace();
            }
            statement.executeQuery(sqlCreate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) throws SQLException {
        generateTables(tableName, sqlCreate, sqlDrop);
    }
}

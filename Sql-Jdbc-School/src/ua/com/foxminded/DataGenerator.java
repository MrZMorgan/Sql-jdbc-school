package ua.com.foxminded;

import java.sql.*;

public class DataGenerator {
    static String sqlCreate = "CREATE TABLE groups (\n" +
            "    group_id BIGSERIAL NOT NULL PRIMARY KEY,\n" +
            "    group_name VARCHAR(50) NOT NULL\n" +
            ")";
    static String sqlDrop = "DROP TABLE groups;";

    public static void generateTables(String tableName, String sqlCreate) throws SQLException {
        String user = "postgres";
        String password = "1234";
        String url = "jdbc:postgresql://localhost:5432/school";
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = null;
            Statement statement = null;
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
            statement.executeQuery(sqlCreate);
            System.out.println(tableName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteTables(String tableName, String sqlDrop) throws SQLException {
        String user = "postgres";
        String password = "1234";
        String url = "jdbc:postgresql://localhost:5432/school";
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(null, null, tableName, null);
            if (tables.next()) {
                statement.executeQuery(sqlDrop);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }

    public static void main(String[] args) throws SQLException {
        deleteTables("groups", sqlDrop);
        generateTables("groups", sqlCreate);
    }
}

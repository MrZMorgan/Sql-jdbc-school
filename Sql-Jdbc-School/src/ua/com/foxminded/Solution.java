package ua.com.foxminded;

import java.sql.SQLException;
import java.util.List;

public class Solution {
    static String tableName = "groups";
    static String sqlCreate = "CREATE TABLE groups (\n" +
            "    group_id BIGSERIAL NOT NULL PRIMARY KEY,\n" +
            "    group_name VARCHAR(50) NOT NULL\n" +
            ")";
    static String sqlDrop = "DROP TABLE groups;";

    public static void main(String[] args) throws SQLException {
        DataGenerator dataGenerator = new DataGenerator();
//        dataGenerator.generateTables(tableName, sqlCreate, sqlDrop);

        List<String> strings = dataGenerator.generateGroupsNamesList();
        for (String s : strings) {
            System.out.println(s);
        }
    }
}

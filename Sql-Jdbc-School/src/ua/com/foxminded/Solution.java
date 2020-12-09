package ua.com.foxminded;

import java.sql.SQLException;
import java.util.List;

public class Solution {
    final static String tableName = "groups";
    final static String courses = "src/ua/com/foxminded/rawdata/courses";
    final static String first_names = "src/ua/com/foxminded/rawdata/first_names";
    final static String last_names = "src/ua/com/foxminded/rawdata/last_names";


    public static void main(String[] args) throws SQLException {
        DataGenerator dataGenerator = new DataGenerator();
//        dataGenerator.generateTables(tableName, sqlCreate, sqlDrop);

        List<String> strings = dataGenerator.generateNamesList(first_names, last_names);
        for (String s : strings) {
            System.out.println(s);
        }
        System.out.println(strings.size());
    }
}

package ua.com.foxminded.facade;

import ua.com.foxminded.DataGenerator;
import static ua.com.foxminded.sql.Queries.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class Facade {
    final static String courses = "src/ua/com/foxminded/rawdata/courses";
    final static String first_names = "src/ua/com/foxminded/rawdata/first_names";
    final static String last_names = "src/ua/com/foxminded/rawdata/last_names";

    public static void main(String[] args) {
        DataGenerator dataGenerator = new DataGenerator();

        dataGenerator.generateTable(SQL_DROP_GROUPS_TABLE , SQL_CREATE_GROUPS_TABLE);
        dataGenerator.generateTable(SQL_DROP_COURSES_TABLE, SQL_CREATE_COURSES_TABLE);
        dataGenerator.generateTable(SQL_DROP_STUDENTS_TABLE, SQL_CREATE_STUDENTS_TABLE);

        Map<String, Integer> groupNames = dataGenerator.generateGroupsNamesList();
        List<String[]> namesLis = dataGenerator.generateNamesList(first_names, last_names);

    }
}

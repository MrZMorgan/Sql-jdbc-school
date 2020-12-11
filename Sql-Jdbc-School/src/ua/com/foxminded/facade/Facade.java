package ua.com.foxminded.facade;

import ua.com.foxminded.DataGenerator;

import java.util.List;

import static ua.com.foxminded.sql.Queries.*;

public class Facade {
    DataGenerator dataGenerator;

    public Facade(DataGenerator dataGenerator) {
        this.dataGenerator = dataGenerator;
    }

    public void generateTestData() {
//        dataGenerator.generateTable(SQL_DROP_GROUPS_TABLE, SQL_CREATE_GROUPS_TABLE);
//        dataGenerator.generateTable(SQL_DROP_COURSES_TABLE,SQL_CREATE_COURSES_TABLE);
//        dataGenerator.generateTable(SQL_DROP_STUDENTS_TABLE, SQL_CREATE_STUDENTS_TABLE);

        List<String> groups = dataGenerator.generateGroupsNamesList();

        groups.forEach(System.out::println);
    }
}

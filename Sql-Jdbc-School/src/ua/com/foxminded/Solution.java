package ua.com.foxminded;

import ua.com.foxminded.dao.CoursesDAO;
import ua.com.foxminded.dao.GroupsDAO;
import ua.com.foxminded.dao.StudentsDAO;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class Solution {
    final static String courses = "src/ua/com/foxminded/rawdata/courses";
    final static String first_names = "src/ua/com/foxminded/rawdata/first_names";
    final static String last_names = "src/ua/com/foxminded/rawdata/last_names";


    public static void main(String[] args) throws SQLException {
        DataGenerator dataGenerator = new DataGenerator();
        GroupsDAO groupsDAO = new GroupsDAO();
        CoursesDAO coursesDAO = new CoursesDAO();
        StudentsDAO studentsDAO = new StudentsDAO();

//        dataGenerator.generateTables("DROP TABLE groups;",
//                "CREATE TABLE groups (\n" +
//                        "    id SERIAL NOT NULL PRIMARY KEY,\n" +
//                        "    group_name VARCHAR(50) NOT NULL\n" +
//                        ");\n");
//
//        dataGenerator.generateTables("DROP TABLE students;",
//                "CREATE TABLE students (\n" +
//                "    id SERIAL NOT NULL PRIMARY KEY,\n" +
//                "    group_id INT NOT NULL,\n" +
//                "    first_name VARCHAR(50) NOT NULL,\n" +
//                "    last_name VARCHAR(50) NOT NULL\n" +
//                ");");
//
//        dataGenerator.generateTables("DROP TABLE courses;",
//                "CREATE TABLE courses (\n" +
//                "    id SERIAL NOT NULL PRIMARY KEY,\n" +
//                "    name VARCHAR(50) NOT NULL,\n" +
//                "    description VARCHAR(200)\n" +
//                ");");

//        dataGenerator.generateTables("DROP TABLE students_courses;",
//                "CREATE TABLE students_courses (\n" +
//                        "    courses_id INT REFERENCES courses (id),\n" +
//                        "    student_id INT REFERENCES students (id)\n" +
//                        ");");

        Map<String, String> students = dataGenerator.generateNamesList(first_names, last_names);
        System.out.println(students.size());
        List<String> groupNames = dataGenerator.generateGroupsNamesList();
        List<String> courcesNames = dataGenerator.read(courses);

//        for (String groupName : groupNames) {
//            groupsDAO.create(groupName);
//        }
//
//        for (String courcesName : courcesNames) {
//            coursesDAO.create(courcesName);
//        }
//
//        for (Map.Entry<String, String> entry : students.entrySet()) {
//            studentsDAO.create(entry.getKey(), entry.getValue());
//        }
    }
}

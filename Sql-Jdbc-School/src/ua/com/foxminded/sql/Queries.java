package ua.com.foxminded.sql;

public class Queries {
    public final static String SQL_DROP_GROUPS_TABLE = "DROP TABLE groups;";
    public final static String SQL_CREATE_GROUPS_TABLE = "CREATE TABLE groups (\n" +
            "    id INT NOT NULL PRIMARY KEY,\n" +
            "    group_name VARCHAR(50) NOT NULL\n" +
            ");\n";

    public final static String SQL_DROP_COURSES_TABLE = "DROP TABLE courses;";
    public final static String SQL_CREATE_COURSES_TABLE = "CREATE TABLE courses (\n" +
            "    id INT NOT NULL PRIMARY KEY,\n" +
            "    name VARCHAR(50) NOT NULL,\n" +
            "    description VARCHAR(200)\n" +
            ");";

    public final static String SQL_DROP_STUDENTS_TABLE = "DROP TABLE students;";
    public final static String SQL_CREATE_STUDENTS_TABLE = "CREATE TABLE students (\n" +
            "    id INT NOT NULL PRIMARY KEY,\n" +
            "    group_id INT,\n" +
            "    first_name VARCHAR(50) NOT NULL,\n" +
            "    last_name VARCHAR(50) NOT NULL\n" +
            ");";

    public final static String SQL_DROP_STUDENTS_COURSES_TABLE = "DROP TABLE students_courses;";
    public final static String SQL_CREATE_STUDENTS_COURSES_TABLE = "CREATE TABLE students_courses (\n" +
            "    student_id INT REFERENCES students (id),\n" +
            "    courses_id INT REFERENCES courses (id)\n" +
            ");";
}

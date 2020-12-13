package ua.com.foxminded.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class StudentsCoursesDAO {

    private static final String user = "postgres";
    private static final String password = "1234";
    private static final String url = "jdbc:postgresql://localhost:5432/school";

    public void create(int studentId, int course_id) {
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
            try {
                statement.executeQuery("INSERT INTO students_courses (student_id , course_id) VALUES (" + studentId + ", " + course_id + ");");
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
}

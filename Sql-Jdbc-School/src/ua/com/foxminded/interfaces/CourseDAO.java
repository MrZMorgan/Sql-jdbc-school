package ua.com.foxminded.interfaces;

import ua.com.foxminded.dao.data.Course;
import ua.com.foxminded.exceptions.DAOException;

import java.io.IOException;
import java.util.List;

    public interface CourseDAO extends GenericDAO <String, Course> {
    @Override
    void create(String courseName) throws DAOException, IOException;

    @Override
    List<Course> readAllData() throws DAOException;

    @Override
    void deleteById(int id) throws DAOException;

    @Override
    void update(int id, String data) throws DAOException;
}

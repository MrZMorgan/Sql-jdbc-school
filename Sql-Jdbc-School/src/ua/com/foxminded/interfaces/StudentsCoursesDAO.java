package ua.com.foxminded.interfaces;

import ua.com.foxminded.dao.data.StudentCourse;
import ua.com.foxminded.exceptions.DAOException;

import java.io.IOException;
import java.util.List;

public interface StudentsCoursesDAO extends GenericDAO <String, StudentCourse> {
    @Override
    void create(String t) throws DAOException, IOException;

    @Override
    List<StudentCourse> readAllData() throws DAOException;

    @Override
    void deleteById(int id) throws DAOException;

    @Override
    void update(int id, String data) throws DAOException;
}

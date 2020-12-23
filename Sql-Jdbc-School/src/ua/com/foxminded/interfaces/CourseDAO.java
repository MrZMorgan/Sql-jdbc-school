package ua.com.foxminded.interfaces;

import ua.com.foxminded.exceptions.DAOException;

import java.io.IOException;
import java.util.List;

public interface CourseDAO extends GenericDAO <String, String[]> {
    @Override
    void create(String courseName) throws DAOException, IOException;

    @Override
    List<String[]> readAllData() throws DAOException;

    @Override
    void deleteById(int id) throws DAOException;

    @Override
    void update(int id, String data) throws DAOException;
}

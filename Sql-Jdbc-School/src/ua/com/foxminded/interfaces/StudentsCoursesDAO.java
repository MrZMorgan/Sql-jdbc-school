package ua.com.foxminded.interfaces;

import ua.com.foxminded.exceptions.DAOException;

import java.io.IOException;
import java.util.List;

public interface StudentsCoursesDAO extends GenericDAO <String> {
    @Override
    void create(String t) throws DAOException, IOException;

    @Override
    List<String[]> readAllData() throws DAOException;

    @Override
    void deleteById(int id) throws DAOException;
}

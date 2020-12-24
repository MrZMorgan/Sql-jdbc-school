package ua.com.foxminded.interfaces;

import ua.com.foxminded.dao.data.Student;
import ua.com.foxminded.exceptions.DAOException;

import java.io.IOException;
import java.util.List;

public interface StudentsDAO extends GenericDAO <String, Student> {
    @Override
    void create(String t) throws DAOException, IOException;

    @Override
    List<Student> readAllData() throws DAOException;

    @Override
    void deleteById(int id) throws DAOException;

    @Override
    void update(int id, String data) throws DAOException;
}

package ua.com.foxminded.interfaces;

import ua.com.foxminded.exceptions.DAOException;

import java.io.IOException;
import java.util.List;

public interface GenericDAO <T> {
    void create(T groupName) throws DAOException, IOException;
    List<String[]> readAllData() throws DAOException;
    void deleteById(int id) throws DAOException;
    void update(int id, String data) throws DAOException;
}

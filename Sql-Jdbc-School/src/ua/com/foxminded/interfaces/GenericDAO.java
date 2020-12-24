package ua.com.foxminded.interfaces;

import ua.com.foxminded.exceptions.DAOException;

import java.io.IOException;
import java.util.List;

public interface GenericDAO <T, K> {
    void create(T groupName) throws DAOException, IOException;
    List<K> readAllData() throws DAOException;
    void deleteById(int id) throws DAOException;
    void update(int id, String data) throws DAOException;
}

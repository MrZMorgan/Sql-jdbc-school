package ua.com.foxminded.interfaces;

import ua.com.foxminded.exceptions.DAOException;

import java.io.IOException;

public interface GenericDAO <T> {
    void create(T groupName) throws DAOException, IOException;
}

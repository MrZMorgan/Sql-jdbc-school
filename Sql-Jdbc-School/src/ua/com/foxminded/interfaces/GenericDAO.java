package ua.com.foxminded.interfaces;

import ua.com.foxminded.exceptions.DAOException;

public interface GenericDAO <T> {
    void create(T groupName) throws DAOException;
}

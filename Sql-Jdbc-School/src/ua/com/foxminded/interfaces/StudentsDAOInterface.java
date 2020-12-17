package ua.com.foxminded.interfaces;

import ua.com.foxminded.exceptions.DAOException;

public interface StudentsDAOInterface extends GenericDAO <String> {
    @Override
    void create(String t) throws DAOException;
}

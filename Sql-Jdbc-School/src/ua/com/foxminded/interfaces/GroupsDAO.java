package ua.com.foxminded.interfaces;

import ua.com.foxminded.exceptions.DAOException;

import java.io.IOException;

public interface GroupsDAO extends GenericDAO <String> {
    @Override
    void create(String t) throws DAOException, IOException;
}

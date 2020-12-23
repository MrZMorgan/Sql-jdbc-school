package ua.com.foxminded.interfaces;

import ua.com.foxminded.dao.data.Group;
import ua.com.foxminded.exceptions.DAOException;

import java.io.IOException;
import java.util.List;

public interface GroupsDAO extends GenericDAO <String, Group> {
    @Override
    void create(String t) throws DAOException, IOException;

    @Override
    List<Group> readAllData() throws DAOException;

    @Override
    void deleteById(int id) throws DAOException;

    @Override
    void update(int id, String data) throws DAOException;
}

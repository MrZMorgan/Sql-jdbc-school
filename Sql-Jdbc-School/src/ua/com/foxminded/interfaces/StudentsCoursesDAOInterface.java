package ua.com.foxminded.interfaces;

import ua.com.foxminded.exceptions.DAOException;

public interface StudentsCoursesDAOInterface extends GenericDAO <String> {
    @Override
    void create(String t) throws DAOException;
}

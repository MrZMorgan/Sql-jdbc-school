package ua.com.foxminded.interfaces;

import ua.com.foxminded.exceptions.DAOException;

public interface CourseDAOInterface extends GenericDAO <String> {
    @Override
    void create(String courseName) throws DAOException;
}

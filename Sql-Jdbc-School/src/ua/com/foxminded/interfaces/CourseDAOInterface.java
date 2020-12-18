package ua.com.foxminded.interfaces;

import ua.com.foxminded.exceptions.DAOException;

import java.io.IOException;

public interface CourseDAOInterface extends GenericDAO <String> {
    @Override
    void create(String courseName) throws DAOException, IOException;
}

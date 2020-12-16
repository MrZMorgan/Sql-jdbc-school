package ua.com.foxminded.interfaces;

import ua.com.foxminded.exceptions.DAOException;

public interface CourseDAOInterface extends GenericDAO{
    @Override
    <T> void create(T t) throws DAOException;
}

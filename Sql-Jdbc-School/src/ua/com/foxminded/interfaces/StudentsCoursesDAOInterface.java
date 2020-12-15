package ua.com.foxminded.interfaces;

public interface StudentsCoursesDAOInterface extends GenericDAO {
    @Override
    <T> void create(T t);
}

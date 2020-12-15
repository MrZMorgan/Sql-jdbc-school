package ua.com.foxminded.interfaces;

public interface StudentsDAOInterface extends GenericDAO {
    @Override
    <T> void create(T t);
}

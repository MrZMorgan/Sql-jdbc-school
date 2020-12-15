package ua.com.foxminded.interfaces;

public interface CourseDAOInterface extends GenericDAO{
    @Override
    <T> void create(T t);
}

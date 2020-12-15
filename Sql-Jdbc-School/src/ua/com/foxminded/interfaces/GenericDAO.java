package ua.com.foxminded.interfaces;

public interface GenericDAO {
    <T> void create(T t);
}

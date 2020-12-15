package ua.com.foxminded.interfaces;

public interface GroupsDAOInterface extends GenericDAO {
    @Override
    <T> void create(T t);
}

package ua.com.foxminded.dao.data;

public class Course {
    private final int id;
    private final String name;
    private final String description;

    public Course(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}

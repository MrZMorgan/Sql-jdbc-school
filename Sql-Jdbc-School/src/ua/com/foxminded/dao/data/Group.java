package ua.com.foxminded.dao.data;

import java.util.Objects;

public class Group {
    private final int id;
    private final String name;

    public Group(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return id == group.id &&
                name.equals(group.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}

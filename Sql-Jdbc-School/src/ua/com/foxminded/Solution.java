package ua.com.foxminded;

import ua.com.foxminded.dao.CoursesDao;
import ua.com.foxminded.dao.GroupsDao;
import ua.com.foxminded.facade.Facade;

public class Solution {
    public static void main(String[] args) {
        Facade facade = new Facade(new DataGenerator(), new CoursesDao(), new GroupsDao());
        facade.generateTestData();
    }
}

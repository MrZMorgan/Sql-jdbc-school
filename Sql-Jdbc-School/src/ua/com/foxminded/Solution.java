package ua.com.foxminded;

import ua.com.foxminded.dao.CoursesDAO;
import ua.com.foxminded.dao.GroupsDAO;
import ua.com.foxminded.dao.StudentsCoursesDAO;
import ua.com.foxminded.dao.StudentsDAO;
import ua.com.foxminded.facade.Facade;

public class Solution {
    public static void main(String[] args) {
        Facade facade = new Facade(new DataGenerator(),
                                   new CoursesDAO(),
                                   new GroupsDAO(),
                                   new StudentsDAO(),
                                   new StudentsCoursesDAO());
        facade.generateTestData();
        facade.workWithDataBase();
    }
}

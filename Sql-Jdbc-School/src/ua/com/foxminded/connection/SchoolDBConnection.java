package ua.com.foxminded.connection;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public interface SchoolDBConnection {
    Connection connect(FileInputStream stream) throws ClassNotFoundException, SQLException, IOException;
}

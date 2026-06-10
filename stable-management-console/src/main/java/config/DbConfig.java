package config;

import exception.DatabaseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConfig {
    private static final String URL = "jdbc:mysql://localhost:3306/stables_management";
    private static final String USER = "root";
    private static final String PASS = "admin";

    public static Connection getDbConnection() {

        try{
            return DriverManager.getConnection(URL, USER, PASS);
        }
        catch(SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }


}

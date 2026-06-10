package repository;

import config.DbConfig;
import enumeration.UserRole;
import exception.DatabaseException;

import java.sql.*;
import java.util.Optional;

public class UserRepository {
    public static Optional<UserAuthRecord> findUserByUsername(String username) throws DatabaseException {
        String query = "select * from users where username = ?;";
        try (Connection con = DbConfig.getDbConnection()) {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1,username);

            ResultSet results = ps.executeQuery();
            if (results.next()) {
                int id = results.getInt("user_id");
                String passwordHash = results.getString("password_hash");
                UserRole role = UserRole.valueOf(results.getString("user_role"));
                return Optional.of(new UserAuthRecord(id,username,passwordHash,role));
            }
            else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Connection error: "+e.getMessage());
        }
    }

    public static Optional<UserAuthRecord> insertUser(String username, String password_hash, UserRole role) throws DatabaseException {
        if(existsByUsername(username)) {
            throw new DatabaseException("Username already exists");
        }
        String query =  "insert into users(username, password_hash, user_role) values(?,?,?)";
        try(Connection con = DbConfig.getDbConnection()) {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1,username);
            ps.setString(2,password_hash);
            ps.setString(3,role.toString());
            ps.executeUpdate();
            return findUserByUsername(username);
        }
        catch (SQLException e) {
            throw new DatabaseException("Connection error: "+e.getMessage());
        }

    }

    public static boolean existsByUsername(String username) throws DatabaseException {
        String query = "select user_id from users where username = ?;";
        try(Connection con = DbConfig.getDbConnection()) {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1,username);
            ResultSet results = ps.executeQuery();
            return results.next();
        }
        catch (SQLException e) {
            throw new DatabaseException("Connection error: "+e.getMessage());
        }
    }

}

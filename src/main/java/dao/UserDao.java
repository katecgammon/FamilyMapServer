package dao;

import model.Event;
import model.User;

import java.sql.*;
import java.util.UUID;

public class UserDao {
    private final Connection conn;
    final static String CONNECTION_ADDRESS = "jdbc:sqlite:database.db";

    /**
     * Establishes the connection
     */
    public UserDao(Connection conn) {
        this.conn = conn;
    }

    public String generatePersonID() {
        UUID ID = UUID.randomUUID();
        return ID.toString();
    }
    /**
     * Takes a new user and inserts them in the user table in the database
     */
    public void insert(User user) throws DataAccessException {
        if (userAlreadyExists((user.getPersonID()))) {
            throw new DataAccessException("User already exists!");
        }
        String sql = "INSERT INTO User (username, password, email, firstName, lastName, " +
                "gender, personID) VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, user.getUsername());
        stmt.setString(2, user.getPassword());
        stmt.setString(3, user.getEmail());
        stmt.setString(4, user.getFirstName());
        stmt.setString(5, user.getLastName());
        stmt.setString(6, user.getGender());
        stmt.setString(7, user.getPersonID());

        stmt.executeUpdate();
        conn.commit();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     *
     * @param username
     * @param password
     * @return true or false based on whether the username and password match
     * @throws DataAccessException
     *
     * Looks at the password of the given username in the table and returns true if the passwords match.
     */
    public boolean verify(String username, String password) throws DataAccessException {
        String sql = "SELECT password FROM User WHERE username = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                String toCompare = result.getString("password");
                if (toCompare.equals(password)) {
                    return true;
                }
                else throw new DataAccessException("Username and password don't match");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding user");
        }
        return false;
    }

    /**
     *
     * @param username
     * @return the user that matches the given userID
     * @throws DataAccessException
     */
    public User find(String username) throws DataAccessException {
        User user;
        ResultSet rs = null;
        String sql = "SELECT * FROM User WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User(rs.getString("username"), rs.getString("password"),
                        rs.getString("email"), rs.getString("firstName"),
                        rs.getString("lastName"), rs.getString("gender"),
                        rs.getString("personID"));
                conn.commit();
                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding user");
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                    conn.commit();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

    /**
     *
     * @throws DataAccessException
     *
     * Drops the user table
     */
    public void clear() throws DataAccessException{
        String sql = "DELETE FROM User;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while clearing the User table");
        }
    }

    public void clearUser(String username) throws DataAccessException {
        String sql = "DELETE FROM User WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while clearing user table");
        }
    }

    /**
     *
     * @param username
     * @return true or false whether the user already exists.
     */
    public boolean userAlreadyExists(String username) throws DataAccessException {
        if (find(username) != null) {
            return true;
        }
        else return false;
    }
}

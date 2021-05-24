package dao;

import model.AuthToken;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AuthTokenDao {
    private final Connection conn;

    public AuthTokenDao(Connection conn)
    {
        this.conn = conn;
    }

    public String generateAuthToken() {
        UUID token = UUID.randomUUID();
        return token.toString();
    }
    /**
     * Inserts a new AuthToken object into the authToken table in the database
     * @param token
     * @throws DataAccessException
     */
    public void insert(AuthToken token) throws DataAccessException {
        String sql = "INSERT INTO AuthToken (authToken, username) VALUES(?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, token.getToken());
            stmt.setString(2, token.getUsername());

            stmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     *
     * @param token
     * @return the authToken object the matches the token given
     * @throws DataAccessException
     */
    public AuthToken find(String token) throws DataAccessException {
        AuthToken authToken;
        ResultSet rs = null;
        String sql = "SELECT * FROM AuthToken WHERE authToken = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, token);
            rs = stmt.executeQuery();
            if (rs.next()) {
                authToken = new AuthToken(rs.getString("authToken"), rs.getString("username"));
                return authToken;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

    /**
     *
     * @param token
     * @return whether or not the token already exists
     */
    boolean tokenAlreadyExists(String token) {
        return false;
    }

    /**
     *
     * @throws DataAccessException
     *
     * Drops the authToken table
     */
    void clear() throws DataAccessException{
        String sql = "DELETE FROM AuthToken;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while clearing the AuthToken table");
        }
    }
}

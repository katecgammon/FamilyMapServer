package service;

import dao.DataAccessException;
import dao.Database;
import dao.UserDao;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.LoginRequest;
import result.LoginResult;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LoginServiceTest {
    private Database db;
    LoginService loginService = new LoginService();
    LoginRequest loginRequest;
    LoginResult loginResult;
    private User newUser;
    private UserDao uDao;

    @BeforeEach
    public void setUp() throws DataAccessException
    {
        db = new Database();
        loginRequest = new LoginRequest("userName", "PASSword");
        newUser = new User("userName", "PASSword", "susan@gmail.com",
                "Susan", "Queen", "female", "12345");
        Connection conn = db.getConnection();
        db.clearTables();
        uDao = new UserDao(conn);

    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    @Test
    public void loginPass() throws DataAccessException {
        uDao.insert(newUser);
        LoginResult result = loginService.login(loginRequest);
        loginResult = new LoginResult("token", "userName", "12345");
        assertNotNull(result);
        assertEquals(loginResult.getUsername(), result.getUsername());
        assertEquals(loginResult.getPersonID(), result.getPersonID());
    }

    @Test
    public void loginFail() throws DataAccessException {
        User user1 = new User("angel", "PASSword", "angel@gmail.com",
                "Angel", "Face", "m", "13542");
        uDao.insert(user1);
        assertEquals("Error: User doesn't exist", loginService.login(loginRequest).getMessage());
    }

}

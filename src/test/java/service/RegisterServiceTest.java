package service;

import dao.DataAccessException;
import dao.Database;
import dao.UserDao;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.RegisterRequest;
import result.RegisterResult;

import javax.xml.crypto.Data;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterServiceTest {
    private Database db;
    RegisterService service = new RegisterService();
    RegisterResult result;
    RegisterRequest request;
    User newUser;
    UserDao uDao;


    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new Database();
        request = new RegisterRequest("kate123", "gammon456", "kate.gammon@gmail.com",
                "Kate", "Gammon", "f");
        newUser = new User("kate123", "gammon456", "kate.gammon@gmail.com",
                "Kate", "Gammon", "f", "personID");
        Connection conn = db.getConnection();
        db.clearTables();
        uDao = new UserDao(conn);

    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    @Test
    public void registerPass() throws DataAccessException {
        RegisterResult toCompare = service.register(request);
        result = new RegisterResult("token", request.getUsername(), toCompare.getPersonID());
        assertNotNull(toCompare);
        assertEquals(result.getUsername(), toCompare.getUsername());
        assertEquals(result.getPersonID(), toCompare.getPersonID());
    }

    @Test
    public void registerFail() throws DataAccessException {
        uDao.insert(newUser);
        assertEquals("Error: Username already exists", service.register(request).getMessage());
    }
}

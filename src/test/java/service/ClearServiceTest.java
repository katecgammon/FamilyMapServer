package service;

import dao.*;
import model.AuthToken;
import model.Event;
import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import result.ClearResult;
import java.sql.Connection;
import static org.junit.jupiter.api.Assertions.*;

public class ClearServiceTest {
    private Database db;
    UserDao uDao;
    User newUser;
    AuthTokenDao aDao;
    PersonDao pDao;
    EventDao eDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new Database();
        newUser = new User("userName", "PASSword", "susan@gmail.com",
                "Susan", "Queen", "female", "12345");
        Connection conn = db.getConnection();
        db.clearTables();
        uDao = new UserDao(conn);
        aDao = new AuthTokenDao(conn);
        eDao = new EventDao(conn);
        pDao = new PersonDao(conn);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    @Test
    public void clearPass() throws DataAccessException {
        uDao.insert(newUser);
        ClearResult result = ClearService.clear();
        assertNotNull(result);
        assertEquals("Clear succeeded.", result.getMessage());
    }

    @Test
    public void clearPass2() throws DataAccessException {
        uDao.insert(newUser);
        uDao.insert(new User("kimmy", "pass", "kimmy.gibbler@gmail.com", "kimantha",
                "giblish", "f", "IDOFAPERSON"));
        aDao.insert(new AuthToken("12304712398", "9123784091"));
        eDao.insert(new Event("12300", "kimmy", "12783987", (float)35.1, (float)99.99,
                "England", "london", "death of a parent", 1));
        pDao.insert(new Person("id", "kimmy", "kimantha", "giblish", "f",
                "893479", "12394870", "138947"));
        ClearResult result = ClearService.clear();
        assertNotNull(result);
        assertEquals("Clear succeeded.", result.getMessage());
    }
}
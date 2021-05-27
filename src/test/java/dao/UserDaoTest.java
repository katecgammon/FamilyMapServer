package dao;

import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import static org.junit.jupiter.api.Assertions.*;

public class UserDaoTest {
    private Database db;
    private User newUser;
    private UserDao uDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new Database();
        newUser = new User("susan", "Queen", "susan@gmail.com",
                "Susan", "Queen", "female", "12345");
        Connection conn = db.getConnection();
        db.clearTables();
        uDao = new UserDao(conn);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(true);
    }

    @Test
    public void insertPass() throws DataAccessException {
        uDao.insert(newUser);
        User compareTest = uDao.find(newUser.getUsername());
        assertNotNull(compareTest);
        assertEquals(newUser, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        uDao.insert(newUser);
        assertThrows(DataAccessException.class, ()-> uDao.insert(newUser));
    }

    @Test
    public void findPass() throws DataAccessException {
        uDao.insert(newUser);
        User newUser1 = new User("jimmy", "kanyewest", "jimmybuckets@hotmail.net",
                "james", "donkey", "m", "42420");
        User newUser2 = new User("queenliz", "heir2thethrone", "stevemcqueen@gmail.com",
                "steve", "McQueen", "m", "23570");
        uDao.insert(newUser1);
        uDao.insert(newUser2);
        User compareTest = uDao.find("jimmy");
        assertNotNull(compareTest);
        assertEquals(newUser1, compareTest);
    }

    @Test
    public void findFail() throws DataAccessException {
        uDao.insert(newUser);
        User newUser1 = new User("jimmy", "kanyewest", "jimmybuckets@hotmail.net",
                "james", "donkey", "m", "42420");
        User newUser2 = new User("queenliz", "heir2thethrone", "stevemcqueen@gmail.com",
                "steve", "McQueen", "m", "23570");
        uDao.insert(newUser1);
        uDao.insert(newUser2);
        assertNull(uDao.find("kanye"));
    }

    @Test
    public void clearPass() throws DataAccessException {
        uDao.clear();
        assertNull(uDao.find(newUser.getUsername()));
    }

    @Test
    public void clearFromUserPass() throws DataAccessException {
        uDao.insert(newUser);
        User newUser1 = new User("jimmy", "kanyewest", "jimmybuckets@hotmail.net",
                "james", "donkey", "m", "42420");
        User newUser2 = new User("queenliz", "heir2thethrone", "stevemcqueen@gmail.com",
                "steve", "McQueen", "m", "23570");
        uDao.insert(newUser1);
        uDao.insert(newUser2);
        uDao.clearUser("jimmy");
        assertNull(uDao.find("jimmy"));
    }
}


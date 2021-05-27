package dao;

import model.AuthToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class AuthTokenDaoTest {
    Database db;
    AuthTokenDao aDao;
    AuthToken token;

    @BeforeEach
    public void setUp() throws DataAccessException
    {
        db = new Database();
        token = new AuthToken("123456789", "susan");
        Connection conn = db.getConnection();
        db.clearTables();
        aDao = new AuthTokenDao(conn);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        aDao.insert(token);
        AuthToken compareTest = aDao.find(token.getToken());
        assertNotNull(compareTest);
        assertEquals(token, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        aDao.insert(token);
        assertThrows(DataAccessException.class, ()-> aDao.insert(token));
    }

    @Test
    public void findPass() throws DataAccessException {
        aDao.insert(token);
        AuthToken newToken = new AuthToken("128379", "ajksdfhlasjkd");
        AuthToken newToken1 = new AuthToken("99999", "john");
        aDao.insert(newToken);
        aDao.insert(newToken1);
        AuthToken compareTest = aDao.find("99999");
        assertNotNull(compareTest);
        assertEquals(newToken1, compareTest);
    }

    @Test
    public void findFail() throws DataAccessException {
        aDao.insert(token);
        AuthToken newToken = new AuthToken("128379", "ajksdfhlasjkd");
        AuthToken newToken1 = new AuthToken("99999", "john");
        aDao.insert(newToken);
        aDao.insert(newToken1);
        AuthToken compareTest = aDao.find("00000");
        assertNull(compareTest);
    }

    @Test
    public void generateAuthTokenPass() {
        String token = aDao.generateAuthToken();
        assertNotNull(token);
    }

    @Test
    public void clearPass() throws DataAccessException {
        aDao.insert(token);
        AuthToken newToken = new AuthToken("128379", "ajksdfhlasjkd");
        AuthToken newToken1 = new AuthToken("99999", "john");
        aDao.insert(newToken);
        aDao.insert(newToken1);
        aDao.clear();
        assertNull(aDao.find(token.getUsername()));
    }

    @Test
    public void clearFromPersonPass() throws DataAccessException {
        aDao.insert(token);
        AuthToken newToken = new AuthToken("128379", "ajksdfhlasjkd");
        AuthToken newToken1 = new AuthToken("99999", "john");
        aDao.insert(newToken);
        aDao.insert(newToken1);
        aDao.clearUser("john");
        assertNull(aDao.find("99999"));
    }

}

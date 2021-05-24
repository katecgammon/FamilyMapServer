package dao;

import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

//We will use this to test that our insert method is working and failing in the right ways
public class PersonDaoTest {
    private Database db;
    private Person newPerson;
    private PersonDao pDao;

    @BeforeEach
    public void setUp() throws DataAccessException
    {
        db = new Database();
        newPerson = new Person("13579", "susanAngel", "Angel",
                "Susan", "f", "123", "", "");
        Connection conn = db.getConnection();
        db.clearTables();
        pDao = new PersonDao(conn);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        pDao.insert(newPerson);
        Person compareTest = pDao.find(newPerson.getPersonID());
        assertNotNull(compareTest);
        assertEquals(newPerson, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        pDao.insert(newPerson);
        assertThrows(DataAccessException.class, ()-> pDao.insert(newPerson));
    }

    @Test
    public void findPass() throws DataAccessException {
        pDao.insert(newPerson);
        Person newPerson1 = new Person("12345", "kanyeisking", "kanye",
                "west", "m", "00000", "11111", "22222");
        Person newPerson2 = new Person("99999", "bookofmormon", "joseph",
                "smith", "m", "13322", "15577", "98765");
        pDao.insert(newPerson1);
        pDao.insert(newPerson2);
        Person compareTest = pDao.find("99999");
        assertNotNull(compareTest);
        assertEquals(newPerson2, compareTest);
    }

    @Test
    public void findFail() throws DataAccessException {
        pDao.insert(newPerson);
        Person newPerson1 = new Person("12345", "kanyeisking", "kanye",
                "west", "m", "00000", "11111", "22222");
        Person newPerson2 = new Person("99999", "bookofmormon", "joseph",
                "smith", "m", "13322", "15577", "98765");
        pDao.insert(newPerson1);
        pDao.insert(newPerson2);
        assertNull(pDao.find("66666"));
    }

    @Test
    public void clearPass() throws DataAccessException {
        pDao.clear();
        assertNull(pDao.find(newPerson.getPersonID()));
    }
}


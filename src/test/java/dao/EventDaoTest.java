package dao;

import model.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import static org.junit.jupiter.api.Assertions.*;

public class EventDaoTest {
    private Database db;
    private Event bestEvent;
    private EventDao eDao;

    @BeforeEach
    public void setUp() throws DataAccessException
    {
        db = new Database();
        bestEvent = new Event("Biking_123A", "Gale", "Gale123A",
                35.9f, 140.1f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        Connection conn = db.getConnection();
        db.clearTables();
        eDao = new EventDao(conn);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        eDao.insert(bestEvent);
        Event compareTest = eDao.find(bestEvent.getEventID());
        assertNotNull(compareTest);
        assertEquals(bestEvent, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        eDao.insert(bestEvent);
        assertThrows(DataAccessException.class, ()-> eDao.insert(bestEvent));
    }

    @Test
    public void findPass() throws DataAccessException {
        eDao.insert(bestEvent);
        Event newEvent1 = new Event("19238", "kate123", "12345", (float)81.6, (float)-15.3333,
                "Denmark","Nord", "Death", 1850);
        eDao.insert(newEvent1);
        Event compareTest = eDao.find("Biking_123A");
        assertNotNull(compareTest);
        assertEquals(bestEvent, compareTest);
    }

    @Test
    public void findFail() throws DataAccessException {
        eDao.insert(bestEvent);
        Event newEvent1 = new Event("19238", "kate123", "12345", (float)81.6, (float)-15.3333,
                "Denmark","Nord", "Death", 1850);
        eDao.insert(newEvent1);
        Event compareTest = eDao.find("00000");
        assertNull(compareTest);
    }

    @Test
    public void clearFromEventPass() throws DataAccessException {
        eDao.insert(bestEvent);
        Event newEvent1 = new Event("19238", "kate123", "12345", (float)81.6, (float)-15.3333,
                "Denmark","Nord", "Death", 1850);
        eDao.insert(newEvent1);
        eDao.clearUser("kate123");
        assertNull(eDao.find("19238"));
    }
}

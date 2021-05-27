package service;

import dao.*;
import model.AuthToken;
import model.Event;
import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import result.AllEventResult;
import result.EventResult;
import result.PersonResult;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EventServiceTest {
    private Database db;
    EventResult result = new EventResult();
    EventService service = new EventService();
    UserDao uDao;
    User newUser;
    AuthTokenDao aDao;
    AuthToken token;
    EventDao eDao;
    Event newEvent;

    @BeforeEach
    public void setUp() throws DataAccessException
    {
        db = new Database();
        newEvent = new Event("8qw76", "kate123", "12345", (float)82.5, (float)-61.6667,
                "Canada","Alert", "Wedding", 1820);
        newUser = new User("kate123", "password", "kate@yahoo.net", "Kate", "Gammon",
                "f", "12345");
        token = new AuthToken("882393412834719", "kate123");
        Connection conn = db.getConnection();
        db.clearTables();
        eDao = new EventDao(conn);
        uDao = new UserDao(conn);
        aDao = new AuthTokenDao(conn);

    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    @Test
    public void findAllEventsPass() throws DataAccessException {
        AllEventResult result1;
        uDao.insert(newUser);
        aDao.insert(token);
        eDao.insert(newEvent);
        Event newEvent1 = new Event("19238", "kate123", "12345", (float)81.6, (float)-15.3333,
                "Denmark","Nord", "Death", 1850);
        eDao.insert(newEvent1);
        Event[] events = {newEvent, newEvent1};
        result1 = new AllEventResult(events);
        AllEventResult allEventsResult = service.findAllEvents("882393412834719");
        assertNotNull(allEventsResult);
        for (int i = 0; i < allEventsResult.getData().length; i++) {
            assertEquals(result1.getData()[i], allEventsResult.getData()[i]);
        }
    }

    @Test
    public void findAllEventsFail() throws DataAccessException {
        AllEventResult result1;
        uDao.insert(newUser);
        aDao.insert(token);
        eDao.insert(newEvent);
        Event newEvent1 = new Event("19238", "kate123", "12345", (float)81.6, (float)-15.3333,
                "Denmark","Nord", "Death", 1850);
        eDao.insert(newEvent1);
        Event[] events = {newEvent, newEvent1};
        result1 = new AllEventResult(events);
        AllEventResult allEventsResult = service.findAllEvents("1934239280");
        assertEquals("Error: Problem in getting all events", allEventsResult.getMessage());
    }


    @Test
    public void findEventPass() throws DataAccessException {
        uDao.insert(newUser);
        aDao.insert(token);
        eDao.insert(newEvent);
        Event newEvent1 = new Event("19238", "kate123", "12345", (float)81.6, (float)-15.3333,
                "Denmark","Nord", "Death", 1850);
        eDao.insert(newEvent1);
        result = new EventResult("kate123", "19238", "12345", (float)81.6, (float)-15.3333,
                "Denmark", "Nord", "Death", 1850);
        EventResult eventResult = service.find("19238", "882393412834719");
        assertNotNull(eventResult);
        assertEquals(result.getCity(), eventResult.getCity());
    }

    @Test
    public void findEventFail() throws DataAccessException {
        uDao.insert(newUser);
        aDao.insert(token);
        eDao.insert(newEvent);
        Event newEvent1 = new Event("19238", "kate123", "12345", (float)81.6, (float)-15.3333,
                "Denmark","Nord", "Death", 1850);
        eDao.insert(newEvent1);
        result = new EventResult("kate123", "19238", "12345", (float)81.6, (float)-15.3333,
                "Denmark", "Nord", "Death", 1850);
        result.setSuccess(true);
        EventResult eventResult = service.find("289347289", "9q823479128370497");
        assertEquals("Invalid AuthToken", eventResult.getMessage());
    }
}

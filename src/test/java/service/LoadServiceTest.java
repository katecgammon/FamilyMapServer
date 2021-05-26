package service;

import dao.*;
import model.AuthToken;
import model.Event;
import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.LoadRequest;
import result.LoadResult;

import java.sql.Connection;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class LoadServiceTest {
    private Database db;
    LoadService service = new LoadService();
    LoadResult result;
    LoadRequest request;
    UserDao uDao;
    User newUser;
    User newUser1;
    PersonDao pDao;
    Person newPerson;
    Person newPerson1;
    EventDao eDao;
    Event newEvent;
    Event newEvent1;
    User[] userArray;
    Person[] personArray;
    Event[] eventArray;

    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new Database();
        newUser = new User("userName", "PASSword", "susan@gmail.com",
                "Susan", "Queen", "female", "12345");
        newUser1 = new User("kate123", "gammon456", "kate.gammon@gmail.com",
                "Kate", "Gammon", "f", "personID");
        newPerson = new Person("12345", "kate123", "Kate",
                "Gammon", "f", null, null, "1356");
        Person newPerson1 = new Person("19992", "kanyeisking", "Kanye",
                "West", "m", "13423", "15522", "99007");
        newEvent = new Event("8qw76", "kate123", "12345", (float)82.5, (float)-61.6667,
                "Canada","Alert", "Wedding", 1820);
        Event newEvent1 = new Event("19238", "kate123", "12345", (float)81.6, (float)-15.3333,
                "Denmark","Nord", "Death", 1850);
        userArray = new User[] {newUser, newUser1};
        personArray = new Person[] {newPerson, newPerson1};
        eventArray = new Event[] {newEvent, newEvent1};
        Connection conn = db.getConnection();
        db.clearTables();
        uDao = new UserDao(conn);
        eDao = new EventDao(conn);
        pDao = new PersonDao(conn);

    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    @Test
    public void loadPass() throws DataAccessException {
        request = new LoadRequest(userArray, personArray, eventArray);
        LoadResult loadResult = service.load(request);
        assertEquals("Successfully added 2 users, 2 persons, and 2 events to the database.", loadResult.getMessage());
    }

    @Test
    public void loadFail() throws DataAccessException {
        Arrays.fill(userArray, null);
        userArray = new User[] {newUser, newUser};
        request = new LoadRequest(userArray, personArray, eventArray);
        LoadResult loadResult = service.load(request);
        assertEquals("Error: Could not load", loadResult.getMessage());
    }
}

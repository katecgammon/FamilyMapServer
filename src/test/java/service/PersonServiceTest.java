package service;

import dao.*;
import model.AuthToken;
import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import result.PersonResult;
import java.sql.Connection;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PersonServiceTest {
    private Database db;
    PersonResult result = new PersonResult();
    PersonService service = new PersonService();
    Person newPerson;
    PersonDao pDao;
    UserDao uDao;
    User newUser;
    AuthTokenDao aDao;
    AuthToken token;

    @BeforeEach
    public void setUp() throws DataAccessException
    {
        db = new Database();
        newPerson = new Person("12345", "kate123", "Kate",
                "Gammon", "f", null, null, "1356");
        newUser = new User("kate123", "password", "kate@yahoo.net", "Kate", "Gammon",
                "f", "12345");
        token = new AuthToken("882393412834719", "kate123");
        Connection conn = db.getConnection();
        db.clearTables();
        pDao = new PersonDao(conn);
        uDao = new UserDao(conn);
        aDao = new AuthTokenDao(conn);

    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }
//TODO: Write fail tests
    @Test
    public void findAllPeoplePass() throws DataAccessException {
        uDao.insert(newUser);
        pDao.insert(newPerson);
        aDao.insert(token);
        Person newPerson1 = new Person("19992", "kanyeisking", "Kanye",
                "West", "m", "13423", "15522", "99007");
        pDao.insert(newPerson1);
        Person[] persons = {newPerson};
        result = new PersonResult(persons);
        PersonResult allPeopleResult = service.findAllPeople("882393412834719");
        assertNotNull(allPeopleResult);
        for (int i = 0; i < allPeopleResult.getPeople().length; i++) {
            assertEquals(result.getPeople()[i], allPeopleResult.getPeople()[i]);
        }
    }

    @Test
    public void findAllPeopleFail() throws DataAccessException {
        uDao.insert(newUser);
        pDao.insert(newPerson);
        aDao.insert(token);
        Person newPerson1 = new Person("19992", "kanyeisking", "Kanye",
                "West", "m", "13423", "15522", "99007");
        pDao.insert(newPerson1);
        Person[] persons = {newPerson};
        result = new PersonResult(persons);
        PersonResult allPeopleResult = service.findAllPeople("1934239280");
        assertEquals("Error: Invalid AuthToken", allPeopleResult.getMessage());
    }

    @Test
    public void findPersonPass() throws DataAccessException {
        uDao.insert(newUser);
        pDao.insert(newPerson);
        aDao.insert(token);
        Person newPerson1 = new Person("19992", "kanyeisking", "Kanye",
                "West", "m", "13423", "15522", "99007");
        pDao.insert(newPerson1);
        Person person = newPerson;
        result = new PersonResult("kate123", "12345", "Kate", "Gammon",
                "f", null, null, "1356");
        result.setSuccess(true);
        PersonResult personResult = service.findPerson("12345", "882393412834719");
        assertNotNull(personResult);
        assertEquals(result.getFirstName(), personResult.getFirstName());
    }

    @Test
    public void findPersonFail() throws DataAccessException {
        uDao.insert(newUser);
        pDao.insert(newPerson);
        aDao.insert(token);
        Person newPerson1 = new Person("19992", "kanyeisking", "Kanye",
                "West", "m", "13423", "15522", "99007");
        pDao.insert(newPerson1);
        Person person = newPerson;
        result = new PersonResult("kate123", "12345", "Kate", "Gammon",
                "f", null, null, "1356");
        result.setSuccess(true);
        PersonResult personResult = service.findPerson("289347289", "9q823479128370497");
        assertEquals("Error: Invalid AuthToken", personResult.getMessage());
    }
}

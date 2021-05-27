package service;

import dao.*;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.FillRequest;
import result.FillResult;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.FileNotFoundException;
import java.sql.Connection;

public class FillServiceTest {
    private Database db;
    FillService service = new FillService();
    FillResult result;
    FillRequest request;
    UserDao uDao;
    User newUser;
    AuthTokenDao aDao;
    PersonDao pDao;
    EventDao eDao;

    public FillServiceTest() throws FileNotFoundException {
    }

    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new Database();
        newUser = new User("kate123", "gammon456", "kate.gammon@gmail.com",
                "Kate", "Gammon", "f", "personID");
        request = new FillRequest("kate123", 4);
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
    public void fillPass() throws DataAccessException, FileNotFoundException {
        uDao.insert(newUser);
        result = service.fill(request);
        assertEquals("Successfully added 31 persons and 91 events to the database.", result.getMessage());
    }

    @Test
    public void fillFail() throws FileNotFoundException, DataAccessException {
        uDao.insert(newUser);
        FillRequest fillRequest = new FillRequest("harrystyles", 2);
        result = service.fill(fillRequest);
        assertEquals("Error: Username doesn't exist", result.getMessage());
    }
}

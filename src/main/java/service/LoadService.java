package service;

import dao.*;
import model.AuthToken;
import model.Event;
import model.Person;
import model.User;
import request.LoadRequest;
import result.LoadResult;
import result.LoginResult;

import java.sql.Connection;

public class LoadService {
    /**
     *
     * Clears all data from the database,
     * and then loads the posted user, person, and event data into the database.
     *
     * @param r
     * @return a success message or it throws an error.
     */
    //TODO: Test this!!!!
    public LoadResult load(LoadRequest r) throws DataAccessException {
        Database db = new Database();
        Connection conn = db.getConnection();
        UserDao userDao = new UserDao(conn);
        PersonDao personDao = new PersonDao(conn);
        EventDao eventDao = new EventDao(conn);
        LoadResult result = new LoadResult();

        try {
            db.clearTables();
            User[] userArray = r.getUsers();
            Person[] personArray = r.getPersons();
            Event[] eventArray =r.getEvents();
            for (int i = 0; i < userArray.length; i++) {
                userDao.insert(userArray[i]);
            }
            for (int i = 0; i < personArray.length; i++) {
                personDao.insert(personArray[i]);
            }
            for (int i = 0; i < eventArray.length; i++) {
                eventDao.insert(eventArray[i]);
            }

            db.closeConnection(true);
            result = new LoadResult(userArray.length, personArray.length, eventArray.length);
            result.setMessage("Successfully added " + result.getNumUsers() + " users, " + result.getNumPersons() +
                    " persons, and " + result.getNumEvents() + " events to the database.");
            result.setSuccess(true);

        }
        catch (DataAccessException ex) {
            result.setSuccess(false);
            result.setMessage("Error: Could not load");
            db.closeConnection(false);
        }
        return result;
    }
}

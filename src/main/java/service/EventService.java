package service;

import dao.*;
import model.AuthToken;
import model.Event;
import model.Person;
import result.AllEventResult;
import result.EventResult;
import result.PersonResult;

import java.sql.Connection;
import java.util.ArrayList;

public class EventService {

    /**
     *
     * Returns all the events for all the members in the family. Or it throws an error.
     *
     * @return an array of Event objects and a success message or it throws an error.
     */
    public AllEventResult findAllEvents(String authToken) throws DataAccessException {
        Database db = new Database();
        Connection conn = db.getConnection();
        EventDao eventDao = new EventDao(conn);
        ArrayList<Event> events;
        AllEventResult result = new AllEventResult();
        AuthTokenDao aDao = new AuthTokenDao(conn);

        try {
            AuthToken foundToken = aDao.find(authToken);
            if (foundToken == null) {
                throw new DataAccessException("Invalid AuthToken");
            }
            events = eventDao.getAllEvents(aDao.find(authToken).getUsername());
            if (events == null) {
                throw new DataAccessException("Invalid AuthToken");
            }
            Event[] eventArr = new Event[events.size()];
            eventArr = events.toArray(eventArr);
            db.closeConnection(true);
            result = new AllEventResult(eventArr);
            result.setSuccess(true);

        }
        catch (DataAccessException ex) {
            result.setSuccess(false);
            if (ex.toString().equals("dao.DataAccessException: Invalid AuthToken")) {
                result.setMessage("Error: Invalid AuthToken");
            }
            else {
                result.setMessage("Error: Problem in getting all events");
            }
            db.closeConnection(false);
        }
        return result;
    }



    /**
     *
     * Searches the event table and returns the event with the correct ID.
     *
     * @return the single event object with the specified ID
     */
    public EventResult find(String eventID, String authToken) throws DataAccessException {
        Database db = new Database();
        Connection conn = db.getConnection();
        EventDao eventDao = new EventDao(conn);
        EventResult result = new EventResult();
        AuthTokenDao aDao = new AuthTokenDao(conn);

        try {
            Event event;
            if (aDao.find(authToken) == null) {
                throw new DataAccessException("Invalid AuthToken");
            }
            event = eventDao.find(eventID);
            if (event == null) {
                throw new DataAccessException("Invalid PersonID");
            }
            String usernameToCompare = aDao.find(authToken).getUsername();
            if (!event.getUsername().equals(usernameToCompare)) {
                throw new DataAccessException("Requested person does not belong to this user");
            }
            db.closeConnection(true);
            result = new EventResult(event.getUsername(), event.getEventID(), event.getPersonID(), event.getLatitude(),
                    event.getLongitude(), event.getCountry(), event.getCity(), event.getEventType(), event.getYear());
            result.setSuccess(true);

        }
        catch (DataAccessException ex) {
            result.setSuccess(false);
            if (ex.toString().equals("dao.DataAccessException: Invalid AuthToken")) {
                result.setMessage("Error: Invalid AuthToken");
            }
            else if (ex.toString().equals("dao.DataAccessException: Invalid PersonID")) {
                result.setMessage("Error: Invalid PersonID");
            }
            else if (ex.toString().equals("dao.DataAccessException: Requested person does not belong to this user")) {
                result.setMessage("Error: Requested person does not belong to this user");
            }
            else {
                result.setMessage("Error: Problem in getting person");
            }
            db.closeConnection(false);
        }
        return result;
    }
}

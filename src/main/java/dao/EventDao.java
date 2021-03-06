package dao;

import model.Event;
import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

public class EventDao {
    private final Connection conn;

    public EventDao(Connection conn)
    {
        this.conn = conn;
    }

    public String generateEventID() {
        UUID ID = UUID.randomUUID();
        return ID.toString();
    }

    /**
     * inserts a new Event into the event table
     * @param event
     * @throws DataAccessException
     */
    public void insert(Event event) throws DataAccessException {

        if (eventAlreadyExists((event.getPersonID()))) {
            throw new DataAccessException("Event already exists!");
        }
        String sql = "INSERT INTO Events (EventID, AssociatedUsername, PersonID, Latitude, Longitude, " +
                "Country, City, EventType, Year) VALUES(?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, event.getEventID());
            stmt.setString(2, event.getUsername());
            stmt.setString(3, event.getPersonID());
            stmt.setFloat(4, event.getLatitude());
            stmt.setFloat(5, event.getLongitude());
            stmt.setString(6, event.getCountry());
            stmt.setString(7, event.getCity());
            stmt.setString(8, event.getEventType());
            stmt.setInt(9, event.getYear());

            stmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     *
     * @param eventID
     * @return event with the matching eventID
     * @throws DataAccessException
     */
    public Event find(String eventID) throws DataAccessException {
        Event event;
        ResultSet rs = null;
        String sql = "SELECT * FROM Events WHERE EventID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eventID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                event = new Event(rs.getString("EventID"), rs.getString("AssociatedUsername"),
                        rs.getString("PersonID"), rs.getFloat("Latitude"), rs.getFloat("Longitude"),
                        rs.getString("Country"), rs.getString("City"), rs.getString("EventType"),
                        rs.getInt("Year"));
                return event;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     *
     * @throws DataAccessException
     *
     * Drops the events table
     */
    void clear() throws DataAccessException{
        String sql = "DELETE FROM Events;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while clearing the Events table");
        }
    }

    /**
     *
     * @throws DataAccessException
     *
     * Deletes Events that are related to the username
     */
    public void clearUser(String username) throws DataAccessException {
        String sql = "DELETE FROM Events WHERE associatedUsername = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while clearing Events table");
        }
    }

    /**
     *
     * @param eventID
     * @return true or false whether the Event already exists
     */
    boolean eventAlreadyExists(String eventID) throws DataAccessException {
        if (find(eventID) != null) {
            return true;
        }
        else return false;
    }

    /**
     *
     * @param username of user
     * @return ArrayList of all the events related to the user
     * @throws DataAccessException
     */
    public ArrayList<Event> getAllEvents(String username) throws DataAccessException {
        ArrayList<Event> events = new ArrayList<Event>();
        ResultSet rs = null;
        String sql = "SELECT * FROM Events WHERE AssociatedUsername = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Event event;
                event = new Event(rs.getString("EventID"), rs.getString("AssociatedUsername"),
                        rs.getString("PersonID"), rs.getFloat("Latitude"), rs.getFloat("Longitude"),
                        rs.getString("Country"), rs.getString("City"), rs.getString("EventType"),
                        rs.getInt("Year"));
                events.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while getting all events");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return events;
    }
}

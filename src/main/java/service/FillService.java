package service;


import dao.*;
import request.FillRequest;
import result.FillResult;
import result.LoadResult;

import javax.management.monitor.GaugeMonitor;
import java.sql.Connection;

public class FillService {

    /**
     *
     * Takes a username and number of generations and populates the server's database
     * with generated data for the specified username.
     *
     * @param r
     * @return a success message or an error message.
     */
    public FillResult fill(FillRequest r) throws DataAccessException {
        Database db = new Database();
        Connection conn = db.getConnection();
        UserDao userDao = new UserDao(conn);
        PersonDao personDao = new PersonDao(conn);
        AuthTokenDao authTokenDao = new AuthTokenDao(conn);
        EventDao eventDao = new EventDao(conn);
        FillResult result = new FillResult();

        try{
            if (userDao.find(r.getUsername()) == null) {
                throw new DataAccessException("Username doesn't exist");
            }
            userDao.clearUser(r.getUsername());
            personDao.clearUser(r.getUsername());
            authTokenDao.clearUser(r.getUsername());
            eventDao.clearUser(r.getUsername());

            //Now actually fill the tables

        } catch (DataAccessException ex) {
            result.setSuccess(false);
            if (ex.toString().equals("dao.DataAccessException: Username doesn't exist")) {
                result.setMessage("Error: Username doesn't exist");
            }
            else {
                result.setMessage("Error: Could not fill");
            }
            db.closeConnection(false);
        }


        return result;
    }
}

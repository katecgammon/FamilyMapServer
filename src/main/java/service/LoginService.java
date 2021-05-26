package service;

import dao.AuthTokenDao;
import dao.DataAccessException;
import dao.Database;
import dao.UserDao;
import model.AuthToken;
import request.LoginRequest;
import result.LoginResult;

import java.sql.Connection;

public class LoginService {

    /**
     *
     * Takes in a username and a password and returns an auth token, username, personID, and success message.
     *
     * @param r
     * @return an auth token, username, personID, and success message or an error message.
     */
    public LoginResult login(LoginRequest r) throws DataAccessException {
        Database db = new Database();
        Connection conn = db.getConnection();
        UserDao userDao = new UserDao(conn);
        AuthTokenDao authTokenDao = new AuthTokenDao(conn);
        LoginResult result = new LoginResult();

        try {

            //Check if username even exists
            if (!userDao.userAlreadyExists(r.getUsername())) {
                throw new DataAccessException("User doesn't exist");
            }
            if (!userDao.verify(r.getUsername(), r.getPassword())) {
                throw new DataAccessException("Username and Password don't match");
            }
            String personID = userDao.find(r.getUsername()).getPersonID();
            AuthToken authToken = new AuthToken(authTokenDao.generateAuthToken(), personID);
            authTokenDao.insert(authToken);
            

            db.closeConnection(true);
            result = new LoginResult(authToken.getToken(), r.getUsername(), personID);
            result.setSuccess(true);

        }
        catch (DataAccessException ex) {
            result.setSuccess(false);
            if (ex.toString().equals("dao.DataAccessException: User doesn't exist")) {
                result.setMessage("Error: User doesn't exist");
            }
            else if (ex.toString().equals("dao.DataAccessException: Username and Password don't match")) {
                result.setMessage("Error: Username and Password don't match");
            }
            else {
                result.setMessage("Error: Could not login");
            }

            db.closeConnection(false);
        }
        return result;
    }

}

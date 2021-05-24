package service;

import dao.AuthTokenDao;
import dao.DataAccessException;
import dao.Database;
import dao.UserDao;
import model.AuthToken;
import model.User;
import request.RegisterRequest;
import result.RegisterResult;

import java.sql.Connection;

public class RegisterService {
    /**
     *
     * When a user registers they enter their username, password, email,
     * first name, last name, and gender. If it is successful, it returns their auth token,
     * username, personID, and a success statement. If it doesn't work, it throws an error.
     *
     *
     * @param r
     * @return RegisterResult that gives info or throws an error
     */
    public RegisterResult register(RegisterRequest r) throws DataAccessException {
        Database db = new Database();
        Connection conn = db.getConnection();
        UserDao userDao = new UserDao(conn);
        RegisterResult result = new RegisterResult();

        try {
            //db.openConnection();

            if (userDao.userAlreadyExists(r.getUsername())) {
                throw new DataAccessException("Username already exists.");
            }
            //Connection conn1 = db.getConnection();
            String personID = userDao.generatePersonID();
            AuthTokenDao authTokenDao = new AuthTokenDao(conn);
            AuthToken authToken = new AuthToken(authTokenDao.generateAuthToken(), r.getUsername());
            authTokenDao.insert(authToken);

            User newUser = new User(r.getUsername(), r.getPassword(), r.getEmail(), r.getFirstName(),
                                    r.getLastName(), r.getGender(), personID);
            userDao.insert(newUser);


            db.closeConnection(true);
            result = new RegisterResult(authToken.getToken(), r.getUsername(), personID);
            result.setSuccess(true);

        }
        catch (DataAccessException ex) {
            //TODO: This line doesn't work because result is null.
            result.setSuccess(false);
            result.setMessage("Error: Could not register");
            db.closeConnection(false);
        }
        return result;
    }
}

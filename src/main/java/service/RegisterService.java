package service;

import dao.*;
import model.AuthToken;
import model.Person;
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
        PersonDao pDao = new PersonDao(conn);
        RegisterResult result = new RegisterResult();

        try {

            if (userDao.userAlreadyExists(r.getUsername())) {
                throw new DataAccessException("Username already exists.");
            }

            String personID = userDao.generatePersonID();
            AuthTokenDao authTokenDao = new AuthTokenDao(conn);
            AuthToken authToken = new AuthToken(authTokenDao.generateAuthToken(), r.getUsername());
            authTokenDao.insert(authToken);

            User newUser = new User(r.getUsername(), r.getPassword(), r.getEmail(), r.getFirstName(),
                                    r.getLastName(), r.getGender(), personID);
            userDao.insert(newUser);
            Person newPerson = new Person(personID, r.getUsername(), r.getFirstName(), r.getLastName(),
                    r.getGender(), null, null, null);
            pDao.insert(newPerson);


            db.closeConnection(true);
            result = new RegisterResult(authToken.getToken(), r.getUsername(), personID);
            result.setSuccess(true);
            result.setMessage(null);

        }
        catch (DataAccessException ex) {
            result.setSuccess(false);
            if (ex.toString().equals("dao.DataAccessException: Username already exists.")) {
                result.setMessage("Error: Username already exists");
            }
            else {
                result.setMessage("Error: Could not register");
            }

            db.closeConnection(false);
        }
        return result;
    }
}

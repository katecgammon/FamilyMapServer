package service;

import com.google.gson.Gson;
import dao.*;
import data.Location;
import data.LocationData;
import model.AuthToken;
import model.Event;
import model.Person;
import model.User;
import request.FillRequest;
import request.RegisterRequest;
import result.RegisterResult;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.util.Random;

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
        EventDao eventDao = new EventDao(conn);
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

            FillService fillService = new FillService();
            FillRequest fillRequest = new FillRequest(r.getUsername(), 4);
            fillService.fill(fillRequest);

            //CREATES A BIRTH EVENT FOR A NEW USER
//            Gson gson = new Gson();
//            Reader reader = new FileReader("json/locations.json");
//
//            LocationData locData = gson.fromJson(reader, LocationData.class);
//            Random rand1 = new Random();
//            int randIndex = rand1.nextInt(977);
//            Random rand = new Random();
//            int randYear = rand.nextInt(2021 - 1902) + 1902;
//            Location loc = locData.getLocationAt(randIndex);
//            pDao.insert(newPerson);
//            Event userBirth = new Event(eventDao.generateEventID(), newUser.getUsername(), newUser.getPersonID(),
//                    loc.getLatitude(), loc.getLongitude(), loc.getCountry(), loc.getCity(), "birth", randYear);
//            eventDao.insert(userBirth);

            //TODO: Create a birth event!


            db.closeConnection(true);
            result = new RegisterResult(authToken.getToken(), r.getUsername(), personID);
            result.setSuccess(true);
            result.setMessage(null);

        }
        catch (DataAccessException | FileNotFoundException ex) {
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

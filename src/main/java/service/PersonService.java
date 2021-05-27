package service;

import dao.*;
import model.AuthToken;
import model.Person;
import result.PersonResult;
import java.sql.Connection;
import java.util.ArrayList;

public class PersonService {

    /**
     *
     * Finds all of the family members of the user
     *
     * @return array of person objects and success message. Or throws an error.
     */
    public PersonResult findAllPeople(String authToken) throws DataAccessException {
        Database db = new Database();
        Connection conn = db.getConnection();
        PersonDao personDao = new PersonDao(conn);
        ArrayList<Person> persons;
        PersonResult result = new PersonResult();
        AuthTokenDao aDao = new AuthTokenDao(conn);

        try {
            AuthToken foundToken = aDao.find(authToken);
            if (foundToken == null) {
                throw new DataAccessException("Invalid AuthToken");
            }
            persons = personDao.getAllPersons(foundToken.getUsername());
            if (persons == null) {
                throw new DataAccessException("Invalid AuthToken");
            }
            Person[] personArr = new Person[persons.size()];
            personArr = persons.toArray(personArr);
            db.closeConnection(true);
            result = new PersonResult(personArr);
            result.setSuccess(true);
        }
        catch (DataAccessException ex) {
            result.setSuccess(false);
            if (ex.toString().equals("dao.DataAccessException: Invalid AuthToken")) {
                result.setMessage("Error: Invalid AuthToken");
            }
            else {
                result.setMessage("Error: Problem in getting all persons");
            }
            db.closeConnection(false);
        }
        return result;
    }


    /**
     *
     * Searches the Person table and returns the Person with the correct ID.
     *
     * @return the single Person object with the specified ID
     */
    public PersonResult findPerson(String personID, String authToken) throws DataAccessException {
        Database db = new Database();
        Connection conn = db.getConnection();
        PersonDao personDao = new PersonDao(conn);
        PersonResult result = new PersonResult();
        AuthTokenDao aDao = new AuthTokenDao(conn);

        try {
            Person person;
            if (aDao.find(authToken) == null) {
                throw new DataAccessException("Invalid AuthToken");
            }
            person = personDao.find(personID);
            if (person == null) {
                throw new DataAccessException("Invalid PersonID");
            }
            String usernameToCompare = aDao.find(authToken).getUsername();
            if (!person.getAssociatedUsername().equals(usernameToCompare)) {
                throw new DataAccessException("Requested person does not belong to this user");
            }
            db.closeConnection(true);
            result = new PersonResult(person.getAssociatedUsername(), person.getPersonID(), person.getFirstName(),
                    person.getLastName(), person.getGender(), person.getFatherID(), person.getMotherID(), person.getSpouseID());
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

package service;

import dao.*;
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
    public PersonResult findAllPeople() throws DataAccessException {
        Database db = new Database();
        Connection conn = db.getConnection();
        PersonDao personDao = new PersonDao(conn);
        ArrayList<Person> persons;
        PersonResult result = new PersonResult();
        AuthTokenDao aDao = new AuthTokenDao(conn);

        try {
            //TODO: How do I get the authtoken from the input from the user?
            persons = personDao.getAllPersons(aDao.find("12345").getUsername());
            Person[] personArray = (Person[]) persons.toArray();
            db.closeConnection(true);
            result = new PersonResult(personArray);
            result.setSuccess(true);

        }
        catch (DataAccessException ex) {
            result.setSuccess(false);
            result.setMessage("Error: Problem in getting all persons");
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
    public PersonResult findPerson(String personID) throws DataAccessException {
        Database db = new Database();
        Connection conn = db.getConnection();
        PersonDao personDao = new PersonDao(conn);
        PersonResult result = new PersonResult();

        try {
            Person person;
            person = personDao.find(personID);
            db.closeConnection(true);
            result = new PersonResult(person.getAssociatedUsername(), person.getPersonID(), person.getFirstName(),
                    person.getLastName(), person.getGender(), person.getFatherID(), person.getMotherID(), person.getSpouseID());
            result.setSuccess(true);

        }
        catch (DataAccessException ex) {
            result.setSuccess(false);
            result.setMessage("Error: Problem in getting person");
            db.closeConnection(false);
        }
        return result;
    }
}

package service;

import com.google.gson.Gson;
import dao.*;
import data.Location;
import data.LocationData;
import data.NameData;
import model.Event;
import model.Person;
import model.User;
import request.FillRequest;
import result.FillResult;
import result.LoginResult;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.util.Random;

public class FillService {
    int numPersons = 0;
    int numEvents = 0;

    int numGen = 4;
    PersonDao personDao;
    Person currentPerson;
    EventDao eventDao;
    Gson gson = new Gson();

    Reader reader = new FileReader("json/locations.json");
    LocationData locData = gson.fromJson(reader, LocationData.class);
    Reader reader1 = new FileReader("json/fnames.json");
    NameData fNameData = gson.fromJson(reader1, NameData.class);
    Reader reader2 = new FileReader("json/mnames.json");
    NameData mNameData = gson.fromJson(reader2, NameData.class);
    Reader reader3 = new FileReader("json/snames.json");
    NameData sNameData = gson.fromJson(reader3, NameData.class);

    public FillService () throws FileNotFoundException {}


    /**
     *
     * Takes a username and number of generations and populates the server's database
     * with generated data for the specified username.
     *
     * @param r
     * @return a success message or an error message.
     */
    public FillResult fill(FillRequest r) throws DataAccessException, FileNotFoundException {

        Database db = new Database();
        Connection conn = db.getConnection();
        UserDao userDao = new UserDao(conn);
        personDao = new PersonDao(conn);
        AuthTokenDao authTokenDao = new AuthTokenDao(conn);
        eventDao = new EventDao(conn);
        FillResult result = new FillResult();
        numGen = r.getNumGenerations();


        try{
            if (userDao.find(r.getUsername()) == null) {
                throw new DataAccessException("Username doesn't exist");
            }
            //userDao.clearUser(r.getUsername());
            personDao.clearUser(r.getUsername());
            //authTokenDao.clearUser(r.getUsername());
            eventDao.clearUser(r.getUsername());

            //Create currentUser into a new Person
            User currentUser = userDao.find(r.getUsername());
            currentPerson = new Person(currentUser.getPersonID(), currentUser.getUsername(), currentUser.getFirstName(),
                    currentUser.getLastName(), currentUser.getGender(), null, null, null);
            Location loc = locData.getLocationAt(randNumber("location"));
            //TODO: Figure out how to get the year.
            Event userBirth = new Event(eventDao.generateEventID(), currentUser.getUsername(), currentUser.getPersonID(),
                    loc.getLatitude(), loc.getLongitude(), loc.getCountry(), loc.getCity(), "birth", randYear(1901, 2021));

            createParents(currentPerson, 0, userBirth.getYear());
            eventDao.insert(userBirth);
            numEvents++;
            personDao.insert(currentPerson);
            numPersons++;

            db.closeConnection(true);
            result = new FillResult();
            result.setMessage("Successfully added " + numPersons + " persons and " + numEvents + " events to the database.");
            result.setSuccess(true);




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

    private void createParents(Person child, int currentGen, int childBirthYear) throws DataAccessException {
        if (currentGen < numGen) {
            Person mother = new Person(personDao.generatePersonID(), currentPerson.getAssociatedUsername(), fNameData.getNameAt(randNumber("fname")),
                    sNameData.getNameAt(randNumber("sname")), "f", null, null, null);
            Person father = new Person(personDao.generatePersonID(), currentPerson.getAssociatedUsername(), mNameData.getNameAt(randNumber("mname")),
                    sNameData.getNameAt(randNumber("sname")), "m", null, null, mother.getPersonID());


            child.setMotherID(mother.getPersonID());
            child.setFatherID(father.getPersonID());
            mother.setSpouseID(father.getPersonID());
            //TODO: Create events

            //CREATES 3 RANDOM EVENTS FOR EACH PARENT
            Location momBirthLocation = locData.getLocationAt(randNumber("location"));
            Location momDeathLocation = locData.getLocationAt(randNumber("location"));
            Location marriage = locData.getLocationAt(randNumber("location"));
            Location dadBirthLocation = locData.getLocationAt(randNumber("location"));
            Location dadDeathLocation = locData.getLocationAt(randNumber("location"));
            Event motherBirth = new Event(eventDao.generateEventID(), currentPerson.getAssociatedUsername(), mother.getPersonID(),
                    momBirthLocation.getLatitude(), momBirthLocation.getLongitude(), momBirthLocation.getCountry(),
                    momBirthLocation.getCity(), "birth", randYear(childBirthYear - 50, childBirthYear - 25));
            Event fatherBirth = new Event(eventDao.generateEventID(), currentPerson.getAssociatedUsername(), father.getPersonID(),
                    dadBirthLocation.getLatitude(), dadBirthLocation.getLongitude(), dadBirthLocation.getCountry(),
                    dadBirthLocation.getCity(), "birth", randYear(childBirthYear - 50, childBirthYear - 25));
            int minMarriageYear;
            int maxMarriageYear;
            if (motherBirth.getYear() > fatherBirth.getYear()) {
                minMarriageYear = motherBirth.getYear() + 13;
            }
            else {
                minMarriageYear = fatherBirth.getYear();
            }
            Event motherMarriage = new Event(eventDao.generateEventID(), currentPerson.getAssociatedUsername(), mother.getPersonID(),
                    marriage.getLatitude(), marriage.getLongitude(), marriage.getCountry(),
                    marriage.getCity(), "marriage", randYear(minMarriageYear, minMarriageYear + 67)); //between 13 and 100
            Event fatherMarriage = new Event(eventDao.generateEventID(), currentPerson.getAssociatedUsername(), father.getPersonID(),
                    marriage.getLatitude(), marriage.getLongitude(), marriage.getCountry(),
                    marriage.getCity(), "marriage", motherMarriage.getYear()); //between 13 and 100
            Event motherDeath = new Event(eventDao.generateEventID(), currentPerson.getAssociatedUsername(), mother.getPersonID(),
                    momDeathLocation.getLatitude(), momDeathLocation.getLongitude(), momDeathLocation.getCountry(),
                    momDeathLocation.getCity(), "death", randYear(motherMarriage.getYear(), motherBirth.getYear() + 120));
            Event fatherDeath = new Event(eventDao.generateEventID(), currentPerson.getAssociatedUsername(), father.getPersonID(),
                    dadDeathLocation.getLatitude(), dadDeathLocation.getLongitude(), dadDeathLocation.getCountry(),
                    dadDeathLocation.getCity(), "death", randYear(fatherMarriage.getYear(), fatherBirth.getYear() + 120));
            //BIRTH: min would be child's birth minus 25, max would be child's birth minus 50
            //DEATH: min would be after marriage, max would be birth + 120
            //MARRIAGE: min would be birth + 13, max would be the least of the two deaths.


            createParents(mother, currentGen + 1, motherBirth.getYear());
            createParents(father, currentGen + 1, fatherBirth.getYear());
            personDao.insert(mother);
            personDao.insert(father);
            eventDao.insert(motherBirth);
            eventDao.insert(fatherBirth);
            eventDao.insert(motherMarriage);
            eventDao.insert(fatherMarriage);
            eventDao.insert(motherDeath);
            eventDao.insert(fatherDeath);
            numEvents += 6;
            numPersons += 2;
        }




    }

    private int randNumber(String type) {
        int upperbound = 0;
        if (type.equals("location")) {
            upperbound = 977;
        }
        else if (type.equals ("fname")) {
            upperbound = 146;
        }
        else if (type.equals("mname")) {
            upperbound = 143;
        }
        else if (type.equals("sname")) {
            upperbound = 151;
        }
        Random rand = new Random();
        int int_random = rand.nextInt(upperbound);
        return int_random;
    }

    private int randYear(int min, int max) {
        //BIRTH: min would be child's birth minus 25, max would be child's birth minus 50
        //DEATH: min would be after marriage, max would be birth + 120
        //MARRIAGE: min would be birth + 13, max would be the least of the two deaths.
        Random rand = new Random();
        return rand.nextInt(max - min) + min;
    }
}

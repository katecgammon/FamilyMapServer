package model;

import java.util.Objects;

public class Person {
    private String personID;
    private String associatedUsername;
    private String firstName;
    private String lastName;
    private String gender;
    private String fatherID = null;
    private String motherID = null;
    private String spouseID = null;

    public Person(String personID, String associatedUsername, String firstName, String lastName,
                  String gender, String fatherID, String motherID, String spouseID) {
        this.personID = personID;
        this.associatedUsername = associatedUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
    }

    /**
     * Get the Person ID
     */
    public String getPersonID() {
        return personID;
    }

    /**
     * Set the Person ID
     */
    public void setPersonID(String personID) {
        this.personID = personID;
    }

    /**
     * Get the associated username
     */
    public String getAssociatedUsername() {
        return associatedUsername;
    }

    /**
     * Set the associated username
     */
    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    /**
     * Get the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Set the first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Get the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Set the last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Get the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * Set the gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Get the Father ID
     */
    public String getFatherID() {
        return fatherID;
    }

    /**
     * Set the Father ID
     */
    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    /**
     * Get the Mother ID
     */
    public String getMotherID() {
        return motherID;
    }

    /**
     * Set the Mother ID
     */
    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    /**
     * Get the Spouse ID
     */
    public String getSpouseID() {
        return spouseID;
    }

    /**
     * Set the Spouse ID
     */
    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return personID.equals(person.personID) &&
                associatedUsername.equals(person.associatedUsername) &&
                firstName.equals(person.firstName) &&
                lastName.equals(person.lastName) &&
                gender.equals(person.gender) &&
                Objects.equals(fatherID, person.fatherID) &&
                Objects.equals(motherID, person.motherID) &&
                Objects.equals(spouseID, person.spouseID);
    }

}

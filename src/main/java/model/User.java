package model;

import java.util.Objects;

public class User {
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;
    private String personID;

    public User(String username, String password, String email, String firstName,
                String lastName, String gender, String personID) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.personID = personID;
    }

    /**
     * Get the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the email
     */
    public void setEmail(String email) {
        this.email = email;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals(user.username) &&
                password.equals(user.password) &&
                email.equals(user.email) &&
                firstName.equals(user.firstName) &&
                lastName.equals(user.lastName) &&
                gender.equals(user.gender) &&
                personID.equals(user.personID);
    }

}

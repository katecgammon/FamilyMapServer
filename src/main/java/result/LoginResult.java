package result;

public class LoginResult {
    private String authToken;
    private String username;
    private String personID;
    private String message;
    private boolean success = false;

    public LoginResult(String token, String username, String personID) {
        this.authToken = token;
        this.username = username;
        this.personID = personID;
    }

    public LoginResult() {}

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String token) {
        this.authToken = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }

    public void setMessage(String message) {
        this.message = message;
    }
}
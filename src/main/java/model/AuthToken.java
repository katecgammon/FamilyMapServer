package model;

import java.util.Objects;
import java.util.UUID;

public class AuthToken {
    private String authToken;
    private String username;

    public AuthToken(String token, String username) {
        this.authToken = token;
        this.username = username;
    }

    public String generateAuthToken() {
        UUID token = UUID.randomUUID();
        return token.toString();
    }

    public String getToken() { return authToken; }

    public void setToken(String token) {
        this.authToken = token;
    }

    public String getUsername() {
        return username;
    }

    public void setPersonID(String personID) {
        this.username = personID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthToken authToken1 = (AuthToken) o;
        return Objects.equals(authToken, authToken1.authToken) && Objects.equals(username, authToken1.username);
    }
}

package result;

public class LoadResult {
    private String message;
    private int numUsers = 0;
    private int numPersons = 0;
    private int numEvents = 0;
    private boolean success = false;

    public LoadResult(int numUsers, int numPersons, int numEvents) {
        this.numUsers = numUsers;
        this.numPersons = numPersons;
        this.numEvents = numEvents;
    }

    public LoadResult() {}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getNumUsers() {
        return numUsers;
    }

    public void setNumUsers(int numUsers) {
        this.numUsers = numUsers;
    }

    public int getNumPersons() {
        return numPersons;
    }

    public void setNumPeople(int numPersons) {
        this.numPersons = numPersons;
    }

    public int getNumEvents() {
        return numEvents;
    }

    public void setNumEvents(int numEvents) {
        this.numEvents = numEvents;
    }

    public boolean getSuccess() { return success; }

    public void setSuccess(boolean success) { this.success = success; }
}

package result;

public class FillResult {
    private String message;
    private int numPersons = 0;
    private int numEvents = 0;
    private boolean success = false;

    public FillResult() {
        //message = "Successfully added " + numPeople + " persons and " + numEvents + " to the database.";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getNumPersons() {
        return numPersons;
    }

    public void serNumPersons(int numPeople) {
        this.numPersons = numPeople;
    }

    public int getNumEvents() {
        return numEvents;
    }

    public void setNumEvents(int numEvents) {
        this.numEvents = numEvents;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}

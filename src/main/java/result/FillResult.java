package result;

public class FillResult {
    private String message;
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

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}

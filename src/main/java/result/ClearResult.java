package result;

public class ClearResult {
    private String message = "Clear succeeded";
    private boolean success = false;

    public ClearResult() {
        //Calls all clear functions of the Dao's.
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }


}

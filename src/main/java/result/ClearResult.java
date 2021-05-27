package result;

public class ClearResult {
    private String message = "Clear failed.";
    private boolean success = false;

    public ClearResult() {}

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

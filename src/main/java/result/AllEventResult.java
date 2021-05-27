package result;

import model.Event;

public class AllEventResult {
    private Event[] data;
    private String message;
    private boolean success = false;

    public AllEventResult(Event[] data) {
        this.data = data;
    }

    public AllEventResult() {}

    public Event[] getData() {
        return data;
    }

    public void setData(Event[] data) {
        this.data = data;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }
}

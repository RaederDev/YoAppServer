package technology.raeder.yoappserver.api;

public abstract class SuccessCallback implements Runnable {
    
    private boolean success;
    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public boolean getSuccess() {
        return success;
    }

}

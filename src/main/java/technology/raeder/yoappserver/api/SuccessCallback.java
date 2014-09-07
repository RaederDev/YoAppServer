package technology.raeder.yoappserver.api;

public abstract class SuccessCallback implements Runnable {
    
    private boolean success;
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public boolean getSuccess() {
        return success;
    }

}

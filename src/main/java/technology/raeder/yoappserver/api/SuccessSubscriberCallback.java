package technology.raeder.yoappserver.api;

public abstract class SuccessSubscriberCallback extends SuccessCallback {
    
    private long subscribers = -1;

    public long getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(long subscribers) {
        this.subscribers = subscribers;
    }
    

}

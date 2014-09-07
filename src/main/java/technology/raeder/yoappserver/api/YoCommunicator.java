package technology.raeder.yoappserver.api;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import technology.raeder.yoappserver.api.exceptions.NoApiKeyException;

public class YoCommunicator {

    private final YoApp app;

    /**
     * The YoCommunicator offers functionality to communicate with the Yo API.
     * @param app The YoApp requesting stuff from the API.
     */
    public YoCommunicator(YoApp app) {
        this.app = app;
    }

    /**
     * Send a yo to a username
     * @param username The username that should receive the yo.
     * @param url The url that should be attached to the yo.
     * @throws NoApiKeyException 
     */
    public void sendYo(final String username, final String url) throws NoApiKeyException {
        if(app.getApiKey() == null) {
            throw new NoApiKeyException();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (url != null) {
                        Jsoup.connect("https://api.justyo.co/yo/").data("api_token", app.getApiKey(), "username", username, "link", url).get();
                    } else {
                        Jsoup.connect("https://api.justyo.co/yo/").data("api_token", app.getApiKey(), "username", username).get();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(YoCommunicator.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
    }

    /**
     * Send a yo to all subscribers.
     * @param url The url that should be attached to the yo.
     * @throws NoApiKeyException 
     */
    public void sendYoToAll(final String url) throws NoApiKeyException {
        if(app.getApiKey() == null) {
            throw new NoApiKeyException();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (url != null) {
                        Jsoup.connect("https://api.justyo.co/yoall/").data("api_token", app.getApiKey(), "link", url).get();
                    } else {
                        Jsoup.connect("https://api.justyo.co/yoall/").data("api_token", app.getApiKey()).get();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(YoCommunicator.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
    }

    /**
     * Send a yo to a username
     * @param username The username that should receive the yo.
     * @throws NoApiKeyException 
     */
    public void sendYo(String username) throws NoApiKeyException {
        this.sendYo(username, null);
    }

    /**
     * Send a yo to all subscribers.
     * @throws NoApiKeyException 
     */
    public void sendYoToAll() throws NoApiKeyException {
        this.sendYoToAll(null);
    }

    public long getSubscriberCount() throws NoApiKeyException {
        if(app.getApiKey() == null) {
            throw new NoApiKeyException();
        }
        return 0;
    }

}

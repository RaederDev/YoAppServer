package technology.raeder.yoappserver.api;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;

public class YoCommunicator {

    private final YoApp app;

    /**
     * The YoCommunicator offers functionality to communicate with the Yo API.
     * @param app The YoApp requesting stuff from the API.
     */
    public YoCommunicator(YoApp app) {
        this.app = app;
    }

    public void sendYo(final String username, final String url) {
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

    public void sendYoToAll(final String url) {
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

    public void sendYo(String username) {
        this.sendYo(username, null);
    }

    public void sendYoToAll() {
        this.sendYoToAll(null);
    }

    public long getSubscriberCount() {
        return 0;
    }

}

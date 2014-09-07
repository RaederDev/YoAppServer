package technology.raeder.yoappserver;

import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import technology.raeder.yoappserver.loader.AppLoader;
import technology.raeder.yoappserver.webserver.WebServer;

public class Server {
    
    public static void main(String[] args) {
        try {
            final AppLoader appLoader = new AppLoader("apps");
            appLoader.loadApps();
            appLoader.enableAllApps();
            new WebServer(appLoader.getApps()).start();
        } catch (URISyntaxException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

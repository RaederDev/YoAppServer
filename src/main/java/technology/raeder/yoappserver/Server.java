package technology.raeder.yoappserver;

import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import technology.raeder.yoappserver.loader.AppLoader;
import technology.raeder.yoappserver.webserver.WebServer;

public class Server {
    
    public static void main(String[] args) {
        try {
            //create or get server configuration
            final ServerConfiguration serverConfig = new ServerConfigurationManager().initializeServer();
            
            //load all apps in the apps folder
            final AppLoader appLoader = new AppLoader("apps");
            appLoader.loadApps();
            
            //enable all loaded apps
            appLoader.enableAllApps();
            
            //add shutdown hook
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    appLoader.disableAllApps();
                }
            });
            
            //start the webserver and listen for yo callbacks
            new WebServer(serverConfig, appLoader.getApps()).start();
        } catch (URISyntaxException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

}

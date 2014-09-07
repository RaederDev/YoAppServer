package technology.raeder.yoappserver.webserver;

import java.util.ArrayList;
import org.eclipse.jetty.server.Server;
import technology.raeder.yoappserver.internal.LoadedYoApp;

public class WebServer {
    
    private final Server server;

    public WebServer(ArrayList<LoadedYoApp> apps) {
        server = new Server(4242);
        server.setHandler(new RequestHandler(apps));
    }

    public void start() throws Exception {
        server.start();
        server.join();
    }

}

package technology.raeder.yoappserver.internal;

import java.util.ArrayList;

public class AsyncYoInformer extends Thread {

    private final ArrayList<LoadedYoApp> apps;
    private final String request;
    private final String username;
    private final String userSuppliedUrl;
    
    public AsyncYoInformer(ArrayList<LoadedYoApp> apps, String request, String username, String userSuppliedUrl) {
        this.apps = apps;
        this.request = request;
        this.username = username;
        this.userSuppliedUrl = userSuppliedUrl;
    }
    
    @Override
    public void run() {
        //inform all apps about the incoming yo
        for(LoadedYoApp app : apps) {
            if(app.getConfig().getUrl().equalsIgnoreCase(request) || app.getConfig().getUrl().equalsIgnoreCase("/")) {
                app.getApp().onMessage(username, request, userSuppliedUrl);
            }
        }
    }

}

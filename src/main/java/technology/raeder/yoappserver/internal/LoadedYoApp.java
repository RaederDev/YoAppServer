package technology.raeder.yoappserver.internal;

import technology.raeder.yoappserver.api.YoApp;
import technology.raeder.yoappserver.loader.AppConfiguration;

public class LoadedYoApp {
    
    private final YoApp app;
    private final AppConfiguration config;

    /**
     * A loaded YoApp contains the app instance and the configuration that is linked to the app.
     * @param app The app instance.
     * @param config The configuration of the app.
     */
    public LoadedYoApp(YoApp app, AppConfiguration config) {
        this.app = app;
        this.config = config;
    }
    
    public YoApp getApp() {
        return app;
    }

    public AppConfiguration getConfig() {
        return config;
    }

}

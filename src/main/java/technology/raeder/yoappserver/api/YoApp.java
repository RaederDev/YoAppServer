package technology.raeder.yoappserver.api;

/**
 * Classes that extend YoApp will be automatically loaded by
 * the app server and notified when a new YO is received.
 */
public abstract class YoApp {
    
    private String apiKey, appPath;
    
    /**
     * Called when the app starts
     */
    public void onEnable() {}
    
    /**
     * Called when the app stops
     */
    public void onDisable() {};
    
    /**
     * This method gets called when a new YO is received.
     * @param username The username that sent the YO.
     * @param queryUrl The url that was called by the YO server.
     * @param userSuppliedUrl If the user sent a url with his YO this parameter contains it otherwise the parameter is null.
     */
    public void onMessage(String username, String queryUrl, String userSuppliedUrl) {};
    
    /**
     * This method is invoked by the server before onEnable to set the API key.
     * Override this method if you need to set your API key and don't want to use the configuration file.
     * @param key The api key configured in the configuration or null if none was found.
     */
    public void setApiKey(String key) {
        this.apiKey = key;
    }
    
    /**
     * Get the API key.
     * @return The Api key.
     */
    public String getApiKey() {
        return this.apiKey;
    }
    
    /**
     * Get the absolute path to the folder where the app is.
     * @return The absolute path to the folder where the app is.
     */
    public String getAppPath() {
        return appPath;
    }
    
    /**
     * Set the app path.
    */
    public void setAppPath(String path) {
        this.appPath = path;
    }

}

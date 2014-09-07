package technology.raeder.yoappserver.loader;

public class AppConfiguration {
    
    private String apiKey, main, name, url;
    
    /**
     * The AppConfiguration is used to store information about the YoApp.
     */
    public AppConfiguration() {
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getMain() {
        return main;
    }

    public String getName() {
        return name;
    }
    
    public String getUrl() {
        return url;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    /**
     * Checks if the configuration is valid
     * @return If the configuration is valid or not.
     */
    public boolean isValid() {
        return (main != null && name != null && url != null);
    }

    @Override
    public String toString() {
        return "AppConfiguration{" + "apiKey=" + apiKey + ", main=" + main + ", name=" + name + '}';
    }

}

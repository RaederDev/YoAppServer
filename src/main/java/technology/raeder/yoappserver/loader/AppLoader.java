package technology.raeder.yoappserver.loader;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xeustechnologies.jcl.JarClassLoader;
import org.xeustechnologies.jcl.JclObjectFactory;
import technology.raeder.yoappserver.api.YoApp;
import technology.raeder.yoappserver.internal.LoadedYoApp;

public class AppLoader {
    
    private final File appFolder;
    private final String pathToAppFolder;
    private final Log log = LogFactory.getLog(AppLoader.class);
    private final ArrayList<LoadedYoApp> apps = new ArrayList<>();
    
    /**
     * The AppLoader class is used to load apps from the specified folder.
     * @param appFolder The folder that contains the apps relative to the server location.
     * @throws URISyntaxException 
     */
    public AppLoader(String appFolder) throws URISyntaxException {
        //get the server location
        final CodeSource codeSource = AppLoader.class.getProtectionDomain().getCodeSource();
        final File jarFile = new File(codeSource.getLocation().toURI().getPath());
        final String jarDir = jarFile.getParentFile().getPath();
        
        //create apps folder
        this.pathToAppFolder = jarDir + File.separator + appFolder + File.separator;
        this.appFolder = new File(this.pathToAppFolder);
        if(!this.appFolder.exists()) {
            this.appFolder.mkdir();
            log.info("Folder " + appFolder + " created.");
        }
    }
    
    /**
     * Load all apps.
     */
    public void loadApps() {
        //reset
        apps.clear();
        
        //get all jar files in the apps directory
        final String[] files = appFolder.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".jar");
            }
        });
        
        //no files found
        if(files.length < 1) {
            log.info("No apps loaded.");
            return;
        }
        
        final JarClassLoader jcl = new JarClassLoader();
        final JclObjectFactory factory = JclObjectFactory.getInstance();
        for(String jarfile: files) {
            try {
                final File jar = new File(appFolder + File.separator + jarfile);
                final URL jarUrl = jar.toURI().toURL();
                log.info("Loading " + jarUrl);
                //get configuration for jar and ignore the file if no config was found
                final AppConfiguration config = new ConfigurationExtractor(jar).getConfig();
                if(config != null && config.isValid()) {
                    jcl.add(jarUrl);
                    Object yoApp = factory.create(jcl, config.getMain());
                    if(yoApp instanceof YoApp) {
                        apps.add(new LoadedYoApp((YoApp) yoApp, config));
                    }
                } else {
                    log.warn("Could not load " + jarUrl + " invalid or missing configuration.");
                }
            } catch (MalformedURLException ex) {
                Logger.getLogger(AppLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Enable all loaded apps.
     */
    public void enableAllApps() {
        for(LoadedYoApp app: apps) {
            app.getApp().setApiKey(app.getConfig().getApiKey());
            app.getApp().onEnable();
        }
    }
    
    /**
     * Disable all loaded apps.
     */
    public void disableAllApps() {
        for(LoadedYoApp app: apps) {
            app.getApp().onDisable();
        }
        apps.clear();
    }
    
    /**
     * Get all loaded apps.
     * @return All loaded apps.
     */
    public ArrayList<LoadedYoApp> getApps() {
        return apps;
    }
    
}

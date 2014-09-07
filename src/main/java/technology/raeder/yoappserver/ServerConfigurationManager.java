package technology.raeder.yoappserver;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import technology.raeder.yoappserver.loader.AppLoader;

public class ServerConfigurationManager {

    /**
     * Initializes the ServerConfiguration.
     * @return The ServerConfiguration.
     */
    public ServerConfiguration initializeServer() {
        try {
            //get jar directory
            final CodeSource codeSource = AppLoader.class.getProtectionDomain().getCodeSource();
            final File jarFile = new File(codeSource.getLocation().toURI().getPath());
            final String jarDir = jarFile.getParentFile().getPath();
            final File file = new File(jarDir + File.separator + "config.json");
            
            //get logger
            final Log log = LogFactory.getLog(Server.class);
            
            //when no config file is found create one
            if(!file.exists()) {
                final JsonWriter writer = new JsonWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
                writer.beginObject();
                writer.setIndent("    ");
                writer.name("port").value(4242);
                writer.endObject();
                writer.flush();
                log.info("Configuration file created. Shutting down Server.");
                System.exit(0);
            } else {
                //config file found, use gson to read the configuration
                final JsonReader reader = new JsonReader(new InputStreamReader(new FileInputStream(file)));
                final ServerConfiguration config = new ServerConfiguration();
                reader.beginObject();
                while(reader.hasNext()) {
                    final String name = reader.nextName();
                    if(name.equals("port")) {
                        config.setPort(reader.nextInt());
                    }
                }
                reader.endObject();
                reader.close();
                if(config.getPort() != -1) {
                    return config;
                } else {
                    log.warn("Invalid configuration. Shutting down Server.");
                    System.exit(0);
                }
            }
            
        } catch (URISyntaxException | UnsupportedEncodingException | FileNotFoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
        return null;
    }
    
}

package technology.raeder.yoappserver.loader;

import com.google.gson.stream.JsonReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;

public class ConfigurationExtractor {

    private final File jarfile;

    public ConfigurationExtractor(File jarfile) {
        this.jarfile = jarfile;
    }

    /**
     * Returns the configuration contained in the jar file or null if no app.json
     * was found in the file.
     *
     * @return The configuration for the jar file or null if none is found.
     */
    public AppConfiguration getConfig() {
        try {
            final ZipFile zip = new ZipFile(jarfile);
            final ZipArchiveEntry entry = zip.getEntry("app.json");
            if (entry != null) {
                final InputStream is = zip.getInputStream(entry);
                final JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));
                final AppConfiguration config = new AppConfiguration();
                reader.beginObject();
                while(reader.hasNext()) {
                    final String name = reader.nextName();
                    switch(name) {
                        case "main":
                            config.setMain(reader.nextString());
                            break;
                        case "name":
                            config.setName(reader.nextString());
                            break;
                        case "apiKey":
                            config.setApiKey(reader.nextString());
                            break;
                        case "url":
                            config.setUrl(reader.nextString());
                            break;
                    }
                }
                reader.endObject();
                reader.close();
                is.close();
                return config;
            }
        } catch (IOException ex) {
            Logger.getLogger(ConfigurationExtractor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}

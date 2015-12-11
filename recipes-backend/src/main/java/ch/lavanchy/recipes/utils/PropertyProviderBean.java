package ch.lavanchy.recipes.utils;

import java.io.IOException;
import java.util.Properties;

/**
 * Implementation of {@link PropertyProviderLocal}
 *
 * @since 1.0.0
 */
public class PropertyProviderBean implements PropertyProviderLocal {
    private final Properties properties = new Properties();

    {
        try {
            properties.load(this.getClass().getClassLoader().getResourceAsStream("config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getStringPropertyByName(String name) {
        return properties.getProperty(name);
    }
}

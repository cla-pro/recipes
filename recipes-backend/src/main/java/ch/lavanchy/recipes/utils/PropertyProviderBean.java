package ch.lavanchy.recipes.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Implementation of {@link PropertyProviderLocal}
 *
 * @since 1.0.0
 */
public class PropertyProviderBean implements PropertyProviderLocal {
    private final Properties properties = new Properties();

    {
        try(final InputStream propertyFile = this.getClass().getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(propertyFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getStringPropertyByName(String name) {
        return properties.getProperty(name);
    }
}

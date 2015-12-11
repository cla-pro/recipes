package ch.lavanchy.recipes.utils;

/**
 * Provide the methods to retrieve the properties
 *
 * @since 1.0.0
 */
public interface PropertyProviderLocal {
    /**
     * Get the string property identified by name
     *
     * @param name Name of the property to retrieve
     * @return The value of the property
     */
    String getStringPropertyByName(final String name);
}

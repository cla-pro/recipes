package ch.lavanchy.recipes.config;

import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

/**
 * Configure the binding for the dependency injection
 *
 * @since 1.0.0
 */
public class ApplicationConfig extends ResourceConfig {
    public ApplicationConfig() {
        register(new ApplicationBinder());
        packages("ch.lavanchy.recipes.services");
    }
}

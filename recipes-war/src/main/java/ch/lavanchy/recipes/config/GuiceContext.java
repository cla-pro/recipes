package ch.lavanchy.recipes.config;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.GuiceServletContextListener;

/**
 * Guice context
 *
 * @since 1.0.0
 */
public class GuiceContext extends GuiceServletContextListener {
    private static final String RECIPES_PERSISTENCE_UNIT = "recipes-pu";

    public GuiceContext() {}

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new JpaPersistModule(RECIPES_PERSISTENCE_UNIT), new RecipesJerseyServletModule());
    }
}

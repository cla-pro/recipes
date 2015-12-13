package ch.lavanchy.recipes.config;

import com.google.inject.Injector;
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
        // new JpaPersistModule(RECIPES_PERSISTENCE_UNIT),

        return com.google.inject.Guice.createInjector(new RecipesJerseyServletModule());
    }
}

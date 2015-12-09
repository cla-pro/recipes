package ch.lavanchy.recipes.config;

import ch.lavanchy.recipes.business.RecipesBusinessBean;
import ch.lavanchy.recipes.business.RecipesBusinessLocal;
import ch.lavanchy.recipes.dao.RecipesDaoBean;
import ch.lavanchy.recipes.dao.RecipesDaoLocal;
import ch.lavanchy.recipes.services.RecipesService;
import com.google.inject.persist.PersistFilter;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import java.util.HashMap;
import java.util.Map;

/**
 * Jersey configuration and dependency injection with Guice
 *
 * @since 1.0.0
 */
public class RecipesJerseyServletModule extends JerseyServletModule {
    @Override
    protected void configureServlets() {
        bind(RecipesDaoLocal.class).to(RecipesDaoBean.class);

        bind(RecipesBusinessLocal.class).to(RecipesBusinessBean.class);

        bind(RecipesService.class);

        // Route all requests through GuiceContainer
        final Map<String, String> params = new HashMap<>();
        params.put("com.sun.jersey.api.json.POJOMappingFeature", "true");
        //filter("/services/*").through(PersistFilter.class);
        serve("/services/*").with(GuiceContainer.class, params);
    }
}

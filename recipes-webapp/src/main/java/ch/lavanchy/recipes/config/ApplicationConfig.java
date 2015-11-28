package ch.lavanchy.recipes.config;

import ch.lavanchy.recipes.business.RecipesBusinessBean;
import ch.lavanchy.recipes.business.RecipesBusinessLocal;
import ch.lavanchy.recipes.dao.RecipesDaoBean;
import ch.lavanchy.recipes.dao.RecipesDaoLocal;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

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

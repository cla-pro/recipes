package ch.lavanchy.recipes.config;

import ch.lavanchy.recipes.business.RecipesBusinessBean;
import ch.lavanchy.recipes.business.RecipesBusinessLocal;
import ch.lavanchy.recipes.dao.RecipesDaoBean;
import ch.lavanchy.recipes.dao.RecipesDaoLocal;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

/**
 * Configure the binding used for the dependency injection
 *
 * @since 1.0.0
 */
public class ApplicationBinder extends AbstractBinder {
    @Override
    protected void configure() {
        bind(RecipesBusinessBean.class).to(RecipesBusinessLocal.class);
        bind(RecipesDaoBean.class).to(RecipesDaoLocal.class);
    }
}
